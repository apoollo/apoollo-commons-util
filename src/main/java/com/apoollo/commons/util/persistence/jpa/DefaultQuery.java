/**
 * 
 */
package com.apoollo.commons.util.persistence.jpa;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.query.QueryUtils;

import com.apoollo.commons.util.Assert;
import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.persistence.PageInput;
import com.apoollo.commons.util.persistence.PageOutput;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * @author liuyulong
 * @since 2023年8月17日
 */
public class DefaultQuery implements Query {

    private static final Sort SORT = Sort.by(Direction.DESC, "id");

    private EntityManager entityManager;

    /**
     * @param entityManager
     */
    public DefaultQuery(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    @Override
    public <E, P, C extends PageInput, T> PageOutput<T> page(Class<E> entityClass, Class<P> projectionClass,
            Function<SpecificationBuilders<E>, Predicate> function, C condition, Function<P, T> after) {
        return page(entityClass, projectionClass, function, condition, SORT, after);
    }

    @Override
    public <E, P, C extends PageInput> PageOutput<P> page(Class<E> entityClass, Class<P> projectionClass,
            Function<SpecificationBuilders<E>, Predicate> function, C condition) {
        return page(entityClass, projectionClass, function, condition, SORT);
    }

    @Override
    public <E, P, C extends PageInput> PageOutput<P> page(Class<E> entityClass, Class<P> projectionClass,
            Function<SpecificationBuilders<E>, Predicate> function, C condition, Sort sort) {
        return page(entityClass, projectionClass, function, condition, sort, Function.identity());
    }

    @Override
    public <E, P, C extends PageInput, T> PageOutput<T> page(Class<E> entityClass, Class<P> projectionClass,
            Function<SpecificationBuilders<E>, Predicate> function, C condition, Sort sort, Function<P, T> after) {
        Long count = count(entityClass, function, condition);
        List<T> list = null;
        if (count > 0) {
            list = pageList(entityClass, projectionClass, function, condition, sort, after);
        }
        return new PageOutput<>(count, list);
    }

    @Override
    public <E, P, C extends PageInput> List<P> pageList(Class<E> entityClass, Class<P> projectionClass,
            Function<SpecificationBuilders<E>, Predicate> function, C condition) {
        return pageList(entityClass, projectionClass, function, condition, SORT);
    }

    @Override
    public <E, P, C extends PageInput> List<P> pageList(Class<E> entityClass, Class<P> projectionClass,
            Function<SpecificationBuilders<E>, Predicate> function, C condition, Sort sort) {
        return pageList(entityClass, projectionClass, function, condition, SORT, Function.identity());
    }

    @Override
    public <E, P, C extends PageInput, T> List<T> pageList(Class<E> entityClass, Class<P> projectionClass,
            Function<SpecificationBuilders<E>, Predicate> function, C condition, Function<P, T> after) {
        return pageList(entityClass, projectionClass, function, condition, SORT, after);
    }

    @Override
    public <E, P, C extends PageInput, T> List<T> pageList(Class<E> entityClass, Class<P> projectionClass,
            Function<SpecificationBuilders<E>, Predicate> function, C condition, Sort sort, Function<P, T> after) {
        TypedQuery<P> query = getTypedQuery(entityClass, projectionClass, function, condition, sort);

        Integer targetPage = LangUtils.defaultValue(condition.getPage(), 1);
        Assert.isTrue(targetPage > 0, "page must great than 0");

        Integer targetPageSize = LangUtils.defaultValue(condition.getPageSize(), 20);
        Assert.isTrue(targetPageSize > 0 && targetPageSize <= 50,
                "pageSize must great than 0 and less than or equals 50");

        query.setFirstResult((targetPage - 1) * targetPageSize);
        query.setMaxResults(targetPageSize);
        List<P> list = query.getResultList();
        return LangUtils.getStream(list).map(after).collect(Collectors.toList());
    }

    public <E, P, T> List<T> list(Class<E> entityClass, Class<P> projectionClass,
            Function<SpecificationBuilders<E>, Predicate> function, Object condition, Function<P, T> after) {
        return list(entityClass, projectionClass, function, condition, SORT, after);
    }

    public <E, P, T> List<T> list(Class<E> entityClass, Class<P> projectionClass,
            Function<SpecificationBuilders<E>, Predicate> function, Object condition, Sort sort, Function<P, T> after) {
        TypedQuery<P> query = getTypedQuery(entityClass, projectionClass, function, condition, sort);
        List<P> list = query.getResultList();
        return LangUtils.getStream(list).map(after).collect(Collectors.toList());
    }

    public <E, P> TypedQuery<P> getTypedQuery(Class<E> entityClass, Class<P> projectionClass,
            Function<SpecificationBuilders<E>, Predicate> function, Object condition, Sort sort) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<P> criteriaQuery = criteriaBuilder.createQuery(projectionClass);
        Root<E> root = criteriaQuery.from(entityClass);
        SpecificationBuilders<E> specificationQuerys = SpecificationBuilders.of(root, criteriaQuery, criteriaBuilder,
                condition);
        Predicate predicate = function.apply(specificationQuerys);
        criteriaQuery.multiselect(specificationQuerys.getSelections());
        if (null != predicate) {
            criteriaQuery.where(predicate);
        }
        if (null != sort) {
            criteriaQuery.orderBy(QueryUtils.toOrders(sort, root, criteriaBuilder));
        }
        TypedQuery<P> query = entityManager.createQuery(criteriaQuery);
        return query;
    }

    @Override
    public <E> Long count(Class<E> entityClass, Function<SpecificationBuilders<E>, Predicate> function,
            Object condition) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<E> root = criteriaQuery.from(entityClass);
        SpecificationBuilders<E> specificationQuerys = SpecificationBuilders.of(root, criteriaQuery, criteriaBuilder,
                condition);
        Predicate predicate = function.apply(specificationQuerys);
        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(predicate);
        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);
        Long count = query.getSingleResult();
        return LangUtils.defaultValue(count, 0L);
    }

    @Override
    public <E> E one(Class<E> entityClass, Object id) {
        return one(entityClass, id, (t) -> t);
    }

    @Override
    public <E, T> T one(Class<E> entityClass, Object id, Function<E, T> after) {
        E e = entityManager.find(entityClass, id);
        return null == e ? null : after.apply(e);
    }

}

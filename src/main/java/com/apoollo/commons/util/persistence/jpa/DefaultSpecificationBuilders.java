/**
 * 
 */
package com.apoollo.commons.util.persistence.jpa;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

import com.apoollo.commons.util.LangUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

/**
 * @author liuyulong
 * @since 2023年8月17日
 */
public class DefaultSpecificationBuilders<T> implements SpecificationBuilders<T> {

	private DirectFieldAccessFallbackBeanWrapper beanWrapper;
	private CriteriaQuery<?> criteriaQuery;
	private CriteriaBuilder criteriaBuilder;
	private Map<String, SpecificationBuilder<T, ?>> predicateQueryMap;
	private Root<T> root;

	public DefaultSpecificationBuilders(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder,
			Object request) {
		this.root = root;
		this.criteriaQuery = criteriaQuery;
		this.criteriaBuilder = criteriaBuilder;
		if (null != request) {
			beanWrapper = new DirectFieldAccessFallbackBeanWrapper(request);
		}
		DefaultSpecificationBuilders<T> defaultSpecificationQuerys = this;
		predicateQueryMap = new LinkedHashMap<String, SpecificationBuilder<T, ?>>() {
			private static final long serialVersionUID = -2636165955586027796L;
			{
				put(root.getModel().getName(), new DefaultSpecificationBuilder<>(defaultSpecificationQuerys,
						beanWrapper, root, criteriaQuery, criteriaBuilder));
			}
		};
	}

	public SpecificationBuilder<T, ?> join(String attributeName, JoinType joinType) {
		SpecificationBuilder<T, ?> predicateQuery = new DefaultSpecificationBuilder<>(this, beanWrapper,
				root.join(attributeName, joinType), criteriaQuery, criteriaBuilder);
		predicateQueryMap.put(attributeName, predicateQuery);
		return predicateQuery;
	}

	public SpecificationBuilder<T, ?> getRootSpecification() {
		return predicateQueryMap.get(root.getModel().getName());
	}

	public Predicate and() {
		List<Predicate> predicates = LangUtils.getStream(predicateQueryMap.values())
				.flatMap(predicateQuery -> LangUtils.getStream(predicateQuery.getPredicates()))
				.collect(Collectors.toList());
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	public Predicate or() {
		List<Predicate> predicates = LangUtils.getStream(predicateQueryMap.values())
				.flatMap(predicateQuery -> LangUtils.getStream(predicateQuery.getPredicates()))
				.collect(Collectors.toList());
		return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
	}

	public List<Selection<?>> getSelections() {
		List<Selection<?>> selections = LangUtils.getStream(predicateQueryMap.values())
				.flatMap(predicateQuery -> LangUtils.getStream(predicateQuery.getSelections()))
				.collect(Collectors.toList());
		return selections;
	}

	public Root<T> getRoot() {
		return root;
	}

	@Override
	public Predicate none() {
		return null;
	}

}

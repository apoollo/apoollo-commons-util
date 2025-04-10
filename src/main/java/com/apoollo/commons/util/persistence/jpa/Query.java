/**
 * 
 */
package com.apoollo.commons.util.persistence.jpa;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Sort;

import com.apoollo.commons.util.persistence.PageInput;
import com.apoollo.commons.util.persistence.PageOutput;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;

/**
 * @author liuyulong
 * @since 2023年8月17日
 */
public interface Query {

	// page

	public <E, P, C extends PageInput, T> PageOutput<T> page(Class<E> entityClass, Class<P> projectionClass,
			Function<SpecificationBuilders<E>, Predicate> function, C condition, Function<P, T> after);

	public <E, P, C extends PageInput> PageOutput<P> page(Class<E> entityClass, Class<P> projectionClass,
			Function<SpecificationBuilders<E>, Predicate> function, C condition);

	public <E, P, C extends PageInput, T> PageOutput<T> page(Class<E> entityClass, Class<P> projectionClass,
			Function<SpecificationBuilders<E>, Predicate> function, C condition, Sort sort, Function<P, T> after);

	public <E, P, C extends PageInput> PageOutput<P> page(Class<E> entityClass, Class<P> projectionClass,
			Function<SpecificationBuilders<E>, Predicate> function, C condition, Sort sort);

	// page list

	public <E, P, C extends PageInput> List<P> pageList(Class<E> entityClass, Class<P> projectionClass,
			Function<SpecificationBuilders<E>, Predicate> function, C condition, Sort sort);

	public <E, P, C extends PageInput> List<P> pageList(Class<E> entityClass, Class<P> projectionClass,
			Function<SpecificationBuilders<E>, Predicate> function, C condition);

	public <E, P, C extends PageInput, T> List<T> pageList(Class<E> entityClass, Class<P> projectionClass,
			Function<SpecificationBuilders<E>, Predicate> function, C condition, Function<P, T> after);

	public <E, P, C extends PageInput, T> List<T> pageList(Class<E> entityClass, Class<P> projectionClass,
			Function<SpecificationBuilders<E>, Predicate> function, C condition, Sort sort, Function<P, T> after);

	// list
	
	public <E, P, T> List<T> list(Class<E> entityClass, Class<P> projectionClass,
			Function<SpecificationBuilders<E>, Predicate> function, Object condition, Function<P, T> after);
	
	public <E, P, T> List<T> list(Class<E> entityClass, Class<P> projectionClass,
			Function<SpecificationBuilders<E>, Predicate> function, Object condition, Sort sort, Function<P, T> after);
	

	// other

	public <E> Long count(Class<E> entityClass, Function<SpecificationBuilders<E>, Predicate> function, Object condition);

	public <E> E one(Class<E> entityClass, Object id);
	
	public <E, T> T one(Class<E> entityClass, Object id, Function<E, T> after);

	public static Query of(EntityManager entityManager) {
		return new DefaultQuery(entityManager);
	}
}

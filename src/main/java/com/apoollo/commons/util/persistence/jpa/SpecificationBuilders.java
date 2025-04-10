/**
 * 
 */
package com.apoollo.commons.util.persistence.jpa;

import java.util.List;

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
public interface SpecificationBuilders<T> {

	public SpecificationBuilder<T, ?> join(String attributeName, JoinType joinType);

	public SpecificationBuilder<T, ?> getRootSpecification();

	public Predicate and();

	public Predicate or();
	
	public Predicate none();

	public List<Selection<?>> getSelections();

	public static <T> SpecificationBuilders<T> of(Root<T> root, CriteriaQuery<?> criteriaQuery,
			CriteriaBuilder criteriaBuilder, Object request) {
		return new DefaultSpecificationBuilders<T>(root, criteriaQuery, criteriaBuilder, request);
	}

}

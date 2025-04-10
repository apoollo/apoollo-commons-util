/**
 * 
 */
package com.apoollo.commons.util.persistence.jpa;

import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Selection;

/**
 * @author liuyulong
 * @since 2023年8月16日
 */
public interface SpecificationBuilder<Z, X> {

	public List<Selection<?>> getSelections();

	public SpecificationBuilder<Z, X> select(List<NameAlias> nameAliasList);

	public SpecificationBuilder<Z, X> select(String... names);

	public SpecificationBuilder<Z, X> select(NameAlias... nameAliasArray);

	public SpecificationBuilder<Z, X> select(Selection<?> selection);

	public SpecificationBuilder<Z, X> select();

	public SpecificationBuilder<Z, X> count(String entityPropertyName, String alias);

	public SpecificationBuilder<Z, X> like(String propertyName);

	public SpecificationBuilder<Z, X> like(String propertyName, boolean start, boolean end);

	public SpecificationBuilder<Z, X> like(String propertyName, String entityPropertyName, boolean start, boolean end);

	public SpecificationBuilder<Z, X> equal(String propertyName);

	public SpecificationBuilder<Z, X> equal(String propertyName, String entityPropertyName);

	public SpecificationBuilder<Z, X> equalValue(String entityPropertyName, Object value);

	public SpecificationBuilder<Z, X> notEqual(String propertyName);

	public SpecificationBuilder<Z, X> notEqual(String propertyName, String entityPropertyName);

	public <T> SpecificationBuilder<Z, X> in(String propertyName);

	public <T> SpecificationBuilder<Z, X> in(String propertyName, String entityPropertyName);

	public <Y extends Comparable<? super Y>> SpecificationBuilder<Z, X> range(String requestPropertyName);

	public <Y extends Comparable<? super Y>> SpecificationBuilder<Z, X> range(String requestPropertyName,
			String entityPropertyName);

	public SpecificationBuilder<Z, X> groupBy(String... entityPropertyNames);

	public SpecificationBuilder<Z, X> groupBy(Expression<?>... grouping);

	public SpecificationBuilder<Z, X> groupBy(List<Expression<?>> expressions);

	public SpecificationBuilder<Z, X> groupBy(List<String> entityPropertyNames, List<Expression<?>> expressions);

	public SpecificationBuilder<Z, X> groupByEnd();

	public Expression<?> entityPropertiesExpression(String entityName);

	public Expression<String> dateFormatExpression(String entityPropertyName, String format);

	public static <Z, X> SpecificationBuilder<Z, X> of(From<Z, X> from, CriteriaQuery<?> criteriaQuery,
			CriteriaBuilder criteriaBuilder, Object request) {
		return new DefaultSpecificationBuilder<Z, X>(request, from, criteriaQuery, criteriaBuilder);
	}

	public Predicate none();

	public Predicate and();

	public Predicate or();

	public List<Predicate> getPredicates();

	public DefaultSpecificationBuilders<Z> end();
}

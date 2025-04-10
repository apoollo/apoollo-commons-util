/**
 * 
 */
package com.apoollo.commons.util.persistence.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.model.MinMax;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Selection;

/**
 * @author liuyulong
 * @since 2023年8月17日
 */
public class DefaultSpecificationBuilder<Z, X> implements SpecificationBuilder<Z, X> {

	private From<Z, X> from;
	private CriteriaQuery<?> criteriaQuery;
	private CriteriaBuilder criteriaBuilder;
	private DirectFieldAccessFallbackBeanWrapper beanWrapper;
	private List<Selection<?>> selections;
	private List<Predicate> predicates;
	private List<Expression<?>> groupByExpressiones;
	private DefaultSpecificationBuilders<Z> specificationQuerys;

	public DefaultSpecificationBuilder(Object request, From<Z, X> from, CriteriaQuery<?> criteriaQuery,
			CriteriaBuilder criteriaBuilder) {

		this(null, null == request ? null : new DirectFieldAccessFallbackBeanWrapper(request), from, criteriaQuery,
				criteriaBuilder);
	}

	public DefaultSpecificationBuilder(DefaultSpecificationBuilders<Z> specificationQuerys,
			DirectFieldAccessFallbackBeanWrapper beanWrapper, From<Z, X> from, CriteriaQuery<?> criteriaQuery,
			CriteriaBuilder criteriaBuilder) {
		super();
		this.specificationQuerys = specificationQuerys;
		this.from = from;
		this.criteriaQuery = criteriaQuery;
		this.criteriaBuilder = criteriaBuilder;
		this.beanWrapper = beanWrapper;
		predicates = new ArrayList<>();
	}

	@Override
	public SpecificationBuilder<Z, X> select(String... names) {
		return select(LangUtils.getStream(names).map(name -> new NameAlias(name, name)).collect(Collectors.toList()));
	}

	@Override
	public SpecificationBuilder<Z, X> select(NameAlias... nameAliasArray) {
		return select(LangUtils.getStream(nameAliasArray).collect(Collectors.toList()));
	}

	@Override
	public SpecificationBuilder<Z, X> select(List<NameAlias> nameAliasList) {
		Stream<Selection<Object>> stream = LangUtils.getStream(nameAliasList)
				.map(nameAlias -> from.get(nameAlias.getName()).alias(nameAlias.getAlias())).distinct();
		return addSelectionsOfObject(stream);
	}

	@Override
	public SpecificationBuilder<Z, X> select(Selection<?> selection) {
		return addSelections(LangUtils.getStream(selection));
	}

	@Override
	public SpecificationBuilder<Z, X> select() {
		return addSelections(LangUtils.getStream(from));
	}

	@Override
	public SpecificationBuilder<Z, X> count(String entityPropertyName, String alias) {
		return addSelections(LangUtils.getStream(criteriaBuilder.count(from.get(entityPropertyName)).alias(alias)));
	}

	@Override
	public SpecificationBuilder<Z, X> like(String propertyName) {
		return like(propertyName, propertyName, true, true);
	}

	@Override
	public SpecificationBuilder<Z, X> like(String propertyName, boolean start, boolean end) {
		return like(propertyName, propertyName, start, end);
	}

	@Override
	public SpecificationBuilder<Z, X> like(String propertyName, String entityPropertyName, boolean start, boolean end) {
		return applyPredicate(propertyName, (value) -> {
			String target = LangUtils.escape(value.toString(), "\\", "_", "%");
			if (start) {
				target = "%".concat(target);
			}
			if (end) {
				target = target.concat("%");
			}
			return criteriaBuilder.like(from.get(entityPropertyName), target);
		});
	}

	@Override
	public SpecificationBuilder<Z, X> equal(String propertyName) {
		return equal(propertyName, propertyName);
	}

	@Override
	public SpecificationBuilder<Z, X> equal(String propertyName, String entityPropertyName) {
		return applyPredicate(propertyName, (value) -> {
			return criteriaBuilder.equal(from.get(entityPropertyName), getValue(value));
		});
	}

	@Override
	public SpecificationBuilder<Z, X> equalValue(String entityPropertyName, Object value) {
		return addPredicate((object) -> {
			return criteriaBuilder.equal(from.get(entityPropertyName), getValue(object));
		}, value);
	}

	@Override
	public SpecificationBuilder<Z, X> notEqual(String propertyName) {
		return notEqual(propertyName, propertyName);
	}

	@Override
	public SpecificationBuilder<Z, X> notEqual(String propertyName, String entityPropertyName) {
		return applyPredicate(propertyName, (value) -> {
			return criteriaBuilder.notEqual(from.get(entityPropertyName), getValue(value));
		});
	}

	@Override
	public <T> SpecificationBuilder<Z, X> in(String propertyName) {
		return in(propertyName, propertyName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> SpecificationBuilder<Z, X> in(String propertyName, String entityPropertyName) {
		return applyPredicate(propertyName, (value) -> {
			if (value instanceof Collection) {
				Collection<T> collection = (Collection<T>) value;
				if (!collection.isEmpty()) {
					In<T> in = criteriaBuilder.in(from.get(entityPropertyName));
					LangUtils.getStream(collection).forEach(elem -> {
						in.value(getValue(elem));
					});
					return in;
				}
			}
			return null;
		});
	}

	@Override
	public <Y extends Comparable<? super Y>> SpecificationBuilder<Z, X> range(String requestPropertyName) {
		return range(requestPropertyName, requestPropertyName);
	}

	@Override
	public <Y extends Comparable<? super Y>> SpecificationBuilder<Z, X> range(String requestPropertyName,
			String entityPropertyName) {
		return applyPredicate(requestPropertyName, (value) -> {
			if (value instanceof MinMax) {
				@SuppressWarnings("unchecked")
				MinMax<Y> minMax = (MinMax<Y>) value;
				Predicate predicate = null;
				if (null != minMax.getMin() && null != minMax.getMax()) {
					predicate = criteriaBuilder.between(from.get(entityPropertyName), getValue(minMax.getMin()),
							getValue(minMax.getMax()));
				} else if (null != minMax.getMin()) {
					predicate = criteriaBuilder.greaterThanOrEqualTo(from.get(entityPropertyName),
							getValue(minMax.getMin()));
				} else if (null != minMax.getMax()) {
					predicate = criteriaBuilder.lessThanOrEqualTo(from.get(entityPropertyName),
							getValue((minMax.getMax())));
				}
				return predicate;
			} else {
				throw new RuntimeException("unknow between object");
			}
		});

	}

	@Override
	public SpecificationBuilder<Z, X> groupBy(String... entityPropertyNames) {
		List<Expression<?>> expressions = LangUtils.getStream(entityPropertyNames).map(from::get)
				.collect(Collectors.toList());
		return groupBy(expressions);
	}

	@Override
	public SpecificationBuilder<Z, X> groupBy(Expression<?>... grouping) {
		return groupBy(LangUtils.getStream(grouping).collect(Collectors.toList()));
	}

	@Override
	public SpecificationBuilder<Z, X> groupBy(List<Expression<?>> expressions) {
		if (null == groupByExpressiones) {
			groupByExpressiones = new ArrayList<>();
		}
		LangUtils.getStream(expressions).forEach(groupByExpressiones::add);
		return this;
	}

	@Override
	public SpecificationBuilder<Z, X> groupBy(List<String> entityPropertyNames, List<Expression<?>> expressions) {
		List<Expression<?>> entityPropertyNameExpressions = LangUtils.getStream(entityPropertyNames).map(from::get)
				.collect(Collectors.toList());
		List<Expression<?>> merged = LangUtils.merge(entityPropertyNameExpressions, expressions);
		return groupBy(merged);
	}

	@Override
	public SpecificationBuilder<Z, X> groupByEnd() {
		if (null != groupByExpressiones && !groupByExpressiones.isEmpty()) {
			criteriaQuery.groupBy(groupByExpressiones);
		}
		return this;
	}

	public Expression<?> entityPropertiesExpression(String entityName) {
		return from.get(entityName);
	}

	public Expression<String> dateFormatExpression(String entityPropertyName, String format) {
		return criteriaBuilder.function("DATE_FORMAT", String.class, from.get(entityPropertyName), getValue(format));
	}

	@Override
	public Predicate and() {
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	@Override
	public Predicate or() {
		return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
	}

	public Predicate none() {
		return null;
	}

	@Override
	public DefaultSpecificationBuilders<Z> end() {
		return specificationQuerys;
	}

	/**
	 * @return the root
	 */
	public From<Z, X> getFrom() {
		return from;
	}

	/**
	 * @return the criteriaQuery
	 */
	public CriteriaQuery<?> getCriteriaQuery() {
		return criteriaQuery;
	}

	/**
	 * @return the criteriaBuilder
	 */
	public CriteriaBuilder getCriteriaBuilder() {
		return criteriaBuilder;
	}

	/**
	 * @return the predicates
	 */
	public List<Predicate> getPredicates() {
		return predicates;
	}

	/**
	 * @return the selections
	 */
	public List<Selection<?>> getSelections() {
		return selections;
	}

	/**
	 * @param selections the selections to set
	 */
	public void setSelections(List<Selection<?>> selections) {
		this.selections = selections;
	}

	private SpecificationBuilder<Z, X> addSelectionsOfObject(Stream<Selection<Object>> stream) {
		if (null == selections) {
			selections = stream.collect(Collectors.toList());
		} else {
			stream.forEach(selections::add);
		}
		return this;
	}

	private SpecificationBuilder<Z, X> addSelections(Stream<Selection<?>> stream) {
		if (null == selections) {
			selections = stream.collect(Collectors.toList());
		} else {
			stream.forEach(selections::add);
		}
		return this;
	}

	private <T> Expression<T> getValue(T value) {
		return criteriaBuilder.literal(value);
	}

	private SpecificationBuilder<Z, X> applyPredicate(String requestPropertyName,
			Function<Object, Predicate> function) {
		if (null != beanWrapper) {
			addPredicate(function, beanWrapper.getPropertyValue(requestPropertyName));
		}
		return this;
	}

	private SpecificationBuilder<Z, X> addPredicate(Function<Object, Predicate> function, Object object) {
		if (null != object) {
			if (!(object instanceof String && StringUtils.isBlank(object.toString()))) {
				addPredicate(function.apply(object));
			}
		}
		return this;
	}

	private void addPredicate(Predicate predicate) {
		if (null != predicate) {
			predicates.add(predicate);
		}
	}

}

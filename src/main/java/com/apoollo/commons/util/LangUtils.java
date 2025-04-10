/**
 * 
 */
package com.apoollo.commons.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.apoollo.commons.util.model.MinMax;
import com.apoollo.commons.util.model.OneTwo;
import com.apoollo.commons.util.model.Processor;
import com.apoollo.commons.util.model.StorageUnit;

/**
 * @author liuyulong
 * @since 2023年7月17日
 */
public class LangUtils {

	public static <T> T ifElseSupplier(boolean expression, Supplier<T> ifSupplier, Supplier<T> elseSupplier) {
		return ifElseSupplier(() -> expression, ifSupplier, elseSupplier);
	}

	public static <T> T ifElseSupplier(Supplier<Boolean> expression, Supplier<T> ifSupplier, Supplier<T> elseSupplier) {
		T t = null;
		if (BooleanUtils.isTrue(expression.get())) {
			t = ifSupplier.get();
		} else {
			t = elseSupplier.get();
		}
		return t;
	}

	public static <O, V> V getPropertyIfPredicate(Supplier<O> instanceSupplier, Function<O, V> valueSupplier,
			Predicate<V> valuePredicate, V defaultValue) {
		O object = instanceSupplier.get();
		V value = null;
		if (null == object) {
			value = defaultValue;
		} else {
			V objectValue = valueSupplier.apply(object);
			if (valuePredicate.test(objectValue)) {
				value = objectValue;
			} else {
				value = defaultValue;
			}
		}
		return value;
	}

	public static <O, V> V getPropertyIfObjectPredicate(O object, Function<O, V> valueSupplier,
			Predicate<V> valuePredicate, V defaultValue) {
		return getPropertyIfPredicate(() -> object, valueSupplier, valuePredicate, defaultValue);
	}

	public static <O, V> V getPropertyIfNotNull(O object, Function<O, V> valueSupplier, V defaultValue) {
		return getPropertyIfObjectPredicate(object, valueSupplier, property -> null != property, defaultValue);
	}

	public static <O, V> V getPropertyIfNotNull(O object, Function<O, V> valueSupplier) {
		return getPropertyIfNotNull(object, valueSupplier, null);
	}

	public static <T> T getIfPredicate(Supplier<T> supplier, Predicate<T> predicate, Function<T, T> transfer) {
		T value = supplier.get();
		if (predicate.test(value)) {
			value = transfer.apply(value);
		}
		return value;
	}

	public static <T> T getIfPredicate(Supplier<T> supplier, Predicate<T> predicate, Consumer<T> consume) {
		return getIfPredicate(supplier, predicate, (value) -> {
			consume.accept(value);
			return value;
		});
	}

	public static <T> void ifNull(Supplier<T> supplier, Processor processor) {
		getIfPredicate(supplier, (value) -> null == value, (value) -> {
			processor.process();
		});
	}

	public static <T> void ifNotNull(Supplier<T> supplier, Consumer<T> consume) {
		getIfPredicate(supplier, (value) -> null != value, (value) -> {
			consume.accept(value);
		});
	}

	public static <T> T getRequiredArgument(Supplier<T> supplier, String message) {
		return getIfPredicate(supplier, value -> null == value, (value) -> {
			Assert.isTrue(false, message);
		});
	}

	public static String getUppercaseUUID() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	public static <K, V> Stream<Map.Entry<K, V>> getStream(Map<K, V> map) {
		return null == map ? Stream.empty() : map.entrySet().stream();
	}

	@SafeVarargs
	public static <T> Stream<T> getStream(T... values) {
		return null == values ? Stream.empty() : Stream.of(values).filter(Objects::nonNull);
	}

	@SafeVarargs
	public static <T> List<T> toList(T... object) {
		return getStream(object).collect(Collectors.toList());
	}

	public static List<String> toCleanList(String[] array) {
		return LangUtils.getStream(array).map(StringUtils::trim).filter(StringUtils::isNotBlank)
				.collect(Collectors.toList());
	}

	public static <T> Stream<T> getStream(Collection<T> collection) {
		return null == collection ? Stream.empty() : collection.stream().filter(Objects::nonNull);
	}

	public static <T> T defaultValue(T value, T defaultValue) {
		return null == value ? defaultValue : value;
	}

	public static <T> T defaultValue(T value, Supplier<T> defaultSupplier) {
		return null == value ? defaultSupplier.get() : value;
	}

	public static String defaultString(String value, Supplier<String> defaultValue) {
		return StringUtils.isBlank(value) ? defaultValue.get() : value;
	}

	public static String defaultString(String value, String defaultValue) {
		return defaultString(value, () -> defaultValue);
	}

	public static long defaultLong(Long value) {
		return defaultValue(value, 0L);
	}

	public static boolean booleanIsTrue(Object object) {
		boolean result = false;
		if (object instanceof Boolean) {
			result = (Boolean) object;
		} else if (object instanceof String) {
			result = "true".equals(object);
		} else if (object instanceof Number) {
			result = ((Number) object).doubleValue() > 0;
		}
		return result;
	}

	@SafeVarargs
	public static <T> List<T> merge(List<T>... lists) {
		List<T> list = new ArrayList<>();
		LangUtils.getStream(lists).forEach(element -> {
			CollectionUtils.addAll(list, element);
		});
		return list;
	}

	public static <E> E getFirst(final Iterable<E> iterable) {
		return get(iterable, 0);
	}

	public static <E> E get(final Iterable<E> iterable, final int index) {
		int i = index;
		E result = null;
		if (null != iterable && index >= 0) {
			Iterator<E> iterator = iterable.iterator();
			while (iterator.hasNext()) {
				E e = iterator.next();
				if (--i == -1) {
					result = e;
					break;
				}
			}
		}
		return result;
	}

	public static <T extends Number & Comparable<? super T>> T getInRange(MinMax<T> range, T value) {
		T result = null;
		if (null != value) {
			double doubleValue = value.doubleValue();
			if (doubleValue < range.getMin().doubleValue()) {
				result = range.getMin();
			} else if (doubleValue > range.getMax().doubleValue()) {
				result = range.getMax();
			} else {
				result = value;
			}
		}
		return result;
	}

	public static <T extends Number & Comparable<? super T>> boolean inRange(MinMax<T> range, T value) {
		return null != range && null != value && null != range.getMin() && null != range.getMax()
				&& range.getMin().doubleValue() <= value.doubleValue()
				&& range.getMax().doubleValue() >= value.doubleValue();
	}

	public static <T extends Number> boolean inPositiveAndNegativeRange(T range, T value) {
		MinMax<Double> minMax = null;
		if (range.doubleValue() > 0) {
			minMax = new MinMax<>(-range.doubleValue(), range.doubleValue());
		} else {
			minMax = new MinMax<>(range.doubleValue(), -range.doubleValue());
		}
		return inRange(minMax, value.doubleValue());
	}

	public static Double getStorageSize(StorageUnit storageUnit, Number byteLength) {
		return byteLength.doubleValue() / storageUnit.getByteCount();
	}

	public static <T, U> U shallowCopy(T origin, U target, BiConsumer<T, U> after) {
		if (null != origin) {
			org.springframework.beans.BeanUtils.copyProperties(origin, target);
		}
		if (null != after) {
			after.accept(origin, target);
		}
		return target;
	}

	public static <T, U> U shallowCopy(T origin, U target) {
		return shallowCopy(origin, target, null);
	}

	public static <T, U> List<U> shallowCopys(List<T> origins, Supplier<U> newInstance, BiConsumer<T, U> after) {
		return LangUtils.getStream(origins).map(origin -> shallowCopy(origin, newInstance.get(), after))
				.collect(Collectors.toList());
	}

	public static <T, U> List<U> shallowCopys(List<T> origins, Supplier<U> newInstance) {
		return shallowCopys(origins, newInstance, null);
	}

	public static String escape(String value, String escape, String... targetCharaters) {
		return null == value ? null
				: getStream(targetCharaters).reduce(value.toString(),
						(it, character) -> it.replace(character, escape + character));
	}

	public static String toString(Object object) {
		return null == object ? null : object.toString();
	}

	public static String toLoggingJsonString(Object object, String[] maskPropertyNames) {
		return toJsonString(object, LangUtils.getMaskValueFilter(maskPropertyNames),
				JSONWriter.Feature.WriteMapNullValue);
	}

	public static String toJsonString(Object object) {
		return toJsonString(object, null);
	}

	public static String toJsonString(Object object, Filter filter, JSONWriter.Feature... features) {
		String ret = null;
		if (object instanceof String) {
			ret = (String) object;
		} else {
			ret = JSON.toJSONString(object, filter, features);
		}
		return ret;
	}

	public static Integer toInteger(Object value) {
		Long longValue = toLong(value);
		return null == longValue ? null : longValue.intValue();
	}

	public static Double doubleScale(Double value, int retained) {
		Double result = null;
		if (null != value) {
			BigDecimal bd = new BigDecimal(value.toString());
			result = bd.setScale(retained, RoundingMode.DOWN).doubleValue();
		}
		return result;
	}

	public static <T, C extends Comparator<T>> OneTwo<T, T> getComparedUnAsserted(List<T> list, C c,
			Function<Integer, Boolean> assertResult) {
		OneTwo<T, T> oneTwo = null;
		for (int i = 0; i < list.size() - 1; i++) {
			T t1 = list.get(i);
			T t2 = list.get(i + 1);
			int compared = c.compare(t1, t2);
			boolean asserted = assertResult.apply(compared);
			if (!asserted) {
				oneTwo = new OneTwo<T, T>(t1, t2);
				break;
			}
		}
		return oneTwo;
	}

	public static <T> T parseObject(Object object, Class<T> clazz) {
		T t = null;
		if (object instanceof String) {
			t = JSON.parseObject((String) object, clazz);
		}
		// TODO other case
		return t;
	}

	public static ValueFilter getMaskValueFilter(String[] inputExludePropertyNames) {
		ValueFilter valueFilter = null;
		if (ArrayUtils.isNotEmpty(inputExludePropertyNames)) {
			valueFilter = (Object object, String name, Object value) -> {
				String mask = LangUtils.getStream(inputExludePropertyNames)
						.filter(propertyName -> null != value && StringUtils.equals(propertyName, name))//
						.map(propertyName -> {
							return "******";
						})//
						.findFirst()//
						.orElse(null);
				return null != mask ? mask : value;
			};
		}
		return valueFilter;
	}

	public static String join(Stream<?> stream, CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
		return null == stream ? ""
				: stream//
						.filter(Objects::nonNull)//
						.map(LangUtils::toString)//
						.filter(StringUtils::isNotBlank)//
						.collect(Collectors.joining(delimiter, prefix, suffix));
	}

	/**
	 * 
	 * @param input      指定字符串
	 * @param keepLength input如果大于等于指定长度，则原样输出
	 * @return
	 */
	public static String fillCompletion(String input, int keepLength, char append, boolean directionLeft) {
		if (null != input && keepLength > input.length()) {
			int length = keepLength - input.length();
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < length; i++) {
				builder.append(append);
			}
			if (directionLeft) {
				builder.append(input);
			} else {
				builder.insert(0, input);
			}
			return builder.toString();
		} else {
			return input;
		}

	}

	public static void tryCatchFinally(Processor tryBlock, Consumer<Throwable> catchBlock, Processor finallyBlock) {
		try {
			tryBlock.process();
		} catch (Throwable throwable) {
			catchBlock.accept(throwable);
		} finally {
			finallyBlock.process();
		}
	}

	public static Long toLong(Object object) {
		if (null == object) {
			return null;
		} else if (object instanceof Number value) {
			return value.longValue();
		} else if (object instanceof String value) {
			return Long.valueOf(value);
		} else {
			throw new ClassCastException(object + "can't cast to Long");
		}
	}

	public static <K, V> Map<K, V> mergeToMap(K[] keys, V[] values) {
		Map<K, V> map = new LinkedHashMap<>();
		if (ArrayUtils.isNotEmpty(keys) && ArrayUtils.isNotEmpty(values)) {
			IntStream.range(0, keys.length).forEach(i -> {
				map.put(keys[i], values[i]);
			});
		}
		return map;
	}

}

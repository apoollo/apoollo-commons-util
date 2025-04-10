/**
 * 
 */
package com.apoollo.commons.util.request.context;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import com.apoollo.commons.util.LangUtils;

/**
 * @author liuyulong
 * @since 2023年8月10日
 */
public interface CodeName<C, N> {

    public C getCode();

    public N getName();

    public default boolean overrideEquals(Object obj) {
        if (obj instanceof CodeName) {
            CodeName<?, ?> codeName = (CodeName<?, ?>) obj;
            return null != codeName.getCode() && null != this.getCode() && codeName.getCode().equals(this.getCode());
        }
        return false;
    }

    public default int overrideHashCode() {
        return this.getCode().hashCode();
    }

    public default boolean is(C code) {
        return is(code, this);
    }

    public static <C, N> boolean is(C code, CodeName<C, N> codeName) {
        return null != codeName && codeName.getCode().equals(code);
    }

    @SafeVarargs
    public static <C, N, T extends CodeName<C, N>> Optional<T> get(C code, T... codeNames) {
        return LangUtils.getStream(codeNames).filter(codeName -> is(code, codeName)).findFirst();
    }

    @SafeVarargs
    public static <C, N> boolean contains(C code, CodeName<C, N>... codeNames) {
        return get(code, codeNames).isPresent();
    }

    @SafeVarargs
    public static <C, N> N getName(C code, N defaultName, CodeName<C, N>... codeNames) {
        return get(code, codeNames).map(codeName -> codeName.getName()).orElse(defaultName);
    }

    @SafeVarargs
    public static <C, N> N getName(C code, CodeName<C, N>... codeNames) {
        return get(code, codeNames).map(codeName -> codeName.getName()).orElse(null);
    }

    public static <C, N, E extends Enum<E> & CodeName<C, N>, R extends CodeName<C, N>> List<R> toCodeNames(
            E[] enumerations, BiFunction<C, N, R> fn) {
        return toCodeNames(Stream.of(enumerations), fn);
    }

    public static <C, N, E extends Enum<E> & CodeName<C, N>, R extends CodeName<C, N>> List<R> toCodeNames(
            Stream<E> enumerations, BiFunction<C, N, R> fn) {
        return getEnumerationStream(enumerations)
                .map(enumeration -> fn.apply(enumeration.getCode(), enumeration.getName())).toList();
    }

    public static <C, N, E extends Enum<E> & CodeName<C, N>, R extends CodeName<C, N>> List<R> toCodeNames(
            E[] enumerations, Function<E, R> fn) {
        return toCodeNames(Stream.of(enumerations), fn);
    }

    public static <C, N, E extends Enum<E> & CodeName<C, N>, R extends CodeName<C, N>> List<R> toCodeNames(
            Stream<E> enumerations, Function<E, R> fn) {
        return getEnumerationStream(enumerations).map(enumeration -> fn.apply(enumeration)).toList();
    }

    public static <C, N, E extends Enum<E> & CodeName<C, N>> Stream<E> getEnumerationStream(Stream<E> enumerations) {
        return enumerations.filter(Objects::nonNull).sorted(Comparator.comparing(Enum::ordinal));
    }

    public static <C, N, E extends Enum<E> & CodeName<C, N>> Stream<E> getExludesEnumerationStream(
            Stream<E> enumerations, List<E> exludes) {
        return enumerations.filter(enumeration -> {
            return !LangUtils.getStream(exludes).filter(exlude -> enumeration.getCode().equals(exlude.getCode()))
                    .findAny().isPresent();

        });

    }

    @SafeVarargs
    public static <C, N, E extends Enum<E> & CodeName<C, N>> Stream<E> getExludesEnumerationStream(
            Stream<E> enumerations, E... exludes) {
        return getExludesEnumerationStream(enumerations, LangUtils.toList(exludes));
    }

}

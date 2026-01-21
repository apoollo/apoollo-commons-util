package com.apoollo.commons.util.poi.doc;

import java.util.List;
import java.util.function.BiFunction;

import org.apache.poi.xwpf.usermodel.XWPFTableCell;

import com.apoollo.commons.util.model.Consumer3;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableData<T> {

	private List<T> datas;

	private int startRowIndex;

	private Consumer3<T, Integer, XWPFTableCell> consumer;

	private BiFunction<T, Integer, String> function;

	private TableData() {
	}

	private TableData(List<T> datas) {
		super();
		this.datas = datas;
	}

	public static <T> TableData<T> newInstance(List<T> datas) {
		return new TableData<>(datas);
	}

	public TableData<T> withStartRowIndex(int startRowIndex) {
		this.startRowIndex = startRowIndex;
		return this;
	}

	public TableData<T> withConsumer(Consumer3<T, Integer, XWPFTableCell> consumer) {
		this.consumer = consumer;
		return this;
	}

	public TableData<T> withFunction(BiFunction<T, Integer, String> function) {
		this.function = function;
		return this;
	}

}

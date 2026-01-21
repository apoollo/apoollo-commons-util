package com.apoollo.commons.util.poi.doc;

import java.util.List;
import java.util.function.BiFunction;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;

import com.apoollo.commons.util.model.Consumer3;

public class TableUtils {

	/**
	 * 动态设置表格行数据并保持样式一致
	 * 
	 * @param table         表格对象
	 * @param startRowIndex 起始行索引
	 * @param data          数据列表
	 * @param consumer      单元格处理函数
	 * @param <T>           数据类型
	 */
	public static <T> void setTableRows(XWPFTable table, int startRowIndex, List<T> data,
			Consumer3<T, Integer, XWPFTableCell> consumer, BiFunction<T, Integer, String> function) {
		if (CollectionUtils.isNotEmpty(data)) {
			List<XWPFTableRow> rows = table.getRows();
			XWPFTableRow templateRow = rows.get(startRowIndex);
			int tableLastCellIndex = templateRow.getTableCells().size() - 1;
			int tableLastRowIndex = rows.size() - 1;

			for (int i = 0; i < data.size(); i++) {

				T object = data.get(i);
				int rowIndex = startRowIndex + i;

				if (rowIndex > tableLastRowIndex) {
					// 当数据行数超过表格模版行数的时候，需要创建新行
					XWPFTableRow tableRow = new XWPFTableRow((CTRow) templateRow.getCtRow().copy(), table);

					setCells(tableLastCellIndex, tableRow, object, consumer, function);

					table.addRow(tableRow);
				} else {
					// 直接设置值
					setCells(tableLastCellIndex, table.getRow(rowIndex), object, consumer, function);
				}

			}
		}
	}

	private static <T> void setCells(int tableLastCellIndex, XWPFTableRow tableRow, T object,
			Consumer3<T, Integer, XWPFTableCell> consumer, BiFunction<T, Integer, String> function) {
		for (int j = 0; j <= tableLastCellIndex; j++) {
			XWPFTableCell tableCell = tableRow.getCell(j);
			if (null == consumer) {
				String value = function.apply(object, j);
				if (null != value) {
					tableCell.setText(value);
				}
			} else {
				consumer.accept(object, j, tableCell);

			}
		}
	}

}

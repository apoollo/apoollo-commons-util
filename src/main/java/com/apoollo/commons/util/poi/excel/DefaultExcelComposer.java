/**
 * 
 */
package com.apoollo.commons.util.poi.excel;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.CodePageUtil;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.poi.excel.model.ListSheet;
import com.apoollo.commons.util.poi.excel.model.NestedObjectColumnSeria;
import com.apoollo.commons.util.poi.excel.model.NestedProperties;
import com.apoollo.commons.util.poi.excel.model.PositionCell;
import com.apoollo.commons.util.poi.excel.model.PositionRow;
import com.apoollo.commons.util.poi.excel.model.PositionSheet;
import com.apoollo.commons.util.poi.excel.model.PositionWorkbook;
import com.apoollo.commons.util.poi.excel.model.RuntimeNestedObject;

/**
 * @author liuyulong
 */
public class DefaultExcelComposer implements ExcelComposer {

    private static final PropertyUtilsBean PROPERTY_UTILS_BEAN = new PropertyUtilsBean();

    private Workbook workbook;
    private PositionWorkbook positionWorkbook;
    private Map<BiPredicate<Integer, Integer>, Consumer<Cell>> lastConsumers;
    private CellStyle defaultCellStyle;
    private CellStyle dateCellStyle;

    public DefaultExcelComposer(Workbook workbook, PositionWorkbook positionWorkbook) {
        super();
        this.workbook = workbook;
        this.positionWorkbook = positionWorkbook;
        this.lastConsumers = positionWorkbook.getLastConsumers();
        this.defaultCellStyle = LangUtils.defaultValue(positionWorkbook.getDefaultCellStyle(),
                () -> PositionWorkbook.createDefaultCellStyle(workbook));
        this.dateCellStyle = LangUtils.defaultValue(positionWorkbook.getDefaultCellStyle(),
                PositionWorkbook.createDefaultDateCellStyle(workbook));
    }

    public DefaultExcelComposer(PositionWorkbook positionWorkbook) {
        this(new HSSFWorkbook(), positionWorkbook);
    }

    @Override
    public Workbook compose() {
        if (workbook instanceof HSSFWorkbook) {
            int codepage = CodePageUtil.CP_UTF8;
            HSSFWorkbook hssfWorkbook = ((HSSFWorkbook) workbook);
            hssfWorkbook.createInformationProperties();
            SummaryInformation summaryInformation = hssfWorkbook.getSummaryInformation();
            summaryInformation.getFirstSection().setCodepage(codepage);
            DocumentSummaryInformation documentSummaryInformation = hssfWorkbook.getDocumentSummaryInformation();
            documentSummaryInformation.getFirstSection().setCodepage(codepage);
        }
        LangUtils.getStream(positionWorkbook.getAutoListSheets()).forEach(entrySet -> {
            Sheet sheet = workbook.getSheet(entrySet.getKey());
            if (null == sheet) {
                sheet = workbook.createSheet(entrySet.getKey());
            }
            autoListSheet(sheet, entrySet.getValue());
            setColumnWidth(sheet, entrySet.getValue().getColumnWidth());
        });

        LangUtils.getStream(positionWorkbook.getFreeSheets()).forEach(entrySet -> {
            Sheet sheet = workbook.getSheet(entrySet.getKey());
            if (null == sheet) {
                sheet = workbook.createSheet(entrySet.getKey());
            }
            freeSheet(sheet, entrySet.getValue());
        });

        return workbook;
    }

    public void setColumnWidth(Sheet sheet, Map<Integer, Integer> columnWidth) {
        if (null != columnWidth) {
            LangUtils.getStream(columnWidth.entrySet()).forEach(entry -> {
                sheet.setColumnWidth(entry.getKey(), entry.getValue() * 700);
            });
        }
    }

    public void freeSheet(Sheet sheet, PositionSheet positionSheet) {
        LangUtils.ifNotNull(positionSheet::getSheetConsumer, consumer -> consumer.accept(sheet));
        LangUtils.getStream(positionSheet.getRows()).forEach(entrySet -> {
            Row row = sheet.getRow(entrySet.getKey());
            if (null == row) {
                row = sheet.createRow(entrySet.getKey());
            }
            freeRow(row, entrySet.getValue());
        });
    }

    public void freeRow(Row row, PositionRow positionRow) {
        LangUtils.getStream(positionRow.getCells()).forEach(entrySet -> {
            Cell cell = row.getCell(entrySet.getKey());
            if (null == cell) {
                cell = row.createCell(entrySet.getKey());
            }
            freeCell(row, cell, entrySet.getValue());
        });
        LangUtils.ifNotNull(positionRow::getRowConsumer, consumer -> consumer.accept(row));
    }

    public void freeCell(Row row, Cell cell, PositionCell positionCell) {
        setCellValue(defaultCellStyle, dateCellStyle, row, cell, positionCell.getValue());
        LangUtils.ifNotNull(positionCell::getCellConsumer, consumer -> consumer.accept(cell));
    }

    public void autoListSheet(Sheet sheet, ListSheet listLheet) {

        CellStyle listSheetTitlesCellStyle = LangUtils.defaultValue(listLheet.getTitlesCellStyle(),
                () -> PositionWorkbook.createTitleCellStyle(workbook));
        CellStyle listSheetValuesCellStyle = LangUtils.defaultValue(listLheet.getValuesCellStyle(), defaultCellStyle);
        CellStyle listSheetDateCellStyle = LangUtils.defaultValue(listLheet.getDateCellStyle(), dateCellStyle);

        int columnStartIndex = LangUtils.defaultValue(listLheet.getColumnStartIndex(), 0);
        int columnValueIndex = columnStartIndex;
        int rowStartIndex = LangUtils.defaultValue(listLheet.getRowStartIndex(), 0);
        if (createTitleRow(listSheetTitlesCellStyle, listSheetDateCellStyle, sheet, columnStartIndex, columnValueIndex,
                rowStartIndex, listLheet.getTitles(), listLheet.getRowAutoSerialNumber(), null)) {
            rowStartIndex++;
        }

        if (BooleanUtils.isTrue(listLheet.getRowAutoSerialNumber())) {
            columnValueIndex++;
        }
        if (CollectionUtils.isNotEmpty(listLheet.getValues())) {
            createDataRows(listSheetValuesCellStyle, listSheetDateCellStyle, sheet, rowStartIndex, columnStartIndex,
                    columnValueIndex, listLheet.getValues(), listLheet.getNestedProperties(),
                    listLheet.getRowAutoSerialNumber(), LangUtils.defaultValue(listLheet.getAutoMergeCells(), true));
        }
    }

    public boolean createTitleRow(CellStyle defaultCellStyle, CellStyle dateCellStyle, Sheet sheet,
            int columnStartIndex, int columnValueIndex, int rowStartIndex, List<String> titles,
            Boolean rowAutoSerialNumber, Boolean autoMergeCells) {
        boolean created = false;
        if (CollectionUtils.isNotEmpty(titles)) {
            Stream<String> stream = titles.stream();
            if (BooleanUtils.isTrue(rowAutoSerialNumber)) {
                stream = Stream.concat(Stream.of("序号"), stream);
            }
            createDataRows(defaultCellStyle, dateCellStyle, sheet, rowStartIndex, columnStartIndex, columnValueIndex,
                    Stream.of(stream.toList()).toList(), null, false, autoMergeCells);
            created = true;
        }
        return created;
    }

    public RuntimeNestedObject createDataRows(CellStyle defaultCellStyle, CellStyle dateCellStyle, Sheet sheet,
            int rowStartIndex, int columnStartIndex, int columnValueIndex, List<?> values,
            NestedProperties nestedProperties, Boolean rowAutoSerialNumber, Boolean autoMergeCells) {
        int rowIndex = rowStartIndex;
        RuntimeNestedObject runtimeNestedObject = null;
        if (CollectionUtils.isNotEmpty(values)) {
            for (int i = 0; i < values.size(); i++) {
                Row row = sheet.createRow(rowIndex);
                Object value = values.get(i);
                runtimeNestedObject = createRowsCells(defaultCellStyle, dateCellStyle, sheet, row, rowStartIndex,
                        rowIndex, value, nestedProperties, columnStartIndex, columnValueIndex, rowAutoSerialNumber,
                        autoMergeCells, false);
                rowIndex = runtimeNestedObject.getRowIndex();
                rowIndex++;
            }
        }

        // 补全空白单元格
        if (null != runtimeNestedObject.getLastNoneNestedObjectColumnValueIndex() && runtimeNestedObject
                .getLastNoneNestedObjectColumnValueIndex() < runtimeNestedObject.getColumnValueIndex()) {
            for (int i = rowStartIndex; i < rowIndex; i++) {
                Row row = sheet.getRow(i);
                for (int j = runtimeNestedObject.getLastNoneNestedObjectColumnValueIndex(); j < runtimeNestedObject
                        .getColumnValueIndex(); j++) {
                    Cell cell = row.getCell(j);
                    if (null == cell) {
                        cell = row.createCell(j);
                        setCellValue(defaultCellStyle, dateCellStyle, row, cell, null);
                    }
                }
            }
        }

        return runtimeNestedObject;
    }

    public RuntimeNestedObject createRowsCells(CellStyle defaultCellStyle, CellStyle dateCellStyle, Sheet sheet,
            Row row, int rowStartIndex, int rowIndex, Object rowObject, NestedProperties rowObjectProperties,
            int columnStartIndex, int columnValueIndex, Boolean rowAutoSerialNumber, Boolean autoMergeCells,
            Boolean rightAppend) {

        createLeftRowCells(defaultCellStyle, dateCellStyle, row, rowStartIndex, columnStartIndex, columnValueIndex,
                rowAutoSerialNumber, rightAppend);

        RuntimeNestedObject runtimeNestedObject = null;

        int columnNestedValueIndex = 0;
        Integer lastNoneNestedObjectColumnValueIndex = null;
        Integer columnValueStartIndex = columnValueIndex;
        if (null != rowObjectProperties) {
            PropertyDescriptor[] descriptors = PROPERTY_UTILS_BEAN.getPropertyDescriptors(rowObject);
            List<String> properties = rowObjectProperties.getProperties();
            // 嵌入对象下标序列链表
            NestedObjectColumnSeria nestedObjectColumnSeria = null;
            NestedObjectColumnSeria nextNestedObjectColumnSeria = null;

            // 遍历属性列表
            for (int i = 0; i < properties.size(); i++) {
                String property = properties.get(i);
                Optional<PropertyDescriptor> descriptorOptional = LangUtils.getStream(descriptors)
                        .filter(descriptor -> descriptor.getName().equals(property)).findAny();

                // 发现可用属性
                if (descriptorOptional.isPresent()) {

                    Object propertyValue = getPropertyValue(descriptorOptional, rowObject);
                    // 如果为基本类型
                    if (isSerializebleBasicValue(propertyValue)) {
                        Cell cell = row.createCell(columnValueIndex);
                        setCellValue(defaultCellStyle, dateCellStyle, row, cell, propertyValue);
                        columnValueIndex++;
                    } else if (propertyValue instanceof Iterable) {
                        // 如果为嵌入属性
                        Iterable<?> nestedIterable = (Iterable<?>) propertyValue;
                        Iterator<?> nestedIterator = nestedIterable.iterator();
                        NestedProperties nestedProperties = rowObjectProperties.getNestedProperties().get(property);
                        int leftColumnSize = columnValueIndex - columnStartIndex;
                        if (nestedIterator.hasNext()) {
                            if (null == nestedObjectColumnSeria) {
                                // 链表碰到嵌入对象开始，写入非嵌入对象的值，以及初始化下一个嵌入对象
                                nextNestedObjectColumnSeria = new NestedObjectColumnSeria();
                                nestedObjectColumnSeria = new NestedObjectColumnSeria(false,
                                        columnValueStartIndex + i - 1, nextNestedObjectColumnSeria);
                            } else {
                                // 写入非嵌入对象的值，以及初始化下一个嵌入对象
                                int nextNestedObjectColumnIndex = nextNestedObjectColumnSeria.getColumnIndex() + i;
                                nextNestedObjectColumnSeria.setNext(new NestedObjectColumnSeria(false,
                                        nextNestedObjectColumnIndex, new NestedObjectColumnSeria()));
                                nextNestedObjectColumnSeria = nextNestedObjectColumnSeria.getNext().getNext();
                            }

                            if (leftColumnSize > 0) {
                                // 如果这一行的左侧已经创建了列，那么需要延续当前行列的创建
                                runtimeNestedObject = createRowsCells(defaultCellStyle, dateCellStyle, sheet, row,
                                        rowStartIndex, rowIndex, nestedIterator.next(), nestedProperties,
                                        columnStartIndex, columnValueIndex, rowAutoSerialNumber, autoMergeCells, true);
                                rowIndex = runtimeNestedObject.getRowIndex();
                                columnNestedValueIndex = runtimeNestedObject.getColumnValueIndex();
                                lastNoneNestedObjectColumnValueIndex = runtimeNestedObject
                                        .getLastNoneNestedObjectColumnValueIndex();

                                while (nestedIterator.hasNext()) {
                                    // 剩余的行
                                    rowIndex++;
                                    runtimeNestedObject = createRowsCells(defaultCellStyle, dateCellStyle, sheet,
                                            sheet.createRow(rowIndex), rowStartIndex, rowIndex, nestedIterator.next(),
                                            nestedProperties, columnStartIndex, columnValueIndex, rowAutoSerialNumber,
                                            autoMergeCells, false);
                                    rowIndex = runtimeNestedObject.getRowIndex();
                                    columnNestedValueIndex = runtimeNestedObject.getColumnValueIndex();
                                    lastNoneNestedObjectColumnValueIndex = runtimeNestedObject
                                            .getLastNoneNestedObjectColumnValueIndex();
                                }

                            } else {
                                // 剩余的行
                                do {
                                    rowIndex++;
                                    runtimeNestedObject = createRowsCells(defaultCellStyle, dateCellStyle, sheet,
                                            sheet.createRow(rowIndex), rowStartIndex, rowIndex, nestedIterator.next(),
                                            nestedProperties, columnStartIndex, columnValueIndex, rowAutoSerialNumber,
                                            autoMergeCells, false);
                                    rowIndex = runtimeNestedObject.getRowIndex();
                                    columnNestedValueIndex = runtimeNestedObject.getColumnValueIndex();
                                } while (nestedIterator.hasNext());
                            }

                            // 将嵌入对象列下标置为当前列下标
                            columnValueIndex = columnNestedValueIndex;

                            // 链表碰到离开嵌入对象结束
                            nextNestedObjectColumnSeria.setNested(true);
                            nextNestedObjectColumnSeria.setColumnIndex(columnNestedValueIndex - 1);
                        }
                    }
                }
            }
            if (null != nextNestedObjectColumnSeria && columnNestedValueIndex < columnValueIndex) {
                int nextNestedObjectColumnIndex = nextNestedObjectColumnSeria.getColumnIndex()
                        + (columnValueIndex - columnNestedValueIndex);
                nextNestedObjectColumnSeria
                        .setNext(new NestedObjectColumnSeria(false, nextNestedObjectColumnIndex, null));
            }

            if (null == runtimeNestedObject) {
                lastNoneNestedObjectColumnValueIndex = columnValueIndex;
            } else {
                if (BooleanUtils.isTrue(autoMergeCells)) {
                    mergeCells(sheet, row.getRowNum(), rowIndex, columnValueStartIndex, nestedObjectColumnSeria);
                }
            }
        } else if (rowObject instanceof Iterable) {
            Iterable<?> iterable = (Iterable<?>) rowObject;
            Iterator<?> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                Object cellValue = iterator.next();
                if (isSerializebleBasicValue(cellValue)) {
                    Cell cell = row.createCell(columnValueIndex);
                    setCellValue(defaultCellStyle, dateCellStyle, row, cell, cellValue);
                    columnValueIndex++;
                } else {
                    throw new UnsupportedOperationException("cells value type not support : " + rowObject);
                }
            }
        }
        return new RuntimeNestedObject(rowIndex, columnValueIndex, columnNestedValueIndex,
                lastNoneNestedObjectColumnValueIndex);
    }

    public Object getPropertyValue(Optional<PropertyDescriptor> descriptorOptional, Object object) {
        PropertyDescriptor propertyDescriptor = descriptorOptional.get();
        Object propertyValue;
        try {
            propertyValue = propertyDescriptor.getReadMethod().invoke(object, new Object[0]);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return propertyValue;
    }

    public void mergeCells(Sheet sheet, int rowStartIndex, int rowEndIndex, int columnValueStartIndex,
            NestedObjectColumnSeria nestedObjectColumnSeria) {

        // 如果嵌入列表只有一个对象，那么就无需合并行
        if (rowEndIndex > rowStartIndex) {

            // 先找到第一个非嵌入对象
            do {
                if (!nestedObjectColumnSeria.getNested()) {
                    for (int i = columnValueStartIndex; i <= nestedObjectColumnSeria.getColumnIndex(); i++) {
                        Integer columnMergeStartIndex = i;
                        Integer columnMergeEndIndex = columnMergeStartIndex;
                        sheet.addMergedRegion(new CellRangeAddress(rowStartIndex, rowEndIndex, columnMergeStartIndex,
                                columnMergeEndIndex));
                    }
                    break;
                }
            } while (null != (nestedObjectColumnSeria = nestedObjectColumnSeria.getNext()));

            // 再从第一个嵌入对象开始轮询
            while (null != nestedObjectColumnSeria
                    && null != (nestedObjectColumnSeria = nestedObjectColumnSeria.getNext())) {

                Integer columnMergeStartIndex = nestedObjectColumnSeria.getColumnIndex() + 1;
                nestedObjectColumnSeria = nestedObjectColumnSeria.getNext();
                if (null != nestedObjectColumnSeria) {
                    for (int i = columnMergeStartIndex; i <= nestedObjectColumnSeria.getColumnIndex(); i++) {
                        sheet.addMergedRegion(new CellRangeAddress(rowStartIndex, rowEndIndex, i, i));
                    }
                }
            }
        }

    }

    public void createLeftRowCells(CellStyle defaultCellStyle, CellStyle dateCellStyle, Row row, int startRowIndex,
            int columnStartIndex, int columnValueIndex, Boolean rowAutoSerialNumber, Boolean rightAppend) {

        if (BooleanUtils.isNotTrue(rightAppend)) {
            if (BooleanUtils.isTrue(rowAutoSerialNumber)) {
                Cell cell = row.createCell(columnStartIndex);
                int cellValue = row.getRowNum() - startRowIndex + 1;
                setCellValue(defaultCellStyle, dateCellStyle, row, cell, cellValue);
            } else {
                Cell cell = row.createCell(columnStartIndex);
                setCellValue(defaultCellStyle, dateCellStyle, row, cell, null);
            }
            int blankColumnSize = columnValueIndex - columnStartIndex;
            for (int i = 1; i <= blankColumnSize; i++) {
                Cell cell = row.createCell(columnStartIndex + i);
                setCellValue(defaultCellStyle, dateCellStyle, row, cell, null);
            }
        }

    }

    public boolean isSerializebleBasicValue(Object value) {
        return null == value || value instanceof Serializable && (value instanceof String || value instanceof Byte
                || value instanceof Short || value instanceof Integer || value instanceof Long || value instanceof Float
                || value instanceof Double || value instanceof Boolean || value instanceof Character
                || value instanceof Date || value instanceof Calendar || value instanceof LocalDate
                || value instanceof LocalDateTime);
    }

    public void setCellValue(CellStyle defaultCellStyle, CellStyle dateCellStyle, Row row, Cell cell,
            Object cellValue) {
        if (cellValue instanceof String) {
            cell.setCellValue((String) cellValue);
            cell.setCellStyle(defaultCellStyle);
        } else if (cellValue instanceof Byte) {
            cell.setCellValue((Byte) cellValue);
            cell.setCellStyle(defaultCellStyle);
        } else if (cellValue instanceof Short) {
            cell.setCellValue((Short) cellValue);
            cell.setCellStyle(defaultCellStyle);
        } else if (cellValue instanceof Integer) {
            cell.setCellValue((Integer) cellValue);
            cell.setCellStyle(defaultCellStyle);
        } else if (cellValue instanceof Long) {
            cell.setCellValue((Long) cellValue);
            cell.setCellStyle(defaultCellStyle);
        } else if (cellValue instanceof Float) {
            cell.setCellValue((Float) cellValue);
            cell.setCellStyle(defaultCellStyle);
        } else if (cellValue instanceof Double) {
            cell.setCellValue((Double) cellValue);
            cell.setCellStyle(defaultCellStyle);
        } else if (cellValue instanceof Boolean) {
            cell.setCellValue((Boolean) cellValue);
            cell.setCellStyle(defaultCellStyle);
        } else if (cellValue instanceof Character) {
            cell.setCellValue((Character) cellValue);
            cell.setCellStyle(defaultCellStyle);
        } else if (cellValue instanceof Date) {
            cell.setCellValue((Date) cellValue);
            cell.setCellStyle(dateCellStyle);
        } else if (cellValue instanceof Calendar) {
            cell.setCellValue((Calendar) cellValue);
            cell.setCellStyle(dateCellStyle);
        } else if (cellValue instanceof LocalDate) {
            cell.setCellValue((LocalDate) cellValue);
            cell.setCellStyle(dateCellStyle);
        } else if (cellValue instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) cellValue);
            cell.setCellStyle(dateCellStyle);
        } else if (null == cellValue) {
            cell.setBlank();
            cell.setCellStyle(defaultCellStyle);
        } else {
            cell.setCellValue(cellValue.toString());
            cell.setCellStyle(defaultCellStyle);
        }

        LangUtils.getStream(this.lastConsumers)
                .filter(entry -> entry.getKey().test(row.getRowNum(), cell.getColumnIndex())).forEach(entry -> {

                    entry.getValue().accept(cell);

                });

    }

}

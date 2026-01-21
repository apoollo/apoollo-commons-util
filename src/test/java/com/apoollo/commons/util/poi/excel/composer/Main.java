/**
 * 
 */
package com.apoollo.commons.util.poi.excel.composer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.poi.excel.ExcelUtils;
import com.apoollo.commons.util.poi.excel.model.ListSheet;
import com.apoollo.commons.util.poi.excel.model.NestedProperties;
import com.apoollo.commons.util.poi.excel.model.PositionCell;
import com.apoollo.commons.util.poi.excel.model.PositionSheet;
import com.apoollo.commons.util.poi.excel.model.PositionWorkbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 */
public class Main {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        ExcelUtils.export(excel1(), new FileOutputStream("C:\\Users\\DELL\\Desktop\\1.xlsx"));
        ExcelUtils.export(excel2(), new FileOutputStream("C:\\Users\\DELL\\Desktop\\2.xlsx"));
        ExcelUtils.export(excel3(), new FileOutputStream("C:\\Users\\DELL\\Desktop\\3.xlsx"));
        ExcelUtils.export(excel4(), new FileOutputStream("C:\\Users\\DELL\\Desktop\\4.xlsx"));

    }

    public static PositionWorkbook excel1() {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        PositionWorkbook positionWorkbook = new PositionWorkbook();
        positionWorkbook.setWorkbook(hssfWorkbook);
        ListSheet listSheet = new ListSheet();
        listSheet.setRowStartIndex(5);
        listSheet.setColumnStartIndex(0);
        listSheet.setRowAutoSerialNumber(true);
        listSheet.setTitles(LangUtils.toList("名称", "状态", "创建时间", "年龄", "成绩", "课程", "书籍", "书籍页数", "性别"));
        listSheet.setValues(LangUtils.toList(new Student("小明", true, new Date(), List.of(

                new Course("大学", List.of(new Book("大学上册", 10), new Book("大学下上册", 10)), "1H"),
                new Course("中庸", List.of(new Book("中庸上册", 20), new Book("中庸下上册", 15)), "2H"),
                new Course("礼记", List.of(new Book("礼记上册", 30), new Book("礼记下上册", 25)), "3H"),
                new Course("春秋", List.of(new Book("春秋上册", 40), new Book("春秋下上册", 35)), "4H")

        ), 11, 100.5,

                true),
                new Student("小红", false, new Date(),
                        List.of(new Course("论语", List.of(new Book("论语上册", 50), new Book("论语下上册", 45)), "5H"),
                                new Course("孟子", List.of(new Book("孟子上册", 60), new Book("孟子下上册", 55)), "6H")),
                        20, 98.999, false)));

        listSheet.setNestedProperties(new NestedProperties(
                List.of("name", "statusIsAvtive", "createTime", "age", "score", "courses", "gender"),
                Map.of("courses", new NestedProperties(List.of("courseName", "books"),
                        Map.of("books", new NestedProperties(List.of("name", "pages")))))));
        Map<Integer, Integer> columnWidth = new HashMap<>();
        columnWidth.put(0, 3);
        columnWidth.put(1, 3);
        columnWidth.put(2, 3);
        columnWidth.put(3, 8);
        columnWidth.put(4, 4);
        columnWidth.put(5, 4);
        columnWidth.put(6, 4);
        columnWidth.put(7, 5);
        columnWidth.put(8, 4);
        columnWidth.put(9, 3);
        listSheet.setColumnWidth(columnWidth);
        positionWorkbook.setAutoListSheets(Map.of("学生表", listSheet));

        PositionSheet positionSheet = new PositionSheet()//
                .addMergedRegionCell(hssfWorkbook, 0, 2, 0, 5, new PositionCell("2024年10月份账单"))//
                .addMergedRegionCell(hssfWorkbook, 3, 3, 0, 2, new PositionCell("前置说明"))//
                .addMergedRegionCell(hssfWorkbook, 3, 3, 3, 5, new PositionCell("后置说明"))//
        ;
        positionWorkbook.setFreeSheets(Map.of("学生表", positionSheet));
        return positionWorkbook;

    }

    public static PositionWorkbook excel2() {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        PositionWorkbook positionWorkbook = new PositionWorkbook();
        positionWorkbook.setWorkbook(hssfWorkbook);
        ListSheet listSheet = new ListSheet();
        listSheet.setRowStartIndex(5);
        listSheet.setColumnStartIndex(0);
        listSheet.setRowAutoSerialNumber(false);
        listSheet.setTitles(LangUtils.toList("名称", "状态", "创建时间", "课程", "书籍", "书籍页数", "年龄", "成绩"));
        listSheet.setValues(LangUtils.toList(
                new Student("小明", true, new Date(),
                        List.of(new Course("大学", List.of(new Book("大学上册", 10), new Book("大学下上册", 10)), "1H"),
                                new Course("中庸", List.of(new Book("中庸上册", 20), new Book("中庸下上册", 15)), "2H"),
                                new Course("礼记", List.of(new Book("礼记上册", 30), new Book("礼记下上册", 25)), "3H"),
                                new Course("春秋", List.of(new Book("春秋上册", 40), new Book("春秋下上册", 35)), "4H")),
                        11, 100.5,

                        true),
                new Student("小红", false, new Date(),
                        List.of(new Course("论语", List.of(new Book("论语上册", 50), new Book("论语下上册", 45)), "5H"),
                                new Course("孟子", List.of(new Book("孟子上册", 60), new Book("孟子下上册", 55)), "6H")),
                        20, 98.999,

                        false)));

        listSheet.setNestedProperties(
                new NestedProperties(List.of("name", "statusIsAvtive", "createTime", "courses", "age", "score"),
                        Map.of("courses", new NestedProperties(List.of("courseName", "books"),
                                Map.of("books", new NestedProperties(List.of("name", "pages")))))));
        Map<Integer, Integer> columnWidth = new HashMap<>();
        columnWidth.put(0, 3);
        columnWidth.put(1, 3);
        columnWidth.put(2, 8);
        columnWidth.put(3, 4);
        columnWidth.put(4, 4);
        columnWidth.put(5, 4);
        columnWidth.put(6, 5);
        columnWidth.put(7, 5);
        columnWidth.put(8, 4);
        columnWidth.put(9, 3);
        listSheet.setColumnWidth(columnWidth);
        positionWorkbook.setAutoListSheets(Map.of("学生表", listSheet));

        PositionSheet positionSheet = new PositionSheet()//
                .addMergedRegionCell(hssfWorkbook, 0, 2, 0, 5, new PositionCell("2024年10月份账单"))//
                .addMergedRegionCell(hssfWorkbook, 3, 3, 0, 2, new PositionCell("前置说明"))//
                .addMergedRegionCell(hssfWorkbook, 3, 3, 3, 5, new PositionCell("后置说明"))//
        ;
        positionWorkbook.setFreeSheets(Map.of("学生表", positionSheet));
        return positionWorkbook;

    }

    public static PositionWorkbook excel3() {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        PositionWorkbook positionWorkbook = new PositionWorkbook();
        positionWorkbook.setWorkbook(hssfWorkbook);
        ListSheet listSheet = new ListSheet();
        listSheet.setRowStartIndex(5);
        listSheet.setColumnStartIndex(0);
        listSheet.setRowAutoSerialNumber(true);
        listSheet.setTitles(LangUtils.toList("名称", "状态", "创建时间", "课程", "书籍", "书籍页数", "课程时长", "年龄", "成绩", "性别"));
        listSheet.setValues(LangUtils.toList(
                new Student("小明", true, new Date(),
                        List.of(new Course("大学", List.of(new Book("大学上册", 10), new Book("大学下上册", 10)), "1H"),
                                new Course("中庸", List.of(new Book("中庸上册", 20), new Book("中庸下上册", 15)), "2H"),
                                new Course("礼记", List.of(new Book("礼记上册", 30), new Book("礼记下上册", 25)), "3H"),
                                new Course("春秋", List.of(new Book("春秋上册", 40), new Book("春秋下上册", 35)), "4H")),
                        11, 100.5,

                        true),
                new Student("小红", false, new Date(),
                        List.of(new Course("论语", List.of(new Book("论语上册", 50), new Book("论语下上册", 45)), "5H"),
                                new Course("孟子", List.of(new Book("孟子上册", 60), new Book("孟子下上册", 55)), "6H")),
                        20, 98.999,

                        false)));

        listSheet.setNestedProperties(new NestedProperties(
                List.of("name", "statusIsAvtive", "createTime", "courses", "age", "score", "gender"),
                Map.of("courses", new NestedProperties(List.of("courseName", "books", "courseDuration"),
                        Map.of("books", new NestedProperties(List.of("name", "pages")))))));
        Map<Integer, Integer> columnWidth = new HashMap<>();
        columnWidth.put(0, 3);
        columnWidth.put(1, 3);
        columnWidth.put(2, 3);
        columnWidth.put(3, 8);
        columnWidth.put(4, 3);
        columnWidth.put(5, 8);
        columnWidth.put(6, 4);
        columnWidth.put(7, 4);
        columnWidth.put(8, 3);
        columnWidth.put(9, 3);
        listSheet.setColumnWidth(columnWidth);
        positionWorkbook.setAutoListSheets(Map.of("学生表", listSheet));

        PositionSheet positionSheet = new PositionSheet()//
                .addMergedRegionCell(hssfWorkbook, 0, 2, 0, 5, new PositionCell("2024年10月份账单"))//
                .addMergedRegionCell(hssfWorkbook, 3, 3, 0, 2, new PositionCell("前置说明"))//
                .addMergedRegionCell(hssfWorkbook, 3, 3, 3, 5, new PositionCell("后置说明"))//
        ;
        positionWorkbook.setFreeSheets(Map.of("学生表", positionSheet));
        return positionWorkbook;

    }

    public static PositionWorkbook excel4() {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        PositionWorkbook positionWorkbook = new PositionWorkbook();
        positionWorkbook.setWorkbook(hssfWorkbook);
        ListSheet listSheet = new ListSheet();
        listSheet.setRowStartIndex(5);
        listSheet.setColumnStartIndex(0);
        listSheet.setAutoMergeCells(true);
        listSheet.setRowAutoSerialNumber(true);
        listSheet.setTitles(LangUtils.toList("名称", "状态", "创建时间", "课程", "书籍", "书籍页数", "课程时长", "年龄", "成绩", "性别"));
        listSheet.setValues(LangUtils.toList(
                new Student("小明", true, new Date(),
                        List.of(new Course("大学", List.of(new Book("大学上册", 10), new Book("大学下上册", 10)), "1H"),
                                new Course("中庸", List.of(new Book("中庸上册", 20), new Book("中庸下上册", 15)), "2H"),
                                new Course("礼记", List.of(new Book("礼记上册", 30), new Book("礼记下上册", 25)), "3H"),
                                new Course("春秋", List.of(new Book("春秋上册", 40), new Book("春秋下上册", 35)), "4H")),
                        11, 100.5, true),
                new Student("小红", false, new Date(),
                        List.of(new Course("论语", List.of(new Book("论语上册", 50), new Book("论语下上册", 45)), "5H"),
                                new Course("孟子", List.of(new Book("孟子上册", 60), new Book("孟子下上册", 55)), "6H")),
                        20, 98.999, false),
                new Student("小强", false, new Date(),
                        List.of(new Course("尚书", List.of(new Book("尚书上册", 70), new Book("尚书下上册", 65)), "7H"),
                                new Course("礼记", List.of(new Book("礼记上册", 80), new Book("礼记下上册", 75)), "8H")),
                        30, 98.999, false)

        ));

        listSheet.setNestedProperties(new NestedProperties(
                List.of("name", "statusIsAvtive", "createTime", "courses", "age", "score", "gender"),
                Map.of("courses", new NestedProperties(List.of("courseName", "books", "courseDuration"),
                        Map.of("books", new NestedProperties(List.of("name", "pages")))))));
        Map<Integer, Integer> columnWidth = new HashMap<>();
        columnWidth.put(0, 3);
        columnWidth.put(1, 3);
        columnWidth.put(2, 8);
        columnWidth.put(3, 8);
        columnWidth.put(4, 4);
        columnWidth.put(5, 4);
        columnWidth.put(6, 4);
        columnWidth.put(7, 4);
        columnWidth.put(8, 3);
        columnWidth.put(9, 3);
        listSheet.setColumnWidth(columnWidth);
        positionWorkbook.setAutoListSheets(Map.of("学生表", listSheet));

        PositionSheet positionSheet = new PositionSheet()//
                .addMergedRegionCell(hssfWorkbook, 0, 2, 0, 5, new PositionCell("2024年10月份账单"))//
                .addMergedRegionCell(hssfWorkbook, 3, 3, 0, 2, new PositionCell("前置说明"))//
                .addMergedRegionCell(hssfWorkbook, 3, 3, 3, 5, new PositionCell("后置说明"))//
        ;
        positionWorkbook.setFreeSheets(Map.of("学生表", positionSheet));

        CellStyle leftCellStyle = PositionWorkbook.createDefaultCellStyle(hssfWorkbook);
        leftCellStyle.setAlignment(HorizontalAlignment.LEFT);
        positionWorkbook.addLastCellStyleConsumer((rowIndex, columnIndex) -> rowIndex > 5 && columnIndex == 5,
                leftCellStyle);
        return positionWorkbook;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Student {
        private String name;
        private Boolean statusIsAvtive;
        private Date createTime;
        private List<Course> courses;
        private Integer age;
        private Double score;
        private Boolean gender;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Course {

        private String courseName;

        private List<Book> books;

        private String courseDuration;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Book {

        private String name;
        private int pages;
    }

}

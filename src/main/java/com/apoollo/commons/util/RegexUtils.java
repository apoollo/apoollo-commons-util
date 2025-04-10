/**
 * 
 */
package com.apoollo.commons.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.apoollo.commons.util.model.OCREffectiveDate;
import com.apoollo.commons.util.model.YearMonthDate;

/**
 * @author liuyulong
 * @since 2023年9月15日
 */
public class RegexUtils {

    // 密码规则：1、8-20位 2、至少有一个特殊字符、一个小写字母、一个大写字母、一个数字
    public static final Pattern PASSWORD = Pattern
            .compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()]).{8,20}$");

    // ${} 匹配此类表达式
    public static final Pattern EXPRESSION = Pattern.compile("\\$\\{\s*[^}]*\\}");

    // 身份证OCR 日期
    public static final Pattern IDENTITY_CARD_OCR_DATE = Pattern
            .compile("([0-9]{4})[^0-9]+([0-9]{1,2})[^0-9]+([0-9]{1,2})[^0-9]*");

    public static boolean matches(Pattern regex, String input) {
        boolean ret = false;
        if (StringUtils.isNotBlank(input)) {
            ret = regex.matcher(input).matches();
        }
        return ret;
    }

    public static void assertion(Pattern regex, String input, String message) {
        Assert.isTrue(matches(regex, input), message);
    }

    public static String compile(String input, Object[] args) {
        String ret = null;
        if (StringUtils.isNotBlank(input)) {
            StringBuffer stringBuffer = new StringBuffer();
            Matcher matcher = EXPRESSION.matcher(input);
            int i = 0;
            while (matcher.find()) {
                String replacement = null;
                if (null != args && args.length > i) {
                    replacement = LangUtils.toString(args[i]);
                } else {
                    replacement = "";
                }
                matcher.appendReplacement(stringBuffer, replacement);
                ++i;
            }
            matcher.appendTail(stringBuffer);
            ret = stringBuffer.toString();
        }
        return ret;
    }

    public static YearMonthDate getYearMonthDate(String ocrDate) {
        YearMonthDate yearMonthDate = null;
        if (StringUtils.isNotBlank(ocrDate)) {
            Matcher matcher = IDENTITY_CARD_OCR_DATE.matcher(ocrDate.trim());
            if (matcher.find()) {
                yearMonthDate = new YearMonthDate(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)),
                        Integer.valueOf(matcher.group(3)));

            }
        }
        return yearMonthDate;
    }

    public static OCREffectiveDate getOCREffectiveDate(String originOcr) {
        OCREffectiveDate OCREffectiveDate = new OCREffectiveDate();
        OCREffectiveDate.setEffectiveDateIsLongValid(false);
        if (StringUtils.isNotBlank(originOcr)) {
            if (originOcr.contains("-")) {
                String start = StringUtils.substringBefore(originOcr, "-");
                OCREffectiveDate.setEffectiveStartDate(getYearMonthDate(start));

                String end = StringUtils.substringAfter(originOcr, "-");
                if (!end.contains("长期")) {
                    OCREffectiveDate.setEffectiveEndDate(getYearMonthDate(end));
                } else {
                    OCREffectiveDate.setEffectiveDateIsLongValid(true);
                }
            } else if (originOcr.contains("长期")) {
                OCREffectiveDate.setEffectiveDateIsLongValid(true);
            }
        }
        return OCREffectiveDate;
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(getYearMonthDate("1985年11月9日 ")));
        System.out.println(JSON.toJSONString(getYearMonthDate("1985年2月31日 ")));
        System.out.println(JSON.toJSONString(getYearMonthDate("1985年07月09日 ")));
        System.out.println(JSON.toJSONString(getYearMonthDate("1985  年  07  月  09日")));
        System.out.println(JSON.toJSONString(getYearMonthDate("  1985年17  月09 日 ")));
        System.out.println(JSON.toJSONString(getYearMonthDate("1985年7月9日")));
        System.out.println(JSON.toJSONString(getYearMonthDate("   1985   年    7     月  9  日")));
        System.out.println(JSON.toJSONString(getOCREffectiveDate("2015.09.17-2035.09.17")));
        System.out.println(JSON.toJSONString(getOCREffectiveDate(" 2015 . 09 . 17 - 2035 . 09 . 17")));
        System.out.println(JSON.toJSONString(getOCREffectiveDate("长期")));
        System.out.println(JSON.toJSONString(getOCREffectiveDate(" 2015 . 09 . 17 - 长期 ")));
    }

}

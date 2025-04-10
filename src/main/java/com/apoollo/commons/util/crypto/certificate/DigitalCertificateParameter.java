/**
 * 
 */
package com.apoollo.commons.util.crypto.certificate;

import java.math.BigInteger;
import java.util.Date;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong6
 *
 */
@Getter
@Setter
public class DigitalCertificateParameter {

	private String cnOfX500Name;// 域名
	private String oOfX500Name;// 公司全称
	private String ouOfX500Name;// 部门全称
	private String cOfX500Name;// 国家代号
	private String stOfX500Name;// 省市全称
	private String lOfX500Name;// 城镇区
	private String emailOfX500Name;// 证书管理人电子邮件地址
	private BigInteger serial;// 序列号, 颁发机构唯一
	private Date notBefore;// 生效日期
	private Date notAfter;// 截止日期
	private Locale dateLocale;// 日期地域
}

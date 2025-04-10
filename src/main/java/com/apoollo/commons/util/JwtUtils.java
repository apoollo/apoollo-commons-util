/**
 * 
 */
package com.apoollo.commons.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.Getter;

/**
 * @author liuyulong6
 */
public class JwtUtils {

	public static final String HEADER_AUTHORIZATIONE = "Authorization";

	private static final String HEADER_AUTHORIZATION_SCHEME = "Bearer ";

	public static String getSecret(String secretKey, String secretKeySaltValue) {
		return StringUtils.isNotBlank(secretKeySaltValue) ? StringUtils.join(secretKey, "@", secretKeySaltValue)
				: secretKey;
	}

	public static String generateJwtToken(String accesskey, String secretKey, String secretKeySaltValue, Date issueDate,
			Date expiresAt) {
		return JWT//
				.create()//
				.withKeyId(accesskey)//
				.withIssuedAt(issueDate)//
				.withExpiresAt(expiresAt)//
				.sign(Algorithm.HMAC256(getSecret(secretKey, secretKeySaltValue)));
	}

	public static String generateJwtToken(String accesskey, String secretKey, String secretKeySaltValue, Date issueDate,
			int seconds) {
		return generateJwtToken(accesskey, secretKey, secretKeySaltValue, issueDate,
				DateUtils.addSeconds(issueDate, seconds));
	}

	public static String generateAuthorizationJwtToken(String accesskey, String secretKey, String secretKeySaltValue,
			Date issueDate, int seconds) {
		return authorizationConcatJwtToken(
				generateJwtToken(accesskey, secretKey, secretKeySaltValue, issueDate, seconds));
	}

	public static String generateAuthorizationJwtToken(String accesskey, String secretKey, String secretKeySaltValue,
			int seconds) {
		return generateAuthorizationJwtToken(accesskey, secretKey, secretKeySaltValue, new Date(), seconds);
	}

	public static String authorizationConcatJwtToken(String jwtToken) {
		return HEADER_AUTHORIZATION_SCHEME.concat(jwtToken);
	}

	public static void jwtVerify(String jwtToken, String secretKey, String secretKeySaltValue) {
		JWT.require(Algorithm.HMAC256(getSecret(secretKey, secretKeySaltValue))).build().verify(jwtToken);
	}

	public static void jwtVerify(DecodedJWT jwtToken, String secretKey, String secretKeySaltValue) {
		JWT.require(Algorithm.HMAC256(getSecret(secretKey, secretKeySaltValue))).build().verify(jwtToken);
	}

	public static String extractJwtToken(String authorizationJwtToken) {
		String jwtToken = null;
		if (StringUtils.isNotBlank(authorizationJwtToken)
				&& authorizationJwtToken.startsWith(HEADER_AUTHORIZATION_SCHEME)) {
			jwtToken = authorizationJwtToken.substring(HEADER_AUTHORIZATION_SCHEME.length());
		}
		return jwtToken;
	}

	@Getter
	public static class JwtToken {
		private String jwtToken;
		private DecodedJWT jwtTokenDecoded;
		private String accessKey;
		private Date expiresAt;
		private Date issuedAt;
		private Long expiresAtTimeMillis;
		private Long issuedAtTimeMillis;

		private void decode(DecodedJWT jwtTokenDecoded) {
			if (null != jwtTokenDecoded) {
				accessKey = jwtTokenDecoded.getKeyId();
				expiresAt = jwtTokenDecoded.getExpiresAt();
				issuedAt = jwtTokenDecoded.getIssuedAt();
				if (null != expiresAt) {
					expiresAtTimeMillis = expiresAt.getTime();
				}
				if (null != issuedAt) {
					issuedAtTimeMillis = issuedAt.getTime();
				}
			}
		}

		public JwtToken(String jwtToken, DecodedJWT jwtTokenDecoded) {
			this.jwtToken = jwtToken;
			this.jwtTokenDecoded = jwtTokenDecoded;
			decode(jwtTokenDecoded);
		}

	}

	@Getter
	public static class Renewal {
		private JwtToken jwtToken;// 原JwtToken
		private String secretKey;// 秘钥
		private String secretKeySaltValue;// 秘钥盐值

		private Boolean renewed;// 是否续订
		private Long renewalDuration;// 续订时长
		private Date renewalExpiresAt;// 续订截止日期
		private String renewalJwtToken;// 续订的JwtToken
		private String renewalAuthorizationJwtToken;// 续订的请求Token

		public Renewal(JwtToken jwtToken, String secretKey, String secretKeySaltValue) {
			this.jwtToken = jwtToken;
			this.secretKey = secretKey;
			this.secretKeySaltValue = secretKeySaltValue;
		}

		public Renewal renewal() {
			return renewal(null);
		}

		public Renewal renewal(Long renewalDuration) {
			Long issuedAtTimeMillis = jwtToken.getIssuedAtTimeMillis();
			Long expiresAtTimeMillis = jwtToken.getExpiresAtTimeMillis();
			if (null != issuedAtTimeMillis && null != expiresAtTimeMillis) {
				Long currentTimeMillis = System.currentTimeMillis();
				Long keepDuration = expiresAtTimeMillis - issuedAtTimeMillis;// 原来需要保持的时长
				Long elapsesTimeMillis = currentTimeMillis - issuedAtTimeMillis;// 已消耗时长
				if (elapsesTimeMillis >= keepDuration / 3) {// 如果消耗时长大于等于保持时长的3分之一，则进行续期
					this.renewed = true;
					this.renewalDuration = null == renewalDuration ? keepDuration : renewalDuration;
					renewalExpiresAt = new Date(currentTimeMillis + this.renewalDuration);
					renewalJwtToken = generateJwtToken(jwtToken.getAccessKey(), secretKey, secretKeySaltValue,
							new Date(currentTimeMillis), renewalExpiresAt);
					renewalAuthorizationJwtToken = authorizationConcatJwtToken(renewalJwtToken);
				}
			}
			return this;
		}

	}
}

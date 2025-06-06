/**
 * 
 */
package com.apoollo.commons.util.web.captcha;

import java.io.IOException;
import java.time.Duration;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.apoollo.commons.util.CaptchaUtils;
import com.apoollo.commons.util.exception.AppIllegalArgumentException;
import com.apoollo.commons.util.redis.service.RedisNameSpaceKey;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 */
public class RedisCaptchaService implements CaptchaService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisCaptchaService.class);

	private RedisTemplate<String, String> redisTemplate;
	private RedisNameSpaceKey redisNameSpaceKey;

	public RedisCaptchaService(RedisTemplate<String, String> redisTemplate, RedisNameSpaceKey redisNameSpaceKey) {
		super();
		this.redisTemplate = redisTemplate;
		this.redisNameSpaceKey = redisNameSpaceKey;
	}

	@Override
	public void writeCaptchaImage(HttpServletResponse response, String token, Duration timeout) throws IOException {
		if (StringUtils.isBlank(token)) {
			throw new AppIllegalArgumentException("token must not be null");
		}
		String captchaText = CaptchaUtils.generateCaptchaText();
		String key = redisNameSpaceKey.getKey(RedisNameSpaceKey.CAPTCHA, token);
		if (BooleanUtils.isNotTrue(redisTemplate.opsForValue().setIfAbsent(key, captchaText, timeout))) {
			throw new AppIllegalArgumentException("token already in used");
		}
		response.setDateHeader("Expires", 0L);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		CaptchaUtils.writeCaptcha(captchaText, response.getOutputStream());
	}

	@Override
	public String getAlreadyCaptchaText(String token) {
		return redisTemplate.opsForValue().get(redisNameSpaceKey.getKey(RedisNameSpaceKey.CAPTCHA, token));
	}

	@Override
	public boolean validated(String token, String captchaText) {
		String key = redisNameSpaceKey.getKey(RedisNameSpaceKey.CAPTCHA, token);
		String alreadyCaptchaText = redisTemplate.opsForValue().get(key);
		if (null == alreadyCaptchaText) {
			throw new AppIllegalArgumentException("验证码已失效");
		}
		redisTemplate.delete(key);
		LOGGER.info("输入验证码: {}, 服务器验证码：{}", captchaText, alreadyCaptchaText);
		return null != captchaText && captchaText.equalsIgnoreCase(alreadyCaptchaText);
	}

}

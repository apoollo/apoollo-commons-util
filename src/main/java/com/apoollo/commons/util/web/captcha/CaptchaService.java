/**
 * 
 */
package com.apoollo.commons.util.web.captcha;

import java.io.IOException;
import java.time.Duration;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author liuyulong
 */
public interface CaptchaService {

    public void writeCaptchaImage(HttpServletResponse response,String token, Duration timeout) throws IOException;

    public String getAlreadyCaptchaText(String token);

    public boolean validated(String token, String captchaText);
}

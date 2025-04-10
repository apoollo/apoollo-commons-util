package com.apoollo.commons.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * @author liuyulong
 */
public class CaptchaUtils {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 30;
    private static final int CHAR_COUNT = 4; // 验证码字符数

    public static void writeCaptcha(String captchaText, OutputStream outputStream) throws IOException {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();

        // 背景颜色
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // 边框
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        // 字体
        Font font = new Font("Arial", Font.BOLD, 20);
        g2d.setFont(font);

        // 文字
        g2d.setColor(Color.BLUE);
        for (int i = 0, n = captchaText.length(); i < n; i++) {
            char c = captchaText.charAt(i);
            float x = (float) (i * WIDTH / n + (WIDTH / n - g2d.getFontMetrics().charWidth(c)) / 2.0);
            float y = (float) (HEIGHT / 2 + g2d.getFontMetrics().getAscent() / 2.0);
            g2d.drawString(String.valueOf(c), x, y);
        }

        // 干扰线
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g2d.drawLine(x1, y1, x2, y2);
        }

        // 输出图像
        ImageIO.write(image, "JPEG", outputStream);
    }

    public static String generateCaptchaText() {
        return generateCaptchaText(CHAR_COUNT);
    }

    public static String generateCaptchaText(int length) {
        String chars = "ABDEFGHJKLMNQRTUVWXYabcdefghijklmnopqrstuvwxyz23456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

}
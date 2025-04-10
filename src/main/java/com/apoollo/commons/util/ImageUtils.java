/**
 * 
 */
package com.apoollo.commons.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuyulong
 * @since 2023年8月7日
 */
public class ImageUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);

	public static String getImageType(InputStream inputStream) {
		String formatName = null;
		try (ImageInputStream image = ImageIO.createImageInputStream(inputStream);) {
			Iterator<ImageReader> readers = ImageIO.getImageReaders(image);

			if (readers.hasNext()) {
				formatName = readers.next().getFormatName();
			}
		} catch (IOException e) {
			LOGGER.error("getImageType error:", e);
		}
		return formatName;
	}

	public static String getImageType(byte[] bytes) {
		return getImageType(new ByteArrayInputStream(bytes));
	}

	public static String getImageTypeSafety(String base64Image) {
		String imageType = null;
		try {
			imageType = getImageType(base64Image);
		} catch (Exception e) {
			LOGGER.error("getImageTypeSafety error:", e);
		}
		return imageType;
	}

	public static String getImageType(String base64Image) {
		return getImageType(Base64.getDecoder().decode(base64Image.getBytes()));
	}

	public static int getPixel(byte[] bytes) {
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			return width * height;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static int getPixel(String base64Image) {
		return getPixel(Base64.getDecoder().decode(base64Image.getBytes()));
	}

}

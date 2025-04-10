/**
 * 
 */
package com.apoollo.commons.util;

import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.exception.AppHttpCodeMessageException;
import com.apoollo.commons.util.model.MinMax;
import com.apoollo.commons.util.model.StorageUnit;
import com.apoollo.commons.util.request.context.HttpCodeName;

/**
 * @author liuyulong
 */
public class ServiceUtils {

	public static void validateSize(Number targetSize, StorageUnit targetSizeStorageUnit, double maxSize,
			StorageUnit maxSizeStorageUnit, HttpCodeName<String, String> httpCodeName, String targetName) {
		Double maxByteSize = maxSizeStorageUnit.getByteCount() * maxSize;
		Double targetByteSize = targetSizeStorageUnit.getByteCount() * targetSize.doubleValue();
		Double targetConvertedSize = LangUtils.getStorageSize(maxSizeStorageUnit, targetByteSize);
		Assert.isTrue(LangUtils.inRange(new MinMax<>(1D, maxByteSize), targetByteSize), httpCodeName, targetName,
				" size must less than or equal [" + maxSize + maxSizeStorageUnit.getCode() + "], your size is [",
				targetConvertedSize + maxSizeStorageUnit.getCode() + "]");
	}

	public static void validateSize(byte[] bytes, double maxSize, StorageUnit maxSizeStorageUnit,
			HttpCodeName<String, String> codeName, String targetName) {
		validateSize(bytes.length, StorageUnit.BYTE, maxSize, maxSizeStorageUnit, codeName, targetName);
	}

	public static String validateImageType(byte[] bytes, String[] supportTypes,
			HttpCodeName<String, String> httpCodeName, String targetName) {
		String imageType = ImageUtils.getImageType(bytes);
		boolean isSupport = LangUtils.getStream(supportTypes)
				.filter(supportType -> supportType.equalsIgnoreCase(imageType)).findFirst().isPresent();
		String supportImageTypes = StringUtils.join(LangUtils.getStream(supportTypes).collect(Collectors.toList()),
				"、");
		Assert.isTrue(isSupport, httpCodeName, targetName,
				" image type must in (" + supportImageTypes + "), your image type is " + imageType);
		return imageType;
	}

	public static byte[] base64Decode(String base64, HttpCodeName<String, String> httpCodeName,
			String messageCompileArg) {
		try {
			return Base64Utils.decode(base64);
		} catch (Exception e) {
			if (null == messageCompileArg) {
				throw new AppHttpCodeMessageException(httpCodeName, null);
			} else {
				throw new AppHttpCodeMessageException(httpCodeName, new Object[] { messageCompileArg });
			}
		}
	}

}

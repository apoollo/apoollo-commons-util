/**
 * 
 */
package com.apoollo.commons.util.request.context.def;

import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.crypto.symmetric.SM4;
import com.apoollo.commons.util.crypto.symmetric.SymmetricEncryption;
import com.apoollo.commons.util.exception.AppIllegalArgumentException;
import com.apoollo.commons.util.request.context.SignatureDecryptor;

/**
 * @author liuyulong
 * @since 2025-05-21
 */
public class DefaultSignatureDecryptor implements SignatureDecryptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSignatureDecryptor.class);

	private SymmetricEncryption symmetricEncryption;
	private String secret;

	public DefaultSignatureDecryptor() {
		this(null);
	}

	public DefaultSignatureDecryptor(String secret) {
		super();
		this.secret = secret;
		symmetricEncryption = new SM4();
	}

	@Override
	public byte[] decrypt(String signature, String secret) {
		String targetSecret = LangUtils.defaultString(secret, this.secret);
		if (StringUtils.isBlank(targetSecret)) {
			throw new RuntimeException("secret must not be blank");
		}
		try {
			return symmetricEncryption.decrypt(Hex.decode(targetSecret), Base64.getDecoder().decode(signature));
		} catch (Exception e) {
			LOGGER.error("signature decrypt  failed:", e);
			throw new AppIllegalArgumentException("signature decrypt  failed");
		}
	}

}

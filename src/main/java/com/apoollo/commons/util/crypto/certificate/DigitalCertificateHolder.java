/**
 * 
 */
package com.apoollo.commons.util.crypto.certificate;

import java.security.KeyPair;

import org.bouncycastle.cert.X509CertificateHolder;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong6
 *
 */
@Getter
@Setter
public class DigitalCertificateHolder {

	private KeyPair keyPair;
	private X509CertificateHolder x509CertificateHolder;

	private String privateKeyChiphertextPassword;
	private String pKCS8EncryptedPrivateKeyPemFormat;
	private String plainTextPublicKeyPemFormat;
	private String plainTextCertificatePemFormat;
}

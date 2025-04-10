/**
 * 
 */
package com.apoollo.commons.util.crypto.certificate;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMEncryptor;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8EncryptorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfoBuilder;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.util.io.pem.PemGenerationException;

import com.apoollo.commons.util.IpUtils;

/**
 * @author liuyulong6
 *
 */
public class X509CertificateOperator {

    /**
     * 
     */
    public X509CertificateOperator() {
        init();
    }

    public void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public KeyFactory getKeyFactory() throws NoSuchAlgorithmException, NoSuchProviderException {
        return KeyFactory.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
    }

    public PublicKey getPublicKey(String publicKeyPem)
            throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        SubjectPublicKeyInfo subjectPublicKeyInfo = this.parsePem(publicKeyPem, SubjectPublicKeyInfo.class);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(subjectPublicKeyInfo.getEncoded());
        PublicKey publicKey = this.getKeyFactory().generatePublic(x509EncodedKeySpec);
        return publicKey;
    }

    public KeyPairGenerator getKeyPairGenerator() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
        keyPairGenerator.initialize(2048);
        return keyPairGenerator;
    }

    public X500NameBuilder getX500NameBuilder(DigitalCertificateParameter digitalCertificateParameter) {
        X500NameBuilder x500NameBuilder = new X500NameBuilder();

        // common name : 通用名称。通常是需要证书签名的完全合格域名 ;如: *.wikipedia.org
        x500NameBuilder.addRDN(BCStyle.CN, digitalCertificateParameter.getCnOfX500Name());

        // organization : 商业名字, 通常是法律上的商业名字; 如: Wikimedia Foundation, Inc.
        x500NameBuilder.addRDN(BCStyle.O, digitalCertificateParameter.getOOfX500Name());

        // organizational unit name : 部门名字 ,如HR, Finance, IT; 可选
        if (StringUtils.isNotBlank(digitalCertificateParameter.getOuOfX500Name())) {
            x500NameBuilder.addRDN(BCStyle.OU, digitalCertificateParameter.getOuOfX500Name());
        }
        // country code: 国家名字 两字母ISO国家代码 US
        x500NameBuilder.addRDN(BCStyle.C, digitalCertificateParameter.getCOfX500Name());

        // state, or province name: 省、州 不应该使用缩写,如 California
        x500NameBuilder.addRDN(BCStyle.ST, digitalCertificateParameter.getStOfX500Name());

        // locality name: 城镇 ; 可选
        if (StringUtils.isNotBlank(digitalCertificateParameter.getLOfX500Name())) {
            x500NameBuilder.addRDN(BCStyle.L, digitalCertificateParameter.getLOfX500Name());
        }

        // Email address: Email地址 该组织中的证书管理人的email地址 ; 可选
        if (StringUtils.isNotBlank(digitalCertificateParameter.getEmailOfX500Name())) {
            x500NameBuilder.addRDN(BCStyle.EmailAddress, digitalCertificateParameter.getEmailOfX500Name());
        }
        return x500NameBuilder;
    }

    public X509v3CertificateBuilder getRootX509v3CertificateBuilder(
            DigitalCertificateParameter digitalCertificateParameter, PublicKey publicKey) throws IOException {
        return getX509v3CertificateBuilder(null, digitalCertificateParameter, publicKey);
    }

    public X509v3CertificateBuilder getX509v3CertificateBuilder(X500Name issuer,
            DigitalCertificateParameter digitalCertificateParameter, PublicKey publicKey) throws IOException {
        X500Name subject = getX500NameBuilder(digitalCertificateParameter).build();
        boolean isRootBuilder = null == issuer;
        if (isRootBuilder) {
            issuer = subject;
        }
        SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfoFactory
                .createSubjectPublicKeyInfo(PublicKeyFactory.createKey(publicKey.getEncoded()));
        X509v3CertificateBuilder x509v3CertificateBuilder = new X509v3CertificateBuilder(issuer,
                digitalCertificateParameter.getSerial(), digitalCertificateParameter.getNotBefore(),
                digitalCertificateParameter.getNotAfter(), digitalCertificateParameter.getDateLocale(), subject,
                publicKeyInfo);
        x509v3CertificateBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(isRootBuilder));

        if (IpUtils.matchesIp(digitalCertificateParameter.getCnOfX500Name())) {
            x509v3CertificateBuilder.addExtension(Extension.subjectAlternativeName, false,
                    new GeneralNames(new GeneralName[] {
                            new GeneralName(GeneralName.iPAddress, digitalCertificateParameter.getCnOfX500Name()) }));
        }
        return x509v3CertificateBuilder;
    }

    public X509CertificateHolder sign(X509v3CertificateBuilder x509v3CertificateBuilder, PrivateKey rootKey)
            throws OperatorCreationException {
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").build(rootKey);
        X509CertificateHolder certificateHolder = x509v3CertificateBuilder.build(signer);
        return certificateHolder;
    }

    // generate root certificate and sign
    public X509CertificateHolder getSignerRootX509CertificateHolder(KeyPair rootKeyPair,
            DigitalCertificateParameter rootDigitalCertificateParameter) throws OperatorCreationException, IOException {
        return getSignerX509CertificateHolder(null, rootKeyPair.getPrivate(), rootKeyPair.getPublic(),
                rootDigitalCertificateParameter);
    }

    // generate certificate and sign
    public X509CertificateHolder getSignerX509CertificateHolder(X500Name issuer, PrivateKey issuerPrivateKey,
            PublicKey subjectPublicKey, DigitalCertificateParameter subjectDigitalCertificateParameter)
            throws IOException, OperatorCreationException {
        X509v3CertificateBuilder x509v3CertificateBuilder = getX509v3CertificateBuilder(issuer,
                subjectDigitalCertificateParameter, subjectPublicKey);
        X509CertificateHolder x509CertificateHolder = sign(x509v3CertificateBuilder, issuerPrivateKey);
        return x509CertificateHolder;
    }

    public String toPem(Object object, PEMEncryptor encryptor) {
        try (//
                StringWriter writer = new StringWriter(); //
                JcaPEMWriter pemWriter = new JcaPEMWriter(writer);//
        ) {
            pemWriter.writeObject(object, encryptor);
            pemWriter.flush();
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String toPem(Object object) {
        return toPem(object, null);
    }

    public <T> T parsePem(String pemText, Class<T> clazz) {
        try (//
                StringReader reader = new StringReader(pemText); //
                PEMParser pemParser = new PEMParser(reader);//
        ) {
            return clazz.cast(pemParser.readObject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public PKCS8EncryptedPrivateKeyInfo getPKCS8EncryptedPrivateKeyInfo(PrivateKey privateKey,
            ASN1ObjectIdentifier algorithm, String password) throws OperatorCreationException {
        PKCS8EncryptedPrivateKeyInfoBuilder pkcs8EncryptedPrivateKeyInfoBuilder = new PKCS8EncryptedPrivateKeyInfoBuilder(
                privateKey.getEncoded());
        JceOpenSSLPKCS8EncryptorBuilder openSSLPKCS8EncryptorBuilder = new JceOpenSSLPKCS8EncryptorBuilder(algorithm);
        openSSLPKCS8EncryptorBuilder.setPassword(password.toCharArray());
        OutputEncryptor outputEncryptor = openSSLPKCS8EncryptorBuilder.build();
        return pkcs8EncryptedPrivateKeyInfoBuilder.build(outputEncryptor);
    }

    public String toPKCS8EncryptedPrivateKeyPem(PrivateKey privateKey) throws PemGenerationException {
        JcaPKCS8Generator pkcs8Generator = new JcaPKCS8Generator(privateKey, null);
        return toPem(pkcs8Generator);
    }

    public String toPKCS8EncryptedPrivateKeyPem(PrivateKey privateKey, String password)
            throws OperatorCreationException {
        PKCS8EncryptedPrivateKeyInfo pkcs8EncryptedPrivateKeyInfo = getPKCS8EncryptedPrivateKeyInfo(privateKey,
                PKCS8Generator.PBE_SHA1_3DES, password);
        return toPem(pkcs8EncryptedPrivateKeyInfo);
    }

    public PrivateKey parseToPrivateKey(String pkcs8EncryptedPrivateKeyPem, String password)
            throws PKCSException, OperatorCreationException, PEMException {
        PKCS8EncryptedPrivateKeyInfo pkcs8EncryptedPrivateKeyInfo = parsePem(pkcs8EncryptedPrivateKeyPem,
                PKCS8EncryptedPrivateKeyInfo.class);
        JceOpenSSLPKCS8DecryptorProviderBuilder jceOpenSSLPKCS8DecryptorProviderBuilder = new JceOpenSSLPKCS8DecryptorProviderBuilder();
        InputDecryptorProvider inputDecryptorProvider = jceOpenSSLPKCS8DecryptorProviderBuilder
                .build(password.toCharArray());
        PrivateKeyInfo privateKeyInfo = pkcs8EncryptedPrivateKeyInfo.decryptPrivateKeyInfo(inputDecryptorProvider);

        return new JcaPEMKeyConverter().getPrivateKey(privateKeyInfo);
    }

    public boolean signatureVerify(PublicKey issuerPublicKey, X509CertificateHolder subjectCertificate)
            throws SignatureException, IOException, InvalidKeyException, NoSuchAlgorithmException {
        Signature signature = Signature.getInstance(subjectCertificate.getSignatureAlgorithm().getAlgorithm().getId());
        signature.initVerify(issuerPublicKey);
        signature.update(subjectCertificate.toASN1Structure().getTBSCertificate().getEncoded());
        return signature.verify(subjectCertificate.toASN1Structure().getSignature().getBytes());
    }

    public boolean certificateVerify(PublicKey issuerPublicKey, X509CertificateHolder subjectCertificate)
            throws InvalidKeyException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException,
            SignatureException {
        new JcaX509CertificateConverter().getCertificate(subjectCertificate).verify(issuerPublicKey);
        return true;
    }

    public DigitalCertificateHolder getRootDigitalCertificateHolder(String privateKeyChiphertextPassword,
            DigitalCertificateParameter rootDigitalCertificateParameter) {
        try {
            KeyPairGenerator keyPairGenerator = getKeyPairGenerator();
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            String privateKeyPem = toPKCS8EncryptedPrivateKeyPem(keyPair.getPrivate(), privateKeyChiphertextPassword);
            String publicKeyPem = toPem(keyPair.getPublic());
            X509CertificateHolder certificateHolder = getSignerRootX509CertificateHolder(keyPair,
                    rootDigitalCertificateParameter);
            String certificatePem = toPem(certificateHolder);
            DigitalCertificateHolder digitalCertificateHolder = new DigitalCertificateHolder();
            digitalCertificateHolder.setKeyPair(keyPair);
            digitalCertificateHolder.setPKCS8EncryptedPrivateKeyPemFormat(privateKeyPem);
            digitalCertificateHolder.setPlainTextCertificatePemFormat(certificatePem);
            digitalCertificateHolder.setPlainTextPublicKeyPemFormat(publicKeyPem);
            digitalCertificateHolder.setPrivateKeyChiphertextPassword(privateKeyChiphertextPassword);
            digitalCertificateHolder.setX509CertificateHolder(certificateHolder);
            return digitalCertificateHolder;

        } catch (NoSuchAlgorithmException | NoSuchProviderException | OperatorCreationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DigitalCertificateHolder getDigitalCertificateHolder(String privateKeyChiphertextPassword, X500Name issuer,
            PrivateKey issuerPrivateKey, DigitalCertificateParameter subjectDigitalCertificateParameter) {
        try {
            KeyPairGenerator keyPairGenerator = getKeyPairGenerator();
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            String privateKeyPem = toPKCS8EncryptedPrivateKeyPem(keyPair.getPrivate(), privateKeyChiphertextPassword);
            String publicKeyPem = toPem(keyPair.getPublic());
            X509CertificateHolder certificateHolder = getSignerX509CertificateHolder(issuer, issuerPrivateKey,
                    keyPair.getPublic(), subjectDigitalCertificateParameter);
            String certificatePem = toPem(certificateHolder);
            DigitalCertificateHolder digitalCertificateHolder = new DigitalCertificateHolder();
            digitalCertificateHolder.setKeyPair(keyPair);
            digitalCertificateHolder.setPKCS8EncryptedPrivateKeyPemFormat(privateKeyPem);
            digitalCertificateHolder.setPlainTextCertificatePemFormat(certificatePem);
            digitalCertificateHolder.setPlainTextPublicKeyPemFormat(publicKeyPem);
            digitalCertificateHolder.setPrivateKeyChiphertextPassword(privateKeyChiphertextPassword);
            digitalCertificateHolder.setX509CertificateHolder(certificateHolder);
            return digitalCertificateHolder;

        } catch (NoSuchAlgorithmException | NoSuchProviderException | OperatorCreationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}

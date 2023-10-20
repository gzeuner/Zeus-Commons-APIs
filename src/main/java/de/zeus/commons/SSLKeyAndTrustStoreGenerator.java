package de.zeus.commons;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Scanner;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;

/**
 * A utility class for generating SSL key and trust stores.
 */
public class SSLKeyAndTrustStoreGenerator {

    /**
     * The main entry point of the application.
     *
     * @param args Command-line arguments (none required).
     */
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        try {
            Scanner scanner = new Scanner(System.in);
            String[] userInputs = getUserInputs(scanner);
            KeyPair keyPair = generateKeyPair();
            X509Certificate certificate = generateSelfSignedCertificate(keyPair);
            createAndSaveKeyStore(userInputs[0], userInputs[2], keyPair, certificate);
            createAndSaveTrustStore(userInputs[1], userInputs[3], certificate);

            System.out.println("Keystore and Truststore have been successfully generated.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get user inputs for generating and saving key and trust stores.
     *
     * @param scanner Scanner object for reading console input.
     * @return An array containing keyStorePassword, trustStorePassword, keyStorePath, and trustStorePath.
     */
    private static String[] getUserInputs(Scanner scanner) {
        System.out.print("Enter the password for the Keystore: ");
        String keyStorePassword = scanner.nextLine();

        System.out.print("Enter the password for the Truststore: ");
        String trustStorePassword = scanner.nextLine();

        System.out.print("Enter the path to save the Keystore (e.g., keystore.jks): ");
        String keyStorePath = scanner.nextLine();

        System.out.print("Enter the path to save the Truststore (e.g., truststore.jks): ");
        String trustStorePath = scanner.nextLine();

        return new String[]{keyStorePassword, trustStorePassword, keyStorePath, trustStorePath};
    }

    /**
     * Generate RSA key pair.
     *
     * @return Generated KeyPair.
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(2048, new SecureRandom());
        System.out.println("Generating RSA key pair...");
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Generate a self-signed X509 Certificate.
     *
     * @param keyPair RSA key pair.
     * @return Generated X509Certificate.
     * @throws Exception
     */
    private static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        X500Principal dnName = new X500Principal("CN=Test CA Certificate");
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date validityBeginDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24);
        Date validityEndDate = new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365 * 10)); // 10 years

        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(dnName, serial, validityBeginDate, validityEndDate, dnName, keyPair.getPublic());

        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSAEncryption").build(keyPair.getPrivate());

        return new JcaX509CertificateConverter().getCertificate(certBuilder.build(contentSigner));
    }

    /**
     * Create a JKS KeyStore, populate it with a private key and certificate, and save it to disk.
     *
     * @param password The password for the keystore.
     * @param path The file path to save the keystore.
     * @param keyPair The KeyPair to include.
     * @param cert The certificate to include.
     * @throws Exception
     */
    private static void createAndSaveKeyStore(String password, String path, KeyPair keyPair, X509Certificate cert) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        keyStore.setKeyEntry("mykey", keyPair.getPrivate(), password.toCharArray(), new X509Certificate[]{cert});

        try (FileOutputStream fos = new FileOutputStream(path)) {
            keyStore.store(fos, password.toCharArray());
        }

        System.out.println("Keystore saved at " + path);
    }

    /**
     * Create a JKS TrustStore, populate it with a certificate, and save it to disk.
     *
     * @param password The password for the truststore.
     * @param path The file path to save the truststore.
     * @param cert The certificate to include.
     * @throws Exception
     */
    private static void createAndSaveTrustStore(String password, String path, X509Certificate cert) throws Exception {
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(null, null);
        trustStore.setCertificateEntry("mykey", cert);

        try (FileOutputStream fos = new FileOutputStream(path)) {
            trustStore.store(fos, password.toCharArray());
        }

        System.out.println("Truststore saved at " + path);
    }
}

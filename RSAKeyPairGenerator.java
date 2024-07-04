import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAKeyPairGenerator {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        // 使用RSA算法初始化密钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        // 初始化密钥长度为2048位
        keyPairGenerator.initialize(2048);

        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 获取公钥和私钥
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // 将公钥和私钥转换为Base64编码的字符串
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        // 将公钥和私钥保存到文件中
        saveKeyToFile(publicKeyString, "public_key.pem");
        saveKeyToFile(privateKeyString, "private_key.pem");

        System.out.println("Public Key: " + publicKeyString);
        System.out.println("Private Key: " + privateKeyString);
    }

    private static void saveKeyToFile(String keyString, String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(keyString.getBytes());
        }
    }
}

package pe.com.foxsoft.ballartelyweb.spring.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

/**
 * Created by Javier Talledo on 02/11/2017.
 */
public class EncriptacionUtil {

    // Definición del tipo de algoritmo a utilizar (AES, DES, RSA, Blowfish)
    private final static String alg = "Blowfish";
    // Definición del modo de cifrado a utilizar
    private final static String cI = "Blowfish/ECB/PKCS5Padding";

    /**
     * Función de tipo String que recibe una llave (key)
     * y el texto que se desea cifrar
     * @param key la llave en tipo String a utilizar
     * @param texto el texto sin cifrar a encriptar
     * @return el texto cifrado en modo String
     * @throws Exception puede devolver excepciones de los siguientes tipos: NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException
     */
    public static String encriptar(String key, String texto) throws BallartelyException {
        try {
        	Cipher cipher = Cipher.getInstance(cI);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), alg);

            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(texto.getBytes());
            return new String(encodeBase64(encrypted));
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
    }

    /**
     * Función de tipo String que recibe una llave (key)
     * y el texto que se desea descifrar
     * @param key la llave en tipo String a utilizar
     * @param encrypted el texto cifrado en modo String
     * @return el texto desencriptado en modo String
     * @throws Exception puede devolver excepciones de los siguientes tipos: NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException
     */
    public static String desencriptar(String key, String encrypted) throws BallartelyException {
        try {
        	Cipher cipher = Cipher.getInstance(cI);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), alg);

            byte[] enc = decodeBase64(encrypted);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decrypted = cipher.doFinal(enc);
            return new String(decrypted);
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
    }

}

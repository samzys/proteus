package proteus.gwt.server.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Provide security encode & decode methods
 * @author Thomas
 *
 */
public final class Cryptography {
	// singleton
	public static Cryptography getInstance() {
		return (Cryptography) SingletonRegistry.REGISTRY.getInstanceOf(Cryptography.class.getName());
	}
    public String getMD5Hash(String str) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(str.getBytes());
            byte[] result = md5.digest();

            StringBuffer hexStringBuff = new StringBuffer();
            for (int i=0; i<result.length; i++) {
                String hexString = Integer.toHexString(0xFF & result[i]);
                if (hexString.length() % 2 != 0) {
                    // Pad with 0
                    hexString = "0"+hexString;
                }
                hexStringBuff.append(hexString);
            }
            return hexStringBuff.toString();
        } catch (NoSuchAlgorithmException ex) {
        }
        return null;
    }

}

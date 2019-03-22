import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	private static String getHash(String txt, String hashType) {
		try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
	        byte[] array = md.digest(txt.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	       }
	        return sb.toString();
	    } catch (java.security.NoSuchAlgorithmException e) {
        return null;
	    }
	}

	public static String md2(String txt) {
		return Hash.getHash(txt, "MD2");
	}
	public static String md5(String txt) {
		return Hash.getHash(txt, "MD5");
	}
	public static String sha1(String txt) {
		return Hash.getHash(txt, "SHA1");
	}
	public static String sha224(String txt) {
		return Hash.getHash(txt, "SHA-224");
	}
	public static String sha256(String txt) {
		return Hash.getHash(txt, "SHA-256");
	}
	public static String sha384(String txt) {
		return Hash.getHash(txt, "SHA-384");
	}
	public static String sha512(String txt) {
		return Hash.getHash(txt, "SHA-512");
	}
}
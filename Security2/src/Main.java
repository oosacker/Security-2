import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;


public class Main {

	ObjectOutputStream oout;
	ObjectInputStream ois;
	Cipher cipher;
	
	public void generateKeys() throws Exception {
		
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024);
		KeyPair pair = keyPairGen.generateKeyPair();	
		
		PrivateKey pri = pair.getPrivate();
		PublicKey pub = pair.getPublic();
		
        oout = new ObjectOutputStream(new FileOutputStream("privatekey"));
        oout.writeObject(pri);
        oout.close();
        
        oout = new ObjectOutputStream(new FileOutputStream("publickey"));
        oout.writeObject(pub);
        oout.close();
	}
	
	public KeyPair readKeys() throws Exception{
		
		ois = new ObjectInputStream(new FileInputStream("privatekey"));
		PrivateKey pri = (PrivateKey) ois.readObject();

		ois = new ObjectInputStream(new FileInputStream("publickey"));
		PublicKey pub = (PublicKey) ois.readObject();
		
		return new KeyPair(pub, pri);
			
	}
	
	public byte[] encrypt(byte[] plainText, Key key) throws Exception {
		
		cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.ENCRYPT_MODE, key);
        
        return cipher.doFinal(plainText);
        
	}
	
	public byte[] decrypt(byte[] cipherText, Key key) throws Exception {
		
		cipher.init(Cipher.DECRYPT_MODE,key, cipher.getParameters());
         
        return cipher.doFinal(cipherText);    
    
	}
	
	public Main() throws Exception {
		
		generateKeys();
		
		KeyPair pair = readKeys();
		
		PublicKey pub = pair.getPublic();
		PrivateKey pri = pair.getPrivate();
		
		String msg = "This is the original message that I will encode!!!";
		System.out.println("Original: " +msg);
		
		byte[] coded = encrypt(msg.getBytes(), pub);
		System.out.println("Encoded: " +new String(coded, 0, coded.length));
		
		byte[] decoded = decrypt(coded, pri);
		System.out.println("Decoded: " +new String(decoded, 0, decoded.length));
		
	}
	
	public static void main(String[] args) throws Exception {
		new Main();
	}
	

}

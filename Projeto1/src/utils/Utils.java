package utils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;

import Server.Peer;

public class Utils {
	public static byte[] concatenateArrays(byte[] header,byte[] body) {
		byte[] combined = new byte[header.length + body.length];

		System.arraycopy(header,0,combined,0,header.length);
		System.arraycopy(body,0,combined,header.length,body.length);
		return combined;
	}
	
	public static synchronized String getFileId(File file) {
		String fileInfo= file.getName()+file.lastModified()+Peer.peerID;	
		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(fileInfo.getBytes(StandardCharsets.UTF_8));
			String encoded = Base64.getEncoder().encodeToString(hash);
			return encoded;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static synchronized byte[] getFileData(File file) {
		byte[] data = new byte[(int) file.length()];
		try {
			FileInputStream inputStream = new FileInputStream(file);
			inputStream.read(data);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static void threadSleep(int time) {
		Random ran = new Random();
		int sleepTime = ran.nextInt(time);
		try {
			Thread.sleep(sleepTime);
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}
}

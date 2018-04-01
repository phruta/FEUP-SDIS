package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;

public class Utils {
	public static byte[] concatenateArrays(byte[] header, byte[] body) {
		byte[] combined = new byte[header.length + body.length];

		System.arraycopy(header, 0, combined, 0, header.length);
		System.arraycopy(body, 0, combined, header.length, body.length);
		return combined;
	}

	public static synchronized String getFileId(File file) {
		String fileInfo = file.getName() + file.lastModified() + file.getPath();

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(fileInfo.getBytes(StandardCharsets.UTF_8));
			StringBuffer result = new StringBuffer();

			for (byte b : hash)
				result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

			return result.toString().toUpperCase();
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

	public static void threadRandomSleep(int time) {
		Random ran = new Random();
		int sleepTime = ran.nextInt(time);
		threadSleep(sleepTime);
	}

	public static void threadSleep(int sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

	public static void save(Object obj, String path) {
		try {
			new File(path).getParentFile().mkdirs(); // if file already exists will do nothing
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
		} catch (Exception i) {
			i.getMessage();
			i.printStackTrace();
		}
	}

	public static Object load(String path) {

		try {
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);

			Object to_load = ois.readObject();
			ois.close();
			fis.close();
			return to_load;
		} catch (Exception e) {
			return null;
		}
	}

	public final static void clearConsole() {
		try {
			final String os = System.getProperty("os.name");

			if (os.contains("Windows")) {
				Runtime.getRuntime().exec("cls");
			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (final Exception e) {
			e.getMessage();
		}
	}

	public static File getFile(String fileStr) {
		File file = new File(fileStr);

		if (!file.exists() || !file.isFile()) {
			System.out.println("ERROR: The path you indicated is not a file");
			return null;
		}

		return file;
	}
}

package files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import utils.Convert;

public class RestoredFile {
	String fileID;
	String fileName;
	HashMap<Integer,byte[]> data = new HashMap<>();
	
	public String getFileID() {
		return fileID;
	}

	public RestoredFile(String fileID, String fileName) {
		super();
		this.fileID = fileID;
		this.fileName = fileName;
	}
	
	public RestoredFile(String fileID) {
		super();
		this.fileID = fileID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileID == null) ? 0 : fileID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestoredFile other = (RestoredFile) obj;
		if (fileID == null) {
			if (other.fileID != null)
				return false;
		} else if (!fileID.equals(other.fileID))
			return false;
		return true;
	}

	public String getFileName() {
		return fileName;
	}

	public synchronized void addData(int chunkNo, String body) {
		data.put(chunkNo, body.getBytes());
	}
	public synchronized int dataSize() {
		return data.size();
	}
	public synchronized boolean saveFile() {
		byte[] file= new byte[0];
		for(int i: data.keySet())
			file = Convert.concatenateArrays(file, data.get(i));
		try {
			Files.write(new File("../RestoredFiles/"+fileName).toPath(), file);
		} catch (IOException e) {
			e.getMessage();
			e.printStackTrace();
			return false;
		
		}
		return true;
	}
}
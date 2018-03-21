package files;

import java.util.ArrayList;

public class Chunk {
	
private String fileID;
private int ChunkNum;
private ArrayList<Byte> FileInfo= new ArrayList<Byte>();


public Chunk(String fileID, int chunkNum, ArrayList<Byte> fileInfo) {
	super();
	this.fileID = fileID;
	ChunkNum = chunkNum;
	FileInfo = fileInfo;
}

public String getFileID() {
	return fileID;
}
public void setFileID(String fileID) {
	this.fileID = fileID;
}
public int getChunkNum() {
	return ChunkNum;
}
public void setChunkNum(int chunkNum) {
	ChunkNum = chunkNum;
}
public ArrayList<Byte> getFileInfo() {
	return FileInfo;
}
public void setFileInfo(ArrayList<Byte> fileInfo) {
	FileInfo = fileInfo;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ChunkNum;
	result = prime * result + ((FileInfo == null) ? 0 : FileInfo.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (!(obj instanceof Chunk))
		return false;
	Chunk other = (Chunk) obj;
	if (ChunkNum != other.ChunkNum)
		return false;
	if (FileInfo == null) {
		if (other.FileInfo != null)
			return false;
	} else if (!FileInfo.equals(other.FileInfo))
		return false;
	return true;
}
}

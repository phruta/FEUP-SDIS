package files;

import java.util.HashMap;

public class DataBase {
    private HashMap<String, File> files = new HashMap<String, File>();
    private int maxContentSize = 64000 * 300; //300 chunks com tamanho 64kB


    private static DataBase instance = null;

    public static DataBase instance() {

        if(instance == null){
            synchronized (DataBase.class) {
                if(instance == null){
                    instance = new DataBase();
                }
            }
        }

        return instance;
    }

	public HashMap<String, File> getFiles() {
		return files;
	}

	public int getMaxContentSize() {
		return maxContentSize;
	}

	public void setMaxContentSize(int maxContentSize) {
		this.maxContentSize = maxContentSize;
	}

	public DataBase() {
		super();
	}
}

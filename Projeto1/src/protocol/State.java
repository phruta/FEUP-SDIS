package protocol;

import files.DataBase;
import files.DiskSpace;
import utils.Utils;

public class State implements Runnable {

	@Override
	public void run() {
		String str = "\n" + DataBase.getInstance().toString() + "\n" + DiskSpace.getInstance().toString();
		Utils.clearConsole();
		System.out.print(str);
	}

}

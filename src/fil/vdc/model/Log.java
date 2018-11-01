package fil.vdc.model;
import java.io.*;
public class Log {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Log log = new Log();
		int i = 1;
		log.info("Hello!" + i);
	}

	public void info(String messeage) {
		File logFile = new File("log.txt");
		if(!logFile.exists())
			try {
				logFile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(logFile, true));
			bw.write(messeage);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

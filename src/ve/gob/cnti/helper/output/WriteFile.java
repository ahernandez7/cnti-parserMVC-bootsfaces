package ve.gob.cnti.helper.output;

import java.io.FileWriter;
import java.io.PrintWriter;

public class WriteFile {
	
	private FileWriter fichero;
	private PrintWriter pw;
	
	public WriteFile(){
		this.fichero = null;
		this.pw = null;
	}
	
	public void writeFile(String file, String text) {

		try {
			this.fichero = new FileWriter(file);
			this.pw = new PrintWriter(fichero);

			this.pw.print(text);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != this.fichero) {
					this.fichero.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}

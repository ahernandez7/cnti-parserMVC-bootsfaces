package ve.gob.cnti.helper.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ReadFile {
	
	File file;
	FileReader fr;
	BufferedReader br;

	public ReadFile() {
		this.file = null;
		this.fr = null;
		this.br = null;
	}

	public String readFile(String pathFile) {
		StringBuffer sb = new StringBuffer();
		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			this.file = new File(pathFile);
			this.fr = new FileReader(file);
			this.br = new BufferedReader(fr);

			// Lectura del fichero
			String linea = null;
		
			while ((linea = br.readLine()) != null){
				sb.append(linea).append("\n");				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta
			// una excepcion.
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return sb.toString();
	}
}

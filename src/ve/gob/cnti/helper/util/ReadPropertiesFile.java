package ve.gob.cnti.helper.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadPropertiesFile {

	Properties propiedades;
	InputStream entrada = null;

	public ReadPropertiesFile(String file) {
		this.propiedades = new Properties();
		
		try {
			this.entrada = new FileInputStream(file);
			// cargamos el archivo de propiedades
			this.propiedades.load(entrada);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getProperty(String property) {
		return this.propiedades.getProperty(property);
	}
		

}

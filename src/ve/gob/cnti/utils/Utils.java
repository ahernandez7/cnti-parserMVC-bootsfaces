package ve.gob.cnti.utils;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JOptionPane;

import ve.gob.cnti.windows.swing.Config;
import ve.gob.cnti.windows.swing.ConfigForm;

public class Utils {

	/**
	 * Ubica el componente dentro de la vista
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return GridBagConstraints
	 */
	public GridBagConstraints limitarComponente(int x, int y, int w, int h) {

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;

		return constraints;
	}

	/**
	 * Ventana de mensaje
	 * 
	 * @param comp
	 *            Ventana
	 * @param msj
	 *            Mensaje de la ventana
	 * @param title
	 *            Titulo de la ventana
	 * @param opc
	 *            Tipo de mensaje [0=Error, 1=Informacion, 2=Advertencia]
	 */
	public void ventanaDeMensaje(Component comp, String msj, String title, short opc) {

		short type = 0;
		switch (opc) {
		case 1:
			type = JOptionPane.INFORMATION_MESSAGE;
			break;
		case 2:
			type = JOptionPane.WARNING_MESSAGE;
		}

		JOptionPane.showMessageDialog(comp, msj, title, type);
	}

	public String executeCommand(String command) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(output.toString());
		return output.toString();
	}

	public String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}
	
	public void copyFile_Java7(String origen, String destino) throws IOException {
        Path FROM = Paths.get(origen);
        Path TO = Paths.get(destino);
        //sobreescribir el fichero de destino, si existe, y copiar
        // los atributos, incluyendo los permisos rwx
        CopyOption[] options = new CopyOption[]{
          StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.COPY_ATTRIBUTES
        }; 
        Files.copy(FROM, TO, options);
    }
	
	public void writeConfig(Config cfg) throws IOException{
		File file = new File("/tmp/cntiParserApp/config.obj");
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(cfg);
		oos.close();
	} 
	
	@SuppressWarnings("resource")
	public Config readConfig() throws ClassNotFoundException, IOException{
		System.out.println();
		ObjectInputStream ois = null;
		Config cfg = null;
		try {
			File file = new File("/tmp/cntiParserApp");
			if(!file.isDirectory()){
				file.mkdirs();
				file = new File("/tmp/cntiParserApp/config.obj");				
				@SuppressWarnings("unused")
				ConfigForm cfgForm = new ConfigForm();
				return cfg;
			}else{
				file = new File("/tmp/cntiParserApp/config.obj");				
				FileInputStream fis = new FileInputStream(file);
				ois = new ObjectInputStream(fis);
				cfg = (Config) ois.readObject();	
			}				
			
		} catch (Exception e) {
			// TODO: handle exception
			@SuppressWarnings("unused")
			ConfigForm cfgForm = new ConfigForm();
		}
		
		return cfg;
	}
}

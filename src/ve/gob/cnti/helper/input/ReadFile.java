package ve.gob.cnti.helper.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadFile {

	public ReadFile() {
	}

	public String readFile(String pathFile){
		
		pathFile=pathFile.replaceAll("resources", "");
		String HoldsText= null;

	    InputStream is = getClass().getResourceAsStream(pathFile);
	    InputStreamReader fr = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(fr);

	    StringBuilder sb = new StringBuilder();
	    try {
			while((HoldsText = br.readLine())!= null){
			    sb.append(HoldsText)
			    .append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	    return sb.toString();
		
	}
	
	public File streamTofile(InputStream in) throws IOException {
        final File tempFile = File.createTempFile("cntiTempTransversalVarsfile", ".tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            int read = 0;
    		byte[] bytes = new byte[1024];

    		while ((read = in.read(bytes)) != -1) {
    			out.write(bytes, 0, read);
    		}
        }
        return tempFile;
    }
}

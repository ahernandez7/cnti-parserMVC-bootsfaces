package ve.gob.cnti.helper.input;

import java.io.BufferedReader;
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
}

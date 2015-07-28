package ve.gob.cnti.windows.swing;

import java.io.IOException;

import ve.gob.cnti.utils.Utils;

public class Main {
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
		Config cfg = new Utils().readConfig();
		if(cfg!=null){
			ParserForm pf = new ParserForm(cfg.getInicialesinstitucion(),cfg.getRutaBandeja());
			pf.setVisible(true);
		}	
		
	}
	
}

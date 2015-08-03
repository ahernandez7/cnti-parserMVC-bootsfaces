package ve.gob.cnti.windows.swing;

import java.io.Serializable;

public class Config implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String rutaBandeja;
	
	
	public Config(String rutaBandeja) {
		super();
		this.rutaBandeja = rutaBandeja;
	}

	public Config() {
		super();
	}
	public String getRutaBandeja() {
		return rutaBandeja;
	}
	public void setRutaBandeja(String rutaBandeja) {
		this.rutaBandeja = rutaBandeja;
	}

}

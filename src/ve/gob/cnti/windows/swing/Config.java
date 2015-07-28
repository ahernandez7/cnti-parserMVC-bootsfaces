package ve.gob.cnti.windows.swing;

import java.io.Serializable;

public class Config implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String inicialesinstitucion;
	private String rutaBandeja;
	
	
	public Config(String inicialesinstitucion, String rutaBandeja) {
		super();
		this.inicialesinstitucion = inicialesinstitucion;
		this.rutaBandeja = rutaBandeja;
	}

	public Config() {
		super();
	}

	public String getInicialesinstitucion() {
		return inicialesinstitucion;
	}
	public void setInicialesinstitucion(String inicialesinstitucion) {
		this.inicialesinstitucion = inicialesinstitucion;
	}
	public String getRutaBandeja() {
		return rutaBandeja;
	}
	public void setRutaBandeja(String rutaBandeja) {
		this.rutaBandeja = rutaBandeja;
	}
	
	@Override
	public String toString() {
		return "Config [inicialesinstitucion=" + inicialesinstitucion + ", rutaBandeja=" + rutaBandeja + "]";
	}

}

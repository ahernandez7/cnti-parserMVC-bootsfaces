package ve.gob.cnti.helper.form;

import java.util.HashMap;
import java.util.Map;

public class Application {
	
	private String appName;
	private Map<String, Form> mapForms;
	

	public Application() {
		
		this.appName = null;
		this.mapForms = new HashMap<String, Form>();		
	}
	
	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Map<String, Form> getMapForms() {
		return this.mapForms;
	}

	public void setMapForms(Map<String, Form> mapForms) {
		this.mapForms = mapForms;
	}

}

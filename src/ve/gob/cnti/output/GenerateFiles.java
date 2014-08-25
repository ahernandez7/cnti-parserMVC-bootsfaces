package ve.gob.cnti.output;

import java.util.List;
import java.util.Map;

import ve.gob.cnti.helper.form.Application;
import ve.gob.cnti.helper.form.Field;
import ve.gob.cnti.helper.form.Form;
import ve.gob.cnti.helper.form.Page;
import ve.gob.cnti.output.bean.GenerateBean;
import ve.gob.cnti.output.controller.GenerateController;

public class GenerateFiles {
	
	private String pathSnippetBean = null;	
	private String pathOutputFileBean = null;
	private String pathSnippetController = null;
	private String pathOutputFileController = null;
	private String packageNameBean = null;
	private String packageNameController = null;
	
	private Map<String, Form> mapForms = null;

	public GenerateFiles(Application app) {
		
		this.mapForms = app.getMapForms();		
	}
	
	
	public void generate(){
		
		GenerateBean gb = new GenerateBean(getPathSnippetBean(),getPathOutputFileBean());		
		GenerateController gc = new GenerateController(getPathSnippetController(),getPathOutputFileController());
		
		gb.createPackageDirsBean(getPackageNameBean());
		gc.createPackageDirsController(getPackageNameController());
				
		for (String key : this.mapForms.keySet()) {
			
			Form formE = this.mapForms.get(key);
			
			List<Page> pages = formE.getListPages();
			
			for(int i = 0; i < pages.size(); i++){
				
				Page page = pages.get(i);
				List<Field> fields = page.getListField();
				
				String nameBean = page.getId();
				
				gb.replaceNameBeanAndNameClassBean(nameBean);
				gb.replacePackagesNameBean(getPackageNameBean());
				
				gc.replaceNameBeanAndNameClassBeanController(nameBean);
				gc.replacePackagesNameBeanController(getPackageNameBean());
				gc.replacePackagesNameController(getPackageNameController());
				gc.writeFileConroller(nameBean);
				
				gb.setImports("");
				gb.setAttributes("");
				gb.setSetAndGetMethods("");
				
				for(int j = 0; j < fields.size(); j++){
					Field field = fields.get(j);										
					
					gb.createImpAttAndMethos(field);				
				}
				
				gb.replaceVaraiablesAndWriteFile(nameBean);
				
			}
		}
	}

	public String getPathSnippetBean() {
		return this.pathSnippetBean;
	}


	public void setPathSnippetBean(String pathSnippetBean) {
		this.pathSnippetBean = pathSnippetBean;
	}


	public String getPathOutputFileBean() {
		return pathOutputFileBean;
	}


	public void setPathOutputFileBean(String pathOutputFileBean) {
		this.pathOutputFileBean = pathOutputFileBean;
	}
	
	public String getPathSnippetController() {
		return this.pathSnippetController;
	}


	public void setPathSnippetController(String pathSnippetController) {
		this.pathSnippetController = pathSnippetController;
	}


	public String getPathOutputFileController() {
		return this.pathOutputFileController;
	}


	public void setPathOutputFileController(String pathOutputFileController) {
		this.pathOutputFileController = pathOutputFileController;
	}
	
	public String getPackageNameBean() {
		return this.packageNameBean;
	}


	public void setPackageNameBean(String packageNameBean) {
		this.packageNameBean = packageNameBean;
	}
	
	public String getPackageNameController() {
		return this.packageNameController;
	}


	public void setPackageNameController(String packageNameController) {
		this.packageNameController = packageNameController;
	}

}

package ve.gob.cnti.output;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ve.gob.cnti.helper.form.Application;
import ve.gob.cnti.helper.form.Field;
import ve.gob.cnti.helper.form.Form;
import ve.gob.cnti.helper.form.Page;
import ve.gob.cnti.helper.form.Validator;
import ve.gob.cnti.output.bean.GenerateBean;
import ve.gob.cnti.output.controller.GenerateController;
import ve.gob.cnti.output.view.GenerateView;

public class GenerateFiles {
	
	private String pathSnippetBean = null;
	private String pathOutputFileBean = null;
	private String pathSnippetController = null;
	private String pathOutputFileController = null;
	private String pathSnippetView = null;
	private String pathOutputFileView = null;
	private String packageNameBean = null;
	private String packageNameController = null;
	private String dirNameAndPathFormToInstitucion = null;
	
	private String controllerButtonNameSubmit = null;
	private String controllerButtonIdSubmit = null;
	
	private Map<String, Form> mapForms = null;
	private String nameApp = null;

	public GenerateFiles(Application app) {
		
		this.mapForms = app.getMapForms();
		this.nameApp = app.getAppName();
	}
	
	
	public void generate(){
		
		GenerateBean gb = new GenerateBean(getPathSnippetBean(),getPathOutputFileBean());		
		GenerateController gc = new GenerateController(getPathSnippetController(),getPathOutputFileController());
		GenerateView gv = new GenerateView(getPathSnippetView(),getPathOutputFileView());
		
		gb.createPackageDirsBean(getPackageNameBean());
		gc.createPackageDirsController(getPackageNameController());
		
		gv.createDirViewAndFormToInst(getDirNameAndPathFormToInstitucion(), this.nameApp);
		
		String[] controllersNameSubmit = getControllerButtonNameSubmit().split(",");
		String[] controllersIdSubmit = getControllerButtonIdSubmit().split(",");
		
		Map<String,String> controllersNameSubmitMap = new HashMap<String, String>();
		
		for (int i = 0; i < controllersNameSubmit.length; i++){
			String[] controllerSubmitName = controllersNameSubmit[i].split("\\.");
			System.out.println(controllerSubmitName[0].trim() + " <==> "+controllerSubmitName[1].trim());
			controllersNameSubmitMap.put(controllerSubmitName[0].trim(), controllerSubmitName[1].trim());
		}
		
		Map<String,String> controllersIdSubmitMap = new HashMap<String, String>();
		
		for (int i = 0; i < controllersIdSubmit.length; i++){
			String[] controllerSubmitId = controllersIdSubmit[i].split("\\.");
			System.out.println(controllerSubmitId[0].trim() + " <==> "+controllerSubmitId[1].trim());
			controllersIdSubmitMap.put(controllerSubmitId[0].trim(), controllerSubmitId[1].trim());
		}
		
		gv.setControllersNameSubmitMap(controllersNameSubmitMap);
		gv.setControllersIdSubmitMap(controllersIdSubmitMap);
		
		
		for (String key : this.mapForms.keySet()) {
			
			
			gv.replaceNameTask(key);
			
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
				
				gv.setNameBean(nameBean);
				gv.setInputElements("");
				gv.setButton("");
				
				for(int j = 0; j < fields.size(); j++){
					Field field = fields.get(j);										
					
					gb.createImpAttAndMethos(field);
					
					gv.setValidator("");
					
					List<Validator> validators = field.getListValidators();
					for(int k = 0; k < validators.size(); k++){
						Validator validator = validators.get(k);
						gv.createValidator(validator.getNameValidator(), field.getNameField());
					}
					
					gv.createInputElement(field);
					gv.createButton(field);
				}
				
				gb.replaceVaraiablesAndWriteFile(nameBean);
				
				gv.writeFileAndCreateDirToView(nameBean);
				
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
	
	public String getPathSnippetView() {
		return this.pathSnippetView;
	}


	public void setPathSnippetView(String pathSnippetView) {
		this.pathSnippetView = pathSnippetView;
	}


	public String getPathOutputFileView() {
		return this.pathOutputFileView;
	}


	public void setPathOutputFileView(String pathOutputFileView) {
		this.pathOutputFileView = pathOutputFileView;
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


	public String getDirNameAndPathFormToInstitucion() {
		return this.dirNameAndPathFormToInstitucion;
	}


	public void setDirNameAndPathFormToInstitucion(String dirNameAndPathFormToInstitucion) {
		this.dirNameAndPathFormToInstitucion = dirNameAndPathFormToInstitucion;
	}


	public String getControllerButtonNameSubmit() {
		return this.controllerButtonNameSubmit;
	}


	public void setControllerButtonNameSubmit(String controllerButtonNameSubmit) {
		this.controllerButtonNameSubmit = controllerButtonNameSubmit;
	}


	public String getControllerButtonIdSubmit() {
		return this.controllerButtonIdSubmit;
	}


	public void setControllerButtonIdSubmit(String controllerButtonIdSubmit) {
		this.controllerButtonIdSubmit = controllerButtonIdSubmit;
	}

}

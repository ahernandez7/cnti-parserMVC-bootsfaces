package ve.gob.cnti.output;

import java.util.ArrayList;
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
	private String pathSnippetViewTab = null;
	private String pathSnippetViewTabSummay = null;
	private String pathSnippetViewSuccess = null;
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

	// Método constructor del MVC de la aplicación
	public void generate() {

		GenerateBean gb = new GenerateBean(getPathSnippetBean(), getPathOutputFileBean(), this.nameApp);
		GenerateController gc = new GenerateController(getPathSnippetController(), getPathOutputFileController(), this.nameApp);
		GenerateView gv = new GenerateView(getPathSnippetView(), getPathOutputFileView(), this.nameApp);
		GenerateView gvSuccess = new GenerateView(getPathSnippetViewSuccess(), getPathOutputFileView(), this.nameApp);
		
		// crear los directorios de los beans y controladores en funcion de los paquetes
		// definidos en el configuracion.properties
		gb.createPackageDirsBean(getPackageNameBean());
		gc.createPackageDirsController(getPackageNameController());
		gv.createDirViewAndFormToInst(getDirNameAndPathFormToInstitucion());
		

		for (String key : this.mapForms.keySet()) {

			// Tareas
			Form formE = this.mapForms.get(key);
			// Formularios de dicha tarea
			List<Page> pages = formE.getListPages();

			String nameBean = formE.getNameForm();

			gv.setNameBean(nameBean);
			gv.replaceNameTask(key);

			gv.setInputElements("");
			gv.setButton("");
			gv.setTabsReferences("");

			List<Field> fieldsComplete = new ArrayList<Field>();
			for (int i = 0; i <= pages.size(); i++) {
				
				gb.replaceNameBeanAndNameClassBean(nameBean);
				gb.replacePackagesNameBean(getPackageNameBean());
				
				gb.setImports("");
				gb.setAttributes("");
				gb.setSetAndGetMethods("");
				
				gc.replaceNameBeanAndNameClassBeanController(nameBean);
				gc.replacePackagesNameBeanController(getPackageNameBean());
				gc.replacePackagesNameController(getPackageNameController());
				
				GenerateView gv_tab;
				if (i < pages.size())
					gv_tab = new GenerateView(getPathSnippetViewTab(), getPathOutputFileView(), this.nameApp);
				else
					gv_tab = new GenerateView(getPathSnippetViewTabSummay(), getPathOutputFileView(), this.nameApp);

				gv_tab.createDirViewTabAndFormToInst(getDirNameAndPathFormToInstitucion(), nameBean);

				gv_tab.setNameBean(nameBean);
				gv_tab.replaceNameTask(key);

				List<Field> fields = new ArrayList<Field>();
				boolean containsFiles = false;

				if (i < pages.size()) {
					Page page = pages.get(i);
					fields = page.getListField();
					gv_tab.replaceIdAndNameTab(page.getId(), page.getName());
					gv_tab.setInputElements("");
					gv_tab.setButton("");
				} else {
					fields = fieldsComplete;
					gv_tab.insertFileTable("TabResumen", true);
				}

				for (int j = 0; j < fields.size(); j++) {

					Field field = fields.get(j);
					if (i < pages.size()) {
						if (!containsFiles && field.getVarName().contains("_FILE"))
							containsFiles = true;

						gv_tab.setValidator("");

						List<Validator> validators = field.getListValidators();
						for (int k = 0; k < validators.size(); k++) {
							Validator validator = validators.get(k);
							gv_tab.createValidator(validator.getNameValidator(), field.getNameField());
						}

						gv_tab.insertMessageTag("Tab" + (i + 1), containsFiles);
						gv_tab.createInputElement(field,String.valueOf(i+1));
						gv_tab.insertFileTable("Tab" + (i + 1), containsFiles);

						fieldsComplete.add(field);
					} else {
						if (!field.getVarName().contains("_FILE"))
							gv_tab.createOutputElement(field);
						
						gc.insertFilesInController(fields);
						gb.createImpAttAndMethos(field);
					}	

				}
				gv_tab.writeFileAndCreateDirToView(nameBean, "tab" + (i + 1));
				gv.createTabReference(nameBean, "tab" + (i + 1), getDirNameAndPathFormToInstitucion());
			}
			gb.replaceVaraiablesAndWriteFile(nameBean);
			
			gv.setNameBean(nameBean);
			gv.replaceNameTask(key);
			gv.replaceTabsReferences();
			
			gv.writeFileAndCreateDirToView(nameBean);
//			gvSucess.createDirViewAndFormToInst(getDirNameAndPathFormToInstitucion())
			
			gc.replaceDirViewSuccess(getDirNameAndPathFormToInstitucion());
			
			gc.writeFileConroller(nameBean);
			

			// TODO
////			 Colocar esta lógica en la clase Generate controller
//			 String files="";
//			 String filesSettersAndGetters="";
//			 String typeFile="";
//			 for (int j = 0; j < fields.size(); j++) {
//				 Field field = fields.get(j);
//				 if(field.isActuation() && field.getVarName().matches("^_FILE_.*$")){
//					 typeFile= "UploadedFile";
//					 files += "private "+typeFile+" "+field.getVarName()+";\n";
//					 filesSettersAndGetters += gb.createSetMethod(field.getVarName(), typeFile);
//				 }else if(!field.isActuation() && field.getVarName().matches("^_FILE_.*$")){
//					 typeFile= "StreamedContent";
//					 files += "private "+typeFile+" "+field.getVarName()+";\n";
//					 filesSettersAndGetters += gb.createSetMethod(field.getVarName(), typeFile);
//				 }
//			 }
			
			
			
		}
		gvSuccess.writeFileAndCreateViewSuccess(getDirNameAndPathFormToInstitucion());

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

	public String getPathSnippetViewTab() {
		return pathSnippetViewTab;
	}

	public void setPathSnippetViewTab(String pathSnippetViewTab) {
		this.pathSnippetViewTab = pathSnippetViewTab;
	}

	public String getPathSnippetViewTabSummay() {
		return pathSnippetViewTabSummay;
	}

	public void setPathSnippetViewTabSummay(String pathSnippetViewTabSummay) {
		this.pathSnippetViewTabSummay = pathSnippetViewTabSummay;
	}

	public String getPathSnippetViewSuccess() {
		return pathSnippetViewSuccess;
	}

	public void setPathSnippetViewSuccess(String pathSnippetViewSuccess) {
		this.pathSnippetViewSuccess = pathSnippetViewSuccess;
	}

}

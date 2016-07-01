package ve.gob.cnti;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.jdom2.JDOMException;

import ve.gob.cnti.core.PaserXmltoForm;
import ve.gob.cnti.helper.form.Application;
import ve.gob.cnti.helper.form.Field;
import ve.gob.cnti.helper.form.Form;
import ve.gob.cnti.helper.form.Page;
import ve.gob.cnti.helper.util.LibUtils;
import ve.gob.cnti.helper.util.ValidateForm;
import ve.gob.cnti.output.GenerateFiles;
import ve.gob.cnti.windows.swing.ParserProcessing;

public class App {

	private static final String FILE_PROPS = "configuracion.properties";

	@SuppressWarnings("unused")
	private PaserXmltoForm pxf = null;

	private String xmlFile = null;

	private String snippetFileBean = null;
	private String pathBeanOutputFile = null;
	private String namePackageBean = null;

	private String snippetFileController = null;
	private String pathControllerOutputFile = null;
	private String namePackageController = null;
	
	private String namePackageValidator = null;
	private String pathValidatorOutputFile = null;
	
	private String snippetFileView = null;
	private String snippetFileViewTab = null;
	private String snippetFileViewTabSummary = null;
	private String snippetFileViewSuccess = null;
	private String snippetFileViewCaseConsult = null;
	private String snippetFileViewCaseConsultTab = null;
	private String pathViewOutputFile = null;
	@SuppressWarnings("unused")
	private String dirNameInstitucion = null;
	private String pathDirFormView = null;

	private String controllerButtonNameSubmit = null;
	private String controllerButtonIdSubmit = null;

	public App() {

		Properties props = LibUtils.loadFileProperties(FILE_PROPS);

		String XML_FILE = props.getProperty("XML_FILE");
		String ROOT_VIEW = props.getProperty("ROOT_VIEW");
		String ROOT_CONTROLLER = props.getProperty("ROOT_CONTROLLER");
		String ROOT_BEAN = props.getProperty("ROOT_BEAN");		
		String ROOT_VALIDATOR = props.getProperty("ROOT_VALIDATOR");
		String ROOT_SNIPPET = props.getProperty("ROOT_SNIPPET");
		String NAME_PACKEGE_BEAN = props.getProperty("NAME_PACKAGE_BEAN");
		String NAME_PACKEGE_VALIDATOR = props.getProperty("NAME_PACKAGE_VALIDATOR");
		String NAME_PACKE_CONTROLLER = props.getProperty("NAME_PACKAGE_CONTROLLER");
		String DIR_INSTITUCION = props.getProperty("DIR_INSTITUCION");
		String PATH_DIR_FORM_VIEW = props.getProperty("PATH_DIR_FORM_VIEW");

		String CONTROLLER_BUTTON_NAME_SUBMIT = props.getProperty("CONTROLLER_BUTTON_NAME_SUBMIT");
		String CONTROLLER_BUTTON_ID_SUBMIT = props.getProperty("CONTROLLER_BUTTON_ID_SUBMIT");

		this.xmlFile = XML_FILE;

		this.snippetFileBean = ROOT_SNIPPET + "Bean.snippet";
		this.pathBeanOutputFile = ROOT_BEAN;
		this.namePackageBean = NAME_PACKEGE_BEAN;

		this.snippetFileController = ROOT_SNIPPET + "ModelController.snippet";
		this.pathControllerOutputFile = ROOT_CONTROLLER;
		this.namePackageController = NAME_PACKE_CONTROLLER;

		this.pathValidatorOutputFile=ROOT_VALIDATOR;
		this.namePackageValidator = NAME_PACKEGE_VALIDATOR;
		
		this.snippetFileView = ROOT_SNIPPET + "View.snippet";
		this.snippetFileViewTab = ROOT_SNIPPET + "Tab.snippet";
		this.snippetFileViewTabSummary = ROOT_SNIPPET + "TabSummary.snippet";
		this.snippetFileViewSuccess = ROOT_SNIPPET + "ViewSuccess.snippet";
		this.snippetFileViewCaseConsult = ROOT_SNIPPET + "CaseConsult.snippet";
		this.snippetFileViewCaseConsultTab = ROOT_SNIPPET + "tabCaseConsult.snippet";
		this.pathViewOutputFile = ROOT_VIEW;
		this.dirNameInstitucion = DIR_INSTITUCION;
		this.pathDirFormView = PATH_DIR_FORM_VIEW;

		this.controllerButtonNameSubmit = CONTROLLER_BUTTON_NAME_SUBMIT;
		this.controllerButtonIdSubmit = CONTROLLER_BUTTON_ID_SUBMIT;

		try {
			pxf = new PaserXmltoForm(xmlFile);

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public App(String pathROOT) {

		Properties props = LibUtils.loadFileProperties(FILE_PROPS);

		String ROOT_VIEW = pathROOT + "views/";
		String ROOT_VALIDATOR=pathROOT+"validators/";
		pathROOT += "beansANDcontrollers/";
		String ROOT_CONTROLLER = pathROOT;
		String ROOT_BEAN = pathROOT;		
		

		String ROOT_SNIPPET = props.getProperty("ROOT_SNIPPET");
		String NAME_PACKEGE_BEAN = props.getProperty("NAME_PACKAGE_BEAN");
		String NAME_PACKE_CONTROLLER = props.getProperty("NAME_PACKAGE_CONTROLLER");
		String DIR_INSTITUCION = props.getProperty("DIR_INSTITUCION");
		String PATH_DIR_FORM_VIEW = props.getProperty("PATH_DIR_FORM_VIEW");

		String CONTROLLER_BUTTON_NAME_SUBMIT = props.getProperty("CONTROLLER_BUTTON_NAME_SUBMIT");
		String CONTROLLER_BUTTON_ID_SUBMIT = props.getProperty("CONTROLLER_BUTTON_ID_SUBMIT");

		String NAME_PACKE_VALIDATOR = props.getProperty("NAME_PACKAGE_VALIDATOR");
		
		this.namePackageValidator=NAME_PACKE_VALIDATOR;
		this.pathValidatorOutputFile = ROOT_VALIDATOR;
		
		this.snippetFileBean = ROOT_SNIPPET + "Bean.snippet";
		this.pathBeanOutputFile = ROOT_BEAN;
		this.namePackageBean = NAME_PACKEGE_BEAN;

		this.snippetFileController = ROOT_SNIPPET + "ModelController.snippet";
		this.pathControllerOutputFile = ROOT_CONTROLLER;
		this.namePackageController = NAME_PACKE_CONTROLLER;

		this.snippetFileView = ROOT_SNIPPET + "View.snippet";
		this.snippetFileViewTab = ROOT_SNIPPET + "Tab.snippet";
		this.snippetFileViewTabSummary = ROOT_SNIPPET + "TabSummary.snippet";
		this.snippetFileViewSuccess = ROOT_SNIPPET + "ViewSuccess.snippet";
		this.snippetFileViewCaseConsult = ROOT_SNIPPET + "CaseConsult.snippet";
		this.snippetFileViewCaseConsultTab = ROOT_SNIPPET + "tabCaseConsult.snippet";
		this.pathViewOutputFile = ROOT_VIEW;
		this.dirNameInstitucion = DIR_INSTITUCION;
		this.pathDirFormView = PATH_DIR_FORM_VIEW;

		this.controllerButtonNameSubmit = CONTROLLER_BUTTON_NAME_SUBMIT;
		this.controllerButtonIdSubmit = CONTROLLER_BUTTON_ID_SUBMIT;

	}

	public void generateFiles(String xml) throws JDOMException, IOException {

		GenerateFiles gf = new GenerateFiles(new PaserXmltoForm(xml).parse());

		
		gf.setPathSnippetBean(this.snippetFileBean);
		gf.setPathOutputFileBean(this.pathBeanOutputFile);
		gf.setPackageNameBean(this.namePackageBean.replaceAll("%\\{institucion\\}", "tramites"));

		gf.setPathSnippetController(this.snippetFileController);
		gf.setPathOutputFileController(this.pathControllerOutputFile);
		gf.setPackageNameController(this.namePackageController.replaceAll("%\\{institucion\\}", "tramites"));

		gf.setPackageNameValidator(this.namePackageValidator);
		gf.setPathOutputFileValidator(this.pathValidatorOutputFile);
		
		gf.setPathSnippetView(this.snippetFileView);
		gf.setPathSnippetViewTab(this.snippetFileViewTab);
		gf.setPathSnippetViewTabSummay(this.snippetFileViewTabSummary);
		gf.setPathSnippetViewSuccess(this.snippetFileViewSuccess);
		gf.setSnippetFileViewCaseConsult(this.snippetFileViewCaseConsult);
		gf.setSnippetFileViewCaseConsultTab(this.snippetFileViewCaseConsultTab);
		gf.setPathOutputFileView(this.pathViewOutputFile);
		gf.setDirNameAndPathFormToInstitucion("tramites" + this.pathDirFormView);
		gf.setInstitucion("tramites");

		gf.setControllerButtonNameSubmit(this.controllerButtonNameSubmit);
		gf.setControllerButtonIdSubmit(this.controllerButtonIdSubmit);

		gf.generate();
	}
	
	public boolean isFormValid(String xml,String XmlProcessDesign,ParserProcessing pp) {
		Application tp;
		try {
			tp = new PaserXmltoForm(xml).parse();
			if(new ValidateForm(tp,XmlProcessDesign).isAppFormValid(pp)==false)
				return false;
			else
				this.printFormDetails(xml);
		} catch (JDOMException | IOException e) {
			System.out.println("La validación del formulario fallo.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void printFormDetails(String xml){
		try {
			Application tp = new PaserXmltoForm(xml).parse();
			Map<String, Form> mapForms = tp.getMapForms();
			printHeader();
			System.out.println("Nombre de Proceso =============>> " + tp.getAppName());
			System.out.println("Numero de tareas humanas ======>> " + mapForms.size());
			System.out.println("Tareas: ");
			for (String key : mapForms.keySet()) {
				System.out.println("=====>> " + mapForms.get(key).getNameForm());
			}
			printHeader();
			for (String key : mapForms.keySet()) {
				Form task = mapForms.get(key);
				System.out.println("  Nombre de la tarea ===============>> " + task.getNameForm());
				System.out.println("  Numero de tabs en la tarea =======>> " + task.getListPages().size());
				for (Page tab : task.getListPages()) {
					System.out.println("\n    Identificador del tab ==========>> " + tab.getId());
					System.out.println("    Nombre del tab =================>> " + tab.getName());
					System.out.println("    Campos:");
					for (Field field : tab.getListField()) {
						if (!field.getTypeField().contentEquals("BUTTON_SUBMIT") && 
								!field.getTypeField().contentEquals("BUTTON_PREVIOUS") && 
								!field.getTypeField().contentEquals("BUTTON_NEXT")) {
							System.out.println("\n\t VarName ==================>> " + field.getVarName());
							System.out.println("\t ReturnType ===============>> " + field.getReturnType());
							System.out.println("\t TypeField ================>> " + field.getTypeField());
							System.out.println("\t Label ====================>> " + field.getLabelField());
							System.out.println("\t Descripción ==============>> " + field.getDescription());
						}
					}
				}
				printHeader();
			}	
			
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
	}

	private static void printHeader() {
		System.out.println("\n********************************************************************************************************");
		System.out.println("--------------------------------------------------------------------------------------------------------");
		System.out.println("********************************************************************************************************\n");
	}

}

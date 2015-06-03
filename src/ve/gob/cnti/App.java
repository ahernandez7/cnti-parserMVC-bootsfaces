package ve.gob.cnti;

import java.io.IOException;
import java.util.Properties;

import org.jdom2.JDOMException;

import ve.gob.cnti.core.PaserXmltoForm;
import ve.gob.cnti.helper.util.LibUtils;
import ve.gob.cnti.output.GenerateFiles;


public class App {

	private static final String FILE_PROPS = "resources/inputs/configuracion.properties";
	
	private PaserXmltoForm pxf = null;	

	private String xmlFile = null;
	
	private String snippetFileBean = null;
	private String pathBeanOutputFile = null;
	private String namePackageBean = null;
	
	private String snippetFileController = null;
	private String pathControllerOutputFile = null;
	private String namePackageController = null;
	
	private String snippetFileView = null;
	private String snippetFileViewTab = null;
	private String snippetFileViewTabSummary = null;
	private String snippetFileViewSuccess = null;
	private String snippetFileViewCaseConsult = null;
	private String snippetFileViewCaseConsultTab = null;
	private String pathViewOutputFile = null;
	private String dirNameInstitucion = null;
	private String pathDirFormView = null;
	
	private String controllerButtonNameSubmit = null;
	private String controllerButtonIdSubmit = null;
	
	public App(){
		
		Properties props = LibUtils.loadFileProperties(FILE_PROPS);
		
		String XML_FILE = props.getProperty("XML_FILE");
		String ROOT_VIEW = props.getProperty("ROOT_VIEW");
		String ROOT_CONTROLLER = props.getProperty("ROOT_CONTROLLER");
		String ROOT_BEAN = props.getProperty("ROOT_BEAN");
		String ROOT_SNIPPET = props.getProperty("ROOT_SNIPPET");
		String NAME_PACKEGE_BEAN = props.getProperty("NAME_PACKAGE_BEAN");
		String NAME_PACKE_CONTROLLER = props.getProperty("NAME_PACKAGE_CONTROLLER");
		String DIR_INSTITUCION = props.getProperty("DIR_INSTITUCION");
		String PATH_DIR_FORM_VIEW = props.getProperty("PATH_DIR_FORM_VIEW");
		
		String CONTROLLER_BUTTON_NAME_SUBMIT = props.getProperty("CONTROLLER_BUTTON_NAME_SUBMIT");
		String CONTROLLER_BUTTON_ID_SUBMIT = props.getProperty("CONTROLLER_BUTTON_ID_SUBMIT");
		
				
		this.xmlFile = XML_FILE; 
		
		this.snippetFileBean = ROOT_SNIPPET+"Bean.snippet";
		this.pathBeanOutputFile = ROOT_BEAN;
		this.namePackageBean = NAME_PACKEGE_BEAN;
		
		this.snippetFileController = ROOT_SNIPPET+"ModelController.snippet";
		this.pathControllerOutputFile = ROOT_CONTROLLER;
		this.namePackageController = NAME_PACKE_CONTROLLER;
		
		this.snippetFileView = ROOT_SNIPPET+"View.snippet";
		this.snippetFileViewTab = ROOT_SNIPPET+"Tab.snippet";
		this.snippetFileViewTabSummary = ROOT_SNIPPET+"TabSummary.snippet";
		this.snippetFileViewSuccess = ROOT_SNIPPET+"ViewSuccess.snippet";
		this.snippetFileViewCaseConsult = ROOT_SNIPPET+"CaseConsult.snippet";
		this.snippetFileViewCaseConsultTab = ROOT_SNIPPET+"tabCaseConsult.snippet";
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
	
	private void generateFiles(){
		GenerateFiles gf = new GenerateFiles(this.pxf.parse());
		
		gf.setPathSnippetBean(this.snippetFileBean);
		gf.setPathOutputFileBean(this.pathBeanOutputFile);
		gf.setPackageNameBean(this.namePackageBean);
		
		gf.setPathSnippetController(this.snippetFileController);
		gf.setPathOutputFileController(this.pathControllerOutputFile);
		gf.setPackageNameController(this.namePackageController);
		
		gf.setPathSnippetView(this.snippetFileView);
		gf.setPathSnippetViewTab(this.snippetFileViewTab);
		gf.setPathSnippetViewTabSummay(this.snippetFileViewTabSummary);
		gf.setPathSnippetViewSuccess(this.snippetFileViewSuccess);
		gf.setSnippetFileViewCaseConsult(this.snippetFileViewCaseConsult);
		gf.setSnippetFileViewCaseConsultTab(this.snippetFileViewCaseConsultTab);
		gf.setPathOutputFileView(this.pathViewOutputFile);
		gf.setDirNameAndPathFormToInstitucion(this.dirNameInstitucion+this.pathDirFormView);
		gf.setInstitucion(this.dirNameInstitucion);
		
		gf.setControllerButtonNameSubmit(this.controllerButtonNameSubmit);
		gf.setControllerButtonIdSubmit(this.controllerButtonIdSubmit);
		
		gf.generate();
	}
	
	public static void main(String[] args) {
		
		App tp = new App();
		tp.generateFiles();
		
//		Test t = new Test();
//		t.showListOutput(tp.pxf.parse());
		
		System.out.println("Listo...");
	}

}

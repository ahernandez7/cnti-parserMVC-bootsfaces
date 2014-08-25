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
	
	public App(){
		
		Properties props = LibUtils.loadFileProperties(FILE_PROPS);
		
		String XML_FILE = props.getProperty("XML_FILE");
//		String ROOT_VIEW = props.getProperty("ROOT_VIEW");
		String ROOT_CONTROLLER = props.getProperty("ROOT_CONTROLLER");
		String ROOT_BEAN = props.getProperty("ROOT_BEAN");
		String ROOT_SNIPPET = props.getProperty("ROOT_SNIPPET");
		String NAME_PACKEGE_BEAN = props.getProperty("NAME_PACKAGE_BEAN");
		String NAME_PACKE_CONTROLLER = props.getProperty("NAME_PACKAGE_CONTROLLER");
		
				
		this.xmlFile = XML_FILE; 
		
		this.snippetFileBean = ROOT_SNIPPET+"Bean.snippet";
		this.pathBeanOutputFile = ROOT_BEAN;
		this.namePackageBean = NAME_PACKEGE_BEAN;
		
		this.snippetFileController = ROOT_SNIPPET+"ModelController.snippet";
		this.pathControllerOutputFile = ROOT_CONTROLLER;
		this.namePackageController = NAME_PACKE_CONTROLLER;
		
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
		
		gf.generate();
	}
	
	public static void main(String[] args) {
		App tp = new App();
		
		Test t = new Test();
		t.showListOutput(tp.pxf.parse());
		
		//tp.generateFiles();
		
		System.out.println("Listo...");
	}

}

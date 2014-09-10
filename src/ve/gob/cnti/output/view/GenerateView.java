package ve.gob.cnti.output.view;


import java.util.Map;

import ve.gob.cnti.helper.form.Field;
import ve.gob.cnti.helper.output.WriteFile;
import ve.gob.cnti.helper.util.LibUtils;

//import ve.gob.cnti.helper.output.WriteFile;

public class GenerateView {

	private String snippetFile = null;
	private String origSnippetFile = null;
	private WriteFile wf = null;
	private String pathOutputFile = null;
	
	private String nameBean = "";
	private String inputElements = "";
	private String required = "";
	private String readOnly = "";
	private String validator = "";
	private String button = "";
	
	private Map<String,String> controllersNameSubmitMap = null;
	private Map<String,String> controllersIdSubmitMap = null;
	
	private String nameApp = null;

		
	public GenerateView(String pathFile, String pathOutputFile, String nameApp) {
		
		this.origSnippetFile = LibUtils.readSnippetFile(pathFile);
		this.pathOutputFile = pathOutputFile;
		this.wf = new WriteFile();
		this.nameApp = nameApp;
	}
		
	public Map<String, String> getControllersNameSubmitMap() {
		return this.controllersNameSubmitMap;
	}

	public void setControllersNameSubmitMap(Map<String, String> controllersNameSubmitMap) {
		this.controllersNameSubmitMap = controllersNameSubmitMap;
	}

	public Map<String, String> getControllersIdSubmitMap() {
		return this.controllersIdSubmitMap;
	}

	public void setControllersIdSubmitMap(Map<String, String> controllersIdSubmitMap) {
		this.controllersIdSubmitMap = controllersIdSubmitMap;
	}

	public String getNameBean() {
		return this.nameBean;
	}

	public void setNameBean(String nameBean) {
		this.nameBean = nameBean;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}
	
	public void setInputElements(String inputElements) {
		this.inputElements = inputElements;
	}
	
	public void setButton(String button){
		this.button = button;
	}

	public boolean createDirViewAndFormToInst(String dirNameAndFormToInstitucion) {
		this.pathOutputFile += dirNameAndFormToInstitucion;
		this.pathOutputFile = LibUtils.replacePattern("%\\{processName\\}",this.nameApp,this.pathOutputFile);
		return LibUtils.createDirs(this.pathOutputFile);
	}
	
	public void replaceNameTask(String nameTask) {
		//Para colocar la variable con el snippet original en la siguiete iteración
		this.snippetFile = this.origSnippetFile;
		this.snippetFile = LibUtils.replacePattern("%\\{nameTask\\}", nameTask,this.snippetFile);
	}
	
	public void createValidator(String nameValidator, String nameField){
		
		//TODO:Completar los validadores restantes
		
		if("mandatory".equalsIgnoreCase(nameValidator)){
			this.required = "required=\"true\" requiredMessage=\"Requerido\"";
		}if ("readonly".equalsIgnoreCase(nameValidator)){
			this.readOnly = "readonly=\"true\"";
		}else if ("LengthValidator".equalsIgnoreCase(nameValidator)){
			this.validator += "\t<f:validateLength maximum=\"20\" minimum=\"3\" for=\""+nameField+"\"/>\n";
		}else if ("EmailValidator".equalsIgnoreCase(nameValidator)){
			this.validator += "\t<f:validator validatorId=\"ve.gob.cnti.gestion.utils.validators.EmailValidator\" for=\""+nameField+"\"/>\n";
		}else if ("CharFieldValidator".equalsIgnoreCase(nameValidator)){
			
		}else if ("NumericIntegerFieldValidator".equalsIgnoreCase(nameValidator)){
			
		}else if ("MailValidator".equalsIgnoreCase(nameValidator)){
			this.validator += "\t<f:validator validatorId=\"ve.gob.cnti.gestion.utils.validators.EmailValidator\" for=\""+nameField+"\"/>\n";
		}else if ("DateFieldValidator".equalsIgnoreCase(nameValidator)){
			
		}else if ("NumericDoubleFieldValidator".equalsIgnoreCase(nameValidator)){
			
		}else if ("NumericFloatFieldValidator".equalsIgnoreCase(nameValidator)){
			
		}else if ("NumericIntegerFieldValidator".equalsIgnoreCase(nameValidator)){
			
		}else if ("NumericLongFieldValidator".equalsIgnoreCase(nameValidator)){
			
		}
	}
	
	public void createInputElement(Field field){
		
		String inputElement = translateField2View(field);
		
		this.inputElements += inputElement;
	}
	
	private void replaceInputsElements() {
		
		this.snippetFile = LibUtils.replacePattern("%\\{inputsElements\\}", this.inputElements,this.snippetFile);
	}
	
	private String translateField2View(Field field){
		
		//TODO: Completar el resto de tipos de datos del campo en el formulario
		String label = field.getLabelField();  
		String name =  field.getNameField();
		String type =  field.getTypeField();
		
		String fieldInput = "";
		
		if (!"BUTTON_SUBMIT".equalsIgnoreCase(type) && !"BUTTON_NEXT".equalsIgnoreCase(type) && !"BUTTON_PREVIOUS".equalsIgnoreCase(type)){
			fieldInput = "<p:outputLabel for=\""+name+"\" value=\""+label+"\"/>\n";
		}
		
		if ("TEXTBOX".equalsIgnoreCase(type)){
			
			fieldInput += "<p:inputText id=\""+name+"\" value=\"#{"+this.nameApp+"_"+LibUtils.firstLetterLower(getNameBean())+"Controller.bean."+name+"}\" "+this.required+" "+this.readOnly+">\n"; 
			fieldInput += this.validator;
			fieldInput += "</p:inputText>\n";
		    
		}else if ("DATE".equalsIgnoreCase(type)){
			
			fieldInput += "<p:calendar id=\""+name+"\" value=\"#{"+this.nameApp+"_"+LibUtils.firstLetterLower(getNameBean())+"Controller.bean."+name+"} locale=\"es\" navigator=\"true\" pattern=\"dd-mm-yyyy\" "; 
			fieldInput += this.required+" "+this.readOnly+">\n";
			fieldInput += this.validator;
			fieldInput += "</p:calendar>\n";
			
		}else if ("CHECKBOX".equalsIgnoreCase(type)){
			
		}else if ("HIDDEN".equalsIgnoreCase(type)){
//			if (){
//				fieldInput +="";
//			}else{
//				fieldInput +="";
//			}
		}else if ("PASSWORD".equalsIgnoreCase(type)){
			
		}else if ("TEXTAREA".equalsIgnoreCase(type)){
			
		}else if ("LISTBOX_SIMPLE".equalsIgnoreCase(type)){
			
		}else if ("RICH_TEXTAREA".equalsIgnoreCase(type)){
			
		}else if ("FILEUPLOAD".equalsIgnoreCase(type)){
			
		}else if ("MESSAGE".equalsIgnoreCase(type)){
			
		}else if ("CHECKBOX_GROUP".equalsIgnoreCase(type)){
			
		}else if ("DURATION".equalsIgnoreCase(type)){
			
		}else if ("LISTBOX_MULTIPLE".equalsIgnoreCase(type)){
			
		}else if ("RADIOBUTTON_GROUP".equalsIgnoreCase(type)){
			fieldInput += "<p:selectOneRadio id=\""+name+"\" value=\"#{"+this.nameApp+"_"+LibUtils.firstLetterLower(getNameBean())+"Controller.bean."+name+"}\">\n";
			fieldInput += "\t<f:selectItem itemLabel=\"Aceptar\" itemValue=\"true\" />\n";
			fieldInput += "\t<f:selectItem itemLabel=\"Rechazar\" itemValue=\"false\" />\n"; 
			fieldInput += "</p:selectOneRadio>\n";
		}else if ("SUGGESTBOX".equalsIgnoreCase(type)){
			
		}
		
		if (!"BUTTON_SUBMIT".equalsIgnoreCase(type) && !"BUTTON_NEXT".equalsIgnoreCase(type) && !"BUTTON_PREVIOUS".equalsIgnoreCase(type)){
			fieldInput += "<p:message for=\""+name+"\" id=\""+name+"Message\" style=\"color:red\"/>\n";
		}
		
		return fieldInput;
	}
	
	public void createButton(Field field){
		
		String button = translateButton2View(field);
		
		this.button += button;
	}
	
	private void replacebutton() {
		
		this.snippetFile = LibUtils.replacePattern("%\\{submitButton\\}", this.button,this.snippetFile);
	}
	
	private String translateButton2View(Field field){
		
		//TODO:Completar los tipos de botones
		String type =  field.getTypeField();
		String buttoInput = "";
		
		String controllerNameSubmit = getControllersNameSubmitMap().get(getNameBean());
		String controllerIdSubmit = getControllersIdSubmitMap().get(getNameBean());
		
		if ("BUTTON_SUBMIT".equalsIgnoreCase(type)){
			
			buttoInput += "<h:commandButton value=\"Enviar\" action=\"#{"+this.nameApp+"_"+LibUtils.firstLetterLower(getNameBean())+"Controller.executeTask}\">\n";
			buttoInput += "\t<f:param name=\""+controllerIdSubmit+"\" value=\"#{"+controllerNameSubmit+"."+controllerIdSubmit+"}\"/>\n";
			buttoInput += "</h:commandButton>";
			
		}else if ("BUTTON_NEXT".equalsIgnoreCase(type)){
			
		}else if ("BUTTON_PREVIOUS".equalsIgnoreCase(type)){
			
		}
		
		return buttoInput;
	}
	
	public void writeFileAndCreateDirToView(String nameViewFile){
		
		LibUtils.createDirs(this.pathOutputFile+"/"+nameViewFile+"/");
		
		replaceInputsElements();
		replacebutton();
		
		this.wf.writeFile(this.pathOutputFile+"/"+nameViewFile+"/"+nameViewFile+".xhtml", this.snippetFile);
	}

}

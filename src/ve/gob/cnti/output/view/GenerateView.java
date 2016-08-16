package ve.gob.cnti.output.view;

import java.util.Iterator;
import java.util.Map;

import ve.gob.cnti.helper.form.Field;
import ve.gob.cnti.helper.form.Validator;
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
	private String tabsReferences = "";
	private String outputElements = "";
	private String documentElements = "";
	private String outputsTableElements = "";

	private Map<String, String> controllersNameSubmitMap = null;
	private Map<String, String> controllersIdSubmitMap = null;

	private String nameApp = null;

	public GenerateView(String pathFile, String pathOutputFile, String nameApp) {
		this.origSnippetFile = LibUtils.readSnippetFile(pathFile);
		this.pathOutputFile = pathOutputFile;
		this.wf = new WriteFile();
		this.nameApp = nameApp;
	}

	public boolean createDirViewAndFormToInst(String dirNameAndFormToInstitucion) {
		this.pathOutputFile += dirNameAndFormToInstitucion;
		this.pathOutputFile = LibUtils.replacePattern("%\\{processName\\}", this.nameApp, this.pathOutputFile);
		return LibUtils.createDirs(this.pathOutputFile);
	}

	public boolean createDirViewTabAndFormToInst(String dirNameAndFormToInstitucion, String nameTask) {
		this.pathOutputFile += dirNameAndFormToInstitucion + nameTask + "/tabs/";
		this.pathOutputFile = LibUtils.replacePattern("%\\{processName\\}", this.nameApp, this.pathOutputFile);
		return LibUtils.createDirs(this.pathOutputFile);
	}

	public void replaceNameTask(String nameTask) {
		// Para colocar la variable con el snippet original en la siguiete iteración
		this.snippetFile = this.origSnippetFile;
		this.snippetFile = LibUtils.replacePattern("%\\{nameTask\\}", (this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller"), this.snippetFile);
	}

	public void createValidator(Validator validator , String nameField) {
		String nameValidator=validator.getNameValidator();
		// TODO:Completar los validadores restantes
		if ("mandatory".equalsIgnoreCase(nameValidator)) 
			this.required = "required=\"true\" requiredMessage=\"Requerido\"";
		if ("Readonly".equalsIgnoreCase(nameValidator)) 
			this.readOnly = "readonly=\"true\"";
		if ("LengthValidator".equalsIgnoreCase(nameValidator)) {
			this.validator += "\t<f:validateLength maximum=\""+validator.getMaximun()+"\" minimum=\""+validator.getMinimun()+"\" for=\"" + nameField + "\"/>\n";
		} else if ("EmailValidator".equalsIgnoreCase(nameValidator)) {
			//TODO Agregar el rango maximo y minimo al validador de longitud
			this.validator += "\t<f:validator validatorId=\"EmailValidator\" for=\"" + nameField + "\"/>\n";
		} else if ("CharFieldValidator".equalsIgnoreCase(nameValidator)) {
			this.validator += "\t<f:validator validatorId=\"CharValidator\" for=\"" + nameField + "\"/>\n";
		} else if ("MailValidator".equalsIgnoreCase(nameValidator)) {
			this.validator += "\t<f:validator validatorId=\"EmailValidator\" for=\"" + nameField + "\"/>\n";
		} else if ("DateFieldValidator".equalsIgnoreCase(nameValidator)) {
			this.validator += "\t<f:validator validatorId=\"DateValidator\" for=\"" + nameField + "\"/>\n";
		} else if ("NumericDoubleFieldValidator".equalsIgnoreCase(nameValidator)) {
			this.validator += "\t<f:validator validatorId=\"DoubleValidator\" for=\"" + nameField + "\"/>\n";
		} else if ("NumericFloatFieldValidator".equalsIgnoreCase(nameValidator)) {
			this.validator += "\t<f:validator validatorId=\"FloatValidator\" for=\"" + nameField + "\"/>\n";
		} else if ("NumericIntegerFieldValidator".equalsIgnoreCase(nameValidator)) {
			this.validator += "\t<f:validator validatorId=\"IntegerValidator\" for=\"" + nameField + "\"/>\n";
		} else if ("NumericLongFieldValidator".equalsIgnoreCase(nameValidator)) {
			this.validator += "\t<f:validator validatorId=\"LongValidator\" for=\"" + nameField + "\"/>\n";
		}else if ("PhoneNumberValidator".equalsIgnoreCase(nameValidator)) {
			this.validator += "\t<f:validator validatorId=\"PhoneValidator\" for=\"" + nameField + "\"/>\n";
		}else if(!("mandatory".equalsIgnoreCase(nameValidator)||"readonly".equalsIgnoreCase(nameValidator))) {
			this.validator += "\t<f:validator validatorId=\""+ this.nameApp + "_" + LibUtils.firstLetterLower(nameValidator)+"\" for=\"" + nameField + "\"/>\n";
		}
	}

	public void createInputElement(Field field, String nPage) {

		String inputElement = translateField2View(field, nPage);

		this.inputElements += inputElement;
	}

	private void replaceInputsElements() {
		this.snippetFile = LibUtils.replacePattern("%\\{inputsElements\\}", this.inputElements, this.snippetFile);
	}

	// Traducción de los elmentos del xml a la vista JSF
	private String translateField2View(Field field, String nPage) {

		// TODO: Completar el resto de tipos de datos del campo en el formulario
		String label = field.getLabelField();
		// String name = field.getNameField();
		String name = field.getVarName();
		String type = field.getTypeField();
		@SuppressWarnings("unused")
		String toolTip = field.getDescription();

		String fieldInput = "";
		
		// TODO:Arreglar aqui estan definidas los readonly de las variables transversales
		if ("nombre".equalsIgnoreCase(name) || "apellido".equalsIgnoreCase(name) ||
				"email".equalsIgnoreCase(name)	|| "nacionalidad".equalsIgnoreCase(name) || "cedula".equalsIgnoreCase(name) ||
				"sexo".equalsIgnoreCase(name) || "edo_civil".equalsIgnoreCase(name)||"estado".equalsIgnoreCase(name)||
				"municipio".equalsIgnoreCase(name)||"parroquia".equalsIgnoreCase(name)||"direccion".equalsIgnoreCase(name)||"pais".equalsIgnoreCase(name)
				||"tlf_local".equalsIgnoreCase(name)||"tlf_movil".equalsIgnoreCase(name)
		){
				this.readOnly = "readonly=\"true\"";
		}	
		
		if("f_nacimiento".equalsIgnoreCase(name)){
			
			this.readOnly = "showOn=\"none\" readonly=\"true\" disabled=\"true\" ";
			
		}
		// Construye el label
		if (!"BUTTON_SUBMIT".equalsIgnoreCase(type) && !"BUTTON_NEXT".equalsIgnoreCase(type) && !"BUTTON_PREVIOUS".equalsIgnoreCase(type) && !"_Datos_Basicos".contentEquals(name)) {
			if (name.matches("^_FILE_.*$")) {
				fieldInput = "<b:column span='6'><h:outputLabel for=\"" + name + "\" value=\"" + label.replace("_FILE_", "") + "\"/>\n";
			}else if("LISTBOX_SIMPLE".equalsIgnoreCase(type)&&name.startsWith("c_")){
				fieldInput = "<b:column span='6'><div class=\"form-group\"><h:outputLabel for=\"" + name + "\" value=\"" + label.replace("_FILE_", "") + "\"/>\n";
			}else {
				fieldInput = "<b:column span='6'><h:outputLabel for=\"" + name + "\" value=\"" + label+ "\"/>\n";
			}
		}
		

			

		if ("TEXTBOX".equalsIgnoreCase(type) && !"_Datos_Basicos".contentEquals(name)) {

			if (name.matches("^_FILE_.*$")) {
				fieldInput += "<p:fileUpload id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.uploadfile}\" ";
				fieldInput += " update=\"t" + nPage + "\" mode=\"advanced\" auto=\"true\" ";
				fieldInput += " fileUploadListener=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.fileUploadListener}\" ";
				fieldInput += " allowTypes=\"/(\\\\.|\\\\/)(pdf)\\$/\" label=\""+field.getLabelField()+"\" ";
				fieldInput += " sizeLimit=\"2097152\" multiple=\"false\" " + this.required + " ";
				fieldInput += " invalidFileMessage=\"Solo se permiten archivos con extensión PDF\" ";
				fieldInput += " invalidSizeMessage=\"El archivo no puede superar los 2MB\"> ";
				fieldInput += " </p:fileUpload>";
			} else {
				fieldInput += "<b:inputText id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" " + this.required + " " + this.readOnly + ">\n";
				fieldInput += this.validator;
				fieldInput += "</b:inputText>\n";
			}

		} else if ("DATE".equalsIgnoreCase(type)) {

			fieldInput += "<b:datepicker id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" locale=\"es\" navigator=\"true\" pattern=\"dd-mm-yyyy\" mode=\"icon-popup\" ";
			fieldInput += this.required + " " + this.readOnly + ">\n";
			fieldInput += this.validator;
			fieldInput += "</b:datepicker>\n";

		} else if ("CHECKBOX".equalsIgnoreCase(type)) {

		} else if ("HIDDEN".equalsIgnoreCase(type)) {
			if (name.matches("^_FILE_.*$")) {
				if ("".equals(this.readOnly)) {
					fieldInput += "<p:fileUpload id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" mode=\"simple\"/>\n";
				} else {
					fieldInput += "<p:commandButton id=\"" + name + "\" value=\"Bajar Archivo\" ajax=\"false\" icon=\"ui-icon-arrowthick-1-s\">\n";
					fieldInput += "\t<p:fileDownload value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.getFile()}\" />\n";
					fieldInput += "\t<f:param name=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" />\n";
					fieldInput += "</p:commandButton>\n";
				}
			}
		} else if ("PASSWORD".equalsIgnoreCase(type)) {

		} else if ("TEXTAREA".equalsIgnoreCase(type)) {

			fieldInput += "<p:inputTextarea id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" rows=\"5\" cols=\"50\" counter=\"display\" ";
			fieldInput += "maxlength=\"255\" counterTemplate=\"Restan {0} caracteres.\" " + this.required + " " + this.readOnly + "/>\n";
			fieldInput += this.validator;
			fieldInput += "<h:outputText id=\"display\"/>";

		}else if("LISTBOX_SIMPLE".equalsIgnoreCase(type)&&name.startsWith("c_")){ 			
			String[] name_field=name.split("_");
			String combo=name_field[1];			
			String ajax="";
			if(field.getOptionValue().get(0)!=null&&!field.getOptionValue().isEmpty()&&!field.getOptionValue().get(0).equals("")){		
				ajax="<p:ajax listener=\"#{"+this.nameApp + "_" + combo+".getFind"+name+"("+ this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name +")}\"  update=\""+field.getOptionValue().get(0)+"\" />\n";
			}			
			fieldInput +="<p:selectOneMenu styleClass=\"form-control\" style = \"width:100%;padding:0px 0px;\" id=\""+name+"\" value=\"#{"+ this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" ";
			fieldInput +=this.required+" "+this.readOnly+" >\n";
			fieldInput +=this.validator;
			fieldInput +="\t"+ajax;			
			fieldInput +="\t<f:selectItem itemLabel=\"Selecione el "+label+"\" noSelectionOption=\"true\" />\n";
			fieldInput +="\t<f:selectItems value=\"#{"+this.nameApp + "_" +combo+"."+name+"}\" />\n";	
			fieldInput +="</p:selectOneMenu>\n";			
		}else if ("LISTBOX_SIMPLE".equalsIgnoreCase(type)) {
			fieldInput += "<b:selectMultiMenu id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" nonSelectedText=\"Por Favor Selecione\" maxSelectedText=\"Por Favor Selecione\" filterMatchMode=\"startsWith\" buttonWidth=\"250px\"\n";
			fieldInput += this.required + " " + this.readOnly + ">\n";
			fieldInput += this.validator;
			fieldInput += "\t<f:selectItems value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "Option" + "}\" />\n";
			fieldInput += "</b:selectMultiMenu>\n";

		} else if ("RICH_TEXTAREA".equalsIgnoreCase(type)) {

		} else if ("FILEUPLOAD".equalsIgnoreCase(type)) {

		} else if ("MESSAGE".equalsIgnoreCase(type)) {

		} else if ("CHECKBOX_GROUP".equalsIgnoreCase(type)) {
			
			fieldInput += "<b:selectMultiMenu id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" allSelectedText=\"Todos los Elementos Seleccionados\" nSelectedText=\"Elementos Seleccionados\" nonSelectedText=\"Seleccione sus Opciones\" buttonWidth=\"550\" \n";
			fieldInput += this.required + " " + this.readOnly + ">\n";
			fieldInput += this.validator;
			fieldInput += "\t<f:selectItems value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "Option" + "}\" />\n";
			fieldInput += "</b:selectMultiMenu>\n";

		} else if ("DURATION".equalsIgnoreCase(type)) {

		} else if ("LISTBOX_MULTIPLE".equalsIgnoreCase(type)) {

			if (name.matches("^_FILE_.*$")) {
				fieldInput += "<p:fileUpload id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.uploadfile}\"";
				fieldInput += " update=\"t" + nPage + "\" mode=\"advanced\" auto=\"true\"";
				fieldInput += " fileUploadListener=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.fileUploadListener}\"";
				fieldInput += " allowTypes=\"/(\\\\.|\\\\/)(pdf)\\$/\" label=\"Subir documento\" ";
				fieldInput += " sizeLimit=\"2097152\" multiple=\"true\" " + this.required;
				fieldInput += " invalidFileMessage=\"Solo se permiten archivos con extensión PDF\"";
				fieldInput += " invalidSizeMessage=\"El archivo no puede superar los 2MB\">";
				fieldInput += "<f:attribute name=\"limiteDeArchivos\" value=\"10\" /></p:fileUpload>";
			} else {

				fieldInput += "<b:selectMultiMenu id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" allSelectedText=\"Todos los Elementos Seleccionados\" nSelectedText=\"Elementos Seleccionados\" nonSelectedText=\"Seleccione sus Opciones\" buttonWidth=\"550\" \n";
				fieldInput += this.required + " " + this.readOnly + ">\n";
				fieldInput += this.validator;
				fieldInput += "\t<f:selectItems value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "Option" + "}\" />\n";
				fieldInput += "</b:selectMultiMenu>\n";
			}

		} else if ("RADIOBUTTON_GROUP".equalsIgnoreCase(type)) {
			fieldInput += "<b:selectOneMenu id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" ";
			fieldInput += this.required + " " + this.readOnly + ">\n";
			fieldInput += this.validator;
			fieldInput += "\t<f:selectItems value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "Option" + "}\" itemLabel=\"#{" + name + "}\" itemValue=\"#{" + name + "}\"/>\n";
			fieldInput += "</b:selectOneMenu>\n";
		} else if ("SUGGESTBOX".equalsIgnoreCase(type)) {

		}

		// Contruye el mensaje
		if (!"BUTTON_SUBMIT".equalsIgnoreCase(type) && !"BUTTON_NEXT".equalsIgnoreCase(type) && !"BUTTON_PREVIOUS".equalsIgnoreCase(type) && !"_Datos_Basicos".contentEquals(name)) {
			if(!"TEXTAREA".equalsIgnoreCase(type))
				fieldInput += "<h:outputText />";
			fieldInput += "<p:message for=\"" + name + "\" id=\"" + name + "Message\" style=\"color:red\"/>\n";
			fieldInput += "<p:tooltip id=\"toolTip" + LibUtils.firstLetterUpper(name) + "\" for=\"" + name + "\" value=\"" + field.getDescription() + "\" />\n";
			if("LISTBOX_SIMPLE".equalsIgnoreCase(type)&&name.startsWith("c_"))	
				fieldInput += "</div></b:column>";
			else
				fieldInput += "</b:column>";
			
		}
		fieldInput +="\n";
		this.readOnly="";
//		this.required="";
		return fieldInput;
	}

	public void createButton(Field field) {

		String button = translateButton2View(field);

		this.button += button;
	}

	private void replacebutton() {

		this.snippetFile = LibUtils.replacePattern("%\\{submitButton\\}", this.button, this.snippetFile);
	}

	private String translateButton2View(Field field) {

		// TODO:Completar los tipos de botones
		String type = field.getTypeField();
		String buttoInput = "";

		String controllerNameSubmit = getControllersNameSubmitMap().get(getNameBean());
		String controllerIdSubmit = getControllersIdSubmitMap().get(getNameBean());

		if ("BUTTON_SUBMIT".equalsIgnoreCase(type)) {

			buttoInput += "<h:commandButton value=\"Enviar\" action=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.executeTask}\">\n";
			buttoInput += "\t<f:param name=\"" + controllerIdSubmit + "\" value=\"#{" + controllerNameSubmit + "." + controllerIdSubmit + "}\"/>\n";
			buttoInput += "</h:commandButton>";

		} else if ("BUTTON_NEXT".equalsIgnoreCase(type)) {

		} else if ("BUTTON_PREVIOUS".equalsIgnoreCase(type)) {

		}

		return buttoInput;
	}

	public void writeFileAndCreateDirToView(String nameViewFile) {

		LibUtils.createDirs(this.pathOutputFile + "/" + nameViewFile + "/");
		replaceOutputsElements();
		replaceInputsElements();
		replacebutton();

		this.wf.writeFile(this.pathOutputFile + "/" + nameViewFile + "/" + nameViewFile + ".xhtml", this.snippetFile);
	}

	public void writeFileAndCreateViewSuccess(String dirNameAndFormToInstitucion) {
		String dirViewSuccess = this.pathOutputFile;
		dirViewSuccess += LibUtils.replacePattern("%\\{processName\\}", this.nameApp, dirNameAndFormToInstitucion);
		this.wf.writeFile(dirViewSuccess + "/success.xhtml", this.origSnippetFile);
	}
	
	public void writeFileAndCreateTemplatePDF(String dirNameAndFormToInstitucion) {
		String dirViewSuccess = this.pathOutputFile;
		this.snippetFile = this.origSnippetFile;
		dirViewSuccess += LibUtils.replacePattern("%\\{processName\\}", this.nameApp, dirNameAndFormToInstitucion);
		this.snippetFile = LibUtils.replacePattern("%\\{outputsTableElements\\}", this.outputsTableElements, this.snippetFile);
		this.wf.writeFile(dirViewSuccess + "/carga/templatePDF.xhtml", this.snippetFile);
	}

	public void writeFileAndCreateDirToView(String nameViewFile, String tab) {

		replaceOutputsElements();
		replaceInputsElements();
		replacebutton();

		this.wf.writeFile(this.pathOutputFile + "/" + tab + ".xhtml", this.snippetFile);
	}

	public void createTabReference(String nameViewFile, String tab, String dirNameAndFormToInstitucion) {
		String temp = "";
		temp = LibUtils.replacePattern("%\\{processName\\}", this.nameApp, dirNameAndFormToInstitucion);
		this.snippetFile = this.origSnippetFile;
		this.tabsReferences += "<ui:include src=\"/views/" + temp + nameViewFile + "/tabs/" + tab + ".xhtml\"/>\n";
	}

	public void replaceTabsReferences() {

		this.snippetFile = LibUtils.replacePattern("%\\{tabsElements\\}", this.tabsReferences, this.snippetFile);
	}

	public void replaceIdAndNameTab(String id, String name) {
		this.snippetFile = LibUtils.replacePattern("%\\{tabId\\}", id, this.snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{namePage\\}", name, this.snippetFile);
	}

	public void insertMessageTag(String tab, boolean containsFiles) {
		if (containsFiles) {
			String tag = "<p:messages id=\"messages" + tab + "\" showDetail=\"true\" autoUpdate=\"true\" closable=\"true\" />";
			this.snippetFile = LibUtils.replacePattern("%\\{messages\\}", tag, this.snippetFile);
		} else
			this.snippetFile = LibUtils.replacePattern("%\\{messages\\}", "", this.snippetFile);
	}

	public void insertFileTable(String tab, boolean containsFiles) {
		String table = "", rendered = "", delete = "";		
		if (containsFiles) {
			table = "<p:dataTable id=\"t" + tab + "\" var=\"list\"	emptyMessage=\"#{i18n['emptyTable']}\" "  +  "value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.beanFiles}\" " + "rowIndexVar=\"index\"> " + "<f:facet name=\"header\">Documentos anexos</f:facet>	"
					+ "<p:column headerText=\"Nombre del documento\" " + rendered + ">" + "<h:outputText value=\"#{list.nombreOficial}\" />" + "</p:column>" + "<p:column headerText=\"Archivos\" " + rendered + "> " + "<p:dataTable var=\"archivos\" value=\"#{list.fileNames}\" rowIndexVar=\"index2\">"
					+ "<p:column><h:outputText value=\"#{archivos}\"></h:outputText></p:column>" + delete + "</p:dataTable>" + "</p:column>" + "</p:dataTable>";
		}else if (!tab.contentEquals("TabResumen")) {
			rendered = "rendered=\"#{list.tab == 'tab" + tab + "'}\"";
			delete = "<p:column>" + "<p:commandButton actionListener=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.removeFile(index,index2)}\"" + " value=\"Suprimir\" update=\":form:t" + tab + "\">" + "</p:commandButton>" + "</p:column>";
		}
		
		this.snippetFile = LibUtils.replacePattern("%\\{dataTable\\}", table, this.snippetFile);
	}

	public void createOutputElement(Field field) {

		String label = field.getLabelField();
		String name = field.getVarName();
		String type = field.getTypeField();

		if (!"BUTTON_SUBMIT".equalsIgnoreCase(type) && !"BUTTON_NEXT".equalsIgnoreCase(type) && !"BUTTON_PREVIOUS".equalsIgnoreCase(type)) {

			String outputElement = "<h:outputLabel for=\"" + name + "_info\" value=\"" + label + "\"/>\n";
			outputElement += "<h:outputText id=\"" + name + "_info\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\">\n";
			if(field.getReturnType().contentEquals("java.util.Date"))
				outputElement += "<f:convertDateTime pattern=\"dd/MM/yyyy\"/>\n";
			outputElement += "</h:outputText>\n";
			this.outputElements += outputElement;
		}
	}
	
	public void createOutputsTableElements(Field field) {

		String label = field.getLabelField();
		String name = field.getVarName();
		String type = field.getTypeField();

		if (!"BUTTON_SUBMIT".equalsIgnoreCase(type) && !"BUTTON_NEXT".equalsIgnoreCase(type) && !"BUTTON_PREVIOUS".equalsIgnoreCase(type)) {

			String outputElement = "<tr>\n<td><label style=\"font-weight:bold;\">"+label+"</label></td> \n";
			outputElement += "<td><label>%{"+name+"}</label></td>\n</tr>\n";
			this.outputsTableElements += outputElement;
		}
	}

	public void createDocumentElement(Field field) {

		String label = field.getLabelField();
		String name = field.getVarName();
		String controller = this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller";
		String renderedvalueBean = "#{" + controller + ".bean." + name + "!=null and not empty " + controller + ".bean." + name + " and " + controller + ".bean." + name + "!='[]' }";
		String valueBean = "#{" + controller + ".bean." + name + "}";
		String outputElement = "";

		//outputElement += "<p:row rendered=\"" + renderedvalueBean + "\">\n";
		//outputElement += "<p:column rendered=\"" + renderedvalueBean + "\"><h:outputLabel value=\"" + label + "\" /></p:column>\n";
		outputElement += "<h:outputLabel value=\"" + label + "\" rendered=\"" + renderedvalueBean + "\" />\n";
		
		outputElement += "<p:column rendered=\"" + renderedvalueBean + "\">\n";
		if ("java.util.List".contentEquals(field.getReturnType())) {
			outputElement += "<p:dataTable var=\"archivos\" rowIndexVar=\"index\" rendered=\"" + renderedvalueBean + "\" value=\"" + valueBean + "\">\n";
			outputElement += "<p:column>\n";
			outputElement += "<p:commandButton value=\"Documento n° #{index+1}\" onclick=\"PF('" + name + "_#{index+1}').show();\" rendered=\"" + renderedvalueBean + "\"></p:commandButton>\n";
			outputElement += "<p:dialog position=\"top\" header=\"Visor PDF\" id=\"" + name + "_#{index+1}\" widgetVar=\"" + name + "_#{index+1}\" resizable=\"false\" \n";
			outputElement += "rendered=\"" + renderedvalueBean + "\"> \n";
			outputElement += "<p:media width=\"900px\" height=\"500px\" player=\"pdf\" \n";
			outputElement += "value=\"#{" + controller + "._FILE}\">\n";
			outputElement += "<f:param name=\"id\" value=\"#{archivos}\" />\n";
			outputElement += "<p:outputPanel layout=\"block\"><h:form> Su navegador es incompatible para la visualización de documentos PDF. Para poder \n";
			outputElement += "examinar el documento, debe actualizar su navegador a una versión que pemita visualizar \n";
			outputElement += "archivos de extensión pdf directamente en el navegador o descargue el documento y utilice el visor de su sistema operativo.\n";
			outputElement += "archivos de extensión pdf directamente en el navegador o descargue el documento y utilice el visor de su sistema operativo.\n";
			outputElement += "<p:commandButton value=\"Bajar Archivo\" ajax=\"false\" icon=\"ui-icon-arrowthick-1-s\">";
			outputElement += "<p:fileDownload value=\"#{" + controller + ".getDownloadFile()}\" />";
			outputElement += "<f:param name=\"id\" value=\"#{archivos}\" />\n";
			outputElement += "</p:commandButton>\n</h:form>\n</p:outputPanel>\n</p:media>\n</p:dialog>\n";
			outputElement += "</p:column>\n";
			outputElement += "</p:dataTable>\n";

		} else {
			outputElement += "<p:commandButton value=\"Visor PDF.\" onclick=\"PF('" + name + "').show();\" rendered=\"" + renderedvalueBean + "\"></p:commandButton>\n";
			outputElement += "<p:dialog position=\"top\" header=\"Visor PDF\" id=\"" + name + "\" widgetVar=\"" + name + "\" resizable=\"false\" \n";
			outputElement += "rendered=\"" + renderedvalueBean + "\"> \n";
			outputElement += "<p:media width=\"900px\" height=\"500px\" player=\"pdf\" \n";
			outputElement += "value=\"#{" + controller + "._FILE}\">\n";
			outputElement += "<f:param name=\"id\" value=\"" + valueBean + "\" />\n";
			outputElement += "<p:outputPanel layout=\"block\"><h:form> Su navegador es incompatible para la visualización de documentos PDF. Para poder \n";
			outputElement += "examinar el documento, debe actualizar su navegador a una versión que pemita visualizar \n";
			outputElement += "archivos de extensión pdf directamente en el navegador o descargue el documento y utilice el visor de su sistema operativo.\n";
			outputElement += "archivos de extensión pdf directamente en el navegador o descargue el documento y utilice el visor de su sistema operativo.\n";
			outputElement += "<p:commandButton value=\"Bajar Archivo\" ajax=\"false\" icon=\"ui-icon-arrowthick-1-s\">";
			outputElement += "<p:fileDownload value=\"#{" + controller + ".getDownloadFile()}\" />";
			outputElement += "<f:param name=\"id\" value=\"" + valueBean + "\" />\n";
			outputElement += "</p:commandButton>\n</h:form>\n</p:outputPanel>\n</p:media>\n</p:dialog>\n";
		}
		outputElement += "</p:column>\n";

		//outputElement += "</p:row>\n";
		
		this.documentElements += outputElement;

	}

	public void insertInputHidden(int nTabsWithFiles, Map<String, String> archivos,Map<String, String> obligatorios) {

		String filesInTabs = "";
		if (nTabsWithFiles > 0) {
			filesInTabs += "<h:inputHidden id=\"files\"> ";
			filesInTabs += "<f:attribute name=\"tabs\" value=\"" + nTabsWithFiles + "\" /> ";
			String tabsNames = "";
			Iterator<String> ite = archivos.keySet().iterator();
			while (ite.hasNext()) {
				String tab = ite.next();
				filesInTabs += "<f:attribute name=\"" + tab + "\" value=\"" + archivos.get(tab) + "\" /> ";
				if (tabsNames.length() == 0)
					tabsNames = tab;
				else
					tabsNames += ","+tab;
			}
			ite = obligatorios.keySet().iterator();
			while (ite.hasNext()) {
				String variable = ite.next();
				filesInTabs += "<f:attribute name=\"" + variable + "\" value=\"" + obligatorios.get(variable) + "\" /> ";
				
			}
			filesInTabs += "<f:attribute name=\"tabsNames\" value=\"" + tabsNames + "\" /> ";

			filesInTabs += "</h:inputHidden>";
		}
		
		
		this.snippetFile = LibUtils.replacePattern("%\\{filesInTabs\\}", filesInTabs, this.snippetFile);
		
	}
	
	private void replaceOutputsElements() {

		this.snippetFile = LibUtils.replacePattern("%\\{outputsElements\\}", this.outputElements, this.snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{documentElements\\}", this.documentElements, this.snippetFile);
	}
	
	public void replaceNameProcessBean() {		
		this.snippetFile = this.origSnippetFile;
		this.snippetFile = LibUtils.replacePattern("%\\{nameProcessBean\\}", (this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean())), this.snippetFile);
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

	public void setButton(String button) {
		this.button = button;
	}

	public String getTabsReferences() {
		return tabsReferences;
	}

	public void setTabsReferences(String tabsReferences) {
		this.tabsReferences = tabsReferences;
	}

	public String getOutputsTableElements() {
		return outputsTableElements;
	}

	public void setOutputsTableElements(String outputsTableElements) {
		this.outputsTableElements = outputsTableElements;
	}

}

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
	private String tabsReferences = "";
	private String outputElements = "";

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

	public void createValidator(String nameValidator, String nameField) {

		// TODO:Completar los validadores restantes

		if ("mandatory".equalsIgnoreCase(nameValidator)) {
			this.required = "required=\"true\" requiredMessage=\"Requerido\"";
		}
		if ("readonly".equalsIgnoreCase(nameValidator)) {
			this.readOnly = "readonly=\"true\"";
		} else if ("LengthValidator".equalsIgnoreCase(nameValidator)) {
			this.validator += "\t<f:validateLength maximum=\"20\" minimum=\"3\" for=\"" + nameField + "\"/>\n";
		} else if ("EmailValidator".equalsIgnoreCase(nameValidator)) {
			this.validator += "\t<f:validator validatorId=\"ve.gob.cnti.gestion.utils.validators.EmailValidator\" for=\"" + nameField + "\"/>\n";
		} else if ("CharFieldValidator".equalsIgnoreCase(nameValidator)) {

		} else if ("NumericIntegerFieldValidator".equalsIgnoreCase(nameValidator)) {

		} else if ("MailValidator".equalsIgnoreCase(nameValidator)) {
			this.validator += "\t<f:validator validatorId=\"ve.gob.cnti.gestion.utils.validators.EmailValidator\" for=\"" + nameField + "\"/>\n";
		} else if ("DateFieldValidator".equalsIgnoreCase(nameValidator)) {

		} else if ("NumericDoubleFieldValidator".equalsIgnoreCase(nameValidator)) {

		} else if ("NumericFloatFieldValidator".equalsIgnoreCase(nameValidator)) {

		} else if ("NumericIntegerFieldValidator".equalsIgnoreCase(nameValidator)) {

		} else if ("NumericLongFieldValidator".equalsIgnoreCase(nameValidator)) {

		}
	}

	public void createInputElement(Field field) {

		String inputElement = translateField2View(field);

		this.inputElements += inputElement;
	}

	private void replaceInputsElements() {

		this.snippetFile = LibUtils.replacePattern("%\\{inputsElements\\}", this.inputElements, this.snippetFile);
	}

	// Traducción de los elmentos del xml a la vista JSF
	private String translateField2View(Field field) {

		// TODO: Completar el resto de tipos de datos del campo en el formulario
		String label = field.getLabelField();
		// String name = field.getNameField();
		String name = field.getVarName();
		String type = field.getTypeField();

		String fieldInput = "";

		// Construye el label
		if (!"BUTTON_SUBMIT".equalsIgnoreCase(type) && !"BUTTON_NEXT".equalsIgnoreCase(type) && !"BUTTON_PREVIOUS".equalsIgnoreCase(type)) {
			if (name.matches("^_FILE_.*$")) {
				fieldInput = "<p:outputLabel for=\"" + name + "\" value=\"" + label.replace("_FILE_", "") + "\"/>\n";
			} else {
				fieldInput = "<p:outputLabel for=\"" + name + "\" value=\"" + label.replace("_FILE_", "") + "\"/>\n";
			}
		}

		if ("TEXTBOX".equalsIgnoreCase(type)) {

			fieldInput += "<p:inputText id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" " + this.required + " " + this.readOnly + ">\n";
			fieldInput += this.validator;
			fieldInput += "</p:inputText>\n";

		} else if ("DATE".equalsIgnoreCase(type)) {

			fieldInput += "<p:calendar id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" locale=\"es\" navigator=\"true\" pattern=\"dd-mm-yyyy\" ";
			fieldInput += this.required + " " + this.readOnly + ">\n";
			fieldInput += this.validator;
			fieldInput += "</p:calendar>\n";

		} else if ("CHECKBOX".equalsIgnoreCase(type)) {

		} else if ("HIDDEN".equalsIgnoreCase(type)) {
			if (name.matches("^_FILE_.*$")) {
				if ("".equals(this.readOnly)) {
					fieldInput += "<p:fileUpload id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" mode=\"simple\"/>\n";
					System.out.println("subiendo");
				} else {
					System.out.println("bajando");
					fieldInput += "<p:commandButton id=\"" + name + "\" value=\"Bajar Archivo\" ajax=\"false\" icon=\"ui-icon-arrowthick-1-s\">\n";
					fieldInput += "\t<p:fileDownload value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.getFile()}\" />\n";
					fieldInput += "\t<f:param name=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" />\n";
					fieldInput += "</p:commandButton>\n";
				}
			}
		} else if ("PASSWORD".equalsIgnoreCase(type)) {

		} else if ("TEXTAREA".equalsIgnoreCase(type)) {

			fieldInput += "<p:inputTextarea id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" rows=\"5\" cols=\"50\" counter=\"display\" ";
			fieldInput += "maxlength=\"255\" counterTemplate=\"Restan {0} caracteres.\"" + this.required + " " + this.readOnly + "/>\n";
			fieldInput += this.validator;
			fieldInput += "<h:outputText id=\"display\"/>";

		} else if ("LISTBOX_SIMPLE".equalsIgnoreCase(type)) {
			fieldInput += "<p:selectOneMenu id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\"\n";
			fieldInput += this.required + " " + this.readOnly + ">\n";
			fieldInput += this.validator;
			fieldInput += "<f:selectItems itemLabel=\"\" itemValue=\"\" />\n";
			fieldInput += "\t<f:selectItems value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "Option" + "}\"/>\n";
			fieldInput += "</p:selectOneMenu>\n";

		} else if ("RICH_TEXTAREA".equalsIgnoreCase(type)) {

		} else if ("FILEUPLOAD".equalsIgnoreCase(type)) {

		} else if ("MESSAGE".equalsIgnoreCase(type)) {

		} else if ("CHECKBOX_GROUP".equalsIgnoreCase(type)) {

		} else if ("DURATION".equalsIgnoreCase(type)) {

		} else if ("LISTBOX_MULTIPLE".equalsIgnoreCase(type)) {

			fieldInput += "<p:selectCheckboxMenu id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" filter=\"true\" filterMatchMode=\"startsWith\" panelStyle=\"width:250px\"\n";
			fieldInput += this.required + " " + this.readOnly + ">\n";
			fieldInput += this.validator;
			fieldInput += "\t<f:selectItems value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "Option" + "}\" />\n";
			fieldInput += "</p:selectCheckboxMenu>\n";

		} else if ("RADIOBUTTON_GROUP".equalsIgnoreCase(type)) {
			fieldInput += "<p:selectOneRadio id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\" ";
			fieldInput += this.required + " " + this.readOnly + ">\n";
			fieldInput += this.validator;
			fieldInput += "\t<f:selectItems value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "Option" + "}\" itemLabel=\"#{" + name + "}\" itemValue=\"#{" + name + "}\"/>\n";
			fieldInput += "</p:selectOneRadio>\n";
		} else if ("SUGGESTBOX".equalsIgnoreCase(type)) {

		}

		// Contruye el mensaje
		if (!"BUTTON_SUBMIT".equalsIgnoreCase(type) && !"BUTTON_NEXT".equalsIgnoreCase(type) && !"BUTTON_PREVIOUS".equalsIgnoreCase(type)) {
			if (name.matches("^_FILE_.*$")) {
				if (!"".equals(this.readOnly)) {
					fieldInput += "<p:messages for=\"" + name + "\" id=\"" + name + "Message\" style=\"color:red\" showDetail=\"true\"/>\n";
				} else {
					fieldInput += "<p:message for=\"" + name + "\" id=\"" + name + "Message\" style=\"color:red\"/>\n";
				}
			} else {
				fieldInput += "<p:message for=\"" + name + "\" id=\"" + name + "Message\" style=\"color:red\"/>\n";
			}
		}
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

		replaceInputsElements();
		replacebutton();

		this.wf.writeFile(this.pathOutputFile + "/" + nameViewFile + "/" + nameViewFile + ".xhtml", this.snippetFile);
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
		this.tabsReferences += "<ui:include src=\"" + temp + nameViewFile + "/tabs/" + tab + ".xhtml\"/>\n";
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
		String table = "";
		if (containsFiles) {
			table = "<p:dataTable id=\"table" + tab + "\" var=\"list\"	" + "value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.fileNames}\" " + "rowIndexVar=\"index\"> " + "<f:facet name=\"header\">Documentos anexos</f:facet>	"
					+ "<p:column headerText=\"N°\"> <h:outputText value=\"#{index+1}\" /> </p:column> " + "<p:column headerText=\"Nombre del documento\"> " + "<h:outputText value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean())
					+ "Controller.fileDocs.get(index).toString()}\" /> " + "</p:column> " + "<p:column headerText=\"Archivo\"> <h:outputText value=\"#{list}\" /> </p:column> " + "</p:dataTable>";
		}
		this.snippetFile = LibUtils.replacePattern("%\\{dataTable\\}", table, this.snippetFile);
	}

	public void createOutputElement(Field field) {

		String label = field.getLabelField();
		String name = field.getVarName();
		String type = field.getTypeField();

		if (!"BUTTON_SUBMIT".equalsIgnoreCase(type) && !"BUTTON_NEXT".equalsIgnoreCase(type) && !"BUTTON_PREVIOUS".equalsIgnoreCase(type)) {

			String outputElement = "<p:outputLabel for=\"" + name + "\" value=\"" + label + "\"/>\n";
			outputElement += "<p:outputLabel id=\"" + name + "\" value=\"#{" + this.nameApp + "_" + LibUtils.firstLetterLower(getNameBean()) + "Controller.bean." + name + "}\"/>\n";
			this.outputElements += outputElement;
		}
	}

	private void replaceOutputsElements() {

		this.snippetFile = LibUtils.replacePattern("%\\{outputsElements\\}", this.outputElements, this.snippetFile);
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

}

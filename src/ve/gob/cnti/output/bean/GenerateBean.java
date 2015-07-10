package ve.gob.cnti.output.bean;

import java.util.List;

import ve.gob.cnti.helper.form.Field;
import ve.gob.cnti.helper.output.WriteFile;
import ve.gob.cnti.helper.util.LibUtils;

public class GenerateBean {

	private String snippetFile = null;
	private String origSnippetFile = null;
	private WriteFile wf = null;
	private String pathOutputFile = null;

	private String imports = null;
	private String attributes = null;
	private String values = null;
	private String setAndGetMethods = null;
	private String nameApp = null;

	public GenerateBean(String pathFile, String pathOutputFile, String nameApp) {

		this.origSnippetFile = LibUtils.readSnippetFile(pathFile);
		this.pathOutputFile = pathOutputFile;
		this.wf = new WriteFile();
		this.nameApp = nameApp;
	}

	public void setImports(String imports) {
		this.imports = imports;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public void setSetAndGetMethods(String setAndGetMethods) {
		this.setAndGetMethods = setAndGetMethods;
	}

	public void replaceNameBeanAndNameClassBean(String nameBean) {

		// Para colocar la variable con el snippet original en la siguiente iteración
		this.snippetFile = this.origSnippetFile;

		if (!Character.isLowerCase(nameBean.charAt(0))) {
			this.snippetFile = LibUtils.replacePattern("%\\{nameBean\\}", this.nameApp + "_" + LibUtils.firstLetterLower(nameBean), this.snippetFile);
			this.snippetFile = LibUtils.replacePattern("%\\{NameBean\\}", nameBean, this.snippetFile);
		} else {
			this.snippetFile = LibUtils.replacePattern("%\\{nameBean\\}", this.nameApp + "_" + nameBean, this.snippetFile);
			nameBean = LibUtils.firstLetterUpper(nameBean);
			this.snippetFile = LibUtils.replacePattern("%\\{NameBean\\}", nameBean, this.snippetFile);
		}
		if(nameBean.contentEquals("Carga")){
			this.snippetFile = LibUtils.replacePattern("%\\{anotacion\\}", "@BeanIsInitCase",this.snippetFile);
		}else{
			this.snippetFile = LibUtils.replacePattern("%\\{anotacion\\}", "",this.snippetFile);
		}
	}

	public void replacePackagesNameBean(String packagesName) {
		packagesName = LibUtils.replacePattern("%\\{processName\\}", this.nameApp, packagesName);
		this.snippetFile = LibUtils.replacePattern("%\\{packages\\}", packagesName, this.snippetFile);
	}

	public void createImpAttAndMethos(Field field) {

		if (!"".equals(field.getReturnType())) {

			if (!field.getReturnType().matches("java\\.lang\\..*") && !this.imports.contains(field.getReturnType())) {
				this.imports += createImports(field.getReturnType());
			}

			String nameField = field.getVarName();
			String typeField = field.getTypeField();
			String returnTypes = field.getReturnType().substring(field.getReturnType().lastIndexOf(".") + 1);
			List<String> nameOptions = field.getOptionValue();
			if(nameOptions != null)
				this.values += createMethodSelectValue(nameOptions) + "\n";
			
			this.attributes += createAttribute(nameField, returnTypes, typeField, field.isActuation(), nameOptions);
			this.setAndGetMethods += createSetMethod(nameField, returnTypes, typeField) + "\n";
			this.setAndGetMethods += createGetMethod(nameField, returnTypes, typeField) + "\n";
		}

	}

	public void replaceVaraiablesAndWriteFile(String nameBean) {

		this.snippetFile = LibUtils.replacePattern("%\\{imports\\}", this.imports, snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{attributes\\}", this.attributes, snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{values\\}", this.values, snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{setAndGetMethods\\}", this.setAndGetMethods, snippetFile);

		this.wf.writeFile(this.pathOutputFile + "/" + LibUtils.firstLetterUpper(nameBean) + ".java", this.snippetFile);
	}

	// Convierte el nombre del paquetre definido en el configuracion.properties en una ruta de directorio valida
	/*
	 * Ejemplo: entrada ve.gob.vicepresidencia.jubilacionespecial.model salida /ve/gob/vicepresidencia/jubilacionespecial/model
	 */
	public boolean createPackageDirsBean(String packages) {
		packages = LibUtils.replacePattern("%\\{processName\\}", this.nameApp, packages);
		String pathPackage = LibUtils.convertPackage2Dirs(packages);
		this.pathOutputFile += pathPackage;
		return LibUtils.createDirs(pathOutputFile);
	}

	private String createImports(String imports) {
		return "import " + imports + ";\n";

	}

	private String createAttribute(String name, String type, String typeInput, boolean isReadOnly, List<String> options) {
		
		if (isReadOnly) {
			if (type.equals("List") && !name.matches("^_FILE_.*$")) {
				return "@FieldIsActuacion\nprivate " + "List<String>" + " " + name + ";\n" + "private " + "List<String> " + name + "Option" + " = Arrays.asList(" + this.createMethodSelectValue(options) + ");\n";
			} else if (typeInput.equals("LISTBOX_SIMPLE") || typeInput.equals("RADIOBUTTON_GROUP") || typeInput.equals("CHECKBOX_GROUP")) {
				return "@FieldIsActuacion\nprivate " + "String" + " " + name + ";\n" + "private " + "List<String> " + name + "Option" + " = Arrays.asList(" + this.createMethodSelectValue(options) + ");\n";
			} else {
				return "@FieldIsActuacion\nprivate " + LibUtils.changeToPrimitiveType(type) + " " + name + ";\n";
			}
		} else {
			if (type.equals(LibUtils.changeToPrimitiveType("List<String>")) && !name.matches("^_FILE_.*$")) {
				return "private " + "String[]" + " " + name + ";\n";
			} 

			else {
				return "private " + LibUtils.changeToPrimitiveType(type) + " " + name + ";\n";
			}
		}

	}

	public String createMethodSelectValue(List<String> nameOptions) {

		String option = "";
		for (int k = 0; k < nameOptions.size(); k++) {
			option += "\"" + nameOptions.get(k) + "\"";
			if (k < (nameOptions.size() - 1)) {
				option += ",";
			}

		}
		return option;

	}

	/*
	 * "LISTBOX_SIMPLE"
	 * 
	 * "LISTBOX_MULTIPLE" RADIOBUTTON_GROUP
	 */
	public String createSetMethod(String name, String type, String typeInput) {

		String method = null;

		if (type.equals("List") && !name.matches("^_FILE_.*$")) {
			method = "public void set" + LibUtils.firstLetterUpper(name) + "(" + "List<String>" + " " + name + " ) {\n" + "\t this." + name + " = " + name + ";\n" + "}\n";
			method += "public void set" + LibUtils.firstLetterUpper(name) + "Option" + "(" + "List<String> " + name + "Option" + " ) {\n" + "\t this." + name + "Option" + "= " + name + "Option" + ";\n" + "}\n";
		} else if (typeInput.equals("LISTBOX_SIMPLE") || typeInput.equals("RADIOBUTTON_GROUP") || typeInput.equals("CHECKBOX_GROUP")) {
			method = "public void set" + LibUtils.firstLetterUpper(name) + "(" + "String" + " " + name + " ) {\n" + "\t this." + name + " = " + name + ";\n" + "}\n";
			method += "public void set" + LibUtils.firstLetterUpper(name) + "Option" + "(" + "List<String> " + name + "Option" + " ) {\n" + "\t this." + name + "Option" + "= " + name + "Option" + ";\n" + "}\n";
		} else {
			method = "public void set" + LibUtils.firstLetterUpper(name) + "(" + LibUtils.changeToPrimitiveType(type) + " " + name + " ) {\n" + "\t this." + name + " = " + name + ";\n" + "}\n";
		}

		return method;
	}

	public String createGetMethod(String name, String type, String typeInput) {

		String method = null;
		if (type.equals("List") && !name.matches("^_FILE_.*$")) {
			method = "public " + "List<String>" + " get" + LibUtils.firstLetterUpper(name) + "() {\n" + "\t return this." + name + ";\n" + "}\n";
			method += "public " + "List<String>" + " get" + LibUtils.firstLetterUpper(name) + "Option" + "() {\n" + "\t return this." + name + "Option" + ";\n" + "}\n";
		} else if (typeInput.equals("LISTBOX_SIMPLE") || typeInput.equals("RADIOBUTTON_GROUP") || typeInput.equals("CHECKBOX_GROUP")) {
			method = "public " + "String" + " get" + LibUtils.firstLetterUpper(name) + "() {\n" + "\t return this." + name + ";\n" + "}\n";
			method += "public " + "List<String>" + " get" + LibUtils.firstLetterUpper(name) + "Option" + "() {\n" + "\t return this." + name + "Option" + ";\n" + "}\n";
		} else {
			method = "public " + LibUtils.changeToPrimitiveType(type) + " get" + LibUtils.firstLetterUpper(name) + "() {\n" + "\t return this." + name + ";\n" + "}\n";
		}

		return method;
	}

}

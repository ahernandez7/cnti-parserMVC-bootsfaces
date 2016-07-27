package ve.gob.cnti.output.dependent;

import java.io.File;
import java.util.List;

import ve.gob.cnti.helper.form.Field;
import ve.gob.cnti.helper.util.LibUtils;
import ve.gob.cnti.output.bean.GenerateBean;

public class GenerateDependent extends GenerateBean{
	
	public GenerateDependent(String pathFile, String pathOutputFile, String nameApp) {
		super(pathFile, pathOutputFile, nameApp);
		// TODO Auto-generated constructor stub
	}
	
	
	public String createGetMethod(String name, String type, String typeInput) {
		
		String method = null;		
		method = "public " + "Map<String,String>" + " get" + LibUtils.firstLetterUpper(name) + "() {\n" + "\t return this." + name + ";\n" + "}\n";
		method += "public " + "void" + " getFind" +name +"(String "+name+") {\n" + "\t "
				+  "}\n";		
		return method;
	}
	
	public void createImpAttAndMethos(Field field) {

		if (!"".equals(field.getReturnType())) {

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
	
	
	@SuppressWarnings("unused")
	private String createAttribute(String name, String type, String typeInput, boolean isReadOnly, List<String> options) {
	     	
			System.out.println("paso es unset");
			return "private " + "Map<String,String>" + " " + name + ";\n";
		

	}
	
	
	public String createSetMethod(String name, String type, String typeInput) {

		String method = null;
		method = "public void set" + LibUtils.firstLetterUpper(name) + "(" + "Map<String,String>" + " " + name + " ) {\n" + "\t this." + name + " = " + name + ";\n" + "}\n";
    	return method;
	}

	
	public void replaceVaraiablesAndWriteFile(String nameBean,String packageNameDependent,String appPath) {		
	
		this.snippetFile = LibUtils.replacePattern("%\\{imports\\}", this.imports, snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{attributes\\}", this.attributes, snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{values\\}", this.values, snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{setAndGetMethods\\}", this.setAndGetMethods, snippetFile);
		if (!this.validatorExist(nameBean,packageNameDependent,  appPath))
			this.wf.writeFile(this.pathOutputFile + "/" + LibUtils.firstLetterUpper(nameBean) + ".java", this.snippetFile);
	}
	
	public Boolean validatorExist( String nameBean,String packageNameDependent,String appPath){
		
		String pathClass=appPath+ packageNameDependent
		.replace("%{processName}", this.nameApp)
		.replace(".","/");	
		pathClass+="/"+LibUtils.firstLetterUpper(nameBean)+".java";
		System.out.println("clase"+pathClass);
		File f = new File(pathClass);		
		return f.exists();
		
	}

}

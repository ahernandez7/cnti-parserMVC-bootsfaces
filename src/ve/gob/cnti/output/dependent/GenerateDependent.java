package ve.gob.cnti.output.dependent;

import java.io.File;
import java.util.List;

import ve.gob.cnti.helper.util.LibUtils;
import ve.gob.cnti.output.bean.GenerateBean;

public class GenerateDependent extends GenerateBean{
	
	public GenerateDependent(String pathFile, String pathOutputFile, String nameApp) {
		super(pathFile, pathOutputFile, nameApp);
		// TODO Auto-generated constructor stub
	}
	
	
	public String createGetMethod(String name, String type, String typeInput) {
		String method = null;		
		if (typeInput.equals("LISTBOX_SIMPLE") || typeInput.equals("RADIOBUTTON_GROUP") || typeInput.equals("CHECKBOX_GROUP")) {
			method = "public " + "String" + " get" + LibUtils.firstLetterUpper(name) + "() {\n" + "\t return this." + name + ";\n" + "}\n";
			method += "public " + "void" + " getBuscar" +name +"(String "+name+") {\n" + "\t "
					+  "}\n";
		} 
		return method;
	}
	
	
	
	public void replaceVaraiablesAndWriteFile(String nameBean,String packageNameDependent,String appPath) {
		
		nameBean=this.nameApp + "_" + LibUtils.firstLetterLower(nameBean);
		this.snippetFile = LibUtils.replacePattern("%\\{imports\\}", this.imports, snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{attributes\\}", this.attributes, snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{values\\}", this.values, snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{setAndGetMethods\\}", this.setAndGetMethods, snippetFile);
		System.out.println("ver"+this.validatorExist(nameBean,packageNameDependent,  appPath));
		if (!this.validatorExist(nameBean,packageNameDependent,  appPath))
			this.wf.writeFile(this.pathOutputFile + "/" + LibUtils.firstLetterUpper(nameBean) + ".java", this.snippetFile);
	}
	
	public Boolean validatorExist( String nameBean,String packageNameDependent,String appPath){
		
		String pathClass=appPath+ packageNameDependent
		.replace("%{processName}", this.nameApp)
		.replace(".","/");	
		pathClass+="/"+nameBean+".java";		
		File f = new File(pathClass);		
		return f.exists();
		
	}

}

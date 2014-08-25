package ve.gob.cnti.output.bean;

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
	private String setAndGetMethods = null;
	

	public GenerateBean(String pathFile, String pathOutputFile) {
		
		this.origSnippetFile = LibUtils.readSnippetFile(pathFile);
		this.pathOutputFile = pathOutputFile;
		this.wf = new WriteFile();
	}
	
	public void setImports(String imports) {
		this.imports = imports;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public void setSetAndGetMethods(String setAndGetMethods) {
		this.setAndGetMethods = setAndGetMethods;
	}

	public void replaceNameBeanAndNameClassBean(String nameBean) {
		
		//Para colocar la variable con el snippet original en la siguiete iteración
		this.snippetFile = this.origSnippetFile;

		if (!Character.isLowerCase(nameBean.charAt(0))) {
			this.snippetFile = LibUtils.replacePattern("%\\{nameBean\\}",LibUtils.firstLetterLower(nameBean), this.snippetFile);
			this.snippetFile = LibUtils.replacePattern("%\\{NameBean\\}", nameBean,this.snippetFile);
		} else {
			this.snippetFile = LibUtils.replacePattern("%\\{nameBean\\}", nameBean,this.snippetFile);
			nameBean = LibUtils.firstLetterUpper(nameBean);
			this.snippetFile = LibUtils.replacePattern("%\\{NameBean\\}", nameBean,this.snippetFile);
		}
	}
	
	public void replacePackagesNameBean(String packagesName) {
		this.snippetFile = LibUtils.replacePattern("%\\{packages\\}", packagesName,this.snippetFile);
	}
	
	public void createImpAttAndMethos(Field field){
		
		if (!"".equals(field.getReturnType())){

			if (!field.getReturnType().matches("java\\.lang\\..*") && !this.imports.contains(field.getReturnType())){
				this.imports += createImports(field.getReturnType());
			}
			
			String nameField = field.getNameField();
			String returnTypes = field.getReturnType().substring(field.getReturnType().lastIndexOf(".")+1);
			
			this.attributes += createAttribute(nameField, returnTypes);
			this.setAndGetMethods += createSetMethod(nameField, returnTypes)+"\n";
			this.setAndGetMethods += createGetMethod(nameField, returnTypes)+"\n";
		}

	}
	
	public void replaceVaraiablesAndWriteFile(String nameBean){
		
		this.snippetFile = LibUtils.replacePattern("%\\{imports\\}",this.imports,snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{attributes\\}",this.attributes,snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{setAndGetMethods\\}",this.setAndGetMethods,snippetFile);
		
		this.wf.writeFile(this.pathOutputFile+"/"+nameBean+".java", this.snippetFile);
	}

	public boolean createPackageDirsBean(String packages) {
		String pathPackage = LibUtils.convertPackage2Dirs(packages);
		this.pathOutputFile += pathPackage;
		return LibUtils.createDirs(pathOutputFile);
	}

	private String createImports(String imports) {
		return "import " + imports + ";\n";
	}

	private String createAttribute(String name, String type) {

		return "private " + type + " " + name + ";\n";
	}

	private String createSetMethod(String name, String type) {

		String method = "public void set" + LibUtils.firstLetterUpper(name)
				+ "(" + type + " " + name + " ) {\n" + "\t this." + name
				+ " = " + name + ";\n" + "}\n";

		return method;
	}

	private String createGetMethod(String name, String type) {

		String method = "public " + type + " get"
				+ LibUtils.firstLetterUpper(name) + "() {\n"
				+ "\t return this." + name + ";\n" + "}\n";

		return method;
	}
	
}

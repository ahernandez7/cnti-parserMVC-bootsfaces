package ve.gob.cnti.output.controller;

import ve.gob.cnti.helper.output.WriteFile;
import ve.gob.cnti.helper.util.LibUtils;

public class GenerateController {

	private String snippetFile = null;
	private String origSnippetFile = null;
	private WriteFile wf = null;
	private String pathOutputFile = null;
	private String nameApp = null;

	public GenerateController(String pathFile, String pathOutputFile, String nameApp) {

		this.origSnippetFile = LibUtils.readSnippetFile(pathFile);
		this.pathOutputFile = pathOutputFile;
		this.wf = new WriteFile();
		this.nameApp = nameApp;
	}
	
	public void replaceNameBeanAndNameClassBeanController(String nameBean) {
		
		//Para colocar la variable con el snippet original en la siguiete iteración
		this.snippetFile = this.origSnippetFile;

		if (!Character.isLowerCase(nameBean.charAt(0))) {
			this.snippetFile = LibUtils.replacePattern("%\\{NameBean\\}", nameBean,this.snippetFile);
			this.snippetFile = LibUtils.replacePattern("%\\{nameController\\}",this.nameApp+"_"+LibUtils.firstLetterLower(nameBean)+"Controller", this.snippetFile);
			this.snippetFile = LibUtils.replacePattern("%\\{NameController\\}", nameBean+"Controller",this.snippetFile);
		} else {
			this.snippetFile = LibUtils.replacePattern("%\\{NameBean\\}", LibUtils.firstLetterUpper(nameBean),this.snippetFile);
			this.snippetFile = LibUtils.replacePattern("%\\{nameController\\}", this.nameApp+"_"+nameBean+"Controller",this.snippetFile);
			this.snippetFile = LibUtils.replacePattern("%\\{NameController\\}", LibUtils.firstLetterUpper(nameBean)+"Controller",this.snippetFile);
		}
	}
	
	public boolean createPackageDirsController(String packages) {
		String pathPackage = LibUtils.convertPackage2Dirs(packages);
		this.pathOutputFile += pathPackage;
		return LibUtils.createDirs(pathOutputFile);
	}

	public void replacePackagesNameBeanController(String packageBean) {
		// Para colocar la variable con el snippet original en la siguiete
		// iteración
		this.snippetFile = LibUtils.replacePattern("%\\{packageBean\\}",packageBean, this.snippetFile);
	}
	
	public void replacePackagesNameController(String packagesName) {
		// Para colocar la variable con el snippet original en la siguiete
		// iteración
		this.snippetFile = LibUtils.replacePattern("%\\{packages\\}",packagesName, this.snippetFile);
	}
	
	public void writeFileConroller(String nameController){
				
		this.wf.writeFile(this.pathOutputFile+"/"+nameController+"Controller.java", this.snippetFile);
	}
	
	public void replaceFilesAttributes(String files) {		
		this.snippetFile = LibUtils.replacePattern("%\\{files\\}",files, this.snippetFile);
	}
	
	public void replaceFilesSettersAndGetters(String filesSettersAndGetters) {		
		this.snippetFile = LibUtils.replacePattern("%\\{filesSettersAndGetters\\}",filesSettersAndGetters, this.snippetFile);
	}

}

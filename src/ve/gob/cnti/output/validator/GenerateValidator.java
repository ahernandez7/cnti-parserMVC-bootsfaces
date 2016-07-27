package ve.gob.cnti.output.validator;

import java.io.File;

import ve.gob.cnti.helper.util.LibUtils;
import ve.gob.cnti.output.bean.GenerateBean;

public class GenerateValidator extends GenerateBean{
	

	public GenerateValidator(String pathFile, String pathOutputFile, String nameApp) {
		super(pathFile, pathOutputFile, nameApp);
		// TODO Auto-generated constructor stub
	}
	
	
	
	public void replaceVaraiablesAndWriteFile(String nameBean,String packageNameValidator,String appPath) {
		
		
		//nameBean=this.nameApp + "_" + LibUtils.firstLetterLower(nameBean);
		this.snippetFile = LibUtils.replacePattern("%\\{imports\\}", this.imports, snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{attributes\\}", this.attributes, snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{values\\}", this.values, snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{setAndGetMethods\\}", this.setAndGetMethods, snippetFile);
		System.out.println("ver"+this.validatorExist(nameBean,packageNameValidator,  appPath));
		if (!this.validatorExist(nameBean,packageNameValidator,  appPath))
			this.wf.writeFile(this.pathOutputFile + "/" + LibUtils.firstLetterUpper(nameBean) + ".java", this.snippetFile);
	}
	
	public Boolean validatorExist( String nameBean,String packageNameValidator,String appPath){
		
		String pathClass=appPath+ packageNameValidator
		.replace("%{processName}", this.nameApp)
		.replace(".","/");	
		pathClass+="/"+nameBean+".java";		
		File f = new File(pathClass);
		System.out.println("ruta"+pathClass);
		return f.exists();
		
	}
	
	
	
	
	
	
	/*public void replaceNameBeanAndNameClassBean(String nameBean) {

		// Para colocar la variable con el snippet original en la siguiente iteración
		this.snippetFile = this.origSnippetFile;
    	this.snippetFile = LibUtils.replacePattern("%\\{nameBean\\}", nameBean, this.snippetFile);
		nameBean = LibUtils.firstLetterUpper(nameBean);
		this.snippetFile = LibUtils.replacePattern("%\\{NameBean\\}", nameBean, this.snippetFile);


	}*/
	
	
/*	public void replaceNameBeanAndNameClassBean(String nameBean) {

		// Para colocar la variable con el snippet original en la siguiente iteración
		this.snippetFile = this.origSnippetFile;		
		this.snippetFile = LibUtils.replacePattern("%\\{nameBean\\}", this.nameApp + "_" + LibUtils.firstLetterLower(nameBean), this.snippetFile);
		
	}*/
	
	
	

}

package ve.gob.cnti.output.validator;

import ve.gob.cnti.helper.util.LibUtils;
import ve.gob.cnti.output.bean.GenerateBean;

public class GenerateValidator extends GenerateBean{
	

	public GenerateValidator(String pathFile, String pathOutputFile, String nameApp) {
		super(pathFile, pathOutputFile, nameApp);
		// TODO Auto-generated constructor stub
	}
	
	
	public void replaceNameBeanAndNameClassBean(String nameBean) {

		// Para colocar la variable con el snippet original en la siguiente iteración
		this.snippetFile = this.origSnippetFile;
    	this.snippetFile = LibUtils.replacePattern("%\\{nameBean\\}", nameBean, this.snippetFile);
		nameBean = LibUtils.firstLetterUpper(nameBean);
		this.snippetFile = LibUtils.replacePattern("%\\{NameBean\\}", nameBean, this.snippetFile);


	}
	
	
	

}

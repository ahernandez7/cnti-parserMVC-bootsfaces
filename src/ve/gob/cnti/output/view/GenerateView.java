package ve.gob.cnti.output.view;

import ve.gob.cnti.helper.output.WriteFile;
import ve.gob.cnti.helper.util.LibUtils;

//import ve.gob.cnti.helper.output.WriteFile;

public class GenerateView {

	private String snippetFile = null;
	private String origSnippetFile = null;
	private WriteFile wf = null;
	private String pathOutputFile = null;

		
	public GenerateView(String pathFile, String pathOutputFile) {
		
		this.origSnippetFile = LibUtils.readSnippetFile(pathFile);
		this.pathOutputFile = pathOutputFile;
		this.wf = new WriteFile();
	}

	
	private String translateField2View(String label, String name, String type){
		
		String fieldHtml = null;
		
		if ("TEXTBOX".equalsIgnoreCase(type)){
			fieldHtml = "<label>"+label+"</label><input type='text' name='"+name+"' value=''>\n";
		}else if ("DATE".equalsIgnoreCase(type)){
			
		}else if ("CHECKBOX".equalsIgnoreCase(type)){
			
		}else if ("BUTTON_SUBMIT".equalsIgnoreCase(type)){
			
		}else if ("BUTTON_NEXT".equalsIgnoreCase(type)){
			
		}else if ("BUTTON_PREVIOUS".equalsIgnoreCase(type)){
			
		}
		
		return fieldHtml;
	}

}

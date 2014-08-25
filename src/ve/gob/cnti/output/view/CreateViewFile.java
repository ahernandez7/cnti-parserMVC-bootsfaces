package ve.gob.cnti.output.view;

import ve.gob.cnti.helper.output.WriteFile;

public class CreateViewFile {

	private String file;
	private WriteFile wf = null;

	public CreateViewFile(String file) {		
		this.file = file;
		this.wf = new WriteFile();
	}

	public void createView() {
		// TODO Verificar, si no es en un entorno multihilo utilizar
		// StringBuilder
		StringBuffer sb = new StringBuffer();

		sb.append( "<html>\n"
				 + " <head>\n"
				 + "  <title>\n"
				 + "    Hola Mundo\n"
				 + "  </title>\n"
				 + " </head>\n"
				 + " <body>\n"
				 + "   %{form}"
				 + " </body>\n"
				 + "</html>");
		
		String form = translateField2View("nombre", "nombre1", "TEXTBOX");

		this.wf.writeFile(this.file, sb.toString().replaceAll("%\\{form\\}", form));
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

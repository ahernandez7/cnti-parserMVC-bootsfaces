package ve.gob.cnti;

import java.util.List;
import java.util.Map;

import ve.gob.cnti.helper.form.Application;
import ve.gob.cnti.helper.form.Field;
import ve.gob.cnti.helper.form.Form;
import ve.gob.cnti.helper.form.Page;
import ve.gob.cnti.helper.form.Validator;
import ve.gob.cnti.helper.input.ReadFile;
import ve.gob.cnti.helper.output.WriteFile;
import ve.gob.cnti.helper.util.LibUtils;

public class Test {

	public Test() {
	}
	
	public void showListOutput(Application app){
		
		System.out.println("Nombre de aplicacion: "+app.getAppName());
		
		Map<String, Form> mapForms = app.getMapForms();
		
		for (String key : mapForms.keySet()) {
			
			System.out.println("Nombres de formularios: "+key);
			
			Form formE = mapForms.get(key);
			
			List<Page> pages = formE.getListPages();
			
			for(int i = 0; i < pages.size(); i++){
				Page page = pages.get(i);
				System.out.println("\t id page: "+page.getId());
				System.out.println("\t name Page: "+page.getName());
				System.out.println("\t next Page: "+page.getNextPage());
				
				List<Field> fields = page.getListField();
				
				System.out.println("=====================================================");
				for(int j = 0; j < fields.size(); j++){
					Field field = fields.get(j);
					
					System.out.println("\t\t Label Field: "+field.getLabelField());
					System.out.println("\t\t Name Field: "+field.getNameField());
					System.out.println("\t\t Type Field: "+field.getTypeField());
					System.out.println("\t\t Return Type Field: "+LibUtils.changeToPrimitiveType(field.getReturnType().substring(field.getReturnType().lastIndexOf(".")+1)));
					System.out.println("\t\t Var Field: "+field.getVarName());
					System.out.println("\t\t description Field: "+field.getDescription());

					
					List<Validator> validators = field.getListValidators();
					System.out.println("");
					for(int k = 0; k < validators.size(); k++){
						Validator validator = validators.get(k);
						
						System.out.println("\t\t\t Name Validator: "+validator.getNameValidator());
						
					}
					
					List<String> options = field.getOptionValue();
					System.out.println("");
					for(int k = 0; k < options.size(); k++){
						String option = options.get(k);
						
						System.out.println("\t\t\t Name Options: "+option);
						
					}
					
					
					
					System.out.println("=====================================================");
				}
			}
		}
	}
	
	public void testReadFile(){
		ReadFile rf = new ReadFile();
		WriteFile wf = new WriteFile();
		
		String textFile = rf.readFile("resources/snippets/ModelController.snippet");
		
		
		textFile = textFile.replaceAll("%\\{nameController\\}", "nuevoController");
		textFile = textFile.replaceAll("%\\{NameController\\}", LibUtils.firstLetterUpper("nuevoController"));
		
		String pathPackage = "ve.gob.tramite.controller".replaceAll("\\.", "/");
		
		LibUtils.createDirs("resources/outputs/controllers/"+pathPackage);
				
		String file = "resources/outputs/controllers/"+pathPackage+"/replace.java";
		wf.writeFile(file, textFile);
		
	}
}
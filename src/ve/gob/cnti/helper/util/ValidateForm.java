package ve.gob.cnti.helper.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ve.gob.cnti.helper.form.Application;
import ve.gob.cnti.helper.form.Field;
import ve.gob.cnti.helper.form.Form;
import ve.gob.cnti.helper.form.Page;
import ve.gob.cnti.windows.swing.ParserProcessing;

public class ValidateForm {

	private Application app;
	private String processDesignXml;

	private List<String> errorsDetails = new ArrayList<String>();

	public ValidateForm(Application app,String processDesignXml) {
		this.app = app;
		this.processDesignXml = processDesignXml;
	}

	public boolean isAppFormValid(ParserProcessing pp) throws IOException {

		boolean isValid = true;
		Map<String, Form> mapForms = this.app.getMapForms();

		if (!isNameFormValid(this.app.getAppName()))
			isValid = false;

		if (!is4TaskPresentAndValid(mapForms))
			isValid = false;

		if (!isFieldsValid(mapForms))
			isValid = false;

		if (!isValid) {
			System.out.println("\n\n\n");
			System.out.println("*****************************************************************************************");
			System.out.println("*****************************************************************************************");
			System.out.println("*****************************************************************************************");
			System.out.println("*****************************************************************************************");
			System.out.println("\nEl proceso "+this.app.getAppName()+" tiene errores\n");
			System.out.println("*****************************************************************************************");
			System.out.println("*****************************************************************************************");
			System.out.println("*****************************************************************************************");
			System.out.println("*****************************************************************************************");
			System.out.println("\nNúmero de Errores: "+errorsDetails.size());
			System.out.println("Detalle de errores: \n");
			
			String errores = "El proceso "+this.app.getAppName()+" tiene errores\n";
			errores += "\nNúmero de Errores: "+errorsDetails.size();
			errores += "\nErrores: ";
			for (String error : errorsDetails) {				
				System.err.println(error);
				errores += "\n====>>>> "+error;
			}
			
			pp.saveLogErrors(errores, pp,this.app.getAppName());
		}

		return isValid;
	}

	/**
	 * Valida que el nombre no contenga espacios, caracteres especiales, acentos, etc.
	 * 
	 * @param name
	 *            Nombre de la tarea
	 */
	private boolean isNameFormValid(String name) {
		try {
			if (name.length() == 0) {
				errorsDetails.add("Proceso: No tiene nombre de proceso");
				return false;
			} else if (!isStringValid(name)) {
				errorsDetails.add("Proceso: El nombre del proceso \"" + name + "\" es incorrecto.");
				return false;
			}
		} catch (Exception e) {
			System.err.println("Error en ve.gob.cnti.helper.util.ValidateForm.isNameFormValid");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Valida que todas las tareas de taquilla existan y que sus nombre sean correctos.
	 * 
	 * @param mapForms
	 *            Map de formularios
	 */
	private boolean is4TaskPresentAndValid(Map<String, Form> mapForms) {
		boolean isValid = true;
		try {
			int i = 0;
			for (String key : mapForms.keySet()) {
				Form task = mapForms.get(key);
				if (task.getNameForm().length() == 0) {
					errorsDetails.add("Tarea: No tiene nombre de tarea");
					isValid = false;
				} else {
					if (!isStringValid(task.getNameForm())) {
						errorsDetails.add("Tarea: El nombre del tarea \"" + task.getNameForm() + "\" es incorrecto.");
						isValid = false;
					}
					if ("carga".contentEquals(task.getNameForm().toLowerCase()))
						i++;
					else if ("revision".contentEquals(task.getNameForm().toLowerCase()))
						i++;
					else if ("notificacion".contentEquals(task.getNameForm().toLowerCase()))
						i++;
					else if ("sgi".contentEquals(task.getNameForm().toLowerCase()))
						i++;
				}
			}
			// Valida que existan las 4 actividades.(carga, revision,sgi,notificacion)
			if (i < 4) {
				errorsDetails.add("Proceso: no están presentes todas las tareas de taquilla(carga, revision,sgi,notificacion)");
				isValid = false;
			}
		} catch (Exception e) {
			System.err.println("Error en ve.gob.cnti.helper.util.ValidateForm.isNameFormValid");
			e.printStackTrace();
			isValid = false;
		}

		return isValid;
	}

	private boolean isFieldsValid(Map<String, Form> mapForms) {
		boolean isValid = true;

		try {
			for (String key : mapForms.keySet()) {
				Map<String,Field> fieldsComplete = new HashMap<String, Field>();
				Form task = mapForms.get(key);
				List<String> varNames = new ArrayList<String>();				
				if (!task.getNameForm().contentEquals("SGI")) {
					for (Page tab : task.getListPages()) {
						for (Field field : tab.getListField()) {
							fieldsComplete.put(field.getVarName(), field);
							if (!field.getTypeField().contentEquals("BUTTON_SUBMIT") && 
									!field.getTypeField().contentEquals("BUTTON_PREVIOUS") && 
									!field.getTypeField().contentEquals("BUTTON_NEXT")) {
								if (field.getVarName().length() == 0) {
									errorsDetails.add("Variable de proceso no esta presente en campo " + 
											field.getNameField() + " de tipo " + field.getTypeField() + " en tab: " + tab.getName());
									isValid = false;
								} else if (!isVarNameValid(field.getVarName())) {
									errorsDetails.add("Nombre de variable de proceso incorrecto \"" + field.getVarName()
											+ "\" en campo " + field.getNameField() + " de tipo " + field.getTypeField() + " en tab: " 
											+ tab.getName());
									isValid = false;
								}
								if (varNames.contains(field.getVarName())) {
									errorsDetails.add("Nombre de variable(" + field.getVarName() 
											+ ") de proceso repetido, coincidencia en tab: " + tab.getName());
									isValid = false;
								} else
									varNames.add(field.getVarName());

								if (field.getDescription().length() == 0) {
									errorsDetails.add("Descripción de campo no esta presente en " 
											+ field.getNameField() + " de tipo " + field.getTypeField() + " en tab: " + tab.getName());
									isValid = false;
								}
								if (field.getLabelField().length() == 0) {
									errorsDetails.add("Label de campo no está presente en " 
											+ field.getNameField() + " de tipo " + field.getTypeField() + " en tab: " + tab.getName());
									isValid = false;
								}
								
								if(field.getTypeField().contentEquals("LISTBOX_MULTIPLE") 
										|| field.getTypeField().contentEquals("CHECKBOX_GROUP") 
										|| field.getReturnType().contentEquals("RADIOBUTTON_GROUP")){									
									if(field.getOptionValue().size()==1 && !field.getNameField().startsWith("_FILE_")){
										errorsDetails.add("Opciones del campo no estan presente en " 
												+ field.getNameField() + " de tipo " + field.getTypeField() + " en tab: " + tab.getName());
										isValid = false;
									}
								} 
							}
						}
					}
				}
				//TODO validar variable transverasles	
				isValid = isTransversalCompleteIntoTask(task.getNameForm(),fieldsComplete,isValid);
			}
		} catch (Exception e) {
			System.err.println("Error en ve.gob.cnti.helper.util.ValidateForm.isFieldsValid");
			e.printStackTrace();
			isValid = false;
		}
		
		isValid = isTransversalCompleteIntoPool(isValid);	

		return isValid;
	}
	
	private boolean isTransversalCompleteIntoPool(boolean isValid){
		
				
		List<TransversalVar> lVars = new TransversalVar().getTranversalsVarsIntoPool();
		
		for(TransversalVar tv : lVars){
			
			if(!tv.isPresentVarIntoPool(tv,processDesignXml)){
				errorsDetails.add("Variable Transversal (" + tv.getVarName()+ ") no esta definida en "
						+ "la tarea de ");
				isValid = false;
			}
			
		}
	
		return isValid;
	}
	
	private boolean isTransversalCompleteIntoTask(String nameTask,Map<String,Field> fieldsComplete,boolean isValid){
		String name = nameTask.toLowerCase();
		name = name.replaceAll(" ", "");		
		if(!"sgi".contentEquals(name)){		
			List<TransversalVar> lVars = new TransversalVar().getTranversalsVarsIntoTask(name);
			for(TransversalVar tv : lVars){
				if(fieldsComplete.containsKey(tv.getVarName())==false){
					errorsDetails.add("Variable Transversal (" + tv.getVarName()+ ") no esta definida en "
							+ "la tarea de "+name);
					isValid = false;
				}else{
					Field f = fieldsComplete.get(tv.getVarName());
					if(!f.getReturnType().contentEquals(tv.getType())){
						errorsDetails.add("Variable Transversal (" + tv.getVarName()+ ") está definida erroneamente, retorna "
								+f.getReturnType()+ " y el valor esperado es "+ tv.getType() +"; tarea ==> "+name);
						isValid = false;
					}
				}				
			}			
		}
		return isValid;
	}
	
	private boolean isStringValid(String string) {
		try {
			final String PATTERN = "^([A-Za-z]+)$";
			Pattern pattern = Pattern.compile(PATTERN);
			Matcher matcher;
			matcher = pattern.matcher(string);
			if (!matcher.matches())
				return false;
		} catch (Exception e) {
			System.err.println("Error en ve.gob.cnti.helper.util.ValidateForm.isStringValid");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean isVarNameValid(String string) {
		try {
			final String PATTERN = "^([A-Za-z_0-9]+)$";
			Pattern pattern = Pattern.compile(PATTERN);
			Matcher matcher;
			matcher = pattern.matcher(string);
			if (!matcher.matches())
				return false;
		} catch (Exception e) {
			System.err.println("Error en ve.gob.cnti.helper.util.ValidateForm.isStringValid");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<String> getErrorsDetails() {
		return errorsDetails;
	}

	public void setErrorsDetails(List<String> errorsDetails) {
		this.errorsDetails = errorsDetails;
	}

}

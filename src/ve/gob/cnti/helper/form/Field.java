package ve.gob.cnti.helper.form;

import java.util.ArrayList;
import java.util.List;

public class Field {
	
	private String labelField;
	private String nameField;
	private String typeField;
	private String varName;
	private String returnType;
	private List<Validator> listValidators;

	public Field() {
		this.labelField = null;
		this.nameField = null;
		this.typeField = null;
		this.varName = null;
		this.returnType = null;
		
		this.listValidators = new ArrayList<Validator>();
	}

	public String getLabelField() {
		return labelField;
	}

	public void setLabelField(String labelField) {
		this.labelField = labelField;
	}

	public String getNameField() {
		return nameField;
	}

	public void setNameField(String nameField) {
		this.nameField = nameField;
	}

	public String getTypeField() {
		return typeField;
	}

	public void setTypeField(String typeField) {
		this.typeField = typeField;
	}
	
	public String getVarName() {
		return varName;
	}
	
	public void setVarName(String varName) {
		this.varName = varName;
	}
	
	public String getReturnType() {
		return returnType;
	}
	
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	public List<Validator> getListValidators() {
		return this.listValidators;
	}
	
	public void addValidator(Validator validator){
		this.listValidators.add(validator);
	}
	
	public void removeValidator(int index){
		this.listValidators.remove(index);
	}
	
	public Validator getValidator(int index){
		return this.listValidators.get(index);
	}

}

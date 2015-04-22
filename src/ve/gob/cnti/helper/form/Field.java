package ve.gob.cnti.helper.form;

import java.util.ArrayList;
import java.util.List;

public class Field {
	
	private String labelField;
	private String nameField;
	private String typeField;
	private String varName;
	private String returnType;
	private boolean isActuation;
	private List<Validator> listValidators;
	private List<String> optionValue;
	private String description;

	public Field() {
		this.labelField = null;
		this.nameField = null;
		this.typeField = null;
		this.varName = null;
		this.returnType = null;
		this.isActuation = true;
		this.listValidators = new ArrayList<Validator>();
	}

	public boolean isActuation() {
		return isActuation;
	}

	public void setIsActuation(boolean isActuation) {
		this.isActuation = isActuation;
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

	public List<String> getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(List<String> optionValue) {
		this.optionValue = optionValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Field [labelField=" + labelField + ", nameField=" + nameField + ", typeField=" + typeField + ", varName=" + varName + ", returnType=" + returnType + ", isActuation=" + isActuation + ", listValidators=" + listValidators + ", optionValue=" + optionValue + ", description="
				+ description + "]";
	}
	
	
	
	

}

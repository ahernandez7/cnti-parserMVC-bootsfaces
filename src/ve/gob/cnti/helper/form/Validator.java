package ve.gob.cnti.helper.form;

public class Validator {

	private String nameValidator;
	private String classType;
	
	public Validator() {
		this.nameValidator = null;
	}

	public String getNameValidator() {
		return this.nameValidator;
	}

	public void setNameValidator(String nameValidator) {
		this.nameValidator = nameValidator;
	}
	
	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	@Override
	public String toString() {
		return "Validator [nameValidator=" + nameValidator + "]";
	}
	
	
	
}
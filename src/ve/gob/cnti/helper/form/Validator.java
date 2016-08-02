package ve.gob.cnti.helper.form;

public class Validator {

	private String nameValidator;
	private String classType;
	private String minimun;
	private String maximun;
	
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

	public String getMinimun() {
		return minimun;
	}

	public void setMinimun(String minimun) {
		this.minimun = minimun;
	}

	public String getMaximun() {
		return maximun;
	}

	public void setMaximun(String maximun) {
		this.maximun = maximun;
	}

	@Override
	public String toString() {
		return "Validator [nameValidator=" + nameValidator + ", classType=" + classType + ", minimun=" + minimun + ", maximun=" + maximun + "]";
	}
	
	
	
	
}
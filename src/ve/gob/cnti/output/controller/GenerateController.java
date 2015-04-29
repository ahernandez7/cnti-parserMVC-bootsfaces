package ve.gob.cnti.output.controller;

import java.util.List;

import ve.gob.cnti.helper.form.Field;
import ve.gob.cnti.helper.output.WriteFile;
import ve.gob.cnti.helper.util.LibUtils;

public class GenerateController {

	private String snippetFile = null;
	private String origSnippetFile = null;
	private WriteFile wf = null;
	private String pathOutputFile = null;
	private String nameApp = null;

	public GenerateController(String pathFile, String pathOutputFile, String nameApp) {

		this.origSnippetFile = LibUtils.readSnippetFile(pathFile);
		this.pathOutputFile = pathOutputFile;
		this.wf = new WriteFile();
		this.nameApp = nameApp;
	}

	public void replaceNameBeanAndNameClassBeanController(String nameBean) {

		// Para colocar la variable con el snippet original en la siguiete iteración
		this.snippetFile = this.origSnippetFile;

		if (!Character.isLowerCase(nameBean.charAt(0))) {
			this.snippetFile = LibUtils.replacePattern("%\\{NameBean\\}", nameBean, this.snippetFile);
			this.snippetFile = LibUtils.replacePattern("%\\{nameController\\}", this.nameApp + "_" + LibUtils.firstLetterLower(nameBean) + "Controller", this.snippetFile);
			this.snippetFile = LibUtils.replacePattern("%\\{NameController\\}", nameBean + "Controller", this.snippetFile);
		} else {
			this.snippetFile = LibUtils.replacePattern("%\\{NameBean\\}", LibUtils.firstLetterUpper(nameBean), this.snippetFile);
			this.snippetFile = LibUtils.replacePattern("%\\{nameController\\}", this.nameApp + "_" + nameBean + "Controller", this.snippetFile);
			this.snippetFile = LibUtils.replacePattern("%\\{NameController\\}", LibUtils.firstLetterUpper(nameBean) + "Controller", this.snippetFile);
		}
	}

	public void replaceDirViewSuccess(String dir) {
		dir = "/views/" + dir + "success.xhtml?faces-redirect=true";
		this.snippetFile = LibUtils.replacePattern("%\\{DirViewSuccess\\}", dir, this.snippetFile);
		this.snippetFile = LibUtils.replacePattern("%\\{processName\\}", this.nameApp, this.snippetFile);
	}

	// Convierte el nombre del paquete definido en el configuracion.properties en una ruta de directorio valida
	/*
	 * Ejemplo: entrada ve.gob.vicepresidencia.jubilacionespecial.controller salida /ve/gob/vicepresidencia/jubilacionespecial/controller
	 */
	public boolean createPackageDirsController(String packages) {
		String pathPackage = LibUtils.convertPackage2Dirs(packages);
		this.pathOutputFile += pathPackage;
		return LibUtils.createDirs(pathOutputFile);
	}

	public void replacePackagesNameBeanController(String packageBean) {
		// Para colocar la variable con el snippet original en la siguiete
		// iteración
		this.snippetFile = LibUtils.replacePattern("%\\{packageBean\\}", packageBean, this.snippetFile);
	}

	public void replacePackagesNameController(String packagesName) {
		// Para colocar la variable con el snippet original en la siguiete
		// iteración
		this.snippetFile = LibUtils.replacePattern("%\\{packages\\}", packagesName, this.snippetFile);
	}

	public void writeFileConroller(String nameController) {

		this.wf.writeFile(this.pathOutputFile + "/" + nameController + "Controller.java", this.snippetFile);
	}

	private void replaceFilesAttributes(String files) {
		this.snippetFile = LibUtils.replacePattern("%\\{files\\}", files, this.snippetFile);
	}

	

	public void insertFilesInController(List<Field> fields) {
		String files = "";		
		for (int j = 0; j < fields.size(); j++) {
			Field field = fields.get(j);
			if (field.isActuation() && (field.getVarName().matches("^_FILE_.*$") || field.getVarName().matches("^_MFILE_.*$"))) {
				files +="@SuppressWarnings(\"unused\")\n";
				files += "private List<UploadedFile> " +field.getVarName() + ";\n";
			}
		}
		this.replaceFilesAttributes(files);
	}

}

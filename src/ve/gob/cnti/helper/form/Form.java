package ve.gob.cnti.helper.form;

import java.util.ArrayList;
import java.util.List;

public class Form {
	
	private String nameForm;
	private String actionForm;
	private List<Page> listPages;
	

	public Form() {
		this.nameForm = null;
		this.actionForm = null;
		this.listPages = new ArrayList<Page>();
	}


	public String getNameForm() {
		return this.nameForm;
	}


	public void setNameForm(String nameForm) {
		this.nameForm = nameForm;
	}


	public String getActionForm() {
		return this.actionForm;
	}


	public void setActionForm(String actionForm) {
		this.actionForm = actionForm;
	}


	public List<Page> getListPages() {
		return this.listPages;
	}
	
	public void addPage(Page page){
		this.listPages.add(page);
	}
	
	public void removePage(int index){
		this.listPages.remove(index);
	}
	
	public Page getPage(int index){
		return this.listPages.get(index);
	}


	@Override
	public String toString() {
		return "Form [nameForm=" + nameForm + ", actionForm=" + actionForm + ", listPages=" + listPages + "]";
	}

}

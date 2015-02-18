package ve.gob.cnti.helper.form;

import java.util.ArrayList;
import java.util.List;

public class Page {

	private String id;
	private List<Field> listFields;
	private String name;
	private String nextPage;
	
	public Page() {
		this.id = null;
		this.listFields = new ArrayList<Field>();
		
	}
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Field> getListField() {
		return this.listFields;
	}
	
	public void addField(Field field){
		this.listFields.add(field);
	}
	
	public void removeField(int index){
		this.listFields.remove(index);
	}
	
	public Field getField(int index){
		return this.listFields.get(index);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

}

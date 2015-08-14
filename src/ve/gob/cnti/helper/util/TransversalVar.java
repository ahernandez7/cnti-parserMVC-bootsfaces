package ve.gob.cnti.helper.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import ve.gob.cnti.helper.input.ReadFile;

public class TransversalVar {
	
	private String name;
	private String varName;
	private String type;
	
	private static final String XML_TRANSVERSAL_VARS = "/inputs/transversal/vars/transversalVars.xml";
	private static Document jdomDocument = null;
	
		
	public List<TransversalVar> getTranversalsVarsIntoTask(String nameTask){

		List<TransversalVar> variables = new ArrayList<TransversalVar>();
		
		try {
			
			SAXBuilder jdomBuilder = new SAXBuilder();
			jdomDocument = jdomBuilder.build(new ReadFile().streamTofile(getClass().getResourceAsStream(XML_TRANSVERSAL_VARS)));			
			Element rootNode = jdomDocument.getRootElement();
			
			Element carga = rootNode.getChild("tasks").getChild(nameTask);
			List<Element> vars = carga.getChild("vars").getChildren("var");
			for(Element var:vars){
				
				TransversalVar tv = new TransversalVar();				
				tv.setName(var.getChildText("name"));
				tv.setVarName(var.getChildText("varName"));
				tv.setType(var.getChildText("type"));
				
				variables.add(tv);
				
			}
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		return variables;
		
	}
	
	public List<TransversalVar> getTranversalsVarsIntoPool(){

		List<TransversalVar> variables = new ArrayList<TransversalVar>();
		
		try {
			
			SAXBuilder jdomBuilder = new SAXBuilder();
			
			jdomDocument = jdomBuilder.build(new ReadFile().streamTofile(getClass().getResourceAsStream(XML_TRANSVERSAL_VARS)));				
			Element rootNode = jdomDocument.getRootElement();
						
			List<Element> vars = rootNode.getChild("pool").getChild("vars").getChildren("var");
			for(Element var:vars){
				
				TransversalVar tv = new TransversalVar();				
				tv.setName(var.getChildText("name"));
				tv.setVarName(var.getChildText("varName"));
				tv.setType(var.getChildText("type"));
				
				variables.add(tv);
				
			}
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		return variables;
		
	}
	
	public boolean isPresentVarIntoPool(TransversalVar tv,String processDesignXml){
		
		return new ReadXml().isVarPresentIntoXML(tv, processDesignXml);
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TransversalVar [name=" + name + ", varName=" + varName + ", type=" + type + "]";
	}
	
}

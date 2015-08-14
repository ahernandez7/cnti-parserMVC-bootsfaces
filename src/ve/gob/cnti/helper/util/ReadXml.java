package ve.gob.cnti.helper.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ReadXml {
	
	public boolean isVarPresentIntoXML(TransversalVar tv,String processDesignXml){
		
		try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File(processDesignXml));
            
            doc.getDocumentElement ().normalize ();

            NodeList listOfDataDefinition = doc.getElementsByTagName("textDataDefinition");
            for(int s=0; s<listOfDataDefinition.getLength() ; s++){
                Node dataDefinition = listOfDataDefinition.item(s);
                if(dataDefinition.getNodeType() == Node.ELEMENT_NODE){
                	Element element = (Element)dataDefinition;
                    if(element.getAttribute("name").contentEquals(tv.getVarName()))
                    	return true;                     
                }
            }
            
            listOfDataDefinition = doc.getElementsByTagName("dataDefinition");
            for(int s=0; s<listOfDataDefinition.getLength() ; s++){
                Node dataDefinition = listOfDataDefinition.item(s);
                if(dataDefinition.getNodeType() == Node.ELEMENT_NODE){
                	Element element = (Element)dataDefinition;
                    if(element.getAttribute("name").contentEquals(tv.getVarName()))
                    	return true;                     
                }
            }
        }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
		
		return false;
	}

}

package ve.gob.cnti.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import ve.gob.cnti.helper.form.Application;
import ve.gob.cnti.helper.form.Field;
import ve.gob.cnti.helper.form.Form;
import ve.gob.cnti.helper.form.Page;
import ve.gob.cnti.helper.form.Validator;

public class PaserXmltoForm {

	private String xmlFile = "resources/inputs/forms/forms.xml";
	private Document jdomDocument = null;
	private Map<String, Form> mapForms = null;

	public PaserXmltoForm() throws JDOMException, IOException {

		SAXBuilder jdomBuilder = new SAXBuilder();

		jdomDocument = jdomBuilder.build(new File(this.xmlFile));

	}

	public PaserXmltoForm(String xmlFile) throws JDOMException, IOException {

		SAXBuilder jdomBuilder = new SAXBuilder();

		// jdomDocument = jdomBuilder.build(xmlSource);
		jdomDocument = jdomBuilder.build(new File(xmlFile));

	}

	private Map<String, Element> processForm(List<Element> forms) {

		Map<String, Element> mapFormElemets = new HashMap<String, Element>();

		Pattern p = Pattern.compile("\\w+--\\d\\.\\d--(.+)\\$entry");

		for (int i = 0; i < forms.size(); i++) {
			Element form = forms.get(i);

			Matcher m = p.matcher(form.getAttributeValue("id"));
			while (m.find()) {
				mapFormElemets.put(m.group(1), form);
			}
		}

		return mapFormElemets;
	}

	public Application parse() {

		Element rootElement = jdomDocument.getRootElement();

		String appName = rootElement.getChild("application")
				.getAttributeValue("name").replaceAll(" ", "");

		List<Element> forms = rootElement.getChild("application")
				.getChild("forms").getChildren("form");

		Map<String, Element> mapFormElemets = processForm(forms);

		mapForms = new HashMap<String, Form>();
		String idPage = null;
		String labelField = null;
		String nameField = null;
		String typeField = null;
		String varName = null;
		String returnType = null;

		String validatorClassName = null;

		for (String nameForm : mapFormElemets.keySet()) {
			Element formE = mapFormElemets.get(nameForm);

			Form form = new Form();

			form.setNameForm(nameForm);

			List<Element> pages = formE.getChild("pages").getChildren("page");
			for (int i = 0; i < pages.size(); i++) {
				Element pageElem = pages.get(i);
				
				Page page = new Page();
				idPage = pageElem.getAttributeValue("id");
				page.setId(idPage);

				List<Element> widgets = pageElem.getChild("widgets").getChildren("widget");
				for (int j = 0; j < widgets.size(); j++) {
					Element widget = widgets.get(j);
					if (widget.getChild("initial-value") != null) {
						varName = widget.getChild("initial-value").getChild("expression").getChild("name").getValue();
						returnType = widget.getChild("initial-value").getChild("expression").getChild("return-type").getValue();
					} else {
						varName = "";
						returnType = "";
					}
					
					if (widget.getChild("label")!=null) {
						labelField = widget.getChild("label").getChild("expression").getChild("name").getValue();
					} else {
						labelField = widget.getAttributeValue("id"); 
					}
					
					nameField = widget.getAttributeValue("id");
					typeField = widget.getAttributeValue("type");

					Field field = new Field();
					field.setLabelField(labelField);
					field.setNameField(nameField);
					field.setTypeField(typeField);
					field.setVarName(varName);
					field.setReturnType(returnType);
					
					if (widget.getChild("mandatory") != null){
						if (Boolean.valueOf(widget.getChild("mandatory").getValue().trim()).booleanValue()) {
							Validator val = new Validator();
							val.setNameValidator("mandatory");
							field.addValidator(val);
						}
					}

					if (widget.getChild("readonly") != null){
						if (Boolean.valueOf(widget.getChild("readonly").getValue().trim()).booleanValue()) {
							Validator val = new Validator();
							val.setNameValidator("readonly");
							field.addValidator(val);
							field.setIsActuation(false);
						}
					}

					if (widget.getChild("validators") != null) {
						List<Element> validators = widget.getChild("validators").getChildren("validator");

						for (int k = 0; k < validators.size(); k++) {
							Element validator = validators.get(k);

							validatorClassName = validator.getChild("classname").getValue();
							validatorClassName = validatorClassName.substring(validatorClassName.lastIndexOf(".") + 1);

							Validator val = new Validator();
							val.setNameValidator(validatorClassName);
							field.addValidator(val);
						}
					}

					page.addField(field);

				}

				form.addPage(page);
			}

			mapForms.put(nameForm, form);
		}

		Application app = new Application();

		app.setAppName(appName);
		app.setMapForms(mapForms);

		return app;
	}

}

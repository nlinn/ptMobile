package me.linnemann.ptmobile.pivotaltracker.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.linnemann.ptmobile.pivotaltracker.IncomingData;
import me.linnemann.ptmobile.pivotaltracker.adapter.DBAdapter;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * handler used for xml parsing and updating data inside db
 * 
 * @author niels
 *
 */
public abstract class XMLBaseHandler extends DefaultHandler {

	protected DBAdapter db;
	protected String currentElementName="";

	public XMLBaseHandler(DBAdapter db) {
		this.db = db;
	}

	public void startElement(String uri, String name, String qName, Attributes attr) {
		currentElementName = name.trim();
	}

	public void go(InputStream in) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp;

		sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();
		xr.setContentHandler(this);
		InputSource is = new InputSource(in);	        
		xr.parse(is);

	}
	
	public void endElement(String uri, String name, String qName) throws SAXException {
		currentElementName = null;
	}
	
	/**
	 * checks if element is field and appends data if true
	 */
	protected boolean checkAndFillString(IncomingData data, String element, Object field, String chars) {

		if (isField(field,element)) {
			data.addDataForKey(field, chars);
			return true;
		} else {
			return false;
		}
	}

	protected boolean isField(Object field, String element) {
		if (field == null) {
			return false;
		} else {

			return field.toString().equalsIgnoreCase(element);
		}
	}
}

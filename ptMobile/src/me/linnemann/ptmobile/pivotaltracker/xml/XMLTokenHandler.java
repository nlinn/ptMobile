package me.linnemann.ptmobile.pivotaltracker.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class XMLTokenHandler extends XMLBaseHandler {

	StringBuilder token = new StringBuilder();
	
	public XMLTokenHandler() {
		super(null);
	}
	
	public void endElement(String uri, String name, String qName) throws SAXException {
		currentElementName = "";
	}

	public void characters(char ch[], int start, int length) {
		 
		String chars = (new String(ch).substring(start, start + length));
		
		if (currentElementName.equalsIgnoreCase("guid")) {
			token.append(chars);
		}
    }
	
	
	public String getToken(InputStream in) throws IOException, ParserConfigurationException, SAXException {
		
		go(in);
		return token.toString();
	}
	
}

package me.linnemann.ptmobile.pivotaltracker.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class XMLTokenHandler extends DefaultHandler {

	private static final String TAG = "XMLTokenHandler";
	
	private StringBuilder token = new StringBuilder();
	private String currentElementName="";
		
	public void startElement(String uri, String name, String qName, Attributes attr) {
		currentElementName = name.trim();
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
	
	public String getTokenAndClose(InputStream in) {
		try {
			parse(in);
			in.close();
			return token.toString();
		} catch (ParserConfigurationException e) {
			Log.e(TAG,"ParserConfigurationException "+e.getMessage());
			throw new RuntimeException("ParserConfigurationException "+e.getMessage(),e);
		} catch (SAXException e) {
			Log.e(TAG,"SAXException "+e.getMessage());
			throw new RuntimeException("SAXException "+e.getMessage(),e);
		} catch (IOException e) {
			Log.e(TAG,"IOException "+e.getMessage());
			throw new RuntimeException("IOException "+e.getMessage(),e);
		}
	}
	
	private void parse(InputStream in) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp;

		sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();
		xr.setContentHandler(this);
		InputSource is = new InputSource(in);	        
		xr.parse(is);

	}
}

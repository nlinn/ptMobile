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

public class XMLStackHandler extends DefaultHandler {

	private static final String TAG = "XMLStackHandler";
	private XMLStack stack;
	
	public XMLStackHandler(XMLStack stack) {
		this.stack = stack;
	}

	public void startElement(String uri, String name, String qName, Attributes attr) {
		Log.v(TAG,"start element: "+name);
		stack.startElement(name);
	}

	public void endElement(String uri, String name, String qName) throws SAXException {
		stack.endElement(name);
	}

	public void characters(char ch[], int start, int length) {
		stack.addData(ch, start, length) ;
	}
	
	public void parse(InputStream in) {
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp;

		try {
			sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(this);
			InputSource is = new InputSource(in);	        
			xr.parse(is);
		} catch (ParserConfigurationException e) {
			Log.e(TAG,"ParserConfigurationException: "+e.getMessage());
			throw new RuntimeException("ParserConfigurationException: "+e.getMessage());
		} catch (SAXException e) {
			Log.e(TAG,"SAXException: "+e.getMessage());
			throw new RuntimeException("SAXException: "+e.getMessage());
		} catch (IOException e) {
			Log.e(TAG,"IOException: "+e.getMessage());
			throw new RuntimeException("IOException: "+e.getMessage());
		}
	}
}

package me.linnemann.ptmobile.test.pivotaltracker.xml;

import me.linnemann.ptmobile.pivotaltracker.xml.XMLStack;
import android.test.AndroidTestCase;

public class XMLStackTests extends AndroidTestCase {

	XMLStack stack;
	
	public void setUp() {
		stack = new XMLStack();
	}
	
	public void test_getparent_returnsRootIfEmpty() {
		assertEquals("root",stack.getParent());
	}
	
	public void test_getparent_returnsRootIfOnlyOneElement() {
		stack.startElement("yo");
		assertEquals("root",stack.getParent());
	}
	
	public void test_endelement_throwsExceptionIfNotCorrectElement() {
		boolean hasException=false;
		try {
			stack.startElement("start");
			stack.endElement("not_start");
		} catch (RuntimeException e) {
			hasException = true;
		}
		assertTrue(hasException);
	}
	
	public void test_getCurrent_returnsCurrent() {
		stack.startElement("start");
		assertEquals("start", stack.getCurrent());
	}
	
	public void test_getCurrentAfterEndelement_returnsCurrent() {
		stack.startElement("start");
		stack.startElement("start2");
		stack.startElement("start3");
		stack.endElement("start3");
		stack.endElement("start2");
		assertEquals("start", stack.getCurrent());
	}
	
	public void test_getPath_returnsCorrenctPath() {
		stack.startElement("start");
		stack.startElement("start2");
		stack.startElement("start3");
		assertEquals("start.start2.start3",stack.getQualifiedPath());
	}
	
	public void test_getPath_isEmptyButNotNullIfStackEmpty() {
		assertEquals("",stack.getQualifiedPath());
	}
	
	public void test_getListenerForElement_foundCorrectListener() {
		XMLStackListenerMock listener = new XMLStackListenerMock();
		
		stack.addListener("niels.is.testing", listener);
		assertEquals(listener, stack.getListener("niels.is.testing"));
	}
	
	public void test_getNotExistingListener_returnsIgnoreListener() {
		assertEquals(XMLStack.IGNORE_LISTENER, stack.getListener("no.listener.registered.here"));
	}
	
	public void test_onEndElement_invokesListenerWithData() {
		XMLStackListenerMock listener = new XMLStackListenerMock();
		
		stack.addListener("niels.is.testing", listener);
		stack.startElement("niels");
		stack.startElement("is");
		stack.startElement("testing");
		stack.startElement("one");
		stack.addData("my data".toCharArray(), 0, "my data".toCharArray().length);
		stack.endElement("one");
		stack.endElement("testing");
		stack.endElement("is");
		stack.endElement("niels");
		
		assertEquals("my data",listener.getElements().get("one"));
	}
	
	public void test_onEndElement_listenerKnowsElementIsPoppedFromStack() {
		XMLStackListenerMock listener = new XMLStackListenerMock();
		
		assertFalse(listener.isElementPoppedFromStack());
		
		stack.addListener("foo.bar", listener);
		stack.startElement("foo");
		stack.startElement("bar");
		stack.startElement("element");
		stack.endElement("element");
		assertFalse(listener.isElementPoppedFromStack());
		stack.endElement("bar");
		assertTrue(listener.isElementPoppedFromStack());
	}
}

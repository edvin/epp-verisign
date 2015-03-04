/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
***********************************************************/
package com.verisign.epp.codec.gen;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;

import com.verisign.epp.util.EPPSchemaCachingParser;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestErrorHandler;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the com.verisign.epp.codec.gen package.  The unit test
 * will     execute, gather statistics, and output the results of a test of
 * each com.verisign.epp.codec.gen package concrete <code>EPPMessage</code>'s
 * and their     expected <code>EPPResponse</code>.  The unit test is
 * dependent on the     use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 * <br>
 * The test <code>EPPResponse</code> is duplicated     for
 * <code>EPPMessage</code> that do not contain a specialized
 * <code>EPPResponse</code>     to provide an example of a full
 * <code>EPPMessage</code> transaction.  All of the com.verisign.epp.codec.gen
 * package <code>EPPMessage</code> classes are associated     with
 * <code>EPPResponse</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.8 $
 */
public class EPPCodecTst extends TestCase {
	/**
	 * Number of unit test iterations to run.  This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;

	/**
	 * Has the test been initialized?
	 */
	private static boolean initialized = false;
	
	/**
	 * Error handler used for XML parsing errors
	 */
	private static ErrorHandler xmlErrorHandler = new TestErrorHandler();
	
	private static final String NAMESPACE_TRANSFORM_XSL =
			"<?xml version=\"1.0\"?>" + 
			"<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">" + 
			"  <xsl:output method=\"xml\" encoding=\"utf-8\" indent=\"yes\"/>" + 
			"  <xsl:param name=\"use-this-prefix\"/>" + 
			"  <xsl:template match=\"*[namespace-uri(.)]\">" + 
			"    <xsl:element name=\"{$use-this-prefix}{local-name()}\" namespace=\"{namespace-uri(.)}\">" + 
			"      <xsl:copy-of select=\"@*\"/>" + 
			"      <xsl:apply-templates/>" + 
			"    </xsl:element>" + 
			"  </xsl:template>" + 
			"</xsl:stylesheet>";

	/**
	 * Allocates an <code>EPPCodecTst</code> with a logical name.  The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 *
	 * @param aName Logical name of the test
	 */
	public EPPCodecTst(String aName) {
		super(aName);
	} // End EPPCodecTst(String)

	/**
	 * Unit test of <code>EPPGreeting</code>.  There is no response associated
	 * with <code>EPPGreeting</code> other than a <code>EPPLoginCmd</code>
	 * which is testing in <code>testLogin</code>.<br>
	 * <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testGreeting() {
		printStart("testGreeting");

		EPPGreeting theGreeting = new EPPGreeting();

		// Add services
		EPPServiceMenu serviceMenu = theGreeting.getServiceMenu();
		Vector		   services = new Vector();
		EPPService     service  =
			new EPPService(
						   "domain", "urn:ietf:params:xml:ns:domain-1.0",
						   "urn:iana:xml:ns:domain-1.0 domain-1.0.xsd");
		service.setServiceType(0);

		services.addElement(service);

		/*services.addElement(new EPPService("urn:ietf:params:xml:ns:domain-1.0",null,0));
		   services.addElement(new EPPService("urn:ietf:params:xml:ns:contact-1.0",null,0));
		   services.addElement(new EPPService("urn:ietf:params:xml:ns:contact-5.0",null,0));*/

		//Vector extservices=new Vector();
		//extservices.addElement(new EPPService(null,"http://custom/obj1ext-1.0",1));

		/*services.addElement(new EPPService("domain", "urn:iana:xml:ns:domain", "urn:iana:xml:ns:domain domain-1.0.xsd"));
		   services.addElement(new EPPService("host", "urn:iana:xml:ns:host", "urn:iana:xml:ns:host host.xsd"));
		   services.addElement(new EPPService("contact", "urn:iana:xml:ns:contact", "urn:iana:xml:ns:contact contact.xsd"));
		 */
		Vector statements = new Vector();

		// DCP Statement Element
		EPPStatement statement = new EPPStatement();

		// DCP Receipient element
		EPPRecipient recipient = new EPPRecipient();
		recipient.setOther(true);
		recipient.addOurs("My description");
		recipient.setPublic(true);

		// DCP Purpose element
		EPPPurpose purpose = new EPPPurpose(true, true, true, true);
		
		Assert.assertTrue(purpose.isAdmin());
		Assert.assertTrue(purpose.isContact());
		Assert.assertTrue(purpose.isOther());
		Assert.assertTrue(purpose.isProv());
		
		purpose.setAdmin(false);
		purpose.setProv(false);
		
		
		statement.setRecipient(recipient);
		statement.setPurpose(purpose);
		statement.setRetention(EPPStatement.RETENTION_BUSINESS);

		EPPStatement statement1 = new EPPStatement();
		EPPPurpose   purpose2 = new EPPPurpose();
		purpose2.setAdmin(true);
		purpose2.setProv(true);
		statement1.setRecipient(recipient);
		statement1.setPurpose(purpose2);
		statement1.setRetention(EPPStatement.RETENTION_INDEFINITE);

		statements.addElement(statement);
		statements.addElement(statement1);

		serviceMenu.setObjectServices(services);

		//serviceMenu.setExtensionServices(extservices);
		EPPDcp dcp = new EPPDcp(EPPDcp.ACCESS_ALL, statements);

		theGreeting.setDcp(dcp);

		theGreeting.setServer("Example Company EPP server epp.example.com");
		theGreeting.setServerDate(new Date());

		// These settings are required since they are the default values, but used
		// to demonstrate the use of the API.
		theGreeting.getServiceMenu().setVersion("1.0");

		Vector langs = new Vector();
		langs.addElement("en");
		langs.addElement("fr");
		theGreeting.getServiceMenu().setLangs(langs);

		EPPEncodeDecodeStats stats = testEncodeDecode(theGreeting);
		System.out.println(stats);

		printEnd("testGreeting");
	} // End EPPCodecTst.testGreeting()

	/**
	 * Unit test of <code>EPPLoginCmd</code>.  The response to
	 * <code>EPPLoginCmd</code>     is <code>EPPResponse</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testLogin() {
		printStart("testLogin");

		Vector services = new Vector();

		// Object services
		EPPService service =
			new EPPService(
						   "domain", "urn:ietf:params:xml:ns:domain-1.0",
						   "urn:iana:xml:ns:domain-1.0 domain-1.0.xsd");
		service.setServiceType(EPPService.OBJ_SERVICE);

		services.addElement(service);

		// Extension services
		Vector     extservices = new Vector();
		EPPService extservice =
			new EPPService(
						   "domain-ext", "urn:ietf:params:xml:ns:domainext-1.0",
						   "urn:iana:xml:ns:domainext-1.0 domainext-1.0.xsd");
		extservice.setServiceType(EPPService.EXT_SERVICE);
		extservices.addElement(extservice);

		EPPLoginCmd theCommand =
			new EPPLoginCmd("ABC-12345", "ClientX", "foo-BAR2", "bar-FOO2");

		theCommand.setServices(services);
		theCommand.setExtensionServices(extservices);

		// These settings are required since they are the default values, but used
		// to demonstrate the use of the API.
		theCommand.setVersion("1.0");
		theCommand.setLang("en");

		EPPEncodeDecodeStats commandStats =
			testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode Login Response (Standard EPPResponse)
		EPPResponse theResponse;
		EPPTransId  respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS);

		EPPEncodeDecodeStats responseStats =
			testEncodeDecode(theResponse);
		System.out.println(responseStats);

		printEnd("testLogin");
	} // End EPPCodecTst.testLogin()

	/**
	 * Unit test of <code>EPPLogoutCmd</code>.  The response to
	 * <code>EPPLogoutCmd</code>     is <code>EPPResponse</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testLogout() {
		printStart("testLogout");

		EPPLogoutCmd		 theCommand = new EPPLogoutCmd("ABC-12345");

		EPPEncodeDecodeStats commandStats =
			testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode Logout Response (Standard EPPResponse)
		EPPResponse theResponse;
		EPPTransId  respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS_END_SESSION);

		EPPEncodeDecodeStats responseStats =
			testEncodeDecode(theResponse);
		System.out.println(responseStats);

		printEnd("testLogout");
	} // End EPPCodecTst.testLogout()

	/**
	 * Unit test of <code>EPPResponse</code>.  This will test
	 * <code>EPPResponse</code>     with various attribute values.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testResponse() {
		printStart("testResponse");

		EPPResponse theResponse;
		theResponse = new EPPResponse(new EPPTransId("ABC-12345", "54321-XYZ"));
		theResponse.setResult(EPPResult.SUCCESS);

		System.out.println("EPPResponse.toString = [" + theResponse + "]");
		System.out.println("EPPTransId.toString = [" + theResponse.getTransId()
						   + "]");

		EPPEncodeDecodeStats responseStats =
			testEncodeDecode(theResponse);
		System.out.println(responseStats);

		theResponse = new EPPResponse(new EPPTransId("ABC-12345", "54321-XYZ"));

		EPPResult theResult = new EPPResult(EPPResult.SUCCESS);
		theResult.addValue(new EPPValue("<epp:helloWorld/>"));
		theResult.addExtValue(new EPPExtValue("My Reason"));
		theResponse.setResult(theResult);

		System.out.println("EPPResponse.toString = [" + theResponse + "]");

		responseStats = testEncodeDecode(theResponse);
		System.out.println(responseStats);

		printEnd("testResponse");
	} // End EPPCodecTst.testResponse()


	/**
	 * A stubbed out test extension used for tests that simply add, remove,
	 * check for existence and get an extension. This extension does not really
	 * encode or decode XML, so it should not be used with
	 * <code>EPPCodecTst.testEncodeDecode(EPPMessage)</code>.
	 */
	private static class Test1Extension implements EPPCodecComponent {

		public Element encode(Document aDocument) throws EPPEncodeException {
			Element root = aDocument.createElementNS(EPPCodec.NS, "test1");
			return root;
		}

		public void decode(Element aElement) throws EPPDecodeException {

		}

		public Object clone() throws CloneNotSupportedException {
			return null;
		}

	} // End class EPPCodecTst.Test1Extension

	/**
	 * A stubbed out test extension used for tests that simply add, remove,
	 * check for existence and get an extension. This extension does not really
	 * encode or decode XML, so it should not be used with
	 * {@link EPPCodecTst#testEncodeDecode(EPPMessage)}.
	 */
	private static class Test2Extension implements EPPCodecComponent {

		public Element encode(Document aDocument) throws EPPEncodeException {
			Element root = aDocument.createElementNS(EPPCodec.NS, "test1");
			return root;
		}

		public void decode(Element aElement) throws EPPDecodeException {

		}

		public Object clone() throws CloneNotSupportedException {
			return null;
		}

	} // End class EPPCodecTst.Test2Extension

	/**
	 * Unit test for using {@link EPPCommand#getExtension(Class, boolean)}.
	 */
	public void testCommandGetExtensionFailOnDuplicate() {
		printStart("testCommandGetExtensionFailOnDuplicate");

		EPPPollCmd theCommand = new EPPPollCmd("ABC-12345", "req");

		EPPCodecComponent theExt1 = new Test1Extension();
		theCommand.addExtension(theExt1);

		EPPCodecComponent theExt2 = new Test2Extension();
		theCommand.addExtension(theExt2);

		try {
			Assert.assertEquals(theExt1, theCommand.getExtension(
					Test1Extension.class, true));
			Assert.assertEquals(theExt2, theCommand.getExtension(
					Test2Extension.class, true));
		}
		catch (EPPDuplicateExtensionException e) {
			Assert
					.fail("testCommandGetExtensionFailOnDuplicate: Failed on duplicate: "
							+ e);
		}

		// Add duplicates
		theCommand.addExtension(new Test1Extension());
		theCommand.addExtension(new Test2Extension());

		// Ensure that duplicate returns first inserted without duplicate test
		try {
			Assert.assertEquals(theExt1, theCommand.getExtension(
					Test1Extension.class, false));
		}
		catch (EPPDuplicateExtensionException e) {
			Assert
					.fail("testCommandGetExtensionFailOnDuplicate: Failed on getting duplicate extension with failOnDuplicate parameter as false: "
							+ e);
		}

		// Test that duplicate extension 1 is caught
		try {
			theCommand.getExtension(Test1Extension.class, true);
			Assert
					.fail("testCommandGetExtensionFailOnDuplicate: Should have failed with EPPDuplicateExtensionException for Test1Extension.class");
		}
		catch (EPPDuplicateExtensionException e) {
			Assert
					.assertEquals(theExt1.getClass(), e.getExtension()
							.getClass());
		}

		// Test that duplicate extension 2 is caught
		try {
			theCommand.getExtension(Test2Extension.class, true);
			Assert
					.fail("testCommandGetExtensionFailOnDuplicate: Should have failed with EPPDuplicateExtensionException for Test2Extension.class");
		}
		catch (EPPDuplicateExtensionException e) {
			Assert
					.assertEquals(theExt2.getClass(), e.getExtension()
							.getClass());
		}

		printEnd("testCommandGetExtensionFailOnDuplicate");
	} // End EPPCodecTst.testCommandGetExtensionFailOnDuplicate()

	/**
	 * Unit test for using {@link EPPResponse#getExtension(Class, boolean)}.
	 */
	public void testResponseGetExtensionFailOnDuplicate() {
		printStart("testResponseGetExtensionFailOnDuplicate");

		EPPResponse theResponse = new EPPResponse(new EPPTransId("ABC-12345",
				"54321-XYZ"));
		theResponse.setResult(EPPResult.SUCCESS);

		EPPCodecComponent theExt1 = new Test1Extension();
		theResponse.addExtension(theExt1);

		EPPCodecComponent theExt2 = new Test2Extension();
		theResponse.addExtension(theExt2);

		try {
			Assert.assertEquals(theExt1, theResponse.getExtension(
					Test1Extension.class, true));
			Assert.assertEquals(theExt2, theResponse.getExtension(
					Test2Extension.class, true));
		}
		catch (EPPDuplicateExtensionException e) {
			Assert
					.fail("testResponseGetExtensionFailOnDuplicate: Failed on duplicate: "
							+ e);
		}

		// Add duplicates
		theResponse.addExtension(new Test1Extension());
		theResponse.addExtension(new Test2Extension());

		// Ensure that duplicate returns first inserted without duplicate test
		try {
			Assert.assertEquals(theExt1, theResponse.getExtension(
					Test1Extension.class, false));
		}
		catch (EPPDuplicateExtensionException e) {
			Assert
					.fail("testResponseGetExtensionFailOnDuplicate: Failed on getting duplicate extension with failOnDuplicate parameter as false: "
							+ e);
		}

		// Test that duplicate extension 1 is caught
		try {
			theResponse.getExtension(Test1Extension.class, true);
			Assert
					.fail("testResponseGetExtensionFailOnDuplicate: Should have failed with EPPDuplicateExtensionException for Test1Extension.class");
		}
		catch (EPPDuplicateExtensionException e) {
			Assert
					.assertEquals(theExt1.getClass(), e.getExtension()
							.getClass());
		}

		// Test that duplicate extension 2 is caught
		try {
			theResponse.getExtension(Test2Extension.class, true);
			Assert
					.fail("testResponseGetExtensionFailOnDuplicate: Should have failed with EPPDuplicateExtensionException for Test2Extension.class");
		}
		catch (EPPDuplicateExtensionException e) {
			Assert
					.assertEquals(theExt2.getClass(), e.getExtension()
							.getClass());
		}

		printEnd("testResponseGetExtensionFailOnDuplicate");
	} // End EPPCodecTst.testResponseGetExtensionFailOnDuplicate()
	
	
	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar".
	 */
	protected void setUp() {
		// EPPTransId.setDefaultClientId("ClientX");
	} // End EPPCodecTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	} // End EPPCodecTst.tearDown();

	/**
	 * Initialize the SDK environment based on the system properties:<br>
	 * <br><ul>
	 * <li>validate - <code>true</code> or <code>false</code> value indicating
	 * if XML schema validation is enabled.  The default value is <code>true</code>
	 * <li>fullschemachecking - <code>true</code> or <code>false</code> value indicating
	 * if full XML schema validation is enabled.  The default value is <code>true</code>.
	 * <li>logfile - Log file name.  The default value is <code>epp.log</code>.
	 * </ul>
	 * <br>
	 * Both the <code>Environment</code> class and the logging will be initialized.
	 */
	public static void initEnvironment() {
		if (!initialized) {
			// Initialize SDK properties
			Properties theProps = new Properties();
			theProps.setProperty("EPP.Validating", System.getProperty("validate", "true"));
			theProps.setProperty("EPP.FullSchemaChecking", System.getProperty("fullschemachecking", "true"));
			Environment.setProperties(theProps);
			
			// Initialize logger
			try {
				Logger root = Logger.getRootLogger();
				root.setLevel(Level.DEBUG);
				root.addAppender(new FileAppender(
								new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN),
								System.getProperty("logfile", "epp.log"), true));
			}
			catch (IOException ex) {
				System.err.println("IOException initializing log4j");
				ex.printStackTrace();
				System.exit(1);
			}

			initialized = true;
		}
	} // End EPPCodecTst.initEnvironment()

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPCodecTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		
		initEnvironment();
		
		TestSuite suite = new TestSuite(EPPCodecTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		return suite;
	} // End EPPCodecTst.suite()

	/**
	 * Unit test main, which accepts the following system property options:<br>
	 * 
	 * <ul>
	 * <li>
	 * iterations    Number of unit test iterations to run
	 * </li>
	 * <li>
	 * validate    Turn XML validation on (<code>true</code>) or off
	 * (<code>false</code>).     If validate is not specified, validation will
	 * be off.
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param args DOCUMENT ME!
	 */
	public static void main(String[] args) {
		// Number of Threads
		int    numThreads = 1;
		String threadsStr = System.getProperty("threads");

		if (threadsStr != null) {
			numThreads = Integer.parseInt(threadsStr);
		}

		// Run test suite in multiple threads?
		if (numThreads > 1) {
			// Spawn each thread passing in the Test Suite
			for (int i = 0; i < numThreads; i++) {
				TestThread thread =
					new TestThread(
								   "EPPCodecTst Thread " + i,
								   EPPCodecTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPCodecTst.suite());
		}
	} // End EPPCodecTst.main(String [])

	/**
	 * Print the start of a test with the <code>Thread</code> name if the
	 * current     thread is a <code>TestThread</code>.
	 *
	 * @param aTest name for the test
	 */
	public static void printStart(String aTest) {
		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ": ");
		}

		System.out.println("Start of " + aTest);
		System.out.println("*************************************************************************************\n");
	} // End EPPCodecTst.testStart(String)

	/**
	 * Print the end of a test with the <code>Thread</code> name if the current
	 * thread is a <code>TestThread</code>.
	 *
	 * @param aTest name for the test
	 */
	public static void printEnd(String aTest) {
		System.out.println("*************************************************************************************");

		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ": ");
		}

		System.out.println("End of " + aTest);
		System.out.println("\n");
	} // End EPPCodecTst.testEnd(String)

	
	/**
	 * Reusable unit test algorithm, which does the following:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * encodes the <code>EPPMessage</code> in XML
	 * </li>
	 * <li>
	 * decodes the <code>EPPMessage</code> from XML
	 * </li>
	 * <li>
	 * Serializes <code>EPPMessage</code> using Java Serialization
	 * </li>
	 * <li>
	 * De-serializes <code>EPPMessage</code> using Java Serialization
	 * </li>
	 * <li>
	 * Gathers unit test statistics and output them to standard out
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aMessage EPP Message to test encoding and decoding
	 *
	 * @return DOCUMENT ME!
	 */
	public static EPPEncodeDecodeStats testEncodeDecode(
														EPPMessage aMessage
														) {
		Document			 doc    = null;
		EPPCodec			 codec  = EPPCodec.getInstance();
		DocumentBuilder		 parser = null;

		EPPEncodeDecodeStats retStats = new EPPEncodeDecodeStats(aMessage);

		parser = new EPPSchemaCachingParser();
		parser.setErrorHandler(xmlErrorHandler);

		retStats.setXmlValidating(parser.isValidating());

		for (int i = 1; i <= numIterations; i++) {
			// XML Encoding/Decoding
			try {
				retStats.startXmlTimer();

				// EPPMessage -> Document
				doc = codec.encode(aMessage);

				// Serialize XML
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				
				TransformerFactory transFac = TransformerFactory.newInstance();
				Transformer trans = transFac.newTransformer();
				trans.setOutputProperty(OutputKeys.INDENT, "yes");
				trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				trans.setOutputProperty(OutputKeys.STANDALONE, "no");
				trans.transform(new DOMSource(doc.getDocumentElement()), new StreamResult(byteOut));
				byteOut.close();
				
				System.out.println("\n");
				System.out.write(byteOut.toByteArray());
				System.out.println("\n");

				// De-serialize XML
				ByteArrayInputStream byteIn =
					new ByteArrayInputStream(byteOut.toByteArray());

				//System.out.println("before parsing");
				Document decodedDoc = parser.parse(byteIn);

				//System.out.println("arriving until here 1");
				// Document -> EPPMessage
				EPPMessage decodedMessage = codec.decode(decodedDoc);

				//System.out.println("after decoding the document"+decodedMessage);
				retStats.stopXmlTimer();

				// EPPMessage's not equal?
				assertEquals(aMessage, decodedMessage);

				// Write format to stats
				if (retStats.getXmlFormat() == null) {
					retStats.setXmlFormat(byteOut.toByteArray());
				}
				
				// Use xslt to change prefix and test again
				retStats.startXmlTimer();

				TransformerFactory factory = TransformerFactory.newInstance();
//				Transformer transformer = factory
//						.newTransformer(new StreamSource(EPPCodecTst.class
//								.getResourceAsStream("/namespaceTransform.xsl")));
				Transformer transformer = factory
						.newTransformer(new StreamSource(new StringReader(NAMESPACE_TRANSFORM_XSL)));
				
				ByteArrayOutputStream byteOutNamespaceTransformed = new ByteArrayOutputStream();
				transformer.transform(new StreamSource(
						new ByteArrayInputStream(byteOut.toByteArray())),
						new StreamResult(byteOutNamespaceTransformed));
				byteOutNamespaceTransformed.close();
				byteOut = byteOutNamespaceTransformed;

				// Decode the transformed encoded xml
				System.out.println("\n");
				System.out.write(byteOut.toByteArray());
				System.out.println("\n");

				// De-serialize XML
				byteIn = new ByteArrayInputStream(byteOut.toByteArray());

				// System.out.println("before parsing");
				decodedDoc = parser.parse(byteIn);

				// System.out.println("arriving until here 1");
				// Document -> EPPMessage
				decodedMessage = codec.decode(decodedDoc);

				// System.out.println("after decoding the document"+decodedMessage);
				retStats.stopXmlTimer();

				// EPPMessage's not equal?
				assertEquals(aMessage, decodedMessage);

				// Write format to stats
				if (retStats.getXmlFormat() == null) {
					retStats.setXmlFormat(byteOut.toByteArray());
				}
			}
			 catch (EPPEncodeException e) {
				e.printStackTrace();
				Assert.fail("EPPEncodeException encoding " + retStats.getName()
							+ ": " + e);
			}
			 catch (EPPDecodeException e) {
				e.printStackTrace();
				Assert.fail("EPPDecodeException decoding " + retStats.getName()
							+ ": " + e);
			}
			 catch (Exception e) {
				e.printStackTrace();
				Assert.fail("General Exception encoding/decoding of "
							+ retStats.getName() + ": " + e);
			}

			// Java Serialization
			try {
				retStats.startSerialTimer();

				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				ObjectOutputStream    out = new ObjectOutputStream(byteOut);

				out.writeObject(aMessage);
				out.close();

				ObjectInputStream in =
					new ObjectInputStream(new ByteArrayInputStream(byteOut
																   .toByteArray()));

				EPPMessage		  serializedMessage =
					(EPPMessage) in.readObject();
				in.close();

				retStats.stopSerialTimer();

				// EPPMessage's not equal?
				assertEquals(aMessage, serializedMessage);

				// Write size to stats
				if (retStats.getSerialSize() == 0) {
					retStats.setSerialSize(byteOut.toByteArray().length);
				}
			}
			 catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Java Serialization of " + retStats.getName()
							+ ": " + e);
			}

			// Java Cloning
			try {
				EPPMessage clone = (EPPMessage) aMessage.clone();
				assertEquals(aMessage, clone);
			}
			 catch (Exception e) {
				e.printStackTrace();
				Assert.fail("Java Cloning Exception of " + retStats.getName()
							+ ": " + e);
			}
		} // end for (int i = 1; i <= numIterations; i++)
		
		return retStats;
	} // End EPPCodecTst.testEncodeDecode(EPPMessage)

	/**
	 * Sets the number of iterations to run per test.
	 *
	 * @param aNumIterations number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	} // End EPPCodecTst.setNumIterations(long)


	/**
	 * Unit test of <code>EPPHello</code>.  The response to
	 * <code>EPPHello</code>     is <code>EPPResponse</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testHello() {
		printStart("testLogout");

		EPPHello			 theCommand = new EPPHello();

		EPPEncodeDecodeStats commandStats =
			testEncodeDecode(theCommand);
		System.out.println(commandStats);

		printEnd("testLogout");
	} // End EPPCodecTst.testHello()

	/**
	 * Unit test of <code>EPPPollCmd</code>.  The response to
	 * <code>EPPPollCmd</code>     is <code>EPPResponse</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testPoll() {
		printStart("testPoll");

		// Poll command
		EPPPollCmd			 theCommand   = new EPPPollCmd("ABC-12345", "req");
		EPPEncodeDecodeStats commandStats =
			testEncodeDecode(theCommand);
		System.out.println(commandStats);

		theCommand		 = new EPPPollCmd("ABC-12345", "ack", "12345");
		commandStats     = testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Poll Response with message
		EPPResponse theResponse;
		EPPTransId  respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ-1");
		theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS_POLL_MSG);
		theResponse.setMsgQueue(new EPPMsgQueue(
												new Long(5), "12345", new Date(),
												"Transfer request pending"));

		EPPEncodeDecodeStats responseStats =
			testEncodeDecode(theResponse);
		System.out.println(responseStats);
		
		// Poll Response with mixed message
		respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-MIXED-MSG-1");
		theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS_POLL_MSG);
		
		// Create NodeList
		Element theElm;
		Text theText;
		Document theDoc = new DocumentImpl();
		DocumentFragment theFragment = theDoc.createDocumentFragment();
		
		theElm = theDoc.createElement("code");
		theText = theDoc.createTextNode("1");
		theElm.appendChild(theText);
		theFragment.appendChild(theElm);
		
		theElm = theDoc.createElement("txt");
		theText = theDoc.createTextNode("Pending domain created completed.");
		theElm.appendChild(theText);
		theFragment.appendChild(theElm);

		theElm = theDoc.createElement("objectId");
		theText = theDoc.createTextNode("example.com");
		theElm.appendChild(theText);
		theFragment.appendChild(theElm);
		
		theResponse.setMsgQueue(new EPPMsgQueue(
												new Long(5), "54321", new Date(),
												theFragment.getChildNodes()));

		responseStats =
			testEncodeDecode(theResponse);
		System.out.println(responseStats);
		

		// Poll Response with no messages
		respTransId     = new EPPTransId(
										 theCommand.getTransId(), "54321-XYZ-2");
		theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS_POLL_NO_MSGS);

		responseStats = testEncodeDecode(theResponse);
		System.out.println(responseStats);

		printEnd("testPoll");
	} // End EPPCodecTst.testPoll()
	
	/**
	 * Test the encoding and decoding of the <code>timeInstantFormat</code>
	 * supported by the {@link EPPUtil#encodeTimeInstant(Date)} and 
	 * {@link EPPUtil#decodeTimeInstant(String)}.  
	 */
	public void testTimeInstantFormat() {
		printStart("testTimeInstantFormat");
		
		String theDateStr = "2014-04-24T19:42:58.588Z";
		
		Date theDecodedDate = EPPUtil.decodeTimeInstant(theDateStr);
		
		String theEncodedDateStr = EPPUtil.encodeTimeInstant(theDecodedDate);
		
		Assert.assertEquals(theDateStr, theEncodedDateStr);
				
		// Set just the time instant format
		EPPUtil.setTimeInstantFormat("yyyy-MM-dd'T'HH':'mm':'ss'Z'");
		
		theEncodedDateStr = EPPUtil.encodeTimeInstant(theDecodedDate);
		
		Assert.assertEquals(theEncodedDateStr, "2014-04-24T19:42:58Z");
		
		EPPUtil.setTimeInstantFormat(EPPUtil.DEFAULT_TIME_INSTANT_FORMAT);
		
		printEnd("testTimeInstantFormat");	
	}
	
} // End class EPPCodeecTst

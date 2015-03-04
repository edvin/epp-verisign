/***********************************************************

 The information in this document is proprietary
 to VeriSign and the VeriSign Registry Business.
 It may not be used, reproduced or disclosed without
 the written approval of the General Manager of
 VeriSign Global Registry Services.

 PRIVILEDGED AND CONFIDENTIAL
 VERISIGN PROPRIETARY INFORMATION
 REGISTRY SENSITIVE INFORMATION

 Copyright (c) 2007 VeriSign, Inc.  All rights reserved.

 ***********************************************************/

//----------------------------------------------
//
// package
//
//----------------------------------------------
package com.verisign.epp.interfaces;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------

// Java Core Imports
import java.util.Random;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.contact.EPPContactAddress;
import com.verisign.epp.codec.contact.EPPContactCreateResp;
import com.verisign.epp.codec.contact.EPPContactDisclose;
import com.verisign.epp.codec.contact.EPPContactDiscloseAddress;
import com.verisign.epp.codec.contact.EPPContactDiscloseName;
import com.verisign.epp.codec.contact.EPPContactDiscloseOrg;
import com.verisign.epp.codec.contact.EPPContactInfoResp;
import com.verisign.epp.codec.contact.EPPContactPostalDefinition;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.jobscontact.EPPJobsContactCreateCmd;
import com.verisign.epp.codec.jobscontact.EPPJobsContactInfoResp;
import com.verisign.epp.codec.jobscontact.EPPJobsContactUpdateCmd;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the using the Contact Extension classes with the
 * <code>EPPContact</code> class. The unit test will initialize a session with
 * an EPP Server, will invoke <code>EPPContact</code> operations with JobsContact
 * Extensions, and will end a session with an EPP Server. The configuration file
 * used by the unit test defaults to epp.config, but can be changed by passing
 * the file path as the first command line argument. The unit test can be run in
 * multiple threads by setting the "threads" system property. For example, the
 * unit test can be run in 2 threads with the configuration file
 * ../../epp.config with the following command: <br>
 * <br>
 * java com.verisign.epp.interfaces.EPPJobsContactTst -Dthreads=2
 * ../../epp.config <br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a>
 */
public class EPPJobsContactTst extends TestCase {

	/**
	 * Allocates an <code>EPPJobsContactTest</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 * 
	 * @param name
	 *            Logical name of the test
	 */
	public EPPJobsContactTst(String name) {
		super(name);
	} // End EPPJobsContactTst(String)

	/**
	 * JUNIT test method to implement the
	 * <code>EPPJobsContactTest TestCase</code>. Each sub-test will be invoked
	 * in order to satisfy testing the EPPDomain interface.
	 */
	public void testJobsContact() {

		int numIterations = 1;

		String iterationsStr = System.getProperty("iterations");

		if (iterationsStr != null) {
			numIterations = Integer.parseInt(iterationsStr);
		}

		for (iteration = 0; (numIterations == 0) || (iteration < numIterations); iteration++) 
		{
			printStart("Test Suite");

			jobsContactCreate();
			jobsContactInfo();
			jobsContactUpdate();
			jobsContactCheck();
			jobsContactDelete();
			
			printEnd("Test Suite");
		}

	} // End EPPJobsContactTst.testJobsContact()

	/**
	 * Unit test of using the <code>EPPJobsContactInfo</code> Extension with
	 * <code>EPPContact</code> info command and to validate the existence and
	 * attributes of the <code>EPPJobsContactInfoData</code> extension in the info
	 * response.
	 */
	public void jobsContactInfo() {

		printStart("jobsContactInfo");

		try {
			
			EPPContactInfoResp contactResponse;

			System.out.println("JobsContact: Contact Info");

			contact.setTransId("ABC-12345-XYZ");
			contact.addContactId("helloworld");
			
			contactResponse = contact.sendInfo();

			// -- Output all of the domain info response attributes
			System.out.println("JobsContact: Response = [" + contactResponse
					+ "]\n\n");

			// -- Output extension attribute(s)
			if (contactResponse.hasExtension(EPPJobsContactInfoResp.class)) {

				EPPJobsContactInfoResp ext = (EPPJobsContactInfoResp) contactResponse
						.getExtension(EPPJobsContactInfoResp.class);

				System.out.println("jobsContact: Title = " + ext.getTitle());
				System.out.println("jobsContact: Website = " + ext.getWebsite());
				System.out.println("jobsContact: industryType = " + ext.getIndustryType());
				System.out.println("jobsContact: isAdminContact = " + ext.isAdminContact());
				System.out.println("jobsContact: isAssociationMember = " + ext.isAssociationMember());
			}
			else {
				Assert.fail("JobsContact: EPPJobsContact extension NOT found");
			}			
		}
		catch (EPPCommandException e) {
			handleException(e);
		}
		printEnd("jobsContactInfo");

	} // End EPPJobsContactTst.jobsContactInfo()


	/**
	 * Unit test of <code>EPPContact.sendCreate</code>.
	 */
	public void jobsContactCreate() {
		
		printStart("jobsContactCreate");

		EPPContactCreateResp response;
		String theName = this.makeContactName();

		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out.println("jobsContactCreate: Contact create for " + theName);
			contact.setTransId("ABC-12345-XYZ");
			contact.setAuthorizationId("ClientXYZ");
			contact.addContactId(theName);
			contact.setVoicePhone("+1.7035555555");
			contact.setVoiceExt("123");
			contact.setFaxNumber("+1.7035555556");
			contact.setFaxExt("456");
			contact.setEmail("jdoe@example.com");

			// Streets
			Vector streets = new Vector();
			streets.addElement("123 Example Dr.");
			streets.addElement("Suite 100");
			streets.addElement("This is third line");

			EPPContactAddress address = new EPPContactAddress();

			address.setStreets(streets);
			address.setCity("Dulles");
			address.setStateProvince("VA");
			address.setPostalCode("20166-6503");
			address.setCountry("US");

			EPPContactPostalDefinition name = new EPPContactPostalDefinition(
					EPPContactPostalDefinition.ATTR_TYPE_LOC);

			name.setName("John Doe");
			name.setOrg("Example Inc.");
			name.setAddress(address);

			contact.addPostalInfo(name);

			// this is not a valid Example but it will do
			EPPContactAddress Intaddress = new EPPContactAddress();

			Intaddress.setStreets(streets);
			Intaddress.setCity("Dulles");
			Intaddress.setStateProvince("VA");
			Intaddress.setPostalCode("20166-6503");
			Intaddress.setCountry("US");

			EPPContactPostalDefinition Intname = new EPPContactPostalDefinition(
					EPPContactPostalDefinition.ATTR_TYPE_INT);

			Intname.setName("John Doe");
			Intname.setOrg("Example Inc.");
			Intname.setAddress(Intaddress);

			contact.addPostalInfo(Intname);

			// disclose names
			Vector names = new Vector();

			// names.addElement(new
			// EPPContactDiscloseName(EPPContactDiscloseName.ATTR_TYPE_LOC));
			names.addElement(new EPPContactDiscloseName(
					EPPContactDiscloseName.ATTR_TYPE_INT));

			// disclose orgs
			Vector orgs = new Vector();
			orgs.addElement(new EPPContactDiscloseOrg(
					EPPContactDiscloseOrg.ATTR_TYPE_LOC));
			orgs.addElement(new EPPContactDiscloseOrg(
					EPPContactDiscloseOrg.ATTR_TYPE_INT));

			// disclose addresses
			Vector addresses = new Vector();
			addresses.addElement(new EPPContactDiscloseAddress(
					EPPContactDiscloseAddress.ATTR_TYPE_LOC));
			addresses.addElement(new EPPContactDiscloseAddress(
					EPPContactDiscloseAddress.ATTR_TYPE_INT));

			// disclose
			EPPContactDisclose disclose = new EPPContactDisclose();
			disclose.setFlag("0");
			disclose.setNames(names);
			disclose.setOrgs(orgs);
			disclose.setAddresses(addresses);
			disclose.setVoice("");
			disclose.setFax("");
			disclose.setEmail("");

			contact.setDisclose(disclose);

			EPPJobsContactCreateCmd createExt = new EPPJobsContactCreateCmd("SE", "www.verisign.com", "IT", "Yes", "No");
						
			contact.addExtension(createExt);
			
			response = (EPPContactCreateResp) contact.sendCreate();

			// -- Output all of the response attributes
			System.out.println("contactCreate: Response = [" + response
					+ "]\n\n");
			System.out.println("Contact ID : " + response.getId());
			System.out.println("Contact Creation Date : " + response.getCreationDate());
			
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("jobsContactCreate");
	}
	

	/**
	 * Unit test of <code>EPPContact.sendUpdate</code>.
	 */
	public void jobsContactUpdate() {
		
		printStart("jobsContactUpdate");

		EPPResponse response;
		String theName = this.makeContactName();

		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out
					.println("\njobsContactUpdate: Contact update for " + theName);
			contact.setTransId("ABC-12345-XYZ");
			contact.addContactId(theName);

			// Streets
			Vector streets = new Vector();
			streets.addElement("123 Example Dr.");
			streets.addElement("Suite 100");
			streets.addElement("This is third line");

			// Address
			EPPContactAddress address = new EPPContactAddress(streets,
					"Dulles", "VA", "20166-6503", "US");

			EPPContactPostalDefinition postal = new EPPContactPostalDefinition(
					"Joe Brown", "Example Corp.",
					EPPContactPostalDefinition.ATTR_TYPE_LOC, address);

			// statuses
			contact.addStatus(EPPContact.STAT_PENDING_DELETE);

			contact.addPostalInfo(postal);
			contact.setVoicePhone("+1.7035555555");
			contact.setVoiceExt("456");
			contact.setFaxNumber("+1.7035555555");
			contact.setFaxExt("789");
			contact.setAuthorizationId("ClientXYZ");

			// disclose names
			Vector names = new Vector();

			// names.addElement(new
			// EPPContactDiscloseName(EPPContactDiscloseName.ATTR_TYPE_LOC));
			names.addElement(new EPPContactDiscloseName(
					EPPContactDiscloseName.ATTR_TYPE_INT));

			// disclose orgs
			Vector orgs = new Vector();
			orgs.addElement(new EPPContactDiscloseOrg(
					EPPContactDiscloseOrg.ATTR_TYPE_LOC));
			orgs.addElement(new EPPContactDiscloseOrg(
					EPPContactDiscloseOrg.ATTR_TYPE_INT));

			// disclose addresses
			Vector addresses = new Vector();
			addresses.addElement(new EPPContactDiscloseAddress(
					EPPContactDiscloseAddress.ATTR_TYPE_LOC));
			addresses.addElement(new EPPContactDiscloseAddress(
					EPPContactDiscloseAddress.ATTR_TYPE_INT));

			// disclose
			EPPContactDisclose disclose = new EPPContactDisclose();
			disclose.setFlag("0");
			disclose.setNames(names);
			disclose.setOrgs(orgs);
			disclose.setAddresses(addresses);
			disclose.setVoice("");
			disclose.setFax("");
			disclose.setEmail("");

			contact.setDisclose(disclose);
		
			EPPJobsContactUpdateCmd updateExt = new EPPJobsContactUpdateCmd();
						
			updateExt.setTitle("Customer Service");
			updateExt.setWebsite("www.verisign.com");
			updateExt.setIndustry("IT");
			updateExt.setAdminContact("No");			
			updateExt.setAssociationMember("No");
			
			contact.addExtension(updateExt);
			
			response = contact.sendUpdate();

			// -- Output all of the response attributes
			System.out.println("jobsContactUpdate: Response = [" + response
					+ "]\n\n");
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("jobsContactUpdate");
	}

	
	/**
	 * Unit test of <code>EPPContact.sendCheck</code>.
	 */
	public void jobsContactCheck() {
		
		printStart("jobsContactCheck");

		EPPResponse response;

		try {
			System.out
					.println("\n----------------------------------------------------------------");
			
			contact.setTransId("ABC-12345-XYZ");
			contact.addContactId(this.makeContactName());
			contact.addContactId(this.makeContactName());
			contact.addContactId(this.makeContactName());		
			
			response = contact.sendCheck();
			
			System.out.println("jobsContactCheck: Response = [" + response + "]\n\n");
		}
		catch (EPPCommandException e) {
			handleException(e);
		}
		printEnd("jobsContactCheck");
	}
	
	
	/**
	 * Unit test of <code>EPPContact.sendDelete</code>.
	 */
	public void jobsContactDelete() {
		
		printStart("jobsContactDelete");

		EPPResponse response;

		try {
			System.out
					.println("\n----------------------------------------------------------------");
			
			contact.setTransId("ABC-12345-XYZ");
			contact.addContactId(this.makeContactName());			
			
			response = contact.sendDelete();
			
			System.out.println("jobsContactDelete: Response = [" + response + "]\n\n");
		}
		catch (EPPCommandException e) {
			handleException(e);
		}
		printEnd("jobsContactDelete");
	}
	
	/**
	 * Unit test of <code>EPPSession.initSession</code>. The session
	 * attribute is initialized with the attributes defined in the EPP sample
	 * files.
	 */
	private void initSession() {

		printStart("initSession");
		// Set attributes for initSession
		session.setTransId("ABC-12345-XYZ");
		session.setVersion("1.0");
		session.setLang("en");

		// Initialize the session
		try {
			session.initSession();
		}
		catch (EPPCommandException e) {
			EPPResponse contactResponse = session.getResponse();

			// Is a server specified error?
			if ((contactResponse != null) && (!contactResponse.isSuccess())) {
				Assert.fail("Server Error : " + contactResponse);
			}
			else {
				e.printStackTrace();
				Assert.fail("initSession Error : " + e);
			}
		}

		printEnd("initSession");
	} 
	// End EPPJobsContactTst.initSession()

	/**
	 * Unit test of <code>EPPSession.endSession</code>. The session with the
	 * EPP Server will be terminated.
	 */
	private void endSession() {
		printStart("endSession");
		session.setTransId("ABC-12345-XYZ");

		// End the session
		try {
			session.endSession();
		}
		catch (EPPCommandException e) {
			EPPResponse domainResponse = session.getResponse();

			// Is a server specified error?
			if ((domainResponse != null) && (!domainResponse.isSuccess())) {
				Assert.fail("Server Error : " + domainResponse);
			}
			else // Other error
			{
				e.printStackTrace();
				Assert.fail("initSession Error : " + e);
			}
		}

		printEnd("endSession");
	} 
	// End EPPJobsContactTst.endSession()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar".
	 */
	protected void setUp() {
		try {
			String theSessionClassName = System.getProperty("EPP.SessionClass");

			if (theSessionClassName != null) {
				try {
					Class theSessionClass = Class.forName(theSessionClassName);

					if (!EPPSession.class.isAssignableFrom((theSessionClass))) {
						Assert.fail(theSessionClassName
								+ " is not a subclass of EPPSession");
					}

					session = (EPPSession) theSessionClass.newInstance();
				}
				catch (Exception ex) {
					Assert
							.fail("Exception instantiating EPP.SessionClass value "
									+ theSessionClassName + ": " + ex);
				}
			}
			else {
				session = new EPPSession();
			}

			session.setClientID(Environment.getProperty("EPP.Test.clientId", "ClientX"));
			session.setPassword(Environment.getProperty("EPP.Test.password", "foo-BAR2"));

		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Error initializing the session: " + e);
		}

		initSession();
		contact = new EPPContact(session);

	} 
	// End EPPJobsContactTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	} 
	// End EPPJobsContactTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPJobsContactTst</code>.
	 */
	public static Test suite() {

		TestSuite suite = new TestSuite(EPPJobsContactTst.class);

		String theConfigFileName = System.getProperty("EPP.ConfigFile");
		if (theConfigFileName != null)
			configFileName = theConfigFileName;

		try {
			app.initialize(configFileName);
		}

		catch (EPPCommandException e) {
			e.printStackTrace();

			Assert.fail("Error initializing the EPP Application: " + e);
		}

		return suite;

	} // End EPPJobsContactTst.suite()

	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a
	 * server generated error or a general exception. If the exception was
	 * caused by a server error, "Server Error :<Response XML>" will be
	 * specified. If the exception was caused by a general algorithm error,
	 * "General Error :<Exception Description>" will be specified.
	 * 
	 * @param aException
	 *            Exception thrown during test
	 */
	public void handleException(Exception aException) {
		EPPResponse domainResponse = session.getResponse();
		aException.printStackTrace();

		// Is a server specified error?
		if ((domainResponse != null) && (!domainResponse.isSuccess())) {
			Assert.fail("Server Error : " + domainResponse);
		}
		else {
			Assert.fail("General Error : " + aException);
		}

	} // End EPPJobsContactTst.handleException(EPPCommandException)

	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * <ul>
	 * <li>iterations Number of unit test iterations to run
	 * <li>validate Turn XML validation on (<code>true</code>) or off (<code>false</code>).
	 * If validate is not specified, validation will be off.
	 * </ul>
	 */
	public static void main(String args[]) {

		// Override the default configuration file name?
		if (args.length > 0) {
			configFileName = args[0];
		}

		// Number of Threads
		int numThreads = 1;
		String threadsStr = System.getProperty("threads");

		if (threadsStr != null)
			numThreads = Integer.parseInt(threadsStr);

		// Run test suite in multiple threads?
		if (numThreads > 1) {
			// Spawn each thread passing in the Test Suite
			for (int i = 0; i < numThreads; i++) {
				TestThread thread = new TestThread("EPPSessionTst Thread " + i,
						EPPJobsContactTst.suite());
				thread.start();
			}

		}
		else
			// Single threaded mode.
			junit.textui.TestRunner.run(EPPJobsContactTst.suite());
		try {
			app.endApplication();
		}
		catch (EPPCommandException e) {
			e.printStackTrace();
			Assert.fail("Error ending the EPP Application: " + e);
		}

	} // End EPPJobsContactTst.main(String [])

	/**
	 * Print the start of a test with the <code>Thread</code> name if the
	 * current thread is a <code>TestThread</code>.
	 * 
	 * @param Logical
	 *            name for the test
	 */
	private void printStart(String aTest) {
		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": ");
			cat.info(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aTest + " Start");
		}

		System.out.println("Start of " + aTest);
		System.out
				.println("****************************************************************\n");
	} // End EPPJobsContactTst.testStart(String)

	/**
	 * Print the end of a test with the <code>Thread</code> name if the
	 * current thread is a <code>TestThread</code>.
	 * 
	 * @param Logical
	 *            name for the test
	 */
	private void printEnd(String aTest) {
		System.out
				.println("****************************************************************");
		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": ");
			cat.info(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aTest + " End");
		}

		System.out.println("End of " + aTest);
		System.out.println("\n");
	} // End EPPJobsContactTst.testEnd(String)

	/**
	 * Print message
	 * 
	 * @param aMsg
	 *            message to print
	 */
	private void printMsg(String aMsg) {
		if (Thread.currentThread() instanceof TestThread) {
			System.out.println(Thread.currentThread().getName()
					+ ", iteration " + iteration + ": " + aMsg);
			cat.info(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aMsg);
		}
		else {
			System.out.println(aMsg);
			cat.info(aMsg);
		}
	} // End EPPJobsContactTest.printMsg(String)

	/**
	 * Print error message
	 * 
	 * @param aMsg
	 *            errpr message to print
	 */
	private void printError(String aMsg) {
		if (Thread.currentThread() instanceof TestThread) {
			System.err.println(Thread.currentThread().getName()
					+ ", iteration " + iteration + ": " + aMsg);
			cat.error(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aMsg);
		}
		else {
			System.err.println(aMsg);
			cat.error(aMsg);
		}
	} // End EPPJobsContactTst.printError(String)

	/**
	 * This method tries to generate a unique String as contact Name
	 * 
	 * @return DOCUMENT ME!
	 */
	public String makeContactName() {
		long tm = System.currentTimeMillis();

		return new String("Con"
				+ String.valueOf(tm + rd.nextInt(5)).substring(7));
	}
	
	/**
	 * EPP Domain associated with test
	 */
	private EPPContact contact = null;

	/**
	 * EPP Session associated with test
	 */
	private EPPSession session = null;

	/**
	 * Current test iteration
	 */
	private int iteration = 0;

	/**
	 * Handle to the Singleton EPP Application instance (<code>EPPApplicationSingle</code>)
	 */
	private static EPPApplicationSingle app = EPPApplicationSingle
			.getInstance();

	/**
	 * Name of configuration file to use for test (default = epp.config).
	 */
	private static String configFileName = "epp.config";

	/**
	 * Random instance for the generation of unique objects (hosts, IP
	 * addresses, etc.).
	 */
	private Random rd = new Random(System.currentTimeMillis());

	/**
	 * Logging category
	 */
	private static final Logger cat = Logger.getLogger(EPPJobsContactTst.class
			.getName(), EPPCatFactory.getInstance().getFactory());

} // End class EPPJobsContactTest

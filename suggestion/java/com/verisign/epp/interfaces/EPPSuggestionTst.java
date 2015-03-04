/***********************************************************
 Copyright (C) 2004 VeriSign, Inc.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-0107  USA

 http://www.verisign.com/nds/naming/namestore/techdocs.html
 ***********************************************************/
package com.verisign.epp.interfaces;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.suggestion.EPPSuggestionAddress;
import com.verisign.epp.codec.suggestion.EPPSuggestionCoordinates;
import com.verisign.epp.codec.suggestion.EPPSuggestionFilter;
import com.verisign.epp.codec.suggestion.EPPSuggestionGeo;
import com.verisign.epp.codec.suggestion.EPPSuggestionInfoCmd;
import com.verisign.epp.codec.suggestion.EPPSuggestionInfoResp;
import com.verisign.epp.codec.suggestion.util.RandomHelper;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the <code>EPPSuggestion</code> class. The unit test will
 * initialize a session with an EPP Server, will invoke
 * <code>EPPSuggestion</code> operations, and will end a session with an EPP
 * Server. The configuration file used by the unit test defaults to epp.config,
 * but can be changed by passing the file path as the first command line
 * argument. The unit test can be run in multiple threads by setting the
 * "threads" system property. For example, the unit test can be run in 2 threads
 * with the configuration file ../../epp.config with the following command: <br>
 * <br>
 * java com.verisign.epp.interfaces.EPPSuggestionTst -Dthreads=2
 * ../../epp.config <br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5 </a> <br>
 * <br>
 * <br>
 * <br>
 * 
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPSuggestionTst extends TestCase {
	/**
	 * Handle to the Singleton EPP Application instance (
	 * <code>EPPApplicationSingle</code>)
	 */
	private static EPPApplicationSingle app = EPPApplicationSingle.getInstance();

	/**
	 * Logging category
	 */
	private static final Logger cat = Logger.getLogger(
			EPPSuggestionTst.class.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Name of configuration file to use for test (default = epp.config).
	 */
	private static String configFileName = "epp.config";

	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * <ul>
	 * <li>iterations Number of unit test iterations to run
	 * <li>validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be
	 * off.
	 * </ul>
	 */
	public static void main(String[] args) {
		// Override the default configuration file name?
		if (args.length > 0) {
			configFileName = args[0];
		}
		// Number of Threads
		int numThreads = 1;
		String threadsStr = System.getProperty("threads");
		if (threadsStr != null) {
			numThreads = Integer.parseInt(threadsStr);
		}
		// Run test suite in multiple threads?
		if (numThreads > 1) {
			// Spawn each thread passing in the Test Suite
			for (int i = 0; i < numThreads; i++) {
				TestThread thread = new TestThread("EPPSessionTst Thread " + i,
						EPPSuggestionTst.suite());
				thread.start();
			}
		} else {// Single threaded mode.
			junit.textui.TestRunner.run(EPPSuggestionTst.suite());
		}
		try {
			app.endApplication();
		} catch (EPPCommandException e) {
			e.printStackTrace();
			Assert.fail("Error ending the EPP Application: " + e);
		}
	}// End EPPSuggestionTst.main(String [])

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPSuggestionTst</code>.
	 */
	public static Test suite() {

		String theConfigFileName = System.getProperty("EPP.ConfigFile");

		if (theConfigFileName != null) {
			configFileName = theConfigFileName;
		}

		TestSuite suite = new TestSuite(EPPSuggestionTst.class);
		try {
			System.out.println(configFileName);
			app.initialize(configFileName);
		} catch (EPPCommandException e) {
			e.printStackTrace();
			Assert.fail("Error initializing the EPP Application: " + e);
		}
		return suite;
	}// End EPPSuggestionTst.suite()


	/**
	 * Current test iteration
	 */
	private int iteration = 0;

	/**
	 * EPP Session associated with test
	 */
	private EPPSession session = null;

	/**
	 * EPP Suggestion associated with test
	 */
	private EPPSuggestion suggestion = null;

	/**
	 * Allocates an <code>EPPSuggestionTst</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 * 
	 * @param name
	 *            Logical name of the test
	 */
	public EPPSuggestionTst(String name) {
		super(name);
	}

	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a
	 * server generated error or a general exception. If the exception was
	 * caused by a server error, "Server Error : <Response XML>" will be
	 * specified. If the exception was caused by a general algorithm error,
	 * "General Error : <Exception Description>" will be specified.
	 * 
	 * @param aException
	 *            Exception thrown during test
	 */
	public void handleException(Exception aException) {
		EPPResponse response = session.getResponse();
		aException.printStackTrace();
		// Is a server specified error?
		if ((response != null) && (!response.isSuccess())) {
			Assert.fail("Server Error : " + response);
		} else {
			Assert.fail("General Error : " + aException);
		}
	}// End EPPSuggestionTst.handleException(EPPCommandException)

	/**
	 * Unit test of <code>EPPSuggestion.sendInfo</code>.
	 */
	public void suggestionInfo() {
		printStart("suggestionInfo");
		try {
			// Test randomized command
			EPPSuggestionInfoCmd theRandomCmd = RandomHelper.getCommand();
			theRandomCmd.setKey("RANDOMIZE");
			theRandomCmd.setLanguage("POR");

			suggestion.setCommand(theRandomCmd);
			System.out.println("\nsuggestionInfoCmd: " + EPPUtil.toString(theRandomCmd));

			EPPSuggestionInfoResp response = suggestion.sendInfo();
			System.out.println("\nsuggestionInfoRsp: " + EPPUtil.toString(response));

			/**
			 * Result Set
			 */
			for (int i = 0; i < response.getResults().size(); i++) {
				EPPResult myResult = (EPPResult) response.getResults().elementAt(i);
				System.out.println("Result Code    : " + myResult.getCode());
				System.out.println("Result Message : " + myResult.getMessage());
				System.out.println("Result Lang    : " + myResult.getLang());
				if (myResult.isSuccess()) {
					System.out.println("Command Passed ");
				} else {
					System.out.println("Command Failed ");
				}
				if (myResult.getValues() != null) {
					for (int k = 0; k < myResult.getValues().size(); k++) {
						System.out.println("Result Values  : "
								+ myResult.getValues().elementAt(k));
					}
				}
			}

		} catch (EPPCommandException e) {
			handleException(e);
		} catch (EPPDecodeException e) {
			System.out.println("Internal error: dummy command is invalid");
		}

		printEnd("suggestionInfo");
	}

	/**
	 * Unit test of <code>EPPSuggestion.sendInfo using table view</code>.
	 */
	public void suggestionInfoTableView() {
		printStart("suggestionInfoTableView");
		try {
			// Test randomized command
			EPPSuggestionInfoCmd theRandomCmd = RandomHelper.getCommand();
			theRandomCmd.setKey("SOCCERTEAM.COM");
			theRandomCmd.setLanguage("ESP");
			theRandomCmd.unsetFilterId();

			/**
			 * TODO: The filter object is already set by the RandomHelper. This
			 * code should remove any existing filterid. Then if the filter
			 * exists, retrieve it and set the view to table. Otherwise, create
			 * a new filter with a table view.
			 */
			EPPSuggestionFilter theFilter = new EPPSuggestionFilter();
			theFilter.setTableView();
			theRandomCmd.setFilter(theFilter);

			suggestion.setCommand(theRandomCmd);
			System.out.println("\nsuggestionInfoCmd: " + EPPUtil.toString(theRandomCmd));

			EPPSuggestionInfoResp response = suggestion.sendInfo();
			System.out.println("\nsuggestionInfoRsp: " + EPPUtil.toString(response));

			/**
			 * Result Set
			 */
			for (int i = 0; i < response.getResults().size(); i++) {
				EPPResult myResult = (EPPResult) response.getResults().elementAt(i);
				System.out.println("Result Code    : " + myResult.getCode());
				System.out.println("Result Message : " + myResult.getMessage());
				System.out.println("Result Lang    : " + myResult.getLang());
				if (myResult.isSuccess()) {
					System.out.println("Command Passed ");
				} else {
					System.out.println("Command Failed ");
				}
				if (myResult.getValues() != null) {
					for (int k = 0; k < myResult.getValues().size(); k++) {
						System.out.println("Result Values  : "
								+ myResult.getValues().elementAt(k));
					}
				}
			}

		} catch (EPPCommandException e) {
			handleException(e);
		} catch (EPPDecodeException e) {
			System.out.println("Internal error: dummy command is invalid");
		}

		printEnd("suggestionInfoTableView");
	}
	
	/**
	 * Unit test of <code>EPPSuggestion.sendInfo using table view in French</code>.
	 */
	public void suggestionInfoTableViewFrench() {
		printStart("suggestionInfoTableViewFrench");
		try {
			// Test randomized command
			EPPSuggestionInfoCmd theRandomCmd = RandomHelper.getCommand();
			theRandomCmd.setKey("SOCCERTEAM.COM");
			theRandomCmd.setLanguage("FRE");
			theRandomCmd.unsetFilterId();

			/**
			 * TODO: The filter object is already set by the RandomHelper. This
			 * code should remove any existing filterid. Then if the filter
			 * exists, retrieve it and set the view to table. Otherwise, create
			 * a new filter with a table view.
			 */
			EPPSuggestionFilter theFilter = new EPPSuggestionFilter();
			theFilter.setTableView();
			theRandomCmd.setFilter(theFilter);

			suggestion.setCommand(theRandomCmd);
			System.out.println("\nsuggestionInfoCmd: " + EPPUtil.toString(theRandomCmd));

			EPPSuggestionInfoResp response = suggestion.sendInfo();
			System.out.println("\nsuggestionInfoRsp: " + EPPUtil.toString(response));

			/**
			 * Result Set
			 */
			for (int i = 0; i < response.getResults().size(); i++) {
				EPPResult myResult = (EPPResult) response.getResults().elementAt(i);
				System.out.println("Result Code    : " + myResult.getCode());
				System.out.println("Result Message : " + myResult.getMessage());
				System.out.println("Result Lang    : " + myResult.getLang());
				if (myResult.isSuccess()) {
					System.out.println("Command Passed ");
				} else {
					System.out.println("Command Failed ");
				}
				if (myResult.getValues() != null) {
					for (int k = 0; k < myResult.getValues().size(); k++) {
						System.out.println("Result Values  : "
								+ myResult.getValues().elementAt(k));
					}
				}
			}

		} catch (EPPCommandException e) {
			handleException(e);
		} catch (EPPDecodeException e) {
			System.out.println("Internal error: dummy command is invalid");
		}

		printEnd("suggestionInfoTableViewFrench");
	}

	/**
	 * Unit test of <code>EPPSuggestion.sendInfo using grid view</code>.
	 */
	public void suggestionInfoGridView() {
		printStart("suggestionInfoGridView");
		try {
			// Test randomized command
			EPPSuggestionInfoCmd theRandomCmd = RandomHelper.getCommand();
			theRandomCmd.setKey("SOCCERTEAM");
			theRandomCmd.setLanguage("GER");
			theRandomCmd.unsetFilterId();

			/**
			 * TODO: The filter object is already set by the RandomHelper. This
			 * code should remove any existing filterid. Then if the filter
			 * exists, retrieve it and set the view to grid. Otherwise, create a
			 * new filter with a grid view.
			 */
			EPPSuggestionFilter theFilter = new EPPSuggestionFilter();
			theFilter.setGridView();
			theRandomCmd.setFilter(theFilter);

			suggestion.setCommand(theRandomCmd);
			System.out.println("\nsuggestionInfoCmd: " + EPPUtil.toString(theRandomCmd));

			EPPSuggestionInfoResp response = suggestion.sendInfo();
			System.out.println("\nsuggestionInfoRsp: " + EPPUtil.toString(response));

			/**
			 * Result Set
			 */
			for (int i = 0; i < response.getResults().size(); i++) {
				EPPResult myResult = (EPPResult) response.getResults().elementAt(i);
				System.out.println("Result Code    : " + myResult.getCode());
				System.out.println("Result Message : " + myResult.getMessage());
				System.out.println("Result Lang    : " + myResult.getLang());
				if (myResult.isSuccess()) {
					System.out.println("Command Passed ");
				} else {
					System.out.println("Command Failed ");
				}
				if (myResult.getValues() != null) {
					for (int k = 0; k < myResult.getValues().size(); k++) {
						System.out.println("Result Values  : "
								+ myResult.getValues().elementAt(k));
					}
				}
			}

		} catch (EPPCommandException e) {
			handleException(e);
		} catch (EPPDecodeException e) {
			System.out.println("Internal error: dummy command is invalid");
		}

		printEnd("suggestionInfoGridView");
	}
	
	/**
	 * Unit test of <code>EPPSuggestion.sendInfo using geodata</code>.
	 */
	public void suggestionInfoGeoCoordinates() {
		printStart("suggestionInfoGridView");
		try {
			// Test randomized command
			EPPSuggestionInfoCmd theRandomCmd = RandomHelper.getCommand();
			theRandomCmd.setKey("SOCCERTEAM");
			theRandomCmd.setLanguage("GER");
			theRandomCmd.unsetFilterId();

			/**
			 * TODO: The filter object is already set by the RandomHelper. This
			 * code should remove any existing filterid. Then if the filter
			 * exists, retrieve it and set the view to grid. Otherwise, create a
			 * new filter with a grid view.
			 */
			EPPSuggestionFilter theFilter = new EPPSuggestionFilter();
			EPPSuggestionGeo geo = new EPPSuggestionGeo();
			geo.setCoordinates(new EPPSuggestionCoordinates(34.1234, -5.4321));
			theFilter.setGeo( geo );
			theFilter.setGridView();
			theRandomCmd.setFilter(theFilter);

			suggestion.setCommand(theRandomCmd);
			System.out.println("\nsuggestionInfoCmd: " + EPPUtil.toString(theRandomCmd));

			EPPSuggestionInfoResp response = suggestion.sendInfo();
			System.out.println("\nsuggestionInfoRsp: " + EPPUtil.toString(response));

			/**
			 * Result Set
			 */
			for (int i = 0; i < response.getResults().size(); i++) {
				EPPResult myResult = (EPPResult) response.getResults().elementAt(i);
				System.out.println("Result Code    : " + myResult.getCode());
				System.out.println("Result Message : " + myResult.getMessage());
				System.out.println("Result Lang    : " + myResult.getLang());
				if (myResult.isSuccess()) {
					System.out.println("Command Passed ");
				} else {
					System.out.println("Command Failed ");
				}
				if (myResult.getValues() != null) {
					for (int k = 0; k < myResult.getValues().size(); k++) {
						System.out.println("Result Values  : "
								+ myResult.getValues().elementAt(k));
					}
				}
			}

		} catch (EPPCommandException e) {
			handleException(e);
		} catch (EPPDecodeException e) {
			System.out.println("Internal error: dummy command is invalid");
		}

		printEnd("suggestionInfoGridView");
	}
	
	/**
	 * Unit test of <code>EPPSuggestion.sendInfo using geodata</code>.
	 */
	public void suggestionInfoGeoLocation() {
		printStart("suggestionInfoGridView");
		try {
			// Test randomized command
			EPPSuggestionInfoCmd theRandomCmd = RandomHelper.getCommand();
			theRandomCmd.setKey("SOCCERTEAM");
			theRandomCmd.setLanguage("GER");
			theRandomCmd.unsetFilterId();

			/**
			 * TODO: The filter object is already set by the RandomHelper. This
			 * code should remove any existing filterid. Then if the filter
			 * exists, retrieve it and set the view to grid. Otherwise, create a
			 * new filter with a grid view.
			 */
			EPPSuggestionFilter theFilter = new EPPSuggestionFilter();
			EPPSuggestionGeo geo = new EPPSuggestionGeo();
			geo.setAddr(new EPPSuggestionAddress("127.0.0.1"));
			theFilter.setGeo( geo );
			theFilter.setGridView();
			theRandomCmd.setFilter(theFilter);

			suggestion.setCommand(theRandomCmd);
			System.out.println("\nsuggestionInfoCmd: " + EPPUtil.toString(theRandomCmd));

			EPPSuggestionInfoResp response = suggestion.sendInfo();
			System.out.println("\nsuggestionInfoRsp: " + EPPUtil.toString(response));

			/**
			 * Result Set
			 */
			for (int i = 0; i < response.getResults().size(); i++) {
				EPPResult myResult = (EPPResult) response.getResults().elementAt(i);
				System.out.println("Result Code    : " + myResult.getCode());
				System.out.println("Result Message : " + myResult.getMessage());
				System.out.println("Result Lang    : " + myResult.getLang());
				if (myResult.isSuccess()) {
					System.out.println("Command Passed ");
				} else {
					System.out.println("Command Failed ");
				}
				if (myResult.getValues() != null) {
					for (int k = 0; k < myResult.getValues().size(); k++) {
						System.out.println("Result Values  : "
								+ myResult.getValues().elementAt(k));
					}
				}
			}

		} catch (EPPCommandException e) {
			handleException(e);
		} catch (EPPDecodeException e) {
			System.out.println("Internal error: dummy command is invalid");
		}

		printEnd("suggestionInfoGridView");
	}
	
	/**
	 * Unit test of <code>EPPSuggestion.sendInfo using geodata</code>.
	 */
	public void suggestionInfoGeoIpaddress() {
		printStart("suggestionInfoGeoIpaddress");
		try {
			// Test randomized command
			EPPSuggestionInfoCmd theRandomCmd = RandomHelper.getCommand();
			theRandomCmd.setKey("SOCCERTEAM");
			theRandomCmd.setLanguage("GER");
			theRandomCmd.unsetFilterId();

			/**
			 * TODO: The filter object is already set by the RandomHelper. This
			 * code should remove any existing filterid. Then if the filter
			 * exists, retrieve it and set the view to grid. Otherwise, create a
			 * new filter with a grid view.
			 */
			EPPSuggestionFilter theFilter = new EPPSuggestionFilter();
			EPPSuggestionGeo geo = new EPPSuggestionGeo();
			EPPSuggestionAddress addr = new EPPSuggestionAddress("127.0.0.1");
			geo.setAddr(addr);
			theFilter.setGeo( geo );
			theFilter.setGridView();
			theRandomCmd.setFilter(theFilter);

			suggestion.setCommand(theRandomCmd);
			System.out.println("\nsuggestionInfoCmd: " + EPPUtil.toString(theRandomCmd));

			EPPSuggestionInfoResp response = suggestion.sendInfo();
			System.out.println("\nsuggestionInfoRsp: " + EPPUtil.toString(response));

			/**
			 * Result Set
			 */
			for (int i = 0; i < response.getResults().size(); i++) {
				EPPResult myResult = (EPPResult) response.getResults().elementAt(i);
				System.out.println("Result Code    : " + myResult.getCode());
				System.out.println("Result Message : " + myResult.getMessage());
				System.out.println("Result Lang    : " + myResult.getLang());
				if (myResult.isSuccess()) {
					System.out.println("Command Passed ");
				} else {
					System.out.println("Command Failed ");
				}
				if (myResult.getValues() != null) {
					for (int k = 0; k < myResult.getValues().size(); k++) {
						System.out.println("Result Values  : "
								+ myResult.getValues().elementAt(k));
					}
				}
			}

		} catch (EPPCommandException e) {
			handleException(e);
		} catch (EPPDecodeException e) {
			System.out.println("Internal error: dummy command is invalid");
		}

		printEnd("suggestionInfoGeoIpaddress");
	}

	/**
	 * JUNIT test method to implement the <code>EPPSuggestionTst TestCase</code>.
	 * Each sub-test will be invoked in order to satisfy testing the
	 * EPPSuggestion interface.
	 */
	public void testSuggestion() {
		int numIterations = 1;
		String iterationsStr = System.getProperty("iterations");
		if (iterationsStr != null) {
			numIterations = Integer.parseInt(iterationsStr);
		}
		for (iteration = 0; (numIterations == 0) || (iteration < numIterations); iteration++) {
			printStart("Test Suite");
			// suggestionInfo();
			suggestionInfoTableView();
			suggestionInfoTableViewFrench();
			suggestionInfoGridView();
			suggestionInfoGeoCoordinates();
			suggestionInfoGeoLocation();
			suggestionInfoGeoIpaddress();
			printEnd("Test Suite");
		}
	}

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
		} catch (EPPCommandException e) {
			EPPResponse response = session.getResponse();
			// Is a server specified error?
			if ((response != null) && (!response.isSuccess())) {
				Assert.fail("Server Error : " + response);
			} else// Other error
			{
				e.printStackTrace();
				Assert.fail("initSession Error : " + e);
			}
		}
		printEnd("endSession");
	}// End EPPSuggestionTst.endSession()

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
		} catch (EPPCommandException e) {
			EPPResponse response = session.getResponse();
			// Is a server specified error?
			if ((response != null) && (!response.isSuccess())) {
				Assert.fail("Server Error : " + response);
			} else {
				e.printStackTrace();
				Assert.fail("initSession Error : " + e);
			}
		}
		printEnd("initSession");
	}// End EPPSuggestionTst.initSession()

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
			cat.info(Thread.currentThread().getName() + ", iteration " + iteration
					+ ": " + aTest + " End");
		}
		System.out.println("End of " + aTest);
		System.out.println("\n");
	}// End EPPSuggestionTst.testEnd(String)


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
			cat.info(Thread.currentThread().getName() + ", iteration " + iteration
					+ ": " + aTest + " Start");
		}
		System.out.println("Start of " + aTest);
		System.out
				.println("****************************************************************\n");
	}// End EPPSuggestionTst.testStart(String)

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar".
	 */
	protected void setUp() {
		try {
			String theSessionClassName = System.getProperty("EPP.SessionClass");

			if (theSessionClassName != null) {
				try {
					Class<?> theSessionClass = Class.forName(theSessionClassName);

					if (!EPPSession.class.isAssignableFrom((theSessionClass))) {
						Assert.fail(theSessionClassName
								+ " is not a subclass of EPPSession");
					}

					session = (EPPSession) theSessionClass.newInstance();
				} catch (Exception ex) {
					Assert.fail("Exception instantiating EPP.SessionClass value "
							+ theSessionClassName + ": " + ex);
				}
			} else {
				session = new EPPSession();
			}

			session.setClientID(Environment.getProperty("EPP.Test.clientId", "ClientX"));
			session.setPassword(Environment.getProperty("EPP.Test.password", "foo-BAR2"));
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Error initializing the session: " + e);
		}
		initSession();
		// System.out.println("out init");
		suggestion = new EPPSuggestion(session);
	}// End EPPSuggestionTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	}// End EPPSuggestionTst.tearDown();
}// End class EPPSuggestionTst

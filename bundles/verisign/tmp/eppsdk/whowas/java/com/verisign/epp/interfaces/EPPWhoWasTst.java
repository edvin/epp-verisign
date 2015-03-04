/***********************************************************
 Copyright (C) 2010 VeriSign, Inc.

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

import java.util.List;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.whowas.EPPWhoWasConstants;
import com.verisign.epp.codec.whowas.EPPWhoWasHistory;
import com.verisign.epp.codec.whowas.EPPWhoWasInfoResp;
import com.verisign.epp.codec.whowas.EPPWhoWasRecord;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the {@link EPPWhoWas} class. The unit test will initialize
 * a session with an EPP Server, will invoke {@link EPPWhoWas} operations, and
 * will end a session with an EPP Server. The configuration file used by the
 * unit test defaults to epp.config, but can be changed by passing the file path
 * as the first command line argument. The unit test can be run in multiple
 * threads by setting the "threads" system property. For example, the unit test
 * can be run in 2 threads with the configuration file ../../epp.config with the
 * following command: <br>
 * <br>
 * java com.verisign.epp.interfaces.EPPWhoWasTst -Dthreads=2 ../../epp.config <br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5 </a> <br>
 * <br>
 * <br>
 * <br>
 * 
 * @author Deepak Deshpande
 * @version 1.0
 */
public class EPPWhoWasTst extends TestCase {

	/**
	 * Handle to the Singleton EPP Application instance (
	 * {@link EPPApplicationSingle})
	 */
	private static EPPApplicationSingle app = EPPApplicationSingle.getInstance();

	/** Constant for client transaction id. */
	private static String TRANSACTION_ID = "ABC-12345";

	private static final String NON_EXISTING_TEST_NAME = "non-existing.com";

	private static final String NON_EXISTING_TEST_ROID = "non-existing";

	/**
	 * Logging category
	 */
	private static final Logger cat =
			Logger.getLogger( EPPWhoWasTst.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );

	/**
	 * Name of configuration file to use for test (default = epp.config).
	 */
	private static String configFileName = "epp.config";

	/**
	 * Current test iteration
	 */
	private int iteration = 0;

	/**
	 * {@link EPPSession} associated with test
	 */
	private EPPSession session = null;

	/**
	 * {@link EPPWhoWas} associated with test
	 */
	private EPPWhoWas whowas = null;


	/**
	 * Allocates an {@link EPPWhoWasTst} with a logical name. The constructor will
	 * initialize the base class {@link TestCase} with the logical name.
	 * 
	 * @param aName
	 *        Logical name of the test
	 */
	public EPPWhoWasTst ( String aName ) {
		super( aName );
	}


	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * <ul>
	 * <li>iterations Number of unit test iterations to run
	 * <li>validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be off.
	 * </ul>
	 */
	public static void main ( String[] args ) {

		// Override the default configuration file name?
		if ( args.length > 0 ) {
			configFileName = args[ 0 ];
		}
		// Number of Threads
		int numThreads = 1;
		String threadsStr = System.getProperty( "threads" );
		if ( threadsStr != null ) {
			numThreads = Integer.parseInt( threadsStr );
		}
		// Run test suite in multiple threads?
		if ( numThreads > 1 ) {
			// Spawn each thread passing in the Test Suite
			for ( int i = 0; i < numThreads; i++ ) {
				TestThread thread =
						new TestThread( "EPPWhoWasTst Thread " + i, EPPWhoWasTst.suite() );
				thread.start();
			}
		}
		else {// Single threaded mode.
			junit.textui.TestRunner.run( EPPWhoWasTst.suite() );
		}

		try {
			app.endApplication();
		}
		catch ( EPPCommandException e ) {
			e.printStackTrace();
			Assert.fail( "Error ending the EPP Application: " + e );
		}
	}


	/**
	 * JUNIT <code>suite</code> static method, which returns the tests associated
	 * with {@link EPPWhoWasTst}.
	 */
	public static Test suite () {

		String theConfigFileName = System.getProperty( "EPP.ConfigFile" );

		if ( theConfigFileName != null ) {
			configFileName = theConfigFileName;
		}

		TestSuite suite = new TestSuite( EPPWhoWasTst.class );
		try {
			System.out.println( configFileName );
			app.initialize( configFileName );
		}
		catch ( EPPCommandException e ) {
			e.printStackTrace();
			Assert.fail( "Error initializing the EPP Application: " + e );
		}
		return suite;
	}


	/**
	 * Handle an {@link EPPCommandException}, which can be either a server
	 * generated error or a general exception. If the exception was caused by a
	 * server error, "Server Error : <Response XML>" will be specified. If the
	 * exception was caused by a general algorithm error,
	 * "General Error : <Exception Description>" will be specified.
	 * 
	 * @param aException
	 *        Exception thrown during test
	 */
	public void handleException ( Exception aException ) {
		EPPResponse response = this.session.getResponse();
		aException.printStackTrace();
		// Is a server specified error?
		if ( (response != null) && (!response.isSuccess()) ) {
			Assert.fail( "Server Error : " + response );
		}
		else {
			Assert.fail( "General Error : " + aException );
		}
	}


	/**
	 * Runs positive test cases for {@link EPPWhoWas}.
	 */
	public void testWhoWasInfo () {

		int numIterations = 1;

		String iterationsStr = System.getProperty( "iterations" );
		if ( iterationsStr != null ) {
			numIterations = Integer.parseInt( iterationsStr );
		}

		try {
			for ( this.iteration = 0; (numIterations == 0)
					|| (this.iteration < numIterations); this.iteration++ ) {
				printStart( "EPPWhoWasTst testWhoWasInfo" );
				whowasInfoByName();
				whowasInfoByRoid();
				printEnd( "EPPWhoWasTst testWhoWasInfo" );
			}
		}
		catch ( EPPCommandException eX ) {
			handleException( eX );
		}
	}


	/**
	 * Runs negative test cases for {@link EPPWhoWas}.
	 */
	public void testNullNameAndRoidWhoWasInfo () {
		int numIterations = 1;
		String iterationsStr = System.getProperty( "iterations" );
		if ( iterationsStr != null ) {
			numIterations = Integer.parseInt( iterationsStr );
		}

		for ( this.iteration = 0; (numIterations == 0)
				|| (this.iteration < numIterations); this.iteration++ ) {
			printStart( "EPPWhoWasTst testNegativeWhoWasInfo" );

			try {
				sendInfoAndAssertResponse( null, null, null );
				fail( "EPPCommandException should have been received" );
			}
			catch ( EPPCommandException eX ) {
				assertEquals( EPPCommandException.class, eX.getClass() );
			}

			printEnd( "EPPWhoWasTst testNegativeWhoWasInfo" );
		}
	}


	/**
	 * Runs non existing entity negative test case for {@link EPPWhoWas}.
	 */
	public void testNonExistingEntityWhoWasInfo () {
		int numIterations = 1;
		String iterationsStr = System.getProperty( "iterations" );
		if ( iterationsStr != null ) {
			numIterations = Integer.parseInt( iterationsStr );
		}

		for ( this.iteration = 0; (numIterations == 0)
				|| (this.iteration < numIterations); this.iteration++ ) {
			printStart( "EPPWhoWasTst testNonExistingEntityWhoWasInfo" );

			try {
				sendInfoAndAssertResponse( EPPWhoWasConstants.TYPE_DOMAIN,
						NON_EXISTING_TEST_NAME, null );
				fail( "EPPCommandException should have been received" );
			}
			catch ( EPPCommandException eX ) {

				EPPResponse response = eX.getResponse();

				assertEquals( "Response should be " + EPPResult.OBJECT_DOES_NOT_EXIST,
						EPPResult.OBJECT_DOES_NOT_EXIST, response.getResult().getCode() );

				System.out.println( "The WhoWas Response for Non Existing object is [ "
						+ response + "]" );
			}

			try {
				sendInfoAndAssertResponse( EPPWhoWasConstants.TYPE_DOMAIN, null,
						NON_EXISTING_TEST_ROID );
				fail( "EPPCommandException should have been received" );
			}
			catch ( EPPCommandException eX ) {

				EPPResponse response = eX.getResponse();

				assertEquals( "Response should be " + EPPResult.OBJECT_DOES_NOT_EXIST,
						EPPResult.OBJECT_DOES_NOT_EXIST, response.getResult().getCode() );

				System.out.println( "The WhoWas Response for Non Existing object is [ "
						+ response + "]" );
			}

			printEnd( "EPPWhoWasTst testNonExistingEntityWhoWasInfo" );
		}
	}


	/**
	 * Unit test of <code>EPPWhoWas.sendInfo</code> by name.
	 */
	public void whowasInfoByName () throws EPPCommandException {
		printStart( "whowasInfoByName" );

		// test case to get create operation history
		sendInfoAndAssertResponse( EPPWhoWasConstants.TYPE_DOMAIN,
				"create-operation-history.com", null );

		// test case to get all operation history
		sendInfoAndAssertResponse( EPPWhoWasConstants.TYPE_DOMAIN,
				"all-operation-history.com", null );

		// test case to set type as null instead of domain
		sendInfoAndAssertResponse( null, "create-operation-history.com", null );

		// test case to get default operation history
		sendInfoAndAssertResponse( EPPWhoWasConstants.TYPE_DOMAIN,
				"default-history.com", null );

		printEnd( "whowasInfoByName" );
	}


	/**
	 * Unit test of <code>EPPWhoWas.sendInfo</code> by roid.
	 */
	public void whowasInfoByRoid () throws EPPCommandException {
		printStart( "whowasInfoByRoid" );

		// test case to get create operation history
		sendInfoAndAssertResponse( EPPWhoWasConstants.TYPE_DOMAIN, null,
				"create-rep" );

		// test case to get all operation history
		sendInfoAndAssertResponse( EPPWhoWasConstants.TYPE_DOMAIN, null, "all-rep" );

		// case to set type as null instead of domain
		sendInfoAndAssertResponse( null, null, "create-rep" );

		// test case to get default operation history
		sendInfoAndAssertResponse( EPPWhoWasConstants.TYPE_DOMAIN, null,
				"default-history" );

		printEnd( "whowasInfoByRoid" );
	}


	/**
	 * Creates {@link EPPWhoWas} from the passed in parameters and invokes
	 * <code>sendInfo</code> method. Asserts attributes from response received
	 * response from the server.
	 * 
	 * @param aType
	 *        - The type to set in info command.
	 * @param aName
	 *        - The name to set in info command.
	 * @param aRoid
	 *        - The roid to set in info command.
	 * @throws EPPCommandException
	 *         - Exception received from the server.
	 */
	private void sendInfoAndAssertResponse ( String aType, String aName,
			String aRoid ) throws EPPCommandException {

		this.whowas.setTransId( TRANSACTION_ID );
		if ( aType != null ) {
			this.whowas.setType( aType );
		}
		this.whowas.setName( aName );
		this.whowas.setRoid( aRoid );

		System.out.println( "\n\nSending InfoCmd to get history by "
				+ (aName != null ? "name: " + aName : "")
				+ (aRoid != null ? "roid: " + aRoid : "") );

		EPPWhoWasInfoResp response = this.whowas.sendInfo();

		System.out.println( "The WhoWas Response is [ " + response + "]" );

		/**
		 * Result Set
		 */
		for ( int i = 0; i < response.getResults().size(); i++ ) {
			EPPResult myResult = (EPPResult) response.getResults().elementAt( i );
			System.out.println( "Result Code    : " + myResult.getCode() );
			System.out.println( "Result Message : " + myResult.getMessage() );
			System.out.println( "Result Lang    : " + myResult.getLang() );

			if ( myResult.isSuccess() ) {
				System.out.println( "Command Passed " );
			}
			else {
				System.out.println( "Command Failed " );
			}

			if ( myResult.getValues() != null ) {
				for ( int k = 0; k < myResult.getValues().size(); k++ ) {
					System.out.println( "Result Values  : "
							+ myResult.getValues().elementAt( k ) );
				}
			}
		}

		if ( response.getResult().isSuccess() ) {

			if ( aType != null ) {
				assertEquals( "Type should match", aType, response.getWhowasType() );
			}
			else {
				assertEquals(
						"If type is NOT set, then type should be domain by default",
						EPPWhoWasConstants.TYPE_DOMAIN, response.getWhowasType() );
			}

			assertEquals( "Name should match", aName, response.getName() );

			assertEquals( "Roid should match", aRoid, response.getRoid() );

			assertWhoWasHistory( response.getHistory() );
		}
	}


	/**
	 * Gets list of records from passed <code>aEPPWhoWasHistory</code> and loops
	 * through the list asserts attributes of each {@link EPPWhoWasRecord} and
	 * prints them on the console.
	 * 
	 * @param aEPPWhoWasHistory
	 */
	private void assertWhoWasHistory ( EPPWhoWasHistory aEPPWhoWasHistory ) {

		assertNotNull( "History should be not null", aEPPWhoWasHistory );

		List historyRecords = aEPPWhoWasHistory.getRecords();

		assertTrue( "History should have more than 0 records", (historyRecords
				.size() > 0) );

		for ( int i = 0; i < historyRecords.size(); i++ ) {

			EPPWhoWasRecord historyRecord = (EPPWhoWasRecord) historyRecords.get( i );

			assertNotNull( "Name shoudn't be null", historyRecord.getName() );
			assertNotNull( "Roid shoudn't be null", historyRecord.getRoid() );
			assertNotNull( "Operation shoudn't be null", historyRecord.getOperation() );
			assertNotNull( "Transaction Date shoudn't be null", historyRecord
					.getTransactionDate() );
			assertNotNull( "Client ID shoudn't be null", historyRecord.getClientID() );
			assertNotNull( "Client Name shoudn't be null", historyRecord
					.getClientName() );

			printStart( "printing attributes of record [" + i + "]" );
			System.out
					.println( "\nRecord Name[" + i + "]:" + historyRecord.getName() );
			System.out.println( "Record Roid[" + i + "]:" + historyRecord.getRoid() );
			System.out.println( "Record New Name[" + i + "]:"
					+ historyRecord.getNewName() );
			System.out.println( "Record Operation[" + i + "]:"
					+ historyRecord.getOperation() );
			System.out.println( "Record Transaction Date[" + i + "]:"
					+ historyRecord.getTransactionDate() );
			System.out.println( "Record Client ID[" + i + "]:"
					+ historyRecord.getClientID() );
			System.out.println( "Record Client Name[" + i + "]:"
					+ historyRecord.getClientName() );
			printEnd( "printing attributes of record [" + i + "]" );
		}
	}


	/**
	 * Unit test of <code>EPPSession.endSession</code>. The session with the EPP
	 * Server will be terminated.
	 */
	private void endSession () {
		printStart( "endSession" );
		this.session.setTransId( "ABC-12345-XYZ" );
		// End the session
		try {
			this.session.endSession();
		}
		catch ( EPPCommandException e ) {
			EPPResponse response = this.session.getResponse();
			// Is a server specified error?
			if ( (response != null) && (!response.isSuccess()) ) {
				Assert.fail( "Server Error : " + response );
			}
			else// Other error
			{
				e.printStackTrace();
				Assert.fail( "initSession Error : " + e );
			}
		}
		printEnd( "endSession" );
	}


	/**
	 * Unit test of <code>EPPSession.initSession</code>. The session attribute is
	 * initialized with the attributes defined in the EPP sample files.
	 */
	private void initSession () {
		printStart( "initSession" );
		// Set attributes for initSession
		this.session.setTransId( "ABC-12345-XYZ" );
		this.session.setVersion( "1.0" );
		this.session.setLang( "en" );

		// Initialize the session
		try {
			this.session.initSession();
		}
		catch ( EPPCommandException e ) {
			EPPResponse response = this.session.getResponse();
			// Is a server specified error?
			if ( (response != null) && (!response.isSuccess()) ) {
				Assert.fail( "Server Error : " + response );
			}
			else {
				e.printStackTrace();
				Assert.fail( "initSession Error : " + e );
			}
		}
		printEnd( "initSession" );
	}


	/**
	 * Print the end of a test with the <code>Thread</code> name if the current
	 * thread is a <code>TestThread</code>.
	 * 
	 * @param Logical
	 *        name for the test
	 */
	private void printEnd ( String aTest ) {
		System.out
				.println( "****************************************************************" );
		if ( Thread.currentThread() instanceof TestThread ) {
			System.out.print( Thread.currentThread().getName() + ", iteration "
					+ this.iteration + ": " );
			cat.info( Thread.currentThread().getName() + ", iteration "
					+ this.iteration + ": " + aTest + " End" );
		}
		System.out.println( "End of " + aTest );
		System.out.println( "\n" );
	}


	/**
	 * Print the start of a test with the <code>Thread</code> name if the current
	 * thread is a <code>TestThread</code>.
	 * 
	 * @param Logical
	 *        name for the test
	 */
	private void printStart ( String aTest ) {
		if ( Thread.currentThread() instanceof TestThread ) {
			System.out.print( Thread.currentThread().getName() + ", iteration "
					+ this.iteration + ": " );
			cat.info( Thread.currentThread().getName() + ", iteration "
					+ this.iteration + ": " + aTest + " Start" );
		}
		System.out.println( "Start of " + aTest );
		System.out
				.println( "****************************************************************\n" );
	}


	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar".
	 */
	protected void setUp () {
		try {
			String theSessionClassName = System.getProperty( "EPP.SessionClass" );

			if ( theSessionClassName != null ) {
				try {
					Class theSessionClass = Class.forName( theSessionClassName );

					if ( !EPPSession.class.isAssignableFrom( (theSessionClass) ) ) {
						Assert.fail( theSessionClassName
								+ " is not a subclass of EPPSession" );
					}

					this.session = (EPPSession) theSessionClass.newInstance();
				}
				catch ( Exception ex ) {
					Assert.fail( "Exception instantiating EPP.SessionClass value "
							+ theSessionClassName + ": " + ex );
				}
			}
			else {
				this.session = new EPPSession();
			}

			this.session.setClientID( Environment.getProperty( "EPP.Test.clientId",
					"ClientX" ) );
			this.session.setPassword( Environment.getProperty( "EPP.Test.password",
					"foo-BAR2" ) );

		}
		catch ( Exception e ) {
			e.printStackTrace();
			Assert.fail( "Error initializing the session: " + e );
		}
		initSession();

		this.whowas = new EPPWhoWas( this.session );
	}


	/**
	 * JUNIT <code>tearDown</code>, invokes <code>endSession</code>.
	 */
	protected void tearDown () {
		endSession();
	}
}

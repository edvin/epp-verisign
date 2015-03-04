/**************************************************************************
 *                                                                        *
 * The information in this document is proprietary to VeriSign, Inc.      *
 * It may not be used, reproduced or disclosed without the written        *
 * approval of VeriSign.                                                  *
 *                                                                        *
 * VERISIGN PROPRIETARY & CONFIDENTIAL INFORMATION                        *
 *                                                                        *
 *                                                                        *
 * Copyright (c) 2011 VeriSign, Inc.  All rights reserved.                *
 *                                                                        *
 *************************************************************************/

package com.verisign.epp.interfaces;

import java.util.Iterator;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.coaext.EPPCoaExtAttr;
import com.verisign.epp.codec.coaext.EPPCoaExtCreate;
import com.verisign.epp.codec.coaext.EPPCoaExtInfData;
import com.verisign.epp.codec.coaext.EPPCoaExtKey;
import com.verisign.epp.codec.coaext.EPPCoaExtUpdate;
import com.verisign.epp.codec.coaext.EPPCoaExtValue;
import com.verisign.epp.codec.domain.EPPDomainCreateResp;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the using the Client Object Attribute classes with the
 * <code>EPPDomain</code> class. The unit test will initialize a session with an
 * EPP Server, will invoke <code>EPPDomain</code> operations with Client Object
 * Attribute Extensions, and will end a session with an EPP Server. The
 * configuration file used by the unit test defaults to epp.config, but can be
 * changed by passing the file path as the first command line argument. The unit
 * test can be run in multiple threads by setting the "threads" system property.
 * For example, the unit test can be run in 2 threads with the configuration
 * file ../../epp.config with the following command: <br>
 * <br>
 * java com.verisign.epp.interfaces.EPPPersRegTst -Dthreads=2 ../../epp.config <br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a>
 */

public class EPPCoaDomainTst extends TestCase {

	/**
	 * Allocates an <code>EPPCoaDomainTst</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with the
	 * logical name.
	 * 
	 * @param aName
	 *        Logical name of the test
	 */
	public EPPCoaDomainTst ( String aName ) {

		super( aName );

	} // End EPPCoaDomainTst(String)


	/**
	 * JUNIT test method to implement the <code>EPPCoaDomainTst TestCase</code>.
	 * Each sub-test will be invoked in order to satisfy testing the EPPDomain
	 * interface.
	 */
	public void testCoa () {

		int numIterations = 1;

		String iterationsStr = System.getProperty( "iterations" );

		if ( iterationsStr != null ) {

			numIterations = Integer.parseInt( iterationsStr );

		}

		for ( iteration = 0; (numIterations == 0) || (iteration < numIterations); iteration++ ) {

			printStart( "Test Suite" );

			coaCreate();
			coaFullInfoOwnedDomain();
			coaFullInfoNotOwnedDomain();
			coaPartialInfo();
			coaUpdatePut();
			coaUpdateRem();

			printEnd( "Test Suite" );

		}

	} // End EPPCoaDomainTst.testCoa()


	/**
	 * Unit test of using the Client Object Attribute Extension with
	 * <code>EPPDomain</code> Create .
	 */

	public void coaCreate () {

		printStart( "coaCreate" );

		// Try Successful Create

		try {

			EPPDomainCreateResp domainResponse;

			System.out.println( "coaCreate: Domain Create" );

			domain.setTransId( "ABC-12345-XYZ" );

			domain.addDomainName( this.makeDomainName() );

			domain.setAuthString( "ClientX" );

			EPPCoaExtKey key = new EPPCoaExtKey( "KEY1" );
			EPPCoaExtValue value = new EPPCoaExtValue( "value1" );
			EPPCoaExtAttr attr = new EPPCoaExtAttr();
			attr.setKey( key );
			attr.setValue( value );
			EPPCoaExtCreate create = new EPPCoaExtCreate();
			create.appendAttr( attr );

			domain.addExtension( create );

			domainResponse = domain.sendCreate();

			// -- Output all of the domainResponse attributes

			System.out.println( "coaCreate: Response = [" + domainResponse + "]\n\n" );

		}

		catch ( EPPCommandException e ) {

			handleException( e );

		}

		printEnd( "coaCreate" );

	} // End EPPCoaDomainTst.coaCreate()


	public void coaUpdateRem () {

		printStart( "coaUpdateRem" );

		// Try Successful Update

		try {

			EPPResponse domainResponse;

			System.out.println( "coaUpdateRem: Domain Update" );

			domain.setTransId( "ABC-12345-XYZ" );

			domain.addDomainName( this.makeDomainName() );

			domain.setAuthString( "ClientX" );

			// Set consent identifier

			EPPCoaExtUpdate updateRem = new EPPCoaExtUpdate();
			EPPCoaExtKey key = new EPPCoaExtKey( "KEY1" );

			updateRem.appendRemAttr( key );

			domain.addExtension( updateRem );

			domainResponse = domain.sendUpdate();

			// -- Output all of the domainResponse attributes

			System.out.println( "coaUpdateRem: Response = [" + domainResponse
					+ "]\n\n" );

			// -- Output extension attribute(s)

			if ( domainResponse.hasExtension( EPPCoaExtUpdate.class ) ) {

				Assert.fail( "Unexpected EPPCoaExtUpdate returned in response." );
			}

			else {

				System.out
						.println( "coaUpdateRem: Response has no extension, which is correct." );

			}

		}

		catch ( EPPCommandException e ) {

			handleException( e );

		}

		printEnd( "coaUpdateRem" );

	} // End EPPCoaDomainTst.coaUpdateRem()


	/**
	 * Unit test of adding COA with <code>EPPDomain</code> Update .
	 */
	public void coaUpdatePut () {

		printStart( "coaUpdateAdd" );

		// Try Successful Update

		try {

			EPPResponse domainResponse;

			System.out.println( "coaUpdateAdd: Domain Update" );

			domain.setTransId( "ABC-12345-XYZ" );

			domain.addDomainName( this.makeDomainName() );

			domain.setAuthString( "ClientX" );

			// Set consent identifier

			EPPCoaExtUpdate updateAdd = new EPPCoaExtUpdate();
			EPPCoaExtAttr attr = new EPPCoaExtAttr();
			EPPCoaExtKey key = new EPPCoaExtKey( "KEY1" );
			EPPCoaExtValue value = new EPPCoaExtValue( "value1" );
			attr.setKey( key );
			attr.setValue( value );
			updateAdd.appendPutAttr( attr );

			domain.addExtension( updateAdd );

			domainResponse = domain.sendUpdate();

			// -- Output all of the domainResponse attributes

			System.out.println( "coaUpdateAdd: Response = [" + domainResponse
					+ "]\n\n" );

			// -- Output extension attribute(s)

			if ( domainResponse.hasExtension( EPPCoaExtCreate.class ) ) {
				Assert.fail( "Coa Create extension included in create response." );
			}
			else {
				System.out.println( "coaUpdateAdd: Response has no extension" );
			}

		}

		catch ( EPPCommandException e ) {

			handleException( e );

		}

		printEnd( "coaUpdateAdd" );

	} // End EPPCoaDomainTst.coaUpdateAdd()


	/**
	 * Unit test of <code>EPPDomain</code> Info on an owned domain - this should
	 * return COA data.
	 */
	public void coaFullInfoOwnedDomain () {
		printStart( "EPPCoaDomainTst coaFullInfoOwnedDomain" );

		EPPDomainInfoResp response;

		try {
			// -- Domain info of key-data-interface.com
			System.out
					.println( "\ntestDomainInfo: Domain info of full-info-owned.com" );

			domain.addDomainName( "coa-full-info-owned.com" );

			response = domain.sendInfo();

			// -- Output all of the response attributes
			System.out.println( "testDomainInfo: Response = [" + response + "]\n\n" );

			// -- Output the coa:infData extension
			if ( response.hasExtension( EPPCoaExtInfData.class ) ) {
				EPPCoaExtInfData coaInfData =
						(EPPCoaExtInfData) response.getExtension( EPPCoaExtInfData.class );

				Assert.assertEquals( 1, coaInfData.getAttrs().size() );
				for ( Iterator iterator = coaInfData.getAttrs().iterator(); iterator
						.hasNext(); ) {
					Object attrObject = iterator.next();
					if ( attrObject != null ) {
						EPPCoaExtAttr attr = (EPPCoaExtAttr) attrObject;
						Assert.assertNotNull( attr.getKey() );
						Assert.assertNotNull( attr.getValue() );
					}
				}

			}
			else {
				Assert
						.fail( "EPPcoaExtInfData extension not included for domain info of full-info-owned.com" );
			}

		}
		catch ( EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "EPPSecDNSDomainTst coaFullInfoOwnedDomain" );
	}


	/**
	 * Unit test of <code>EPPDomain</code> Info on a domain owned by a different
	 * registrar - This should NOT return COA data.
	 */
	public void coaFullInfoNotOwnedDomain () {
		printStart( "EPPCoaDomainTst coaFullInfoNotOwnedDomain" );

		EPPDomainInfoResp response;

		try {
			// -- Domain info of key-data-interface.com
			System.out
					.println( "\ntestDomainInfo: Domain info of full-info-not-owned.com" );

			domain.addDomainName( "full-info-not-owned.com" );

			response = domain.sendInfo();

			// -- Output all of the response attributes
			System.out.println( "testDomainInfo: Response = [" + response + "]\n\n" );

			// -- Output the coa:infData extension
			if ( response.hasExtension( EPPCoaExtInfData.class ) ) {
				Assert
						.fail( "EPPCoaExtInfData extension should not be included for domain info of full-info-not-owned.com" );

			}

		}
		catch ( EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "EPPSecDNSDomainTst coaFullInfoNotOwnedDomain" );
	}


	/**
	 * Unit test of <code>EPPDomain</code> Info returning partial info results. -
	 * This should NOT return COA data.
	 */
	public void coaPartialInfo () {
		printStart( "EPPCoaDomainTst coaPartialInfo" );

		EPPDomainInfoResp response;

		try {
			// -- Domain info of key-data-interface.com
			System.out.println( "\ntestDomainInfo: Domain info of partial-info.com" );

			domain.addDomainName( "partial-info.com" );

			response = domain.sendInfo();

			// -- Output all of the response attributes
			System.out.println( "testDomainInfo: Response = [" + response + "]\n\n" );

			// -- Output the coa:infData extension
			if ( response.hasExtension( EPPCoaExtInfData.class ) ) {
				Assert
						.fail( "EPPCoaExtInfData extension should not be included for domain info of partial-info.com" );
			}
		}
		catch ( EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "EPPSecDNSDomainTst coaPartialInfo" );
	}


	/**
	 * Unit test of <code>EPPSession.initSession</code>. The session attribute is
	 * initialized with the attributes defined in the EPP sample files.
	 */

	private void initSession () {

		printStart( "initSession" );

		// Set attributes for initSession

		session.setTransId( "ABC-12345-XYZ" );

		session.setVersion( "1.0" );

		session.setLang( "en" );

		// Initialize the session

		try {

			session.initSession();

		}

		catch ( EPPCommandException e ) {

			EPPResponse domainResponse = session.getResponse();

			// Is a server specified error?

			if ( (domainResponse != null) && (!domainResponse.isSuccess()) ) {

				Assert.fail( "Server Error : " + domainResponse );

			}
			else {

				e.printStackTrace();

				Assert.fail( "initSession Error : " + e );

			}

		}

		printEnd( "initSession" );

	} // End EPPCoaDomainTst.initSession()


	/**
	 * Unit test of <code>EPPSession.endSession</code>. The session with the EPP
	 * Server will be terminated.
	 */

	private void endSession () {

		printStart( "endSession" );

		session.setTransId( "ABC-12345-XYZ" );

		// End the session

		try {

			session.endSession();

		}

		catch ( EPPCommandException e ) {

			EPPResponse domainResponse = session.getResponse();

			// Is a server specified error?

			if ( (domainResponse != null) && (!domainResponse.isSuccess()) ) {

				Assert.fail( "Server Error : " + domainResponse );

			}

			else // Other error

			{

				e.printStackTrace();

				Assert.fail( "initSession Error : " + e );

			}

		}

		printEnd( "endSession" );

	} // End EPPCoaDomainTst.endSession()


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

					session = (EPPSession) theSessionClass.newInstance();
				}
				catch ( Exception ex ) {
					Assert.fail( "Exception instantiating EPP.SessionClass value "
							+ theSessionClassName + ": " + ex );
				}
			}
			else {
				session = new EPPSession();
			}

			session.setClientID( Environment.getProperty( "EPP.Test.clientId",
					"ClientX" ) );
			session.setPassword( Environment.getProperty( "EPP.Test.password",
					"foo-BAR2" ) );

		}

		catch ( Exception e ) {

			e.printStackTrace();

			Assert.fail( "Error initializing the session: " + e );

		}

		initSession();

		domain = new EPPDomain( session );

	} // End EPPCoaDomainTst.setUp();


	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */

	protected void tearDown () {

		endSession();

	} // End EPPCoaDomainTst.tearDown();


	/**
	 * JUNIT <code>suite</code> static method, which returns the tests associated
	 * with <code>EPPCoaDomainTst</code>.
	 */

	public static Test suite () {

		TestSuite suite = new TestSuite( EPPCoaDomainTst.class );

		String theConfigFileName = System.getProperty( "EPP.ConfigFile" );
		if ( theConfigFileName != null )
			configFileName = theConfigFileName;

		try {
			app.initialize( configFileName );
		}

		catch ( EPPCommandException e ) {
			e.printStackTrace();

			Assert.fail( "Error initializing the EPP Application: " + e );
		}

		return suite;

	} // End EPPCoaDomainTst.suite()


	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a server
	 * generated error or a general exception. If the exception was caused by a
	 * server error, "Server Error :<Response XML>" will be specified. If the
	 * exception was caused by a general algorithm error,
	 * "General Error :<Exception Description>" will be specified.
	 * 
	 * @param aException
	 *        Exception thrown during test
	 */

	public void handleException ( Exception aException ) {

		EPPResponse domainResponse = session.getResponse();

		aException.printStackTrace();

		// Is a server specified error?

		if ( (domainResponse != null) && (!domainResponse.isSuccess()) ) {

			Assert.fail( "Server Error : " + domainResponse );

		}

		else {

			Assert.fail( "General Error : " + aException );

		}

	} // End EPPCoaDomainTst.handleException(EPPCommandException)


	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * <ul>
	 * <li>iterations Number of unit test iterations to run
	 * <li>validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be off.
	 * </ul>
	 */

	public static void main ( String args[] ) {

		// Override the default configuration file name?

		if ( args.length > 0 ) {

			configFileName = args[ 0 ];

		}

		// Number of Threads

		int numThreads = 1;

		String threadsStr = System.getProperty( "threads" );

		if ( threadsStr != null )
			numThreads = Integer.parseInt( threadsStr );

		// Run test suite in multiple threads?

		if ( numThreads > 1 ) {

			// Spawn each thread passing in the Test Suite

			for ( int i = 0; i < numThreads; i++ ) {

				TestThread thread =
						new TestThread( "EPPSessionTst Thread " + i, EPPCoaDomainTst
								.suite() );

				thread.start();

			}

		}

		else
			// Single threaded mode.

			junit.textui.TestRunner.run( EPPCoaDomainTst.suite() );

		try {

			app.endApplication();

		}

		catch ( EPPCommandException e ) {

			e.printStackTrace();

			Assert.fail( "Error ending the EPP Application: " + e );

		}

	} // End EPPCoaDomainTst.main(String [])


	/**
	 * This method tries to generate a unique String as Domain Name and Name
	 * Server
	 */

	public String makeDomainName () {

		long tm = System.currentTimeMillis();

		return new String( Thread.currentThread()
				+ String.valueOf( tm + rd.nextInt( 12 ) ).substring( 10 )
				+ ".second.name" );

	}


	/**
	 * This method tries to generate a unique String as Domain Name and Name
	 * Server
	 */

	public String makeEmail () {

		long tm = System.currentTimeMillis();

		return new String( Thread.currentThread()
				+ String.valueOf( tm + rd.nextInt( 12 ) ).substring( 10 )
				+ "@second.name" );

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
					+ iteration + ": " );

			cat.info( Thread.currentThread().getName() + ", iteration " + iteration
					+ ": " + aTest + " Start" );

		}

		System.out.println( "Start of " + aTest );

		System.out
				.println( "****************************************************************\n" );

	} // End EPPCoaDomainTst.testStart(String)


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
					+ iteration + ": " );

			cat.info( Thread.currentThread().getName() + ", iteration " + iteration
					+ ": " + aTest + " End" );

		}

		System.out.println( "End of " + aTest );

		System.out.println( "\n" );

	} // End EPPCoaDomainTst.testEnd(String)

	/**
	 * EPP Domain associated with test
	 */

	private EPPDomain domain = null;

	/**
	 * EPP Session associated with test
	 */

	private EPPSession session = null;

	/**
	 * Current test iteration
	 */

	private int iteration = 0;

	/**
	 * Handle to the Singleton EPP Application instance (
	 * <code>EPPApplicationSingle</code>)
	 */

	private static EPPApplicationSingle app = EPPApplicationSingle.getInstance();

	/**
	 * Name of configuration file to use for test (default = epp.config).
	 */

	private static String configFileName = "epp.config";

	/**
	 * Random instance for the generation of unique objects (hosts, IP addresses,
	 * etc.).
	 */

	private Random rd = new Random( System.currentTimeMillis() );

	/**
	 * Logging category
	 */

	private static final Logger cat =
			Logger.getLogger( EPPCoaDomainTst.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );

} // End class EPPCoaDomainTst

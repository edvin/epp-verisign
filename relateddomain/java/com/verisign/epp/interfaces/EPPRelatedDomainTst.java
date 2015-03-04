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

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.domain.EPPDomainCreateResp;
import com.verisign.epp.codec.domain.EPPDomainInfoCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainMapFactory;
import com.verisign.epp.codec.domain.EPPDomainRenewResp;
import com.verisign.epp.codec.domain.EPPDomainStatus;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtAuthInfo;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtCreate;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtCreateResp;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtDelete;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtDeleteResp;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtDomain;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtInfData;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtInfo;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtPeriod;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtRenew;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtRenewResp;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtTransfer;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtTransferResp;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtUpdate;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the using the Related Domain Extension classes with the
 * <code>EPPDomain</code> class. The unit test will initialize a session with an
 * EPP Server, will invoke <code>EPPDomain</code> operations with Client Object
 * Attribute Extensions, and will end a session with an EPP Server. The
 * configuration file used by the unit test defaults to epp.config, but can be
 * changed by passing the file path as the first command line argument. The unit
 * test can be run in multiple threads by setting the "threads" system property.
 * For example, the unit test can be run in 2 threads with the configuration
 * file ../../epp.config with the following command: <br>
 * <br>
 * java com.verisign.epp.interfaces.EPPRelatedDomainTst -Dthreads=2
 * ../../epp.config <br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a>
 */

public class EPPRelatedDomainTst extends TestCase {

	/**
	 * Allocates an <code>EPPRelatedDomainTst</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with the
	 * logical name.
	 * 
	 * @param aName
	 *        Logical name of the test
	 */
	public EPPRelatedDomainTst ( final String aName ) {

		super( aName );

	} // End EPPRelatedDomainTst(String)


	/**
	 * JUNIT test method to implement the
	 * <code>EPPRelatedDomainTst TestCase</code>. Each sub-test will be invoked in
	 * order to satisfy testing the EPPDomain interface.
	 */
	public void testRelatedDomain () {

		int numIterations = 1;

		final String iterationsStr = System.getProperty( "iterations" );

		if ( iterationsStr != null ) {

			numIterations = Integer.parseInt( iterationsStr );

		}

		for ( this.iteration = 0; (numIterations == 0)
				|| (this.iteration < numIterations); this.iteration++ ) {

			printStart( "Test Suite" );

			domainInfo();
			domainCreate();
			domainUpdate();
			domainDelete();
			domainRenew();
			domainTransfer();

			printEnd( "Test Suite" );

		}

	}


	/**
	 * Unit test of <code>EPPRelatedDomain.sendRelatedInfo</code>.
	 */
	public void domainInfo () {
		printStart( "relatedDomainInfo " );

		EPPResponse response;

		try {
			System.out
					.println("\ntestDomainInfo: Domain info of xn--test.tld1 with Domain Info Form");

			this.relatedDomain.addDomainName("xn--test.tld1");
			this.relatedDomain.setTransId("ABC-12349");
			this.relatedDomain.setHosts(EPPDomainInfoCmd.HOSTS_DELEGATED);
			this.relatedDomain.setAuthString("2fooBAR");
			this.relatedDomain.setInfoForm(EPPRelatedDomain.DOMAIN_INFO_FORM);
			response = this.relatedDomain.sendRelatedInfo();

			// -- Output all of the response attributes
			System.out.println("testDomainInfo: Response = [" + response
					+ "]\n\n");

			// -- Output the relDom:infData extension
			if (response.hasExtension(EPPRelatedDomainExtInfData.class)) {
				final EPPRelatedDomainExtInfData relatedDomainInfData = (EPPRelatedDomainExtInfData) response
						.getExtension(EPPRelatedDomainExtInfData.class);
				System.out.println("EPPRelatedDomainExtInfData = ["
						+ relatedDomainInfData + "]\n\n");

			}
			else {
				Assert.fail("EPPRelatedDomainExtInfData extension not included in response");
			}

			System.out
					.println("\ntestDomainInfo: Domain info of xn--test.tld2 with Related Info Form");

			this.relatedDomain.addDomainName("xn--test.tld2");
			this.relatedDomain.setTransId("ABC-12349");
			this.relatedDomain.setInfoForm(EPPRelatedDomain.RELATED_INFO_FORM);
			response = this.relatedDomain.sendRelatedInfo();

			// -- Output all of the response attributes
			System.out.println("testDomainInfo: Response = [" + response
					+ "]\n\n");

			// -- Output the relDom:infData extension
			if (response.hasExtension(EPPRelatedDomainExtInfData.class)) {
				final EPPRelatedDomainExtInfData relatedDomainInfData = (EPPRelatedDomainExtInfData) response
						.getExtension(EPPRelatedDomainExtInfData.class);
				System.out.println("EPPRelatedDomainExtInfData = ["
						+ relatedDomainInfData + "]\n\n");

			}
			else {
				Assert.fail("EPPRelatedDomainExtInfData extension not included in response");
			}

		}
		catch (final EPPCommandException e) {
			handleException(e);
		}

		printEnd( "EPPRelatedDomainTst relatedDomainInfo" );
	}


	/**
	 * Unit test of <code>EPPRelatedDomain.sendRelatedCreate</code>.
	 */
	public void domainCreate () {
		printStart( "relatedDomainCreate " );

		EPPDomainCreateResp response;

		try {
			System.out
					.println( "\ntestDomainCreate: Domain create of example.com and related domains" );

			this.relatedDomain.addDomainName( "example.com" );
			this.relatedDomain.setTransId( "ABC-12349" );
			this.relatedDomain.setAuthString( "2fooBAR" );

			
			final EPPRelatedDomainExtAuthInfo authInfo = new EPPRelatedDomainExtAuthInfo(
					"relDom123!");
			final EPPRelatedDomainExtPeriod period = new EPPRelatedDomainExtPeriod(
					5);
			this.relatedDomain.addRelatedDomain(new EPPRelatedDomainExtDomain(
					"domain1.com", authInfo, period));
			this.relatedDomain.addRelatedDomain(new EPPRelatedDomainExtDomain(
					"domain2.com", authInfo, period));
			this.relatedDomain.addRelatedDomain(new EPPRelatedDomainExtDomain("xn--idn.com", authInfo,
					period, "CHI"));		
			
			response = this.relatedDomain.sendRelatedCreate();

			// -- Output all of the response attributes
			System.out
					.println( "testDomainCreate: Response = [" + response + "]\n\n" );

			// -- Output the relDom:infData extension
			if ( response.hasExtension( EPPRelatedDomainExtCreateResp.class ) ) {
				final EPPRelatedDomainExtCreateResp relatedDomainCreData =
						(EPPRelatedDomainExtCreateResp) response
								.getExtension( EPPRelatedDomainExtCreateResp.class );
				System.out.println( "EPPRelatedDomainExtCreateResp = ["
						+ relatedDomainCreData + "]\n\n" );

			}
			else {
				Assert
						.fail( "EPPRelatedDomainExtCreateResp extension not included for domain-create with related domains." );
			}

		}
		catch ( final EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "relatedDomainCreate" );
	}


	/**
	 * Unit test of <code>EPPRelatedDomain.sendRelatedDelete</code>.
	 */
	public void domainDelete () {
		printStart( "relatedDomainDelete " );

		EPPResponse response;

		try {

			System.out
					.println( "\ntestDomainDelete: Domain delete of example.com and related domains" );
			this.relatedDomain.addDomainName( "example.com" );
			this.relatedDomain.setTransId( "ABC-12349" );

			this.relatedDomain.addRelatedName("domain1.com");
			this.relatedDomain.addRelatedName("domain2.com");
			this.relatedDomain.addRelatedName("xn--idn.com");

			response = this.relatedDomain.sendRelatedDelete();

			// -- Output all of the response attributes
			System.out
					.println( "testDomainDelete: Response = [" + response + "]\n\n" );

			// -- Output the relDom:infData extension
			if ( response.hasExtension( EPPRelatedDomainExtDeleteResp.class ) ) {
				final EPPRelatedDomainExtDeleteResp relatedDomainCreData =
						(EPPRelatedDomainExtDeleteResp) response
								.getExtension( EPPRelatedDomainExtDeleteResp.class );
				System.out.println( "EPPRelatedDomainExtDeleteResp = ["
						+ relatedDomainCreData + "]\n\n" );

			}
			else {
				Assert
						.fail( "EPPRelatedDomainExtDeleteResp extension not included for domain-delete with related domains." );
			}

		}
		catch ( final EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "relatedDomainDelete" );
	}


	/**
	 * Unit test of <code>EPPRelatedDomain.sendRelatedTransfer</code>.
	 */
	public void domainTransfer () {
		printStart( "relatedDomainTransfer " );

		EPPResponse response;

		try {

			System.out
					.println( "\ntestDomainTransferRequest: Domain transfer of example.com and related domains" );
			setDomainTransferAttributes();
			this.relatedDomain.setTransferOpCode( EPPDomain.TRANSFER_REQUEST );
			response = this.relatedDomain.sendRelatedTransfer();
			System.out.println( "testDomainTransferRequest: Response = [" + response
					+ "]\n\n" );
			assertTransferExtRespExists( response );

			System.out
					.println( "\ntestDomainTransferApprove: Domain transfer of example.com and related domains" );
			setDomainTransferAttributes();
			this.relatedDomain.setTransferOpCode( EPPDomain.TRANSFER_APPROVE );
			response = this.relatedDomain.sendRelatedTransfer();
			System.out.println( "testDomainTransferApprove: Response = [" + response
					+ "]\n\n" );
			assertTransferExtRespExists( response );

			System.out
					.println( "\ntestDomainTransferReject: Domain transfer of example.com and related domains" );
			setDomainTransferAttributes();
			this.relatedDomain.setTransferOpCode( EPPDomain.TRANSFER_REJECT );
			response = this.relatedDomain.sendRelatedTransfer();
			System.out.println( "testDomainTransferReject: Response = [" + response
					+ "]\n\n" );
			assertTransferExtRespExists( response );

			System.out
					.println( "\ntestDomainTransferCancel: Domain transfer of example.com and related domains" );
			setDomainTransferAttributes();
			this.relatedDomain.setTransferOpCode( EPPDomain.TRANSFER_CANCEL );
			response = this.relatedDomain.sendRelatedTransfer();
			System.out.println( "testDomainTransferCancel: Response = [" + response
					+ "]\n\n" );
			assertTransferExtRespExists( response );

			System.out
					.println( "\ntestDomainTransferQuery: Domain transfer of example.com and related domains" );
			setDomainTransferAttributes();
			this.relatedDomain.setTransferOpCode( EPPDomain.TRANSFER_QUERY );
			response = this.relatedDomain.sendRelatedTransfer();
			System.out.println( "testDomainTransferQuery: Response = [" + response
					+ "]\n\n" );
			assertTransferExtRespExists( response );
		}
		catch ( final EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "relatedDomainTransfer" );
	}


	/**
	 * Utility method to populate the required objects with related domains for a
	 * domain transfer command.
	 */
	private void setDomainTransferAttributes () {
		this.relatedDomain.setTransId("ABC-12345");

		this.relatedDomain.setAuthString("ClientX");

		this.relatedDomain.addDomainName("example.com");

		this.relatedDomain.setPeriodLength(1);

		this.relatedDomain.setPeriodUnit(EPPDomain.PERIOD_YEAR);

		EPPRelatedDomainExtAuthInfo authInfo = new EPPRelatedDomainExtAuthInfo(
				"relDom123!");
		EPPRelatedDomainExtPeriod period = new EPPRelatedDomainExtPeriod(1);

		this.relatedDomain.addRelatedDomain(new EPPRelatedDomainExtDomain(
				"domain1.com", authInfo, period));
		this.relatedDomain.addRelatedDomain(new EPPRelatedDomainExtDomain(
				"domain2.com", authInfo));
	}


	/**
	 * Utility method to ensure that the related domain extension is present in
	 * the <code>EPPResponse</code>.
	 * 
	 * @param response
	 */
	private void assertTransferExtRespExists ( final EPPResponse response ) {
		if ( response.hasExtension( EPPRelatedDomainExtTransferResp.class ) ) {
			final EPPRelatedDomainExtTransferResp relatedDomainCreData =
					(EPPRelatedDomainExtTransferResp) response
							.getExtension( EPPRelatedDomainExtTransferResp.class );
			System.out.println( "EPPRelatedDomainExtTransferResp = ["
					+ relatedDomainCreData + "]\n\n" );

		}
		else {
			Assert
					.fail( "EPPRelatedDomainExtTransferResp extension not included for domain-transfer with related domains." );
		}

	}


	/**
	 * Unit test of <code>EPPDomain.sendRenew</code>.
	 */
	public void domainRenew () {
		printStart( "domainRenew" );

		EPPDomainRenewResp response;

		try {
			
			
			System.out.println( "\ndomainRenew: Domain renew" );

			this.relatedDomain.setTransId( "ABC-12345-XYZ" );

			this.relatedDomain.addDomainName( this.makeDomainName() );

			this.relatedDomain.setExpirationDate( new GregorianCalendar( 2004, 2, 3 )
					.getTime() );

			this.relatedDomain.setPeriodLength( 10 );

			this.relatedDomain.setPeriodUnit( EPPDomain.PERIOD_YEAR );
			
			final EPPRelatedDomainExtPeriod period =
					new EPPRelatedDomainExtPeriod( 5 );
			
			this.relatedDomain.addRelatedDomain(new EPPRelatedDomainExtDomain("domain1.com", new Date(),
					period));

			this.relatedDomain.addRelatedDomain(new EPPRelatedDomainExtDomain("domain2.com", new Date(),
					period));			

			response = this.relatedDomain.sendRelatedRenew();

			// -- Output the relDom:infData extension
			System.out.println( "testDomainRenew: Response = [" + response + "]\n\n" );
			if ( response.hasExtension( EPPRelatedDomainExtRenewResp.class ) ) {
				final EPPRelatedDomainExtRenewResp relatedDomainCreData =
						(EPPRelatedDomainExtRenewResp) response
								.getExtension( EPPRelatedDomainExtRenewResp.class );
				System.out.println( "EPPRelatedDomainExtRenewResp = ["
						+ relatedDomainCreData + "]\n\n" );

			}
			else {
				Assert
						.fail( "EPPRelatedDomainExtRenewResp extension not included for domain-renew with related domains." );
			}

		}

		catch ( final EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "domainRenew" );
	}


	/**
	 * Unit test of <code>EPPDomain.sendUpdate</code>.
	 */
	public void domainUpdate () {
		printStart( "domainUpdate" );

		EPPResponse response;

		try {
			System.out.println( "\ndomainUpdate: Domain update" );

			this.relatedDomain.setTransId( "ABC-12345-XYZ" );

			final String theDomainName = this.makeDomainName();

			this.relatedDomain.addDomainName( theDomainName );

			this.relatedDomain.addRelatedName("domain1.com");
			this.relatedDomain.addRelatedName("domain2.com");

			// Add attributes
			// Is the contact mapping supported?
			if ( EPPFactory.getInstance().hasService( EPPDomainMapFactory.NS_CONTACT ) ) {
				this.relatedDomain.setUpdateAttrib( EPPDomain.CONTACT, "SH0000",
						EPPDomain.CONTACT_BILLING, EPPDomain.ADD );
			}

			this.relatedDomain.setUpdateAttrib( EPPDomain.HOST,
					this.makeHostName( theDomainName ), EPPDomain.ADD );

			this.relatedDomain.setUpdateAttrib( EPPDomain.STATUS, new EPPDomainStatus(
					EPPDomain.STATUS_CLIENT_HOLD ), EPPDomain.ADD );

			// Remove attributes
			this.relatedDomain.setUpdateAttrib( EPPDomain.HOST,
					this.makeHostName( theDomainName ), EPPDomain.REMOVE );

			this.relatedDomain.setUpdateAttrib( EPPDomain.STATUS, new EPPDomainStatus(
					EPPDomain.STATUS_CLIENT_HOLD ), EPPDomain.REMOVE );

			// Is the contact mapping supported?
			if ( EPPFactory.getInstance().hasService( EPPDomainMapFactory.NS_CONTACT ) ) {
				this.relatedDomain.setUpdateAttrib( EPPDomain.CONTACT, "SH0000",
						EPPDomain.CONTACT_BILLING, EPPDomain.REMOVE );
			}

			// Execute update
			response = this.relatedDomain.sendRelatedUpdate();

			// -- Output all of the response attributes
			System.out.println( "domainUpdate: Response = [" + response + "]\n\n" );

			/**
			 * Result Set
			 */
			for ( int i = 0; i < response.getResults().size(); i++ ) {
				final EPPResult myResult =
						(EPPResult) response.getResults().elementAt( i );

				System.out.println( "Result Code    : " + myResult.getCode() );

				System.out.println( "Result Message : " + myResult.getMessage() );

				System.out.println( "Result Lang    : " + myResult.getLang() );

				if ( myResult.isSuccess() ) {
					System.out.println( "Command Passed " );
				}

				else {
					System.out.println( "Command Failed " );
				}

				if ( myResult.getAllValues() != null ) {
					for ( int k = 0; k < myResult.getAllValues().size(); k++ ) {
						System.out.println( "Result Values  : "
								+ myResult.getAllValues().elementAt( k ) );
					}
				}
			}
		}

		catch ( final EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "domainUpdate" );
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

		catch ( final EPPCommandException e ) {

			final EPPResponse domainResponse = this.session.getResponse();

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

	} // End EPPRelatedDomainTst.initSession()


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

		catch ( final EPPCommandException e ) {

			final EPPResponse domainResponse = this.session.getResponse();

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

	} // End EPPRelatedDomainTst.endSession()


	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar".
	 */

	@Override
	protected void setUp () {

		try {

			final String theSessionClassName =
					System.getProperty( "EPP.SessionClass" );

			if ( theSessionClassName != null ) {
				try {
					final Class theSessionClass = Class.forName( theSessionClassName );

					if ( !EPPSession.class.isAssignableFrom( (theSessionClass) ) ) {
						Assert.fail( theSessionClassName
								+ " is not a subclass of EPPSession" );
					}

					this.session = (EPPSession) theSessionClass.newInstance();
				}
				catch ( final Exception ex ) {
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

		catch ( final Exception e ) {

			e.printStackTrace();

			Assert.fail( "Error initializing the session: " + e );

		}

		initSession();

		this.relatedDomain = new EPPRelatedDomain( this.session );

	} // End EPPRelatedDomainTst.setUp();


	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */

	@Override
	protected void tearDown () {

		endSession();

	} // End EPPRelatedDomainTst.tearDown();


	/**
	 * JUNIT <code>suite</code> static method, which returns the tests associated
	 * with <code>EPPRelatedDomainTst</code>.
	 */

	public static Test suite () {

		final TestSuite suite = new TestSuite( EPPRelatedDomainTst.class );

		final String theConfigFileName = System.getProperty( "EPP.ConfigFile" );
		if ( theConfigFileName != null )
			configFileName = theConfigFileName;

		try {
			app.initialize( configFileName );
		}

		catch ( final EPPCommandException e ) {
			e.printStackTrace();

			Assert.fail( "Error initializing the EPP Application: " + e );
		}

		return suite;

	} // End EPPRelatedDomainTst.suite()


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

	public void handleException ( final Exception aException ) {

		final EPPResponse domainResponse = this.session.getResponse();

		aException.printStackTrace();

		// Is a server specified error?

		if ( (domainResponse != null) && (!domainResponse.isSuccess()) ) {

			Assert.fail( "Server Error : " + domainResponse );

		}

		else {

			Assert.fail( "General Error : " + aException );

		}

	} // End EPPRelatedDomainTst.handleException(EPPCommandException)


	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * <ul>
	 * <li>iterations Number of unit test iterations to run
	 * <li>validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be off.
	 * </ul>
	 */

	public static void main ( final String args[] ) {

		// Override the default configuration file name?

		if ( args.length > 0 ) {

			configFileName = args[ 0 ];

		}

		// Number of Threads

		int numThreads = 1;

		final String threadsStr = System.getProperty( "threads" );

		if ( threadsStr != null )
			numThreads = Integer.parseInt( threadsStr );

		// Run test suite in multiple threads?

		if ( numThreads > 1 ) {

			// Spawn each thread passing in the Test Suite

			for ( int i = 0; i < numThreads; i++ ) {

				final TestThread thread =
						new TestThread( "EPPSessionTst Thread " + i,
								EPPRelatedDomainTst.suite() );

				thread.start();

			}

		}

		else
			// Single threaded mode.

			junit.textui.TestRunner.run( EPPRelatedDomainTst.suite() );

		try {

			app.endApplication();

		}

		catch ( final EPPCommandException e ) {

			e.printStackTrace();

			Assert.fail( "Error ending the EPP Application: " + e );

		}

	} // End EPPRelatedDomainTst.main(String [])


	/**
	 * This method tries to generate a unique String as Domain Name and Name
	 * Server
	 */

	public String makeDomainName () {

		final long tm = System.currentTimeMillis();

		return new String( Thread.currentThread()
				+ String.valueOf( tm + this.rd.nextInt( 12 ) ).substring( 10 )
				+ ".second.name" );

	}


	/**
	 * Makes a unique host name for a domain using the current time.
	 * 
	 * @param newDomainName
	 *        DOCUMENT ME!
	 * @return Unique host name <code>String</code>
	 */
	public String makeHostName ( final String newDomainName ) {
		final long tm = System.currentTimeMillis();

		return new String( String.valueOf( tm + this.rd.nextInt( 10 ) ).substring(
				10 )
				+ "." + newDomainName );
	}


	/**
	 * This method tries to generate a unique String as Domain Name and Name
	 * Server
	 */

	public String makeEmail () {

		final long tm = System.currentTimeMillis();

		return new String( Thread.currentThread()
				+ String.valueOf( tm + this.rd.nextInt( 12 ) ).substring( 10 )
				+ "@second.name" );

	}


	/**
	 * Print the start of a test with the <code>Thread</code> name if the current
	 * thread is a <code>TestThread</code>.
	 * 
	 * @param Logical
	 *        name for the test
	 */

	private void printStart ( final String aTest ) {

		if ( Thread.currentThread() instanceof TestThread ) {

			System.out.print( Thread.currentThread().getName() + ", iteration "
					+ this.iteration + ": " );

			cat.info( Thread.currentThread().getName() + ", iteration "
					+ this.iteration + ": " + aTest + " Start" );

		}

		System.out.println( "Start of " + aTest );

		System.out
				.println( "****************************************************************\n" );

	} // End EPPRelatedDomainTst.testStart(String)


	/**
	 * Print the end of a test with the <code>Thread</code> name if the current
	 * thread is a <code>TestThread</code>.
	 * 
	 * @param Logical
	 *        name for the test
	 */

	private void printEnd ( final String aTest ) {

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

	} // End EPPRelatedDomainTst.testEnd(String)

	/**
	 * EPP Related Domain associated with test
	 */
	private EPPRelatedDomain relatedDomain = null;
	
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

	private final Random rd = new Random( System.currentTimeMillis() );

	/**
	 * Logging category
	 */

	private static final Logger cat = Logger.getLogger( EPPRelatedDomainTst.class
			.getName(), EPPCatFactory.getInstance().getFactory() );

} // End class EPPRelatedDomainTst

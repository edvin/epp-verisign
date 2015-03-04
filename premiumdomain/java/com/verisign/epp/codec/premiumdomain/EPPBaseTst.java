/*******************************************************************************
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced, or disclosed without the
 * written approval of the General Manager of VeriSign Information Services.
 * 
 * PRIVILEGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION (REGISTRY
 * SENSITIVE INFORMATION)
 * Copyright (c) 2007 VeriSign, Inc. All rights reserved.
 * **********************************************************
 */

package com.verisign.epp.codec.premiumdomain;

import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.interfaces.EPPApplicationSingle;
import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.interfaces.EPPSession;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;

/**
 * This is the base class for all SDK unit test. The class defines the common
 * methods like create session, end session, end application etc which can be
 * used by most of the test cases.The configuration file used by the unit test
 * defaults to epp.config. The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a>
 */

public abstract class EPPBaseTst extends TestCase {

	/**
	 * EPP Session associated with test
	 */
	private EPPSession session = null;

	/**
	 * Logging category
	 */
	private static final Logger cat = Logger.getLogger( EPPBaseTst.class
			.getName(), EPPCatFactory.getInstance().getFactory() );

	/**
	 * Name of configuration file to use for test (default = epp.config).
	 */
	protected static String configFileName = "epp.config";

	/**
	 * Handle to the Singleton EPP Application instance (<code>EPPApplicationSingle</code>)
	 */
	private static EPPApplicationSingle app = EPPApplicationSingle
			.getInstance();

	/**
	 * Current test iteration
	 */
	protected int iteration = 0;
	
	/**
	 * Random instance for the generation of unique objects
	 */
	private Random rd = new Random(System.currentTimeMillis());

	/**
	 * The constructor will initialize the base class <code>TestCase</code>
	 * with the logical name.
	 * 
	 * @param aName
	 *        Logical name of the test
	 */
	public EPPBaseTst ( String aName ) {
		super( aName );
	}


	/**
	 * This method returns the active EPP Session
	 * 
	 * @return EPPSession.
	 */
	public EPPSession getSession () {
		return session;
	}


	/**
	 * Unit test of <code>EPPSession.endSession</code>. The session with the
	 * EPP Server will be terminated.
	 */
	protected void endSession () {
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
	}


	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a
	 * server generated error or a general exception. If the exception was
	 * caused by a server error, "Server Error :<Response XML>" will be
	 * specified. If the exception was caused by a general algorithm error,
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

	}


	/**
	 * Print the start of a test with the <code>Thread</code> name if the
	 * current thread is a <code>TestThread</code>.
	 * 
	 * @param aTest
	 *        Logical name for the test
	 */
	protected void printStart ( String aTest ) {

		if ( Thread.currentThread() instanceof TestThread ) {
			System.out.print( Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " );
			cat.info( Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aTest + " Start" );
		}

		System.out.println( "Start of " + aTest );
		System.out
				.println( "****************************************************************\n" );
	}


	/**
	 * Print the end of a test with the <code>Thread</code> name if the
	 * current thread is a <code>TestThread</code>.
	 * 
	 * @param aTest
	 *        Logical name for the test
	 */
	protected void printEnd ( String aTest ) {
		System.out
				.println( "****************************************************************" );
		if ( Thread.currentThread() instanceof TestThread ) {
			System.out.print( Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " );
			cat.info( Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aTest + " End" );
		}

		System.out.println( "End of " + aTest );
		System.out.println( "\n" );
	}


	/**
	 * Print message
	 * 
	 * @param aMsg
	 *        message to print
	 */
	protected void printMsg ( String aMsg ) {

		if ( Thread.currentThread() instanceof TestThread ) {
			System.out.println( Thread.currentThread().getName()
					+ ", iteration " + iteration + ": " + aMsg );
			cat.info( Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aMsg );
		}
		else {
			System.out.println( aMsg );
			cat.info( aMsg );
		}
	}


	/**
	 * Print error message
	 * 
	 * @param aMsg
	 *        errpr message to print
	 */
	protected void printError ( String aMsg ) {
		if ( Thread.currentThread() instanceof TestThread ) {
			System.err.println( Thread.currentThread().getName()
					+ ", iteration " + iteration + ": " + aMsg );
			cat.error( Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aMsg );
		}
		else {
			System.err.println( aMsg );
			cat.error( aMsg );
		}
	}


	/**
	 * create session method, which sets the default client Id to
	 * "theRegistrar". Create instance of EPP session and establish the
	 * connection with the EPP server.
	 */
	protected void createSession () {
		try {
			String theSessionClassName = System
					.getProperty( "EPP.SessionClass" );

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
					Assert
							.fail( "Exception instantiating EPP.SessionClass value "
									+ theSessionClassName + ": " + ex );
				}
			}
			else {
				session = new EPPSession();
			}

			session.setClientID(Environment.getProperty("EPP.Test.clientId", "ClientX"));
			session.setPassword(Environment.getProperty("EPP.Test.password", "foo-BAR2"));

		}
		catch ( Exception e ) {
			e.printStackTrace();
			Assert.fail( "Error initializing the session: " + e );
		}

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

	}


	protected static void endApplication () {
		try {
			app.endApplication();
		}
		catch ( EPPCommandException e ) {
			e.printStackTrace();
			Assert.fail( "Error ending the EPP Application: " + e );
		}
	}


	protected static void initApplication () {
		try {
			app.initialize( configFileName );
		}

		catch ( EPPCommandException e ) {
			e.printStackTrace();

			Assert.fail( "Error initializing the EPP Application: " + e );
		}
	}


	/**
	 * This method tries to generate a unique String
	 */
	public String makeDomainName () {	
		long tm = System.currentTimeMillis();
		
		return new String("premium-"
				+ String.valueOf(tm + rd.nextInt(24)).substring(10)
				+ ".tv");
	}

}

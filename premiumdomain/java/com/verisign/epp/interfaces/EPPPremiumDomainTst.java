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

package com.verisign.epp.interfaces;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.premiumdomain.EPPBaseTst;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainCheck;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainReAssignCmd;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the using the Domain Extension classes with the
 * <code>EPPDomain</code> class. The unit test will initialize a session with
 * an EPP Server, will invoke <code>EPPDomain</code> operations with
 * Premiumdomain Extensions, and will end a session with an EPP Server. The
 * configuration file used by the unit test defaults to epp.config, but can be
 * changed by passing the file path as the first command line argument. The unit
 * test can be run in multiple threads by setting the "threads" system property.
 * For example, the unit test can be run in 2 threads with the configuration
 * file ../../epp.config with the following command: <br>
 * <br>
 * java com.verisign.epp.interfaces.EPPPremiumDomainTst -Dthreads=2
 * ../../epp.config <br>
 * <br>
 */
public class EPPPremiumDomainTst extends EPPBaseTst {

	/**
	 * EPP Domain associated with test
	 */
	private EPPDomain domain = null;


	/**
	 * Allocates an <code>EPPPremiumDomainTst</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 * 
	 * @param aName
	 *        Logical name of the test
	 */
	public EPPPremiumDomainTst ( String aName ) {
		super( aName );
	}


	/**
	 * JUNIT test method to implement the
	 * <code>EPPPremiumDomainTst TestCase</code>. Each sub-test will be
	 * invoked in order to satisfy testing the EPPDomain interface.
	 */
	public void testPremiumDomain () {

		int numIterations = 1;

		String iterationsStr = System.getProperty( "iterations" );

		if ( iterationsStr != null ) {
			numIterations = Integer.parseInt( iterationsStr );
		}

		for ( iteration = 0; (numIterations == 0)
				|| (iteration < numIterations); iteration++ ) {
			printStart( "Test Suite" );

			nonPremiumSingleDomainCheckWithNoPremiumExtension (domain);
			nonPremiumSingleDomainCheckWithFlagTrue (domain);
			nonPremiumSingleDomainCheckWithFlagFalse (domain);
			
			premiumSingleDomainCheckWithFlagTrue( domain );
			premiumSingleDomainCheckWithFlagFalse( domain );
			
			premiumMultipleDomainCheckWithFlagTrue( domain );
			premiumMultipleDomainCheckWithFlagFalse( domain );
			premiumDomainUpdate( domain );

			printEnd( "Test Suite" );
		}

	}

	/**
	 * Unit test of <code>EPPDomain.sendCheck</code>.
	 */
	public void nonPremiumSingleDomainCheckWithNoPremiumExtension ( EPPDomain aDomain ) {

		printStart( "Non-Premium Domain Check With No Premium Extension (Check single domain name)" );

		String domainName = "non-premiumdomain.tv";
		EPPResponse response;
		try {

			aDomain.setTransId( "ABC-12345-XYZ" );
			aDomain.addDomainName( domainName );

			response = aDomain.sendCheck();

			System.out.println( "nonPremiumSingleDomainCheckWithNoPremiumExtension: Response = [" + response
					+ "]\n\n" );
		}
		catch ( EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "Non-Premium Domain Check With No Premium Extension (Check single domain name)" );
	}
	
	/**
	 * Unit test of <code>EPPDomain.sendCheck</code>.
	 */
	public void nonPremiumSingleDomainCheckWithFlagTrue ( EPPDomain aDomain ) {

		printStart( "Non-Premium Domain Check With Flag True (Check single domain name)" );

		String domainName = "non-premiumdomain.tv";
		EPPResponse response;
		try {

			aDomain.setTransId( "ABC-12345-XYZ" );
			aDomain.addDomainName( domainName );

			EPPPremiumDomainCheck extension = new EPPPremiumDomainCheck( true );
			aDomain.addExtension( extension );
	
			response = aDomain.sendCheck();

			System.out.println( "nonPremiumSingleDomainCheckWithFlagTrue: Response = [" + response
					+ "]\n\n" );
		}
		catch ( EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "Non-Premium Domain Check With Flag True (Check single domain name)" );
	}
	
	/**
	 * Unit test of <code>EPPDomain.sendCheck</code>.
	 */
	public void nonPremiumSingleDomainCheckWithFlagFalse ( EPPDomain aDomain ) {

		printStart( "Non-Premium Domain Check With Flag False (Check single domain name)" );

		String domainName = "non-premiumdomain.tv";
		EPPResponse response;
		try {

			aDomain.setTransId( "ABC-12345-XYZ" );
			aDomain.addDomainName( domainName );

			EPPPremiumDomainCheck extension = new EPPPremiumDomainCheck( false );
			aDomain.addExtension( extension );
	
			response = aDomain.sendCheck();

			System.out.println( "nonPremiumSingleDomainCheckWithFlagFalse: Response = [" + response
					+ "]\n\n" );
		}
		catch ( EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "Non-Premium Domain Check With Flag False (Check single domain name)" );
	}	
	
	/**
	 * Unit test of <code>EPPDomain.sendCheck</code>.
	 */
	public void premiumSingleDomainCheckWithFlagTrue ( EPPDomain aDomain ) {

		printStart( "Premium Domain Check With Flag True (Check single domain name)" );

		String domainName = this.makeDomainName();
		EPPResponse response;
		try {

			aDomain.setTransId( "ABC-12345-XYZ" );
			aDomain.addDomainName( domainName );

			EPPPremiumDomainCheck extension = new EPPPremiumDomainCheck( true );
			aDomain.addExtension( extension );
			
			response = aDomain.sendCheck();

			System.out.println( "premiumSingleDomainCheckWithFlagTrue: Response = [" + response
					+ "]\n\n" );
		}
		catch ( EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "Premium Domain Check With Flag True (Check single domain name)" );
	}
	
	/**
	 * Unit test of <code>EPPDomain.sendCheck</code>.
	 */
	public void premiumSingleDomainCheckWithFlagFalse ( EPPDomain aDomain ) {

		printStart( "Premium Domain Check With Flag False (Check single domain name)" );

		String domainName = this.makeDomainName();
		EPPResponse response;
		try {

			aDomain.setTransId( "ABC-12345-XYZ" );
			aDomain.addDomainName( domainName );

			EPPPremiumDomainCheck extension = new EPPPremiumDomainCheck( false );
			aDomain.addExtension( extension );
			
			response = aDomain.sendCheck();

			System.out.println( "premiumSingleDomainCheckWithFlagFalse: Response = [" + response
					+ "]\n\n" );
		}
		catch ( EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "Premium Domain Check With Flag False (Check single domain name)" );
	}
	
	/**
	 * Unit test of <code>EPPDomain.sendCheck</code>.
	 */
	public void premiumMultipleDomainCheckWithFlagTrue ( EPPDomain aDomain ) {

		printStart( "Premium Domain Check With Flag True (Check multiple domain names)" );

		EPPResponse response;
		try {

			aDomain.setTransId( "ABC-12345-XYZ" );
			
			for (int i = 1; i <= 3; i++) {
				aDomain.addDomainName( this.makeDomainName() );
			}
			
			EPPPremiumDomainCheck extension = new EPPPremiumDomainCheck( true );
			aDomain.addExtension( extension );
			
			response = aDomain.sendCheck();

			System.out.println( "premiumMultipleDomainCheckWithFlagTrue: Response = [" + response
					+ "]\n\n" );
		}
		catch ( EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "Premium Domain Check With Flag True (Check multiple domain names)" );
	}	
	
	/**
	 * Unit test of <code>EPPDomain.sendCheck</code>.
	 */
	public void premiumMultipleDomainCheckWithFlagFalse ( EPPDomain aDomain ) {

		printStart( "Premium Domain Check With Flag False (Check multiple domain names)" );

		EPPResponse response;
		try {

			aDomain.setTransId( "ABC-12345-XYZ" );
			
			for (int i = 1; i <= 3; i++) {
				aDomain.addDomainName( this.makeDomainName() );
			}
			
			EPPPremiumDomainCheck extension = new EPPPremiumDomainCheck( false );
			aDomain.addExtension( extension );
			
			response = aDomain.sendCheck();

			System.out.println( "premiumMultipleDomainCheckWithFlagFalse: Response = [" + response
					+ "]\n\n" );
		}
		catch ( EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "Premium Domain Check With Flag False (Check multiple domain names)" );
	}
	
	/**
	 * Unit test of <code>EPPDomain.sendUpdate</code>.
	 */
	public void premiumDomainUpdate ( EPPDomain aDomain ) {

		printStart( "Premium Domain Update" );

		String domainName = this.makeDomainName();
		EPPResponse response;
		try {

			aDomain.setTransId( "ABC-12345-XYZ" );
			aDomain.addDomainName( domainName );

			EPPPremiumDomainReAssignCmd extension = new EPPPremiumDomainReAssignCmd(
					"testregistrar" );
			aDomain.addExtension( extension );

			response = aDomain.sendUpdate();

			System.out.println( "premiumDomainUpdate: Response = [" + response
					+ "]\n\n" );
		}
		catch ( EPPCommandException e ) {
			handleException( e );
		}

		printEnd( "Premium Domain Update" );
	}


	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar".
	 */
	protected void setUp () {
		createSession();
		domain = new EPPDomain( getSession() );

	}


	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown () {
		endSession();
	}


	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPPremiumDomainTst</code>.
	 */
	public static Test suite () {

		TestSuite suite = new TestSuite( EPPPremiumDomainTst.class );

		String theConfigFileName = System.getProperty( "EPP.ConfigFile" );
		if ( theConfigFileName != null )
			configFileName = theConfigFileName;

		initApplication();

		return suite;

	}


	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * <ul>
	 * <li>iterations Number of unit test iterations to run
	 * <li>validate Turn XML validation on (<code>true</code>) or off (<code>false</code>).
	 * If validate is not specified, validation will be off.
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
				TestThread thread = new TestThread(
						"EPPSessionTst Thread " + i, EPPPremiumDomainTst
								.suite() );
				thread.start();
			}

		}
		else {
			// Single threaded mode.
			junit.textui.TestRunner.run( EPPPremiumDomainTst.suite() );
		}

		endApplication();

	}

}

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

package com.verisign.epp.codec.whowas;

import java.util.Date;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the com.verisign.epp.codec.whowas package. The unit test
 * will execute, gather statistics, and output the results of a test of each
 * com.verisign.epp.codec.whowas package concrete
 * {@link com.verisign.epp.codec.gen.EPPCommand}'s and their expected
 * {@link com.verisign.epp.codec.gen.EPPResponse}. The unit test is dependent on
 * the use of <a href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a>
 * 
 * @author Deepak Deshpande
 */
public class EPPWhoWasTst extends TestCase {
	/**
	 * Number of unit test iterations to run. This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	private static long numIterations = 1;


	/**
	 * Creates a new EPPWhoWasTst object.
	 */
	public EPPWhoWasTst ( String name ) {
		super( name );
	}


	/**
	 * Runs codec tests on {@link EPPWhoWasInfoCmd}.
	 */
	public void testWhoWasInfoCmd () {
		EPPCodecTst.printStart( "testWhoWasInfoCmd" );

		/**
		 * The test is randomized so we'll run it many times.
		 */

		String type = EPPWhoWasConstants.TYPE_DOMAIN;

		for ( int i = 0; i < numIterations; i++ ) {

			EPPCodecTst.printStart( "EPPWhoWasInfoCmd set with both type and name" );
			EPPWhoWasInfoCmd cmd = new EPPWhoWasInfoCmd( "51364-CLI", type );
			cmd.setName( "abc.com" );

			EPPCodecTst.testEncodeDecode( cmd );

			EPPCodecTst.printStart( "EPPWhoWasInfoCmd set with both type and roid" );
			cmd = new EPPWhoWasInfoCmd( "51364-CLI", type );
			cmd.setRoid( "abc-rep" );

			EPPCodecTst.testEncodeDecode( cmd );

			EPPCodecTst
					.printStart( "EPPWhoWasInfoCmd set with only name and null type" );
			cmd = new EPPWhoWasInfoCmd( "51364-CLI" );
			cmd.setName( "abc.com" );

			EPPCodecTst.testEncodeDecode( cmd );

			EPPCodecTst
					.printStart( "EPPWhoWasInfoCmd set with only roid and null type" );
			cmd = new EPPWhoWasInfoCmd( "51364-CLI" );
			cmd.setRoid( "abc-rep" );

			EPPCodecTst.testEncodeDecode( cmd );

		}

		EPPCodecTst.printEnd( "testWhoWasInfoCmd" );
	}


	/**
	 * Runs codec tests on {@link EPPWhoWasInfoResp}.
	 */
	public void testWhoWasInfoResponse () {

		EPPCodecTst.printStart( "testWhoWasInfoResponse" );

		/**
		 * The test is randomized so we'll run it many times.
		 */

		String type = EPPWhoWasConstants.TYPE_DOMAIN;

		for ( int i = 0; i < numIterations; i++ ) {

			EPPWhoWasInfoCmd cmd = new EPPWhoWasInfoCmd( "51364-CLI", type );

			EPPTransId aTransId = new EPPTransId( cmd.getTransId(), "SRV-43659" );

			EPPCodecTst.printStart( "EPPWhoWasInfoResp set with both type and name" );
			EPPWhoWasInfoResp resp = new EPPWhoWasInfoResp( aTransId, type );
			resp.setName( "abc.com" );

			EPPWhoWasRecord record = new EPPWhoWasRecord();

			record.setTransactionDate( new Date() );
			record.setName( "abc.com" );
			record.setOperation( "CREATE" );
			record.setRoid( "EXAMPLE1-REP" );
			record.setClientID( "ClientX" );
			record.setClientName( "Client X Corp" );

			EPPWhoWasHistory history = new EPPWhoWasHistory();
			history.addRecord( record );

			resp.setHistory( history );

			EPPCodecTst.testEncodeDecode( resp );

			EPPCodecTst.printStart( "EPPWhoWasInfoResp set with both type and name" );
			resp = new EPPWhoWasInfoResp( aTransId, type );
			resp.setRoid( "abc-rep" );

			resp.setHistory( history );

			EPPCodecTst.testEncodeDecode( resp );

			EPPCodecTst
					.printStart( "EPPWhoWasInfoResp set with only name and null type" );
			resp = new EPPWhoWasInfoResp( aTransId );
			resp.setName( "abc.com" );

			resp.setHistory( history );

			EPPCodecTst.testEncodeDecode( resp );

			EPPCodecTst
					.printStart( "EPPWhoWasInfoResp set with only roid and null type" );
			resp = new EPPWhoWasInfoResp( aTransId );
			resp.setRoid( "abc-rep" );

			resp.setHistory( history );

			EPPCodecTst.testEncodeDecode( resp );
		}

		EPPCodecTst.printEnd( "testWhoWasInfoResponse" );
	}


	/**
	 * JUNIT <code>setUp</code>, which currently does nothing.
	 */
	protected void setUp () {
	}


	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown () {
	}


	/**
	 * JUNIT <code>suite</code> static method, which returns the tests associated
	 * with <code>EPPWhoWasTst</code>.
	 */
	public static Test suite () {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite( EPPWhoWasTst.class );

		// iterations Property
		String numIterProp = System.getProperty( "iterations" );

		if ( numIterProp != null ) {
			numIterations = Integer.parseInt( numIterProp );
		}

		// Add the EPPWhoWasMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.whowas.EPPWhoWasMapFactory" );
		}
		catch ( EPPCodecException e ) {
			Assert.fail( "EPPCodecException adding EPPWhoWasMapFactory to EPPCodec: "
					+ e );
		}

		return suite;
	}


	/**
	 * Unit test main, which accepts the following system property options:<br>
	 * <ul>
	 * <li>iterations Number of unit test iterations to run</li>
	 * <li>validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be off.</li>
	 * </ul>
	 */
	public static void main ( String[] args ) {
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
		else { // Single threaded mode.
			junit.textui.TestRunner.run( EPPWhoWasTst.suite() );
		}
	}


	/**
	 * Sets the number of iterations to run per test.
	 * 
	 * @param aNumIterations
	 *        number of iterations to run per test
	 */
	public static void setNumIterations ( long aNumIterations ) {
		numIterations = aNumIterations;
	}
}

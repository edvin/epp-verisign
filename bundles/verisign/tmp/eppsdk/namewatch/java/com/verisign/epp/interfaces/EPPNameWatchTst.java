/* ===========================================================================

 * Copyright (C) 2001 VeriSign, Inc.

 *

 * This program is free software; you can redistribute it and/or

 * modify it under the terms of the GNU General Public License

 * as published by the Free Software Foundation; either version 2

 * of the License, or (atEPP your option) any later version.

 *

 * This program is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

 * GNU General Public License for more details.

 *

 * You should have received a copy of the GNU General Public License

 * along with this program; if not, write to the Free Software

 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA

 *

 * VeriSign Global Registry Services

 * 505 Huntmar Park Dr.

 * Herndon, VA 20170

 * ===========================================================================

 * The EPP, APIs and Software are provided "as-is" and without any warranty

 * of any kind. VeriSign Corporation EXPRESSLY DISCLAIMS ALL WARRANTIES

 * AND/OR CONDITIONS, EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO,

 * THE IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY OR SATISFACTORY

 * QUALITY AND FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD

 * PARTY RIGHTS. VeriSign Corporation DOES NOT WARRANT THAT THE FUNCTIONS

 * CONTAINED IN THE EPP, APIs OR SOFTWARE WILL MEET REGISTRAR'S REQUIREMENTS,

 * OR THAT THE OPERATION OF THE EPP, APIs OR SOFTWARE WILL BE UNINTERRUPTED

 * OR ERROR-FREE,OR THAT DEFECTS IN THE EPP, APIs OR SOFTWARE WILL BE CORRECTED.

 * FURTHERMORE, VeriSign Corporation DOES NOT WARRANT NOR MAKE ANY

 * REPRESENTATIONS REGARDING THE USE OR THE RESULTS OF THE EPP, APIs, SOFTWARE

 * OR RELATED DOCUMENTATION IN TERMS OF THEIR CORRECTNESS, ACCURACY,

 * RELIABILITY, OR OTHERWISE.  SHOULD THE EPP, APIs OR SOFTWARE PROVE DEFECTIVE,

 * REGISTRAR ASSUMES THE ENTIRE COST OF ALL NECESSARY SERVICING, REPAIR OR

 * CORRECTION.

 * ===========================================================================

 *

 * $Id: EPPNameWatchTst.java,v 1.2 2004/01/16 03:44:48 jim Exp $

 *

 * ======================================================================== */



//----------------------------------------------

//

// package

//

//----------------------------------------------

package	com.verisign.epp.interfaces;







//----------------------------------------------

//

// imports...

//

//----------------------------------------------



// Java Core Imports

import java.util.Date;

import java.util.GregorianCalendar;

import java.util.Vector;

import java.util.Enumeration;

import java.io.ByteArrayOutputStream;

import java.io.ByteArrayInputStream;

import java.io.ObjectOutputStream;

import java.io.ObjectInputStream;

import java.io.PrintWriter;

import java.util.Random;





// JUNIT Imports

import junit.framework.*;



// Log4j imports

import org.apache.log4j.*;



// EPP Imports

import com.verisign.epp.interfaces.EPPApplicationSingle;

import com.verisign.epp.interfaces.EPPNameWatch;

import com.verisign.epp.interfaces.EPPCommandException;

import com.verisign.epp.transport.EPPClientCon;

import com.verisign.epp.transport.EPPConException;

import com.verisign.epp.codec.gen.EPPResponse;

import com.verisign.epp.codec.gen.EPPResult;

import com.verisign.epp.codec.gen.EPPFactory;

import com.verisign.epp.codec.nameWatch.*;

import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;

import com.verisign.epp.util.TestErrorHandler;

import com.verisign.epp.util.EPPCatFactory;





/**

 *	Is a unit test of the <code>EPPNameWatch</code> class.  The unit test will initialize a

 *	session with an EPP Server, will invoke <code>EPPNameWatch</code> operations,

 *	and will end a session with an EPP Server.  The

 *	configuration file used by the unit test defaults to epp.config, but

 *	can be changed by passing the file path as the first command line

 *	argument.  The unit test can be run in multiple threads by setting

 *	the "threads" system property.  For example, the unit test can

 *	be run in 2 threads with the configuration file ../../epp.config with

 *	the following command:<br><br>

 *	java com.verisign.epp.interfaces.EPPNameWatchTst -Dthreads=2 ../../epp.config

 *	<br><br>

 *	The unit test is dependent on the

 *	use of <a href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a>

 *	<br><br>

 *	<br><br>

 * @author	$Author: jim $

 * @version	$Revision: 1.2 $

 */

public class EPPNameWatchTst extends TestCase

{



	/**

	 *	Allocates an <code>EPPNameWatchTst</code> with a logical name.  The

	 *	constructor will initialize the base class <code>TestCase</code>

	 *	with the logical name.

	 *

	 *	@param	name	Logical name of the test

	 */

	public EPPNameWatchTst(String name) {

		super(name);



	} // End EPPNameWatchTst(String)



	/**

	 *	JUNIT test method to implement the <code>EPPNameWatchTst TestCase</code>.  Each

	 *	sub-test will be invoked in order to satisfy testing the EPPNameWatch interface.

	 */

	public void testNameWatch(){

		int numIterations = 1;

		String iterationsStr = System.getProperty("iterations");



		if (iterationsStr != null) {

			numIterations = Integer.parseInt(iterationsStr);

		}



		for (iteration = 0; (numIterations == 0) || (iteration < numIterations); iteration++) {



			printStart("Test Suite");



			nameWatchInfo();

			nameWatchCreate();

			nameWatchDelete();

			nameWatchRenew();

			nameWatchUpdate();

			nameWatchTransferQuery();

            nameWatchTransfer();



			printEnd("Test Suite");

		}





	} // End EPPNameWatchTst.testNameWatch()







	/**

	 *	Unit test of <code>EPPNameWatch.sendInfo</code>.

	 */

	public void nameWatchInfo()

	{

		printStart("nameWatchInfo");



		EPPNameWatchInfoResp response;



		try

		{

			System.out.println("\nnameWatchInfo: NameWatch info for EXAMPLE1-REP");

			nameWatch.setTransId("ABC-12345-XYZ");

            nameWatch.setRoid("EXAMPLE1-REP");



			response = nameWatch.sendInfo();



			//-- Output all of the response attributes

			System.out.println("nameWatchInfo: Response = [" + response + "]\n\n");



			//-- Output required response attributes using accessors

			System.out.println("nameWatchInfo: name            = " + response.getName());

            System.out.println("nameWatchInfo: roid            = " + response.getRoid());

            System.out.println("nameWatchInfo: registrant      = " + response.getRegistrant());

			System.out.println("nameWatchInfo: client id       = " + response.getClientId());

			System.out.println("nameWatchInfo: created by      = " + response.getCreatedBy());

			System.out.println("nameWatchInfo: create date     = " + response.getCreatedDate());

			System.out.println("nameWatchInfo: expiration date = " + response.getExpirationDate());





            /**

             * Get RptTo

             */

            if (response.getRptTo() != null) {

                System.out.println("Reply To        :" + response.getRptTo().getRptTo());

                System.out.println("Frequency Type  :" + response.getRptTo().getFreqType());

            }



			/**

			* Get AuthInfo

			*/



			if(response.getAuthInfo() != null){

				System.out.println("Authorization        : " + response.getAuthInfo().getPassword());

				System.out.println("Authorization (Roid) : " + response.getAuthInfo().getRoid());

			}



			/**

			* Get Status

			*/

			if(response.getStatuses() != null){

				for(int i = 0;i < response.getStatuses().size();i++){

					EPPNameWatchStatus myStatus = (EPPNameWatchStatus) response.getStatuses().elementAt(i);

					System.out.println("Lang     : " + myStatus.getLang());

					System.out.println("Status   : " + myStatus.getStatus());

				}

			}



			/**

			* Result Set

			*/

			for(int i=0;i < response.getResults().size();i++){

				EPPResult myResult = (EPPResult)response.getResults().elementAt(i);

				System.out.println("Result Code    : " + myResult.getCode());

				System.out.println("Result Message : " + myResult.getMessage());

				System.out.println("Result Lang    : " + myResult.getLang());



				if(myResult.isSuccess()){

					System.out.println("Command Passed ");

				}else{

					System.out.println("Command Failed ");

				}

				if(myResult.getValues() != null){

					for(int k =0; k < myResult.getValues().size(); k++){

						System.out.println("Result Values  : " + myResult.getValues().elementAt(k));

					}

				}

			}

		}

		catch (EPPCommandException e){

			handleException(e);

		}



		printEnd("nameWatchInfo");



	} // End EPPNameWatchTst.nameWatchInfo()







	/**

	 *	Unit test of <code>EPPNameWatch.sendCreate</code>.

	 */

	public void nameWatchCreate()

	{

		printStart("nameWatchCreate");



		EPPNameWatchCreateResp response;



		try{

			System.out.println("\n----------------------------------------------------------------");

			System.out.println("nameWatchCreate: Create \"doe\" with no optional attributes");



			nameWatch.setTransId("ABC-12345-XYZ");

            nameWatch.setName("doe");

			nameWatch.setRegistrant("jd1234");

			nameWatch.setRptTo("jdoe@example.com");

			nameWatch.setFreq(EPPNameWatch.FREQ_WEEKLY);

            nameWatch.setAuthString("2fooBAR");
            
            nameWatch.setPeriodLength(2);



			response = nameWatch.sendCreate();



			//-- Output all of the response attributes

			System.out.println("nameWatchCreate: Response = [" + response + "]\n\n");



			//-- Output response attributes using accessors

			System.out.println("nameWatchCreate: name = " + response.getName());

            System.out.println("nameWatchCreate: roid = " + response.getRoid());

            System.out.println("nameWatchCreate: creation date = " + response.getCreationDate());



            /**

			 * Result Set

			 */

			for (int i=0;i < response.getResults().size();i++) {

				EPPResult myResult = (EPPResult)response.getResults().elementAt(i);

				System.out.println("Result Code    : " + myResult.getCode());

				System.out.println("Result Message : " + myResult.getMessage());

				System.out.println("Result Lang    : " + myResult.getLang());



				if(myResult.isSuccess()) {

					System.out.println("Command Passed ");

				}

				else {

					System.out.println("Command Failed ");

				}

				if (myResult.getValues() != null) {

					for (int k =0; k < myResult.getValues().size(); k++) {

						System.out.println("Result Values  : " + myResult.getValues().elementAt(k));

					}

				}



			}

		}

		catch (EPPCommandException e){

			handleException(e);

		}





		printEnd("nameWatchCreate");



	} // End EPPNameWatchTst.nameWatchCreate()





	/**

	 *	Unit test of <code>EPPNameWatch.sendDelete</code>.

	 */

	public void nameWatchDelete()

	{

		printStart("nameWatchDelete");



		EPPResponse response;



		try{

			System.out.println("\nnameWatchDelete: NameWatch delete for EXAMPLE1-REP");

			nameWatch.setTransId("ABC-12345-XYZ");

            nameWatch.setRoid("EXAMPLE1-REP");



			response = nameWatch.sendDelete();



			//-- Output all of the response attributes

			System.out.println("nameWatchDelete: Response = [" + response + "]\n\n");

			/**

			 * Result Set

			 */

			for(int i=0;i < response.getResults().size();i++){

				EPPResult myResult = (EPPResult)response.getResults().elementAt(i);

				System.out.println("Result Code    : " + myResult.getCode());

				System.out.println("Result Message : " + myResult.getMessage());

				System.out.println("Result Lang    : " + myResult.getLang());



				if(myResult.isSuccess()){

					System.out.println("Command Passed ");

				}else{

					System.out.println("Command Failed ");

				}

				if(myResult.getValues() != null) {



					for(int k =0; k < myResult.getValues().size(); k++) {

						System.out.println("Result Values  : " + myResult.getValues().elementAt(k));

					}

				}



			}



		}

		catch (EPPCommandException e)

		{

			handleException(e);

		}



		printEnd("nameWatchDelete");

	} // End EPPNameWatchTst.nameWatchDelete()





	/**

	 *	Unit test of <code>EPPNameWatch.sendRenew</code>.

	 */

	public void nameWatchRenew()

	{



		printStart("nameWatchRenew");

		EPPNameWatchRenewResp response;



		try{

			System.out.println("\nnameWatchRenew: NameWatch renew for EXAMPLE1-REP");

			nameWatch.setTransId("ABC-12345-XYZ");

			nameWatch.setRoid("EXAMPLE1-REP");

			nameWatch.setExpirationDate(new GregorianCalendar(2000,4,3).getTime());

			nameWatch.setPeriodLength(10);



			response = nameWatch.sendRenew();



			//-- Output all of the response attributes

			System.out.println("nameWatchRenew: Response = [" + response + "]\n\n");



			//-- Output response attributes using accessors

			System.out.println("nameWatchRenew: roid = " + response.getRoid());

			System.out.println("nameWatchRenew: expiration date = " + response.getExpirationDate());



			/**

			 * Result Set

			 */

			for (int i=0;i < response.getResults().size();i++) {

				EPPResult myResult = (EPPResult)response.getResults().elementAt(i);

				System.out.println("Result Code    : " + myResult.getCode());

				System.out.println("Result Message : " + myResult.getMessage());

				System.out.println("Result Lang    : " + myResult.getLang());



				if(myResult.isSuccess()) {

					System.out.println("Command Passed ");

				}

				else {

					System.out.println("Command Failed ");

				}

				if (myResult.getValues() != null) {

					for (int k =0; k < myResult.getValues().size(); k++) {

						System.out.println("Result Values  : " + myResult.getValues().elementAt(k));

					}

				}



			}





		}

		catch (EPPCommandException e) {

		    handleException(e);

		}

		printEnd("nameWatchRenew");

	} // End EPPNameWatchTst.nameWatchRenew()





	/**

	 *	Unit test of <code>EPPNameWatch.sendUpdate</code>.

	 */

	public void nameWatchUpdate()

	{

		printStart("nameWatchUpdate");

		EPPResponse response;



		try{

			System.out.println("\nnameWatchUpdate: NameWatch update for EXAMPLE1-REP");

			nameWatch.setTransId("ABC-12345-XYZ");

            nameWatch.setRoid("EXAMPLE1-REP");

			nameWatch.setRegistrant("sh8013");

			nameWatch.setRptTo("jdoe@example.com");

			nameWatch.setFreq(EPPNameWatch.FREQ_DAILY);

            nameWatch.setAuthString("2BARfoo");

			nameWatch.addStatus(new EPPNameWatchStatus(

				EPPNameWatch.STATUS_CLIENT_HOLD, "Payment overdue"));

			nameWatch.removeStatus(new EPPNameWatchStatus(

				EPPNameWatch.STATUS_CLIENT_UPDATE_PROHIBITED));



			// Execute update

			response = nameWatch.sendUpdate();



			//-- Output all of the response attributes

		    System.out.println("nameWatchUpdate: Response = [" + response + "]\n\n");



			/**

			 * Result Set

			 */

			for (int i=0;i < response.getResults().size();i++) {



				EPPResult myResult = (EPPResult)response.getResults().elementAt(i);

				System.out.println("Result Code    : " + myResult.getCode());

				System.out.println("Result Message : " + myResult.getMessage());

				System.out.println("Result Lang    : " + myResult.getLang());



				if(myResult.isSuccess()) {

					System.out.println("Command Passed ");

				}

				else{

					System.out.println("Command Failed ");

				}



				if(myResult.getValues() != null) {



					for (int k =0; k < myResult.getValues().size(); k++) {

						System.out.println("Result Values  : " + myResult.getValues().elementAt(k));

					}

				}



			}



		}

		catch (EPPCommandException e){

			handleException(e);

		}



		printEnd("nameWatchUpdate");

	} // End EPPNameWatchTst.nameWatchUpdate()



	/**

	 *	Unit test of <code>EPPNameWatch.sendTransfer</code> for a transfer query.

	 */

	public void nameWatchTransferQuery()

	{

		printStart("nameWatchTransferQuery");



		EPPNameWatchTransferResp response;



		try

		{



			System.out.println("\nnameWatchTransferQuery: NameWatch transfer query for EXAMPLE1-REP");

			nameWatch.setTransferOpCode(EPPNameWatch.TRANSFER_QUERY);

			nameWatch.setTransId("ABC-12345-XYZ");

			nameWatch.setRoid("EXAMPLE1-REP");



			// Execute the transfer query

			response = nameWatch.sendTransfer();



			//-- Output all of the response attributes

			System.out.println("nameWatchTransferQuery: Response = [" + response + "]\n\n");



			//-- Output required response attributes using accessors

			System.out.println("nameWatchTransferQuery: roid = " + response.getRoid());

			System.out.println("nameWatchTransferQuery: transfer status = " + response.getTransferStatus());

			System.out.println("nameWatchTransferQuery: request client = " + response.getRequestClient());

            System.out.println("nameWatchTransferQuery: request date = " + response.getRequestDate());

			System.out.println("nameWatchTransferQuery: action client = " + response.getActionClient());

			System.out.println("nameWatchTransferQuery: action date = " + response.getActionDate());



			//-- Output optional response attributes using accessors

			if (response.getExpirationDate() != null)

				System.out.println("nameWatchTransferQuery: expiration date = " + response.getExpirationDate());



		}

		catch (EPPCommandException e)

		{

			handleException(e);

		}



		printEnd("nameWatchTransferQuery");



	} // End EPPNameWatchTst.nameWatchTransferQuery()



	/**

	 *	Unit test of <code>EPPNameWatch.sendTransfer</code> for a transfer query.

	 */

	public void nameWatchTransfer()

	{

		printStart("nameWatchTransfer");



		EPPNameWatchTransferResp response;



		try

		{



			System.out.println("\nnameWatchTransfer: NameWatch transfer request for EXAMPLE1-REP");

			nameWatch.setTransferOpCode(EPPNameWatch.TRANSFER_REQUEST);

			nameWatch.setTransId("ABC-12345-XYZ");

			nameWatch.setAuthString("2fooBAR");

			nameWatch.setRoid("EXAMPLE1-REP");

			nameWatch.setPeriodLength(10);



			// Execute the transfer query

			response = nameWatch.sendTransfer();



			//-- Output all of the response attributes

			System.out.println("nameWatchTransferQuery: Response = [" + response + "]\n\n");



			//-- Output required response attributes using accessors

			System.out.println("nameWatchTransferQuery: roid = " + response.getRoid());

			System.out.println("nameWatchTransferQuery: transfer status = " + response.getTransferStatus());

			System.out.println("nameWatchTransferQuery: request client = " + response.getRequestClient());

            System.out.println("nameWatchTransferQuery: request date = " + response.getRequestDate());

			System.out.println("nameWatchTransferQuery: action client = " + response.getActionClient());

			System.out.println("nameWatchTransferQuery: action date = " + response.getActionDate());



			//-- Output optional response attributes using accessors

			if (response.getExpirationDate() != null)

				System.out.println("nameWatchTransfer: expiration date = " + response.getExpirationDate());



		}

		catch (EPPCommandException e)

		{

			handleException(e);

		}





		// Transfer Cancel

		try {

			System.out.println("\n----------------------------------------------------------------");

			System.out.println("\nnameWatchTransfer: NameWatch transfer cancel for EXAMPLE1-REP");

			nameWatch.setTransferOpCode(EPPNameWatch.TRANSFER_CANCEL);

			nameWatch.setRoid("EXAMPLE1-REP");



			// Execute the transfer cancel

			response = nameWatch.sendTransfer();



			//-- Output all of the response attributes

			System.out.println("nameWatchTransfer: Response = [" + response + "]\n\n");

		}

		catch (EPPCommandException e) {

			handleException(e);

		}



		// Transfer Reject

		try {

			System.out.println("\n----------------------------------------------------------------");

			System.out.println("\nnameWatchTransfer: NameWatch transfer reject for EXAMPLE1-REP");

			nameWatch.setTransferOpCode(EPPNameWatch.TRANSFER_REJECT);

			nameWatch.setRoid("EXAMPLE1-REP");



			// Execute the transfer cancel

			response = nameWatch.sendTransfer();



			//-- Output all of the response attributes

			System.out.println("nameWatchTransfer: Response = [" + response + "]\n\n");

		}

		catch (EPPCommandException e) {

			handleException(e);

		}





		// Transfer Approve

		try {

			System.out.println("\n----------------------------------------------------------------");

			System.out.println("\nnameWatchTransfer: NameWatch transfer approve for EXAMPLE1-REP");

			nameWatch.setTransferOpCode(EPPNameWatch.TRANSFER_APPROVE);

			nameWatch.setRoid("EXAMPLE1-REP");



			// Execute the transfer cancel

			response = nameWatch.sendTransfer();



			//-- Output all of the response attributes

			System.out.println("nameWatchTransfer: Response = [" + response + "]\n\n");

		}

		catch (EPPCommandException e) {

			handleException(e);

		}





		printEnd("nameWatchTransfer");



	} // End EPPNameWatchTst.nameWatchTransfer()



	/**

	 *	Unit test of <code>EPPSession.initSession</code>.  The session attribute

	 *	is initialized with the attributes defined in the EPP sample files.

	 */

	private void initSession()

	{

		printStart("initSession");



		// Set attributes for initSession

		session.setTransId("ABC-12345-XYZ");

		session.setVersion("1.0");

		session.setLang("en");



		// Initialize the session

		try

		{

			session.initSession();

		}

		catch (EPPCommandException e)

		{

			EPPResponse response = session.getResponse();



			// Is a server specified error?

			if ((response != null) && (!response.isSuccess()))	{

				Assert.fail("Server Error : " + response);

			}else {

				e.printStackTrace();

				Assert.fail("initSession Error : " + e);

			}

		}



		printEnd("initSession");



	} // End EPPNameWatchTst.initSession()







	/**

	 *	Unit test of <code>EPPSession.endSession</code>.  The session with the

	 *	EPP Server will be terminated.

	 */

	private void endSession()

	{

		printStart("endSession");



		session.setTransId("ABC-12345-XYZ");



		// End the session

		try

		{

			session.endSession();

		}

		catch (EPPCommandException e)

		{

			EPPResponse response = session.getResponse();



			// Is a server specified error?

			if ((response != null) && (!response.isSuccess()))

			{

				Assert.fail("Server Error : " + response);

			}

			else // Other error

			{

				e.printStackTrace();

				Assert.fail("initSession Error : " + e);

			}

		}





		printEnd("endSession");



	} // End EPPNameWatchTst.endSession()





	/**

	 *	JUNIT <code>setUp</code> method, which sets the default client Id to "theRegistrar".

	 */

	protected void setUp() {



		try {



			session  = new EPPSession();

			session.setClientID(Environment.getProperty("EPP.Test.clientId", "ClientX"));
			session.setPassword(Environment.getProperty("EPP.Test.password", "foo-BAR2"));



		}

		catch (Exception e) {



			e.printStackTrace();

			Assert.fail("Error initializing the session: " + e);

		}



		initSession();

                //System.out.println("out init");

		nameWatch = new EPPNameWatch(session);



	} // End EPPNameWatchTst.setUp();





	/**

	 *	JUNIT <code>tearDown</code>, which currently does nothing.

	 */

	protected void tearDown(){

		endSession();

	} // End EPPNameWatchTst.tearDown();







	/**

	 *	JUNIT <code>suite</code> static method, which returns the tests

	 *	associated with <code>EPPNameWatchTst</code>.

	 */

	public static Test suite(){

		TestSuite suite = new TestSuite(EPPNameWatchTst.class);

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

	} // End EPPNameWatchTst.suite()



	/**

	 *	Handle an <code>EPPCommandException</code>, which can be either

	 * 	a server generated error or a general exception.  If the exception

	 *	was caused by a server error, "Server Error : <Response XML>" will be specified.

	 *	If the exception was caused by a general

	 *	algorithm error, "General Error : <Exception Description>" will

	 *	be specified.

	 *

	 *	@param	aException Exception thrown during test

	 */

	public void handleException(Exception aException){
		EPPResponse theResponse = null;
		if (aException instanceof EPPCommandException) {
			theResponse = ((EPPCommandException) aException).getResponse();
		}
		
		aException.printStackTrace();

		// Is a server specified error?
		if ((theResponse != null) && (!theResponse.isSuccess())) {
			Assert.fail("Server Error : " + theResponse);
		}

		else {
			Assert.fail("General Error : " + aException);
		}
	} // End EPPNameWatchTst.handleException(EPPCommandException)





	/**

	 *	Unit test main, which accepts the following system property options:<br>

	 *	<ul>

	 *	<li>iterations	Number of unit test iterations to run

	 *	<li>validate	Turn XML validation on (<code>true</code>) or off (<code>false</code>).

	 *					If validate is not specified, validation will be off.

	 *	</ul>

	 */

	public static void main(String args[]){

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

				TestThread thread = new TestThread("EPPSessionTst Thread " + i, EPPNameWatchTst.suite());

				thread.start();

			}

		}

	    else // Single threaded mode.

			junit.textui.TestRunner.run(EPPNameWatchTst.suite());



		try {

			app.endApplication();

		}

		catch (EPPCommandException e) {

			e.printStackTrace();

				Assert.fail("Error ending the EPP Application: " + e);

		}



    } // End EPPNameWatchTst.main(String [])





	/**

	 *	Print the start of a test with the <code>Thread</code> name if the current

	 *	thread is a <code>TestThread</code>.

	 *

	 *	@param	Logical name for the test

	 */

	private void printStart(String aTest)

	{

		if (Thread.currentThread() instanceof TestThread) {

			System.out.print(Thread.currentThread().getName() + ", iteration " + iteration + ": ");

			cat.info(Thread.currentThread().getName() + ", iteration " + iteration + ": " + aTest + " Start");

		}



		System.out.println("Start of " + aTest);

		System.out.println("****************************************************************\n");

	} // End EPPNameWatchTst.testStart(String)





	/**

	 *	Print the end of a test with the <code>Thread</code> name if the current

	 *	thread is a <code>TestThread</code>.

	 *

	 *	@param	Logical name for the test

	 */

	private void printEnd(String aTest)

	{

		System.out.println("****************************************************************");



		if (Thread.currentThread() instanceof TestThread) {

			System.out.print(Thread.currentThread().getName() + ", iteration " + iteration + ": ");

			cat.info(Thread.currentThread().getName() + ", iteration " + iteration + ": " + aTest + " End");

		}



		System.out.println("End of " + aTest);

		System.out.println("\n");

	} // End EPPNameWatchTst.testEnd(String)





	/**

	 * Print message

	 *

	 *	@param	aMsg message to print

	 */

	private void printMsg(String aMsg)

	{

		if (Thread.currentThread() instanceof TestThread) {

			System.out.println(Thread.currentThread().getName() + ", iteration " + iteration + ": " + aMsg);

			cat.info(Thread.currentThread().getName() + ", iteration " + iteration + ": " + aMsg);

		}

		else {

			System.out.println(aMsg);

			cat.info(aMsg);

		}



	} // End EPPNameWatchTst.printMsg(String)





	/**

	 * Print error message

	 *

	 *	@param	aMsg errpr message to print

	 */

	private void printError(String aMsg)

	{

		if (Thread.currentThread() instanceof TestThread) {

			System.err.println(Thread.currentThread().getName() + ", iteration " + iteration + ": " + aMsg);

			cat.error(Thread.currentThread().getName() + ", iteration " + iteration + ": " + aMsg);

		}

		else {

			System.err.println(aMsg);

			cat.error(aMsg);

		}



	} // End EPPNameWatchTst.printError(String)





	/**

	 *	EPP NameWatch associated with test

	 */

	private EPPNameWatch nameWatch = null;



	/**

	 *	EPP Session associated with test

	 */

	private EPPSession session = null;



	/**

	 *	Connection to the EPP Server.

	 */

	private EPPClientCon connection = null;



	/**

	 *	Current test iteration

	 */

	private int iteration = 0;



	/**

	 *	Handle to the Singleton EPP Application instance (<code>EPPApplicationSingle</code>)

	 */

	private static EPPApplicationSingle app = EPPApplicationSingle.getInstance();



	/**

	 *	Name of configuration file to use for test (default = epp.config).

	 */

	private static String configFileName = "epp.config";



	/**

	 * Logging category

	 */

    private static final Logger cat = Logger.getLogger(EPPNameWatchTst.class.getName(),

													   EPPCatFactory.getInstance().getFactory());





} // End class EPPNameWatchTst


/**************************************************************************
 *                                                                        *
 * The information in this document is proprietary to VeriSign and the    *
 * VeriSign Registry Business. It may not be used, reproduced or          *
 * disclosed without the written approval of the General Manager of       *
 * VeriSign Information Services.                                         *
 *                                                                        *
 * PRIVILEGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION           *
 * (REGISTRY SENSITIVE INFORMATION)                                       *
 *                                                                        *
 * Copyright (c) 2010 VeriSign, Inc.  All rights reserved.                *
 *                                                                        *
 *************************************************************************/

package com.verisign.epp.serverstub;

import java.util.Calendar;
import java.util.Date;

import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.whowas.EPPWhoWasConstants;
import com.verisign.epp.codec.whowas.EPPWhoWasHistory;
import com.verisign.epp.codec.whowas.EPPWhoWasInfoCmd;
import com.verisign.epp.codec.whowas.EPPWhoWasInfoResp;
import com.verisign.epp.codec.whowas.EPPWhoWasRecord;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;
import com.verisign.epp.framework.EPPHandleEventException;
import com.verisign.epp.framework.EPPWhoWasHandler;

/**
 * This class is a concrete implementation of the abstract
 * {@link EPPWhoWasHandler} class. It defines the Server's response to all
 * received EPP WhoWas Commands. <br>
 * <br>
 * 
 * @author Deepak Deshpande
 * @version 1.0 Mar 26, 2010
 */
public class WhoWasHandler extends EPPWhoWasHandler {

	/**
	 * The server transaction id used for all responses
	 */
	private static final String svrTransId = "SRV-43659";

	/** Test Data used to return canned responses. */

	/** Test data to return CREATE operation record for info by name. */
	private static final String CREATE_OPERATION_ENTITY_TEST_NAME =
			"create-operation-history.com";

	/** Test data to return CREATE operation record for info by roid. */
	private static final String CREATE_OPERATION_ENTITY_TEST_ROID = "create-rep";

	/** Test data to return all possible operation records for info by name. */
	private static final String ALL_OPERATION_ENTITY_TEST_NAME =
			"all-operation-history.com";

	/** Test data to return all possible operation records for info by roid. */
	private static final String ALL_OPERATION_ENTITY_TEST_ROID = "all-rep";

	/**
	 * Test data to return <code>EPPResult.OBJECT_DOES_NOT_EXIST</code> response
	 * for info by name.
	 */
	private static final String NON_EXISTING_ENTITY_TEST_NAME =
			"non-existing.com";

	/**
	 * Test data to return <code>EPPResult.OBJECT_DOES_NOT_EXIST</code> response
	 * for info by roid.
	 */
	private static final String NON_EXISTING_ENTITY_TEST_ROID = "non-existing";

	/** Default entity name to use in record object. */
	private static final String DEFAULT_ENTITY_NAME = "abc.com";

	/** Default entity roid to use in record object. */
	private static final String DEFAULT_ENTITY_ROID = "EXAMPLE1-REP";


	/**
	 * Default constructor
	 */
	public WhoWasHandler () {
		// default constructor
	}


	/**
	 * Verifies that passed in <code>aSessionData</code> has valid logged in
	 * state, otherwise throws {@link EPPHandleEventException}.
	 * 
	 * @param aEvent
	 *        The {@link EPPEvent} that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        {@link EPPWhoWasHandler}
	 * @exception Thrown
	 *            if an error condition occurs. It must contain an
	 *            {@link EPPEventResponse}
	 */
	protected void preHandleEvent ( EPPEvent aEvent, Object aData )
			throws EPPHandleEventException {

		SessionData sessionData = (SessionData) aData;
		EPPCommand theMessage = (EPPCommand) aEvent.getMessage();

		if ( !sessionData.isLoggedIn() ) {
			/**
			 * Create the transId for the response with the client trans id and the
			 * server trans id.
			 */
			EPPTransId transId = new EPPTransId( theMessage.getTransId(), svrTransId );

			// Create the Response (Standard EPPResponse)
			EPPResponse theResponse = new EPPResponse( transId );

			theResponse.setResult( EPPResult.COMMAND_USE_ERROR );
			throw new EPPHandleEventException(
					"The client has not established a session", theResponse );
		}
	}


	/**
	 * Returns the {@link EPPEventResponse} back to the client after processing
	 * the passed in <code>aEvent</code>. Invoked when a WhoWas Info command is
	 * received. Subclasses should define the behavior when a WhoWas Info command
	 * is received.<br>
	 * The following is the handling by input name or roid:<br>
	 * <ul>
	 * <li>Name <code>create-operation-history.com</code> or Roid
	 * <code>create-rep</code> - Return history with CREATE operation record.
	 * <li>Name <code>all-operation-history.com.com</code> or Roid
	 * <code>all-rep</code> - Return history with list of all possible operation
	 * records.
	 * <li>Name <code>non-existing.com</code> or Roid <code>non-existing</code> -
	 * Return <code>EPPResult.OBJECT_DOES_NOT_EXIST</code> response.
	 * <li>default - Return history with CREATE operation record.
	 * </ul>
	 * 
	 * @param aEvent
	 *        The {@link EPPEvent} that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        {@link EPPWhoWasHandler}
	 * @return the {@link EPPEventResponse} back to the client after processing
	 *         the passed in <code>aEvent</code>.
	 */
	protected EPPEventResponse doWhoWasInfo ( EPPEvent aEvent, Object aData ) {
		EPPWhoWasInfoCmd theMessage = (EPPWhoWasInfoCmd) aEvent.getMessage();

		EPPTransId theTransId =
				new EPPTransId( theMessage.getTransId(), svrTransId );

		EPPWhoWasInfoResp theResponse =
				new EPPWhoWasInfoResp( theTransId, theMessage.getWhowasType() );
		theResponse.setResult( EPPResult.SUCCESS );

		String name = theMessage.getName();
		String roid = theMessage.getRoid();

		theResponse.setName( name );
		theResponse.setRoid( roid );

		EPPWhoWasHistory history = new EPPWhoWasHistory();

		// check for canned name and roid and return response accordingly
		if ( CREATE_OPERATION_ENTITY_TEST_NAME.equalsIgnoreCase( name )
				|| CREATE_OPERATION_ENTITY_TEST_ROID.equalsIgnoreCase( roid ) ) {

			if ( name != null ) {
				roid = CREATE_OPERATION_ENTITY_TEST_ROID;
			}
			else {
				name = CREATE_OPERATION_ENTITY_TEST_NAME;
			}

			// create record with create operation
			EPPWhoWasRecord createRecord =
					new EPPWhoWasRecord( name, roid, new Date(),
							EPPWhoWasConstants.OP_CREATE, "ClientX", "Client X Corp" );

			history.addRecord( createRecord );
		}
		else if ( ALL_OPERATION_ENTITY_TEST_NAME.equalsIgnoreCase( name )
				|| ALL_OPERATION_ENTITY_TEST_ROID.equalsIgnoreCase( roid ) ) {

			history = getAllOperationsHistory( name, roid );
		}
		else if ( NON_EXISTING_ENTITY_TEST_NAME.equalsIgnoreCase( name )
				|| NON_EXISTING_ENTITY_TEST_ROID.equalsIgnoreCase( roid ) ) {

			// return non existing response back
			EPPResponse eppResponse = new EPPResponse( theTransId );
			eppResponse.setResult( EPPResult.OBJECT_DOES_NOT_EXIST );

			return new EPPEventResponse( eppResponse );
		}
		else { // return default response
			if ( name != null ) {
				roid = DEFAULT_ENTITY_ROID;
			}
			else {
				name = DEFAULT_ENTITY_NAME;
			}

			// create record with create operation
			EPPWhoWasRecord createRecord =
					new EPPWhoWasRecord( name, roid, new Date(),
							EPPWhoWasConstants.OP_CREATE, "ClientX", "Client X Corp" );

			history.addRecord( createRecord );
		}

		// set the history created with records in the WhoWas response
		theResponse.setHistory( history );

		return new EPPEventResponse( theResponse );
	}


	/**
	 * Returns {@link EPPWhoWasHistory} set with list of {@link EPPWhoWasRecord}
	 * for all possible operations that can be returned by the server.
	 * 
	 * @param aName
	 * @param aRoid
	 * @return
	 */
	private EPPWhoWasHistory getAllOperationsHistory ( String aName, String aRoid ) {

		EPPWhoWasHistory history = new EPPWhoWasHistory();

		Calendar transactionDate = Calendar.getInstance();
		transactionDate.setTime( new Date() );
		transactionDate.add( Calendar.MONTH, -12 );

		if ( aName != null ) {
			aRoid = ALL_OPERATION_ENTITY_TEST_ROID;
		}
		else {
			aName = ALL_OPERATION_ENTITY_TEST_NAME;
		}

		// create record with create operation
		EPPWhoWasRecord createRecord =
				new EPPWhoWasRecord( aName, aRoid, transactionDate.getTime(),
						EPPWhoWasConstants.OP_CREATE, "ClientX", "Client X Corp" );

		// add 1 month to create transaction date
		transactionDate.add( Calendar.MONTH, 1 );

		// create record with create operation
		EPPWhoWasRecord deleteRecord =
				new EPPWhoWasRecord( aName, aRoid, transactionDate.getTime(),
						EPPWhoWasConstants.OP_DELETE, "ClientX", "Client X Corp" );

		// add 1 month to create transaction date
		transactionDate.add( Calendar.MONTH, 1 );

		// create record with create operation
		EPPWhoWasRecord newCreateRecord =
				new EPPWhoWasRecord( aName, aRoid, transactionDate.getTime(),
						EPPWhoWasConstants.OP_CREATE, "ClientX", "Client X Corp" );

		// add 1 month to create transaction date
		transactionDate.add( Calendar.MONTH, 1 );

		// create record for transfer operation
		EPPWhoWasRecord transferRecord =
				new EPPWhoWasRecord( aName, aRoid, transactionDate.getTime(),
						EPPWhoWasConstants.OP_TRANSFER, "ClientY", "Client Y Corp" );

		// add 1 month to create transaction date
		transactionDate.add( Calendar.MONTH, 1 );

		// create record for transfer operation
		EPPWhoWasRecord serverTransferRecord =
				new EPPWhoWasRecord( aName, aRoid, transactionDate.getTime(),
						EPPWhoWasConstants.OP_SERVER_TRANSFER, "ClientX", "Client X Corp" );

		// add 1 month to create transaction date
		transactionDate.add( Calendar.MONTH, 1 );

		// add servertransfer record to the list
		history.addRecord( serverTransferRecord );

		// add transfer record to the list
		history.addRecord( transferRecord );

		// add create record to the list
		history.addRecord( newCreateRecord );

		// add delete record to the list
		history.addRecord( deleteRecord );

		// add create record to the list
		history.addRecord( createRecord );

		return history;
	}
}

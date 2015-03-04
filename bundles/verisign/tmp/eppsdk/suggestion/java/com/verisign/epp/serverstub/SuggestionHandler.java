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
package com.verisign.epp.serverstub;

import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.suggestion.EPPSuggestionAnswer;
import com.verisign.epp.codec.suggestion.EPPSuggestionCell;
import com.verisign.epp.codec.suggestion.EPPSuggestionGrid;
import com.verisign.epp.codec.suggestion.EPPSuggestionInfoCmd;
import com.verisign.epp.codec.suggestion.EPPSuggestionInfoResp;
import com.verisign.epp.codec.suggestion.EPPSuggestionRecord;
import com.verisign.epp.codec.suggestion.EPPSuggestionRow;
import com.verisign.epp.codec.suggestion.EPPSuggestionTable;
import com.verisign.epp.codec.suggestion.util.InvalidValueException;
import com.verisign.epp.codec.suggestion.util.RandomHelper;
import com.verisign.epp.codec.suggestion.util.StatusEnum;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;
import com.verisign.epp.framework.EPPHandleEventException;
import com.verisign.epp.framework.EPPSuggestionHandler;

public class SuggestionHandler extends EPPSuggestionHandler {

	/**
	 * The server transaction id used for all responses
	 */
	private static final String svrTransId = "SRV-43659";

	/**
	 * Constructs an instance of SuggestionHandler
	 */
	public SuggestionHandler() {}

	/**
	 * Handles any common behavior that all Suggestion commands need to execute
	 * before they execute their command specific behavior.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPSuggestionHandler</code> This is assumed to be an
	 *            instance of SessionData here.
	 * @exception EPPHandleEventException
	 *                Thrown if an error condition occurs. It must contain an
	 *                <code>EPPEventResponse</code>
	 */
	protected void preHandleEvent( final EPPEvent aEvent, final Object aData)
			throws EPPHandleEventException {
		SessionData sessionData = (SessionData) aData;
		EPPCommand theMessage = (EPPCommand) aEvent.getMessage();

		if (!sessionData.isLoggedIn()) {
			/**
			 * Create the transId for the response with the client trans id and
			 * the server trans id.
			 */
			EPPTransId transId = new EPPTransId(theMessage.getTransId(), svrTransId);

			// Create the Response (Standard EPPResponse)
			EPPResponse theResponse = new EPPResponse(transId);

			theResponse.setResult(EPPResult.COMMAND_USE_ERROR);
			throw new EPPHandleEventException(
					"The client has not established a session", theResponse);
		}
	}

	/**
	 * Handles any common behavior that all Suggestion commands need to execute
	 * after they execute their command specific behavior.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPSuggestionHandler</code>
	 * @exception EPPHandleEventException
	 *                Thrown if an error condition occurs. It must contain an
	 *                <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent( final EPPEvent aEvent, final Object aData)
			throws EPPHandleEventException {}

	/**
	 * Invoked when a Suggestion Info command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPSuggestiondHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doSuggestionInfo( final EPPEvent aEvent, final Object aData) {
		EPPSuggestionInfoCmd theMessage = (EPPSuggestionInfoCmd) aEvent.getMessage();

		EPPTransId theTransId = new EPPTransId(theMessage.getTransId(), svrTransId);

		EPPSuggestionInfoResp theResponse = new EPPSuggestionInfoResp(theTransId,
				theMessage.getKey());
		theResponse.setResult(EPPResult.SUCCESS);

		// Use random response?
		if (theMessage.getKey().equalsIgnoreCase("RANDOMIZE")) {
			try {
				theResponse = (EPPSuggestionInfoResp) RandomHelper
						.getResponse(theMessage.getTransId());
			} catch (InvalidValueException e) {
				e.printStackTrace();
			}
		} // Use table view?
		else if (theMessage.getFilter() == null || theMessage.getFilter().isTable()) {

			EPPSuggestionTable theTable = new EPPSuggestionTable();

			try {
				theTable.addRow(new EPPSuggestionRow("SOCCERTEAM.COM", (short) 1000,
						StatusEnum.STATUS_REGISTERED));
				theTable.addRow(new EPPSuggestionRow("SOCCERTEAM.CC", (short) 930,
						StatusEnum.STATUS_AVAILABLE));
				theTable.addRow(new EPPSuggestionRow("SOCCERGROUP.NET", (short) 720,
						StatusEnum.STATUS_AVAILABLE));
				theTable.addRow(new EPPSuggestionRow("FOOTBALLGROUP.NET", (short) 710,
						StatusEnum.STATUS_AVAILABLE));
				theTable.addRow(new EPPSuggestionRow("SOCCERSPIRIT.NET", (short) 710,
						StatusEnum.STATUS_AVAILABLE));
				theTable.addRow(new EPPSuggestionRow("SOCCERSQUAD.NET", (short) 700,
						StatusEnum.STATUS_AVAILABLE));
				theTable.addRow(new EPPSuggestionRow("THESOCCERTEAM.NET", (short) 690,
						StatusEnum.STATUS_AVAILABLE));
				theTable.addRow(new EPPSuggestionRow("SOCCERTEAMUP.COM", (short) 680,
						StatusEnum.STATUS_AVAILABLE));
				theTable.addRow(new EPPSuggestionRow("FOOTBALLTEAMUP.COM", (short) 670,
						StatusEnum.STATUS_AVAILABLE));
				theTable.addRow(new EPPSuggestionRow("XN--QUIPEDEFOOTBALL-9MB.COM", (short) 670,
						StatusEnum.STATUS_AVAILABLE, "équipedefootball.com"));
				theTable.addRow(new EPPSuggestionRow("SOCCERGANG.COM", (short) 660,
						StatusEnum.STATUS_AVAILABLE));
				theTable.addRow(new EPPSuggestionRow("SOCCERWORK.COM", (short) 660,
						StatusEnum.STATUS_AVAILABLE));
				theTable.addRow(new EPPSuggestionRow("SOCCERTEAMUP.NET", (short) 660,
						StatusEnum.STATUS_UNKNOWN));
				theTable.addRow(new EPPSuggestionRow("SOCCERTEAMS.CC", (short) 660,
						StatusEnum.STATUS_FORSALE));
				theTable.addRow(new EPPSuggestionRow("ST.COM", (short) 650,
						StatusEnum.STATUS_RESTRICTED));
			} catch (InvalidValueException e) {
				e.printStackTrace();
			}
			theResponse.setAnswer(new EPPSuggestionAnswer(theTable));

		} // Use grid view
		else {
			EPPSuggestionGrid theGrid = new EPPSuggestionGrid();
			EPPSuggestionRecord theRecord = null;

			try {
				theRecord = new EPPSuggestionRecord("SOCCERTEAM");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 1000,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 975,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 966,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("SOCCERGROUP");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 675,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 702,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 640,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("FOOTBALLGROUP");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 667,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 694,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 632,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("SOCCERSPIRIT");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 667,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 694,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 632,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("THESOCCERTEAM");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 665,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 693,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 630,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("SOCCERSQUAD");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 659,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 687,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 624,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("BALLWORK");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 651,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 679,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 617,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("TEAMUNIT");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 651,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 679,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 617,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("FOOTBALLTEAMS");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 711,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 686,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 677,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("SOCCERTEAMUP");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 664,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 639,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 614,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("TEAMGANG");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 636,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 664,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 601,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("SOCCERWORK");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 695,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 670,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 662,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("FOOTBALLTEAMUP");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 656,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 632,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 607,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("XN--QUIPEDEFOOTBALL-9MB");
				theRecord.setUName( "équipedefootball" );
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 656,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 632,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 607,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("SOCCERUNIT");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 656,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 632,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 607,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("RUGBYPOOL");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 628,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 656,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 593,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("FREESOCCERTEAM");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 655,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 630,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 605,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("SOCCERTEAMONLINE");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 655,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 630,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 605,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("WEBSOCCERTEAM");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 655,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 630,
						StatusEnum.STATUS_AVAILABLE));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 605,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);

				theRecord = new EPPSuggestionRecord("ST");
				theRecord.addCell(new EPPSuggestionCell("COM", (short) 686,
						StatusEnum.STATUS_RESTRICTED));
				theRecord.addCell(new EPPSuggestionCell("NET", (short) 661,
						StatusEnum.STATUS_REGISTERED));
				theRecord.addCell(new EPPSuggestionCell("TV", (short) 652,
						StatusEnum.STATUS_AVAILABLE));
				theGrid.addRecord(theRecord);
			} catch (InvalidValueException e) {
				e.printStackTrace();
			}
			theResponse.setAnswer(new EPPSuggestionAnswer(theGrid));			
		}

        theResponse.setLanguage(theMessage.getLanguage());
        
		return new EPPEventResponse(theResponse);
	}
}

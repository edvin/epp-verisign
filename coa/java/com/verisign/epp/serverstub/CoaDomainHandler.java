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

package com.verisign.epp.serverstub;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.coaext.EPPCoaExtAttr;
import com.verisign.epp.codec.coaext.EPPCoaExtCreate;
import com.verisign.epp.codec.coaext.EPPCoaExtInfData;
import com.verisign.epp.codec.coaext.EPPCoaExtKey;
import com.verisign.epp.codec.coaext.EPPCoaExtUpdate;
import com.verisign.epp.codec.coaext.EPPCoaExtValue;
import com.verisign.epp.codec.domain.EPPDomainCheckCmd;
import com.verisign.epp.codec.domain.EPPDomainCheckResp;
import com.verisign.epp.codec.domain.EPPDomainCheckResult;
import com.verisign.epp.codec.domain.EPPDomainContact;
import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainCreateResp;
import com.verisign.epp.codec.domain.EPPDomainDeleteCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainMapFactory;
import com.verisign.epp.codec.domain.EPPDomainRenewCmd;
import com.verisign.epp.codec.domain.EPPDomainRenewResp;
import com.verisign.epp.codec.domain.EPPDomainStatus;
import com.verisign.epp.codec.domain.EPPDomainTransferCmd;
import com.verisign.epp.codec.domain.EPPDomainTransferResp;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.framework.EPPDomainHandler;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;
import com.verisign.epp.framework.EPPHandleEventException;
import com.verisign.epp.framework.EPPPollQueueException;
import com.verisign.epp.framework.EPPPollQueueMgr;
import com.verisign.epp.util.EPPCatFactory;

/**
 * The <code>CoaDomainHandler</code> class is a concrete implementation of the
 * abstract <code>com.verisign.epp.framework.EPPDomainHandler</code> class. It
 * defines the Server's response to all received EPP Host Commands.
 * 
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class CoaDomainHandler extends EPPDomainHandler {

	/** sample server trans id */
	private static final String svrTransId = "54322-XYZ";

	/** sample server roid */
	private static final String roid = "NS1EXAMPLE1-VRSN";

	/** Log4j category for logging */
	private static Logger cat =
			Logger.getLogger( CoaDomainHandler.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );


	/**
	 * Constructs an instance of CoaDomainHandler
	 */
	public CoaDomainHandler () {
	}


	/**
	 * Handles any common behavior that all Domain commands need to execute before
	 * they execute their command specific behavior.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>CoaDomainHandler</code> This is assumed to be an instance of
	 *        SessionData here.
	 * @exception EPPHandleEventException
	 *            Thrown if an error condition occurs. It must contain an
	 *            <code>EPPEventResponse</code>
	 */
	protected void preHandleEvent ( EPPEvent aEvent, Object aData )
			throws EPPHandleEventException {
		SessionData sessionData = (SessionData) aData;
		EPPCommand theMessage = (EPPCommand) aEvent.getMessage();

		if ( !sessionData.isLoggedIn() ) {
			/**
			 * The client isn't logged in so they can't successfully invoke a command.
			 * Sending COMMAND_FAILED_END
			 */
			/**
			 * Create the transId for the response with the client trans id and the
			 * server trans id.
			 */
			EPPTransId transId = new EPPTransId( theMessage.getTransId(), svrTransId );

			// Create the Response (Standard EPPResponse)
			EPPResponse theResponse = new EPPResponse( transId );

			theResponse.setResult( EPPResult.COMMAND_FAILED_END );
			throw new EPPHandleEventException(
					"The client has not established a session", theResponse );
		}
	}


	/**
	 * Handles any common behavior that all Domain commands need to execute after
	 * they execute their command specific behavior.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>CoaDomainHandler</code>
	 * @exception EPPHandleEventException
	 *            Thrown if an error condition occurs. It must contain an
	 *            <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent ( EPPEvent aEvent, Object aData )
			throws EPPHandleEventException {
	}


	/**
	 * Invoked when a Domain Check command is received.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>EPPDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainCheck ( EPPEvent aEvent, Object aData ) {
		EPPDomainCheckCmd theMessage = (EPPDomainCheckCmd) aEvent.getMessage();
		EPPResponse theResponse;

		// This is just a vector of strings representing Domain Names
		Vector vNames = theMessage.getNames();
		Enumeration eNames = vNames.elements();

		Vector vResults = new Vector();

		// create a Vector of Ping Results.
		boolean known = true;

		while ( eNames.hasMoreElements() ) {
			String domainName = (String) eNames.nextElement();
			known = !known;
			vResults.addElement( new EPPDomainCheckResult( domainName, known ) );
		}

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId = new EPPTransId( theMessage.getTransId(), svrTransId );

		// Response for many domain names
		theResponse = new EPPDomainCheckResp( transId, vResults );
		theResponse.setResult( EPPResult.SUCCESS );

		return new EPPEventResponse( theResponse );
	}


	/**
	 * Invoked when a Domain Renew command is received.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>EPPDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainRenew ( EPPEvent aEvent, Object aData ) {
		EPPDomainRenewCmd theMessage = (EPPDomainRenewCmd) aEvent.getMessage();
		EPPResponse theResponse;

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId = new EPPTransId( theMessage.getTransId(), svrTransId );

		// Test with transId, domain name, roid, and expiration date
		theResponse =
				new EPPDomainRenewResp( transId, theMessage.getName(), new Date() );

		theResponse.setResult( EPPResult.SUCCESS );

		return new EPPEventResponse( theResponse );
	}


	/**
	 * Invoked when a Domain Delete command is received.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>EPPDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainDelete ( EPPEvent aEvent, Object aData ) {
		EPPDomainDeleteCmd theMessage = (EPPDomainDeleteCmd) aEvent.getMessage();

		// Test with transId, domain name, and expiration date

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId = new EPPTransId( theMessage.getTransId(), svrTransId );

		EPPResponse theResponse =
				new EPPDomainCreateResp( transId, theMessage.getName(), new Date() );
		theResponse.setResult( EPPResult.SUCCESS );

		return new EPPEventResponse( theResponse );
	}


	/**
	 * Invoked when a Domain Create command is received.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>EPPDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainCreate ( EPPEvent aEvent, Object aData ) {
		EPPDomainCreateCmd theMessage = (EPPDomainCreateCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId = new EPPTransId( theMessage.getTransId(), svrTransId );
		EPPResponse theResponse =
				new EPPDomainCreateResp( transId, theMessage.getName(), new Date() );

		// Get the coa:create
		EPPCoaExtCreate create =
				(EPPCoaExtCreate) theMessage.getExtension( EPPCoaExtCreate.class );

		// Print the key/value pairs, if present.
		if ( create != null ) {
			// The requirement that there be one or more attributes is handled by the
			// xsd.
			List attrList = create.getAttrs();
			for ( Iterator iterator = attrList.iterator(); iterator.hasNext(); ) {
				Object attrObject = iterator.next();
				if ( attrObject != null ) {
					EPPCoaExtAttr attr = (EPPCoaExtAttr) attrObject;
					String key = attr.getKey().getKey();
					String value = attr.getValue().getValue();

					// The requirement that the key and value not be null or blank is
					// handled by the xsd.
					// Just log what came in for informational purposes.
					cat.info( "Client Object Attribute to create: key='" + key
							+ "', value='" + value + "'" );
				}
			}
		}

		theResponse.setResult( EPPResult.SUCCESS );

		return new EPPEventResponse( theResponse );
	}


	/**
	 * Invoked when a Domain Transfer command is received.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>EPPDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainTransfer ( EPPEvent aEvent, Object aData ) {
		EPPDomainTransferCmd theMessage =
				(EPPDomainTransferCmd) aEvent.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId = new EPPTransId( theMessage.getTransId(), svrTransId );

		// Is a transfer request?
		if ( theMessage.getOp().equals( EPPCommand.OP_REQUEST ) ) {
			// Insert transfer response in poll queue
			EPPDomainTransferResp thePollMsg = new EPPDomainTransferResp();

			thePollMsg.setName( "example" );
			thePollMsg.setTransferStatus( EPPResponse.TRANSFER_PENDING );
			thePollMsg.setRequestClient( "ClientX" );
			thePollMsg.setRequestDate( new Date() );
			thePollMsg.setActionClient( "ClientY" );
			thePollMsg.setActionDate( new Date() );
			thePollMsg.setExpirationDate( new Date() );
			thePollMsg.setResult( EPPResult.SUCCESS );

			try {
				EPPPollQueueMgr.getInstance().put( null, EPPDomainMapFactory.NS,
						thePollMsg, null );
			}
			catch ( EPPPollQueueException ex ) {
				cat.error( "doDomainTransfer: Error putting message [" + thePollMsg
						+ "]" );

				EPPResult theResult = new EPPResult( EPPResult.COMMAND_FAILED );
				EPPResponse theResponse = new EPPResponse( transId, theResult );

				return new EPPEventResponse( theResponse );
			}
		}

		EPPDomainTransferResp theResponse =
				new EPPDomainTransferResp( transId, theMessage.getName() );
		theResponse.setTransferStatus( EPPResponse.TRANSFER_PENDING );

		theResponse.setRequestClient( "ClientX" );
		theResponse.setRequestDate( new Date() );

		theResponse.setActionClient( "ClientY" );
		theResponse.setActionDate( new Date() );

		/**
		 * The Expiration date is optional
		 */
		theResponse.setExpirationDate( new Date() );

		theResponse.setResult( EPPResult.SUCCESS );

		return new EPPEventResponse( theResponse );
	}


	/**
	 * Invoked when a Domain Update command is received.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>EPPDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainUpdate ( EPPEvent aEvent, Object aData ) {

		EPPDomainUpdateCmd theMessage = (EPPDomainUpdateCmd) aEvent.getMessage();

		EPPResponse theResponse = null;

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId = new EPPTransId( theMessage.getTransId(), svrTransId );

		EPPCoaExtUpdate update =
				(EPPCoaExtUpdate) theMessage.getExtension( EPPCoaExtUpdate.class );
		List attrsToAdd = update.getPutAttrs();
		List attrsToRem = update.getRemAttrs();

		if ( attrsToRem != null ) {
			for ( Iterator iterator = attrsToRem.iterator(); iterator.hasNext(); ) {
				Object keyObject = iterator.next();
				if ( keyObject != null ) {
					EPPCoaExtKey key = (EPPCoaExtKey) keyObject;
					cat.info( "Client Object Attribute to remove: key='" + key.getKey()
							+ "'" );
				}
			}
		}

		if ( attrsToAdd != null ) {
			for ( Iterator iterator = attrsToAdd.iterator(); iterator.hasNext(); ) {
				Object attrObject = iterator.next();
				if ( attrObject != null ) {
					EPPCoaExtAttr attr = (EPPCoaExtAttr) attrObject;
					String key = attr.getKey().getKey();
					String value = attr.getValue().getValue();
					cat.info( "Client Object Attribute to add: key='" + key
							+ "', value='" + value + "'" );
				}
			}
		}

		theResponse = new EPPResponse( transId );
		theResponse.setResult( EPPResult.SUCCESS );

		return new EPPEventResponse( theResponse );
	}


	/**
	 * Invoked when a Domain Info command is received.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>EPPDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainInfo ( EPPEvent aEvent, Object aData ) {
		EPPDomainInfoCmd theMessage = (EPPDomainInfoCmd) aEvent.getMessage();

		// EPPDomainInfo Response requires a vector of status
		Vector statuses = new Vector();
		statuses.addElement( new EPPDomainStatus( EPPDomainStatus.ELM_STATUS_OK ) );
		statuses.addElement( new EPPDomainStatus(
				EPPDomainStatus.ELM_STATUS_CLIENT_HOLD ) );

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId = new EPPTransId( theMessage.getTransId(), svrTransId );

		// Required EPPDomainInfoResp attributes.
		// trans id, domain name, roid, client id, statuses, created by id,
		// expiration date, Auth Info
		EPPDomainInfoResp theResponse =
				new EPPDomainInfoResp( transId, roid, "example.com", "ClientX",
						statuses, "ClientY", new Date(), new EPPAuthInfo( "2fooBAR" ) );

		// Add Name Servers
		Vector theNses = new Vector();
		theNses.addElement( "ns1.example.com" );
		theNses.addElement( "ns2.example.com" );
		theResponse.setNses( theNses );

		// Add Subordinate Hosts
		Vector theHosts = new Vector();
		theHosts.addElement( "ns1.example.com" );
		theHosts.addElement( "ns2.example.com" );
		theResponse.setHosts( theHosts );

		// Add Contacts
		Vector theContacts = new Vector();
		theContacts.addElement( new EPPDomainContact( "sh8013",
				EPPDomainContact.TYPE_ADMINISTRATIVE ) );
		theContacts.addElement( new EPPDomainContact( "sh8013",
				EPPDomainContact.TYPE_BILLING ) );
		theContacts.addElement( new EPPDomainContact( "sh8013",
				EPPDomainContact.TYPE_TECHNICAL ) );
		theResponse.setContacts( theContacts );

		// Set the expiration date to today plus one year
		GregorianCalendar theCal = new GregorianCalendar();
		theCal.setTime( new Date() );
		theCal.add( Calendar.YEAR, 1 );

		theResponse.setExpirationDate( theCal.getTime() );

		// Set the Registrant
		theResponse.setRegistrant( "jd1234" );

		EPPCoaExtKey key = new EPPCoaExtKey( "KEY1" );
		EPPCoaExtValue value = new EPPCoaExtValue( "value1" );
		EPPCoaExtAttr attr = new EPPCoaExtAttr();
		attr.setKey( key );
		attr.setValue( value );
		EPPCoaExtInfData infData = new EPPCoaExtInfData();
		infData.appendAttr( attr );

		if ( theMessage.getName().equalsIgnoreCase( "coa-full-info-owned.com" ) ) {
			theResponse.addExtension( infData );
		}

		theResponse.setResult( EPPResult.SUCCESS );
		return new EPPEventResponse( theResponse );
	}
}

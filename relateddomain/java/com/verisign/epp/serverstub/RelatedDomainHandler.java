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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.log4j.Logger;

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
import com.verisign.epp.codec.gen.EPPEncodeDecodeStats;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtAvailable;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtCreate;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtCreateResp;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtDelete;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtDeleteResp;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtDomain;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtDomainData;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtField;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtFields;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtGroup;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtInfData;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtInfo;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtName;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtRegistered;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtRenew;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtRenewResp;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtTransfer;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtTransferResp;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtUpdate;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;
import com.verisign.epp.framework.EPPPollQueueException;
import com.verisign.epp.framework.EPPPollQueueMgr;
import com.verisign.epp.util.EPPCatFactory;

/**
 * The <code>RelatedDomainHandler</code> class is a concrete implementation of
 * the abstract <code>com.verisign.epp.framework.EPPDomainHandler</code> class.
 * It defines the Server's response to all received EPP Domain Commands.
 * 
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class RelatedDomainHandler extends DomainHandler {

	/** sample server trans id */
	private static final String svrTransId = "54322-XYZ";

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger( RelatedDomainHandler.class
			.getName(), EPPCatFactory.getInstance().getFactory() );


	/**
	 * Constructs an instance of RelatedDomainHandler
	 */
	public RelatedDomainHandler () {
	}


	/**
	 * Invoked when a Domain Create command is received.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>RelatedDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	@Override
	protected EPPEventResponse doDomainCreate ( final EPPEvent aEvent,
			final Object aData ) {
		final EPPDomainCreateCmd theMessage =
				(EPPDomainCreateCmd) aEvent.getMessage();

		if ( !theMessage.hasExtension( EPPRelatedDomainExtCreate.class ) ) {
			return super.doDomainCreate( aEvent, aData );
		}
		
		EPPRelatedDomainExtCreate relDomCreate = (EPPRelatedDomainExtCreate) theMessage
				.getExtension(EPPRelatedDomainExtCreate.class);

		cat.info( "Domain Create has relatedDomain extension for domain = " + theMessage.getName() );

		EPPDomainCreateResp theResponse;

		final Date theDate = getTodaysUTCDateAtMidnight();
		final Date expDate = getFiveYearsFutureDate();
		
		// Return EPPDomainCreateResp with required attributes.
		final EPPTransId respTransId =
				new EPPTransId( theMessage.getTransId(), "54321-XYZ" );
		theResponse =
				new EPPDomainCreateResp( respTransId, theMessage.getName(), theDate, expDate );
		theResponse.setResult( EPPResult.SUCCESS );
		
		// At least one domain in related domain command extension?
		if (relDomCreate.hasDomains()) {
			
			// Add EPPRelatedDomainExtCreateResp extension
			final EPPRelatedDomainExtCreateResp resp =
					new EPPRelatedDomainExtCreateResp();

			for (EPPRelatedDomainExtDomain domain : relDomCreate.getDomains()) {
				resp.addDomain(new EPPRelatedDomainExtDomainData( domain.getName(), theDate, expDate ));
			}
			
			theResponse.addExtension( resp );
			
		}


		return new EPPEventResponse( theResponse );
	}


	/**
	 * Invoked when a Domain Delete command is received.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>RelatedDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	@Override
	protected EPPEventResponse doDomainDelete ( final EPPEvent aEvent,
			final Object aData ) {
		final EPPDomainDeleteCmd theMessage =
				(EPPDomainDeleteCmd) aEvent.getMessage();

		if ( !theMessage.hasExtension( EPPRelatedDomainExtDelete.class ) ) {
			return super.doDomainDelete( aEvent, aData );
		}

		EPPRelatedDomainExtDelete relDomDelete = (EPPRelatedDomainExtDelete) theMessage
				.getExtension(EPPRelatedDomainExtDelete.class);
		
		cat.info( "Domain Delete has relatedDomain extension for domain = " + theMessage.getName() );

		// Test with just required EPPDomainCreateResp attributes.
		final EPPTransId respTransId =
				new EPPTransId( theMessage.getTransId(), "54321-XYZ" );
		final EPPResponse theResponse = new EPPResponse( respTransId );
		theResponse.setResult( EPPResult.SUCCESS_PENDING );

		// At least one domain in related domain command extension?
		if (relDomDelete.hasDomains()) {
			
			// Add EPPRelatedDomainExtCreateResp extension
			final EPPRelatedDomainExtDeleteResp resp =
					new EPPRelatedDomainExtDeleteResp();

			boolean isDeleted = true;
			
			for (String domain : relDomDelete.getDomains()) {
				resp.addDomain(new EPPRelatedDomainExtDomainData( domain, (isDeleted ? "deleted" : "pendingDelete") ));
				isDeleted = !isDeleted;
			}
			
			theResponse.addExtension( resp );
			
		}

		return new EPPEventResponse( theResponse );
	}


	/**
	 * Invoked when a Domain Renew command is received.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>RelatedDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	@Override
	protected EPPEventResponse doDomainRenew ( final EPPEvent aEvent,
			final Object aData ) {
		final EPPDomainRenewCmd theMessage =
				(EPPDomainRenewCmd) aEvent.getMessage();

		if ( !theMessage.hasExtension( EPPRelatedDomainExtRenew.class ) ) {
			return super.doDomainRenew( aEvent, aData );
		}

		EPPRelatedDomainExtRenew relDomRenew = (EPPRelatedDomainExtRenew) theMessage
				.getExtension(EPPRelatedDomainExtRenew.class);
		
		cat.info( "Domain Renew has relatedDomain extension for domain = " + theMessage.getName() );

		final Date theDate = getTodaysUTCDateAtMidnight();
		final Date expDate = getFiveYearsFutureDate();

		// Test with just required EPPDomainCreateResp attributes.
		final EPPTransId respTransId =
				new EPPTransId( theMessage.getTransId(), "54321-XYZ" );

		final EPPDomainRenewResp theResponse =
				new EPPDomainRenewResp( respTransId, "example.com", expDate );
		theResponse.setResult( EPPResult.SUCCESS );

		// At least one domain in related domain command extension?
		if (relDomRenew.hasDomains()) {
			
			// Add EPPRelatedDomainExtCreateResp extension
			final EPPRelatedDomainExtRenewResp resp =
					new EPPRelatedDomainExtRenewResp();
			
			for (EPPRelatedDomainExtDomain domain : relDomRenew.getDomains()) {
				resp.addDomain(new EPPRelatedDomainExtDomainData( domain.getName(), expDate ));
			}
			
			theResponse.addExtension( resp );
			
		}
		
		return new EPPEventResponse( theResponse );
	}


	/**
	 * Invoked when a Domain Update command is received.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>RelatedDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	@Override
	protected EPPEventResponse doDomainUpdate ( final EPPEvent aEvent,
			final Object aData ) {
		final EPPDomainUpdateCmd theMessage =
				(EPPDomainUpdateCmd) aEvent.getMessage();
		if ( !theMessage.hasExtension( EPPRelatedDomainExtUpdate.class ) ) {
			return super.doDomainUpdate( aEvent, aData );
		}

		cat.info( "Domain Update has relatedDomain extension for domain = " + theMessage.getName() );

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		final EPPTransId transId =
				new EPPTransId( theMessage.getTransId(), svrTransId );

		final EPPResponse theResponse = new EPPResponse( transId );
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
	 *        <code>RelatedDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	@Override
	protected EPPEventResponse doDomainTransfer ( final EPPEvent aEvent,
			final Object aData ) {
		final EPPDomainTransferCmd theMessage =
				(EPPDomainTransferCmd) aEvent.getMessage();

		if ( !theMessage.hasExtension( EPPRelatedDomainExtTransfer.class ) ) {
			return super.doDomainTransfer( aEvent, aData );
		}
		EPPRelatedDomainExtTransfer relDomTransfer = (EPPRelatedDomainExtTransfer) theMessage
				.getExtension(EPPRelatedDomainExtTransfer.class);
		
		cat.info( "Domain Transfer has relatedDomain extension for domain = " + theMessage.getName() );
		
		final EPPTransId transId =
				new EPPTransId( theMessage.getTransId(), svrTransId );

		EPPDomainTransferResp theResponse = getEPPDomainTransferResp( theMessage.getName(), theMessage.getOp() );;
		theResponse.setResult( EPPResult.SUCCESS );
						
		
		// At least one domain in related domain command extension?
		if (relDomTransfer.hasDomains()) {
			
			// Add EPPRelatedDomainExtCreateResp extension
			final EPPRelatedDomainExtTransferResp resp =
					new EPPRelatedDomainExtTransferResp();
			
			for (EPPRelatedDomainExtDomain domain : relDomTransfer.getDomains()) {
				resp.addDomain(getDomainDataForTransferResp( domain.getName(), theMessage.getOp() ));
			}
			
			theResponse.addExtension( resp );
			
		}
		
		// Is a transfer request?
		if (!theMessage.getOp().equals(EPPCommand.OP_QUERY)) {

			// Insert transfer response in poll queue
			EPPDomainTransferResp thePollMsg = getEPPDomainTransferResp(
					theMessage.getName(), theMessage.getOp());

			try {
				EPPPollQueueMgr.getInstance().put(null, EPPDomainMapFactory.NS,
						thePollMsg, null);

				if (relDomTransfer.hasDomains()) {

					for (EPPRelatedDomainExtDomain domain : relDomTransfer
							.getDomains()) {
						thePollMsg = getEPPDomainTransferResp(domain.getName(),
								theMessage.getOp());
					}

					EPPPollQueueMgr.getInstance().put(null,
							EPPDomainMapFactory.NS, thePollMsg, null);
				}

			}
			catch (final EPPPollQueueException ex) {
				cat.error("doDomainTransfer: Error putting message ["
						+ thePollMsg + "]");

				final EPPResult theResult = new EPPResult(
						EPPResult.COMMAND_FAILED);
				final EPPResponse failureResponse = new EPPResponse(transId,
						theResult);

				return new EPPEventResponse(failureResponse);
			}
		}

		return new EPPEventResponse( theResponse );
	}


	/**
	 * Utility method to return a populated <code>EPPDomainTransferResp</code>
	 * object with the given domain name and the appropriate transfer status based
	 * on the value of the parameter <code>aCommandType</code>
	 * 
	 * @param aDomainName
	 * @param aCommandType
	 * @return <code>EPPDomainTransferResp</code>
	 */
	private EPPDomainTransferResp getEPPDomainTransferResp (
			final String aDomainName, final String aCommandType ) {

		final Date theDate = getTodaysUTCDateAtMidnight();
		final Date expDate = getOneYearFutureDate();

		final EPPTransId respTransId = new EPPTransId( "ABC-12345", "54321-XYZ" );
		final EPPDomainTransferResp theResponse =
				new EPPDomainTransferResp( respTransId, aDomainName );
		theResponse.setResult( EPPResult.SUCCESS );

		theResponse.setRequestClient( "ClientX" );
		theResponse.setActionClient( "ClientY" );
		theResponse.setTransferStatus( getTransferStatus( aCommandType ) );
		theResponse.setRequestDate( theDate );
		theResponse.setActionDate( theDate );
		theResponse.setExpirationDate( expDate );
		return theResponse;
	}


	/**
	 * Utility method to return a populated
	 * <code>EPPRelatedDomainExtDomainData</code> object with the given domain
	 * name and the appropriate transfer status based on the value of the
	 * parameter <code>aCommandType</code>
	 * 
	 * @param aDomainName
	 * @param aCommandType
	 * @return <code>EPPDomainTransferResp</code>
	 */
	private EPPRelatedDomainExtDomainData getDomainDataForTransferResp (
			final String aDomainName, final String aCommandType ) {
		final Date theDate = getTodaysUTCDateAtMidnight();
		final Date expDate = getOneYearFutureDate();

		final EPPRelatedDomainExtDomainData result =
				new EPPRelatedDomainExtDomainData();
		result.setName( aDomainName );
		result.setRequestClient( "ClientX" );
		result.setActionClient( "ClientY" );
		result.setTransferStatus( getTransferStatus( aCommandType ) );
		result.setRequestDate( theDate );
		result.setActionDate( theDate );
		result.setExpirationDate( expDate );
		return result;
	}


	/**
	 * Utility method to return a transfer status corresponding to the appropriate
	 * transfer command
	 * 
	 * @param aCommandType
	 * @return a transfer status corresponding to the appropriate transfer command
	 *         type (QUERY, REQUEST, REJECT, CANCEL, APPROVE).
	 */
	private String getTransferStatus ( final String aCommandType ) {
		if ( aCommandType.equals( EPPCommand.OP_REQUEST )
				|| aCommandType.equals( EPPCommand.OP_QUERY ) ) {
			return EPPResponse.TRANSFER_PENDING;
		}
		else if ( aCommandType.equals( EPPCommand.OP_CANCEL ) ) {
			return EPPResponse.TRANSFER_CLIENT_CANCELLED;
		}
		else if ( aCommandType.equals( EPPCommand.OP_REJECT ) ) {
			return EPPResponse.TRANSFER_CLIENT_REJECTED;
		}
		else if ( aCommandType.equals( EPPCommand.OP_APPROVE ) ) {
			return EPPResponse.TRANSFER_CLIENT_APPROVED;
		}

		return EPPResponse.TRANSFER_PENDING;
	}


	/**
	 * Invoked when a Domain Info command is received.
	 * 
	 * @param aEvent
	 *        The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *        Any data that a Server needs to send to this
	 *        <code>RelatedDomaindHandler</code>
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	@Override
	protected EPPEventResponse doDomainInfo ( final EPPEvent aEvent,
			final Object aData ) {
		final EPPDomainInfoCmd theMessage = (EPPDomainInfoCmd) aEvent.getMessage();

		if ( !theMessage.hasExtension( EPPRelatedDomainExtInfo.class ) ) {
			return super.doDomainInfo( aEvent, aData );
		}

		cat.info( "Domain Info has relatedDomain extension" );
		
		EPPRelatedDomainExtInfo cmdExt = (EPPRelatedDomainExtInfo) theMessage
				.getExtension(EPPRelatedDomainExtInfo.class);
		
		EPPResponse theResponse;
		final EPPTransId respTransId =
				new EPPTransId( theMessage.getTransId(), "54321-XYZ" );
		
		if (cmdExt.getType().equals(EPPRelatedDomainExtInfo.TYPE_DOMAIN)) {
			
			final Vector statuses = new Vector();
			statuses.addElement( new EPPDomainStatus( EPPDomainStatus.ELM_STATUS_OK ) );

			// Test with just required EPPDomainInfoResp attributes.
			theResponse =
					new EPPDomainInfoResp( respTransId, "EXAMPLE1-VRSN", theMessage.getName(),
							"ClientX", statuses, "ClientY", new Date(), new EPPAuthInfo(
									"2fooBAR" ) );
			
			EPPDomainInfoResp theDomainInfoResp = (EPPDomainInfoResp) theResponse;

			theDomainInfoResp.setResult( EPPResult.SUCCESS );

			theDomainInfoResp.setRegistrant( "sh0813" );
			final EPPDomainContact contactAdmin =
					new EPPDomainContact( "sh0813", "admin" );
			final EPPDomainContact contactBilling =
					new EPPDomainContact( "sh0813", "billing" );
			final EPPDomainContact contactTech =
					new EPPDomainContact( "sh0813", "tech" );
			final Vector contacts = new Vector();
			contacts.add( contactAdmin );
			contacts.add( contactTech );
			contacts.add( contactBilling );
			theDomainInfoResp.setContacts( contacts );
			
		} // cmdExt.getType().equals(EPPRelatedDomainExtInfo.TYPE_RELATED))
		else  {
			theResponse =
					new EPPResponse( respTransId );		
			theResponse.setResult( EPPResult.SUCCESS );
		}

		final EPPEncodeDecodeStats responseStats;


		final EPPRelatedDomainExtInfData infData = new EPPRelatedDomainExtInfData();

		EPPRelatedDomainExtFields fields = new EPPRelatedDomainExtFields();
		fields.setInSync(false);
		fields.addField(new EPPRelatedDomainExtField("clID", false));
		fields.addField(new EPPRelatedDomainExtField("registrant", true));
		fields.addField(new EPPRelatedDomainExtField("ns", false));

		EPPRelatedDomainExtRegistered registered = new EPPRelatedDomainExtRegistered();
		registered.addRegisteredDomain(new EPPRelatedDomainExtName(
				"xn--test.tld1"));
		registered.addRegisteredDomain(new EPPRelatedDomainExtName(
				"xn--test.tld2"));

		infData.addGroup(new EPPRelatedDomainExtGroup(
				EPPRelatedDomainExtGroup.TYPE_TLD, fields, null, registered));

		fields = new EPPRelatedDomainExtFields();
		fields.setInSync(true);
		fields.addField(new EPPRelatedDomainExtField("clID", true));
		fields.addField(new EPPRelatedDomainExtField("registrant", true));
		fields.addField(new EPPRelatedDomainExtField("ns", true));

		registered = new EPPRelatedDomainExtRegistered();
		registered.addRegisteredDomain(new EPPRelatedDomainExtName(
				"xn--test-variant1.tld1"));
		registered.addRegisteredDomain(new EPPRelatedDomainExtName(
				"xn--test-variant2.tld1"));
		registered.addRegisteredDomain(new EPPRelatedDomainExtName(
				"xn--test-variant3.tld1"));

		final EPPRelatedDomainExtAvailable avail = new EPPRelatedDomainExtAvailable();
		avail.addAvailableDomain(new EPPRelatedDomainExtName(
				"xn--test-variant4.tld1"));
		avail.addAvailableDomain(new EPPRelatedDomainExtName(
				"xn--test-variant5.tld1"));
		avail.addAvailableDomain(new EPPRelatedDomainExtName(
				"xn--test-variant6.tld1"));

		infData.addGroup(new EPPRelatedDomainExtGroup(
				EPPRelatedDomainExtGroup.TYPE_VARIANT, fields, avail,
				registered));

		theResponse.addExtension(infData);

		theResponse.setResult( EPPResult.SUCCESS );
		return new EPPEventResponse( theResponse );
	}


	/**
	 * @return UTC date as of midnight
	 */
	private Date getTodaysUTCDateAtMidnight () {
		final Calendar cal = new GregorianCalendar( TimeZone.getTimeZone( "UTC" ) );
		cal.set( Calendar.HOUR_OF_DAY, 0 );
		cal.set( Calendar.MINUTE, 0 );
		cal.set( Calendar.SECOND, 0 );
		cal.set( Calendar.MILLISECOND, 0 );

		return cal.getTime();
	}


	/**
	 * @return UTC date five years into the future
	 */
	private Date getFiveYearsFutureDate () {
		final Calendar cal = new GregorianCalendar( TimeZone.getTimeZone( "UTC" ) );
		cal.set( Calendar.HOUR_OF_DAY, 0 );
		cal.set( Calendar.MINUTE, 0 );
		cal.set( Calendar.SECOND, 0 );
		cal.set( Calendar.MILLISECOND, 0 );

		cal.add( Calendar.YEAR, 5 );
		return cal.getTime();
	}


	/**
	 * @return UTC date one year into the future
	 */
	private Date getOneYearFutureDate () {
		final Calendar cal = new GregorianCalendar( TimeZone.getTimeZone( "UTC" ) );
		cal.set( Calendar.HOUR_OF_DAY, 0 );
		cal.set( Calendar.MINUTE, 0 );
		cal.set( Calendar.SECOND, 0 );
		cal.set( Calendar.MILLISECOND, 0 );

		cal.add( Calendar.YEAR, 1 );
		return cal.getTime();
	}
}

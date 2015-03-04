/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
***********************************************************/

package com.verisign.epp.serverstub;

import com.verisign.epp.codec.domain.*;
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.codec.namestoreext.*;

// EPP imports
import com.verisign.epp.framework.*;

import com.verisign.epp.util.EPPCatFactory;
import org.apache.log4j.Logger;


/**
 * The <code>NamestoreExtDomainHandler</code> class extends
 * <code>DomainHandler</code> to include responding with Namestore Extension
 * attributes. Specifically, the sub-product is passed into the handler and is
 * returned. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.5 $
 *
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class NamestoreExtDomainHandler extends DomainHandler {
	
	/** Log4j category for logging */
	private static Logger cat =
	Logger.getLogger(
			NamestoreExtDomainHandler.class.getName(),
			EPPCatFactory.getInstance().getFactory());
	
	/**
	 * Constructs an instance of NamestoreExtDomainHandler
	 */
	public NamestoreExtDomainHandler() {
	}
	
	/**
	 * Will ensure that the namestore extension is provided.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomainHandler</code> This is assumed to be an instance
	 * 		  of SessionData here.
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void preHandleEvent(EPPEvent aEvent, Object aData)
		throws EPPHandleEventException {
			
			super.preHandleEvent(aEvent, aData);
			
			SessionData sessionData = (SessionData) aData;
			EPPCommand  theMessage = (EPPCommand) aEvent.getMessage();
			
			EPPNamestoreExtNamestoreExt theExt =
				(EPPNamestoreExtNamestoreExt) ((EPPCommand) aEvent.getMessage())
				.getExtension(EPPNamestoreExtNamestoreExt.class);
			
			// NameStore Extension not provided?
			if (theExt == null) {
				EPPTransId transId =
				new EPPTransId(theMessage.getTransId(), "54322-XYZ");
				
				EPPResponse theResponse = new EPPResponse(transId);
				
				theResponse.setResult(EPPResult.PARAM_VALUE_POLICY_ERROR);
				theResponse.addExtension(new EPPNamestoreExtNSExtErrData(EPPNamestoreExtNSExtErrData.ERROR_SUB_PRODUCT_NOT_EXISTS));
				cat.error("NameStoreExtDomainHandler.preHandleEvent(): NameStore Extension not provided with command " + theMessage);
				throw new EPPHandleEventException("NameStore Extension not provided",
												  theResponse);
			}
			
			cat.debug("NameStoreExtDomainHandler.preHandleEvent(): NameStore Extension sub-product = " + 
					  theExt.getSubProductID());
		}
	
	
	/**
	 * Override base handler <code>doDomainCreate</code> method and add 
	 * NameStore Extension to response.
	 * 
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainCreate(EPPEvent aEvent, Object aData) {
		
		EPPDomainCreateCmd		    theCommand = (EPPDomainCreateCmd) aEvent.getMessage();
		
		EPPNamestoreExtNamestoreExt theExt =
			(EPPNamestoreExtNamestoreExt) theCommand.getExtension(
				EPPNamestoreExtNamestoreExt.class);
		
		EPPEventResponse theEventResponse = super.doDomainCreate(aEvent, aData);

		// Add extension to response
		EPPResponse theResponse = (EPPResponse) theEventResponse.getResponse();
		theResponse.addExtension(theCommand.getExtension(EPPNamestoreExtNamestoreExt.class));
				
		return theEventResponse;
	}
	
	

	/**
	 * Override base handler <code>doDomainCheck</code> method and add 
	 * NameStore Extension to response.
	 * 
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainCheck(EPPEvent aEvent, Object aData) {
		EPPDomainCheckCmd		    theCommand = (EPPDomainCheckCmd) aEvent.getMessage();
		
		EPPNamestoreExtNamestoreExt theExt =
			(EPPNamestoreExtNamestoreExt) theCommand.getExtension(
				EPPNamestoreExtNamestoreExt.class);
		
		EPPEventResponse theEventResponse = super.doDomainCheck(aEvent, aData);

		// Add extension to response
		EPPResponse theResponse = (EPPResponse) theEventResponse.getResponse();
		theResponse.addExtension(theCommand.getExtension(EPPNamestoreExtNamestoreExt.class));
		
		return theEventResponse;
	}

	/**
	 * Override base handler <code>doDomainDelete</code> method and add 
	 * NameStore Extension to response.
	 * 
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainDelete(EPPEvent aEvent, Object aData) {
		EPPDomainDeleteCmd theCommand = (EPPDomainDeleteCmd) aEvent.getMessage();
		
		EPPNamestoreExtNamestoreExt theExt =
		(EPPNamestoreExtNamestoreExt) theCommand.getExtension(
				EPPNamestoreExtNamestoreExt.class);
		
		EPPEventResponse theEventResponse = super.doDomainDelete(aEvent, aData);

		// Add extension to response
		EPPResponse theResponse = (EPPResponse) theEventResponse.getResponse();
		theResponse.addExtension(theCommand.getExtension(EPPNamestoreExtNamestoreExt.class));
		
		return theEventResponse;
	}

	/**
	 * Override base handler <code>doDomainInfo</code> method and add 
	 * NameStore Extension to response.
	 * 
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainInfo(EPPEvent aEvent, Object aData) {
		EPPDomainInfoCmd theCommand = (EPPDomainInfoCmd) aEvent.getMessage();
		
		EPPNamestoreExtNamestoreExt theExt =
		(EPPNamestoreExtNamestoreExt) theCommand.getExtension(
				EPPNamestoreExtNamestoreExt.class);
		
		EPPEventResponse theEventResponse = super.doDomainInfo(aEvent, aData);

		// Add extension to response
		EPPResponse theResponse = (EPPResponse) theEventResponse.getResponse();
		theResponse.addExtension(theCommand.getExtension(EPPNamestoreExtNamestoreExt.class));
		
		return theEventResponse;
	}

	/**
	 * Override base handler <code>doDomainRenew</code> method and add 
	 * NameStore Extension to response.
	 * 
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainRenew(EPPEvent aEvent, Object aData) {
		EPPDomainRenewCmd theCommand = (EPPDomainRenewCmd) aEvent.getMessage();
		
		EPPNamestoreExtNamestoreExt theExt =
		(EPPNamestoreExtNamestoreExt) theCommand.getExtension(
				EPPNamestoreExtNamestoreExt.class);
		
		EPPEventResponse theEventResponse = super.doDomainRenew(aEvent, aData);

		// Add extension to response
		EPPResponse theResponse = (EPPResponse) theEventResponse.getResponse();
		theResponse.addExtension(theCommand.getExtension(EPPNamestoreExtNamestoreExt.class));
		
		return theEventResponse;
	}

	/**
	 * Override base handler <code>doDomainTransfer</code> method and add 
	 * NameStore Extension to response.
	 * 
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainTransfer(
		EPPEvent aEvent,
		Object aData) {
		EPPDomainTransferCmd theCommand = (EPPDomainTransferCmd) aEvent.getMessage();
		
		EPPNamestoreExtNamestoreExt theExt =
		(EPPNamestoreExtNamestoreExt) theCommand.getExtension(
				EPPNamestoreExtNamestoreExt.class);
		
		EPPEventResponse theEventResponse = super.doDomainTransfer(aEvent, aData);

		// Add extension to response
		EPPResponse theResponse = (EPPResponse) theEventResponse.getResponse();
		theResponse.addExtension(theCommand.getExtension(EPPNamestoreExtNamestoreExt.class));
		
		return theEventResponse;
	}

	/**
	 * Override base handler <code>doDomainUpdate</code> method and add 
	 * NameStore Extension to response.
	 * 
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doDomainUpdate(EPPEvent aEvent, Object aData) {
		EPPDomainUpdateCmd theCommand = (EPPDomainUpdateCmd) aEvent.getMessage();
		
		EPPNamestoreExtNamestoreExt theExt =
		(EPPNamestoreExtNamestoreExt) theCommand.getExtension(
				EPPNamestoreExtNamestoreExt.class);
		
		EPPEventResponse theEventResponse = super.doDomainUpdate(aEvent, aData);

		// Add extension to response
		EPPResponse theResponse = (EPPResponse) theEventResponse.getResponse();
		theResponse.addExtension(theCommand.getExtension(EPPNamestoreExtNamestoreExt.class));
		
		return theEventResponse;
	}

}

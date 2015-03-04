/***********************************************************
Copyright (C) 2007 VeriSign, Inc.

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


// Logging Imports
import org.apache.log4j.Logger;

import com.verisign.epp.codec.contact.EPPContactCreateCmd;
import com.verisign.epp.codec.contact.EPPContactInfoCmd;
import com.verisign.epp.codec.contact.EPPContactUpdateCmd;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.jobscontact.EPPJobsContactCreateCmd;
import com.verisign.epp.codec.jobscontact.EPPJobsContactInfoResp;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;
import com.verisign.epp.util.EPPCatFactory;

/**
 * Extension to the standard <code>DomainHandler</code> that 
 * looks for the <code>EPPWhoisInf</code> command extension 
 * with the info command and 
 * adds the <code>EPPWhoisInfData</code> extension to the response. 
 */
public class JobsContactHandler extends ContactHandler {

	/** Logging category */
	private static Logger cat =
		Logger.getLogger(JobsContactHandler.class.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Constructs an instance of WhoisDomainHandler
	 */
	public JobsContactHandler() {}
	
	/**
	 * Invoked when a Domain Info command is received.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPDomaindHandler</code>
	 *
	 * @return EPPEventResponse The response that should be sent back to the
	 * 		   client.
	 */
	protected EPPEventResponse doContactCreate(EPPEvent aEvent, Object aData) {
		EPPContactCreateCmd theCommand = (EPPContactCreateCmd) aEvent.getMessage();
		
		EPPJobsContactCreateCmd theExt = (EPPJobsContactCreateCmd) theCommand.getExtension(
				EPPJobsContactCreateCmd.class);
		
		EPPEventResponse theEventResponse = super.doContactCreate(aEvent, aData);
		EPPResponse theResponse = (EPPResponse) theEventResponse.getResponse();
	
		return theEventResponse;
	}
	
	protected EPPEventResponse doContactInfo(EPPEvent aEvent, Object aData) {
		EPPContactInfoCmd theCommand = (EPPContactInfoCmd) aEvent.getMessage();
		
		EPPJobsContactCreateCmd theExt = (EPPJobsContactCreateCmd) theCommand.getExtension(
				EPPJobsContactCreateCmd.class);
		
		EPPEventResponse theEventResponse = super.doContactInfo(aEvent, aData);
		EPPResponse theResponse = (EPPResponse) theEventResponse.getResponse();

		theResponse.addExtension(new EPPJobsContactInfoResp("Title", "http://www.verisign.jobs", "IT", "Yes", "Yes"));

		return theEventResponse;
	}
	
	protected EPPEventResponse doContactUpdate(EPPEvent aEvent, Object aData) {
		EPPContactUpdateCmd theCommand = (EPPContactUpdateCmd) aEvent.getMessage();
		
		EPPEventResponse theEventResponse = super.doContactUpdate(aEvent, aData);
		EPPResponse theResponse = (EPPResponse) theEventResponse.getResponse();
		
		return theEventResponse;
	}

}

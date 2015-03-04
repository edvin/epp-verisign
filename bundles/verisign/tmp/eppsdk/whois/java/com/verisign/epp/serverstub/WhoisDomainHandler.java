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


// Logging Imports
import org.apache.log4j.Logger;

import com.verisign.epp.codec.domain.EPPDomainInfoCmd;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.whois.EPPWhoisInf;
import com.verisign.epp.codec.whois.EPPWhoisInfData;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;
import com.verisign.epp.util.EPPCatFactory;

/**
 * Extension to the standard <code>DomainHandler</code> that 
 * looks for the <code>EPPWhoisInf</code> command extension 
 * with the info command and 
 * adds the <code>EPPWhoisInfData</code> extension to the response. 
 */
public class WhoisDomainHandler extends DomainHandler {

	/** Logging category */
	private static Logger cat =
		Logger.getLogger(
						 WhoisDomainHandler.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Constructs an instance of WhoisDomainHandler
	 */
	public WhoisDomainHandler() {}
	
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
	protected EPPEventResponse doDomainInfo(EPPEvent aEvent, Object aData) {
		EPPDomainInfoCmd theCommand = (EPPDomainInfoCmd) aEvent.getMessage();
		
		EPPWhoisInf theExt = (EPPWhoisInf) theCommand.getExtension(
				EPPWhoisInf.class);
		
		EPPEventResponse theEventResponse = super.doDomainInfo(aEvent, aData);
		EPPResponse theResponse = (EPPResponse) theEventResponse.getResponse();

		// Add extension to response if command extension was passed, 
		// the extension flag is true and the response is a successful response.
		if (theExt != null && theExt.getFlag().booleanValue() && theResponse.isSuccess()) {
			theResponse.addExtension(new EPPWhoisInfData("Example Registrar Inc.", "whois.example.com", 
					"http://www.example.com", "iris.example.com"));
		}
		
		return theEventResponse;
	}
}

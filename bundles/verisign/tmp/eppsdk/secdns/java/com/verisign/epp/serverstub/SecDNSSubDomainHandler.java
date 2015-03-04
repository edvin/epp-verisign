/***********************************************************
Copyright (C) 2010 VeriSign, Inc.

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

import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.gen.EPPResponse;

/**
 * Sub Domain Handler for use with the secDNS extension.  
 * This interface is used to support handling multiple versions of the 
 * secDNS extension, where each different version of the 
 * secDNS extension to implement this interface.  
 */
public interface SecDNSSubDomainHandler {
	/**
	 * Handle an EPP Domain Create Command.  
	 * 
	 * @param aCreateCommand Command sent by the client
	 * @param aData Server data.  This is assumed to be an instance of <code>SessionData</code>.
	 * 
	 * @return An <code>EPPResponse</code> to be returned to the client. The transaction id (client and server) is not set.  
	 */
	EPPResponse doDomainCreate(EPPDomainCreateCmd aCreateCommand, Object aData);
	
	/**
	 * Handle an EPP Domain Update Command.  
	 * 
	 * @param aUpdateCommand Command sent by the client
	 * @param aData Server data.  This is assumed to be an instance of <code>SessionData</code>.
	 * 
	 * @return An <code>EPPResponse</code> to be returned to the client.  The transaction id (client and server) is not set.  
	 */	
	EPPResponse doDomainUpdate(EPPDomainUpdateCmd aUpdateCommand, Object aData);
	
	
	/**
	 * Handle an EPP Domain Info Command.  
	 * 
	 * @param aInfoCommand Command sent by the client
	 * @param aInfoResponse Response filled in without the secDNS extension.
	 * @param aData Server data.  This is assumed to be an instance of <code>SessionData</code>.
	 * 
	 * @return An <code>EPPResponse</code> to be returned to the client. The transaction id (client and server) is not set.   
	 */	
	EPPDomainInfoResp doDomainInfo(EPPDomainInfoCmd aInfoCommand, EPPDomainInfoResp aInfoResponse, Object aData);
}

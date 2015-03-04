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


// Logging Imports
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainCreateResp;
import com.verisign.epp.codec.domain.EPPDomainInfoCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.secdnsext.v10.EPPSecDNSAlgorithm;
import com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtCreate;
import com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtDsData;
import com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtInfData;
import com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtKeyData;
import com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtUpdate;
import com.verisign.epp.util.EPPCatFactory;


/**
 * The <code>SecDNSV10SubDomainHandler</code> class is a concrete {@link SecDNSSubDomainHandler} 
 * for version 1.0 of the secDNS extension.  It is wrapped by the wrapping {@link SecDNSDomainHandler} to support
 * multiple versions of the secDNS extension.  Any version 1.0 specific behavior is handled by <code>SecDNSV10SubDomainHandler</code>.
 */
public class SecDNSV10SubDomainHandler implements SecDNSSubDomainHandler {


	/** Used for logging */
	private static Logger cat =
		Logger.getLogger(
				SecDNSV10SubDomainHandler.class.getName(),
				EPPCatFactory.getInstance().getFactory());
	

	/**
	 * Handle an EPP Domain Create Command for version 1.0 of the secDNS extension. 
	 * 
	 * @param aCreateCommand Command sent by the client
	 * @param aData Server data.  This is assumed to be an instance of <code>SessionData</code>.
	 * 
	 * @return An <code>EPPResponse</code> to be returned to the client. The transaction id (client and server) is not set.  
	 */
	public EPPResponse doDomainCreate(EPPDomainCreateCmd aCreateCommand, Object aData) {
		EPPResponse theResponse;
		

		// print the incoming secDNS extension
		EPPSecDNSExtCreate secDNScreate = (EPPSecDNSExtCreate) aCreateCommand.getExtension(EPPSecDNSExtCreate.class);
		
		if (secDNScreate != null) {
			List dsData = secDNScreate.getDsData();
			cat.info("SecDNSV10SubDomainHandler.doDomainCreate: secDNS:create dsData = \n" + dsData);
		}
		else {
			cat.info("SecDNSV10SubDomainHandler.doDomainCreate: no EPPSecDNSExtCreate extension\n");
		}
		
		theResponse = new EPPDomainCreateResp(null, aCreateCommand.getName(), new Date());


		return theResponse;
	}

	/**
	 * Handle an EPP Domain Update Command for version 1.0 of the secDNS extension.  
	 * 
	 * @param aUpdateCommand Command sent by the client
	 * @param aData Server data.  This is assumed to be an instance of <code>SessionData</code>.
	 * 
	 * @return An <code>EPPResponse</code> to be returned to the client.  The transaction id (client and server) is not set.  
	 */	
	public EPPResponse doDomainUpdate(EPPDomainUpdateCmd aUpdateCommand, Object aData) {

 		// print the incoming secDNS extension
		EPPSecDNSExtUpdate secDNSupdate = (EPPSecDNSExtUpdate)aUpdateCommand.getExtension(EPPSecDNSExtUpdate.class);
		if (secDNSupdate != null) {
			// secDNS:add
			List addDsData = secDNSupdate.getAdd();
			if (addDsData != null) {
				cat.info("SecDNSDomainHandler.doDomainUpdate: secDNS:update/secDNS:add dsData = \n" + addDsData);
			}
			else {
				cat.info("SecDNSDomainHandler.doDomainUpdate: secDNS:update/secDNS:add extension not found\n");
			}

			// secDNS:chg
			List chgDsData = secDNSupdate.getChg();
			if (chgDsData != null) {
				cat.info("SecDNSDomainHandler.doDomainUpdate: secDNS:update/secDNS:chg dsData = \n" + chgDsData);
			}
			else {
				cat.info("SecDNSDomainHandler.doDomainUpdate: secDNS:update/secDNS:chg extension not found\n");
			}
			
			// secDNS:rem
			List remKeyTag = secDNSupdate.getRem();
			if (remKeyTag != null) {
				cat.info("SecDNSDomainHandler.doDomainUpdate: secDNS:update/secDNS:rem keyTag = \n" + remKeyTag);
			}
			else {
				cat.info("SecDNSDomainHandler.doDomainUpdate: secDNS:update/secDNS:rem extension not found\n");
			}
		}
		else {
			cat.info("SecDNSDomainHandler.doDomainUpdate: no EPPSecDNSExtUpdate extension\n");
		}
		
		// Return successful EPPResponse, which is the default.
		return new EPPResponse();
	}

	/**
	 * Handle an EPP Domain Info Command.  
	 * 
	 * @param aInfoCommand Command sent by the client
	 * @param aInfoResponse Response filled in without the secDNS extension.
	 * @param aData Server data.  This is assumed to be an instance of <code>SessionData</code>.
	 * 
	 * @return An <code>EPPDomainInfoResp</code> to be returned to the client with the secDNS extension attached.   
	 */	
	public EPPDomainInfoResp doDomainInfo(EPPDomainInfoCmd aInfoCommand, EPPDomainInfoResp aInfoResponse, Object aData) {
		

 		// instantiate a secDNS:keyData object
        EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
        keyData.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
        keyData.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
        keyData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
        keyData.setPubKey("AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj" +
                    "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs" +
                    "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2" +
                    "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO" +
                    "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==");
        
        // instantiate another secDNS:keyData object
        EPPSecDNSExtKeyData keyData2 = new EPPSecDNSExtKeyData(
        		EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP,
        		EPPSecDNSExtKeyData.DEFAULT_PROTOCOL,
        		EPPSecDNSAlgorithm.RSASHA1,
        		"AQOxXpFbRp7+zPBoTt6zL7Af0aEKzpS4JbVB" +
        			"5ofk5E5HpXuUmU+Hnt9hm2kMph6LZdEEL142" +
                    "nq0HrgiETFCsN/YM4Zn+meRkELLpCG93Cu/H" +
                    "hwvxfaZenUAAA6Vb9FwXQ1EMYRW05K/gh2Ge" +
                    "w5Sk/0o6Ev7DKG2YiDJYA17QsaZtFw==");
        
        // instantiate a secDNS:dsData object
        EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData();
        dsData.setKeyTag(34095);
        dsData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
        dsData.setDigestType(EPPSecDNSExtDsData.SHA1_DIGEST_TYPE);
        dsData.setDigest("6BD4FFFF11566D6E6A5BA44ED0018797564AA289");
        dsData.setMaxSigLife(604800);
        dsData.setKeyData(keyData);
        
        // instantiate another secDNS:dsData object
        EPPSecDNSExtDsData dsData2 = new EPPSecDNSExtDsData(
        		10563, 
        		EPPSecDNSAlgorithm.RSASHA1, 
        		EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
        		"9C20674BFF957211D129B0DFE9410AF753559D4B",
        		604800,
        		keyData2);
        
        // instantiate the secDNS:infData object
        EPPSecDNSExtInfData infData = new EPPSecDNSExtInfData();
        Vector dsDataVec = new Vector();
        dsDataVec.add(dsData);
        infData.setDsData(dsDataVec);
        infData.appendDsData(dsData2);
        
        // set the secDNS:infData in the response as an extension
        aInfoResponse.addExtension(infData);

		return aInfoResponse;
	}
}

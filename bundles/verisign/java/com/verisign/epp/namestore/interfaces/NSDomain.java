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
package com.verisign.epp.namestore.interfaces;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.idnext.EPPIdnLangTag;
import com.verisign.epp.codec.namestoreext.EPPNamestoreExtNamestoreExt;
import com.verisign.epp.codec.rgpext.EPPRgpExtReport;
import com.verisign.epp.codec.rgpext.EPPRgpExtRestore;
import com.verisign.epp.codec.rgpext.EPPRgpExtUpdate;
import com.verisign.epp.codec.syncext.EPPSyncExtUpdate;
import com.verisign.epp.codec.whois.EPPWhoisInf;
import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.interfaces.EPPRelatedDomain;
import com.verisign.epp.interfaces.EPPSession;

/**
 * NameStore Domain interface that extends that standard
 * <code>EPPDomain</code> by adding new operations like 
 * restore request, restore report, and sync.   
 */
public class NSDomain extends EPPRelatedDomain {
	
	/**
	 * Restore report information
	 */
	private EPPRgpExtReport report = null;
	
	/**
	 * Month for sync command
	 */
	private int month = -1;
	
	/**
	 * Day for sync command
	 */
	private int day = -1; 
	
	/**
	 * Constant used to remove all DS using <code>aRemDsData</code> parameter of the method {@link #setSecDNSUpdate(List, List)}.
	 */
	public static final List REM_ALL_DS = new ArrayList();
	
	
	/**
	 * Creates an <code>NSDomain</code> with an 
	 * established <code>EPPSession</code>. 
	 * 
	 * @param aSession Established session
	 */
	public NSDomain(EPPSession aSession) {
		super(aSession);
	}

	
	/**
	 * Send a restore request.<br>  
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * <ul>
	 * <li>
	 * <code>addDomainName</code> - Sets the domain name to restore.  Only one
	 * domain name is valid.
	 * </li>
	 * <li>
	 * <code>setSubProductID</code> - Sets the sub-product id
	 * </li>
	 * </ul>
	 * <br><br>
	 * The optional attributes have been set with the following:<br><br>
	 * <ul>
	 * <li>
	 * <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * </ul>
	 * 
	 * @return <code>EPPResponse</code> containing the Domain restore request result.
	 *
	 * @exception EPPCommandException Error executing the restore request command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPResponse sendRestoreRequest() throws EPPCommandException {
		super.addExtension(new EPPRgpExtUpdate(new EPPRgpExtRestore()));
		return super.sendUpdate();
	}
	
	/**
	 * Send a restore report.<br>  
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * <ul>
	 * <li>
	 * <code>addDomainName</code> - Sets the domain name for report.  Only one
	 * domain name is valid.
	 * </li>
	 * <li>
	 * <code>setSubProductID</code> - Sets the sub-product id
	 * </li>
	 * <li>
	 * <code>setReport</code> - Sets the report information
	 * </li>
	 * </ul>
	 * <br><br>
	 * The optional attributes have been set with the following:<br><br>
	 * <ul>
	 * <li>
	 * <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * </ul>
	 * 
	 * @return <code>EPPResponse</code> containing the Domain restore report result.
	 *
	 * @exception EPPCommandException Error executing the restore report command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPResponse sendRestoreReport() throws EPPCommandException {
		if (this.report == null) {
			throw new EPPCommandException("Report is required for sendRestoreReport()");
		}
		super.addExtension(new EPPRgpExtUpdate(new EPPRgpExtRestore(this.report)));
		return super.sendUpdate();
	}
	
	/**
	 * Send a sync command for a domain with a specified 
	 * expiration month and day<br>  
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * <ul>
	 * <li>
	 * <code>addDomainName</code> - Sets the domain name for report.  Only one
	 * domain name is valid.
	 * </li>
	 * <li>
	 * <code>setSubProductID</code> - Sets the sub-product id
	 * </li>
	 * <li>
	 * <code>setMonth</code> - Month to set expiration date
	 * </li>
	 * <li>
	 * <code>setDay</code> - Day to set expiration date
	 * </li>
	 * </ul>
	 * <br><br>
	 * The optional attributes have been set with the following:<br><br>
	 * <ul>
	 * <li>
	 * <code>setTransId</code> - Sets the client transaction identifier
	 * </li>
	 * </ul>
	 * 
	 * @return <code>EPPResponse</code> containing the Domain sync result.
	 *
	 * @exception EPPCommandException Error executing the sync command.  Use
	 * 			  <code>getResponse</code> to get the associated server error
	 * 			  response.
	 */
	public EPPResponse sendSync() throws EPPCommandException {
		if (this.month < Calendar.JANUARY || this.month > Calendar.DECEMBER) {
			throw new EPPCommandException("Month " + this.month + " is invalid value for sendRestoreReport()");			
		}

		if (this.day < 1 || this.day > 31) {
			throw new EPPCommandException("Day " + this.day + " is invalid value for sendRestoreReport()");			
		}
		
		super.addExtension(new EPPSyncExtUpdate(this.month, this.day));
		return super.sendUpdate();
	}
		
	/**
	 * Gets the day of sync.
	 * 
	 * @return Returns the day of sync if set; <code>-1</code> otherwise
	 */
	public int getDay() {
		return this.day;
	}
	
	/**
	 * Sets the day of sync.
	 * 
	 * @param aDay The day to set.
	 */
	public void setDay(int aDay) {
		this.day = aDay;
	}
	/**
	 * Gets the month of sync.
	 * 
	 * @return the month using a  
     * <code>java.util.Calendar</code> month constant if defined; 
     * <code>-1</code> otherwise
	 */
	public int getMonth() {
		return this.month;
	}
	/**
	 * Sets the month of sync.
	 * 
	 * @param aMonth the month using a 
     * <code>java.util.Calendar</code> month constant
	 */
	public void setMonth(int aMonth) {
		this.month = aMonth;
	}
		
	/**
	 * Gets the report information for the restore report.
	 * 
	 * @return Returns the report if defined; <code>null</code> otherwise.
	 */
	public EPPRgpExtReport getReport() {
		return this.report;
	}
	/**
	 * Sets the report information for the restore report.
	 * 
	 * @param aReport Report information
	 */
	public void setReport(EPPRgpExtReport aReport) {
		this.report = aReport;
	}
	
	/**
	 * Set the IDN language tag used with <code>sendCreate</code>.  
	 * 
	 * @param aLangTag Valid XML schema language value as defined by 
	 * <a href="http://www.w3.org/TR/xmlschema-2/#language"/>.  For example, 
	 * use a two letter language tag like <code>en</code> or <code>fr</code>.
	 */
	public void setIDNLangTag(String aLangTag) {
		super.addExtension(new EPPIdnLangTag(aLangTag));
	}
	
	/**
	 * Sets the domain sub-product id which specifies which is the 
	 * target registry for the domain operation.  Some possible 
	 * values include dotCC, dotTV, dotBZ, dotCOM, dotNET.  This results 
	 * in a <code>EPPNamestoreExtNamestoreExt</code> extension being 
	 * added to the command.
	 * 
	 * @param aSubProductID Sub-product id of domain operation.  Should use one 
	 * of the @link{NSSubProduct} constants.  Passing <code>null</code> 
	 * will not add any extension.
	 */
	public void setSubProductID(String aSubProductID) {
		if (aSubProductID != null) {
			super.addExtension(new EPPNamestoreExtNamestoreExt(aSubProductID));			
		}
	}
	
	/**
	 * Sets if whois information is desired in the response to a 
	 * call to <code>sendInfo()</code>.  If <code>true</code> is specified, 
	 * the @link{com.verisign.epp.codec.whois.EPPWhoisInfData} extension 
	 * will be added to the @link{com.verisign.epp.codec.domain.EPPDomainInfoResp} 
	 * when the server supports it.  
	 * 
	 * @param aWhoisInfo <code>true</code> to include the whois response information;<code>false</code> 
	 * otherwise.
	 */
	public void setWhoisInfo(boolean aWhoisInfo) {
		super.addExtension(new EPPWhoisInf(aWhoisInfo));
	}
	


	/**
	 * Sets the list of <code>EPPSecDNSExtDsData</code> instances to add along
	 * with the list of <code>EPPSecDNSExtDsData</code> instances to remove.
	 * This method only supports secDNS-1.1. This method can be used to remove
	 * all DS data by passing the {@link #REM_ALL_DS} constant with the
	 * <code>aRemDsData</code> parameter, and can be used to replace all DS data
	 * by passing the {@link #REM_ALL_DS} constant with the
	 * <code>aRemDsData</code> parameter and setting the <code>aAddDsData</code>
	 * to a non-null, non-empty list.
	 * 
	 * @param aAddDsData
	 *            - List of
	 *            {@link com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtDsData}
	 *            instances to add; <code>null</code> if there is nothing to
	 *            add.
	 * @param aRemDsData
	 *            - List of
	 *            {@link com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtDsData}
	 *            instances to remove; <code>null</code> if there is nothing to
	 *            remove. Set to {@link #REM_ALL_DS} constant to remove all DS
	 *            data.
	 * @exception EPPCommandException
	 *                Error with parameters passed in.
	 */
	public void setSecDNSUpdate ( List aAddDsData, List aRemDsData) throws EPPCommandException {
		
		// Validate aAddDsData is secDNS-1.1
		if (aAddDsData != null && !aAddDsData.isEmpty()) {
			if (!(aAddDsData.get(0) instanceof com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtDsData)) {
				throw new EPPCommandException(
						"NSDomain.setSecDNSUpdate: Invalid aAddDsData element of type = "
								+ aAddDsData.get(0).getClass().getName());
			}
		}

		// Validate aRemDsData is secDNS-1.1
		if (aRemDsData != REM_ALL_DS && aRemDsData != null && !aRemDsData.isEmpty()) {
			if (!(aRemDsData.get(0) instanceof com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtDsData)) {
				throw new EPPCommandException(
						"NSDomain.setSecDNSUpdate: Invalid aRemDsData element of type = "
								+ aRemDsData.get(0).getClass().getName());
			}
		}
		
		
		com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtUpdate theExt = new com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtUpdate();
		theExt.setAddDsData(aAddDsData);
		
		if (aRemDsData == REM_ALL_DS) {
			theExt.setRemAllData(true);
		}
		else {
			theExt.setRemDsData(aRemDsData);			
		}
		
		super.addExtension( theExt );
	}
	
	/**
	 * Sets the list of <code>EPPSecDNSExtDsData</code> instances in order to create
	 * delegation signer (DS) information.
	 * 
	 * @param aDsData -
	 *        List of <code>EPPSecDNSExtDsData</code> instances
	 */
	public void setSecDNSCreate ( List aDsData ) {
		
		// aDsData has elements?
		if (aDsData != null && !aDsData.isEmpty()) {
			
			// secDNS-1.0?
			if (aDsData.get(0) instanceof com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtDsData) {
				super.addExtension( new com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtCreate( aDsData ) );
			} // secDNS-1.1?
			else if (aDsData.get(0) instanceof com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtDsData) {
				com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtCreate theExt = new com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtCreate();
				theExt.setDsData(aDsData);
				super.addExtension(theExt);				
			}
			
		}
		
	}
	
	/**
	 * Sets the list of <code>EPPCoaExtAttr</code> instances to associate Client Object Attributes
	 * with the object being created.
	 * @param aAttrs - List of <code>EPPCoaExtAttr</code> instances 
	 */
	public void setCoaCreate( List aAttrs ) {
		if ( aAttrs != null && !aAttrs.isEmpty()) {
			super.addExtension( new com.verisign.epp.codec.coaext.EPPCoaExtCreate(aAttrs) );
		}
	}
	
	/**
	 * Sets the list of <code>EPPCoaExtAttr</code> instances to associate Client Object Attributes
	 * with the object being updated.
	 * @param aAttrs - List of <code>EPPCoaExtAttr</code> instances 
	 */
	public void setCoaUpdateForPut( List aAttrs ) {
		if ( aAttrs != null && !aAttrs.isEmpty()) {
			com.verisign.epp.codec.coaext.EPPCoaExtUpdate update = new com.verisign.epp.codec.coaext.EPPCoaExtUpdate();
			update.setPutAttrs(aAttrs);
			super.addExtension( update );
		}
	}
	
	/**
	 * Sets the list of <code>EPPCoaExtKey</code> instances to specify Client Object Attributes
	 * to be removed from the object being updated.
	 * @param aKeys - List of <code>EPPCoaExtKey</code> instances 
	 */
	public void setCoaUpdateForRem( List aKeys ) {
		if ( aKeys != null && !aKeys.isEmpty()) {
			com.verisign.epp.codec.coaext.EPPCoaExtUpdate update = new com.verisign.epp.codec.coaext.EPPCoaExtUpdate();
			update.setRemAttrs(aKeys);
			super.addExtension( update );
		}
	}


	/**
	 * Sets the list of <code>EPPSecDNSExtDsData</code> instances in order to add
	 * delegation signer (DS) information.
	 * 
	 * @param aAddDsData -
	 *        List of <code>EPPSecDNSExtDsData</code> instances
	 * @param aUrgent -
	 *        boolean value indicates whether it is a high priority request
	 * @deprecated Supports secDNS-1.0 and secDNS-1.1, but use the secDNS-1.1 method {@link #setSecDNSUpdate(List, List)}.
	 */
	public void setSecDNSUpdateForAdd ( List aAddDsData, boolean aUrgent ) {
		
		// aDsData has elements?
		if (aAddDsData != null && !aAddDsData.isEmpty()) {
			
			// secDNS-1.0?
			if (aAddDsData.get(0) instanceof com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtDsData) {
				com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtUpdate theExt = new com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtUpdate();
				theExt.setAdd(aAddDsData);
				theExt.setUrgent(aUrgent);
				super.addExtension(theExt);				
			} // secDNS-1.1?
			else if (aAddDsData.get(0) instanceof com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtDsData) {
				com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtUpdate theExt = new com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtUpdate();
				theExt.setAddDsData(aAddDsData);
				theExt.setUrgent(aUrgent);
				super.addExtension(theExt);				
			}
			
		}
		
	}


	/**
	 * Sets the list of <code>EPPSecDNSExtDsData</code> instances in order to
	 * change delegation signer (DS) information. This method is only used for
	 * secDNS-1.0.
	 * 
	 * @param aChgDsData
	 *            - List of {@link com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtDsData} instances
	 * @param aUrgent
	 *            - boolean value indicates whether it is a high priority
	 *            request
	 * @deprecated Only supported with secDNS-1.0.  Look to use the secDNS-1.1 method {@link #setSecDNSUpdate(List, List)}.
	 */
	public void setSecDNSUpdateForChg(List aChgDsData, boolean aUrgent) {

		// Does list contain secDNS-1.0 EPPSecDNSExtDsData?
		if (aChgDsData.get(0) instanceof com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtDsData) {
			com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtUpdate updateExt = new com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtUpdate(
					null, aChgDsData, null);
			updateExt.setUrgent(aUrgent);
			super.addExtension(updateExt);
		}

	}


	/**
	 * Sets the list of <code>Integer</code> instances in order to remove
	 * delegation signer (DS) information.
	 * 
	 * @param aRemDsData
	 *            - List of <code>Integer</code> instances for secDNS-1.0 or
	 *            {@link com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtDsData}
	 *            instances for secDNS-1.1.
	 * @param aUrgent
	 *            - boolean value indicates whether it is a high priority
	 *            request
	 * @deprecated Supports secDNS-1.0 and secDNS-1.1, but use the secDNS-1.1 method {@link #setSecDNSUpdate(List, List)}.
	 */
	public void setSecDNSUpdateForRem(List aRemDsData, boolean aUrgent) {

		// aDsData has elements?
		if (aRemDsData != null && !aRemDsData.isEmpty()) {

			// secDNS-1.0?
			if (aRemDsData.get(0) instanceof Integer) {
				com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtUpdate theExt = new com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtUpdate();
				theExt.setRem(aRemDsData);
				theExt.setUrgent(aUrgent);
				super.addExtension(theExt);
			} // secDNS-1.1?
			else if (aRemDsData.get(0) instanceof com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtDsData) {
				com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtUpdate theExt = new com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtUpdate();
				theExt.setRemDsData(aRemDsData);
				theExt.setUrgent(aUrgent);
				super.addExtension(theExt);
			}

		}

	}

		
	/**
	 * Resets the domain attributes for the next command.
	 */
	protected void resetDomain() {
		super.resetDomain();
		this.day = -1;
		this.month = -1;
		this.report = null;
	}
	
} // End class NSDomain

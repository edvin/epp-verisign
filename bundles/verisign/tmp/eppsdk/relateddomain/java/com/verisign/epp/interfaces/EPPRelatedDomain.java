/***********************************************************
Copyright (C) 2013 VeriSign, Inc.

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
 ***********************************************************/
package com.verisign.epp.interfaces;

// W3C Imports
import java.util.ArrayList;
import java.util.List;

import com.verisign.epp.codec.domain.EPPDomainCreateResp;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainRenewResp;
import com.verisign.epp.codec.domain.EPPDomainTransferResp;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtCreate;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtCreateResp;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtDelete;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtDeleteResp;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtDomain;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtInfData;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtInfo;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtRenew;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtRenewResp;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtTransfer;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtTransferResp;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtUpdate;

/**
 * <code>EPPRelatedDomain</code> is the client interface specific to the Related
 * Domain EPP Extension to support sending the info command in both the Domain
 * Info Form and the Related Info Form via the {@link #sendInfo()} method. The
 * form is defined by the <code>type</code> attribute using one of the two
 * constants of <code>TYPE_DOMAIN</code> for the Domain Info Form and
 * <code>TYPE_RELATED</code> for the Related Info Form. The default form is the
 * Domain Info Form.
 * 
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtInfData
 */
public class EPPRelatedDomain extends EPPDomain {

	/**
	 * Use the Domain Info Form for the info command.
	 */
	public final static String DOMAIN_INFO_FORM = EPPRelatedDomainExtInfo.TYPE_DOMAIN;

	/**
	 * Use the Related Info Form for the info command.
	 */
	public final static String RELATED_INFO_FORM = EPPRelatedDomainExtInfo.TYPE_RELATED;

	/**
	 * Defines info form type with one of the <code>TYPE</code> constants.
	 */
	private String infoForm = DOMAIN_INFO_FORM;

	/**
	 * List of related domains used in {@link #sendRelatedCreate()},
	 * {@link #sendRelatedRenew()}, and {@link #sendRelatedTransfer()}.
	 */
	private List<EPPRelatedDomainExtDomain> relatedDomains = null;

	/**
	 * List of related domain names used in {@link #sendRelatedDelete()}, and
	 * {@link #sendRelatedUpdate()}.
	 */
	private List<String> relatedNames = null;

	/**
	 * Constructs an <code>EPPRelatedDomain</code> given an initialized EPP
	 * session.
	 * 
	 * @param aSession
	 *            Server session to use.
	 */
	public EPPRelatedDomain(EPPSession aSession) {
		super(aSession);
	}

	/**
	 * Add a related domain for a call to either {@link #sendRelatedCreate()},
	 * {@link #sendRelatedRenew()}, or {@link #sendRelatedTransfer()}.
	 * 
	 * @param aRelatedDomain
	 *            Related domain to add to the list of related domains.
	 */
	public void addRelatedDomain(EPPRelatedDomainExtDomain aRelatedDomain) {
		if (this.relatedDomains == null) {
			this.relatedDomains = new ArrayList<EPPRelatedDomainExtDomain>();
		}

		this.relatedDomains.add(aRelatedDomain);
	}

	/**
	 * Add a related domain for a call to either {@link #sendRelatedDelete()},
	 * or {@link #sendRelatedUpdate()}.
	 * 
	 * @param aRelatedName
	 *            Related domain name to add to the list of related domain
	 *            names.
	 */
	public void addRelatedName(String aRelatedName) {
		if (this.relatedNames == null) {
			this.relatedNames = new ArrayList<String>();
		}

		this.relatedNames.add(aRelatedName);
	}

	/**
	 * Send the related domain info, which which supports two different forms
	 * set with the {@link #setInfoForm(String)} method. The two constants
	 * <code>DOMAIN_INFO_FORM</code> and <code>RELATED_INFO_FORM</code> are used
	 * for the form values, with the default being <code>DOMAIN_INFO_FORM</code>
	 * . The attributes of {@link EPPDomain} must be set based on the
	 * requirements for calling {@link EPPDomain#sendInfo()}.
	 * 
	 * @return Standard {@link EPPDomainInfoResp} for
	 *         <code>DOMAIN_INFO_FORM</code> and a standard {@link EPPResponse}
	 *         for <code>RELATED_INFO_FORM</code> with a
	 *         {@link EPPRelatedDomainExtInfData} extension attached.
	 * @throws EPPCommandException
	 *             Error creating and sending the command.
	 */
	public EPPResponse sendRelatedInfo() throws EPPCommandException {

		// Create related domain info extension
		EPPRelatedDomainExtInfo theExt = new EPPRelatedDomainExtInfo(
				this.infoForm);

		// Add extension
		super.addExtension(theExt);

		EPPResponse response;

		try {
			response = super.sendInfo();
		}
		catch (EPPCommandException ex) {

			response = super.getResponse();

			if (response == null) {
				throw new EPPCommandException(
						"EPPRelatedDomain.sendRelatedInfo : Error sending command",
						response);
			}

			if (!response.isSuccess()) {
				throw new EPPCommandException(
						"EPPRelatedDomain.sendRelatedInfo : Error in response from Server",
						response);
			}
		}

		// Reset domain attributes
		resetDomain();

		// process the command and response
		return response;
	}

	/**
	 * Send the related domain create, which allows for the create of more than
	 * one domain name. At least one related domain must be set using the
	 * {@link #addRelatedDomain(EPPRelatedDomainExtDomain)} method. The
	 * attributes of {@link EPPDomain} must be set based on the requirements for
	 * calling {@link EPPDomain#sendCreate()}.
	 * 
	 * @return Standard {@link EPPDomainCreateResp} with a
	 *         {@link EPPRelatedDomainExtCreateResp} extension attached.
	 * @throws EPPCommandException
	 *             Error creating and sending the command.
	 */
	public EPPDomainCreateResp sendRelatedCreate() throws EPPCommandException {

		EPPDomainCreateResp response;

		// No related domains?
		if (this.relatedDomains == null || this.relatedDomains.isEmpty()) {
			throw new EPPCommandException(
					"EPPRelatedDomain.sendRelatedCreate : No related domains defined.");
		}

		EPPRelatedDomainExtCreate create = new EPPRelatedDomainExtCreate();
		create.setDomains(this.relatedDomains);
		super.addExtension(create);

		response = super.sendCreate();

		// Reset domain attributes
		resetDomain();

		return response;
	}

	/**
	 * Send the related domain delete, which allows for the deletion of more
	 * than one domain name. At least one related domain name must be set using
	 * the {@link #addRelatedName(String)} method. The attributes of
	 * {@link EPPDomain} must be set based on the requirements for calling
	 * {@link EPPDomain#sendDelete()}.
	 * 
	 * @return Standard {@link EPPResponse} with a
	 *         {@link EPPRelatedDomainExtDeleteResp} extension attached.
	 * @throws EPPCommandException
	 *             Error creating and sending the command.
	 */
	public EPPResponse sendRelatedDelete() throws EPPCommandException {

		EPPResponse response;

		// No related domain names?
		if (this.relatedNames == null || this.relatedNames.isEmpty()) {
			throw new EPPCommandException(
					"EPPRelatedDomain.sendRelatedDelete : No related domain names defined.");
		}

		EPPRelatedDomainExtDelete delete = new EPPRelatedDomainExtDelete();
		delete.setDomains(this.relatedNames);
		super.addExtension(delete);

		response = super.sendDelete();

		// Reset domain attributes
		resetDomain();

		return response;
	}

	/**
	 * Send the related domain transfer, which allows for the transfer of more
	 * than one domain name. At least one related domain must be set using the
	 * {@link #addRelatedDomain(EPPRelatedDomainExtDomain)} method. The
	 * attributes of {@link EPPDomain} must be set based on the requirements for
	 * calling {@link EPPDomain#sendTransfer()}.
	 * 
	 * @return Standard {@link EPPDomainTransferResp} with a
	 *         {@link EPPRelatedDomainExtTransferResp} extension attached.
	 * @throws EPPCommandException
	 *             Error creating and sending the command.
	 */
	public EPPDomainTransferResp sendRelatedTransfer()
			throws EPPCommandException {

		EPPDomainTransferResp response;

		// No related domain names?
		if (this.relatedDomains == null || this.relatedDomains.isEmpty()) {
			throw new EPPCommandException(
					"EPPRelatedDomain.sendRelatedTransfer : No related domains defined.");
		}

		EPPRelatedDomainExtTransfer transfer = new EPPRelatedDomainExtTransfer();
		transfer.setDomains(this.relatedDomains);
		super.addExtension(transfer);

		response = super.sendTransfer();

		// Reset domain attributes
		resetDomain();

		return response;
	}

	/**
	 * Send the related domain renew, which allows for the renewal of more than
	 * one domain name. At least one related domain must be set using the
	 * {@link #addRelatedDomain(EPPRelatedDomainExtDomain)} method. The
	 * attributes of {@link EPPDomain} must be set based on the requirements for
	 * calling {@link EPPDomain#sendRenew()}.
	 * 
	 * @return Standard {@link EPPDomainRenewResp} with a
	 *         {@link EPPRelatedDomainExtRenewResp} extension attached.
	 * @throws EPPCommandException
	 *             Error creating and sending the command.
	 */
	public EPPDomainRenewResp sendRelatedRenew() throws EPPCommandException {

		EPPDomainRenewResp response;

		// No related domain names?
		if (this.relatedDomains == null || this.relatedDomains.isEmpty()) {
			throw new EPPCommandException(
					"EPPRelatedDomain.sendRelatedRenew : No related domains defined.");
		}

		EPPRelatedDomainExtRenew renew = new EPPRelatedDomainExtRenew();
		renew.setDomains(this.relatedDomains);
		super.addExtension(renew);

		response = super.sendRenew();

		// Reset domain attributes
		resetDomain();

		return response;
	}

	/**
	 * Send the related domain update, which allows for the update of more than
	 * one domain name. At least one related domain name must be set using the
	 * {@link #addRelatedName(String)} method. The attributes of
	 * {@link EPPDomain} must be set based on the requirements for calling
	 * {@link EPPDomain#sendUpdate()}.
	 * 
	 * @return Standard {@link EPPResponse}.
	 * @throws EPPCommandException
	 *             Error creating and sending the command.
	 */
	public EPPResponse sendRelatedUpdate() throws EPPCommandException {

		EPPResponse response;

		// No related domain names?
		if (this.relatedNames == null || this.relatedNames.isEmpty()) {
			throw new EPPCommandException(
					"EPPRelatedDomain.sendRelatedUpdate : No related domain names defined.");
		}

		EPPRelatedDomainExtUpdate update = new EPPRelatedDomainExtUpdate();
		update.setDomains(this.relatedNames);
		super.addExtension(update);

		response = super.sendUpdate();

		// Reset domain attributes
		resetDomain();

		return response;
	}

	/**
	 * Resets the domain instance to its initial state.
	 */
	protected void resetDomain() {
		super.resetDomain();
		this.relatedDomains = null;
		this.relatedNames = null;
		this.infoForm = DOMAIN_INFO_FORM;
	}

	/**
	 * Sets the info form type. The XML schema defines the default as
	 * {@link #DOMAIN_INFO_FORM} if undefined.
	 * 
	 * @param aType
	 *            {@link #DOMAIN_INFO_FORM} or {@link #RELATED_INFO_FORM}
	 */
	public void setInfoForm(String aType) {
		this.infoForm = aType;
	}

}
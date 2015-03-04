/***********************************************************
Copyright (C) 2013 VeriSign, Inc.

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

package com.verisign.epp.codec.registry;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * This class holds a {@code List} of supported status used in
 * {@link EPPRegistryDomain}, {@link EPPRegistryHost} and
 * {@link EPPRegistryContact}, as per RFC 5731, 5732 and 5733, respectively.
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryDomain
 * @see com.verisign.epp.codec.registry.EPPRegistryHost
 * @see com.verisign.epp.codec.registry.EPPRegistryContact
 */
public class EPPRegistrySupportedStatus implements EPPCodecComponent {
	private static final long serialVersionUID = 8543267756169534269L;

	/**
	 * XML Element Name of <code>EPPRegistrySupportedStatus</code> root element.
	 */
	public static final String ELM_NAME = "registry:supportedStatus";
	/** XML Element Name of <code>status</code> attribute. */
	public static final String ELM_NAME_STATUS = "registry:status";

	/** {@code List} of status {@code String} */
	private List statuses = new ArrayList();

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistrySupportedStatus} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistrySupportedStatus} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistrySupportedStatus}
	 *                instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistrySupportedStatus.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		EPPUtil.encodeList(aDocument, root, statuses, EPPRegistryMapFactory.NS,
				ELM_NAME_STATUS);

		return root;
	}

	/**
	 * Validate the state of the <code>EPPRegistrySupportedStatus</code>
	 * instance. A valid state means that all of the required attributes have
	 * been set. If validateState returns without an exception, the state is
	 * valid. If the state is not valid, the EPPCodecException will contain a
	 * description of the error. throws EPPCodecException State error. This will
	 * contain the name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if (statuses == null || statuses.size() <= 0) {
			throw new EPPCodecException("statuses is required");
		}
	}

	/**
	 * Decode the {@code EPPRegistrySupportedStatus} attributes from the
	 * aElement DOM Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistrySupportedStatus}
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		statuses = EPPUtil.decodeList(aElement, EPPRegistryMapFactory.NS,
				ELM_NAME_STATUS);
	}

	/**
	 * Clone <code>EPPRegistrySupportedStatus</code>.
	 * 
	 * @return clone of <code>EPPRegistrySupportedStatus</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistrySupportedStatus clone = (EPPRegistrySupportedStatus) super
				.clone();

		if (statuses != null) {
			clone.statuses = (List) ((ArrayList) statuses).clone();
		}

		return clone;
	}

	/**
	 * implements a deep <code>EPPRegistrySupportedStatus</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistrySupportedStatus</code> instance to compare
	 *            with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistrySupportedStatus)) {
			return false;
		}

		EPPRegistrySupportedStatus theComp = (EPPRegistrySupportedStatus) aObject;

		if (!((statuses == null) ? (theComp.statuses == null) : EPPUtil
				.equalLists(statuses, theComp.statuses))) {
			return false;
		}

		return true;
	}

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful;
	 *         <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	/**
	 * Gets the {@code List} of statuses.
	 * 
	 * @return @{code List} of statuses in {@code String}
	 */
	public List getStatuses() {
		return statuses;
	}

	/**
	 * Sets the {@code List} of statuses.
	 * 
	 * @param statuses
	 *            {@code List} of statuses in {@code String}
	 */
	public void setStatuses(List statuses) {
		this.statuses = statuses;
	}

	/**
	 * Add one status to an existing {@code List}.
	 * 
	 * @param status
	 *            status {@code String}
	 */
	public void addStatus(String status) {
		if (statuses == null) {
			statuses = new ArrayList();
		}
		statuses.add(status);
	}

	/**
	 * Defines constants for statuses.
	 * 
	 * @author ljia
	 * 
	 */
	public class Status {
		public static final String DOMAIN_CLIENTDELETEPROHIBITED = "clientDeleteProhibited";
		public static final String DOMAIN_SERVERDELETEPROHIBITED = "serverDeleteProhibited";
		public static final String DOMAIN_CLIENTHOLD = "clientHold";
		public static final String DOMAIN_SERVERHOLD = "serverHold";
		public static final String DOMAIN_CLIENTRENEWPROHIBITED = "clientRenewProhibited";
		public static final String DOMAIN_SERVERRENEWPROHIBITED = "serverRenewProhibited";
		public static final String DOMAIN_CLIENTTRANSFERPROHIBITED = "clientTransferProhibited";
		public static final String DOMAIN_SERVERTRANSFERPROHIBITED = "serverTransferProhibited";
		public static final String DOMAIN_CLIENTUPDATEPROHIBITED = "clientUpdateProhibited";
		public static final String DOMAIN_SERVERUPDATEPROHIBITED = "serverUpdateProhibited";
		public static final String DOMAIN_INACTIVE = "inactive";
		public static final String DOMAIN_OK = "ok";
		public static final String DOMAIN_PENDINGCREATE = "pendingCreate";
		public static final String DOMAIN_PENDINGDELETE = "pendingDelete";
		public static final String DOMAIN_PENDINGRENEW = "pendingRenew";
		public static final String DOMAIN_PENDINGTRANSFER = "pendingTransfer";
		public static final String DOMAIN_PENDINGUPDATE = "pendingUpdate";

		public static final String HOST_CLIENTDELETEPROHIBITED = "clientDeleteProhibited";
		public static final String HOST_SERVERDELETEPROHIBITED = "serverDeleteProhibited";
		public static final String HOST_CLIENTUPDATEPROHIBITED = "clientUpdateProhibited";
		public static final String HOST_SERVERUPDATEPROHIBITED = "serverUpdateProhibited";
		public static final String HOST_LINKED = "linked";
		public static final String HOST_OK = "ok";
		public static final String HOST_PENDINGCREATE = "pendingCreate";
		public static final String HOST_PENDINGDELETE = "pendingDelete";
		public static final String HOST_PENDINGTRANSFER = "pendingTransfer";
		public static final String HOST_PENDINGUPDATE = "pendingUpdate";

		public static final String CONTACT_CLIENTDELETEPROHIBITED = "clientDeleteProhibited";
		public static final String CONTACT_SERVERDELETEPROHIBITED = "serverDeleteProhibited";
		public static final String CONTACT_CLIENTTRANSFERPROHIBITED = "clientTransferProhibited";
		public static final String CONTACT_SERVERTRANSFERPROHIBITED = "serverTransferProhibited";
		public static final String CONTACT_CLIENTUPDATEPROHIBITED = "clientUpdateProhibited";
		public static final String CONTACT_SERVERUPDATEPROHIBITED = "serverUpdateProhibited";
		public static final String CONTACT_LINKED = "linked";
		public static final String CONTACT_OK = "ok";
		public static final String CONTACT_PENDINGCREATE = "pendingCreate";
		public static final String CONTACT_PENDINGDELETE = "pendingDelete";
		public static final String CONTACT_PENDINGTRANSFER = "pendingTransfer";
		public static final String CONTACT_PENDINGUPDATE = "pendingUpdate";
	}
}

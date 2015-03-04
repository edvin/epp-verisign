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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Defined the Registry Grace Period (RGP) status periods. The
 * &lt;registry:rgp&gt; element contains the following child elements, where
 * each child element supports the "unit" attribute with the possible values of
 * "y" for year, "m" for month, "d" for day, and "h" for hour: <br>
 * <br>
 * <ul>
 * <li>&lt;registry:redemptionPeriod&gt; - The length of time that a domain
 * object will remain in the redemptionPeriod status unless the restore request
 * command is received. Use {@link #getRedemptionPeriod()} and
 * {@link #setRedemptionPeriod(EPPRegistryRedemptionPeriodType)} to get and set
 * the element.</li>
 * <li>&lt;registry:pendingRestore&gt; - The length of time that the domain
 * object will remain in the pendingRestore status unless the restore report
 * command is received. Use {@link #getPendingRestorePeriod()} and
 * {@link #setPendingRestorePeriod(EPPRegistryPendingRestorePeriodType)} to get
 * and set the element.</li>
 * <li>&lt;registry:pendingDelete&gt; - The length of time that the domain
 * object will remain in the pendingDelete status prior to be purged. Use
 * {@link #getPendingDeletePeriod()} and
 * {@link #setPendingDeletePeriod(EPPRegistryPendingDeletePeriodType)} to get
 * and set the element.</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryDomain
 * @see com.verisign.epp.codec.registry.EPPRegistryRedemptionPeriodType
 * @see com.verisign.epp.codec.registry.EPPRegistryPendingRestorePeriodType
 * @see com.verisign.epp.codec.registry.EPPRegistryPendingDeletePeriodType
 */
public class EPPRegistryRGP implements EPPCodecComponent {
	private static final long serialVersionUID = -5930890654584746393L;

	/** XML Element Name of <code>EPPRegistryRGP</code> root element. */
	public static final String ELM_NAME = "registry:rgp";

	/** attributes for the redemption period */
	private EPPRegistryRedemptionPeriodType redemptionPeriod = null;

	/** attributes for the pending restore period */
	private EPPRegistryPendingRestorePeriodType pendingRestorePeriod = null;

	/** attributes for the pending delete period */
	private EPPRegistryPendingDeletePeriodType pendingDeletePeriod = null;

	/**
	 * Default constructor. All attributes are initialized to {@code null}. Must
	 * call {@link #setRedemptionPeriod(EPPRegistryRedemptionPeriodType)},
	 * {@link #setPendingRestorePeriod(EPPRegistryPendingRestorePeriodType)} and
	 * {@link #setPendingDeletePeriod(EPPRegistryPendingDeletePeriodType)}
	 * before calling {@link #encode(Document)} method.
	 */
	public EPPRegistryRGP() {
		super();
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryRGP} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the {@code EPPRegistryRGP}
	 *         instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryRGP} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryRGP.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		EPPUtil.encodeComp(aDocument, root, redemptionPeriod);
		EPPUtil.encodeComp(aDocument, root, pendingRestorePeriod);
		EPPUtil.encodeComp(aDocument, root, pendingDeletePeriod);

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryRGP} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryRGP} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		redemptionPeriod = (EPPRegistryRedemptionPeriodType) EPPUtil
				.decodeComp(aElement, EPPRegistryMapFactory.NS,
						EPPRegistryRedemptionPeriodType.ELM_NAME,
						EPPRegistryRedemptionPeriodType.class);
		pendingRestorePeriod = (EPPRegistryPendingRestorePeriodType) EPPUtil
				.decodeComp(aElement, EPPRegistryMapFactory.NS,
						EPPRegistryPendingRestorePeriodType.ELM_NAME,
						EPPRegistryPendingRestorePeriodType.class);
		pendingDeletePeriod = (EPPRegistryPendingDeletePeriodType) EPPUtil
				.decodeComp(aElement, EPPRegistryMapFactory.NS,
						EPPRegistryPendingDeletePeriodType.ELM_NAME,
						EPPRegistryPendingDeletePeriodType.class);
	}

	/**
	 * Validate the state of the <code>EPPRegistryRGP</code> instance. A valid
	 * state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if (redemptionPeriod == null) {
			throw new EPPCodecException("redemptionPeriod element is not set");
		}
		if (pendingRestorePeriod == null) {
			throw new EPPCodecException(
					"pendingRestorePeriod element is not set");
		}
		if (pendingDeletePeriod == null) {
			throw new EPPCodecException(
					"pendingDeletePeriod element is not set");
		}
	}

	/**
	 * Clone <code>EPPRegistryRGP</code>.
	 * 
	 * @return clone of <code>EPPRegistryRGP</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryRGP clone = (EPPRegistryRGP) super.clone();

		if (redemptionPeriod != null) {
			clone.redemptionPeriod = (EPPRegistryRedemptionPeriodType) redemptionPeriod
					.clone();
		}
		if (pendingRestorePeriod != null) {
			clone.pendingRestorePeriod = (EPPRegistryPendingRestorePeriodType) pendingRestorePeriod
					.clone();
		}
		if (pendingDeletePeriod != null) {
			clone.pendingDeletePeriod = (EPPRegistryPendingDeletePeriodType) pendingDeletePeriod
					.clone();
		}

		return clone;
	}

	/**
	 * implements a deep <code>EPPRegistryRGP</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryRGP</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryRGP)) {
			return false;
		}

		EPPRegistryRGP theComp = (EPPRegistryRGP) aObject;

		if (!((redemptionPeriod == null) ? (theComp.redemptionPeriod == null)
				: redemptionPeriod.equals(theComp.redemptionPeriod))) {
			return false;
		}
		if (!((pendingRestorePeriod == null) ? (theComp.pendingRestorePeriod == null)
				: pendingRestorePeriod.equals(theComp.pendingRestorePeriod))) {
			return false;
		}
		if (!((pendingDeletePeriod == null) ? (theComp.pendingDeletePeriod == null)
				: pendingDeletePeriod.equals(theComp.pendingDeletePeriod))) {
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
	 * Get redemption period.
	 * 
	 * @return instance of {@link EPPRegistryRedemptionPeriodType} that defines
	 *         redemption period attributes.
	 */
	public EPPRegistryRedemptionPeriodType getRedemptionPeriod() {
		return redemptionPeriod;
	}

	/**
	 * Set redemption period.
	 * 
	 * @param redemptionPeriod
	 *            instance of {@link EPPRegistryRedemptionPeriodType} that
	 *            defines redemption period attributes.
	 */
	public void setRedemptionPeriod(
			EPPRegistryRedemptionPeriodType redemptionPeriod) {
		this.redemptionPeriod = redemptionPeriod;
	}

	/**
	 * Get pending delete period.
	 * 
	 * @return instance of {@link EPPRegistryPendingDeletePeriodType} that
	 *         defines pending delete period attributes.
	 */
	public EPPRegistryPendingDeletePeriodType getPendingDeletePeriod() {
		return pendingDeletePeriod;
	}

	/**
	 * Set pending delete period.
	 * 
	 * @param pendingDeletePeriod
	 *            instance of {@link EPPRegistryPendingDeletePeriodType} that
	 *            defines pending delete period attributes.
	 */
	public void setPendingDeletePeriod(
			EPPRegistryPendingDeletePeriodType pendingDeletePeriod) {
		this.pendingDeletePeriod = pendingDeletePeriod;
	}

	/**
	 * Get pending restore period.
	 * 
	 * @return instance of {@link EPPRegistryPendingRestorePeriodType} that
	 *         defines pending restore period attributes.
	 */
	public EPPRegistryPendingRestorePeriodType getPendingRestorePeriod() {
		return pendingRestorePeriod;
	}

	/**
	 * Set pending restore period.
	 * 
	 * @param pendingRestorePeriod
	 *            instance of {@link EPPRegistryPendingRestorePeriodType} that
	 *            defines pending restore period attributes.
	 */
	public void setPendingRestorePeriod(
			EPPRegistryPendingRestorePeriodType pendingRestorePeriod) {
		this.pendingRestorePeriod = pendingRestorePeriod;
	}
}

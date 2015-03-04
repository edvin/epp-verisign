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
package com.verisign.epp.codec.gen;


// Log4j Imports
import org.apache.log4j.Logger;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Vector;

import com.verisign.epp.util.EPPCatFactory;


/**
 * Identifies the Recipient DataCollectionPolicy supported by the server:
 * 
 * <ul>
 * <li>
 * &ltother&gt other entities follwoing unknown practices
 * </li>
 * <li>
 * &ltours&gt server operator and or servers operator agents
 * </li>
 * <li>
 * &ltpublic&gt public forums
 * </li>
 * <li>
 * &ltsame&gt   other entities following server practices
 * </li>
 * <li>
 * &ltunrelated&gt unrelated third parties.
 * </li>
 * <li>
 * Use methods <code>setOther</code>, <code>setPublic</code>,
 * <code>setSame</code>, <code>setUnRelated</code> with a boolean value of
 * true to create the necessary child element nodes.  Use <code>setOurs</code>
 * to set a <code>Vector</code> of ours child element nodes with optional
 * descriptions.
 * </li>
 * </ul>
 * 
 *
 * @author $Author: jim $
 * @version $Revision: 1.6 $
 *
 * @see com.verisign.epp.codec.gen.EPPStatement
 */
public class EPPRecipient implements EPPCodecComponent {
	/** Default XML root tag name for <code>recipient</code> element. */
	final static String ELM_NAME = "recipient";

	/*
	 *    XML tag name for the <code>public</code>element
	 */

	/** XML tag name for <code>other</code>. */
	private static final String ELM_OTHER = "other";

	/** XML tag name for <code>ours</code>. */
	private static final String ELM_OURS = "ours";

	/** Optional <code>recDesc</code> element of an <code>ours</code> element. */
	private static final String ELM_OURS_DESC = "recDesc";

	/** XML tag name for the <code>public</code>element */
	private static final String ELM_PUBLIC = "public";

	/** XML tag name for the <code>same</code>element */
	private static final String ELM_SAME = "same";

	/** XML tag name for the <code>unrelated</code>element */
	private static final String ELM_UNRELATED = "unrelated";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPRecipient.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** boolean constant for the other element */
	private boolean otherRecipient = false;

	/** boolean constant for the ours element */
	private Vector oursRecipient = null;

	/** boolean constant for the public element */
	private boolean publicRecipient = false;

	/** boolean constant for the same element */
	private boolean sameRecipient = false;

	/** boolean constant for the unrelated element */
	private boolean unrelatedRecipient = false;

	/**
	 * Default constructor.
	 */
	public EPPRecipient() {
	}

	/**
	 * Allocates a new <code>EPPRecipient</code> with differnt child elements
	 * based on the input parameters. The child element are created based on
	 * the following input parameter values: <br><br>
	 * 
	 * <ul>
	 * <li>
	 * if aOther parameter is set to true then the other element
	 * <code>other</code> is created
	 * </li>
	 * <li>
	 * if aOurs <code>Vector</code> of <code>String</code> that describes the
	 * recipient.   A <code>null String</code> indicates that the recipient
	 * has no description.
	 * </li>
	 * <li>
	 * if aPublic parameter is set to true then the public element
	 * <code>public</code> is created
	 * </li>
	 * <li>
	 * if aSame parameter is set to true then the same element
	 * <code>same</code> is created
	 * </li>
	 * <li>
	 * if aUnrelated parameter is set to true then the unrelated element
	 * <code>unrelated</code> is created
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aOther DOCUMENT ME!
	 * @param aOurs DOCUMENT ME!
	 * @param aPublic DOCUMENT ME!
	 * @param aSame DOCUMENT ME!
	 * @param aUnrelated DOCUMENT ME!
	 */
	public EPPRecipient(
						boolean aOther, Vector aOurs, boolean aPublic,
						boolean aSame, boolean aUnrelated) {
		this.otherRecipient		    = aOther;
		this.oursRecipient		    = aOurs;
		this.publicRecipient	    = aPublic;
		this.sameRecipient		    = aSame;
		this.unrelatedRecipient     = aUnrelated;
	}

	// End EPPRecipient.EPPRecipient()

	/**
	 * &lt;other&gt; element set?
	 *
	 * @return <code>true</code> if is set; <code>false</code> otherwise.
	 */
	public boolean isOther() {
		return this.otherRecipient;
	}

	/**
	 * Sets the &lt;other&gt; element.
	 *
	 * @param aOther <code>true</code> to include the &lt;other&gt; element;
	 * 		  <code>false</code> otherwise.
	 */
	public void setOther(boolean aOther) {
		this.otherRecipient = aOther;
	}

	/**
	 * Gets the ours recipient descriptions.  A <code>null</code> description
	 * indicates a ours recipient without a description.
	 *
	 * @return <code>Vector</code> of <code>String</code> ours descriptions if
	 * 		   defined; <code>null</code> otherwise.
	 */
	public Vector getOurs() {
		return this.oursRecipient;
	}

	/**
	 * Sets the &lt;ours&gt; elements that include a description
	 * <code>String</code> per <code>ours</code> element.  A
	 * <code>null</code><code>String</code> indicates no description for the
	 * &lt;ours&gt; element.
	 *
	 * @param aOurs <code>Vector</code> of nullable <code>String</code>
	 * 		  &lt;ours&gt; descriptions
	 */
	public void setOurs(Vector aOurs) {
		this.oursRecipient = aOurs;
	}

	/**
	 * Add &lt;ours&gt; element with an optional description.  A
	 * non-<code>null</code> aOursDesc represents a description, while a
	 * <code>null</code> value represents a &lt;ours&gt; element without a
	 * description.
	 *
	 * @param aOursDesc A nullable &lt;ours&gt; description
	 */
	public void addOurs(String aOursDesc) {
		if (this.oursRecipient == null) {
			this.oursRecipient = new Vector();
		}

		this.oursRecipient.add(aOursDesc);
	}

	/**
	 * &lt;public&gt; element set?
	 *
	 * @return <code>true</code> if is set; <code>false</code> otherwise.
	 */
	public boolean isPublic() {
		return this.publicRecipient;
	}

	/**
	 * Sets the &lt;public&gt; element to specify public forums.
	 *
	 * @param aPublic <code>true</code> to include the &lt;public&gt; element;
	 * 		  <code>false</code> otherwise.
	 */
	public void setPublic(boolean aPublic) {
		this.publicRecipient = aPublic;
	}

	/**
	 * &lt;same&gt; element set?
	 *
	 * @return <code>true</code> if is set; <code>false</code> otherwise.
	 */
	public boolean isSame() {
		return this.sameRecipient;
	}

	/**
	 * Sets the &lt;same&gt; element to specify other entities following server
	 * practices.
	 *
	 * @param aSame <code>true</code> to include the &lt;same&gt; element;
	 * 		  <code>false</code> otherwise.
	 */
	public void setSame(boolean aSame) {
		this.sameRecipient = aSame;
	}

	/**
	 * &lt;unrelated&gt; element set?
	 *
	 * @return <code>true</code> if is set; <code>false</code> otherwise.
	 */
	public boolean isUnrelated() {
		return this.unrelatedRecipient;
	}

	/**
	 * Sets the &lt;unrelated&gt; element to specify Unrelated third parties.
	 *
	 * @param aUnrelated <code>true</code> to include the &lt;unrelated&gt;
	 * 		  element; <code>false</code> otherwise.
	 */
	public void setUnrelated(boolean aUnrelated) {
		this.unrelatedRecipient = aUnrelated;
	}

	/**
	 * encode <code>EPPRecipient</code> into a DOM element tree.  The
	 * "recipient"     element is created and the child nodes are     appended
	 * as children.
	 *
	 * @param aDocument DOCUMENT ME!
	 *
	 * @return recipient root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Validate that at least one recipient is set
		if (
			!this.otherRecipient && (this.oursRecipient == null)
				&& !this.publicRecipient && !this.sameRecipient
				&& !this.unrelatedRecipient) {
			cat.error("EPPRecipient.encode(): At least one recipient must be set");
			throw new EPPEncodeException("EPPRecipient.encode(): At least one recipient must be set");
		}

		Element theElm = null;
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		// Other
		if (this.otherRecipient) {
			theElm = aDocument.createElementNS(EPPCodec.NS, ELM_OTHER);
			root.appendChild(theElm);
		}

		// Ours
		if (this.oursRecipient != null) {
			Iterator theIter = oursRecipient.iterator();

			while (theIter.hasNext()) {
				Element theOursElm =
					aDocument.createElementNS(EPPCodec.NS, ELM_OURS);
				root.appendChild(theOursElm);

				String theDesc = (String) theIter.next();

				if (theDesc != null) {
					EPPUtil.encodeString(
										 aDocument, theOursElm, theDesc,
										 EPPCodec.NS, ELM_OURS_DESC);
				}
			}

			// end while (theIter.hasNext())
		}

		// end if (this.ourRecipient != null)
		// Public
		if (this.publicRecipient) {
			theElm = aDocument.createElementNS(EPPCodec.NS, ELM_PUBLIC);
			root.appendChild(theElm);
		}

		// Same
		if (this.sameRecipient) {
			theElm = aDocument.createElementNS(EPPCodec.NS, ELM_SAME);
			root.appendChild(theElm);
		}

		// Unrelated
		if (this.unrelatedRecipient) {
			theElm = aDocument.createElementNS(EPPCodec.NS, ELM_UNRELATED);
			root.appendChild(theElm);
		}

		return root;
	}

	// End EPPRecipient.encode(Document)

	/**
	 * Reset all attribute to their initial values.
	 */
	private void reset() {
		otherRecipient		   = false;
		oursRecipient		   = null;
		publicRecipient		   = false;
		sameRecipient		   = false;
		unrelatedRecipient     = false;
	}

	// End reset()

	/**
	 * decode <code>EPPRecipient</code> from a DOM element tree.  The
	 * <code>aElement</code> argument needs to be the &ltrecipient&gt element
	 *
	 * @param aElement root element tree.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		NodeList theList = aElement.getChildNodes();

		reset();

		// For each child node
		for (int i = 0; i < theList.getLength(); i++) {
			Node theNode = theList.item(i);

			if (theNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element theElm = (Element) theNode;

			if (theElm.getLocalName().equals(EPPUtil.getLocalName(ELM_OTHER))) {
				this.otherRecipient = true;
			}
			else if (theElm.getLocalName().equals(ELM_OURS)) {
				String theDesc =
					EPPUtil.decodeString(theElm, EPPCodec.NS, EPPUtil.getLocalName(ELM_OURS_DESC));
				this.addOurs(theDesc);
			}
			else if (theElm.getLocalName().equals(EPPUtil.getLocalName(ELM_PUBLIC))) {
				this.publicRecipient = true;
			}
			else if (theElm.getLocalName().equals(EPPUtil.getLocalName(ELM_SAME))) {
				this.sameRecipient = true;
			}
			else if (theElm.getLocalName().equals(EPPUtil.getLocalName(ELM_UNRELATED))) {
				this.unrelatedRecipient = true;
			}
			else {
				cat.error("EPPRecipient.decode(): Unknown element " + theElm);
				throw new EPPDecodeException("EPPRecipient.decode(): Unknown element "
											 + theElm);
			}
		}

		// end for 
	}

	// End EPPRecipient.decode(Element)

	/**
	 * implements a deep <code>EPPRecipient</code> compare.
	 *
	 * @param aObject <code>EPPRecipient</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRecipient)) {
			cat.error("EPPRecipient.equals(): " + aObject.getClass().getName()
					  + " not EPPRecipient instance");

			return false;
		}

		EPPRecipient theRecipient = (EPPRecipient) aObject;

		if (this.otherRecipient != theRecipient.otherRecipient) {
			cat.error("EPPRecipient.equals(): otherRecipient not equal");

			return false;
		}

		if (
			!EPPUtil.equalVectors(
									  this.oursRecipient,
										  theRecipient.oursRecipient)) {
			cat.error("EPPRecipient.equals(): oursRecipient not equal");

			return false;
		}

		if (this.publicRecipient != theRecipient.publicRecipient) {
			cat.error("EPPRecipient.equals(): publicRecipient not equal");

			return false;
		}

		if (this.sameRecipient != theRecipient.sameRecipient) {
			cat.error("EPPRecipient.equals(): sameRecipient not equal");

			return false;
		}

		if (this.unrelatedRecipient != theRecipient.unrelatedRecipient) {
			cat.error("EPPRecipient.equals(): unrelatedRecipient not equal");

			return false;
		}

		return true;
	}

	// End EPPRecipient.equals(Object)

	/**
	 * Clone <code>EPPRecipient</code>.
	 *
	 * @return clone of <code>EPPRecipient</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRecipient clone = null;

		clone = (EPPRecipient) super.clone();

		// Ours
		if (this.oursRecipient != null) {
			clone.oursRecipient = (Vector) this.oursRecipient.clone();
		}
		else {
			clone.oursRecipient = null;
		}

		return clone;
	}

	// End EPPRecipient.clone()*/

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 *
	 * @return Indented XML <code>String</code> if successful;
	 * 		   <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	// End EPPRecipient.toString()
}


// End class EPPRecipient

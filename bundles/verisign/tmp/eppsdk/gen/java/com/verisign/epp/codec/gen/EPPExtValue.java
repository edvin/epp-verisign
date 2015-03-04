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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.verisign.epp.util.EPPCatFactory;


/**
 * elements that can be used to provide additional error diagnostic
 * information, including: <br><br>
 * 
 * <ul>
 * <li>
 * A &lt;value&gt; element that identifies a client-provided element (including
 * XML tag and value) that caused a server error condition.
 * </li>
 * <li>
 * A &lt;reason&gt; element containing a human-readable message that describes
 * the reason for the error.  The language of the response is identified via
 * an OPTIONAL "lang" attribute.  If not specified, the default attribute
 * value MUST be "en" (English).
 * </li>
 * </ul>
 */
public class EPPExtValue implements EPPCodecComponent {
	/** The default language of the result message "en". */
	public static final String DEFAULT_LANG = "en";

	/**
	 * Default value which is used when there is only a reason and the server
	 * can not identify a single client element that caused the error.
	 */
	public static final String DEFAULT_VALUE = "<epp:undef/>";

	/** XML root tag name for <code>EPPExtValue</code>. */
	final static String ELM_NAME = "extValue";

	/** XML tag name for the reason attribute. */
	private static final String ELM_REASON = "reason";

	/**
	 * XML attribute name for the optional result <code>lang</code> attribute.
	 */
	private final static String ATTR_LANG = "lang";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPExtValue.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * identifies a client-provided element (including XML tag and value) that
	 * caused a server error condition.
	 */
	private EPPValue value = null;

	/** human-readable message that describes the reason for the error. */
	private String reason;

	/**
	 * Language of the <code>reason</code> attribute.  A value of null
	 * indicates     the default value of "en".
	 */
	private String lang = DEFAULT_LANG;

	/**
	 * Default constructor for serialization.  The <code>value</code> and
	 * <code>reason</code> attributes must be set before  calling
	 * <code>encode</code>.
	 */
	public EPPExtValue() {
		// Do nothing
	}

	/**
	 * Allocates a new <code>EPPExtValue</code> with only a <code>reason</code>
	 * attributes specified.  The value  will be set to the
	 * <code>DEFAULT_VALUE</code> constant, which indicates that no client
	 * element is specified. The default XML prefix and XML namespace will be
	 * used.
	 *
	 * @param aReason Human-readable message that describes the reason for the
	 * 		  error.
	 */
	public EPPExtValue(String aReason) {
		this.reason     = aReason;
		this.value	    = new EPPValue(DEFAULT_VALUE);
	}

	// End EPPExtValue.EPPExtValue(String)

	/**
	 * Allocates a new <code>EPPExtValue</code> with both the
	 * <code>value</code> and <code>reason</code> attributes specified. The
	 * default XML prefix and XML namespace will be used.
	 *
	 * @param aReason Human-readable message that describes the reason for the
	 * 		  error.
	 * @param aValue XML <code>String</code> that identifies a client-provided
	 * 		  element (including XML tag and value) that caused a server
	 * 		  error.  For example,
	 * 		  &lt;domain:name&gt;example.com&lt;/domain&gt;.
	 */
	public EPPExtValue(String aReason, String aValue) {
		this.reason     = aReason;
		this.value	    = new EPPValue(aValue);
	}

	// End EPPExtValue.EPPExtValue(String, String)

	/**
	 * Allocates a new <code>EPPExtValue</code> with both the
	 * <code>value</code> and <code>reason</code> attributes specified.
	 *
	 * @param aReason Human-readable message that describes the reason for the
	 * 		  error.
	 * @param aValue Value will a value <code>String</code> and  XML prefix and
	 * 		  namespace.
	 */
	public EPPExtValue(String aReason, EPPValue aValue) {
		this.reason     = aReason;
		this.value	    = aValue;
	}

	// End EPPExtValue.EPPExtValue(String, EPPValue)

	/**
	 * Allocates a new <code>EPPExtValue</code> with all attributes specified.
	 * This is a convenience constructor in place of using
	 * <code>EPPExtValue(String, EPPValue)</code>.
	 *
	 * @param aReason Human-readable message that describes the reason for the
	 * 		  error.
	 * @param aValue XML <code>String</code> that identifies a client-provided
	 * 		  element (including XML tag and value) that caused a server
	 * 		  error.  For example,
	 * 		  &lt;domain:name&gt;example.com&lt;/domain&gt;.
	 * @param aPrefix XML Namespace prefix.  For example, "domain" or "epp".
	 * @param aNamespace XML Namespace URI.  For example,
	 * 		  "urn:ietf:params:xml:ns:domain-1.0".
	 */
	public EPPExtValue(
					   String aReason, String aValue, String aPrefix,
					   String aNamespace) {
		this.reason     = aReason;
		this.value	    = new EPPValue(aValue, aPrefix, aNamespace);
	}

	// End EPPExtValue.EPPExtValue(String, EPPValue)

	/**
	 * Gets the value element associated with the error.
	 *
	 * @return The value object that includes the XML value with prefix and
	 * 		   namespace.
	 */
	public EPPValue getValue() {
		return this.value;
	}

	// End EPPExtValue.getValue()

	/**
	 * Sets the value associated with the error.
	 *
	 * @param aValue The value object that includes the XML value with prefix
	 * 		  and namespace.
	 */
	public void setValue(EPPValue aValue) {
		this.value = aValue;
	}

	// End EPPExtValue.setValue(EPPValue)

	/**
	 * Gets the value element <code>String</code> associated with the error.
	 *
	 * @return Contained <code>EPPValue String</code> value if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getValueStr() {
		if (this.value != null) {
			return this.value.getValue();
		}
		else {
			return null;
		}
	}

	// End EPPExtValue.getValueStr()

	/**
	 * Gets the human-readable message that describes the reason for the error.
	 * The language defaults to  <code>DEFAULT_LANG</code>, but the value can
	 * be retrieved with the <code>getLang</code>  method.
	 *
	 * @return the human-readable message that describes the reason for the
	 * 		   error.
	 */
	public String getReason() {
		return this.reason;
	}

	// End EPPExtValue.getReason()

	/**
	 * Sets the human-readable message that describes the reason for the error.
	 * The language defaults to  <code>DEFAULT_LANG</code>, but can be set
	 * with the <code>setLang</code>  method.
	 *
	 * @param aReason Human-readable message that describes the reason for the
	 * 		  error.
	 */
	public void setReason(String aReason) {
		this.reason = aReason;
	}

	// End EPPExtValue.setReason(String)

	/**
	 * Gets the reason language.   The Language must be structured as
	 * documented in <a
	 * href="http://www.ietf.org/rfc/rfc1766.txt?number=1766">[RFC1766]</a>.
	 *
	 * @return Language of the reason.
	 */
	public String getLang() {
		return this.lang;
	}

	// End EPPResult.getLang()

	/**
	 * Sets the reason language.   The Language must be structured as
	 * documented in <a
	 * href="http://www.ietf.org/rfc/rfc1766.txt?number=1766">[RFC1766]</a>.
	 *
	 * @param aLang Language of the reason.
	 */
	public void setLang(String aLang) {
		if ((aLang == null) || (aLang.equals(""))) {
			this.lang = DEFAULT_LANG;
		}
		else {
			this.lang = aLang;
		}
	}

	// End EPPResult.setMessage(String)

	/**
	 * encode <code>EPPExtValue</code> into a DOM element tree.
	 *
	 * @param aDocument DOCUMENT ME!
	 *
	 * @return &lt;extValue&gt; root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element currElm;
		Text    currVal;

		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		// Value
		EPPUtil.encodeComp(aDocument, root, this.value);

		// Reason
		currElm     = aDocument.createElementNS(EPPCodec.NS, ELM_REASON);
		currVal     = aDocument.createTextNode(this.reason);
		currElm.appendChild(currVal);
		root.appendChild(currElm);

		// Lang
		if (!this.lang.equals(DEFAULT_LANG)) {
			currElm.setAttribute(ATTR_LANG, this.lang);
		}

		return root;
	}

	// End EPPExtValue.encode(Document)

	/**
	 * decode <code>EPPExtValue</code> from a DOM element tree.  The
	 * <code>aElement</code> argument needs to be the "extValue" element.
	 *
	 * @param aElement The "extValue" XML element.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// Value
		this.value =
			(EPPValue) EPPUtil.decodeComp(
										  aElement, EPPCodec.NS,
										  EPPValue.ELM_NAME, EPPValue.class);

		// Reason
		Element theReasonElm =
			EPPUtil.getElementByTagNameNS(aElement, EPPCodec.NS, ELM_REASON);

		if (theReasonElm == null) {
			cat.error("EPPExtValue.decode(): Required element " + ELM_REASON
					  + " not found");
			throw new EPPDecodeException("EPPExtValue.decode(): Required element "
										 + ELM_REASON + " not found");
		}

		Node theTextNode = theReasonElm.getFirstChild();
		if (theTextNode != null)
			this.reason = theTextNode.getNodeValue();
		else 
			this.reason = "";

		// Lang
		this.setLang(theReasonElm.getAttribute(ATTR_LANG));
	}

	// End EPPExtValue.decode(Element)

	/**
	 * implements a deep <code>EPPExtValue</code> compare.
	 *
	 * @param aObject <code>EPPExtValue</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPExtValue)) {
			cat.error("EPPExtValue.equals(): " + aObject.getClass().getName()
					  + " not EPPExtValue instance");

			return false;
		}

		EPPExtValue theExtValue = (EPPExtValue) aObject;

		// Value
		if (
			!(
					(this.value == null) ? (theExtValue.value == null)
											 : this.value.equals(theExtValue.value)
				)) {
			cat.error("EPPExtValue.equals(): value not equal");

			return false;
		}

		// Reason
		if (
			!(
					(this.reason == null) ? (theExtValue.reason == null)
											  : this.reason.equals(theExtValue.reason)
				)) {
			cat.error("EPPExtValue.equals(): reason not equal");

			return false;
		}

		// Lang
		if (!this.lang.equals(theExtValue.lang)) {
			cat.error("EPPExtValue.equals(): lang not equal");

			return false;
		}

		return true;
	}

	// End EPPExtValue.equals(Object)

	/**
	 * Clone <code>EPPExtValue</code>.
	 *
	 * @return Deep copy clone of <code>EPPExtValue</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPExtValue clone = null;

		clone = (EPPExtValue) super.clone();

		if (this.value != null) {
			clone.value = (EPPValue) this.value.clone();
		}

		return clone;
	}

	// End EPPExtValue.clone()

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

	// End EPPExtValue.toString()
}


// End class EPPExtValue

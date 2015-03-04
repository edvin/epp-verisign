/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

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
package com.verisign.epp.codec.defReg;

import org.w3c.dom.Document;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.Text;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents EPPDefRegName information which is a shared structure been used
 * by other objects.     The &lt;defReg:name&gt; element MUST contain the
 * following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;defReg:level&gt;attribute  that maps to either "premium" or
 * "standard". .Use <code>getName</code> and <code>setName</code>     to get
 * and set the Names. Use <code>getLevel</code> and <code>setLevel</code> to
 * get and set the level attribute.
 * </li>
 * </ul>
 */
public class EPPDefRegName
	implements com.verisign.epp.codec.gen.EPPCodecComponent {
	/**
	 * Standard level of Defensive Registration.  For example, use this level
	 * when checking or creating name "john.smith".
	 */
	public static final String LEVEL_STANDARD = "standard";

	/**
	 * Premium level of Defensive Registration.  For example, use this level
	 * when checking or creating name "smith".
	 */
	public static final String LEVEL_PREMIUM = "premium";

	/** XML Element name of <code>name</code>  element. */
	final static String ELM_NAME = "defReg:name";

	/**
	 * XML Element type attribute name of <code>level</code> associated with
	 * name element.
	 */
	protected final static String ATTR_LEVEL = "level";

	/** name information. */
	protected String name = null;

	/** level informnation */
	protected String level = null;

	/**
	 * Root mapping name such as defReg (e.g. <code>defReg:Name</code> for
	 * defReg mapping). This attribute needs to be specified before calling
	 * encode(Document) method.
	 */
	private String rootName = ELM_NAME;

	/**
	 * Default constructor, which will set the level and name attributes to
	 * <code>null</code>.  These attributes must be set before calling
	 * <code>encode</code>.
	 */
	public EPPDefRegName() {
	}

	/**
	 * EPPDefRegName which takes attributes as arguments (aLevel, aName).
	 *
	 * @param aLevel Either <code>LEVEL_STANDARD</code> or
	 * 		  <code>LEVEL_PREMIUM</code>
	 * @param aName name
	 */
	public EPPDefRegName(String aLevel, String aName) {
		level     = aLevel;
		name	  = aName;
	}

	// End EPPDefRegName.EPPDefRegName(String, String)

	/**
	 * Validate the state of the <code>EPPDefRegCreateCmd</code> instance.  A
	 * valid state means that all of the required attributes have been set. If
	 * validateState     returns without an exception, the state is valid. If
	 * the state is not     valid, the <code>EPPCodecException</code> will
	 * contain a description of the error.     throws     EPPCodecException
	 * State error.  This will contain the name of the     attribute that is
	 * not valid.
	 *
	 * @throws EPPEncodeException DOCUMENT ME!
	 */
	public void validate() throws EPPEncodeException {
		if (name == null) {
			throw new EPPEncodeException("name required attribute is not set");
		}

		if (level == null) {
			throw new EPPEncodeException("level required attribute is not set");
		}

		if (!level.equals(LEVEL_STANDARD) && !level.equals(LEVEL_PREMIUM)) {
			throw new EPPEncodeException("Invalid level of " + level + " set");
		}

		if (level.equals(LEVEL_STANDARD)) {
			// Name does not contain a "."?
			if (name.indexOf(".") == -1) {
				throw new EPPEncodeException("standard level should atleast contain a dot in name");
			}
		}
		else // LEVEL_PREMIUM
		 {
			// Name does contain a "."?
			if (name.indexOf(".") != -1) {
				throw new EPPEncodeException("premium level should Not contain a dot in name");
			}
		}
	}

	/**
	 * Decode the EPPDefRegName attributes from the aElement DOM Element tree.
	 *
	 * @param aElement - Root DOM Element to decode EPPDomainContact from.
	 *
	 * @exception EPPDecodeException - Unable to decode aElement.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// level
		level     = aElement.getAttribute(ATTR_LEVEL);

		// name
		name = aElement.getFirstChild().getNodeValue();
	}

	// End EPPAuthInfo.decode(Element)

	/**
	 * Encode a DOM Element tree from the attributes of the EPPDefRegName
	 * instance.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    - Root DOM Element representing the EPPDefRegName
	 * 		   instance.
	 *
	 * @exception EPPEncodeException - Unable to encode EPPDefRegName instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validate();
		}
		 catch (EPPEncodeException ex) {
			throw new EPPEncodeException("Invalid state on EPPDefRegName.encode: "
										 + ex);
		}

		Element root = aDocument.createElementNS(EPPDefRegMapFactory.NS, rootName);
		root.setAttribute(ATTR_LEVEL, level);

		Text textNode = aDocument.createTextNode(name);
		root.appendChild(textNode);

		return root;
	}

	// End EPPDefRegName.encode(Document)

	/**
	 * implements a deep <code>EPPDefRegName</code> compare.
	 *
	 * @param aObject <code>EPPDefRegName</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegName)) {
			return false;
		}

		EPPDefRegName theComp = (EPPDefRegName) aObject;

		// name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}

		// level
		if (
			!(
					(level == null) ? (theComp.level == null)
										: level.equals(theComp.level)
				)) {
			return false;
		}

		return true;
	}

	// End EPPAuthInfo.equals(Object)

	/**
	 * Clone <code>EPPDefRegName</code>.
	 *
	 * @return clone of <code>EPPDefRegName</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegName clone = null;

		clone = (EPPDefRegName) super.clone();

		return clone;
	}

	// End EPPDefRegName.clone()

	/**
	 * get Name attribute assocaited with defRegName
	 *
	 * @return String of EPPDefRegName name.
	 */
	public String getName() {
		return name;
	}

	// End EPPDefReg.getName()

	/**
	 * get Level assocaited with defRegName
	 *
	 * @return String of EPPDefRegName level if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public String getLevel() {
		return level;
	}

	// End EPPDefReg.getLevel()

	/**
	 * Sets Name attribute.
	 *
	 * @param aName Sets defRegName Name  attribute.
	 */
	public void setName(String aName) {
		name = aName;
	}

	/**
	 * Sets Level attribute.
	 *
	 * @param aLevel Either <code>LEVEL_STANDARD</code> or
	 * 		  <code>LEVEL_PREMIUM</code>
	 */
	public void setLevel(String aLevel) {
		level = aLevel;
	}

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

	// End EPPDefRegName.toString()
}

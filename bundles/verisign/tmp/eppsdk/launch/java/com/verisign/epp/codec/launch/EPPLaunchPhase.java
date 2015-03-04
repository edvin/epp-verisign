/***********************************************************
Copyright (C) 2012 VeriSign, Inc.

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
package com.verisign.epp.codec.launch;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

public class EPPLaunchPhase implements EPPCodecComponent {
	
	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPLaunchPhase.class.getName(),
			EPPCatFactory.getInstance().getFactory());
	
	/**
	 * Phase when trademark holders can submit registrations or applications
	 * with trademark information that can be validated by.
	 */
	public final static String PHASE_SUNRISE = "sunrise";

	/**
	 * Post sunrise phase when non-trademark holders are allowed to register
	 * domain names with steps taken to address a large volume of initial
	 * registrations.
	 */
	public final static String PHASE_LANDRUSH = "landrush";

	/**
	 * Trademark claims phase 1 as defined by Trademark Clearinghouse model of
	 * displaying a claims notice to clients for domain names
	 * that match trademarks.
	 */
	public final static String PHASE_CLAIMS = "claims";

	/**
	 * Post launch phase that is also referred to as "steady state". Servers MAY
	 * require additional trademark protection with this phase.
	 */
	public final static String PHASE_OPEN = "open";

	/**
	 * Post launch phase that is also referred to as "steady state". Servers MAY
	 * require additional trademark protection with this phase.
	 */
	public final static String PHASE_CUSTOM = "custom";

	/**
	 * Constant for the phase local name
	 */
	public static final String ELM_LOCALNAME = "phase";
	
	/**
	 * Constant for the phase qualified name (prefix and local name)
	 */
	public static final String ELM_NAME = EPPLaunchExtFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;
	

	/**
	 * OPTIONAL attribute name that can define a sub-phase or the full name of
	 * the phase when the &lt;launch:phase&gt; element has the "custom" value.
	 */
	public static final String ATTR_NAME = "name";

	/**
	 * Launch phase name using using of the <code>PHASE</code> constants.
	 */
	private String phase;

	/**
	 * OPTIONAL "name" attribute that can define a sub-phase or the full name of
	 * the phase when the &lt;launch:phase&gt; element has the "custom" value.
	 */
	private String name;
	
	/**
	 * Default constructor.  The phase value MUST be set using the 
	 * {@link #setPhase(String)} method.
	 */
	public EPPLaunchPhase() {
	}
	
	/**
	 * Create <code>EPPLaunchPhase</code> instance with a defined phase value.
	 * 
	 * @param aPhase Phase value using one of the <code>PHASE</code> constants.
	 */
	public EPPLaunchPhase(String aPhase) {
		this.phase = aPhase;
	}
	
	/**
	 * Create <code>EPPLaunchPhase</code> instance with a defined phase value and 
	 * with the phase name value.
	 * 
	 * @param aPhase Phase value using one of the <code>PHASE</code> constants.
	 * @param aName Name of sub-phase or full name of phase when <code>aPhase</code> 
	 * is set to <code>PHASE_CUSTOM</code>.
	 */
	public EPPLaunchPhase(String aPhase, String aName) {
		this.phase = aPhase;
		this.name = aName;
	}

	/**
	 * Gets the phase value, which should match one of the <code>PHASE</code> constants.
	 * @return Phase value
	 */
	public String getPhase() {
		return this.phase;
	}

	/**
	 * Sets the phase value.
	 * @param aPhase Phase value, which should be one of the <code>PHASE</code> constants.
	 */
	public void setPhase(String aPhase) {
		this.phase = aPhase;
	}

	/**
	 * Gets the OPTIONAL phase name or sub-phase name.  
	 * @return The phase name or sub-phase name if defined; </code>null</code> otherwise.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the OPTIONAL phase name or sub-phase name.
	 * @param aName Phase name
	 */
	public void setName(String aName) {
		this.name = aName;
	}
	
	/**
	 * Clone <code>EPPLaunchPhase</code>.
	 *
	 * @return clone of <code>EPPLaunchPhase</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPLaunchPhase clone = null;

		clone = (EPPLaunchPhase) super.clone();

		return clone;
	}
	
	/**
	 * Decode the <code>EPPLaunchPhase</code> element aElement DOM Element tree.
	 *
	 * @param aElement - Root DOM Element to decode <code>EPPLaunchPhase</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// name
		String theName = aElement.getAttribute(ATTR_NAME);
		if (theName != null && !theName.isEmpty()) {
			this.name = theName;
		}
		else {
			this.name = null;
		}

		// phase
		Node textNode = aElement.getFirstChild();

		if (textNode != null) {
			this.phase = textNode.getNodeValue();
		}
	}
	
	/**
	 * Encode a DOM Element tree from the attributes of the <code>EPPLaunchPhase</code>
	 * instance.
	 *
	 * @param aDocument - DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Element    - Root DOM Element representing the <code>EPPLaunchPhase</code>
	 * 		   instance.
	 *
	 * @exception EPPEncodeException - Unable to encode <code>EPPLaunchPhase</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		
		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " on in EPPLaunchPhase.encode(Document)");
		}
		if (this.phase == null) {
			throw new EPPEncodeException("phase is null"
					+ " on in EPPLaunchPhase.encode(Document)");			
		}
		
		// Status with Attributes
		Element root =
			aDocument.createElementNS(EPPLaunchExtFactory.NS, ELM_NAME);

		// name
		if (this.name != null) {
			root.setAttribute(ATTR_NAME, this.name);
		}

		// phase
		Text phaseText = aDocument.createTextNode(this.phase);
		root.appendChild(phaseText);

		return root;
	}
	
	/**
	 * implements a deep <code>EPPLaunchPhase</code> compare.
	 *
	 * @param aObject <code>EPPLaunchPhase</code> instance to compare with
	 *
	 * @return <code>true</code> if equal; <code>false</code> otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPLaunchPhase)) {
			return false;
		}

		EPPLaunchPhase other = (EPPLaunchPhase) aObject;

		// name
		if (!EqualityUtil.equals(this.name, other.name)) {
			cat.error("EPPLaunchPhase.equals(): name not equal");
			return false;
		}
		
		
		// phase
		if (!EqualityUtil.equals(this.phase, other.phase)) {
			cat.error("EPPLaunchPhase.equals(): phase not equal");
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
	 * 		   <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}
	
}

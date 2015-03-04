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

import com.verisign.epp.util.EPPCatFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.w3c.dom.*;

import javax.xml.bind.DatatypeConverter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Provides a set of utility static methods for use by the EPP Codec classes.
 */
public class EPPUtil {
	/** Default format used for the XML Schema <code>dateTime</code> data type */
	public static final String DEFAULT_TIME_INSTANT_FORMAT = "yyyy-MM-dd'T'HH':'mm':'ss'.'SSS'Z'";

	/**
	 * Format used for the XML Schema <code>dateTime</code> data type in the
	 * {@link #encodeTimeInstant(Date) and #encodeTimeInstant(Document, Element,
	 * Date, String, String)} methods supported by the {@link SimpleDateFormat}.
	 */
	private static String timeInstantFormat = DEFAULT_TIME_INSTANT_FORMAT;

	/** Format used for the XML Schema date data type. */
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPUtil.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	/**
	 * Used to format decimal values according to the US locale to remove locale
	 * specific impacts when encoding decimal values.
	 */
	private static DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(
			Locale.US);

    /**
     * Gets the XML Schema <code>timeDate</code> format used 
     * in {@link #encodeTimeInstant(Date) and #encodeTimeInstant(Document, Element,
	 * Date, String, String)}.  The defauLt format is defined by the constant 
	 * {@link #DEFAULT_TIME_INSTANT_FORMAT} and it can be overridden using 
	 * the {@link #setTimeInstantFormat(String)} method.  
	 *     
     * @return XML Schema <code>timeDate</code> format used 
     * in {@link #encodeTimeInstant(Date) and #encodeTimeInstant(Document, Element,
	 * Date, String, String)}.
     */
	public static String getTimeInstantFormat() {
		return timeInstantFormat;
	}

	/**
	 * Sets the XML Schema <code>timeDate</code> format used 
     * in {@link #encodeTimeInstant(Date) and #encodeTimeInstant(Document, Element,
	 * Date, String, String)}.  The format must follow the format 
	 * supported by {@link SimpleDateFormat}.    
	 * 
	 * @param aTimeInstantFormat XML Schema <code>timeDate</code> format 
	 * supported by {@link SimpleDateFormat}. 
	 */
	public static void setTimeInstantFormat(String aTimeInstantFormat) {
		timeInstantFormat = aTimeInstantFormat;
	}
	
	
	/**
	 * Appends the children Nodes of <code>aSource</code> as children nodes of
	 * <code>aDest</code>.
	 * 
	 * @param aSource
	 *            Source Element tree to get children from
	 * @param aDest
	 *            Destination Element to append <code>aSource</code> children.
	 */
	public static void appendChildren(Element aSource, Element aDest) {
		NodeList children = aSource.getChildNodes();

		for (int i = 0; i < children.getLength(); i++) {
			aDest.appendChild(children.item(i).cloneNode(true));
		}
	}

	// End EPPUtil.encodeDate(Date)

	/**
	 * Decode <code>BigDecimal</code>, by XML namespace and tag name, from an
	 * XML Element. The children elements of <code>aElement</code> will be
	 * searched for the specified <code>aNS</code> namespace URI and the
	 * specified <code>aTagName</code>. The first XML element found will be
	 * decoded and returned. If no element is found, <code>null</code> is
	 * returned.
	 * 
	 * @param aElement
	 *            XML Element to scan. For example, the element could be
	 *            &ltdomain:create&gt
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name is
	 *            "domain:name".
	 * @return <code>BigDecimal</code> value if found; <code>null</code>
	 *         otherwise.
	 * @exception EPPDecodeException
	 *                Error decoding <code>aElement</code>.
	 */
	public static BigDecimal decodeBigDecimal(Element aElement, String aNS,
			String aTagName) throws EPPDecodeException {

		Element theElm = EPPUtil.getElementByTagNameNS(aElement, aNS, aTagName);

		BigDecimal retBigDecimal = null;
		if (theElm != null) {
			Node textNode = theElm.getFirstChild();

			// Element does have a text node?
			if (textNode != null) {

				String doubleValStr = textNode.getNodeValue();
				try {

					Double tempDouble = Double.valueOf(doubleValStr);
					retBigDecimal = new BigDecimal(tempDouble.doubleValue());
					retBigDecimal = retBigDecimal.setScale(2,
							BigDecimal.ROUND_HALF_UP);
				}
				catch (NumberFormatException e) {
					throw new EPPDecodeException(
							"Can't convert value to Double: " + doubleValStr
									+ e);
				}
			}
			else {
				throw new EPPDecodeException(
						"Can't decode numeric value from non-existant text node");
			}
		}
		return retBigDecimal;
	}

	// End EPPUtil.decodeDate(String)

	/**
	 * Decode <code>Boolean</code>, by XML namespace and tag name, from an XML
	 * Element. The children elements of <code>aElement</code> will be searched
	 * for the specified <code>aNS</code> namespace URI and the specified
	 * <code>aTagName</code>. The first XML element found will be decoded and
	 * returned. If no element is found, <code>null</code> is returned.
	 * 
	 * @param aElement
	 *            XML Element to scan. For example, the element could be
	 *            &ltdomain:create&gt
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name is
	 *            "domain:name".
	 * @return <code>Boolean</code> value if found; <code>null</code> otherwise.
	 * @exception EPPDecodeException
	 *                Error decoding <code>aElement</code>.
	 */
	public static Boolean decodeBoolean(Element aElement, String aNS,
			String aTagName) throws EPPDecodeException {
		Boolean retVal = null;
		String theVal = null;

		Element theElm = EPPUtil.getElementByTagNameNS(aElement, aNS, aTagName);

		if (theElm != null) {
			Node textNode = theElm.getFirstChild();

			// Element does have a text node?
			if (textNode != null) {
				theVal = textNode.getNodeValue();

				if (theVal.equalsIgnoreCase("true") || theVal.equals("1")) {
					retVal = new Boolean(true);
				}
				else {
					retVal = new Boolean(false);
				}
			}
			else {
				retVal = null;
			}
		}

		return retVal;
	}

	// End EPPUtil.encodeTimeInstant(Date)

	/**
	 * Decodes a boolean attribute value given the <code>Element</code> and
	 * attribute name.
	 * 
	 * @param aElement
	 *            Element with the attribute to look for
	 * @param aAttr
	 *            Attribute name
	 * @return Decoded boolean value
	 * @exception EPPDecodeException
	 *                Cound not find attribute or the attribute value is not a
	 *                valid boolean value
	 */
	public static boolean decodeBooleanAttr(Element aElement, String aAttr)
			throws EPPDecodeException {
		boolean theRet = false;

		String theAttrVal = aElement.getAttribute(aAttr);

		if (theAttrVal == null) {
			throw new EPPDecodeException(
					"EPPUtil.decodeBooleanAttr: Could not find attr " + aAttr);
		}

		if (theAttrVal.equals("1") || theAttrVal.equals("true")) {
			theRet = true;
		}
		else if (theAttrVal.equals("0") || theAttrVal.equals("false")) {
			theRet = false;
		}
		else {
			throw new EPPDecodeException(
					"EPPUtil.decodeBooleanAttr: Invalid boolean attr " + aAttr
							+ " value of " + theAttrVal);
		}

		return theRet;
	}

	// End EPPUtil.decodeTimeInstant(String)

	/**
	 * Decode a <code>EPPCodecComponent</code>, by XML namespace and tag name,
	 * from an XML Element. The children elements of <code>aElement</code> will
	 * be searched for the specified <code>aNS</code> namespace URI and the
	 * specified <code>aTagName</code>. There first XML element found will be
	 * used to initial and instance of <code>aClass</code>.
	 * 
	 * @param aElement
	 *            XML Element to scan. For example, the element could be
	 *            &ltdomain:update&gt
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name servers
	 *            is "domain:server".
	 * @param aClass
	 *            Class to instantiate for a found XML element. This must be a
	 *            class that implements <code>EPPCodecComponent</code>
	 * @return Instance of <code>aClass</code> that represents the found XML
	 *         elements if found; <code>null</code> otherwise.
	 * @exception EPPDecodeException
	 *                Error decoding <code>aElement</code>.
	 */
	public static EPPCodecComponent decodeComp(Element aElement, String aNS,
			String aTagName, Class aClass) throws EPPDecodeException {
		EPPCodecComponent retVal = null;

		try {
			Element theElm = EPPUtil.getElementByTagNameNS(aElement, aNS,
					aTagName);

			if (theElm != null) {
				retVal = (EPPCodecComponent) aClass.newInstance();

				retVal.decode(theElm);
			}
		}
		catch (IllegalAccessException e) {
			throw new EPPDecodeException(
					"EPPUtil.decodeComp(), IllegalAccessException: " + e);
		}
		catch (InstantiationException e) {
			throw new EPPDecodeException(
					"EPPUtil.decodeComp(), InstantiationException: " + e);
		}

		return retVal;
	}

	/**
	 * Decode a <code>List</code> of <code>EPPCodecComponent</code>'s, by XML
	 * namespace and tag name, from an XML Element. The children elements of
	 * <code>aElement</code> will be searched for the specified <code>aNS</code>
	 * namespace URI and the specified <code>aTagName</code>. Each XML element
	 * found will result in the instantiation of <code>aClass</code>, which will
	 * decode the associated XML element and added to the returned
	 * <code>List</code>.
	 * 
	 * @param aElement
	 *            XML Element to scan. For example, the element could be
	 *            &ltdomain:add&gt
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the contact elements of
	 *            &gtdomain:add&gt is "domain:contact".
	 * @param aClass
	 *            Class to instantiate for each found XML element. This must be
	 *            a class that implements <code>EPPCodecComponent</code>
	 * @return <code>List</code> of <code>EPPCodecComponent</code> elements
	 *         representing the found XML elements.
	 * @exception EPPDecodeException
	 *                Error decoding <code>aElement</code>.
	 */
	public static List decodeCompList(Element aElement, String aNS,
			String aTagName, Class aClass) throws EPPDecodeException {
		List retVal = new ArrayList();

		try {
			Vector theChildren = EPPUtil.getElementsByTagNameNS(aElement, aNS,
					aTagName);

			// For each element
			for (int i = 0; i < theChildren.size(); i++) {
				EPPCodecComponent currComp = (EPPCodecComponent) aClass
						.newInstance();

				currComp.decode((Element) theChildren.elementAt(i));

				retVal.add(currComp);
			}

			// end for each element
		}
		catch (IllegalAccessException e) {
			throw new EPPDecodeException(
					"EPPUtil.decodeCompList(), IllegalAccessException: " + e);
		}
		catch (InstantiationException e) {
			throw new EPPDecodeException(
					"EPPUtil.decodeCompList(), InstantiationException: " + e);
		}

		return retVal;
	}

	/**
	 * Decode a <code>Vector</code> of <code>EPPCodecComponent</code>'s, by XML
	 * namespace and tag name, from an XML Element. The children elements of
	 * <code>aElement</code> will be searched for the specified <code>aNS</code>
	 * namespace URI and the specified <code>aTagName</code>. Each XML element
	 * found will result in the instantiation of <code>aClass</code>, which will
	 * decode the associated XML element and added to the returned
	 * <code>Vector</code>.
	 * 
	 * @param aElement
	 *            XML Element to scan. For example, the element could be
	 *            &ltdomain:add&gt
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the contact elements of
	 *            &gtdomain:add&gt is "domain:contact".
	 * @param aClass
	 *            Class to instantiate for each found XML element. This must be
	 *            a class that implements <code>EPPCodecComponent</code>
	 * @return <code>Vector</code> of <code>EPPCodecComponent</code> elements
	 *         representing the found XML elements.
	 * @exception EPPDecodeException
	 *                Error decoding <code>aElement</code>.
	 */
	public static Vector decodeCompVector(Element aElement, String aNS,
			String aTagName, Class aClass) throws EPPDecodeException {
		Vector retVal = new Vector();

		try {
			Vector theChildren = EPPUtil.getElementsByTagNameNS(aElement, aNS,
					aTagName);

			// For each element
			for (int i = 0; i < theChildren.size(); i++) {
				EPPCodecComponent currComp = (EPPCodecComponent) aClass
						.newInstance();

				currComp.decode((Element) theChildren.elementAt(i));

				retVal.addElement(currComp);
			}

			// end for each element
		}
		catch (IllegalAccessException e) {
			throw new EPPDecodeException(
					"EPPUtil.decodeCompVector(), IllegalAccessException: " + e);
		}
		catch (InstantiationException e) {
			throw new EPPDecodeException(
					"EPPUtil.decodeCompVector(), InstantiationException: " + e);
		}

		return retVal;
	}

	/**
	 * Decode <code>Date</code>, by XML namespace and tag name, from an XML
	 * Element. The children elements of <code>aElement</code> will be searched
	 * for the specified <code>aNS</code> namespace URI and the specified
	 * <code>aTagName</code>. The first XML element found will be decoded and
	 * returned. If no element is found, <code>null</code> is returned. The
	 * format used for decoding the date is defined by the constant
	 * <code>EPPUtil.DATE_FORMAT</code>.
	 * 
	 * @param aElement
	 *            XML Element to scan. For example, the element could be
	 *            &lttrans-id&gt
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:epp".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the transaction Id date
	 *            is "date".
	 * @return <code>Date</code> value if found; <code>null</code> otherwise.
	 * @exception EPPDecodeException
	 *                Error decoding <code>aElement</code>.
	 */
	public static Date decodeDate(Element aElement, String aNS, String aTagName)
			throws EPPDecodeException {
		Date retVal = null;

		Element theElm = EPPUtil.getElementByTagNameNS(aElement, aNS, aTagName);

		if (theElm != null) {
			retVal = EPPUtil.decodeDate(theElm.getFirstChild().getNodeValue());
		}

		return retVal;
	}

	/**
	 * Decode an XML Schema date data type (YYYY-MM-DD) to a Java Date object.
	 * 
	 * @param aDateValue
	 *            XML Schema date data type string (YYYY-MM-DD).
	 * @return Java Date object.
	 */
	public static Date decodeDate(String aDateValue) {
		
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

		// Set to UTC with no time element
		Calendar theCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		theCal.set(Calendar.HOUR_OF_DAY, 0);
		theCal.set(Calendar.MINUTE, 0);
		theCal.set(Calendar.SECOND, 0);
		theCal.set(Calendar.MILLISECOND, 0);
		formatter.setCalendar(theCal);

		Date theDate = formatter.parse(aDateValue, new ParsePosition(0));
		
		return theDate;
	}

	/**
	 * Decode <code>Integer</code>, by XML namespace and tag name, from an XML
	 * Element. The children elements of <code>aElement</code> will be searched
	 * for the specified <code>aNS</code> namespace URI and the specified
	 * <code>aTagName</code>. The first XML element found will be decoded and
	 * returned. If no element is found, <code>null</code> is returned.
	 * 
	 * @param aElement
	 *            XML Element to scan. For example, the element could be
	 *            &ltdomain:create&gt
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name is
	 *            "domain:name".
	 * @return <code>Integer</code> value if found; <code>null</code> otherwise.
	 * @exception EPPDecodeException
	 *                Error decoding <code>aElement</code>.
	 */
	public static Integer decodeInteger(Element aElement, String aNS,
			String aTagName) throws EPPDecodeException {

		Element theElm = EPPUtil.getElementByTagNameNS(aElement, aNS, aTagName);

		Integer retInteger = null;
		if (theElm != null) {
			Node textNode = theElm.getFirstChild();

			// Element does have a text node?
			if (textNode != null) {

				String intValStr = textNode.getNodeValue();
				try {
					retInteger = Integer.valueOf(intValStr);
				}
				catch (NumberFormatException e) {

					throw new EPPDecodeException(
							"Can't convert value to Integer: " + intValStr + e);
				}
			}
			else {
				throw new EPPDecodeException(
						"Can't decode numeric value from non-existant text node");
			}
		}
		return retInteger;
	}

	/**
	 * Decode a <code>List</code> of <code>String</code>'s by XML namespace and
	 * tag name, from an XML Element. The children elements of
	 * <code>aElement</code> will be searched for the specified <code>aNS</code>
	 * namespace URI and the specified <code>aTagName</code>. Each XML element
	 * found will be decoded and added to the returned <code>List</code>. Empty
	 * child elements, will result in empty string ("") return <code>List</code>
	 * elements.
	 * 
	 * @param aElement
	 *            XML Element to scan. For example, the element could be
	 *            &ltdomain:update&gt
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name servers
	 *            is "domain:server".
	 * @return <code>List</code> of <code>String</code> elements representing
	 *         the text nodes of the found XML elements.
	 * @exception EPPDecodeException
	 *                Error decoding <code>aElement</code>.
	 */
	public static List decodeList(Element aElement, String aNS, String aTagName)
			throws EPPDecodeException {
		List retVal = new ArrayList();

		Vector theChildren = EPPUtil.getElementsByTagNameNS(aElement, aNS,
				aTagName);

		for (int i = 0; i < theChildren.size(); i++) {
			Element currChild = (Element) theChildren.elementAt(i);

			Text currVal = (Text) currChild.getFirstChild();

			// Element has text?
			if (currVal != null) {
				retVal.add(currVal.getNodeValue());
			}
			else { // No text in element.
				retVal.add("");
			}
		}

		return retVal;
	} // End EPPUtil.decodeList(Element, String, String)

	/**
	 * Decode <code>String</code>, by XML namespace and tag name, from an XML
	 * Element. The children elements of <code>aElement</code> will be searched
	 * for the specified <code>aNS</code> namespace URI and the specified
	 * <code>aTagName</code>. The first XML element found will be decoded and
	 * returned. If no element is found, <code>null</code> is returned.
	 * 
	 * @param aElement
	 *            XML Element to scan. For example, the element could be
	 *            &ltdomain:create&gt
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name is
	 *            "domain:name".
	 * @return <code>String</code> value if found; <code>null</code> otherwise.
	 * @exception EPPDecodeException
	 *                Error decoding <code>aElement</code>.
	 */
	public static String decodeString(Element aElement, String aNS,
			String aTagName) throws EPPDecodeException {
		String retVal = null;

		Element theElm = EPPUtil.getElementByTagNameNS(aElement, aNS, aTagName);

		if (theElm != null) {
			Node textNode = theElm.getFirstChild();

			// Element does have a text node?
			if (textNode != null) {
				retVal = textNode.getNodeValue();
			}
			else {
				retVal = "";
			}
		}

		// end if (currElm != null)
		return retVal;
	}

	/**
	 * Decode <code>Date</code>, as date and time, by XML namespace and tag
	 * name, from an XML Element. The children elements of <code>aElement</code>
	 * will be searched for the specified <code>aNS</code> namespace URI and the
	 * specified <code>aTagName</code>. The first XML element found will be
	 * decoded and returned. If no element is found, <code>null</code> is
	 * returned. The format used for decoding the date is defined by the
	 * constant <code>EPPUtil.TIME_INSTANT_FORMAT</code>.
	 * 
	 * @param aElement
	 *            XML Element to scan. For example, the element could be
	 *            &lttrans-id&gt
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:epp".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the transaction Id date
	 *            is "date".
	 * @return <code>Date</code> value as date and time if found;
	 *         <code>null</code> otherwise.
	 * @exception EPPDecodeException
	 *                Error decoding <code>aElement</code>.
	 */
	public static Date decodeTimeInstant(Element aElement, String aNS,
			String aTagName) throws EPPDecodeException {
		Date retVal = null;

		Element theElm = EPPUtil.getElementByTagNameNS(aElement, aNS, aTagName);

		if (theElm != null) {
			retVal = EPPUtil.decodeTimeInstant(theElm.getFirstChild()
					.getNodeValue());
		}

		return retVal;
	}

	/**
	 * Decode an XML Schema timeInstant data type to a Java Date object. 
	 * 
	 * @param aTimeInstant
	 *            XML Schema timeInstant data type string.
	 * @return Java Date object if <code>aTimeInstant</code> format is valid;
	 *         <code>null</code> otherwise
	 */
	public static Date decodeTimeInstant(String aTimeInstant) {
		Date theDate = null;
		
		try {
			theDate = DatatypeConverter.parseDateTime(aTimeInstant).getTime();
		}
		catch (IllegalArgumentException ex) {
			cat.error("Exception decoding dataTime: " + ex);
			theDate = null;
		}
		
		return theDate;
	}

	/**
	 * Decode a <code>Vector</code> of <code>String</code>'s by XML namespace
	 * and tag name, from an XML Element. The children elements of
	 * <code>aElement</code> will be searched for the specified <code>aNS</code>
	 * namespace URI and the specified <code>aTagName</code>. Each XML element
	 * found will be decoded and added to the returned <code>Vector</code>.
	 * Empty child elements, will result in empty string ("") return
	 * <code>Vector</code> elements.
	 * 
	 * @param aElement
	 *            XML Element to scan. For example, the element could be
	 *            &ltdomain:update&gt
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name servers
	 *            is "domain:server".
	 * @return <code>Vector</code> of <code>String</code> elements representing
	 *         the text nodes of the found XML elements.
	 * @exception EPPDecodeException
	 *                Error decoding <code>aElement</code>.
	 */
	public static Vector decodeVector(Element aElement, String aNS,
			String aTagName) throws EPPDecodeException {
		Vector retVal = new Vector();

		Vector theChildren = EPPUtil.getElementsByTagNameNS(aElement, aNS,
				aTagName);

		for (int i = 0; i < theChildren.size(); i++) {
			Element currChild = (Element) theChildren.elementAt(i);

			Text currVal = (Text) currChild.getFirstChild();

			// Element has text?
			if (currVal != null) {
				retVal.addElement(currVal.getNodeValue());
			}
			else { // No text in element.
				retVal.addElement("");
			}
		}

		return retVal;
	} // End EPPUtil.decodeVector(Element, String, String)

	/**
	 * Encode a <code>Double</code> in XML with a given XML namespace and tag
	 * name. Takes an optional argument for the format of the Double, if not
	 * provided then it uses #0.00 which formats the double to 2 decimal places.
	 * 
	 * @param aDocument
	 *            DOM Document of <code>aRoot</code>. This parameter also acts
	 *            as a factory for XML elements.
	 * @param aRoot
	 *            XML Element to add children nodes to. For example, the root
	 *            node could be &ltdomain:update&gt
	 * @param aBigDecimal
	 *            <code>Double</code> to add.
	 * @param aNS
	 *            XML namespace of the element. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name is
	 *            "domain:name".
	 * @exception EPPEncodeException
	 *                Error encoding the <code>Double</code>.
	 */
	public static void encodeBigDecimal(Document aDocument, Element aRoot,
			BigDecimal aBigDecimal, String aNS, String aTagName, String aFormat)
			throws EPPEncodeException {

		if (aBigDecimal != null) {

			String format = aFormat != null ? aFormat : "#0.00";
			DecimalFormat decFormat = new DecimalFormat(format,
					decimalFormatSymbols);

			String numberStr = decFormat.format(aBigDecimal);
			EPPUtil.encodeString(aDocument, aRoot, numberStr, aNS, aTagName);
		}
	}

	/**
	 * Encode a <code>Boolean</code> in XML with a given XML namespace and tag
	 * name. If aBoolean is <code>null</code> an element is not added.
	 * 
	 * @param aDocument
	 *            DOM Document of <code>aRoot</code>. This parameter also acts
	 *            as a factory for XML elements.
	 * @param aRoot
	 *            XML Element to add children nodes to. For example, the root
	 *            node could be &ltdomain:update&gt
	 * @param aBoolean
	 *            <code>Boolean</code> to add.
	 * @param aNS
	 *            XML namespace of the element. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name is
	 *            "domain:name".
	 * @exception EPPEncodeException
	 *                Error encoding the <code>Boolean</code>.
	 */
	public static void encodeBoolean(Document aDocument, Element aRoot,
			Boolean aBoolean, String aNS, String aTagName)
			throws EPPEncodeException {
		if (aBoolean != null) {
			Element currElm = aDocument.createElementNS(aNS, aTagName);

			String xmlValue;

			if (aBoolean.booleanValue()) {
				xmlValue = "1";
			}
			else {
				xmlValue = "0";
			}

			Text currVal = aDocument.createTextNode(xmlValue);

			currElm.appendChild(currVal);
			aRoot.appendChild(currElm);
		}
	}

	/**
	 * Encodes a boolean <code>Element</code> attribute value. The attribute is
	 * set to &quot;1&quot; with a value of <code>true</code> and is set to
	 * &quot;0&quot; with a value of <code>false</code>.
	 * 
	 * @param aElement
	 *            Element to add attribute to
	 * @param aAttr
	 *            Attribute name
	 * @param aVal
	 *            Attribute boolean value
	 */
	public static void encodeBooleanAttr(Element aElement, String aAttr,
			boolean aVal) {
		if (aVal) {
			aElement.setAttribute(aAttr, "1");
		}
		else {
			aElement.setAttribute(aAttr, "0");
		}
	}

	// End EPPUtil.encodeComp(Document, Element, EPPCodecComponent)

	/**
	 * Encode a <code>EPPCodecComponent</code> instance in XML. The component is
	 * first checked for not being <code>null</code>, than it will be appended
	 * as a child of <code>aRoot</code>.
	 * 
	 * @param aDocument
	 *            DOM Document of <code>aRoot</code>. This parameter also acts
	 *            as a factory for XML elements.
	 * @param aRoot
	 *            XML Element to add to. For example, the root node could be
	 *            &ltdomain:update&gt
	 * @param aComp
	 *            <code>EPPCodecComponent's</code> to add.
	 * @exception EPPEncodeException
	 *                Error encoding <code>EPPCodecComponent</code>.
	 */
	public static void encodeComp(Document aDocument, Element aRoot,
			EPPCodecComponent aComp) throws EPPEncodeException {
		if (aComp != null) {
			aRoot.appendChild(aComp.encode(aDocument));
		}
	}

	// End EPPUtil.decodeComp(Element, String, String, Class)

	/**
	 * Encode a <code>List</code> of <code>EPPCodecComponent</code>'s in XML.
	 * Each <code>aList</code> element will be encoded and added to
	 * <code>aRoot</code>.
	 * 
	 * @param aDocument
	 *            DOM Document of <code>aRoot</code>. This parameter also acts
	 *            as a factory for XML elements.
	 * @param aRoot
	 *            XML Element to add children nodes to. For example, the root
	 *            node could be &ltdomain:update&gt
	 * @param aList
	 *            <code>List</code> of <code>EPPCodecComponent's</code> to add.
	 * @exception EPPEncodeException
	 *                Error encoding the <code>List</code> of
	 *                <code>EPPCodecComponent</code>'s.
	 */
	public static void encodeCompList(Document aDocument, Element aRoot,
			List aList) throws EPPEncodeException {
		if (aList != null) {
			Iterator elms = aList.iterator();

			while (elms.hasNext()) {
				EPPCodecComponent currComp = (EPPCodecComponent) elms.next();

				aRoot.appendChild(currComp.encode(aDocument));
			}

			// End while (elms.hasMoreElements())
		}
	} // End EPPUtil.encodeCompList(Document,Element, List)

	// End EPPUtil.encodeString(Document, Element, String, String, String)

	/**
	 * Encode a <code>Vector</code> of <code>EPPCodecComponent</code>'s in XML.
	 * Each <code>aVector</code> element will be encoded and added to
	 * <code>aRoot</code>.
	 * 
	 * @param aDocument
	 *            DOM Document of <code>aRoot</code>. This parameter also acts
	 *            as a factory for XML elements.
	 * @param aRoot
	 *            XML Element to add children nodes to. For example, the root
	 *            node could be &ltdomain:update&gt
	 * @param aVector
	 *            <code>Vector</code> of <code>EPPCodecComponent's</code> to add.
	 * @exception EPPEncodeException
	 *                Error encoding the <code>Vector</code> of
	 *                <code>EPPCodecComponent</code>'s.
	 */
	public static void encodeCompVector(Document aDocument, Element aRoot,
			Vector aVector) throws EPPEncodeException {
		if (aVector != null) {
			Enumeration elms = aVector.elements();

			while (elms.hasMoreElements()) {
				EPPCodecComponent currComp = (EPPCodecComponent) elms
						.nextElement();

				aRoot.appendChild(currComp.encode(aDocument));
			}

			// End while (elms.hasMoreElements())
		}
	} // End EPPUtil.encodeVector(Document,Element, Vector)

	// End EPPUtil.decodeString(Element, String, String)

	/**
	 * Encode a Java Date into an XML Schema date data type string.
	 * 
	 * @param aDate
	 *            Java Date to encode into a XML Schema date data type string.
	 * @return Encoded XML Schema date data type string.
	 */
	public static String encodeDate(Date aDate) {
		TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		formatter.setTimeZone(utcTimeZone);

		return formatter.format(aDate);
	}

	// End EPPUtil.encodeBoolean(Document, Element, Boolean, String, String)

	/**
	 * Encode a <code>Date</code> in XML with a given XML namespace and tag
	 * name. The format used for encoding the date is defined by the constant
	 * <code>EPPUtil.DATE_FORMAT</code>.
	 * 
	 * @param aDocument
	 *            DOM Document of <code>aRoot</code>. This parameter also acts
	 *            as a factory for XML elements.
	 * @param aRoot
	 *            XML Element to add children nodes to. For example, the root
	 *            node could be &ltdomain:update&gt
	 * @param aDate
	 *            <code>Date</code> to add.
	 * @param aNS
	 *            XML namespace of the element. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name is
	 *            "domain:name".
	 * @exception EPPEncodeException
	 *                Error encoding <code>Date</code>.
	 */
	public static void encodeDate(Document aDocument, Element aRoot,
			Date aDate, String aNS, String aTagName) throws EPPEncodeException {
		if (aDate != null) {
			encodeString(aDocument, aRoot, EPPUtil.encodeDate(aDate), aNS,
					aTagName);
		}
	}

	// End EPPUtil.decodeBoolean(Element, String, String)

	/**
	 * Encode a <code>List</code> of <code>String</code>'s in XML with a given
	 * XML namespace and tag name.
	 * 
	 * @param aDocument
	 *            DOM Document of <code>aRoot</code>. This parameter also acts
	 *            as a factory for XML elements.
	 * @param aRoot
	 *            XML Element to add children nodes to. For example, the root
	 *            node could be &ltdomain:update&gt
	 * @param aList
	 *            <code>List</code> of <code>String's</code> to add.
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name servers
	 *            is "domain:server".
	 * @exception EPPEncodeException
	 *                Error encoding the <code>List</code> of
	 *                <code>String</code>'s.
	 */
	public static void encodeList(Document aDocument, Element aRoot,
			List aList, String aNS, String aTagName) throws EPPEncodeException {
		Element currElm;
		Text currVal;

		if (aList != null) {
			Iterator elms = aList.iterator();

			while (elms.hasNext()) {
				currElm = aDocument.createElementNS(aNS, aTagName);
				currVal = aDocument.createTextNode(elms.next().toString());

				currElm.appendChild(currVal);
				aRoot.appendChild(currElm);
			}

			// end while (elms.hasMoreElements())
		}

		// end if (aList != null)
	} // End EPPUtil.encodeList(Document,Element, List, String, String)

	/**
	 * DOCUMENT ME!
	 * 
	 * @param aDocument
	 *            DOCUMENT ME!
	 * @param aRoot
	 *            DOCUMENT ME!
	 * @param aNS
	 *            DOCUMENT ME!
	 * @param aTagName
	 *            DOCUMENT ME!
	 * @throws EPPEncodeException
	 *             DOCUMENT ME!
	 */
	public static void encodeNill(Document aDocument, Element aRoot,
			String aNS, String aTagName) throws EPPEncodeException {
		Element localElement = aDocument.createElementNS(aNS, aTagName);

		localElement.setAttribute("xsi:nil", "true");
		aRoot.appendChild(localElement);
	}

	/**
	 * Encode a <code>String</code> in XML with a given XML namespace and tag
	 * name.
	 * 
	 * @param aDocument
	 *            DOM Document of <code>aRoot</code>. This parameter also acts
	 *            as a factory for XML elements.
	 * @param aRoot
	 *            XML Element to add children nodes to. For example, the root
	 *            node could be &ltdomain:update&gt
	 * @param aString
	 *            <code>String</code> to add.
	 * @param aNS
	 *            XML namespace of the element. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name is
	 *            "domain:name".
	 * @exception EPPEncodeException
	 *                Error encoding the <code>String</code>.
	 */
	public static void encodeString(Document aDocument, Element aRoot,
			String aString, String aNS, String aTagName)
			throws EPPEncodeException {
		if (aString != null) {
			Element currElm = aDocument.createElementNS(aNS, aTagName);
			Text currVal = aDocument.createTextNode(aString);

			currElm.appendChild(currVal);
			aRoot.appendChild(currElm);
		}
	}

	// End EPPUtil.encodeDate(Document, Element, Date, String, String)

	public static void encodeStringList(Document aDocument, Element aRoot,
			List aList, String aNS, String aTagName) {
		String aString;
		Element aElement;
		Text aValue;

		if (aList != null) {
			Iterator elms = aList.iterator();

			while (elms.hasNext()) {
				aString = (String) elms.next();
				aElement = aDocument.createElementNS(aNS, aTagName);
				aValue = aDocument.createTextNode(aString);
				aElement.appendChild(aValue);
				aRoot.appendChild(aElement);
			}
		}
	}

	// End EPPUtil.decodeDate(Element, String, String)

	/**
	 * Encode a Java Date into an XML Schema timeInstant data type string. The
	 * XML Schema timeInstant data type string has the following format:
	 * CCYY-MM-DDThh:mm:ss.ssss can be followed by a Z to indicate Coordinated
	 * Universal Time
	 * 
	 * @param aDate
	 *            Java Date to encode into a XML Schema timeInstant data type
	 *            string.
	 * @return Encoded XML Schema timeInstant data type string.
	 */
	public static String encodeTimeInstant(Date aDate) {
		TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
		SimpleDateFormat formatter = new SimpleDateFormat(timeInstantFormat);
		formatter.setTimeZone(utcTimeZone);

		return formatter.format(aDate);
	}

	// End EPPUtil.encodeTimeInstance(Document, Element, Date, String, String)

	/**
	 * Encode a <code>Date</code>, as date and time, in XML with a given XML
	 * namespace and tag name. The format used for encoding the date is defined
	 * by the constant <code>EPPUtil.TIME_INSTANT_FORMAT</code>.
	 * 
	 * @param aDocument
	 *            DOM Document of <code>aRoot</code>. This parameter also acts
	 *            as a factory for XML elements.
	 * @param aRoot
	 *            XML Element to add children nodes to. For example, the root
	 *            node could be &ltdomain:expire-data&gt
	 * @param aDate
	 *            <code>Date</code> as date and time to add.
	 * @param aNS
	 *            XML namespace of the element. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain expiration
	 *            date and time is "domain:expiration-date".
	 * @exception EPPEncodeException
	 *                Error encoding <code>Date</code>.
	 */
	public static void encodeTimeInstant(Document aDocument, Element aRoot,
			Date aDate, String aNS, String aTagName) throws EPPEncodeException {
		if (aDate != null) {
			encodeString(aDocument, aRoot, EPPUtil.encodeTimeInstant(aDate),
					aNS, aTagName);
		}
	}

	/**
	 * Encode a <code>Vector</code> of <code>String</code>'s in XML with a given
	 * XML namespace and tag name.
	 * 
	 * @param aDocument
	 *            DOM Document of <code>aRoot</code>. This parameter also acts
	 *            as a factory for XML elements.
	 * @param aRoot
	 *            XML Element to add children nodes to. For example, the root
	 *            node could be &ltdomain:update&gt
	 * @param aVector
	 *            <code>Vector</code> of <code>String's</code> to add.
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name servers
	 *            is "domain:server".
	 * @exception EPPEncodeException
	 *                Error encoding the <code>Vector</code> of
	 *                <code>String</code>'s.
	 */
	public static void encodeVector(Document aDocument, Element aRoot,
			Vector aVector, String aNS, String aTagName)
			throws EPPEncodeException {
		Element currElm;
		Text currVal;

		if (aVector != null) {
			Enumeration elms = aVector.elements();

			while (elms.hasMoreElements()) {
				currElm = aDocument.createElementNS(aNS, aTagName);
				currVal = aDocument.createTextNode(elms.nextElement()
						.toString());

				currElm.appendChild(currVal);
				aRoot.appendChild(currElm);
			}

			// end while (elms.hasMoreElements())
		}

		// end if (aVector != null)
	} // End EPPUtil.encodeVector(Document,Element, Vector, String, String)

	// End EPPUtil.decodeTimeInstance(Element, String, String)

	/**
	 * compares two <code>List</code> instances. The Java 1.1 <code>List</code>
	 * class does not implement the <code>equals</code> methods, so
	 * <code>equalLists</code> was written to implement <code>equals</code> of
	 * two <code>Lists</code> (aV1 and aV2). This method is not need for Java 2.
	 * 
	 * @param aV1
	 *            First List instance to compare.
	 * @param aV2
	 *            Second List instance to compare.
	 * @return <code>true</code> if List's are equal; <code>false</code>
	 *         otherwise.
	 */
	public static boolean equalLists(List aV1, List aV2) {
		if ((aV1 == null) && (aV2 == null)) {
			return true;
		}

		else if ((aV1 != null) && (aV2 != null)) {
			if (aV1.size() != aV2.size()) {
				return false;
			}

			Iterator v1Iter = aV1.iterator();
			Iterator v2Iter = aV2.iterator();

			for (int i = 0; i < aV1.size(); i++) {
				Object elm1 = v1Iter.next();
				Object elm2 = v2Iter.next();

				if (!((elm1 == null) ? (elm2 == null) : elm1.equals(elm2))) {
					return false;
				}
			}

			return true;
		}
		else {
			return false;
		}
	} // End EPPUtil.equalLists(List, List)

	// End EPPUtil.getFirstElementChild(Element)

	/**
	 * compares two <code>Vector</code> instances. The Java 1.1
	 * <code>Vector</code> class does not implement the <code>equals</code>
	 * methods, so <code>equalVectors</code> was written to implement
	 * <code>equals</code> of two <code>Vectors</code> (aV1 and aV2). This
	 * method is not need for Java 2.
	 * 
	 * @param aV1
	 *            First Vector instance to compare.
	 * @param aV2
	 *            Second Vector instance to compare.
	 * @return <code>true</code> if Vector's are equal; <code>false</code>
	 *         otherwise.
	 */
	public static boolean equalVectors(Vector aV1, Vector aV2) {
		if ((aV1 == null) && (aV2 == null)) {
			return true;
		}

		else if ((aV1 != null) && (aV2 != null)) {
			if (aV1.size() != aV2.size()) {
				return false;
			}

			for (int i = 0; i < aV1.size(); i++) {
				Object elm1 = aV1.elementAt(i);
				Object elm2 = aV2.elementAt(i);

				if (!((elm1 == null) ? (elm2 == null) : elm1.equals(elm2))) {
					return false;
				}
			}

			return true;
		}
		else {
			return false;
		}
	} // End EPPUtil.equalVectors(Vector, Vector)

	// End EPPUtil.getNextElementSibling(Element)

	/**
	 * Gets the first direct child element with a given tag name and XML
	 * namespace.
	 * 
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name to scan for.
	 * @return Matching <code>Element</code> if found; <code>null</code>
	 *         otherwise.
	 */
	public static Element getElementByTagNameNS(Element aElement, String aNS,
			String aTagName) {

		Element retElm = null;

		aTagName = EPPUtil.getLocalName(aTagName);

		NodeList theNodes = aElement.getChildNodes();

		// Found matching nodes?
		if (theNodes != null) {

			for (int i = 0; (i < theNodes.getLength()) && retElm == null; i++) {
				if (theNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element currElm = (Element) theNodes.item(i);

					if (currElm.getNamespaceURI().equals(aNS)
							&& currElm.getLocalName().equals(aTagName)) {
						retElm = currElm;
					}
				}
			}
		}

		return retElm;
	}

	/**
	 * Gets all of the direct child element with a given tag name and XML
	 * namespace.
	 * 
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name to scan for.
	 * @return <code>Vector</code> of <code>Node</code> instances that are
	 *         elements and have the specified tag name and XML namespace.
	 */
	public static Vector getElementsByTagNameNS(Element aElement, String aNS,
			String aTagName) {
		Vector retVal = new Vector();

		aTagName = EPPUtil.getLocalName(aTagName);

		NodeList theNodes = aElement.getChildNodes();

		if (theNodes != null) {
			for (int i = 0; i < theNodes.getLength(); i++) {
				if (theNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element currElm = (Element) theNodes.item(i);

					if (currElm.getNamespaceURI().equals(aNS)
							&& currElm.getLocalName().equals(aTagName)) {
						retVal.add(theNodes.item(i));
					}
				}
			}
		}

		return retVal;
	}

	/**
	 * Gets the local name given the qualified element name. If the local name
	 * is passed, it will be simply returned back.R
	 * 
	 * @param aQualifiedName
	 *            Qualified name of the element like <code>domain:name</code>. A
	 *            localhost like <code>name</code> can be passed without error.
	 * @return Localname of the qualified name
	 */
	public static String getLocalName(String aQualifiedName) {
		if (aQualifiedName.indexOf(':') != -1) {
			aQualifiedName = aQualifiedName.substring(aQualifiedName
					.indexOf(':') + 1);
		}

		return aQualifiedName;
	}

	/**
	 * Gets the namespace prefix given a qualified name. If no prefix is found,
	 * empty string is returned.
	 * 
	 * @param aQualifiedName
	 *            Qualified name of the element like <code>domain:name</code>.
	 * @return Namespace prefix of the qualified name if found; empty string
	 *         ("") if not found.
	 */
	public static String getPrefix(String aQualifiedName) {
		String prefix = "";

		if (aQualifiedName.indexOf(':') != -1) {
			prefix = aQualifiedName.substring(0, aQualifiedName.indexOf(':'));
		}

		return prefix;
	}

	/**
	 * Gets the first DOM Element Node of the <code>aElement</code> DOM Element.
	 * 
	 * @param aElement
	 *            Element to scan for the first element.
	 * @return Found DOM Element Node if found; <code>null</code> otherwise.
	 */
	public static Element getFirstElementChild(Element aElement) {
		NodeList children = aElement.getChildNodes();

		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
				return (Element) children.item(i);
			}
		}

		return null;
	}

	/**
	 * Gets the next sibling Element of a provided Element.
	 * 
	 * @param aElement
	 *            Element to start scan for the next Element.
	 * @return Found DOM Element Node if found; <code>null</code> otherwise.
	 */
	public static Element getNextElementSibling(Element aElement) {
		Element theSibling = null;
		Node theCurrNode = aElement;

		while ((theCurrNode != null) && (theSibling == null)) {
			theCurrNode = theCurrNode.getNextSibling();

			if ((theCurrNode != null)
					&& (theCurrNode.getNodeType() == Node.ELEMENT_NODE)) {
				theSibling = (Element) theCurrNode;
			}
		}

		return theSibling;
	}

	/**
	 * A pass-thru to the getTextContent() method using a default value of false
	 * for the required flag.
	 */
	static public String getTextContent(Node node) throws EPPDecodeException {
		return getTextContent(node, false);
	}

	/**
	 * Return the text data within an XML tag.<br>
	 * <id>12345</id> yields the String "12345"<br>
	 * If the node==null, child==null, value==null, or value=="" => Exception<br>
	 * UNLESS allowEmpty, in which case return ""
	 * <p>
	 * For reference:<br>
	 * &lt;tag&gt;&lt;/tag&gt; => child is null<br>
	 * &lt;tag/&gt; => child is null<br>
	 * &lt;tag&gt; &lt;/tag&gt; => value is empty
	 */
	static public String getTextContent(Node node, boolean allowEmpty)
			throws EPPDecodeException {
		if (node != null) {
			Node child = node.getFirstChild();
			if (child != null) {
				String value = child.getNodeValue();
				if (value != null) {
					value = value.trim();
					if (value.length() > 0) {
						return value;
					}
				}
			}
		}
		if (allowEmpty)
			return "";
		throw new EPPDecodeException("Empty tag encountered during decode");
	}

	/**
	 * Determines if one <code>List</code> is a subset of another
	 * <code>List</code>. If every element in <code>aV1</code> is in
	 * <code>aV2</code> return <code>true</code>; otherwise return
	 * <code>false</code>.
	 * 
	 * @param aV1
	 *            Subset <code>List</code> to compare against <code>aV2</code>
	 * @param aV2
	 *            Superset <code>List</code> to compare against <code>aV1</code>
	 * @return <code>true</code> if <code>aV1</code> is a subset of
	 *         <code>aV2</code>; <code>false</code> otherwise.
	 */
	public static boolean listSubset(List aV1, List aV2) {
		Iterator v1Iter = aV1.iterator();

		while (v1Iter.hasNext()) {
			if (!aV2.contains(v1Iter.next())) {
				return false;
			}
		}

		return true;
	} // End EPPUtil.listSubset(List, List)

	/**
	 * Converts an <code>EPPCodecComponent</code> to a <code>String</code> for
	 * printing. Each <code>EPPCodecComponent</code> can use this utility method
	 * to implement <code>Object.toString()</code>.
	 * 
	 * @param aComponent
	 *            a concrete <code>EPPCodecComponent</code> instance
	 * @return <code>String</code> representation of
	 *         <code>EPPCodecComponent</code> if successful; "ERROR"
	 *         <code>String</code> otherwise.
	 */
	public static String toString(EPPCodecComponent aComponent) {
		String ret = "ERROR";

		try {
			Document document = new DocumentImpl();
			Element elm = aComponent.encode(document);

			ret = toString(elm);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return ret;
	}

	/**
	 * Converts an <code>aElement</code> to a <code>String</code> for printing.
	 * 
	 * @param aElement
	 *            <code>Element</code> to print
	 * @return <code>String</code> representation of <code>Element</code> if
	 *         successful; "ERROR" <code>String</code> otherwise.
	 */
	public static String toString(Element aElement) {
		String ret = "ERROR";
		ByteArrayOutputStream theBuffer = new ByteArrayOutputStream();

		// Serialize DOM Document to stream
		try {
			TransformerFactory transFac = TransformerFactory.newInstance();
			Transformer trans = transFac.newTransformer();
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.transform(new DOMSource(aElement),
					new StreamResult(theBuffer));
			theBuffer.close();
			ret = new String(theBuffer.toByteArray());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return ret;
	}
	
	/**
	 * Converts an <code>aElement</code> to a <code>String</code> for printing 
	 * without pretty printing.
	 * 
	 * @param aElement
	 *            <code>Element</code> to print
	 * @return <code>String</code> representation of <code>Element</code> if
	 *         successful; "ERROR" <code>String</code> otherwise.
	 */
	public static String toStringNoIndent(Element aElement) {
		String ret = "ERROR";
		ByteArrayOutputStream theBuffer = new ByteArrayOutputStream();

		// Serialize DOM Document to stream
		try {
			TransformerFactory transFac = TransformerFactory.newInstance();
			Transformer trans = transFac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.transform(new DOMSource(aElement),
					new StreamResult(theBuffer));
			theBuffer.close();
			ret = new String(theBuffer.toByteArray());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return ret;
	}
	

	/**
	 * Determines if one <code>Vector</code> is a subset of another
	 * <code>Vector</code>. If every element in <code>aV1</code> is in
	 * <code>aV2</code> return <code>true</code>; otherwise return
	 * <code>false</code>.
	 * 
	 * @param aV1
	 *            Subset <code>Vector</code> to compare against <code>aV2</code>
	 * @param aV2
	 *            Superset <code>Vector</code> to compare against
	 *            <code>aV1</code>
	 * @return <code>true</code> if <code>aV1</code> is a subset of
	 *         <code>aV2</code>; <code>false</code> otherwise.
	 */
	public static boolean vectorSubset(Vector aV1, Vector aV2) {
		Enumeration v1Enum = aV1.elements();

		while (v1Enum.hasMoreElements()) {
			if (!aV2.contains(v1Enum.nextElement())) {
				return false;
			}
		}

		return true;
	} // End EPPUtil.vectorSubset(Vector, Vector)

	/**
	 * Decode a <code>List</code> of <code>Integer</code>'s by XML namespace and
	 * tag name, from an XML Element. The children elements of
	 * <code>aElement</code> will be searched for the specified <code>aNS</code>
	 * namespace URI and the specified <code>aTagName</code>. Each XML element
	 * found will be decoded and added to the returned <code>List</code>. Empty
	 * child elements, will result in an <code>EPPDecodeException</code>.
	 * 
	 * @param aElement
	 *            XML Element to scan. For example, the element could be
	 *            &ltdomain:update&gt
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name servers
	 *            is "domain:server".
	 * 
	 * @return <code>List</code> of <code>Integer</code> elements representing
	 *         the text nodes of the found XML elements.
	 * 
	 * @exception EPPDecodeException
	 *                Error decoding <code>aElement</code>.
	 */
	public static List decodeIntegerList(Element aElement, String aNS,
			String aTagName) throws EPPDecodeException {

		List retVal = new ArrayList();

		Vector theChildren = EPPUtil.getElementsByTagNameNS(aElement, aNS,
				aTagName);

		for (int i = 0; i < theChildren.size(); i++) {
			Element currChild = (Element) theChildren.elementAt(i);

			Integer retInteger = null;

			Node textNode = currChild.getFirstChild();

			// Element does have a text node?
			if (textNode != null) {

				String intValStr = textNode.getNodeValue();
				try {
					retInteger = Integer.valueOf(intValStr);
				}
				catch (NumberFormatException e) {

					throw new EPPDecodeException(
							"EPPUtil.decodeIntegerList Can't convert value to Integer: "
									+ intValStr + e);
				}
			}
			else {
				throw new EPPDecodeException(
						"EPPUtil.decodeIntegerList Can't decode numeric value from non-existant text node");
			}

			retVal.add(retInteger);
		}

		return retVal;
	} // End EPPUtil.decodeIntegerList(Element, String, String)

	/**
	 * Encode a <code>List</code> of <code>Integer</code>'s in XML with a given
	 * XML namespace and tag name.
	 * 
	 * @param aDocument
	 *            DOM Document of <code>aRoot</code>. This parameter also acts
	 *            as a factory for XML elements.
	 * @param aRoot
	 *            XML Element to add children nodes to. For example, the root
	 *            node could be &ltdomain:update&gt
	 * @param aList
	 *            <code>List</code> of <code>Integer</code>'s to add.
	 * @param aNS
	 *            XML namespace of the elements. For example, for domain element
	 *            this is "urn:iana:xmlns:domain".
	 * @param aTagName
	 *            Tag name of the element including an optional namespace
	 *            prefix. For example, the tag name for the domain name servers
	 *            is "domain:server".
	 * 
	 * @exception EPPEncodeException
	 *                Error encoding the <code>List</code> of
	 *                <code>Integer</code>'s.
	 */
	public static void encodeIntegerList(Document aDocument, Element aRoot,
			List aList, String aNS, String aTagName) throws EPPEncodeException {
		Element currElm;
		Text currVal;

		if (aList != null) {
			Iterator elms = aList.iterator();

			while (elms.hasNext()) {
				currElm = aDocument.createElementNS(aNS, aTagName);
				currVal = aDocument.createTextNode(elms.next().toString());

				currElm.appendChild(currVal);
				aRoot.appendChild(currElm);
			} // end while (elms.hasNext())

		} // end if (aList != null)

	} // End EPPUtil.encodeIntegerList(Document,Element, List, String, String)

	/**
	 * Collapse Whitespace, which means that all leading and trailing whitespace
	 * will be stripped, and all internal whitespace collapsed to single space
	 * characters. Intended to emulate XML Schema whitespace (collapse)
	 * constraining facet: http://www.w3.org/TR/xmlschema-2/#rf-whiteSpace
	 * 
	 * @param inString
	 *            <code>String</code> to be collapsed
	 * @return <code>String</code> with whitespace collapsed, or null when
	 *         result is empty
	 */
	public static String collapseWhitespace(String inString) {

		if (inString == null) {
			return null;
		}

		String s = inString.trim(); // remove leading and trailing whitespace
		StringBuffer sb = new StringBuffer();

		int len = s.length();
		boolean prevWhitespace = true;
		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if (Character.isWhitespace(c) == false) {
				sb.append(c); // maintain all non-whitespace characters
				prevWhitespace = false;
			}
			else {
				if (prevWhitespace == false) {
					sb.append(' '); // replace each sequence of whitespace with
					// a single space
					prevWhitespace = true;
				}
			}
		}

		if (sb.length() == 0) {
			return null; // return null string when result is empty
		}
		else {
			return sb.toString(); // return collapsed string
		}

	}

	/**
	 * Remove Whitespace, which means that all whitespace will be stripped.
	 * 
	 * @param inString
	 *            <code>String</code> from which to remove whitespace
	 * @return <code>String</code> with whitespace removed, or null when result
	 *         is empty
	 */
	public static String removeWhitespace(String inString) {

		if (inString == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer();

		int len = inString.length();
		for (int i = 0; i < len; i++) {
			char c = inString.charAt(i);
			if (Character.isWhitespace(c) == false) {
				sb.append(c); // maintain all non-whitespace characters
			}
		}

		if (sb.length() == 0) {
			return null; // return null string when result is empty
		}
		else {
			return sb.toString(); // return result string
		}

	}

	/**
	 * Base64 encode the XML <code>Element</code> tree.
	 * 
	 * @param aElement Root element to encode in Base64
	 * @return Base64 encoded value of the XML <code>Element</code> tree.
	 * @throws Exception Error encoding the <code>Element</code>
	 */
	public static String encodeBase64(Element aElement) throws Exception {
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		
		TransformerFactory transFac = TransformerFactory.newInstance();
		Transformer trans = transFac.newTransformer();
		trans.transform(new DOMSource(aElement), new StreamResult(ostream));
		
		return (new String(Base64.encodeBase64(ostream.toByteArray(), true)));
	}

}

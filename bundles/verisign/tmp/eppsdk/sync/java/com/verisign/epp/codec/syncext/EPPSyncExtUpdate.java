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

package com.verisign.epp.codec.syncext;

import java.util.*;
import java.text.*;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

// Log4j Imports
import org.apache.log4j.Logger;

// EPP imports
import com.verisign.epp.util.*;
import com.verisign.epp.codec.gen.*;

/**
 *
 * EPPCodecComponent that encodes and decodes a sync update.
 *
 * <p>Title: EPP 1.0 RGP </p>
 * <p>Description: SYNC Extension to the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: VeriSign</p>
 * @author clloyd
 * @version 1.0
 */

public class EPPSyncExtUpdate
    implements EPPCodecComponent {

    /**
     * Log4j category for logging
     */
    private static Logger cat = Logger.getLogger(EPPSyncExtUpdate.class.getName(),
                                EPPCatFactory.getInstance().getFactory());

    /**
     * Constant for the update tag name
     */
    public static final String ELM_NAME = "sync:update";

    /**
     * Constant for the expiration month/day tag name
     */
    public static final String ELM_MONTH_DAY = "sync:expMonthDay";

    /**
     * The month.
     */
    private int month = -1;

    /**
     * The day of the month
     */
    private int day = -1;

    /**
     * Create an EPPSyncExtUpdate instance
     */
    public EPPSyncExtUpdate() {}

    /**
     * Create a EPPSyncExtUpdate intance with the given month and day
     *
     * @param aMonth the month using a 
     * <code>java.util.Calendar</code> month constant
     * 
     * @param aDay the day
     */
    public EPPSyncExtUpdate(int aMonth, int aDay) {
        month = aMonth;
        day = aDay;
    }


    /**
     * Clone <code>EPPSyncExtUpdate</code>.
     *
     * @return clone of <code>EPPSyncExtUpdate</code>
     * @exception CloneNotSupportedException standard Object.clone exception
     */
    public Object clone() throws CloneNotSupportedException {

        EPPSyncExtUpdate clone = null;

        clone = (EPPSyncExtUpdate)super.clone();

        return clone;
    }

    /**
     * Sets all this instance's data in the given XML document.  
     * Only a basic precondition check is done on the range 
     * of month (Calendar.JANUARY - Calendar.DECEMBER) and day values 
     * (1 - 31).   
     *
     * @param aDocument a DOM Document to attach data to.
     * @return The root element of this component.
     *
     * @throws EPPEncodeException Thrown if any errors prevent encoding.
     */
    public Element encode(Document aDocument) throws EPPEncodeException {

        try {
            //Validate States
            validateState();
        }
        catch (EPPCodecException e) {
            cat.error("EPPSyncExtUpdate.encode(): Invalid state on encode: "
                      + e);
            throw new EPPEncodeException("EPPSyncExtUpdate invalid state: "
                                         + e);
        }

        if (aDocument == null) {
            throw new EPPEncodeException("aDocument is null" +
                " on in EPPSyncExtUpdate.encode(Document)");
        }

        Element root =
            aDocument.createElementNS(EPPSyncExtFactory.NS, ELM_NAME);

        root.setAttribute("xmlns:sync", EPPSyncExtFactory.NS);
        root.setAttributeNS(
            EPPCodec.NS_XSI, "xsi:schemaLocation",
            EPPSyncExtFactory.NS_SCHEMA);

        int theMonth = this.month + 1; // Calendar is zero based 
        String theMonthStr = null;
        if (theMonth < 10) {
        	theMonthStr = "0" + theMonth;
        } 
        else {
        	theMonthStr = "" + theMonth;
        }
        
        String theDayStr = null;
        if (this.day < 10) {
        	theDayStr = "0" + this.day;
        } 
        else {
        	theDayStr = "" + this.day;
        }
            
        String dateString = "--" + theMonthStr + "-" + theDayStr;

        // encode the string
        EPPUtil.encodeString(aDocument, root, dateString, EPPSyncExtFactory.NS, ELM_MONTH_DAY);

        return root;
    }

    /**
     * Decode the EPPSyncExtUpdate component.  It's assumed that 
     * date has already been validated by the XML parsing, so 
     * the month and day are decoded with no additional validation.
     *
     * @param aElement
     * @throws EPPDecodeException
     */
    public void decode(Element aElement) throws EPPDecodeException {

        String dateString =
            EPPUtil.decodeString(aElement, EPPSyncExtFactory.NS, ELM_MONTH_DAY);

        if (dateString.length() != 7) {
            throw new EPPDecodeException("Couldn't parse date string: " + dateString + 
            		" it should be 7 characters with the format --MM-dd");
        }
        
        String theMonth = dateString.substring(2, 4);
        String theDay = dateString.substring(5);
        
        try {
			this.month = Integer.parseInt(theMonth) - 1; // Calendar uses zero
														 // based constants
			this.day = Integer.parseInt(theDay);
		} 
        catch (NumberFormatException ex) {
			throw new EPPDecodeException("Error converting month " + theMonth
					+ " or day " + theDay + " to integer: " + ex);
		}
     }

    /**
	 * Validate the state of the <code>EPPSyncExtUpdate</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the <code>EPPCodecException</code> will contain a
	 * description of the error. throws EPPCodecException State error. This will
	 * contain the name of the attribute that is not valid. Both the month and
	 * day must be set to valid values.
	 * 
	 * @throws EPPCodecException
	 *             Thrown if the instance is in an invalid state
	 */
    void validateState() throws EPPCodecException {
        if (this.month < Calendar.JANUARY || this.month > Calendar.DECEMBER)
        	throw new EPPCodecException("EPPSyncExtUpdate month value of " + 
        			this.month + " is out of range [Calendar.JANUARY(" + 
					Calendar.JANUARY + ")<=month<=Calendar.DECEMBER(" + 
					Calendar.DECEMBER + ")]");
        
        if (this.day < 1 || this.day > 31) 
           	throw new EPPCodecException("EPPSyncExtUpdate day value of " + 
        			this.day + " is out of range [1<=day<=31]");
    }

    /**
     * implements a deep <code>EPPSyncExtUpdate</code> compare.
     *
     * @param aObject <code>EPPSyncExtUpdate</code> instance to compare with
     *
     * @return true if equal false otherwise
     */
    public boolean equals(Object aObject) {

        if (! (aObject instanceof EPPSyncExtUpdate)) {
            return false;
        }
        EPPSyncExtUpdate theComp = (EPPSyncExtUpdate) aObject;

        return (month == theComp.month && day == theComp.day);
    }


    /**
     * Returns the month.
     *
     * @return the month using a  
     * <code>java.util.Calendar</code> month constant
     */
    public int getMonth() { 
        return month;
    }

    /**
     * Sets the  month
     *
     * @param aMonth the month using a 
     * <code>java.util.Calendar</code> month constant
     */
    public void setMonth(int aMonth) {
        this.month = aMonth;
    }

    /**
     * Gets the day of the month
     * @return the day of the month
     */
    public int getDay() {
        return day;
    }

    /**
     * Sets the day of the month
     *
     * @param aDay the day of the month
     */
    public void setDay(int aDay) {
        this.day = aDay;
    }
}
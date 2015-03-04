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

package com.verisign.epp.codec.rgpext;

// DOM imports
import org.w3c.dom.Element;
import org.w3c.dom.Document;

// Log4j Imports
import org.apache.log4j.Logger;

// EPP imports
import com.verisign.epp.util.*;
import com.verisign.epp.codec.gen.*;



/**
 *
 * The EPPRgpExtUpdate is the EPPCodecComponent that knows how to encode and
 * decode RGP update elements from/to XML and object instance.
 *
 * <p>Title: EPP 1.0 RGP </p>
 * <p>Description: RGP Extension to the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: VeriSign</p>
 * @author clloyd
 * @version 1.0
 */
public class EPPRgpExtUpdate implements EPPCodecComponent {

    /**
     * The update element name in XML
     */
    public static final String ELM_NAME = "rgp:update";

    /** Log4j category for logging */
    private static Logger cat =
        Logger.getLogger(
                         EPPRgpExtUpdate.class.getName(),
                         EPPCatFactory.getInstance().getFactory());


 /**
  * The contained restore EPPCodecComponent
  */
    private EPPRgpExtRestore restore;

    /**
     * Create a new EPPRgpExtUpdate instance
     */
    public EPPRgpExtUpdate() {}

    /**
     * Create a new EPPRgpExtUpdate instance with contained 
     * request or report.
     * 
     * @param aRestore the new contained restore request or report
     */
    public EPPRgpExtUpdate(EPPRgpExtRestore aRestore) {
    		this.restore = aRestore;
    	}

    /**
     * Append all data from this RGP inf data to the given DOM Document
     *
     * @param aDocument The DOM Document to append data to
     * @return Encoded DOM <code>Element</code>
     * @throws EPPEncodeException Thrown when errors occur during the
     * encode attempt or if the instance is invalid.
     */
    public Element encode(Document aDocument) throws EPPEncodeException {
        try {
            //Validate States
            validateState();
        }
         catch (EPPCodecException e) {
            cat.error("EPPRgpExtUpdate.encode(): Invalid state on encode: "
                      + e);
            throw new EPPEncodeException("EPPRgpExtUpdate invalid state: "
                                         + e);
        }

        if (aDocument == null) {
            throw new EPPEncodeException("aDocument is null" +
            " on in EPPRgpExtUpdate.encode(Document)");
        }

        Element root =
            aDocument.createElementNS(EPPRgpExtFactory.NS, ELM_NAME);

        root.setAttribute("xmlns:rgp", EPPRgpExtFactory.NS);
        root.setAttributeNS(
                            EPPCodec.NS_XSI, "xsi:schemaLocation",
                            EPPRgpExtFactory.NS_SCHEMA);

        EPPUtil.encodeComp(aDocument,root,restore);

        return root;
    }

    /**
     * Returns the RGP namespace
     *
     * @return the RGP namespace
     */
    public String getNamespace() {
        return EPPRgpExtFactory.NS;
    }

    /**
     * implements a deep <code>EPPRgpExtUpdate</code> compare.
     *
     * @param aObject <code>EPPRgpExtUpdate</code> instance to compare with
     *
     * @return true if equal false otherwise
     */
    public boolean equals(Object aObject) {

        if (! (aObject instanceof EPPRgpExtUpdate)) {
            return false;
        }
        EPPRgpExtUpdate theComp = (EPPRgpExtUpdate) aObject;

        // restore
        if (! ( (restore == null) ? (theComp.restore == null) :
               restore.equals(theComp.restore)
               )) {
            return false;
        }

        return true;
    }


    /**
     * Validate the state of the <code>EPPRgpExtUpdate</code> instance. A
     * valid state means that all of the required attributes have been set. If
     * validateState returns without an exception, the state is valid. If the
     * state is not valid, the <code>EPPCodecException</code> will contain a
     * description of the error.  throws EPPCodecException State error. This
     * will contain the name of the attribute that is not valid.
     *
     * @throws EPPCodecException Thrown if the instance is in an invalid state
     */
    void validateState() throws EPPCodecException {
        // report
        if (restore == null) {
            throw new EPPCodecException("EPPRgpExtStatus required attribute is not set [restore]");
        }
    }

    /**
     * Populate the data of this instance with the data stored in the
     * given Element of the DOM tree
     *
     * @param aElement The root element of the report fragment of XML
     * @throws EPPDecodeException Thrown if any errors occur during decoding.
     */
    public void decode(Element aElement) throws EPPDecodeException {

        //restore
        restore = (EPPRgpExtRestore)EPPUtil.decodeComp(aElement, EPPRgpExtFactory.NS,
                                    EPPRgpExtRestore.ELM_NAME, EPPRgpExtRestore.class);
    }

    /**
     * Clone <code>EPPRgpExtUpdate</code>.
     *
     * @return clone of <code>EPPRgpExtUpdate</code>
     *
     * @exception CloneNotSupportedException standard Object.clone exception
     */
    public Object clone() throws CloneNotSupportedException {

        EPPRgpExtUpdate clone = null;

        clone = (EPPRgpExtUpdate) super.clone();

        clone.setRestore((EPPRgpExtRestore)restore.clone());

        return clone;
    }

    /**
     * Gets the contained restore request or report.
     * 
     * @return the contained restore request or report
     */
    public EPPRgpExtRestore getRestore() {
        return restore;
    }

    /**
     * Sets the contained restore request or report.
     * 
     * @param aRestore the new contained restore request or report
     */
    public void setRestore(EPPRgpExtRestore aRestore) {
        this.restore = aRestore;
    }
}
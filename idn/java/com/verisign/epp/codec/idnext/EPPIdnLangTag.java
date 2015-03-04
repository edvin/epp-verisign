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

package com.verisign.epp.codec.idnext;

import java.util.*;
import java.text.*;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

// Log4j Imports
import org.apache.log4j.Logger;

// EPP imports
import com.verisign.epp.util.*;
import com.verisign.epp.codec.gen.*;

/**
 *
 * EPPCodecComponent that encodes and decodes a IDN Lang Tag.
 *
 * <p>Title: EPP 1.0 RGP </p>
 * <p>Description: IDN Extension to the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: VeriSign</p>
 * @author clloyd
 * @version 1.0
 */

public class EPPIdnLangTag
    implements EPPCodecComponent {

    /**
     * Log4j category for logging
     */
    private static Logger cat = Logger.getLogger(EPPIdnLangTag.class.getName(),
                                EPPCatFactory.getInstance().getFactory());

    /**
     * Constant for the idn lang tag 
     */
    public static final String ELM_NAME = "idnLang:tag";
    
   
    /**
     * The Language code.
     */
    private String lang;

  
    /**
     * Create an EPPIdnLangTag instance
     */
    public EPPIdnLangTag() {}

    /**
     * Create a EPPIdnLangTag intance with the given Language
     *
     * @param aLang the language
     */
    public EPPIdnLangTag(String aLang) {
        lang = aLang;
    }


    /**
     * Clone <code>EPPIdnLangTag</code>.
     *
     * @return clone of <code>EPPIdnLangTag</code>
     * @exception CloneNotSupportedException standard Object.clone exception
     */
    public Object clone() throws CloneNotSupportedException {

        EPPIdnLangTag clone = null;

        clone = (EPPIdnLangTag)super.clone();

        return clone;
    }

    /**
     * Sets all this instance's data in the given XML document
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
            cat.error("EPPIdnLangTag.encode(): Invalid state on encode: "
                      + e);
            throw new EPPEncodeException("EPPIdnLangTag invalid state: "
                                         + e);
        }

        if (aDocument == null) {
            throw new EPPEncodeException("aDocument is null" +
                " on in EPPIdnLangTag.encode(Document)");
        }

        Element root =
            aDocument.createElementNS(EPPIdnExtFactory.NS, ELM_NAME);

        root.setAttribute("xmlns:idnLang", EPPIdnExtFactory.NS);
        root.setAttributeNS(
            EPPCodec.NS_XSI, "xsi:schemaLocation",
            EPPIdnExtFactory.NS_SCHEMA);

        
        // encode the string
         
        Text langText = aDocument.createTextNode(lang);
        
        root.appendChild(langText);
        
        return root;
    }

    /**
     * Decode the EPPIdnLangExtCrete component
     *
     * @param aElement
     * @throws EPPDecodeException
     */
    public void decode(Element aElement) throws EPPDecodeException {

        
    	Text langText = (Text) aElement.getFirstChild() ;
    	
    	lang = langText.getData();
    	
    }

    /**
     * Validate the state of the <code>EPPIdnLangTag</code> instance. A
     * valid state means that all of the required attributes have been set. If
     * validateState returns without an exception, the state is valid. If the
     * state is not valid, the <code>EPPCodecException</code> will contain a
     * description of the error.  throws EPPCodecException State error. This
     * will contain the name of the attribute that is not valid.
     *
     * @throws EPPCodecException Thrown if the instance is in an invalid state
     */
    void validateState() throws EPPCodecException {
        // don't need to validate anything.
    }

    /**
     * implements a deep <code>EPPIdnLangTag</code> compare.
     *
     * @param aObject <code>EPPIdnLangTag</code> instance to compare with
     *
     * @return true if equal false otherwise
     */
    public boolean equals(Object aObject) {

        if (! (aObject instanceof EPPIdnLangTag)) {
            return false;
        }
        EPPIdnLangTag theComp = (EPPIdnLangTag) aObject;

        return (lang.equalsIgnoreCase(theComp.getLang()));
    }


    /**
     * Returns the Language Code.
     *
     * @return the language code
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the Language Code
     *
     * @param aLang the language code
     */
    public void setLang(String aLang) {
        this.lang = aLang;
    }


}
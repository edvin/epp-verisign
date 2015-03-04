/* ===========================================================================
 * Copyright (C) 2001 VeriSign, Inc.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
 * 
 * VeriSign Global Registry Services
 * 505 Huntmar Park Dr.
 * Herndon, VA 20170
 * ===========================================================================
 * The EPP, APIs and Software are provided "as-is" and without any warranty
 * of any kind. VeriSign Corporation EXPRESSLY DISCLAIMS ALL WARRANTIES
 * AND/OR CONDITIONS, EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY OR SATISFACTORY
 * QUALITY AND FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD
 * PARTY RIGHTS. VeriSign Corporation DOES NOT WARRANT THAT THE FUNCTIONS
 * CONTAINED IN THE EPP, APIs OR SOFTWARE WILL MEET REGISTRAR'S REQUIREMENTS,
 * OR THAT THE OPERATION OF THE EPP, APIs OR SOFTWARE WILL BE UNINTERRUPTED
 * OR ERROR-FREE,OR THAT DEFECTS IN THE EPP, APIs OR SOFTWARE WILL BE CORRECTED.
 * FURTHERMORE, VeriSign Corporation DOES NOT WARRANT NOR MAKE ANY
 * REPRESENTATIONS REGARDING THE USE OR THE RESULTS OF THE EPP, APIs, SOFTWARE
 * OR RELATED DOCUMENTATION IN TERMS OF THEIR CORRECTNESS, ACCURACY,
 * RELIABILITY, OR OTHERWISE.  SHOULD THE EPP, APIs OR SOFTWARE PROVE DEFECTIVE,
 * REGISTRAR ASSUMES THE ENTIRE COST OF ALL NECESSARY SERVICING, REPAIR OR
 * CORRECTION.
 * ===========================================================================
 *
 * $Id: EPPCredentials.java,v 1.5 2002/02/13 17:51:17 jgould Exp $
 *
 * ======================================================================== */

//----------------------------------------------
//
// package
//
//----------------------------------------------
package com.verisign.epp.codec.gen;



//----------------------------------------------
//
// imports...
//
//----------------------------------------------

// Java Core Imports

// W3C Imports
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Represents the optional &ltcreds&gt element in an EPP Command. A "cred" element
 * SHALL contain the following child elements:  
 * <li>A "clID" element that contains the client identifier assigned
 * to the client by the server.  The value of this element is case
 * insensitive.  Use <code>getClientId</code> and <code>setClientId</code> 
 * to get and set the element.
 * <li>A "pw" element that contains the client's plain text password.
 * Use <code>getPassword</code> and <code>setPassword</code> to get and set
 * the element.
 * <li>An OPTIONAL "newPW" element that contains a new plain text
 * password to be assigned to the client for use with subsequent "login"
 * commands.  The value of this element is case sensitive.  Use <code>getNewPassword</code>
 * and <code>setNewPassword</code> to get and set the element.
 * <li>An "options" element that contains the "version" and "lang" elements.
 * Use <code>getVersion</code> and <code>setVersion</code> to get and set the version.
 * Use <code>getLang</code> and <code>setLang</code> to get and set the language. 
 * </ul>
 * <div align="center">
 * Copyright (C) 2001 VeriSign, Inc.
 * <br>
 * <b>Veri</b><i><font color="#ef0000"><b>S</b></font></i><b>ign Global Registry Services</b><br>
 * 505 Huntmar Park Dr.
 * <br>
 * Herndon, VA 20170
 * </div>
 * @author	$Author: jgould $		
 * @version	$Revision: 1.5 $		
 * @see	com.verisign.epp.codec.gen.EPPCommand
 */
public class EPPCredentials
            implements EPPCodecComponent
{

    /**
     * Minimum length of a password.
     */
    public static final int MIN_PASSWORD_LEN = 6;

    /**
     * Maximum length of a password.
     */
    public static final int MAX_PASSWORD_LEN = 16;


    /**
     * Allocates a new <code>EPPCredentials</code> with the following default 
     * attribute values:<br><ul>
     * <li>client id - Default of <code>null</code>.  Set with <code>setClientId</code>
     * <li>password - Default of <code>null</code>.  Set with <code>setPassword</code>
     * <li>new password - Default of <code>null</code>. Set with <code>setNewPassword</code>
     * <li>version - Default of <code>EPPCodec.VERSION</code>.  Set with <code>setVersion</code>
     * <li>language - Default of "en".  Set with <code>setLang</code>
     * </ul><br><br>
     */
    public 	EPPCredentials()
    {
        // Attributes initialized in definition.
    } // End EPPCredentials.EPPCredentials()



    /**
     * Allocates a new <code>EPPCredentials</code> with the required attributes.  The 
     * other attributes are initialized as follows:
     * <br><br><ul>
     * <li>new password - Default of <code>null</code>. Set with <code>setNewPassword</code>
     * <li>version - Default of <code>EPPCodec.VERSION</code>.  Set with <code>setVersion</code>
     * <li>language - Default of "en".  Set with <code>setLang</code>
     * </ul>
     *	
     * @param aClientId Client login id
     * @param aPassword Client password
     */
    public EPPCredentials(String aClientId, String aPassword)
    {
        clientId 		= aClientId;
        password 		= aPassword;
    } // End EPPCredentials.EPPCredentials(String, String)

    /**
     * Allocates a new <code>EPPCredentials</code> with client id, password, and new password.  
     * The other attributes are initialized as follows:
     * <br><br><ul>
     * <li>version - Default of <code>EPPCodec.VERSION</code>.  Set with <code>setVersion</code>
     * <li>language - Default of "en".  Set with <code>setLang</code>
     * </ul>
     *	
     * @param aClientId Client login id
     * @param aPassword Client password
     * @param aNewPassword Client new password
     */
    public EPPCredentials(String aClientId, String aPassword, String aNewPassword)
    {
        clientId 		= aClientId;
        password 		= aPassword;
        newPassword		= aNewPassword;
    } // End EPPCredentials.EPPCredentials(String, String, String)


    /**
     * Allocates a new <code>EPPCredentials</code> with all attributes.  
     *	
     * @param aClientId Client login id
     * @param aPassword Client password
     * @param aNewPassword Client new password
     * @param aVersion EPP protocol version desired by Client
     * @param aLang Desired language for result messages
     */
    public EPPCredentials(String aClientId, String aPassword, String aNewPassword,
                          String aVersion, String aLang)
    {
        clientId 		= aClientId;
        password 		= aPassword;
        newPassword		= aNewPassword;
        version			= aVersion;
        lang			= aLang;
    } // End EPPCredentials.EPPCredentials(String, String, String, String, String)




    /**
     *	Gets the client login identifier.
     *
     *	@return	Client login identifier if defined; <code>null</code> otherwise.
     */
    public String	getClientId()
    {
        return clientId;
    } // End EPPCredentials.getClientId()


    /**
     *	Sets the client login identifier.
     *
     *	@param aClientId Client login identifier.
     */
    public void	setClientId(String aClientId)
    {
        clientId = aClientId;
    } // End EPPCredentials.setClientId(String)


    /**
     *	Gets the client password.
     *
     *	@return	Client password if defined; <code>null</code> otherwise.
     */
    public String	getPassword()
    {
        return password;
    } // End EPPCredentials.getPassword()


    /**
     *	Sets the client password.
     *
     *	@param aPassword Client password.
     */
    public void	setPassword(String aPassword)
    {
        password = aPassword;
    } // End EPPCredentials.setPassword(String)


    /**
     *	Gets the new client password.
     *
     *	@return	New client password if defined; <code>null</code> otherwise.
     */
    public String	getNewPassword()
    {
        return newPassword;
    } // End EPPCredentials.getNewPassword()


    /**
     *	Sets the new client password.
     *
     *	@param aNewPassword New client password.
     */
    public void	setNewPassword(String aNewPassword)
    {
        newPassword = aNewPassword;
    } // End EPPCredentials.setNewPassword(String)


    /**
     *	Is a new password defined?
     *
     *	@return	<code>true</code> if the new password is defined; <code>false</code> otherwise.
     */
    public boolean	hasNewPassword()
    {
        if (newPassword != null)
            return true;
        else
            return false;
    } // End EPPCredentials.hasNewPassword()

    /**
     * Gets the desired EPP version. The default version is set to 
     * <code>EPPCodec.VERSION</code>.
     *
     *	@return	EPP version identifier if defined; <code>null</code> otherwise.	
     */
    public String	getVersion()
    {
        return version;
    } // End EPPCredentials.getVersion()


    /**
     * Sets the desired EPP version.  The default version is set to 
     * <code>EPPCodec.VERSION</code>.
     *
     *	@param aVersion	EPP version identifier 
     */
    public void	setVersion(String aVersion)
    {
        version = aVersion;
    } // End EPPCredentials.setVersion(String)


    /**
     *	Gets the desired EPP language.  The EPP language determines the language
     *	of the error description strings and should be one of the supported 
     *	languages of the <code>EPPGreeting</code>.  The default language is 
     *	"en".
     *
     *	@return	The desired EPP language if defined; <code>null</code> otherwise.
     */
    public String	getLang()
    {
        return lang;
    } // End EPPCredentials.getLang()


    /**
     *	Sets the desired EPP language.  The EPP language determines the language
     *	of the error description strings and should be one of the supported 
     *	languages of the <code>EPPGreeting</code>.  The default language is 
     *	"en".
     *
     *	@param	aLang	The desired EPP language
     */
    public void	setLang(String aLang)
    {
        lang = aLang;
    } // End EPPCredentials.setLang(String)


    /**
     *	encode <code>EPPCredentials</code> into a DOM element tree.  
     *	
     *	@return	Encoded DOM element.
     *
     *	@exception	EPPEncodeException	Error encoding the DOM element tree.
     */
    public Element	encode(Document aDocument) throws EPPEncodeException
    {
        // Validate pre-conditions
        if (clientId == null)
            throw new EPPEncodeException("EPPCredentials required attribute \"client id\" is null.");

        if (password == null)
            throw new EPPEncodeException("EPPCredentials required attribute \"password\" is null.");

        if ((password.length() < MIN_PASSWORD_LEN) ||
                (password.length() > MAX_PASSWORD_LEN))
            throw new EPPEncodeException("EPPCredentials password length of " + password.length() +
                                         "is out of range, must be between " + MIN_PASSWORD_LEN +
                                         " and " + MAX_PASSWORD_LEN);
        if ((newPassword != null) &&
                ((newPassword.length() < MIN_PASSWORD_LEN) ||
                 (newPassword.length() > MAX_PASSWORD_LEN)))
            throw new EPPEncodeException("EPPCredentials new password length of " + newPassword.length() +
                                         "is out of range, must be between " + MIN_PASSWORD_LEN +
                                         " and " + MAX_PASSWORD_LEN);

        if (version == null)
            throw new EPPEncodeException("EPPCredentials required attribute \"version\" is null.");

        if (lang == null)
            throw new EPPEncodeException("EPPCredentials required attribute \"lang\" is null.");



        Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

        // Client Id
        EPPUtil.encodeString(aDocument, root, clientId, EPPCodec.NS, ELM_CLIENT_ID);

        // Password
        EPPUtil.encodeString(aDocument, root, password, EPPCodec.NS, ELM_PASSWORD);

        // New Password
        EPPUtil.encodeString(aDocument, root, newPassword, EPPCodec.NS, ELM_NEW_PASSWORD);

        // Options
        Element optionsElm = aDocument.createElementNS(EPPCodec.NS, ELM_OPTIONS);
        root.appendChild(optionsElm);

        // Version
        EPPUtil.encodeString(aDocument, optionsElm, version, EPPCodec.NS, ELM_VERSION);

        // Language
        EPPUtil.encodeString(aDocument, optionsElm, lang, EPPCodec.NS, ELM_LANG);
        return root;

    } // End EPPCredentials.encode(Document)



    /**
     *	decode <code>EPPCredentials</code> from a DOM element tree.  The 
     *	<code>aElement</code> argument needs to be the "trans-id" element, or
     *	an element that conforms to the XML structure of "trans-id".
     *
     *	@param aElement	The "trans-id" XML element.
     *
     *	@exception	EPPDecodeException	Error decoding the DOM element tree.
     */
    public void	decode(Element aElement) throws EPPDecodeException
    {

        // Client Id
        clientId 	= EPPUtil.decodeString(aElement, EPPCodec.NS, ELM_CLIENT_ID);

        // Password
        password 	= EPPUtil.decodeString(aElement, EPPCodec.NS, ELM_PASSWORD);

        // New Password
        newPassword = EPPUtil.decodeString(aElement, EPPCodec.NS, ELM_NEW_PASSWORD);

        // Options
		Element currElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPCodec.NS, ELM_OPTIONS);

        if (currElm != null)
        {
            // Version
            version = EPPUtil.decodeString(currElm, EPPCodec.NS, ELM_VERSION);

            // Language
            lang = EPPUtil.decodeString(currElm, EPPCodec.NS, ELM_LANG);
        }
        else
        {
            version = null;
            lang = null;
        }

    } // End EPPCredentials.decode(Element)


    /**
     *	implements a deep <code>EPPCredentials</code> compare.
     *
     *	@param	aObject	<code>EPPCredentials</code> instance to compare with
     */
    public boolean equals(Object aObject)
    {

        if (!(aObject instanceof EPPCredentials))
            return false;

        EPPCredentials theCred = (EPPCredentials) aObject;

        // clientId
        if (!(clientId == null ? theCred.clientId == null : clientId.equals(theCred.clientId)))
            return false;

        // password
        if (!(password == null ? theCred.password == null : password.equals(theCred.password)))
            return false;

        // newPassword
        if (!(newPassword == null ? theCred.newPassword == null : newPassword.equals(theCred.newPassword)))
            return false;

        // version
        if (!(version == null ? theCred.version == null : version.equals(theCred.version)))
            return false;

        // lang
        if (!(lang == null ? theCred.lang == null : lang.equals(theCred.lang)))
            return false;


        return true;

    } // End EPPCredentials.equals(Object)

    /**
     *	Clone <code>EPPCredentials</code>.
     *
     *	@return	Deep copy clone of <code>EPPCredentials</code>
     *	@exception	CloneNotSupportedException	standard Object.clone exception
     */
    public Object clone() throws CloneNotSupportedException
    {
        EPPCredentials clone = null;

        clone = (EPPCredentials) super.clone();

        return clone;

    } // End EPPCredentials.clone()

    /**
     *	Implementation of <code>Object.toString</code>, which will result in
     *	an indented XML <code>String</code> representation of the concrete 
     *	<code>EPPCodecComponent</code>.
     *
     *	@return	Indented XML <code>String</code> if successful; <code>ERROR</code> otherwise.
     */
    public String toString()
    {
        return EPPUtil.toString(this);
    } // End EPPCredentials.toString()


    /**
     * Client login id
     */
    private String	clientId = null;

    /**
     * Client password
     */
    private String	password = null;

    /**
     * New client password
     */
    private String	newPassword = null;

    /**
     * Desired EPP protocol version
     */
    private String	version = EPPCodec.VERSION;

    /**
     *	Desired language for error result messages.
     */
    private String	lang = "en";

    /**
     *	XML Element Name of <code>EPPDeleteCmd</code> root element.
     */
    final static String ELM_NAME					= "creds";

    /**
     *	XML tag name for the <code>clientId</code> attribute.
     */
    private final static String ELM_CLIENT_ID		= "clID";

    /**
     *	XML tag name for the <code>password</code> attribute.
     */
    private final static String ELM_PASSWORD		= "pw";

    /**
     *	XML tag name for the <code>newPassword</code> attribute.
     */
    private final static String ELM_NEW_PASSWORD	= "newPW";

    /**
     *	XML tag name for the credential options.
     */
    private final static String ELM_OPTIONS	= "options";

    /**
     *	XML tag name for the <code>version</code> attribute.
     */
    private final String ELM_VERSION 			= "version";

    /**
     *	XML tag name for the <code>lang</code> attribute.
     */
    private final String ELM_LANG 				= "lang";

}

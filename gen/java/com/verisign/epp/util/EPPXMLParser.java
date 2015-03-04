/* ===========================================================================
 * Copyright (C) 2002 VeriSign, Inc.
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
 * $Id: EPPXMLParser.java,v 1.2 2002/05/08 19:45:54 jgould Exp $
 *
 * ======================================================================== */

//----------------------------------------------
//
// package
//
//----------------------------------------------
package com.verisign.epp.util;


// Java imports
import java.io.InputStream;
import java.io.File;
import java.io.IOException;

// W3C Imports
import org.w3c.dom.*;
import org.xml.sax.*;

// JAXP imports
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

// Log4j Imports
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

// EPP SDK Imports


/**
* XML Parser class used for EPP parsing.  This class is
* a subclass of <code>DocumentBuilder</code> and is
* a thin-wrapper around an initialized <code>DocumentBuilder</code>
* instance.  The <code>EPPXMLParser</code> sets the
* required parser settings (i.e. Namespace aware, validating)
* and sets a standard error handler and entity resolver.
* Instances of <code>EPPXMLParser</code> can be polled,
* since the default constructor will do all of the initialization
* required for the parser to properly parse EPP XML messages.
*	<br>
*	<br>
* 	<div align="center">
*          Copyright (C) 2001 VeriSign, Inc.
* 	<br>
*	<b>Veri</b><i><font color="#ef0000"><b>S</b></font></i><b>ign Global Registry Services</b><br>
*	505 Huntmar Park Dr.
*	<br>
*	Herndon, VA 20170
*	</div>
* @author $Author: jgould $
* @version $Revision: 1.2 $
*/
public class EPPXMLParser extends DocumentBuilder {

	/**
	 * Name of the EPP XML Parser Pool managed by <code>GenericPoolManager</code>.
	 */
	public static final String POOL = "EPP_XML_PARSER_POOL";

	/**
	 * Default constructor, which will create the contained
	 * <code>DocumentBuilder</code> and will set the default
	 * error handler (<code>EPPXMLErrorHandler</code>) and the
	 * default entity resolver (<code>EPPEntityResolver</code>).
	 */
	public EPPXMLParser()  {
		try {
			_parser = _factory.newDocumentBuilder();
			_parser.setErrorHandler(new EPPXMLErrorHandler());
			_parser.setEntityResolver(new EPPEntityResolver());
		}
		catch (ParserConfigurationException ex) {
			cat.error("EPPXMLParser(): Error initializing parser: " + ex);
			ex.printStackTrace();
		}
	}

    /**
     * Create a new EPPXMLParser with the specified validation setting.
     *
     * @param validateSchema Parser will perform schema validation if true and won't
     * if false.
     */
    public EPPXMLParser(boolean validateSchema) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(validateSchema);
            factory.setNamespaceAware(true);

            _parser = factory.newDocumentBuilder();
            _parser.setErrorHandler(new EPPXMLErrorHandler());
            _parser.setEntityResolver(new EPPEntityResolver());
        }
        catch (ParserConfigurationException ex) {
            cat.error("EPPXMLParser(): Error initializing parser: " + ex);
            ex.printStackTrace();
        }
    }


	/**
	 * Delegates to the contained <code>DocumentBuilder</code>
	 * instance for behavior.
	 */
	public DOMImplementation getDOMImplementation() {
		return _parser.getDOMImplementation();
	}

	/**
	 * Delegates to the contained <code>DocumentBuilder</code>
	 * instance for behavior.
	 */
	public boolean isNamespaceAware() {
		return _parser.isNamespaceAware();
	}

	/**
	 * Delegates to the contained <code>DocumentBuilder</code>
	 * instance for behavior.
	 */
	public boolean isValidating() {
		return _parser.isValidating();
	}

	/**
	 * Delegates to the contained <code>DocumentBuilder</code>
	 * instance for behavior.
	public Document getDocument() {
		return _parser.getDocument();
	}
	 */

	/**
	 * Delegates to the contained <code>DocumentBuilder</code>
	 * instance for behavior.
	 */
	public Document parse(File aFile)
		throws SAXException, IOException {
		return _parser.parse(aFile);
	}

	/**
	 * Delegates to the contained <code>DocumentBuilder</code>
	 * instance for behavior.
	 */
	public Document parse(InputStream aStream)
		throws SAXException, IOException {
		return _parser.parse(aStream);
	}

	/**
	 * Delegates to the contained <code>DocumentBuilder</code>
	 * instance for behavior.
	 */
	public Document parse(InputSource aSource)
		throws SAXException, IOException {
		return _parser.parse(aSource);
	}


	/**
	 * Delegates to the contained <code>DocumentBuilder</code>
	 * instance for behavior.
	 */
	public Document parse(InputStream aStream, String aSystemId)
		throws SAXException, IOException {
		return _parser.parse(aStream, aSystemId);
	}

	/**
	 * Delegates to the contained <code>DocumentBuilder</code>
	 * instance for behavior.
	 */
	public Document parse(String aURI)
		throws SAXException, IOException {
		return _parser.parse(aURI);
	}

	/**
	 * Delegates to the contained <code>DocumentBuilder</code>
	 * instance for behavior.
	 */
	public void setEntityResolver(EntityResolver aResolver) {
		_parser.setEntityResolver(aResolver);
	}

	/**
	 * Delegates to the contained <code>DocumentBuilder</code>
	 * instance for behavior.
	 */
	public void setErrorHandler(ErrorHandler aHandler) {
		_parser.setErrorHandler(aHandler);
	}

	/**
	 * Delegates to the contained <code>DocumentBuilder</code>
	 * instance for behavior.
	 */
	public Document newDocument() {
		return _parser.newDocument();
	}


	/**
	 * Contained <code>DocumentBuilder</code> instance.
	 */
	private DocumentBuilder _parser = null;

	/**
	 * <code>DocumentBuilderFactory</code> for creating <code>DocumentBuidler</code>
	 * instances that have validation set to <code>true</code> and
	 * Namespace awareness set to <code>true</code>.
	 */
    private static DocumentBuilderFactory _factory;


	/**
	 * Static initializer for the <code>DocumentBuilderFactor</code>,
	 * which sets validation to <code>true</code> and
	 * Namespace awareness to <code>true</code>.
	 */
    static {
        _factory = DocumentBuilderFactory.newInstance();

        _factory.setNamespaceAware(true);
        _factory.setValidating(EPPEnv.getValidating());
	}


    /**
     * Log4j category for logging
     */
    private static Logger cat = Logger.getLogger(EPPXMLParser.class.getName(),
													   EPPCatFactory.getInstance().getFactory());

} // End class EPPXMLParser

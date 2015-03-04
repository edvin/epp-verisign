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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.verisign.epp.util.EPPCatFactory;


/**
 * <code>EPPFactory</code> is a Singleton class that will create
 * <code>EPPCommand</code> or <code>EPPResponse</code>     instances for any
 * of the supported EPP Command Mappings (e.g. domain, host, contact).  When a
 * create method is called, <code>EPPFactory</code> will use the XML namespace
 * of the element to     call the appropriate concrete
 * <code>EPPMapFactory</code>, which will create the correct concrete
 * <code>EPPCommand</code> or <code>EPPResponse</code>.
 * <code>createCommand(String, String)</code>     is provided to instantiate
 * <code>EPPCommand</code> objects associated with the     general EPP
 * Specification, which includes <code>EPPLoginCmd</code> and
 * <code>EPPLogout</code>.     <br>
 * <br>
 * <code>EPPFactory</code> is initialized with the set of available EPP Command
 * Mappings.     A method is provided to retrieve the list
 * <code>EPPService</code> descriptions     of available EPP Command Mappings.
 * The list of available EPP Command Mappings can be used     in
 * <code>EPPGreeting</code> and in <code>EPPLoginCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.4 $
 *
 * @see com.verisign.epp.codec.gen.EPPMapFactory
 * @see com.verisign.epp.codec.gen.EPPGreeting
 * @see com.verisign.epp.codec.gen.EPPLoginCmd
 */
public class EPPFactory {
	/** Single EPPFactory instance. */
	private static EPPFactory instance = new EPPFactory();

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPFactory.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** Hash of EPP Command Mappings indexed by XML Namespace URI. */
	private Hashtable factories = new Hashtable();

	/** Hash of EPP Extension Factories indexed by XML Namespace URI. */
	private Hashtable extFactories = new Hashtable();
	
	/** 
	 * Ordered list of registered map factories based on registration order.
	 * This is needed to maintain the order of the registered factories for 
	 * pre-loading the XML schemas.
	 */
	private Set factoriesSet = new LinkedHashSet();
	
	/** 
	 * Ordered list of registered ext factories based on registration order.
	 * This is needed to maintain the order of the registered ext factories for 
	 * pre-loading the XML schemas.
	 */
	private Set extFactoriesSet = new LinkedHashSet();
	

	/**
	 * Protected constructor following the Singleton Design Pattern.
	 */
	protected EPPFactory() {
	}

	/**
	 * Singleton Design Pattern method to retrieve the single
	 * <code>EPPFactory</code> instance.
	 *
	 * @return Single <code>EPPFactory</code> instance.
	 */
	public static EPPFactory getInstance() {
		return instance;
	}

	/**
	 * creates a concrete <code>EPPCommand</code> given a     command type and
	 * a command operation.  This method should only be used for     commands
	 * that don't have an EPP Namespace extension.  The only commands
	 * currently supported include <code>EPPCommand.TYPE_LOGIN</code> and
	 * <code>EPPCommand.EPP_LOGOUT</code>.
	 *
	 * @param aCommandType <code>Command.TYPE_</code> constant associated with
	 * 		  the <code>EPPCommand</code>.
	 * @param aOp <code>Command.OP_</code> constant associated with
	 * 		  <code>EPPCommand</code>.  This should be     <code>null</code>
	 * 		  if there is no operation value associated with the command.
	 *
	 * @return Concrete command associated with the command tag and optional
	 * 		   operation tag
	 *
	 * @exception EPPCodecException Unable to create command map.
	 */
	public EPPCommand createCommand(String aCommandType, String aOp)
							 throws EPPCodecException {
		String commandTypeLocalName = EPPUtil.getLocalName(aCommandType);
		
		if (commandTypeLocalName.equals(EPPUtil.getLocalName(EPPCommand.TYPE_LOGIN))) {
			return new EPPLoginCmd();
		}
		else if (commandTypeLocalName.equals(EPPUtil.getLocalName(EPPCommand.TYPE_LOGOUT))) {
			return new EPPLogoutCmd();
		}
		else if (commandTypeLocalName.equals(EPPUtil.getLocalName(EPPCommand.TYPE_POLL))) {
			return new EPPPollCmd();
		}
		else {
			throw new EPPCodecException("Invalid command type " + commandTypeLocalName
										+ " in EPPFactory.createCommand");
		}
	}

	/**
	 * creates a concrete <code>EPPCommand</code> given a DOM Command Mapping
	 * Element.  The DOM Command     Mapping Element must have an XML
	 * namespace URI, which is used to determine the concrete
	 * <code>EPPMapFactory</code> to use to create the
	 * <code>EPPCommand</code>.
	 *
	 * @param aMapElement The DOM Element associated with the command mapping.
	 *
	 * @return EPPCommand    Concrete <code>EPPCommand</code> associated with
	 * 		   <code>aMapElement</code> parameter.
	 *
	 * @exception EPPCodecException Unable to create concrete
	 * 			  <code>EPPCommand</code>.
	 */
	public EPPCommand createCommand(Element aMapElement)
							 throws EPPCodecException {
		String		  nsUri = aMapElement.getNamespaceURI();

		EPPMapFactory target = (EPPMapFactory) factories.get(nsUri);

		if (target == null) {
			throw new EPPCodecException("Unable to find factory for namespace "
										+ aMapElement.getNamespaceURI());
		}

		return target.createCommand(aMapElement);
	}

	/**
	 * creates a concrete <code>EPPResponse</code> given a DOM Command Mapping
	 * Element.  The DOM Command     Mapping Element must have an XML
	 * Namespace URI, which is used to determine the concrete
	 * <code>EPPMapFactory</code> to use to create the
	 * <code>EPPResponse</code>.
	 *
	 * @param aMapElement The DOM Element associated with the command mapping.
	 *
	 * @return EPPCommand    Concrete <code>EPPResponse</code> associated with
	 * 		   <code>aMapElement</code> parameter.
	 *
	 * @exception EPPCodecException Unable to create concrete
	 * 			  <code>EPPCommand</code>.
	 */
	public EPPResponse createResponse(Element aMapElement)
							   throws EPPCodecException {
		EPPMapFactory target =
			(EPPMapFactory) factories.get(aMapElement.getNamespaceURI());

		if (target == null) {
			throw new EPPCodecException("Unable to find factory for namespace "
										+ aMapElement.getNamespaceURI());
		}

		return target.createResponse(aMapElement);
	}

	/**
	 * creates a concrete <code>EPPCommand</code> given a DOM Command Mapping
	 * Element.  The DOM Command     Mapping Element must have an XML
	 * namespace URI, which is used to determine the concrete
	 * <code>EPPMapFactory</code> to use to create the
	 * <code>EPPCommand</code>.
	 *
	 * @param aExtensionElm The DOM Element associated with the command
	 * 		  mapping.
	 *
	 * @return EPPCommand    Concrete <code>EPPCommand</code> associated with
	 * 		   <code>aMapElement</code> parameter.
	 *
	 * @exception EPPCodecException Unable to create concrete
	 * 			  <code>EPPCommand</code>.
	 */
	public EPPCodecComponent createExtension(Element aExtensionElm)
									  throws EPPCodecException {
		String		  nsUri = aExtensionElm.getNamespaceURI();

		EPPExtFactory target = (EPPExtFactory) extFactories.get(nsUri);

		if (target == null) {
			throw new EPPCodecException("Unable to find extension factory for namespace "
										+ aExtensionElm.getNamespaceURI());
		}

		return target.createExtension(aExtensionElm);
	}

	/**
	 * creates a concrete <code>EPPProtocolExtension</code> given a DOM
	 * protocol extension Element. The extension element must have an XML
	 * namespace URI, which is used to determine the concrete
	 * <code>EPPExtFactory</code> to use to create the
	 * <code>EPPProtocolExtension</code>.
	 *
	 * @param aExtensionElm The Protocol extension Element .
	 *
	 * @return EPPProtocolExtension.
	 *
	 * @exception EPPCodecException Unable to create concrete
	 * 			  <code>EPPCommand</code>.
	 */
	public EPPProtocolExtension createProtocolExtension(Element aExtensionElm)
												 throws EPPCodecException {
		String		  nsUri = aExtensionElm.getNamespaceURI();

		EPPExtFactory target = (EPPExtFactory) extFactories.get(nsUri);

		if (target == null) {
			throw new EPPCodecException("Unable to find extension factory for namespace "
										+ aExtensionElm.getNamespaceURI());
		}

		return target.createProtocolExtension(aExtensionElm);
	}

	/**
	 * Gets a Vector of EPPService objects that represent the supported EPP
	 * Command Mappings.  EPP     can support a dynamic set of command
	 * mappings, and each command mapping will have     an EPPService object
	 * that contains information about the command mapping.
	 *
	 * @return Vector of EPPService objects, each representing an EPP Command
	 * 		   Mapping.
	 */
	public Vector getServices() {
		Vector	    retServices = new Vector();

		Enumeration factoryElms = factories.elements();

		while (factoryElms.hasMoreElements()) {
			EPPMapFactory currFactory =
				(EPPMapFactory) factoryElms.nextElement();

			retServices.addElement(currFactory.getService());
		}

		return retServices;
	}

	/**
	 * Gets a Vector of EPPService objects that represent the supported EPP
	 * Extensions. EPP can support a dynamic set of command and response
	 * extensions. an <code>EPPService</code>. object that contains
	 * information about the extension.
	 *
	 * @return <code>Vector</code> of <code>EPPService</code> objects, each
	 * 		   representing an EPP Extension.
	 */
	public Vector getExtensions() {
		Vector	    retServices = new Vector();

		Enumeration factoryElms = extFactories.elements();

		while (factoryElms.hasMoreElements()) {
			EPPExtFactory currFactory =
				(EPPExtFactory) factoryElms.nextElement();

			retServices.addElement(currFactory.getService());
		}

		return retServices;
	}

	/**
	 * Does the EPP support a service specified by the service namespace URI?
	 * For example,     the service namespace URI for the Domain Command
	 * Mapping is "urn:iana:xmlns:domain".
	 *
	 * @param aNamespace XML Namespace URI associated with service.  For
	 * 		  example "urn:iana:xmlns:domain"     for the Domain Command
	 * 		  Mapping.
	 *
	 * @return <code>true</code> if EPP supports the service;
	 * 		   <code>false</code> otherwise.
	 */
	public boolean hasService(String aNamespace) {
		return factories.containsKey(aNamespace);
	}

	/**
	 * Does the EPP support an extension specified by the namespace URI?  For
	 * example,     the service namespace URI for the Domain Command Mapping
	 * is "urn:verisign:xmlns:pricing".
	 *
	 * @param aNamespace XML Namespace URI associated with extension.  For
	 * 		  example "urn:verisign:xmlns:pricing"     for the Domain Command
	 * 		  Mapping.
	 *
	 * @return <code>true</code> if EPP supports the extension;
	 * 		   <code>false</code> otherwise.
	 */
	public boolean hasExtension(String aNamespace) {
		return extFactories.containsKey(aNamespace);
	}

	/**
	 * Initialize the Singleton EPP instance with the list of available
	 * concrete EPPMapFactory's.  The     concrete EPPMapFactory's are
	 * specified by their fully qualified class names.  For example,     the
	 * EPPDomainMapFactory can be initialized in <code>EPPFactory</code> with
	 * the following     calls:<br>
	 * <pre>
	 * 	Vector theFactories = new Vector();
	 * 	theFactories.addElement("com.verisign.epp.codec.domain.EPPDomainMapFactory");
	 * 	EPPFactory.getInstance().init(theFactories);
	 * 	</pre>
	 *
	 * @param someFactories a Vector of concrete <code>EPPMapFactory</code>
	 * 		  fully qualified class names.
	 *
	 * @exception EPPCodecException Error initializing <code>EPPFactory</code>
	 */
	public void init(Vector someFactories) throws EPPCodecException {
		cat.debug("init(Vector): enter");

		EPPService    currService     = null;
		EPPMapFactory currFactory     = null;
		String		  currFactoryName = null;

		factories = new Hashtable();

		// For each concrete EPPMapFactory
		for (int i = 0; i < someFactories.size(); i++) {
			currFactoryName = (String) someFactories.elementAt(i);
			cat.debug("init(Vector): Add Map Factory <" + currFactoryName + ">");
			addMapFactory(currFactoryName);
		}

		// end for each factory
		extFactories = new Hashtable();

		cat.debug("init(Vector): exit");
	}

	/**
	 * Initialize the Singleton EPP instance with the list of available
	 * concrete <code>EPPMapFactory</code>'s and concrete
	 * <code>EPPExtFactory</code>'s.  The concrete
	 * <code>EPPMapFactory</code>'s and <code>EPPExtFactory</code>'s are
	 * specified by their fully qualified class names.  For example, the
	 * EPPDomainMapFactory and EPPPricingExtFactory can be initialized in
	 * <code>EPPFactory</code> with the following calls:<br>
	 * <pre>
	 * Vector theMapFactories = new Vector();
	 * theMapFactories.addElement("com.verisign.epp.codec.domain.EPPDomainMapFactory");
	 * theExtFactories = new Vector();
	 * theExtFactories.addElement("com.verisign.epp.codec.pricing.EPPPricingExtFactory");
	 * EPPFactory.getInstance().init(theMapFactories, theExtFactories);
	 * </pre>
	 *
	 * @param someFactories a <code>Vector</code> of concrete
	 * 		  <code>EPPMapFactory</code> fully qualified class names.
	 * @param someExtFactories a <code>Vector</code> of concrete
	 * 		  <code>EPPExtFactory</code> fully qualified class names.
	 *
	 * @exception EPPCodecException Error initializing <code>EPPFactory</code>
	 */
	public void init(Vector someFactories, Vector someExtFactories)
			  throws EPPCodecException {
		cat.debug("init(Vector, Vector): enter");

		// Initialize the EPPMapFactory's
		init(someFactories);

		EPPService    currService     = null;
		EPPExtFactory currFactory     = null;
		String		  currFactoryName = null;

		// For each concrete EPPExtFactory
		for (int i = 0; i < someExtFactories.size(); i++) {
			currFactoryName = (String) someExtFactories.elementAt(i);
			cat.debug("init(Vector, Vector): Add Ext Factory <"
					  + currFactoryName + ">");
			addExtFactory(currFactoryName);
		}

		// end for each factory
		cat.debug("init(Vector, Vector): exit");
	}

	/**
	 * Add a concrete <code>EPPMapFactory</code> if it does not already exist
	 * in <code>EPPFactory</code>.
	 *
	 * @param aMapFactory a concrete <code>EPPMapFactory</code> fully qualified
	 * 		  class names.
	 *
	 * @exception EPPCodecException Error add <code>EPPMapFactory</code> to
	 * 			  <code>EPPFactory</code>.
	 */
	public void addMapFactory(String aMapFactory) throws EPPCodecException {
		cat.debug("addMapFactory(String): enter");

		try {
			// Does aMapFactory not already exist?
			if (!mapFactoryExists(aMapFactory)) {
				cat.info("addMapFactory(String): Loading <" + aMapFactory + ">");

				// Instantiate EPPMapFactory and add to factories Hashtable.
				Class factoryClass = Class.forName(aMapFactory);

				if (!EPPMapFactory.class.isAssignableFrom((factoryClass))) {
					throw new EPPCodecException(aMapFactory
												+ " is not a subclass of EPPMapFactory");
				}

				EPPMapFactory theFactory =
					(EPPMapFactory) factoryClass.newInstance();

				EPPService    theService = theFactory.getService();

				if (theService == null) {
					throw new EPPCodecException(aMapFactory
												+ " returned null on call to getService");
				}

				cat.info("addMapFactory(String): Adding <" + aMapFactory
						 + "> with Namespace <" + theService.getNamespaceURI()
						 + ">");

				factories.put(theService.getNamespaceURI(), theFactory);
				
				this.factoriesSet.add(theFactory);
			}
		}
		 catch (ClassNotFoundException e) {
			throw new EPPCodecException(aMapFactory
										+ " ClassNotFoundException: " + e);
		}
		 catch (IllegalAccessException e) {
			throw new EPPCodecException(aMapFactory
										+ " IllegalAccessException: " + e);
		}
		 catch (InstantiationException e) {
			throw new EPPCodecException(aMapFactory
										+ " InstantiationException: " + e);
		}

		cat.debug("addMapFactory(String): exit");
	}

	/**
	 * Add a concrete <code>EPPExtFactory</code> if it does not already exist
	 * in <code>EPPFactory</code>.
	 *
	 * @param aExtFactory a concrete <code>EPPExtFactory</code> fully qualified
	 * 		  class names.
	 *
	 * @exception EPPCodecException Error add <code>EPPExtFactory</code> to
	 * 			  <code>EPPFactory</code>.
	 */
	public void addExtFactory(String aExtFactory) throws EPPCodecException {
		cat.debug("addExtFactory(String): enter");

		try {
			// Does aExtFactory not already exist?
			if (!extFactoryExists(aExtFactory)) {
				cat.info("addExtFactory(String): Loading <" + aExtFactory + ">");

				// Instantiate EPPExtFactory and add to factories Hashtable.
				Class factoryClass = Class.forName(aExtFactory);

				if (!EPPExtFactory.class.isAssignableFrom((factoryClass))) {
					throw new EPPCodecException(aExtFactory
												+ " is not a subclass of EPPExtFactory");
				}

				EPPExtFactory theFactory =
					(EPPExtFactory) factoryClass.newInstance();

				EPPService    theService = theFactory.getService();

				if (theService == null) {
					throw new EPPCodecException(aExtFactory
												+ " returned null on call to getService");
				}

				cat.info("addExtFactory(String): Adding <" + aExtFactory
						 + "> with Namespace <" + theService.getNamespaceURI()
						 + ">");

				extFactories.put(theService.getNamespaceURI(), theFactory);
				
				this.extFactoriesSet.add(theFactory);
			}
		}
		 catch (ClassNotFoundException e) {
			throw new EPPCodecException(aExtFactory
										+ " ClassNotFoundException: " + e);
		}
		 catch (IllegalAccessException e) {
			throw new EPPCodecException(aExtFactory
										+ " IllegalAccessException: " + e);
		}
		 catch (InstantiationException e) {
			throw new EPPCodecException(aExtFactory
										+ " InstantiationException: " + e);
		}

		cat.debug("addExtFactory(String): exit");
	}

	
	/**
	 * Gets the list of XML schemas that need to be pre-loaded into the 
	 * XML Parser.  Each {@link EPPMapFactory} and {@link EPPExtFactory} include 
	 * a list of XML schemas that need to be loaded into the XML Parser, so 
	 * <code>getXmlSchemas()</code> will aggregate them into a single <code>List</code>.
	 * Each XML schemas should not include the path, since the schema will be 
	 * loaded as a resource from schemas folder of the classpath.
	 *    
	 * @return <code>Set</code> of <code>String</code> XML Schema names that 
	 * should be pre-loaded in the XML Parser.  For example, at a minimum, 
	 * the following two XML schemas will be returned:<br>
	 * <br><ul>
	 * <li><code>eppcom-1.0.xsd</code>
	 * <li><code>epp-1.0.xsd</code>
	 * </ul>
	 */
	public Set getXmlSchemas() {
		cat.debug("getXmlSchemas(): enter");
		
		Set theSchemas = new LinkedHashSet();
		
		// Pre-load the two required EPP XML schemas
		cat.debug("getXmlSchemas(): Loading required EPP XML schemas");
		theSchemas.add("eppcom-1.0.xsd");
		theSchemas.add("epp-1.0.xsd");
		
		// Load the EPP XML schemas from the EPPMapFactory instances
		Iterator theFactoryIter = this.factoriesSet.iterator();

		while (theFactoryIter.hasNext()) {
			EPPMapFactory currFactory =
				(EPPMapFactory) theFactoryIter.next();
			
			cat.debug("getXmlSchemas(): Loading EPP XML schemas from " + currFactory.getClass().getName());
		
			
			if (currFactory.getXmlSchemas() != null) {
				theSchemas.addAll(currFactory.getXmlSchemas());
			}
		}
		
		// Load the EPP XML schemas from the EPPExtFactory instances
		Iterator theExtFactoryIter = this.extFactoriesSet.iterator();

		while (theExtFactoryIter.hasNext()) {
			EPPExtFactory currFactory =
				(EPPExtFactory) theExtFactoryIter.next();
			
			cat.debug("getXmlSchemas(): Loading EPP XML schemas from " + currFactory.getClass().getName());
			
			if (currFactory.getXmlSchemas() != null) {
				theSchemas.addAll(currFactory.getXmlSchemas());
			}
		}
		
		if (cat.isDebugEnabled()) {
			cat.debug("getXmlSchemas(): XML Schemas = " + theSchemas);
		}
		
		cat.debug("getXmlSchemas(): exit");
		return theSchemas;
	}
	
	/**
	 * Does the concrete <code>EPPMapFactory</code> exist in
	 * <code>EPPFactory</code>?
	 *
	 * @param aMapFactory a concrete <code>EPPMapFactory</code> fully qualified
	 * 		  class names.
	 *
	 * @return <code>true</code> if <code>aMapFactory</code> exists;
	 * 		   <code>false</code> otherwise.
	 */
	boolean mapFactoryExists(String aMapFactory) {
		Enumeration enumeration = factories.elements();

		while (enumeration.hasMoreElements()) {
			if (enumeration.nextElement().getClass().getName().equals(aMapFactory)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Does the concrete <code>EPPExtFactory</code> exist in
	 * <code>EPPFactory</code>?
	 *
	 * @param aExtFactory a concrete <code>EPPExtFactory</code> fully qualified
	 * 		  class names.
	 *
	 * @return <code>true</code> if <code>aExtFactory</code> exists;
	 * 		   <code>false</code> otherwise.
	 */
	boolean extFactoryExists(String aExtFactory) {
		Enumeration enumeration = extFactories.elements();

		while (enumeration.hasMoreElements()) {
			if (enumeration.nextElement().getClass().getName().equals(aExtFactory)) {
				return true;
			}
		}

		return false;
	}
	
	
}

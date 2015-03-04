package com.verisign.epp.codec.registry;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPMapFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPService;
import com.verisign.epp.codec.gen.EPPUtil;

public class EPPRegistryMapFactory extends EPPMapFactory {
	/** Namespace URI associated with EPPRegistryMapFactory. */
	public static final String NS = "http://www.verisign.com/epp/registry-1.0";

    /** Namespace prefix associated with EPPRegistryMapFactory. */
	public static final String NS_PREFIX = "registry";

	/** EPP Registry XML Schema. */
	public static final String NS_SCHEMA =
		"http://www.verisign.com/epp/registry-1.0 registry-1.0.xsd";
	
	/**
	 * XML tag name associated with registry authorization information. This
	 * value will be passed to the authInfo object when it is initialized in
	 * registry command mappings.
	 */
	public static final String ELM_REGISTRY_AUTHINFO = "registry:authInfo";

	private EPPService service = null;

	public EPPRegistryMapFactory() {
		service = new EPPService(NS_PREFIX, NS, NS_SCHEMA);
	}

	public EPPCommand createCommand(Element aMapElement)
			throws EPPCodecException {

		String name = aMapElement.getLocalName();

		if (!aMapElement.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid mapping type " + name);
		}		
		
		if (name.equals(EPPUtil.getLocalName(EPPRegistryInfoCmd.ELM_NAME))) {
			return new EPPRegistryInfoCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPRegistryCheckCmd.ELM_NAME))) {
			return new EPPRegistryCheckCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPRegistryCreateCmd.ELM_NAME))) {
			return new EPPRegistryCreateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPRegistryUpdateCmd.ELM_NAME))) {
			return new EPPRegistryUpdateCmd();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPRegistryDeleteCmd.ELM_NAME))) {
			return new EPPRegistryDeleteCmd();
		}
		else {
			throw new EPPCodecException("Invalid response element " + name);
		}
	}

	/**
	 * creates a concrete <code>EPPResponse</code> from the passed in XML
	 * Element tree. <code>aMapElement</code> must be the root node for the
	 * command extension. For example, &lt;contact:info-data&gt; must be the
	 * element passed for a Contact Info Response.
	 * 
	 * @param aMapElement
	 *            Mapping Extension EPP XML Element.
	 * 
	 * @return Concrete <code>EPPResponse</code> instance associated with
	 *         <code>aMapElement</code>.
	 * 
	 * @exception EPPCodecException
	 *                Error creating concrete <code>EPPResponse</code>
	 */
	public EPPResponse createResponse(Element aMapElement)
			throws EPPCodecException {
		String name = aMapElement.getLocalName();

		if (!aMapElement.getNamespaceURI().equals(NS)) {
			throw new EPPCodecException("Invalid mapping type " + name);
		}		

		if (name.equals(EPPUtil.getLocalName(EPPRegistryInfoResp.ELM_NAME))) {
			return new EPPRegistryInfoResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPRegistryCheckResp.ELM_NAME))) {
			return new EPPRegistryCheckResp();
		}
		else if (name.equals(EPPUtil.getLocalName(EPPRegistryCreateResp.ELM_NAME))) {
			return new EPPRegistryCreateResp();
		}
		else {
			throw new EPPCodecException("Invalid response element " + name);
		}
	}

	/**
	 * Gets the <code>EPPService</code> associated with
	 * <code>EPPRegistryMapFactory</code>. The <code>EPPService</code> is used
	 * by <code>EPPFactory</code> for distributing the responsibility of
	 * creating concrete <code>EPPCommand</code> and <code>EPPResponse</code>
	 * objects by XML namespace. The XML namespace is defined in the returned
	 * <code>EPPService</code>.
	 * 
	 * @return service description for the Registry Command Mapping.
	 */
	public EPPService getService() {
		return service;
	}

	/**
	 * Gets the list of XML schemas that need to be pre-loaded into the XML
	 * Parser.
	 * 
	 * @return <code>Set</code> of <code>String</code> XML Schema names that
	 *         should be pre-loaded in the XML Parser.
	 * 
	 * @see com.verisign.epp.codec.gen.EPPMapFactory#getXmlSchemas()
	 */
	public Set getXmlSchemas() {
		Set theSchemas = new HashSet();
		theSchemas.add("registry-1.0.xsd");
		return theSchemas;
	}
}

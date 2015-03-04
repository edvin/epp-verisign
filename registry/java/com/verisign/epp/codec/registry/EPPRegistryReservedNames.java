package com.verisign.epp.codec.registry;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

public class EPPRegistryReservedNames implements EPPCodecComponent {
	private static final long serialVersionUID = -483506152124947305L;

	final static String ELM_NAME = "registry:reservedNames";
	final static String ELM_RESERVED_NAME = "registry:reservedName";
	final static String ELM_RESERVED_NAME_URI = "registry:reservedNameURI";

	// String
	private List reservedNames = new ArrayList();
	private String reservedNameURI = null;

	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryReservedNames.encode: " + e);
		}
		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		if (reservedNames != null && reservedNames.size() > 0) {
			EPPUtil.encodeList(aDocument, root, reservedNames,
					EPPRegistryMapFactory.NS, ELM_RESERVED_NAME);
		}
		if (reservedNameURI != null && reservedNameURI.trim().length() > 0) {
			EPPUtil.encodeString(aDocument, root, reservedNameURI,
					EPPRegistryMapFactory.NS, ELM_RESERVED_NAME_URI);
		}

		return root;
	}

	public void decode(Element aElement) throws EPPDecodeException {
		reservedNames = EPPUtil.decodeList(aElement, EPPRegistryMapFactory.NS,
				ELM_RESERVED_NAME);
		reservedNameURI = EPPUtil.decodeString(aElement,
				EPPRegistryMapFactory.NS, ELM_RESERVED_NAME_URI);
	}

	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryReservedNames)) {
			return false;
		}

		EPPRegistryReservedNames theComp = (EPPRegistryReservedNames) aObject;
		if (!((reservedNames == null) ? (theComp.reservedNames == null)
				: EPPUtil.equalLists(reservedNames, theComp.reservedNames))) {
			return false;
		}
		if (!((reservedNameURI == null) ? (theComp.reservedNameURI == null)
				: reservedNameURI.equals(theComp.reservedNameURI))) {
			return false;
		}

		return true;
	}

	void validateState() throws EPPCodecException {
		if (reservedNames != null && reservedNames.size() > 0
				&& reservedNameURI != null
				&& reservedNameURI.trim().length() > 0) {
			throw new EPPCodecException(
					"reservedNames and reservedNameURI cannot be used together");
		}
	}

	public Object clone() throws CloneNotSupportedException {
		EPPRegistryReservedNames clone = (EPPRegistryReservedNames) super
				.clone();
		if (reservedNames != null) {
			clone.reservedNames = (List) ((ArrayList) reservedNames).clone();
		}
		return clone;
	}

	public String toString() {
		return EPPUtil.toString(this);
	}

	public List getReservedNames() {
		return reservedNames;
	}

	public void setReservedNames(List reservedNames) {
		this.reservedNames = reservedNames;
	}

	public void addReservedName(String reservedName) {
		if (this.reservedNames == null) {
			this.reservedNames = new ArrayList();
		}
		this.reservedNames.add(reservedName);
	}

	public String getReservedNameURI() {
		return reservedNameURI;
	}

	public void setReservedNameURI(String reservedNameURI) {
		this.reservedNameURI = reservedNameURI;
	}
}

package com.verisign.epp.codec.registry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

public class EPPRegistryMaxSig implements EPPCodecComponent {
	private static final long serialVersionUID = 4505254498272997391L;

	public static final String ELM_NAME = "registry:maxSigLife";

	public static final String ELM_CLIENT_DEFINED = "registry:clientDefined";
	public static final String ELM_DEFAULT = "registry:default";
	public static final String ELM_MIN = "registry:min";
	public static final String ELM_MAX = "registry:max";

	private Boolean clientDefined = Boolean.FALSE;
	private Integer defaultLife;
	private Integer min = null;
	private Integer max = null;

	public EPPRegistryMaxSig() {

	}

	public EPPRegistryMaxSig(Boolean clientDefined, Integer defaultLife,
			Integer min, Integer max) {
		super();
		this.clientDefined = clientDefined;
		this.defaultLife = defaultLife;
		this.min = min;
		this.max = max;
	}

	public EPPRegistryMaxSig(boolean clientDefined, int defaultLife, int min,
			int max) {
		this(Boolean.valueOf(clientDefined), new Integer(defaultLife),
				new Integer(min), new Integer(max));
	}

	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryMaxSig.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		if (clientDefined == null) {
			clientDefined = Boolean.FALSE;
		}
		EPPUtil.encodeString(aDocument, root, clientDefined.toString(),
				EPPRegistryMapFactory.NS, ELM_CLIENT_DEFINED);
		if (defaultLife != null) {
			EPPUtil.encodeString(aDocument, root, defaultLife.toString(),
					EPPRegistryMapFactory.NS, ELM_DEFAULT);
		}
		if (min != null) {
			EPPUtil.encodeString(aDocument, root, min.toString(),
					EPPRegistryMapFactory.NS, ELM_MIN);
		}
		if (max != null) {
			EPPUtil.encodeString(aDocument, root, max.toString(),
					EPPRegistryMapFactory.NS, ELM_MAX);
		}

		return root;
	}

	public void decode(Element aElement) throws EPPDecodeException {
		clientDefined = EPPUtil.decodeBoolean(aElement,
				EPPRegistryMapFactory.NS, ELM_CLIENT_DEFINED);
		defaultLife = EPPUtil.decodeInteger(aElement, EPPRegistryMapFactory.NS,
				ELM_DEFAULT);
		min = EPPUtil
				.decodeInteger(aElement, EPPRegistryMapFactory.NS, ELM_MIN);
		max = EPPUtil
				.decodeInteger(aElement, EPPRegistryMapFactory.NS, ELM_MAX);

	}

	void validateState() throws EPPCodecException {
		if (clientDefined == null || clientDefined == Boolean.FALSE) {
			if (min != null || max != null) {
				throw new EPPCodecException(
						"None of min or max should be set when clientDefined is set to false");
			}
		}
		if (min != null && min.intValue() < 1) {
			throw new EPPCodecException(
					"min, if specified, should be no less than 1");
		}
		if (max != null && max.intValue() < 1) {
			throw new EPPCodecException(
					"max, if specified, should be no less than 1");
		}
		if (defaultLife != null && defaultLife.intValue() < 1) {
			throw new EPPCodecException(
					"defaultLife, if specified, should be no less than 1");
		}
		if (min != null && max != null) {
			if (max.intValue() < min.intValue()) {
				throw new EPPCodecException(
						"max should be no less than min");
			}
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return (EPPRegistryMaxSig) super.clone();
	}

	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryMaxSig)) {
			return false;
		}

		EPPRegistryMaxSig theComp = (EPPRegistryMaxSig) aObject;

		if (!((clientDefined == null) ? (theComp.clientDefined == null)
				: clientDefined.equals(theComp.clientDefined))) {
			return false;
		}
		if (!((defaultLife == null) ? (theComp.defaultLife == null)
				: defaultLife.equals(theComp.defaultLife))) {
			return false;
		}
		if (!((min == null) ? (theComp.min == null) : min.equals(theComp.min))) {
			return false;
		}
		if (!((max == null) ? (theComp.max == null) : max.equals(theComp.max))) {
			return false;
		}

		return true;
	}

	public String toString() {
		return EPPUtil.toString(this);
	}

	public Boolean getClientDefined() {
		return clientDefined;
	}

	public void setClientDefined(Boolean clientDefined) {
		this.clientDefined = clientDefined;
	}

	public Integer getDefaultLife() {
		return defaultLife;
	}

	public void setDefaultLife(Integer defaultLife) {
		this.defaultLife = defaultLife;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}
}

package com.verisign.epp.codec.registry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

public class EPPRegistryMinMax implements EPPCodecComponent {
	private static final long serialVersionUID = -2336341337762947833L;

	public static final String ELM_MIN = "registry:min";
	public static final String ELM_MAX = "registry:max";

	public static final String ELM_MIN_LENGTH = "registry:min";
	public static final String ELM_MAX_LENGTH = "registry:max";

	protected String elmMin = ELM_MIN;
	protected String elmMax = ELM_MAX;

	protected Integer min = null;
	protected Integer max = null;

	protected String rootName = null;

	public EPPRegistryMinMax() {
		super();
		this.elmMin = ELM_MIN_LENGTH;
		this.elmMax = ELM_MAX_LENGTH;
	}

	public EPPRegistryMinMax(Integer min, Integer max) {
		this();
		this.min = min;
		this.max = max;
	}

	public EPPRegistryMinMax(int min, int max) {
		this(new Integer(min), new Integer(max));
	}

	public Element encode(Document aDocument) throws EPPEncodeException {
		if (rootName == null) {
			throw new EPPEncodeException("Invalid state on "
					+ getClass().getName() + ".encode: rootName is not set");
		}
		if (min == null || min.intValue() < 0) {
			throw new EPPEncodeException("Invalid state on "
					+ getClass().getName()
					+ ".encode: min is required and should be greater than 0");
		}
		if (max != null && max.intValue() < min.intValue()) {
			throw new EPPEncodeException("Invalid state on "
					+ getClass().getName()
					+ ".encode: max, if specified, should be greater than min");
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				getRootName());
		EPPUtil.encodeString(aDocument, root, min.toString(),
				EPPRegistryMapFactory.NS, elmMin);
		if (max != null) {
			EPPUtil.encodeString(aDocument, root, max.toString(),
					EPPRegistryMapFactory.NS, elmMax);
		}

		return root;
	}

	public void decode(Element aElement) throws EPPDecodeException {
		min = EPPUtil.decodeInteger(aElement, EPPRegistryMapFactory.NS, elmMin);
		max = EPPUtil.decodeInteger(aElement, EPPRegistryMapFactory.NS, elmMax);
	}

	public Object clone() throws CloneNotSupportedException {
		return (EPPRegistryMinMax) super.clone();
	}

	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryMinMax)) {
			return false;
		}

		EPPRegistryMinMax theComp = (EPPRegistryMinMax) aObject;
		if (!((elmMin == null) ? (theComp.elmMin == null) : elmMin
				.equals(theComp.elmMin))) {
			return false;
		}
		if (!((elmMax == null) ? (theComp.elmMax == null) : elmMax
				.equals(theComp.elmMax))) {
			return false;
		}
		if (!((min == null) ? (theComp.min == null) : min.equals(theComp.min))) {
			return false;
		}
		if (!((max == null) ? (theComp.max == null) : max.equals(theComp.max))) {
			return false;
		}
		if (!((rootName == null) ? (theComp.rootName == null) : rootName
				.equals(theComp.rootName))) {
			return false;
		}

		return true;
	}

	public String toString() {
		return EPPUtil.toString(this);
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

	public String getRootName() {
		return rootName;
	}
	
	public void setRootName(String aRootName) {
		this.rootName = aRootName;
	}

	public String getElmMin() {
		return elmMin;
	}

	public void setElmMin(String elmMin) {
		this.elmMin = elmMin;
	}

	public String getElmMax() {
		return elmMax;
	}

	public void setElmMax(String elmMax) {
		this.elmMax = elmMax;
	}
}

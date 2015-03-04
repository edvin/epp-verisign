package com.verisign.epp.codec.registry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

public class EPPRegistryMinMaxPeriod implements EPPCodecComponent {
	private static final long serialVersionUID = -3291192948356470780L;

	public static final String ELM_NAME = "registry:length";
	private EPPRegistryMinPeriodType min;
	private EPPRegistryMaxPeriodType max;
	private EPPRegistryDefaultPeriodType defaultLength;

	private String rootName = null;

	public EPPRegistryMinMaxPeriod() {
		this.rootName = ELM_NAME;
	}

	public EPPRegistryMinMaxPeriod(Integer lMin, String uMin, Integer lMax,
			String uMax, Integer lDefault, String uDefault) {
		this();
		this.min = new EPPRegistryMinPeriodType(lMin, uMin);
		this.max = new EPPRegistryMaxPeriodType(lMax, uMax);
		this.defaultLength = new EPPRegistryDefaultPeriodType(lDefault,
				uDefault);
	}

	public EPPRegistryMinMaxPeriod(int min, String minUnit, int max,
			String maxUnit, int defaultLength, String defaultLengthUnit) {
		this(new Integer(min), minUnit, new Integer(max), maxUnit, new Integer(
				defaultLength), defaultLengthUnit);
	}

	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryMinMaxPeriod.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				getRootName());

		EPPUtil.encodeComp(aDocument, root, min);
		EPPUtil.encodeComp(aDocument, root, max);
		EPPUtil.encodeComp(aDocument, root, defaultLength);

		return root;
	}

	public void decode(Element aElement) throws EPPDecodeException {
		this.setMin((EPPRegistryMinPeriodType) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryMinPeriodType.ELM_NAME,
				EPPRegistryMinPeriodType.class));
		this.setMax((EPPRegistryMaxPeriodType) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryMaxPeriodType.ELM_NAME,
				EPPRegistryMaxPeriodType.class));
		this.setDefaultLength((EPPRegistryDefaultPeriodType) EPPUtil
				.decodeComp(aElement, EPPRegistryMapFactory.NS,
						EPPRegistryDefaultPeriodType.ELM_NAME,
						EPPRegistryDefaultPeriodType.class));
	}

	public Object clone() throws CloneNotSupportedException {
		EPPRegistryMinMaxPeriod clone = (EPPRegistryMinMaxPeriod) super.clone();

		if (min != null) {
			clone.min = (EPPRegistryMinPeriodType) min.clone();
		}

		if (max != null) {
			clone.max = (EPPRegistryMaxPeriodType) max.clone();
		}

		if (defaultLength != null) {
			clone.defaultLength = (EPPRegistryDefaultPeriodType) defaultLength
					.clone();
		}

		return clone;
	}

	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryMinMaxPeriod)) {
			return false;
		}

		EPPRegistryMinMaxPeriod theComp = (EPPRegistryMinMaxPeriod) aObject;

		if (!((min == null) ? (theComp.min == null) : min.equals(theComp.min))) {
			return false;
		}
		if (!((max == null) ? (theComp.max == null) : max.equals(theComp.max))) {
			return false;
		}
		if (!((defaultLength == null) ? (theComp.defaultLength == null)
				: defaultLength.equals(theComp.defaultLength))) {
			return false;
		}
		if (!((rootName == null) ? (theComp.rootName == null) : rootName
				.equals(theComp.rootName))) {
			return false;
		}

		return true;
	}

	void validateState() throws EPPCodecException {
		if (rootName == null || rootName.trim().length() == 0) {
			throw new EPPCodecException("rootName is not set");
		}
		if (min == null) {
			throw new EPPCodecException("min element is not set");
		}
		min.validateState();
		if (max == null) {
			throw new EPPCodecException("max element is not set");
		}
		max.validateState();
		if (defaultLength == null) {
			throw new EPPCodecException("defaultLength element is not set");
		}
		defaultLength.validateState();
	}

	public String toString() {
		return EPPUtil.toString(this);
	}

	public EPPRegistryMinPeriodType getMin() {
		return min;
	}

	public void setMin(EPPRegistryMinPeriodType min) {
		this.min = min;
	}

	public EPPRegistryMaxPeriodType getMax() {
		return max;
	}

	public void setMax(EPPRegistryMaxPeriodType max) {
		this.max = max;
	}

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public EPPRegistryDefaultPeriodType getDefaultLength() {
		return defaultLength;
	}

	public void setDefaultLength(EPPRegistryDefaultPeriodType defaultLength) {
		this.defaultLength = defaultLength;
	}
}

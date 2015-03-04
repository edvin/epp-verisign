package com.verisign.epp.codec.registry;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

public abstract class EPPRegistryPeriodType implements EPPCodecComponent {
	private static final long serialVersionUID = -3138571257611606559L;

	/** XML attribute name for the <code>period</code> element. */
	private final static java.lang.String ELM_PERIOD_UNIT = "unit";

	/** Period in Unit Month */
	public final static java.lang.String PERIOD_UNIT_MONTH = "m";

	/** Period in Unit Year */
	public final static java.lang.String PERIOD_UNIT_YEAR = "y";

	/** Period in Unit Day */
	public final static java.lang.String PERIOD_UNIT_DAY = "d";

	/** Period in Unit Hour */
	public final static java.lang.String PERIOD_UNIT_HOUR = "h";

	public final static Set VALID_UNITS = new HashSet();

	protected String rootName = null;

	public static final String ELM_NAME = "registry:period";

	protected Integer number = null;
	protected String unit = null;

	static {
		VALID_UNITS.add(PERIOD_UNIT_DAY);
		VALID_UNITS.add(PERIOD_UNIT_HOUR);
		VALID_UNITS.add(PERIOD_UNIT_MONTH);
		VALID_UNITS.add(PERIOD_UNIT_YEAR);
	}

	public EPPRegistryPeriodType() {
		super();
		this.rootName = ELM_NAME;
	}

	public EPPRegistryPeriodType(Integer number, String unit) {
		this();
		this.number = number;
		this.unit = unit;
	}

	public EPPRegistryPeriodType(int number, String unit) {
		this();
		this.number = new Integer(number);
		this.unit = unit;
	}

	public String getRootName() {
		return rootName;
	}

	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException("Invalid state on "
					+ this.getClass().getName() + ".encode: " + e);
		}
		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				getRootName());
		Text currVal = aDocument.createTextNode(String.valueOf(number));
		root.setAttribute(ELM_PERIOD_UNIT, unit);
		root.appendChild(currVal);

		return root;
	}

	public void decode(Element aElement) throws EPPDecodeException {
		if (aElement != null) {
			Node textNode = aElement.getFirstChild();

			// Element does have a text node?
			if (textNode != null) {

				String intValStr = textNode.getNodeValue();
				try {
					number = Integer.valueOf(intValStr);
				} catch (NumberFormatException e) {
					throw new EPPDecodeException(
							"Can't convert value to Integer: " + intValStr + e);
				}
			} else {
				throw new EPPDecodeException(
						"Can't decode numeric value from non-existant text node");
			}
		}
		unit = aElement.getAttribute(ELM_PERIOD_UNIT);
	}

	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryPeriodType)) {
			return false;
		}

		EPPRegistryPeriodType theComp = (EPPRegistryPeriodType) aObject;

		if (!((rootName == null) ? (theComp.rootName == null) : rootName
				.equals(theComp.rootName))) {
			return false;
		}
		if (!((number == null) ? (theComp.number == null) : number
				.equals(theComp.number))) {
			return false;
		}
		if (!((unit == null) ? (theComp.unit == null) : unit
				.equals(theComp.unit))) {
			return false;
		}

		return true;
	}

	void validateState() throws EPPCodecException {
		if (rootName == null || rootName.trim().length() == 0) {
			throw new EPPCodecException("rootName is not set");
		}
		if (number == null) {
			throw new EPPCodecException(getRootName()
					+ ": number should not be null");
		}
		if (unit == null || unit.length() == 0) {
			throw new EPPCodecException(getRootName()
					+ ": unit should not be null");
		}
		extraValidate();
	}

	/**
	 * Child class must implement this method for extra validation according to
	 * the mapping document.
	 * 
	 * @throws EPPCodecException
	 */
	abstract void extraValidate() throws EPPCodecException;

	public Object clone() throws CloneNotSupportedException {
		return (EPPRegistryPeriodType) super.clone();
	}

	public String toString() {
		return EPPUtil.toString(this);
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}

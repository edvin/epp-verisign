package com.verisign.epp.codec.registry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

public class EPPRegistryPhase implements EPPCodecComponent {
	private static final long serialVersionUID = 5493994152033865390L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPRegistryPhase.class);

	/**
	 * Constant for the phase local name
	 */
	public static final String ELM_LOCALNAME = "phase";

	/**
	 * Constant for the phase qualified name (prefix and local name)
	 */
	public final static String ELM_NAME = EPPRegistryMapFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	public final static String ELM_START_DATE = "startDate";

	public final static String ELM_END_DATE = "endDate";

	public final static String ATTR_TYPE = "type";

	public final static String ATTR_NAME = "name";

	public final static String ATTR_MODE = "mode";

	/**
	 * Phase when pre-delegation testing is done.
	 */
	public final static String PHASE_PRE_DELEGATION = "pre-delegation";

	/**
	 * A phase prior to the sunrise where no writable operations will be
	 * allowed.
	 */
	public final static String PHASE_PRE_LAUNCH = "pre-launch";

	/**
	 * Phase when trademark holders can submit registration applications with
	 * trademark information that can be validated by the server.
	 */
	public final static String PHASE_SUNRISE = "sunrise";

	/**
	 * Post sunrise phase when non-trademark holders are allowed to register
	 * domain names.
	 */
	public final static String PHASE_LANDRUSH = "landrush";

	/**
	 * Trademark claims phase as defined by Trademark Clearinghouse model of
	 * displaying a claims notice to clients for domain names that match
	 * trademarks.
	 */
	public final static String PHASE_CLAIMS = "claims";

	/**
	 * Post launch phase that is also referred to as "steady state".
	 */
	public final static String PHASE_OPEN = "open";

	/**
	 * A custom server launch phase that is defined using the name attribute.
	 */
	public final static String PHASE_CUSTOM = "custom";

	public final static String MODE_FCFS = "fcfs";

	public final static String MODE_PENDING_REGISTRATION = "pending-registration";

	public final static String MODE_PENDING_APPLICATION = "pending-application";

	private String type = null;

	private String mode = MODE_FCFS;

	private String name = null;

	private Date startDate = null;

	private Date endDate = null;

	public static List VALID_PHASES = new ArrayList();

	public static List VALID_MODES = new ArrayList();

	static {
		VALID_PHASES.add(PHASE_PRE_DELEGATION);
		VALID_PHASES.add(PHASE_SUNRISE);
		VALID_PHASES.add(PHASE_LANDRUSH);
		VALID_PHASES.add(PHASE_CLAIMS);
		VALID_PHASES.add(PHASE_PRE_LAUNCH);
		VALID_PHASES.add(PHASE_OPEN);
		VALID_PHASES.add(PHASE_CUSTOM);

		VALID_MODES.add(MODE_FCFS);
		VALID_MODES.add(MODE_PENDING_REGISTRATION);
		VALID_MODES.add(MODE_PENDING_APPLICATION);
	}

	public EPPRegistryPhase() {
		super();
	}

	public EPPRegistryPhase(String type, Date startDate, Date endDate) {
		this();
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.name = null;
	}

	public EPPRegistryPhase(String type, String name, Date startDate,
			Date endDate) {
		this(type, startDate, endDate);
		this.name = name;
	}

	public EPPRegistryPhase(String type, String name, String mode,
			Date startDate, Date endDate) {
		this(type, name, startDate, endDate);
		this.mode = mode;
	}

	public EPPRegistryPhase(String type, Date startDate) {
		this(type, startDate, null);
	}

	public EPPRegistryPhase(String type, String name, Date startDate) {
		this(type, name, startDate, null);
	}

	public EPPRegistryPhase(String type, String name, String mode,
			Date startDate) {
		this(type, name, mode, startDate, null);
	}

	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		}
		catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryPhase.encode: " + e);
		}
		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		EPPUtil.encodeTimeInstant(aDocument, root, startDate,
				EPPRegistryMapFactory.NS, EPPRegistryMapFactory.NS_PREFIX + ":"
						+ ELM_START_DATE);
		if (endDate != null) {
			EPPUtil.encodeTimeInstant(aDocument, root, endDate,
					EPPRegistryMapFactory.NS, EPPRegistryMapFactory.NS_PREFIX
							+ ":" + ELM_END_DATE);
		}
		root.setAttribute(ATTR_TYPE, type);
		if (mode == null || mode.trim().length() == 0) {
			mode = MODE_FCFS;
		}
		root.setAttribute(ATTR_MODE, mode);
		if (name != null && name.trim().length() > 0) {
			root.setAttribute(ATTR_NAME, name);
		}

		return root;
	}

	public void decode(Element aElement) throws EPPDecodeException {
		this.type = aElement.getAttribute(ATTR_TYPE);
		this.name = aElement.getAttribute(ATTR_NAME);
		this.mode = aElement.getAttribute(ATTR_MODE);
		if (mode == null) {
			mode = MODE_FCFS;
		}
		this.startDate = EPPUtil.decodeTimeInstant(aElement,
				EPPRegistryMapFactory.NS, ELM_START_DATE);
		this.endDate = EPPUtil.decodeTimeInstant(aElement,
				EPPRegistryMapFactory.NS, ELM_END_DATE);
	}

	public Object clone() throws CloneNotSupportedException {
		return (EPPRegistryPhase) super.clone();
	}

	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryPhase)) {
			return false;
		}

		EPPRegistryPhase theComp = (EPPRegistryPhase) aObject;
		if (!type.equals(theComp.type)) {
			cat.error("EPPRegistryPhase.equals(): type not equal");
			return false;
		}
		if (!((name == null) ? (theComp.name == null || theComp.name.trim()
				.length() == 0) : name.equals(theComp.name))) {
			cat.error("EPPRegistryPhase.equals(): name not equal");
			return false;
		}
		if (!startDate.equals(theComp.startDate)) {
			cat.error("EPPRegistryPhase.equals(): startDate not equal");
			return false;
		}
		if (!((endDate == null) ? (theComp.endDate == null) : endDate
				.equals(theComp.endDate))) {
			cat.error("EPPRegistryPhase.equals(): endDate not equal");
			return false;
		}

		return true;
	}

	void validateState() throws EPPCodecException {
		if (type == null || !VALID_PHASES.contains(type)) {
			throw new EPPCodecException(
					"type is required. Valid values are: pre-delegation/pre-launch/sunrise/landrush/claims/open/custom");
		}
		if (PHASE_CUSTOM.equals(type)
				&& (name == null || name.trim().length() == 0)) {
			throw new EPPCodecException(
					"when type is custom, name attribute is required");
		}
		if (startDate == null) {
			throw new EPPCodecException("startDate attribute is not set");
		}
		if (!VALID_MODES.contains(mode)) {
			throw new EPPCodecException(
					"invalid mode, valide values: fcfs/pending-registration/pending-application");
		}
	}

	public String toString() {
		return EPPUtil.toString(this);
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}

package com.verisign.epp.codec.registry;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

public class EPPRegistrySLA implements EPPCodecComponent {
	private static final long serialVersionUID = 776401602842928568L;

	private static Logger cat = Logger.getLogger(EPPRegistrySLA.class);

	public static final String COMMAND_DOMAIN_CREATE = "domain:create";
	public static final String COMMAND_DOMAIN_UPDATE = "domain:update";
	public static final String COMMAND_DOMAIN_DELETE = "domain:delete";
	public static final String COMMAND_CONTACT_CREATE = "contact:create";
	public static final String COMMAND_CONTACT_DELETE = "contact:delete";
	public static final String COMMAND_CONTACT_UPDATE = "contact:update";
	public static final String COMMAND_NAMESERVER_CREATE = "nameserver:create";
	public static final String COMMAND_NAMESERVER_DELETE = "nameserver:delete";
	public static final String COMMAND_NAMESERVER_UPDATE = "nameserver:update";
	public static final String COMMAND_DOMAIN_CHECK = "domain:check";
	public static final String COMMAND_SUGGESTION_INFO = "suggestion:info";
	public static final String COMMAND_LOGIN = "login";
	public static final String COMMAND_LOGOUT = "logout";

	public final static String ELM_NAME = "registry:sla";
	public final static String ATTR_COMMAND = "command";
	public final static String ATTR_TYPE = "type";
	public final static String ATTR_SUBTYPE = "subtype";
	public final static String ATTR_UNIT = "unit";

	private BigDecimal value = null;
	private String type = null;
	private String subtype = null;
	private String command = null;
	private String unit = null;

	public EPPRegistrySLA() {
		super();
	}

	public EPPRegistrySLA(String type, String subtype, String command,
			BigDecimal value, String unit) {
		super();
		this.value = value;
		this.unit = unit;
		this.type = type;
		this.subtype = subtype;
		this.command = command;
	}

	public EPPRegistrySLA(String type, String subtype, String command,
			double value, String unit) {
		this(type, subtype, command, new BigDecimal(value), unit);
	}

	public Element encode(Document aDocument) throws EPPEncodeException {
		if (type == null) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistrySLA.encode: type is required");
		}
		if (value == null) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistrySLA.encode: value is required");
		}
		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		Text currVal = aDocument.createTextNode(value.toString());
		root.appendChild(currVal);
		root.setAttribute(ATTR_TYPE, type);
		if (command != null && command.trim().length() > 0) {
			root.setAttribute(ATTR_COMMAND, command);
		}
		if (unit != null && unit.trim().length() > 0) {
			root.setAttribute(ATTR_UNIT, unit);
		}
		if (subtype != null && subtype.trim().length() > 0) {
			root.setAttribute(ATTR_SUBTYPE, subtype);
		}

		return root;
	}

	public void decode(Element aElement) throws EPPDecodeException {
		this.unit = aElement.getAttribute(ATTR_UNIT);
		this.command = aElement.getAttribute(ATTR_COMMAND);
		this.type = aElement.getAttribute(ATTR_TYPE);
		this.subtype = aElement.getAttribute(ATTR_SUBTYPE);
		this.value = new BigDecimal(aElement.getFirstChild().getNodeValue());
	}

	public Object clone() throws CloneNotSupportedException {
		return (EPPRegistrySLA) super.clone();
	}

	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistrySLA)) {
			return false;
		}

		EPPRegistrySLA theComp = (EPPRegistrySLA) aObject;

		if (!((value == null) ? (theComp.value == null) : value
				.equals(theComp.value))) {
			cat.error("EPPRegistrySLA.equals(): value not equal");
			return false;
		}
		if (!((command == null) ? (theComp.command == null || theComp.command
				.trim().length() == 0) : command.equals(theComp.command))) {
			cat.error("EPPRegistrySLA.equals(): command not equal");
			return false;
		}
		if (!((type == null) ? (theComp.type == null || theComp.type.trim()
				.length() == 0) : type.equals(theComp.type))) {
			cat.error("EPPRegistrySLA.equals(): type not equal");
			return false;
		}
		if (!((subtype == null) ? (theComp.subtype == null || theComp.subtype
				.trim().length() == 0) : subtype.equals(theComp.subtype))) {
			cat.error("EPPRegistrySLA.equals(): subtype not equal");
			return false;
		}
		if (!((unit == null) ? (theComp.unit == null || theComp.unit.trim()
				.length() == 0) : unit.equals(theComp.unit))) {
			cat.error("EPPRegistrySLA.equals(): unit not equal");
			return false;
		}

		return true;
	}

	public String toString() {
		return EPPUtil.toString(this);
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public class Unit {
		public static final String UNIT_MILLISECOND = "ms";
		public static final String UNIT_PERCENTAGE = "percent";
	}
}

package com.verisign.epp.codec.registry;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

public class EPPRegistryLanguageType implements EPPCodecComponent {
	private static final long serialVersionUID = 5827887024830764234L;

	final static String ELM_NAME = "registry:language";
	final static String ELM_TABLE_NAME = "registry:table";
	final static String ELM_VARIANT_STRATEGY_NAME = "registry:variantStrategy";
	final static String ATTR_CODE = "code";

	public final static List VALID_VARIANT_STRATEGIES = new ArrayList();
	public final static String VARIANT_STRATEGY_BLOCKED = "blocked";
	public final static String VARIANT_STRATEGY_RESTRICTED = "restricted";
	public final static String VARIANT_STRATEGY_OPEN = "open";

	static {
		VALID_VARIANT_STRATEGIES.add(VARIANT_STRATEGY_BLOCKED);
		VALID_VARIANT_STRATEGIES.add(VARIANT_STRATEGY_RESTRICTED);
		VALID_VARIANT_STRATEGIES.add(VARIANT_STRATEGY_OPEN);
	}

	private String table = null;
	private String strategy = null;
	private String code = null;

	public EPPRegistryLanguageType() {
	}

	public EPPRegistryLanguageType(String code, String table,
			String variantStrategy) {
		this.code = code;
		this.table = table;
		this.strategy = variantStrategy;
	}

	public Element encode(Document aDocument) throws EPPEncodeException {
		if (code == null || code.trim().length() == 0) {
			throw new EPPEncodeException(
					"Invalide state on EPPRegistryLanguageType.encode: "
							+ "attribute code is required.");
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		root.setAttribute(ATTR_CODE, code);
		if (table != null) {
			EPPUtil.encodeString(aDocument, root, table,
					EPPRegistryMapFactory.NS, ELM_TABLE_NAME);
		}
		if (strategy != null) {
			EPPUtil.encodeString(aDocument, root, strategy,
					EPPRegistryMapFactory.NS, ELM_VARIANT_STRATEGY_NAME);
		}

		return root;
	}

	public void decode(Element aElement) throws EPPDecodeException {
		this.code = aElement.getAttribute(ATTR_CODE);
		this.strategy = EPPUtil.decodeString(aElement,
				EPPRegistryMapFactory.NS, ELM_VARIANT_STRATEGY_NAME);
		this.table = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
				ELM_TABLE_NAME);
	}

	public Object clone() throws CloneNotSupportedException {
		return (EPPRegistryLanguageType) super.clone();
	}

	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryLanguageType)) {
			return false;
		}

		EPPRegistryLanguageType theComp = (EPPRegistryLanguageType) aObject;
		if (!((code == null) ? (theComp.code == null) : code
				.equals(theComp.code))) {
			return false;
		}
		if (!((table == null) ? (theComp.table == null) : table
				.equals(theComp.table))) {
			return false;
		}
		if (!((strategy == null) ? (theComp.strategy == null) : strategy
				.equals(theComp.strategy))) {
			return false;
		}

		return true;
	}

	public String toString() {
		return EPPUtil.toString(this);
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}

package com.verisign.epp.codec.registry;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

public class EPPRegistrySLAInfo implements EPPCodecComponent {
	private static final long serialVersionUID = -262589505904784625L;

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPRegistrySLAInfo.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	public final static String ELM_NAME = "registry:slaInfo";

	// EPPRegistrySLA
	private List slas = new ArrayList();

	public Element encode(Document aDocument) throws EPPEncodeException {
		if (slas == null || slas.size() == 0) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistrySLAInfo.encode: at leave one SLA item is required");
		}
		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		EPPUtil.encodeCompList(aDocument, root, slas);

		return root;
	}

	public void decode(Element aElement) throws EPPDecodeException {
		slas = EPPUtil.decodeCompList(aElement, EPPRegistryMapFactory.NS,
				EPPRegistrySLA.ELM_NAME, EPPRegistrySLA.class);
	}

	public Object clone() throws CloneNotSupportedException {
		EPPRegistrySLAInfo clone = (EPPRegistrySLAInfo) super.clone();

		if (slas != null) {
			clone.slas = (List) ((ArrayList) slas).clone();
		}

		return clone;
	}

	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistrySLAInfo)) {
			return false;
		}

		EPPRegistrySLAInfo theComp = (EPPRegistrySLAInfo) aObject;
		if (!((slas == null) ? (theComp.slas == null) : EPPUtil.equalLists(
				slas, theComp.slas))) {
			cat.error("EPPRegistrySLAInfo.equals(): slas not equal");
			return false;
		}

		return true;
	}

	public String toString() {
		return EPPUtil.toString(this);
	}

	public List getSlas() {
		return slas;
	}

	public void addSla(EPPRegistrySLA sla) {
		if (this.slas == null) {
			this.slas = new ArrayList();
		}
		this.slas.add(sla);
	}

	public void setSlas(List slas) {
		this.slas = slas;
	}
}

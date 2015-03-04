package com.verisign.epp.codec.registry;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

public class EPPRegistryServices implements EPPCodecComponent {
	private static final long serialVersionUID = 1632373332571396110L;

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPRegistryServices.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	public final static String ELM_NAME = "registry:services";

	// EPPRegistryObjURI
	private List objURIs = new ArrayList();
	EPPRegistryServicesExt extension;

	public EPPRegistryServices() {
	}

	public Element encode(Document aDocument) throws EPPEncodeException {
		if (objURIs == null || objURIs.size() == 0) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryServices.encode: objURIs is required.");
		}
		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		EPPUtil.encodeCompList(aDocument, root, objURIs);
		if (extension != null) {
			EPPUtil.encodeComp(aDocument, root, extension);
		}

		return root;
	}

	public void decode(Element aElement) throws EPPDecodeException {
		objURIs = EPPUtil.decodeCompList(aElement, EPPRegistryMapFactory.NS,
				EPPRegistryURI.ELM_OBJ_URI, EPPRegistryObjURI.class);
		extension = (EPPRegistryServicesExt) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryServicesExt.ELM_NAME,
				EPPRegistryServicesExt.class);
	}

	public Object clone() throws CloneNotSupportedException {
		EPPRegistryServices clone = (EPPRegistryServices) super.clone();

		if (objURIs != null) {
			clone.objURIs = (List) ((ArrayList) objURIs).clone();
		}
		if (extension != null) {
			clone.extension = (EPPRegistryServicesExt) extension.clone();
		}

		return clone;
	}

	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryServices)) {
			return false;
		}

		EPPRegistryServices theComp = (EPPRegistryServices) aObject;
		if (!((objURIs == null) ? (theComp.objURIs == null || theComp.objURIs
				.size() == 0) : EPPUtil.equalLists(objURIs, theComp.objURIs))) {
			cat.error("EPPRegistryServices.equals(): objURIs not equal");
			return false;
		}
		if (!((extension == null) ? (theComp.extension == null) : extension
				.equals(theComp.extension))) {
			cat.error("EPPRegistryServices.equals(): extension not equal");
			return false;
		}

		return true;
	}

	public String toString() {
		return EPPUtil.toString(this);
	}

	public List getObjURIs() {
		return objURIs;
	}

	public void setObjURIs(List objURIs) {
		this.objURIs = objURIs;
	}

	public void addObjURI(EPPRegistryObjURI uri) {
		if (this.objURIs == null) {
			this.objURIs = new ArrayList();
		}
		this.objURIs.add(uri);
	}

	public EPPRegistryServicesExt getExtension() {
		return extension;
	}

	public void setExtension(EPPRegistryServicesExt extension) {
		this.extension = extension;
	}

	public static abstract class EPPRegistryURI implements
			EPPCodecComponent {
		private static final long serialVersionUID = 7553465772991675424L;

		public final static String ELM_OBJ_URI = "registry:objURI";
		public final static String ELM_EXT_URI = "registry:extURI";
		public final static String ATTR_REQUIRED = "required";

		private Boolean required = null;
		private String uri = null;

		public EPPRegistryURI() {
			super();
		}

		public EPPRegistryURI(String uri, Boolean required) {
			super();
			this.uri = uri;
			this.required = required;
		}

		abstract public String getRootName();

		public Element encode(Document aDocument) throws EPPEncodeException {
			try {
				validateState();
			} catch (EPPCodecException e) {
				throw new EPPEncodeException("Invalid state on "
						+ this.getClass().getName() + ".encode: " + e);
			}
			Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
					getRootName());
			Text currVal = aDocument.createTextNode(uri);
			root.appendChild(currVal);
			root.setAttribute(ATTR_REQUIRED, required.toString());

			return root;
		}

		public void decode(Element aElement) throws EPPDecodeException {
			uri = aElement.getFirstChild().getNodeValue();
			required = new Boolean(EPPUtil.decodeBooleanAttr(aElement,
					ATTR_REQUIRED));
		}

		private void validateState() throws EPPCodecException {
			if (uri == null || uri.trim().length() == 0) {
				throw new EPPCodecException("uri attribute is not set");
			}
			if (required == null) {
				throw new EPPCodecException("required attribute is not set");
			}
		}

		public Object clone() throws CloneNotSupportedException {
			return (EPPRegistryURI) super.clone();
		}

		public boolean equals(Object aObject) {
			if (!(aObject instanceof EPPRegistryURI)) {
				return false;
			}

			EPPRegistryURI theComp = (EPPRegistryURI) aObject;

			if (!((uri == null) ? (theComp.uri == null) : uri
					.equals(theComp.uri))) {
				cat.error(this.getClass().getName()
						+ ".equals(): uri not equal");
				return false;
			}
			if (!((required == null) ? (theComp.required == null) : required
					.booleanValue() == theComp.required.booleanValue())) {
				cat.error(this.getClass().getName()
						+ "EPPRegistryExtension.equals(): \"required\" not equal");
				return false;
			}

			return true;
		}

		public String toString() {
			return EPPUtil.toString(this);
		}

		public Boolean getRequired() {
			return required;
		}

		public void setRequired(Boolean required) {
			this.required = required;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}
	}

	public static class EPPRegistryObjURI extends EPPRegistryURI {
		private static final long serialVersionUID = -4093552942649030852L;

		public EPPRegistryObjURI() {
			super();
		}

		public EPPRegistryObjURI(String uri, Boolean required) {
			super(uri, required);
		}

		public String getRootName() {
			return EPPRegistryURI.ELM_OBJ_URI;
		}

		public boolean equals(Object aObject) {
			return super.equals(aObject);
		}
	}
}

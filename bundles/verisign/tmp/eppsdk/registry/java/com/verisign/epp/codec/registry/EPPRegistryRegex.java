package com.verisign.epp.codec.registry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * <code>EPPRegistryRegex</code> represents a general regular expression that
 * includes both the expression and an optional explanation. Since this is a
 * generic regular expression <code>EPPCodecComponent</code>, used by multiple
 * parent <code>EPPCodecComponent</code>'s, the root element must be set using
 * the {@link #setRootName(String)}.
 */
public class EPPRegistryRegex implements EPPCodecComponent {
	private static final long serialVersionUID = 6061392945665281499L;

	/**
	 * Regular expression element label.
	 */
	public static final String ELM_EXPRESSION = "expression";

	/**
	 * Regular expression optional explanation element label.
	 */
	public static final String ELM_EXPLANATION = "explanation";

	/**
	 * Language attribute of the explanation element.
	 */
	public static final String ATTR_LANG = "lang";

	/**
	 * Root element label to use.
	 */
	private String rootName = null;

	/**
	 * Regular expression attribute value.
	 */
	private String expression = null;

	/**
	 * Regular expression explanation attribute value.
	 */
	private String explanation = null;

	/**
	 * Language of the explanation attribute value.
	 */
	private String lang = null;

	/**
	 * Default constructor for <code>EPPRegistryRegex</code>.
	 */
	public EPPRegistryRegex() {
		super();
	}

	/**
	 * Constructor that takes the required regular expression value.
	 * 
	 * @param aExpression
	 *            Regular expression value.
	 */
	public EPPRegistryRegex(String aExpression) {
		this();
		this.expression = aExpression;
	}

	/**
	 * Constructor that takes the required regular expression value along with
	 * the optional explanation.
	 * 
	 * @param aExpression
	 *            Regular expression value.
	 * @param aExplanation
	 *            Explanation of the regular expression.
	 */
	public EPPRegistryRegex(String aExpression, String aExplanation) {
		this(aExpression);
		this.explanation = aExplanation;
	}

	/**
	 * Constructor that takes the required regular expression value along with
	 * the optional explanation and the language of the explanation.
	 * 
	 * @param aExpression
	 *            Regular expression value.
	 * @param aExplanation
	 *            Explanation of the regular expression.
	 * @param aLang
	 *            Language of the explanation.
	 */
	public EPPRegistryRegex(String aExpression, String aExplanation,
			String aLang) {
		this(aExpression);
		this.explanation = aExplanation;
		this.lang = aLang;
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPRegistryRegex</code> instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         <code>EPPRegistryRegex</code> instance.
	 * 
	 * @exception EPPEncodeException
	 *                Unable to encode <code>EPPRegistryRegex</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		// Required attributes set?
		if (rootName == null || rootName.trim().length() == 0) {
			throw new EPPEncodeException("rootName is not set");
		}
		if (expression == null || expression.trim().length() == 0) {
			throw new EPPEncodeException(
					"expression attribute is not set in EPPRegistryRegex.encode");
		}
				
		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				EPPRegistryMapFactory.NS_PREFIX + ":" + getRootName());
		EPPUtil.encodeString(aDocument, root, this.expression,
				EPPRegistryMapFactory.NS, EPPRegistryMapFactory.NS_PREFIX + ":"
						+ ELM_EXPRESSION);
		if (this.explanation != null && this.explanation.trim().length() > 0) {
			if (this.lang == null || this.lang.trim().length() == 0) {
				this.lang = "en";
			}
			Element currElm = aDocument.createElementNS(
					EPPRegistryMapFactory.NS, EPPRegistryMapFactory.NS_PREFIX
							+ ":" + ELM_EXPLANATION);
			Text currVal = aDocument.createTextNode(explanation);
			currElm.setAttribute(ATTR_LANG, lang);
			currElm.appendChild(currVal);
			root.appendChild(currElm);
		}

		return root;
	}

	/**
	 * Decode the <code>EPPRegistryRegex</code> attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode <code>EPPRegistryRegex</code> from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		this.rootName = aElement.getLocalName();

		this.expression = EPPUtil.decodeString(aElement,
				EPPRegistryMapFactory.NS, ELM_EXPRESSION);
		this.explanation = EPPUtil.decodeString(aElement,
				EPPRegistryMapFactory.NS, ELM_EXPLANATION);
		if (this.explanation != null) {
			Element theElm = EPPUtil.getElementByTagNameNS(aElement,
					EPPRegistryMapFactory.NS, ELM_EXPLANATION);
			this.lang = theElm.getAttribute(ATTR_LANG);
			if (this.lang == null || this.lang.trim().length() == 0) {
				this.lang = "en";
			}
		}

		if (this.expression == null || this.expression.trim().length() == 0) {
			throw new EPPDecodeException(
					"expression attribute is not set in EPPRegistryRegex.decode");
		}
	}

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful;
	 *         <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	/**
	 * Clone <code>EPPRegistryRegex</code>.
	 * 
	 * @return clone of <code>EPPRegistryRegex</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		return (EPPRegistryRegex) super.clone();
	}

	/**
	 * implements a deep <code>EPPRegistryRegex</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryRegex</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryRegex)) {
			return false;
		}

		EPPRegistryRegex theComp = (EPPRegistryRegex) aObject;
		if (!((this.rootName == null) ? (theComp.rootName == null)
				: this.rootName.equals(theComp.rootName))) {
			return false;
		}
		if (!((this.expression == null) ? (theComp.expression == null)
				: this.expression.equals(theComp.expression))) {
			return false;
		}
		if (!((this.explanation == null) ? (theComp.explanation == null)
				: this.explanation.equals(theComp.explanation))) {
			return false;
		}
		if (this.explanation != null) {
			if (!((this.lang == null) ? (theComp.lang == null) : this.lang
					.equals(theComp.lang))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Gets the regular expression value.
	 * 
	 * @return Regular expression value if defined; <code>null</code> otherwise.
	 */
	public String getExpression() {
		return this.expression;
	}

	/**
	 * Sets the regular expression value.
	 * 
	 * @param aExpression
	 *            Regular expression value.
	 */
	public void setExpression(String aExpression) {
		this.expression = aExpression;
	}

	/**
	 * Gets the explanation of the regular expression.
	 * 
	 * @return Regular expression explanation if defined; <code>null</code>
	 *         otherwise.
	 */
	public String getExplanation() {
		return this.explanation;
	}

	/**
	 * Sets the explanation of the regular expression.
	 * 
	 * @param aExplanation
	 *            Regular expression explanation.
	 */
	public void setExplanation(String aExplanation) {
		this.explanation = aExplanation;
	}

	/**
	 * Gets the root element label (local name).
	 * 
	 * @return Root element label if defined; <code>null</code> otherwise.
	 */
	String getRootName() {
		return this.rootName;
	}

	/**
	 * Sets the root element label (local name).  If 
	 * the element label includes a XML namespace prefix, the 
	 * namespace prefix is trimmed.  
	 * 
	 * @param aRootName
	 *            Root element label.
	 */
	void setRootName(String aRootName) {
		if (aRootName != null) {
			int namespaceSepIndex = aRootName.indexOf(':');

			if (namespaceSepIndex != -1
					&& aRootName.length() > namespaceSepIndex) {
				aRootName = aRootName.substring(namespaceSepIndex + 1);
			}
		}
		this.rootName = aRootName;
	}

	/**
	 * Gets the language of the explanation.
	 * 
	 * @return Language of the explanation if defined; <code>null</code>
	 *         otherwise.
	 */
	public String getLang() {
		return this.lang;
	}

	/**
	 * Sets the language of the explanation.
	 * 
	 * @param aLang
	 *            Language of the explanation.
	 */
	public void setLang(String aLang) {
		this.lang = aLang;
	}
}

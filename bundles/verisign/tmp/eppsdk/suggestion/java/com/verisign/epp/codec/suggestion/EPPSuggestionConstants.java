/*******************************************************************************
 * 
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced or disclosed without the
 * written approval of the General Manager of VeriSign Global Registry Services.
 * 
 * PRIVILEDGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION REGISTRY
 * SENSITIVE INFORMATION
 * 
 * Copyright (c) 2008 VeriSign, Inc. All rights reserved.
 * 
 ******************************************************************************/

package com.verisign.epp.codec.suggestion;

/**
 * Title: EPPSuggestionConstants Description: This interface defines constants
 * to be used when processing the language and key elements from the EPP
 * Suggestion INFO command. It also includes constants which identify the
 * expected values for the <code>language</code> element. Copyright: Copyright
 * (c) 2008 Company: VeriSign
 * 
 * @author psitowitz
 * @version 1.0, Oct 21, 2008
 */
public interface EPPSuggestionConstants {
	/**
	 * EPP Suggestion <code>language</code> element.
	 */
	public static final String ELM_LANGUAGE = "suggestion:language";

	/**
	 * EPP Suggestion <code>key</code> element.
	 */
	public static final String ELM_KEY = "suggestion:key";

	/**
	 * English language Constant as represented in Name Suggestions.
	 */
	public static final String ENGLISH_NAME = "ENGLISH";

	/**
	 * English language Constant as represented in ISO 639-2/B Code used with
	 * EPP.
	 */
	public static final String ENGLISH_CODE = "ENG";

	/**
	 * Spanish language Constant as represented in Name Suggestions.
	 */
	public static final String SPANISH_NAME = "SPANISH";

	/**
	 * Spanish language Constant used with EPP.
	 */
	public static final String SPANISH_CODE = "ESP";

	/**
	 * Portuguese language Constant as represented in Name Suggestions.
	 */
	public static final String PORTUGUESE_NAME = "PORTUGUESE";

	/**
	 * Portuguese language Constant as represented in ISO 639-2/B Code used with
	 * EPP.
	 */
	public static final String PORTUGUESE_CODE = "POR";

	/**
	 * German language Constant as represented in Name Suggestions.
	 */
	public static final String GERMAN_NAME = "GERMAN";

	/**
	 * German language Constant as represented in ISO 639-2/B Code used with
	 * EPP.
	 */
	public static final String GERMAN_CODE = "GER";

	/**
	 * French language Constant as represented in Name Suggestions.
	 */
	public static final String FRENCH_NAME = "FRENCH";

	/**
	 * French language Constant as represented in ISO 639-2/B Code used with
	 * EPP.
	 */
	public static final String FRENCH_CODE = "FRE";

	/**
	 * Turkish language Constant as represented in Name Suggestions.
	 */
	public static final String TURKISH_NAME = "TURKISH";

	/**
	 * Turkish language Constant as represented in ISO 639-2/B Code used with
	 * EPP.
	 */
	public static final String TURKISH_CODE = "TUR";
	
	/**
	 * Chinese language Constant as represented in ISO 639-2/B Code used with
	 * EPP.
	 */
	public static final String CHINESE_CODE = "CHI";
	
	/**
	 * Chinese language Constant as represented in Name Suggestions.
	 */
	public static final String CHINESE_NAME = "CHINESE";
	
	/**
	 * Korean language Constant as represented in ISO 639-2/B Code used with
	 * EPP.
	 */
	public static final String KOREAN_CODE = "KOR";
	
	/**
	 * Korean language Constant as represented in Name Suggestions.
	 */
	public static final String KOREAN_NAME = "KOREAN";

	/**
	 * Default the language to English for backward compatibility.
	 */
	public static final String DEFAULT_LANGUAGE = ENGLISH_CODE;

} // END interface EPPSuggestionConstants

/*********************************************************************
 *                                                                   *
 * The information in this document is proprietary to VeriSign, Inc. *
 * It may not be used, reproduced or disclosed without the written   *
 * approval of VeriSign.                                             *
 *                                                                   *
 * VERISIGN PROPRIETARY & CONFIDENTIAL INFORMATION                   *
 *                                                                   *
 *                                                                   *
 * Copyright (c) 2012 VeriSign, Inc.  All rights reserved.           *
 *                                                                   *
 ********************************************************************/

package com.verisign.epp.serverstub;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.registry.EPPRegistryCheckCmd;
import com.verisign.epp.codec.registry.EPPRegistryCheckResp;
import com.verisign.epp.codec.registry.EPPRegistryCheckResult;
import com.verisign.epp.codec.registry.EPPRegistryContact;
import com.verisign.epp.codec.registry.EPPRegistryContactAddress;
import com.verisign.epp.codec.registry.EPPRegistryContactCity;
import com.verisign.epp.codec.registry.EPPRegistryContactName;
import com.verisign.epp.codec.registry.EPPRegistryContactOrg;
import com.verisign.epp.codec.registry.EPPRegistryContactPostalCode;
import com.verisign.epp.codec.registry.EPPRegistryContactStateProvince;
import com.verisign.epp.codec.registry.EPPRegistryContactStreet;
import com.verisign.epp.codec.registry.EPPRegistryCreateCmd;
import com.verisign.epp.codec.registry.EPPRegistryCreateResp;
import com.verisign.epp.codec.registry.EPPRegistryCustomData;
import com.verisign.epp.codec.registry.EPPRegistryDNSSEC;
import com.verisign.epp.codec.registry.EPPRegistryDS;
import com.verisign.epp.codec.registry.EPPRegistryDeleteCmd;
import com.verisign.epp.codec.registry.EPPRegistryDomain;
import com.verisign.epp.codec.registry.EPPRegistryDomainContact;
import com.verisign.epp.codec.registry.EPPRegistryDomainHostLimit;
import com.verisign.epp.codec.registry.EPPRegistryDomainNSLimit;
import com.verisign.epp.codec.registry.EPPRegistryDomainName;
import com.verisign.epp.codec.registry.EPPRegistryDomainPeriod;
import com.verisign.epp.codec.registry.EPPRegistryExternalHost;
import com.verisign.epp.codec.registry.EPPRegistryFields;
import com.verisign.epp.codec.registry.EPPRegistryRelated;
import com.verisign.epp.codec.registry.EPPRegistryGracePeriod;
import com.verisign.epp.codec.registry.EPPRegistryHost;
import com.verisign.epp.codec.registry.EPPRegistryIDN;
import com.verisign.epp.codec.registry.EPPRegistryInfoCmd;
import com.verisign.epp.codec.registry.EPPRegistryInfoResp;
import com.verisign.epp.codec.registry.EPPRegistryInternalHost;
import com.verisign.epp.codec.registry.EPPRegistryKeyValue;
import com.verisign.epp.codec.registry.EPPRegistryLanguageType;
import com.verisign.epp.codec.registry.EPPRegistryMaxSig;
import com.verisign.epp.codec.registry.EPPRegistryMinMaxLength;
import com.verisign.epp.codec.registry.EPPRegistryPendingDeletePeriodType;
import com.verisign.epp.codec.registry.EPPRegistryPendingRestorePeriodType;
import com.verisign.epp.codec.registry.EPPRegistryPhase;
import com.verisign.epp.codec.registry.EPPRegistryPostal;
import com.verisign.epp.codec.registry.EPPRegistryRGP;
import com.verisign.epp.codec.registry.EPPRegistryRedemptionPeriodType;
import com.verisign.epp.codec.registry.EPPRegistryRegex;
import com.verisign.epp.codec.registry.EPPRegistryReservedNames;
import com.verisign.epp.codec.registry.EPPRegistrySLA;
import com.verisign.epp.codec.registry.EPPRegistrySLAInfo;
import com.verisign.epp.codec.registry.EPPRegistryServices;
import com.verisign.epp.codec.registry.EPPRegistryServicesExt;
import com.verisign.epp.codec.registry.EPPRegistrySupportedStatus;
import com.verisign.epp.codec.registry.EPPRegistryServices.EPPRegistryObjURI;
import com.verisign.epp.codec.registry.EPPRegistryServicesExt.EPPRegistryExtURI;
import com.verisign.epp.codec.registry.EPPRegistrySupportedStatus.Status;
import com.verisign.epp.codec.registry.EPPRegistryTransferHoldPeriodType;
import com.verisign.epp.codec.registry.EPPRegistryUpdateCmd;
import com.verisign.epp.codec.registry.EPPRegistryZone;
import com.verisign.epp.codec.registry.EPPRegistryZoneInfo;
import com.verisign.epp.codec.registry.EPPRegistryZoneList;
import com.verisign.epp.codec.registry.EPPRegistryZoneMember;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;
import com.verisign.epp.framework.EPPRegistryHandler;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPEnvSingle;

public class RegistryHandler extends EPPRegistryHandler {
	private static final String svrTransId = "54322-XYZ";

	// EPPRegistryZoneInfo
	private static List availableTlds = new ArrayList();

	static {
		int len = 10;
		// iterations Property
		String num = EPPEnvSingle.getProperty("Ini.Zone.Count", "10");

		if (num != null) {
			len = Integer.parseInt(num);
		}
		for (int i = 0; i < len; i++) {
			EPPRegistryZoneInfo zoneInfo = new EPPRegistryZoneInfo("samplezone"
					+ i, "client" + i, new Date());
			zoneInfo.setLastUpdatedBy("client" + i);
			zoneInfo.setLastUpdatedDate(new Date());

			zoneInfo.setGroup("g" + i);
			zoneInfo.setSubProduct("dotSamplezone" + i);

			EPPRegistryRelated related = new EPPRegistryRelated();
			
			EPPRegistryFields fields = new EPPRegistryFields();
			fields.setType(EPPRegistryFields.TYPE_SYNC);
			fields.addField("clID");
			fields.addField("registrant");
			fields.addField("ns");
			related.setFields(fields);

			related.addMember(new EPPRegistryZoneMember("EXAMPLE",  EPPRegistryZoneMember.TYPE_EQUAL));
			related.addMember(new EPPRegistryZoneMember("EXAMPLE2",  EPPRegistryZoneMember.TYPE_EQUAL));
			related.addMember(new EPPRegistryZoneMember("EXAMPLE3",  EPPRegistryZoneMember.TYPE_EQUAL));

			zoneInfo.setRelated(related);

			EPPRegistryPhase phase = new EPPRegistryPhase(
					EPPRegistryPhase.PHASE_SUNRISE, new Date(), new Date());
			phase.setMode(EPPRegistryPhase.MODE_PENDING_APPLICATION);
			zoneInfo.addPhase(phase);
			phase = new EPPRegistryPhase(EPPRegistryPhase.PHASE_PRE_DELEGATION,
					new Date(), new Date());
			phase.setMode(EPPRegistryPhase.MODE_PENDING_REGISTRATION);
			zoneInfo.addPhase(phase);
			zoneInfo.addPhase(new EPPRegistryPhase(
					EPPRegistryPhase.PHASE_LANDRUSH, new Date(), new Date()));
			zoneInfo.addPhase(new EPPRegistryPhase(
					EPPRegistryPhase.PHASE_PRE_DELEGATION, new Date(),
					new Date()));
			zoneInfo.addPhase(new EPPRegistryPhase(
					EPPRegistryPhase.PHASE_CLAIMS, new Date(), new Date()));
			zoneInfo.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_OPEN,
					new Date()));
			zoneInfo.addPhase(new EPPRegistryPhase(
					EPPRegistryPhase.PHASE_CUSTOM, "userDefined", new Date()));

			EPPRegistryServices services = new EPPRegistryServices();
			services.addObjURI(new EPPRegistryObjURI(
					"http://www.verisign.com/epp/rgp-poll-1.0", Boolean.TRUE));
			services.addObjURI(new EPPRegistryObjURI(
					"urn:ietf:params:xml:ns:host-1.0", Boolean.TRUE));
			services.addObjURI(new EPPRegistryObjURI(
					"urn:ietf:params:xml:ns:contact-1.0", Boolean.TRUE));
			services.addObjURI(new EPPRegistryObjURI(
					"urn:ietf:params:xml:ns:domain-1.0", Boolean.TRUE));
			services.addObjURI(new EPPRegistryObjURI(
					"http://www.verisign.com/epp/lowbalance-poll-1.0",
					Boolean.FALSE));
			EPPRegistryServicesExt svcExt = new EPPRegistryServicesExt();
			services.setExtension(svcExt);
			svcExt.addExtURI(new EPPRegistryExtURI(
					"http://www.verisign-grs.com/epp/namestoreExt-1.1",
					Boolean.TRUE));
			svcExt.addExtURI(new EPPRegistryExtURI(
					"urn:ietf:params:xml:ns:rgp-1.0", Boolean.TRUE));
			svcExt.addExtURI(new EPPRegistryExtURI(
					"http://www.verisign.com/epp/sync-1.0", Boolean.TRUE));
			svcExt.addExtURI(new EPPRegistryExtURI(
					"http://www.verisign.com/epp/idnLang-1.0", Boolean.TRUE));
			svcExt.addExtURI(new EPPRegistryExtURI(
					"http://www.verisign.com/epp/jobsContact-1.0", Boolean.TRUE));
			svcExt.addExtURI(new EPPRegistryExtURI(
					"http://www.verisign.com/epp/premiumdomain-1.0",
					Boolean.TRUE));
			svcExt.addExtURI(new EPPRegistryExtURI(
					"urn:ietf:params:xml:ns:secDNS-1.1", Boolean.FALSE));
			zoneInfo.setServices(services);

			EPPRegistrySLAInfo slaInfo = new EPPRegistrySLAInfo();
			slaInfo.addSla(new EPPRegistrySLA("response", "ext", "create", 500,
					"ms"));
			slaInfo.addSla(new EPPRegistrySLA("availability", null, "create",
					99.9, "percent"));
			zoneInfo.setSlaInfo(slaInfo);

			zoneInfo.setCreatedBy("crId");
			zoneInfo.setCreatedDate(new Date());
			zoneInfo.setLastUpdatedBy("upId");
			zoneInfo.setLastUpdatedDate(new Date());

			zoneInfo.setDomain(buildInfoDomain(i));

			zoneInfo.setHost(buildInfoHost(i));

			zoneInfo.setContact(buildContact(i));
			availableTlds.add(zoneInfo);
		}
	}

	private static Logger cat = Logger.getLogger(RegistryHandler.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	protected EPPEventResponse doRegistryCreate(EPPEvent aEvent, Object aData) {
		cat.debug("doRegistryCreate: enter");
		EPPRegistryCreateCmd message = (EPPRegistryCreateCmd) aEvent
				.getMessage();

		// Encode EPPRegistryCreate Response
		EPPRegistryCreateResp theResponse;

		EPPTransId respTransId = new EPPTransId(message.getTransId(),
				svrTransId);
		String name = message.getZone().getName();
		boolean tldExist = false;
		theResponse = new EPPRegistryCreateResp(respTransId, name);
		Iterator phaseIterator = null;
		for (Iterator it = availableTlds.iterator(); it.hasNext();) {
			EPPRegistryZoneInfo zoneInfo = (EPPRegistryZoneInfo) it.next();
			if (zoneInfo.getName().equals(name)) {
				tldExist = true;
				theResponse.setResult(EPPResult.OBJECT_EXISTS,
						"TLD name exists");
				phaseIterator = zoneInfo.getPhases().iterator();
				break;
			}
		}
		if (!tldExist) {
			EPPRegistryZoneInfo zoneInfo = new EPPRegistryZoneInfo();

			zoneInfo.setName(message.getZone().getName());
			zoneInfo.setPhases(message.getZone().getPhases());
			zoneInfo.setServices(message.getZone().getServices());
			zoneInfo.setDomain(message.getZone().getDomain());
			zoneInfo.setHost(message.getZone().getHost());
			zoneInfo.setContact(message.getZone().getContact());
			zoneInfo.setCreatedDate(new Date());
			zoneInfo.setLastUpdatedBy(null);
			zoneInfo.setLastUpdatedDate(null);

			availableTlds.add(zoneInfo);
			phaseIterator = message.getZone().getPhases().iterator();
		}

		while (phaseIterator.hasNext()) {
			EPPRegistryPhase phase = (EPPRegistryPhase) phaseIterator.next();
			if (EPPRegistryPhase.PHASE_SUNRISE.equals(phase.getType())) {
				theResponse.setCreateDate(phase.getStartDate());
			}
			if (EPPRegistryPhase.PHASE_OPEN.equals(phase.getType())) {
				if (phase.getEndDate() != null) {
				}
			}
		}

		cat.debug("doRegistryCreate: exit");
		return new EPPEventResponse(theResponse);
	}

	protected EPPEventResponse doRegistryUpdate(EPPEvent aEvent, Object aData) {
		cat.debug("doRegistryUpdate: enter");
		EPPRegistryUpdateCmd message = (EPPRegistryUpdateCmd) aEvent
				.getMessage();

		// Encode EPPRegistryUpdate Response
		EPPResponse theResponse;

		EPPTransId respTransId = new EPPTransId(message.getTransId(),
				svrTransId);
		String name = message.getZone().getName();
		boolean tldExist = false;
		theResponse = new EPPResponse(respTransId);
		EPPRegistryZoneInfo zoneInfo = null;
		for (Iterator it = availableTlds.iterator(); it.hasNext();) {
			zoneInfo = (EPPRegistryZoneInfo) it.next();
			if (zoneInfo.getName().equals(name)) {
				tldExist = true;
				it.remove();
				break;
			}
		}
		if (tldExist) {
			EPPRegistryZoneInfo oldTld = zoneInfo;
			zoneInfo = new EPPRegistryZoneInfo();

			zoneInfo.setName(message.getZone().getName());
			zoneInfo.setPhases(message.getZone().getPhases());
			zoneInfo.setServices(message.getZone().getServices());
			zoneInfo.setDomain(message.getZone().getDomain());
			zoneInfo.setHost(message.getZone().getHost());
			zoneInfo.setContact(message.getZone().getContact());
			zoneInfo.setCreatedDate(oldTld.getCreatedDate());
			zoneInfo.setLastUpdatedDate(new Date());
			zoneInfo.setCreatedBy(oldTld.getCreatedBy());

			availableTlds.add(zoneInfo);
			theResponse.setResult(EPPResult.SUCCESS);
		} else {
			theResponse.setResult(EPPResult.OBJECT_DOES_NOT_EXIST, "Tld "
					+ message.getZone().getName()
					+ " does not exist. Please create it first");
		}

		cat.debug("doRegistryUpdate: exit");
		return new EPPEventResponse(theResponse);
	}

	protected EPPEventResponse doRegistryCheck(EPPEvent aEvent, Object aData) {
		EPPRegistryCheckCmd message = (EPPRegistryCheckCmd) aEvent.getMessage();

		// Encode EPPRegistryInfo Response
		EPPRegistryCheckResp theResponse;

		EPPTransId respTransId = new EPPTransId(message.getTransId(),
				svrTransId);
		List names = message.getNames();
		List results = new ArrayList();
		outer: for (Iterator it = names.iterator(); it.hasNext();) {
			String name = (String) it.next();
			EPPRegistryCheckResult result = null;
			for (Iterator itt = availableTlds.iterator(); itt.hasNext();) {
				EPPRegistryZoneInfo zoneInfo = (EPPRegistryZoneInfo) itt.next();
				if (zoneInfo.getName().equals(name)) {
					result = new EPPRegistryCheckResult(name, Boolean.FALSE);
					result.setReason("Already taken");
					results.add(result);
					continue outer;
				}
			}
			result = new EPPRegistryCheckResult(name, Boolean.TRUE);
			result.setReason("Available");
			results.add(result);
		}

		if (results.size() == 1) {
			theResponse = new EPPRegistryCheckResp(respTransId,
					(EPPRegistryCheckResult) results.get(0));
		} else {
			theResponse = new EPPRegistryCheckResp(respTransId, results);
		}

		return new EPPEventResponse(theResponse);
	}

	protected EPPEventResponse doRegistryInfo(EPPEvent aEvent, Object aData) {
		cat.debug("doRegistryInfo ...");
		EPPRegistryInfoCmd message = (EPPRegistryInfoCmd) aEvent.getMessage();

		// Encode EPPRegistryInfo Response
		EPPResponse theResponse;

		EPPTransId respTransId = new EPPTransId(message.getTransId(),
				svrTransId);
		boolean all = message.isAll();
		if (all) {
			EPPRegistryZoneList zoneList = new EPPRegistryZoneList();
			for (Iterator it = availableTlds.iterator(); it.hasNext();) {
				EPPRegistryZoneInfo info = (EPPRegistryZoneInfo) it.next();
				EPPRegistryZone zone = new EPPRegistryZone(info.getName(),
						info.getCreatedDate(), info.getLastUpdatedDate());
				zoneList.addZone(zone);
			}
			theResponse = new EPPRegistryInfoResp(respTransId, zoneList);
		} else {
			EPPRegistryZoneInfo zoneInfo = null;
			for (Iterator it = availableTlds.iterator(); it.hasNext();) {
				EPPRegistryZoneInfo ti = (EPPRegistryZoneInfo) it.next();
				if (ti.getName().equalsIgnoreCase(message.getName())) {
					zoneInfo = ti;
					zoneInfo.setGroup("1");
					if (zoneInfo.getName().length() == 0) {
						zoneInfo.setSubProduct("dot");
					} else if (zoneInfo.getName().length() == 1) {
						zoneInfo.setSubProduct("dot"
								+ zoneInfo.getName().substring(0, 1)
										.toUpperCase());
					} else {
						zoneInfo.setSubProduct("dot"
								+ zoneInfo.getName().substring(0, 1)
										.toUpperCase()
								+ zoneInfo.getName().substring(1));
					}
					break;
				}
			}
			if (zoneInfo != null) {
				if ("com".equalsIgnoreCase(zoneInfo.getName())) {
					if (zoneInfo.getRelated() == null) {
						EPPRegistryRelated related = new EPPRegistryRelated();
						
						EPPRegistryFields fields = new EPPRegistryFields();
						fields.setType(EPPRegistryFields.TYPE_SYNC);
						fields.addField("clID");
						fields.addField("registrant");
						fields.addField("ns");
						related.setFields(fields);

						related.addMember(new EPPRegistryZoneMember("com",  EPPRegistryZoneMember.TYPE_EQUAL));
						related.addMember(new EPPRegistryZoneMember("com2",  EPPRegistryZoneMember.TYPE_EQUAL));
						related.addMember(new EPPRegistryZoneMember("com3",  EPPRegistryZoneMember.TYPE_EQUAL));

						zoneInfo.setRelated(related);
					}
				}
				theResponse = new EPPRegistryInfoResp(respTransId, zoneInfo);

				theResponse.setResult(EPPResult.SUCCESS);
			} else {
				cat.warn("tld does not exist: " + message.getName());
				theResponse = new EPPResponse();
				theResponse.setResult(EPPResult.OBJECT_DOES_NOT_EXIST);
			}
		}

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Registry Delete command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPRegistryHandler</code>
	 * 
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doRegistryDelete(EPPEvent aEvent, Object aData) {
		EPPRegistryDeleteCmd theMessage = (EPPRegistryDeleteCmd) aEvent
				.getMessage();

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId = new EPPTransId(theMessage.getTransId(), svrTransId);

		// Create Delete Response (Standard EPPResponse)
		EPPResponse theResponse = new EPPResponse(transId);

		String name = theMessage.getName();
		boolean deleted = false;
		for (Iterator it = availableTlds.iterator(); it.hasNext();) {
			EPPRegistryZoneInfo zoneInfo = (EPPRegistryZoneInfo) it.next();
			if (zoneInfo.getName().equals(name)) {
				it.remove();
				theResponse.setResult(EPPResult.SUCCESS);
				deleted = true;
				break;
			}
		}
		if (!deleted) {
			theResponse.setResult(EPPResult.OBJECT_DOES_NOT_EXIST,
					"Requested tld \"" + name + "\" does not exist");
		}

		return new EPPEventResponse(theResponse);
	}

	private static EPPRegistryDomain buildInfoDomain(int seq) {
		EPPRegistryDomain domain = new EPPRegistryDomain();

		List domainNames = new ArrayList();
		EPPRegistryDomainName domainName = new EPPRegistryDomainName();
		domainName.setLevel(new Integer(2));
		domainName.setMinLength(new Integer(5));
		domainName.setMaxLength(new Integer(50));
		domainName.setAlphaNumStart(new Boolean(true));
		domainName.setAlphaNumEnd(new Boolean(false));
		domainName.setOnlyDnsChars(new Boolean(true));

		List regex = new ArrayList();
		EPPRegistryRegex r = new EPPRegistryRegex("^\\w+.*$", "test regex");
		regex.add(r);

		r = new EPPRegistryRegex("^\\d+.*$");
		regex.add(r);
		domainName.setRegex(regex);

		EPPRegistryReservedNames reservedNames = new EPPRegistryReservedNames();
		List rNames = new ArrayList();
		reservedNames.setReservedNames(rNames);
		rNames.add("reserved" + seq + "1");
		rNames.add("reserved" + seq + "2");
		// reservedNames.setReservedNameURI("http://example.com/reservedNames");

		domainName.setReservedNames(reservedNames);
		domainNames.add(domainName);

		try {
			domainName = (EPPRegistryDomainName) domainName.clone();
			domainName.setLevel(new Integer(3));
			domainName.getReservedNames().setReservedNames(new ArrayList());
			domainName.getReservedNames().setReservedNameURI(
					"http://testrn.vrsn.com");
			domainNames.add(domainName);
		} catch (CloneNotSupportedException e) {
			// Swallow. This should not happen
		}

		domain.setDomainNames(domainNames);

		EPPRegistryIDN idn = new EPPRegistryIDN();
		idn.setIdnVersion("1.1");
		idn.setIdnaVersion("2008");
		idn.setUnicodeVersion("6.0");
		idn.addLanguage(new EPPRegistryLanguageType("CHI",
				"http://www.iana.org/domains/idn-tables/tables/com_zh_1.1.txt",
				EPPRegistryLanguageType.VARIANT_STRATEGY_RESTRICTED));
		idn.addLanguage(new EPPRegistryLanguageType(
				"LATN",
				"http://www.iana.org/domains/idn-tables/tables/eu_latn_1.0.html",
				EPPRegistryLanguageType.VARIANT_STRATEGY_BLOCKED));
		idn.setCommingleAllowed(Boolean.TRUE);
		domain.setIdn(idn);

		domain.setPremiumSupport(new Boolean(true));
		domain.setContactsSupported(new Boolean(true));

		domain.addContact(new EPPRegistryDomainContact(
				EPPRegistryDomainContact.TYPE_ADMIN, 1, 4));
		domain.addContact(new EPPRegistryDomainContact(
				EPPRegistryDomainContact.TYPE_BILLING, 2, 5));
		domain.addContact(new EPPRegistryDomainContact(
				EPPRegistryDomainContact.TYPE_TECH, 3, 6));

		domain.setNameServerLimit(new EPPRegistryDomainNSLimit(1, 16));

		domain.setChildHostLimit(new EPPRegistryDomainHostLimit(2, 32));

		// domain.addPeriod(new EPPRegistryDomainPeriod("create",
		// Boolean.TRUE));
		domain.addPeriod(new EPPRegistryDomainPeriod("create", 1, "y", 10, "y",
				2, "y"));
		domain.addPeriod(new EPPRegistryDomainPeriod("renew", 1, "y", 10, "y",
				2, "y"));
		domain.addPeriod(new EPPRegistryDomainPeriod("transfer", 1, "y", 8,
				"y", 2, "y"));

		domain.setTransferHoldPeriod(new EPPRegistryTransferHoldPeriodType(1,
				"y"));

		domain.addGracePeriod(new EPPRegistryGracePeriod("create", 1, "d"));
		domain.addGracePeriod(new EPPRegistryGracePeriod("renew", 2, "m"));
		domain.addGracePeriod(new EPPRegistryGracePeriod("transfer", 3, "h"));

		EPPRegistryRGP rgp = new EPPRegistryRGP();
		rgp.setPendingDeletePeriod(new EPPRegistryPendingDeletePeriodType(1,
				"m"));
		rgp.setRedemptionPeriod(new EPPRegistryRedemptionPeriodType(1, "m"));
		rgp.setPendingRestorePeriod(new EPPRegistryPendingRestorePeriodType(1,
				"m"));
		domain.setRgp(rgp);

		EPPRegistryDNSSEC dnssec = new EPPRegistryDNSSEC();
		EPPRegistryDS ds = new EPPRegistryDS(1, 3, null, null);
		ds.addAlgorithm("algDS" + seq + "1");
		ds.addAlgorithm("algDS" + seq + "2");
		ds.addDigestType("digest" + seq + "1");
		ds.addDigestType("digest" + seq + "2");
		dnssec.setDs(ds);

		// EPPRegistryKey key = new EPPRegistryKey(2, 4, null);
		// key.addAlgorithm("algKey1");
		// key.addAlgorithm("algKey2");
		// key.addAlgorithm("algKey3");
		// dnssec.setKey(key);

		dnssec.setMaxSigLife(new EPPRegistryMaxSig(true, 1, 2, 3));
		dnssec.setUrgent(Boolean.TRUE);

		domain.setDnssec(dnssec);
		domain.setMaxCheckDomain(new Integer(12));
		domain.setAuthInfoRegex(new EPPRegistryRegex("^.*$", "exp"));

		EPPRegistryCustomData customData = new EPPRegistryCustomData();
		customData.addKeyValue(new EPPRegistryKeyValue("dk" + seq + "1", "dv"
				+ seq + "1"));
		customData.addKeyValue(new EPPRegistryKeyValue("dk" + seq + "2", "dv"
				+ seq + "2"));
		domain.setCustomData(customData);

		EPPRegistrySupportedStatus supportedStatus = new EPPRegistrySupportedStatus();
		supportedStatus.addStatus(Status.DOMAIN_CLIENTDELETEPROHIBITED);
		supportedStatus.addStatus(Status.DOMAIN_SERVERDELETEPROHIBITED);
		supportedStatus.addStatus(Status.DOMAIN_CLIENTHOLD);
		supportedStatus.addStatus(Status.DOMAIN_SERVERHOLD);
		supportedStatus.addStatus(Status.DOMAIN_CLIENTRENEWPROHIBITED);
		supportedStatus.addStatus(Status.DOMAIN_SERVERRENEWPROHIBITED);
		supportedStatus.addStatus(Status.DOMAIN_CLIENTTRANSFERPROHIBITED);
		supportedStatus.addStatus(Status.DOMAIN_SERVERTRANSFERPROHIBITED);
		supportedStatus.addStatus(Status.DOMAIN_CLIENTUPDATEPROHIBITED);
		supportedStatus.addStatus(Status.DOMAIN_SERVERUPDATEPROHIBITED);
		supportedStatus.addStatus(Status.DOMAIN_INACTIVE);
		supportedStatus.addStatus(Status.DOMAIN_OK);
		supportedStatus.addStatus(Status.DOMAIN_PENDINGCREATE);
		supportedStatus.addStatus(Status.DOMAIN_PENDINGDELETE);
		supportedStatus.addStatus(Status.DOMAIN_PENDINGRENEW);
		supportedStatus.addStatus(Status.DOMAIN_PENDINGTRANSFER);
		supportedStatus.addStatus(Status.DOMAIN_PENDINGUPDATE);
		domain.setSupportedStatus(supportedStatus);

		return domain;
	}

	private static EPPRegistryHost buildInfoHost(int seq) {
		EPPRegistryHost host = new EPPRegistryHost();

		host.setInternal(new EPPRegistryInternalHost(5, 15,
				EPPRegistryInternalHost.TYPE_PER_ZONE));
		host.setExternal(new EPPRegistryExternalHost(2, 12,
				EPPRegistryExternalHost.TYPE_PER_ZONE));
		host.addNameRegex(new EPPRegistryRegex("^.*$", "exp" + seq + "1"));
		host.addNameRegex(new EPPRegistryRegex("^.*$", "exp" + seq + "2"));
		host.setMaxCheckHost(new Integer(15));

		EPPRegistryCustomData customData = new EPPRegistryCustomData();
		customData.addKeyValue(new EPPRegistryKeyValue("hk" + seq + "1", "hv"
				+ seq + "1"));
		customData.addKeyValue(new EPPRegistryKeyValue("hk" + seq + "2", "hv"
				+ seq + "2"));
		host.setCustomData(customData);

		EPPRegistrySupportedStatus supportedStatus = new EPPRegistrySupportedStatus();
		supportedStatus.addStatus(Status.HOST_CLIENTDELETEPROHIBITED);
		supportedStatus.addStatus(Status.HOST_SERVERDELETEPROHIBITED);
		supportedStatus.addStatus(Status.HOST_CLIENTUPDATEPROHIBITED);
		supportedStatus.addStatus(Status.HOST_SERVERUPDATEPROHIBITED);
		supportedStatus.addStatus(Status.HOST_LINKED);
		supportedStatus.addStatus(Status.HOST_OK);
		supportedStatus.addStatus(Status.HOST_PENDINGCREATE);
		supportedStatus.addStatus(Status.HOST_PENDINGDELETE);
		supportedStatus.addStatus(Status.HOST_PENDINGTRANSFER);
		supportedStatus.addStatus(Status.HOST_PENDINGUPDATE);
		host.setSupportedStatus(supportedStatus);

		return host;
	}

	private static EPPRegistryContact buildContact(int seq) {
		EPPRegistryContact contact = new EPPRegistryContact();
		
		contact.setContactIdRegex(new EPPRegistryRegex("^.*$"));		
		contact.setSharePolicy(EPPRegistryContact.TYPE_PER_ZONE);
		contact.setLocSupport(Boolean.TRUE);
		contact.setIntSupport(Boolean.FALSE);

		EPPRegistryCustomData customData = new EPPRegistryCustomData();
		customData.addKeyValue(new EPPRegistryKeyValue("ck" + seq + "1", "cv1"
				+ seq + ""));
		customData.addKeyValue(new EPPRegistryKeyValue("ck" + seq + "2", "cv"
				+ seq + "2"));
		contact.setCustomData(customData);

		contact.setAuthInfoRegex(new EPPRegistryRegex("^.*$", "exp"));

		contact.setMaxCheckContact(new Integer(15));

		EPPRegistryPostal postalInfo = new EPPRegistryPostal();
		postalInfo.setName(new EPPRegistryContactName(6, 16));
		postalInfo.setOrg(new EPPRegistryContactOrg(2, 12));
		postalInfo.setVoiceRequired(Boolean.TRUE);
		postalInfo.setVoiceExt(new EPPRegistryMinMaxLength(5, 15));
		postalInfo.setFaxExt(new EPPRegistryMinMaxLength(5, 15));
		List emailRegex = new ArrayList();
		emailRegex.add(new EPPRegistryRegex("^.*$", "exp"));
		emailRegex.add(new EPPRegistryRegex("^.*$", "exp in ch", "ch"));
		postalInfo.setEmailRegex(emailRegex);

		EPPRegistryContactAddress address = new EPPRegistryContactAddress();
		address.setStreet(new EPPRegistryContactStreet(2, 12, 0, 3));
		address.setCity(new EPPRegistryContactCity(5, 15));
		address.setStateProvince(new EPPRegistryContactStateProvince(1, 11));
		address.setPostalCode(new EPPRegistryContactPostalCode(2, 12));

		postalInfo.setAddress(address);

		contact.setMaxCheckContact(new Integer(5));

		contact.setPostalInfo(postalInfo);

		EPPRegistrySupportedStatus supportedStatus = new EPPRegistrySupportedStatus();
		supportedStatus.addStatus(Status.CONTACT_CLIENTDELETEPROHIBITED);
		supportedStatus.addStatus(Status.CONTACT_SERVERDELETEPROHIBITED);
		supportedStatus.addStatus(Status.CONTACT_CLIENTTRANSFERPROHIBITED);
		supportedStatus.addStatus(Status.CONTACT_SERVERTRANSFERPROHIBITED);
		supportedStatus.addStatus(Status.CONTACT_CLIENTUPDATEPROHIBITED);
		supportedStatus.addStatus(Status.CONTACT_SERVERUPDATEPROHIBITED);
		supportedStatus.addStatus(Status.CONTACT_LINKED);
		supportedStatus.addStatus(Status.CONTACT_OK);
		supportedStatus.addStatus(Status.CONTACT_PENDINGCREATE);
		supportedStatus.addStatus(Status.CONTACT_PENDINGDELETE);
		supportedStatus.addStatus(Status.CONTACT_PENDINGTRANSFER);
		supportedStatus.addStatus(Status.CONTACT_PENDINGUPDATE);
		contact.setSupportedStatus(supportedStatus);

		return contact;
	}
}

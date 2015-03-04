package com.verisign.epp.codec.registry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPEncodeDecodeStats;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.registry.EPPRegistryServices.EPPRegistryObjURI;
import com.verisign.epp.codec.registry.EPPRegistryServicesExt.EPPRegistryExtURI;
import com.verisign.epp.codec.registry.EPPRegistrySupportedStatus.Status;
import com.verisign.epp.util.TestThread;

public class EPPRegistryTst extends TestCase {
	/**
	 * Number of unit test iterations to run. This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;

	public EPPRegistryTst(String name) {
		super(name);
	}

	public void testRegistryInfo() {
		EPPCodecTst.printStart("testRegistryInfo");

		EPPRegistryInfoCmd theCommand = new EPPRegistryInfoCmd("ABC-12349",
				true);
		// theCommand.setAuthInfo(new EPPAuthInfo("2fooBAR"));

		EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		EPPRegistryZoneList zoneList = new EPPRegistryZoneList();
		EPPTransId respTransId = new EPPTransId(theCommand.getTransId(),
				"54321-XYZ");
		EPPRegistryInfoResp theResponse = new EPPRegistryInfoResp(respTransId,
				zoneList);
		zoneList.addZone(new EPPRegistryZone("com", new Date(), new Date()));
		zoneList.addZone(new EPPRegistryZone("love", new Date()));
		zoneList.addZone(new EPPRegistryZone("tree", new Date(), new Date()));
		zoneList.addZone(new EPPRegistryZone("coffee", new Date(), new Date()));

		EPPEncodeDecodeStats responseStats = EPPCodecTst
				.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		theCommand = new EPPRegistryInfoCmd("ABC-12349", "com");
		// theCommand.setAuthInfo(new EPPAuthInfo("2fooBAR"));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Test with contact policy information
		respTransId = new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		EPPRegistryZoneInfo zoneInfo = createZoneInfo();

		theResponse = new EPPRegistryInfoResp(respTransId, zoneInfo);
		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);
		
		// Test without contact policy information
		zoneInfo.setContact(null);
		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);
		

		EPPCodecTst.printEnd("testRegistryInfo");
	}

	private EPPRegistryZoneInfo createZoneInfo() {
		EPPRegistryZoneInfo zoneInfo = new EPPRegistryZoneInfo("com");
		zoneInfo.setGroup("1");
		zoneInfo.setSubProduct("dotCom");

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
		

		EPPRegistryPhase phase = new EPPRegistryPhase(EPPRegistryPhase.PHASE_SUNRISE, new Date(),
				new Date());
		phase.setMode(EPPRegistryPhase.MODE_PENDING_APPLICATION);
		zoneInfo.addPhase(phase);
		phase = new EPPRegistryPhase(EPPRegistryPhase.PHASE_PRE_DELEGATION, new Date(),
				new Date());
		phase.setMode(EPPRegistryPhase.MODE_PENDING_REGISTRATION);
		zoneInfo.addPhase(phase);
		zoneInfo.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_LANDRUSH, new Date(),
				new Date()));
		zoneInfo.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_PRE_DELEGATION, new Date(),
				new Date()));
		zoneInfo.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_CLAIMS, new Date(), new Date()));
		zoneInfo.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_OPEN, new Date()));
		zoneInfo.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_CUSTOM, "userDefined",
				new Date()));

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
				"http://www.verisign.com/epp/premiumdomain-1.0", Boolean.TRUE));
		svcExt.addExtURI(new EPPRegistryExtURI(
				"urn:ietf:params:xml:ns:secDNS-1.1", Boolean.FALSE));
		zoneInfo.setServices(services);

		EPPRegistrySLAInfo slaInfo = new EPPRegistrySLAInfo();
		slaInfo.addSla(new EPPRegistrySLA("response", "ext", "create", 500,
				"ms"));
		slaInfo.addSla(new EPPRegistrySLA("availability", null, "create", 99.9,
				"percent"));
		zoneInfo.setSlaInfo(slaInfo);

		zoneInfo.setCreatedBy("crId");
		zoneInfo.setCreatedDate(new Date());
		zoneInfo.setLastUpdatedBy("upId");
		zoneInfo.setLastUpdatedDate(new Date());

		zoneInfo.setDomain(buildInfoDomain());

		zoneInfo.setHost(buildInfoHost());

		zoneInfo.setContact(buildContact());
		return zoneInfo;
	}

	public void testRegistryCheck() {
		EPPCodecTst.printStart("testRegistryCheck");

		EPPRegistryCheckCmd theCommand = new EPPRegistryCheckCmd("ABC-12345",
				"com");

		EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		theCommand = new EPPRegistryCheckCmd("ABC-12345", "com");
		theCommand.addName("weirdname");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode EPPRegistryInfo Response
		EPPRegistryCheckResp theResponse;
		EPPEncodeDecodeStats responseStats;

		EPPTransId respTransId = new EPPTransId(theCommand.getTransId(),
				"54321-XYZ");
		EPPRegistryCheckResult result = new EPPRegistryCheckResult("com",
				Boolean.FALSE);
		result.setReason("Already taken");
		theResponse = new EPPRegistryCheckResp(respTransId, result);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		respTransId = new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		List results = new ArrayList();
		result = new EPPRegistryCheckResult("com", Boolean.FALSE);
		result.setReason("Already taken");
		results.add(result);
		results.add(new EPPRegistryCheckResult("availtld", Boolean.FALSE));

		theResponse = new EPPRegistryCheckResp(respTransId, results);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testRegistryCheck");
	}

	public void testRegistryCreate() {
		EPPCodecTst.printStart("testRegistryCreate");

		EPPRegistryZoneInfo zone = createZoneInfo();
		zone.setName("newtld");

		EPPRegistryCreateCmd theCommand = null;
		theCommand = new EPPRegistryCreateCmd("ABC-12349", zone);

		EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPTransId respTransId = new EPPTransId(theCommand.getTransId(),
				"54321-XYZ");

		EPPRegistryCreateResp theResponse = new EPPRegistryCreateResp(
				respTransId, "newtld");
		Calendar baseCal = Calendar.getInstance();
		Calendar endCal = (Calendar) baseCal.clone();
		theResponse.setCreateDate(baseCal.getTime());
		endCal = (Calendar) baseCal.clone();
		endCal.add(Calendar.MONTH, 9);
		endCal.add(Calendar.YEAR, 10);

		EPPEncodeDecodeStats responseStats = EPPCodecTst
				.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testRegistryCreate");
	}

	public void testRegistryUpdate() {
		EPPCodecTst.printStart("testRegistryUpdate");

		EPPRegistryZoneInfo zone = new EPPRegistryZoneInfo();
		zone.setName("newtld");

		EPPRegistryUpdateCmd theCommand = null;
		theCommand = new EPPRegistryUpdateCmd("ABC-12349", zone);

		zone.setDomain(this.buildInfoDomain());
		zone.setHost(this.buildInfoHost());
		zone.setContact(this.buildContact());

		EPPRegistryServices services = new EPPRegistryServices();
		services.addObjURI(new EPPRegistryObjURI(EPPRegistryMapFactory.NS,
				Boolean.TRUE));
		EPPRegistryServicesExt svcExt = new EPPRegistryServicesExt();
		services.setExtension(svcExt);
		svcExt.addExtURI(new EPPRegistryExtURI(EPPRegistryMapFactory.NS,
				Boolean.TRUE));
		zone.setServices(services);

		EPPRegistrySLAInfo slaInfo = new EPPRegistrySLAInfo();
		slaInfo.addSla(new EPPRegistrySLA("response", "ext", "create", 500,
				"ms"));
		slaInfo.addSla(new EPPRegistrySLA("availability", null, "create", 99.9,
				"percent"));
		zone.setSlaInfo(slaInfo);
		zone.setCreatedBy("crId");
		zone.setCreatedDate(new Date());

		Calendar baseCal = Calendar.getInstance();
		Calendar startCal = (Calendar) baseCal.clone();
		Calendar endCal = (Calendar) baseCal.clone();
		endCal.add(Calendar.MONTH, 3);
		EPPRegistryPhase phase = new EPPRegistryPhase(EPPRegistryPhase.PHASE_SUNRISE, new Date(),
				new Date());
		phase.setMode(EPPRegistryPhase.MODE_PENDING_APPLICATION);
		zone.addPhase(phase);
		phase = new EPPRegistryPhase(EPPRegistryPhase.PHASE_PRE_DELEGATION, new Date(),
				new Date());
		phase.setMode(EPPRegistryPhase.MODE_PENDING_REGISTRATION);
		zone.addPhase(phase);
		zone.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_LANDRUSH, new Date(),
				new Date()));
		zone.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_PRE_DELEGATION, new Date(),
				new Date()));
		zone.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_CLAIMS, new Date(), new Date()));
		zone.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_OPEN, new Date()));
		zone.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_CUSTOM, "userDefined",
				new Date()));

		EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPTransId respTransId = new EPPTransId(theCommand.getTransId(),
				"54321-XYZ");

		EPPResponse theResponse = new EPPResponse(respTransId);

		EPPEncodeDecodeStats responseStats = EPPCodecTst
				.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testRegistryUpdate");
	}

	public void testRegistryDelete() {
		EPPCodecTst.printStart("testRegistryDelete");

		EPPRegistryDeleteCmd theCommand = null;
		theCommand = new EPPRegistryDeleteCmd("ABC-12349", "newetld");

		EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPTransId respTransId = new EPPTransId(theCommand.getTransId(),
				"54321-XYZ");

		EPPResponse theResponse = new EPPResponse(respTransId);

		EPPEncodeDecodeStats responseStats = EPPCodecTst
				.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testRegistryDelete");
	}

	private EPPRegistryDomain buildInfoDomain() {
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
		rNames.add("reserved1");
		rNames.add("reserved2");
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
			fail("failed to clone domainName");
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
		domain.setContactsSupported(new Boolean(false));

		domain.addContact(new EPPRegistryDomainContact(
				EPPRegistryDomainContact.TYPE_ADMIN, 1, 4));
		domain.addContact(new EPPRegistryDomainContact(
				EPPRegistryDomainContact.TYPE_BILLING, 2, 5));
		domain.addContact(new EPPRegistryDomainContact(
				EPPRegistryDomainContact.TYPE_TECH, 3, 6));

		domain.setNameServerLimit(new EPPRegistryDomainNSLimit(1, 16));

		domain.setChildHostLimit(new EPPRegistryDomainHostLimit(2, 32));

		domain.addPeriod(new EPPRegistryDomainPeriod("create", Boolean.TRUE));
		domain.addPeriod(new EPPRegistryDomainPeriod("renew", 1, "y", 60, "m",
				5, "y"));
		domain.addPeriod(new EPPRegistryDomainPeriod("transfer", 1, "y", 8,
				"y", 3, "y"));

		domain.setTransferHoldPeriod(new EPPRegistryTransferHoldPeriodType(5,
				"d"));

		domain.addGracePeriod(new EPPRegistryGracePeriod("create", 1, "d"));
		domain.addGracePeriod(new EPPRegistryGracePeriod("renew", 2, "h"));
		domain.addGracePeriod(new EPPRegistryGracePeriod("transfer", 3, "m"));

		EPPRegistryRGP rgp = new EPPRegistryRGP();
		rgp.setPendingDeletePeriod(new EPPRegistryPendingDeletePeriodType(1,
				"m"));
		rgp.setRedemptionPeriod(new EPPRegistryRedemptionPeriodType(1, "m"));
		rgp.setPendingRestorePeriod(new EPPRegistryPendingRestorePeriodType(1,
				"m"));
		domain.setRgp(rgp);

		EPPRegistryDNSSEC dnssec = new EPPRegistryDNSSEC();
		EPPRegistryDS ds = new EPPRegistryDS(1, 3, null, null);
		ds.addAlgorithm("algDS1");
		ds.addAlgorithm("algDS2");
		ds.addDigestType("digest1");
		ds.addDigestType("digest2");
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
		customData.addKeyValue(new EPPRegistryKeyValue("dk1", "dv1"));
		customData.addKeyValue(new EPPRegistryKeyValue("dk2", "dv2"));
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

	private EPPRegistryHost buildInfoHost() {
		EPPRegistryHost host = new EPPRegistryHost();

		host.setInternal(new EPPRegistryInternalHost(5, 15,
				EPPRegistryInternalHost.TYPE_PER_ZONE));
		host.setExternal(new EPPRegistryExternalHost(2, 12,
				EPPRegistryExternalHost.TYPE_PER_ZONE));
		host.addNameRegex(new EPPRegistryRegex("^.*$", "exp1"));
		host.addNameRegex(new EPPRegistryRegex("^.*$", "exp2"));
		host.setMaxCheckHost(new Integer(15));

		EPPRegistryCustomData customData = new EPPRegistryCustomData();
		customData.addKeyValue(new EPPRegistryKeyValue("hk1", "hv1"));
		customData.addKeyValue(new EPPRegistryKeyValue("hk2", "hv2"));
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

	private EPPRegistryContact buildContact() {
		EPPRegistryContact contact = new EPPRegistryContact();

		contact.setLocSupport(Boolean.TRUE);
		contact.setIntSupport(Boolean.FALSE);

		EPPRegistryCustomData customData = new EPPRegistryCustomData();
		customData.addKeyValue(new EPPRegistryKeyValue("ck1", "cv1"));
		customData.addKeyValue(new EPPRegistryKeyValue("ck2", "cv2"));
		contact.setCustomData(customData);

		contact.setContactIdRegex(new EPPRegistryRegex("^.*$"));
		contact.setSharePolicy(EPPRegistryContact.TYPE_PER_ZONE);
		contact.setAuthInfoRegex(new EPPRegistryRegex("^.*$", "exp"));

		contact.setMaxCheckContact(new Integer(15));

		EPPRegistryPostal postalInfo = new EPPRegistryPostal();
		postalInfo.setName(new EPPRegistryContactName(5, 15));
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

		contact.setClientDisclosureSupported(Boolean.TRUE);

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
		
		contact.setTransferHoldPeriod(new EPPRegistryTransferHoldPeriodType(5,
				"d"));

		return contact;
	}

	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * 
	 * <ul>
	 * <li>
	 * iterations Number of unit test iterations to run</li>
	 * <li>
	 * validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be
	 * off.</li>
	 * </ul>
	 * 
	 * 
	 * @param args
	 *            DOCUMENT ME!
	 */
	public static void main(String[] args) {
		// Number of Threads
		int numThreads = 1;
		String threadsStr = System.getProperty("threads");

		if (threadsStr != null) {
			numThreads = Integer.parseInt(threadsStr);
		}

		// Run test suite in multiple threads?
		if (numThreads > 1) {
			// Spawn each thread passing in the Test Suite
			for (int i = 0; i < numThreads; i++) {
				TestThread thread = new TestThread(
						"EPPRegistryTst Thread " + i, EPPRegistryTst.suite());
				thread.start();
			}
		} else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPRegistryTst.suite());
		}
	}

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPRegistryTst</code>.
	 * 
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPRegistryTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPRegistryMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.registry.EPPRegistryMapFactory");
		} catch (EPPCodecException e) {
			Assert.fail("EPPCodecException adding EPPRegistryMapFactory to EPPCodec: "
					+ e);
		}

		return suite;
	}

	/**
	 * JUNIT <code>setUp</code>, which currently does nothing.
	 */
	protected void setUp() {
	}

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	/**
	 * Sets the number of iterations to run per test.
	 * 
	 * @param aNumIterations
	 *            number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}
}

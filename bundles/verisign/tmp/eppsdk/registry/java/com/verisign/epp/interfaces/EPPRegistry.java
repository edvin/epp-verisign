package com.verisign.epp.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.registry.EPPRegistryCheckCmd;
import com.verisign.epp.codec.registry.EPPRegistryCheckResp;
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
import com.verisign.epp.codec.registry.EPPRegistryDeleteCmd;
import com.verisign.epp.codec.registry.EPPRegistryDomain;
import com.verisign.epp.codec.registry.EPPRegistryDomainContact;
import com.verisign.epp.codec.registry.EPPRegistryDomainHostLimit;
import com.verisign.epp.codec.registry.EPPRegistryDomainNSLimit;
import com.verisign.epp.codec.registry.EPPRegistryDomainName;
import com.verisign.epp.codec.registry.EPPRegistryDomainPeriod;
import com.verisign.epp.codec.registry.EPPRegistryExternalHost;
import com.verisign.epp.codec.registry.EPPRegistryGracePeriod;
import com.verisign.epp.codec.registry.EPPRegistryHost;
import com.verisign.epp.codec.registry.EPPRegistryIDN;
import com.verisign.epp.codec.registry.EPPRegistryInfoCmd;
import com.verisign.epp.codec.registry.EPPRegistryInfoResp;
import com.verisign.epp.codec.registry.EPPRegistryInternalHost;
import com.verisign.epp.codec.registry.EPPRegistryKey;
import com.verisign.epp.codec.registry.EPPRegistryKeyValue;
import com.verisign.epp.codec.registry.EPPRegistryLanguageType;
import com.verisign.epp.codec.registry.EPPRegistryMapFactory;
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
import com.verisign.epp.codec.registry.EPPRegistryServices.EPPRegistryObjURI;
import com.verisign.epp.codec.registry.EPPRegistryServicesExt;
import com.verisign.epp.codec.registry.EPPRegistryServicesExt.EPPRegistryExtURI;
import com.verisign.epp.codec.registry.EPPRegistrySupportedStatus;
import com.verisign.epp.codec.registry.EPPRegistrySupportedStatus.Status;
import com.verisign.epp.codec.registry.EPPRegistryTransferHoldPeriodType;
import com.verisign.epp.codec.registry.EPPRegistryUpdateCmd;
import com.verisign.epp.codec.registry.EPPRegistryZoneInfo;

public class EPPRegistry {
	/** An instance of a session. */
	private EPPSession session = null;

	/** Transaction Id provided by cliet */
	private String transId = null;

	private boolean allTlds = false;

	// String
	private List zoneList = null;

	/**
	 * This Attribute Contains Authorization String provided by client when
	 * manipulation information on the Server
	 */
	private String authString;

	/**
	 * This attributes contains the roid for hte regisrant or contact object
	 * that the <code>authString</code> is associated with.
	 */
	private String authRoid;

	/**
	 * Constructs an <code>EPPRegistry</code> given an initialized EPP session.
	 * 
	 * @param aSession
	 *            Server session to use.
	 */
	public EPPRegistry(EPPSession aSession) {
		this.session = aSession;

		return;
	}

	/**
	 * Sends a TLD Name Check Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following methods:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addTld</code> - Adds a TLD name to check. More than one TLD names
	 * can be checked in <code>sendCheck</code></li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setTransId</code> - Sets the client transaction identifier</li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPRegistryCheckResp</code> containing the TLD check
	 *         information.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the check command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPRegistryCheckResp sendCheck() throws EPPCommandException {
		// Create the command
		EPPRegistryCheckCmd theCommand = new EPPRegistryCheckCmd(this.transId,
				this.zoneList);

		// theCommand.setExtensions(this.extensions);

		// Reset registry attributes
		resetRegistry();

		// process the command and response
		return (EPPRegistryCheckResp) this.session.processDocument(theCommand,
				EPPRegistryCheckResp.class);
	}

	public EPPResponse sendDelete() throws EPPCommandException {
		if (this.zoneList == null || this.zoneList.size() != 1) {
			throw new EPPCommandException(
					"One Tld Name is required for sendDelete()");
		}
		// Create the command
		EPPRegistryDeleteCmd theCommand = new EPPRegistryDeleteCmd(
				this.transId, (String) this.zoneList.get(0));

		// Reset registry attributes
		resetRegistry();

		// process the command and response
		return (EPPResponse) this.session.processDocument(theCommand,
				EPPResponse.class);
	}

	/**
	 * Sends a Registry Info Command to the server.<br>
	 * <br>
	 * The required attributes have been set with the following methods:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addTldName</code> - Sets the tld name to get info for. Only one tld
	 * name is valid.</li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setTransId</code> - Sets the client transaction identifier</li>
	 * <li>
	 * <code>setAuthString</code> - Sets the Authorization string</li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPRegistryInfoResp</code> containing the Registry
	 *         information.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the info command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPRegistryInfoResp sendInfo() throws EPPCommandException {
		if ((this.zoneList == null || this.zoneList.size() != 1)
				&& !this.allTlds) {
			throw new EPPCommandException(
					"One Tld Name is required for sendInfo(), unless allTlds is set to true");
		}

		EPPRegistryInfoCmd theCommand = null;
		if (!this.allTlds) {
			theCommand = new EPPRegistryInfoCmd("ABC-12349",
					(String) this.zoneList.get(0));
		} else {
			theCommand = new EPPRegistryInfoCmd("ABC-12349", true);
		}

		// Authorization string was provided?
		if (this.authString != null) {

			EPPAuthInfo theAuthInfo = new EPPAuthInfo(this.authString);

			// Authorization roid was provided?
			if (this.authRoid != null) {
				theAuthInfo.setRoid(this.authRoid);
			}

			// theCommand.setAuthInfo(theAuthInfo);
		}

		resetRegistry();

		return (EPPRegistryInfoResp) this.session.processDocument(theCommand,
				EPPRegistryInfoResp.class);
	}

	public EPPRegistryCreateResp sendCreate() throws EPPCommandException {
		if (this.zoneList == null || this.zoneList.size() != 1) {
			throw new EPPCommandException(
					"One Tld Name is required for sendCreate()");
		}

		EPPRegistryZoneInfo tld = new EPPRegistryZoneInfo();
		tld.setName((String) this.zoneList.get(0));

		EPPRegistryCreateCmd theCommand = null;
		theCommand = new EPPRegistryCreateCmd("ABC-12349", tld);

		// Authorization string was provided?
		if (this.authString != null) {

			EPPAuthInfo theAuthInfo = new EPPAuthInfo(this.authString);

			// Authorization roid was provided?
			if (this.authRoid != null) {
				theAuthInfo.setRoid(this.authRoid);
			}
		}

		tld.setDomain(this.buildDomain());
		tld.setHost(this.buildHost());
		tld.setContact(this.buildContact());

		if ("com".equals((String) this.zoneList.get(0))) {
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
			tld.setServices(services);
		} else {
			EPPRegistryServices services = new EPPRegistryServices();
			services.addObjURI(new EPPRegistryObjURI(EPPRegistryMapFactory.NS,
					Boolean.TRUE));
			EPPRegistryServicesExt svcExt = new EPPRegistryServicesExt();
			services.setExtension(svcExt);
			svcExt.addExtURI(new EPPRegistryExtURI(EPPRegistryMapFactory.NS,
					Boolean.TRUE));
			tld.setServices(services);
		}

		EPPRegistrySLAInfo slaInfo = new EPPRegistrySLAInfo();
		slaInfo.addSla(new EPPRegistrySLA("response", "ext", "create", 500,
				"ms"));
		slaInfo.addSla(new EPPRegistrySLA("availability", null, "create", 99.9,
				"percent"));
		tld.setSlaInfo(slaInfo);
		tld.setCreatedBy("crId");
		tld.setCreatedDate(new Date());

		EPPRegistryPhase phase = new EPPRegistryPhase(EPPRegistryPhase.PHASE_SUNRISE, new Date(),
				new Date());
		phase.setMode(EPPRegistryPhase.MODE_PENDING_APPLICATION);
		tld.addPhase(phase);
		phase = new EPPRegistryPhase(EPPRegistryPhase.PHASE_PRE_DELEGATION, new Date(),
				new Date());
		phase.setMode(EPPRegistryPhase.MODE_PENDING_REGISTRATION);
		tld.addPhase(phase);
		tld.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_LANDRUSH, new Date(),
				new Date()));
		tld.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_PRE_DELEGATION, new Date(),
				new Date()));
		tld.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_CLAIMS, new Date(), new Date()));
		tld.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_OPEN, new Date()));
		tld.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_CUSTOM, "userDefined",
				new Date()));

		resetRegistry();

		return (EPPRegistryCreateResp) this.session.processDocument(theCommand,
				EPPRegistryCreateResp.class);
	}

	public EPPResponse sendUpdate() throws EPPCommandException {
		if (this.zoneList == null || this.zoneList.size() != 1) {
			throw new EPPCommandException(
					"One Tld Name is required for sendUpdate()");
		}

		EPPRegistryZoneInfo tld = new EPPRegistryZoneInfo();
		tld.setName((String) this.zoneList.get(0));

		EPPRegistryUpdateCmd theCommand = null;
		theCommand = new EPPRegistryUpdateCmd("ABC-12349", tld);

		// Authorization string was provided?
		if (this.authString != null) {

			EPPAuthInfo theAuthInfo = new EPPAuthInfo(this.authString);

			// Authorization roid was provided?
			if (this.authRoid != null) {
				theAuthInfo.setRoid(this.authRoid);
			}
		}

		tld.setDomain(this.buildDomain());
		tld.setHost(this.buildHost());
		tld.setContact(this.buildContact());

		EPPRegistryServices services = new EPPRegistryServices();
		services.addObjURI(new EPPRegistryObjURI(
				"urn:ietf:params:xml:ns:registry-1.2", Boolean.TRUE));
		services.addObjURI(new EPPRegistryObjURI(
				"urn:ietf:params:xml:ns:registry-1.1", Boolean.FALSE));
		EPPRegistryServicesExt svcExt = new EPPRegistryServicesExt();
		services.setExtension(svcExt);
		svcExt.addExtURI(new EPPRegistryExtURI(
				"urn:ietf:params:xml:ns:registry-1.2", Boolean.TRUE));
		svcExt.addExtURI(new EPPRegistryExtURI(
				"urn:ietf:params:xml:ns:registry-1.1", Boolean.FALSE));
		tld.setServices(services);

		EPPRegistrySLAInfo slaInfo = new EPPRegistrySLAInfo();
		slaInfo.addSla(new EPPRegistrySLA("response", "ext", "create", 500,
				"ms"));
		slaInfo.addSla(new EPPRegistrySLA("availability", null, "create", 99.9,
				"percent"));
		tld.setSlaInfo(slaInfo);
		tld.setCreatedBy("crId");
		tld.setCreatedDate(new Date());

		EPPRegistryPhase phase = new EPPRegistryPhase(EPPRegistryPhase.PHASE_SUNRISE, new Date(),
				new Date());
		phase.setMode(EPPRegistryPhase.MODE_PENDING_APPLICATION);
		tld.addPhase(phase);
		phase = new EPPRegistryPhase(EPPRegistryPhase.PHASE_PRE_DELEGATION, new Date(),
				new Date());
		phase.setMode(EPPRegistryPhase.MODE_PENDING_REGISTRATION);
		tld.addPhase(phase);
		tld.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_LANDRUSH, new Date(),
				new Date()));
		tld.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_PRE_DELEGATION, new Date(),
				new Date()));
		tld.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_CLAIMS, new Date(), new Date()));
		tld.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_OPEN, new Date()));
		tld.addPhase(new EPPRegistryPhase(EPPRegistryPhase.PHASE_CUSTOM, "userDefined",
				new Date()));

		resetRegistry();

		return (EPPResponse) this.session.processDocument(theCommand,
				EPPResponse.class);
	}

	private EPPRegistryDomain buildDomain() {
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
			e.printStackTrace();
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
		domain.addPeriod(new EPPRegistryDomainPeriod("renew", 1, "y", 10, "y",
				2, "y"));
		domain.addPeriod(new EPPRegistryDomainPeriod("transfer", 1, "y", 8,
				"y", 3, "y"));

		domain.setTransferHoldPeriod(new EPPRegistryTransferHoldPeriodType(1,
				"y"));

		domain.addGracePeriod(new EPPRegistryGracePeriod("create", 1, "m"));
		domain.addGracePeriod(new EPPRegistryGracePeriod("renew", 2, "h"));
		domain.addGracePeriod(new EPPRegistryGracePeriod("transfer", 3, "d"));

		EPPRegistryRGP rgp = new EPPRegistryRGP();
		rgp.setPendingDeletePeriod(new EPPRegistryPendingDeletePeriodType(1,
				"m"));
		rgp.setRedemptionPeriod(new EPPRegistryRedemptionPeriodType(1, "m"));
		rgp.setPendingRestorePeriod(new EPPRegistryPendingRestorePeriodType(1,
				"m"));
		domain.setRgp(rgp);

		EPPRegistryDNSSEC dnssec = new EPPRegistryDNSSEC();
		// EPPRegistryDS ds = new EPPRegistryDS(1, 3, null, null);
		// ds.addAlgorithm("algDS1");
		// ds.addAlgorithm("algDS2");
		// ds.addDigestType("digest1");
		// ds.addDigestType("digest2");
		// dnssec.setDs(ds);

		EPPRegistryKey key = new EPPRegistryKey(2, 4, null);
		key.addAlgorithm("algKey1");
		key.addAlgorithm("algKey2");
		key.addAlgorithm("algKey3");
		dnssec.setKey(key);

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

	private EPPRegistryHost buildHost() {
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

		contact.setContactIdRegex(new EPPRegistryRegex("^.*$"));
		contact.setSharePolicy(EPPRegistryContact.TYPE_PER_ZONE);
		contact.setLocSupport(Boolean.TRUE);
		contact.setIntSupport(Boolean.FALSE);

		EPPRegistryCustomData customData = new EPPRegistryCustomData();
		customData.addKeyValue(new EPPRegistryKeyValue("ck1", "cv1"));
		customData.addKeyValue(new EPPRegistryKeyValue("ck2", "cv2"));
		contact.setCustomData(customData);

		contact.setAuthInfoRegex(new EPPRegistryRegex("^.*$", "exp"));

		contact.setMaxCheckContact(new Integer(15));

		EPPRegistryPostal postalInfo = new EPPRegistryPostal();
		postalInfo.setName(new EPPRegistryContactName(5, 15));
		postalInfo.setOrg(new EPPRegistryContactOrg(2, 12));
		postalInfo.setVoiceRequired(Boolean.TRUE);
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
		postalInfo.setVoiceRequired(Boolean.TRUE);
		postalInfo.setVoiceExt(new EPPRegistryMinMaxLength(5, 15));
		postalInfo.setFaxExt(new EPPRegistryMinMaxLength(5, 15));

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

	public EPPSession getSession() {
		return session;
	}

	public void setSession(EPPSession session) {
		this.session = session;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public List getZoneList() {
		return zoneList;
	}

	public void setZoneList(List zoneList) {
		this.zoneList = zoneList;
	}

	public void addTld(String tld) {
		if (this.zoneList == null) {
			this.zoneList = new ArrayList();
		}
		this.zoneList.add(tld);
		this.allTlds = false;
	}

	public boolean isAllTlds() {
		return allTlds;
	}

	public void setAllTlds(boolean allTlds) {
		this.allTlds = allTlds;
		if (allTlds) {
			this.zoneList = new ArrayList();
		}
	}

	public void resetRegistry() {
		this.transId = null;
		this.zoneList = new ArrayList();
		this.authRoid = null;
		this.authRoid = null;
		this.allTlds = false;
	}

	public String getAuthString() {
		return authString;
	}

	public void setAuthString(String authString) {
		this.authString = authString;
	}

	public String getAuthRoid() {
		return authRoid;
	}

	public void setAuthRoid(String authRoid) {
		this.authRoid = authRoid;
	}
}

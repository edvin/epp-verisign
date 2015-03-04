package com.verisign.epp.serverstub;

import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.registry.EPPRegistryMapFactory;
import com.verisign.epp.framework.EPPPollDataRecord;
import com.verisign.epp.framework.EPPPollHandler;
import com.verisign.epp.framework.EPPPollQueueException;

public class RegistryPollHandler implements EPPPollHandler {

	public String getKind() {
		return EPPRegistryMapFactory.NS;
	}

	public EPPResponse toResponse(EPPPollDataRecord aRecord)
			throws EPPPollQueueException {
		if (!aRecord.getKind().equals(getKind())) {
			throw new EPPPollQueueException("Handler for kind "
					+ aRecord.getKind() + " does not match");
		}

		// Get the concrete response from the record
		EPPResponse theResponse = (EPPResponse) aRecord.getData();

		throw new EPPPollQueueException("Unable to handle message class <"
				+ theResponse.getClass().getName());
	}
}

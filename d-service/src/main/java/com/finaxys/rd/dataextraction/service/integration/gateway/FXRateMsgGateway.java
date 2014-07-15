package com.finaxys.rd.dataextraction.service.integration.gateway;

public interface FXRateMsgGateway {

/**
* Publish fx rates.
*/
public void publishCurrentFXRatesList(String symbols);
}

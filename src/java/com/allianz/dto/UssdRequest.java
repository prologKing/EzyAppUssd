/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allianz.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author Bolaji
 */
public class UssdRequest {

    private String msisdn;
    private String UserSessionID;
    private String data;
    private String network;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getUserSessionID() {
        return UserSessionID;
    }

    public void setUserSessionID(String UserSessionID) {
        this.UserSessionID = UserSessionID;
    }

    public String getMsg() {
        return data;
    }

    public void setMsg(String data) {
        this.data = data;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    
    
     @Override
    public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}

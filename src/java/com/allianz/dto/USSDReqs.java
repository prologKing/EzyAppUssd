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
public class USSDReqs {
    
    private UssdRequest USSDReq;

    public UssdRequest getUSSDReq() {
        return USSDReq;
    }

    public void setUSSDReq(UssdRequest USSDReq) {
        this.USSDReq = USSDReq;
    }
    
     @Override
    public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
    
}

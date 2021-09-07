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
 * @author Developer
 */
public class ProductTbPKDTO {
    private int pdtSysid;
    private int pdtIttSysid;
    private int pdtUserSysid;

    public ProductTbPKDTO() {
    }

    public int getPdtSysid() {
        return pdtSysid;
    }

    public void setPdtSysid(int pdtSysid) {
        this.pdtSysid = pdtSysid;
    }

    public int getPdtIttSysid() {
        return pdtIttSysid;
    }

    public void setPdtIttSysid(int pdtIttSysid) {
        this.pdtIttSysid = pdtIttSysid;
    }

    public int getPdtUserSysid() {
        return pdtUserSysid;
    }

    public void setPdtUserSysid(int pdtUserSysid) {
        this.pdtUserSysid = pdtUserSysid;
    }

   @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    
    
}

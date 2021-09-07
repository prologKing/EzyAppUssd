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
public class CustomerTbPKDTO {
int cusSysid;
    int cusUserSysid;
    int cusBranchSysid;

    public CustomerTbPKDTO() {
    }
    
    

    public CustomerTbPKDTO(int cusSysid, int cusUserSysid, int cusBranchSysid) {
        this.cusSysid = cusSysid;
        this.cusUserSysid = cusUserSysid;
        this.cusBranchSysid = cusBranchSysid;
    }
   

    public int getCusSysid() {
        return cusSysid;
    }

    public void setCusSysid(int cusSysid) {
        this.cusSysid = cusSysid;
    }

    public int getCusUserSysid() {
        return cusUserSysid;
    }

    public void setCusUserSysid(int cusUserSysid) {
        this.cusUserSysid = cusUserSysid;
    }

    public int getCusBranchSysid() {
        return cusBranchSysid;
    }

    public void setCusBranchSysid(int cusBranchSysid) {
        this.cusBranchSysid = cusBranchSysid;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    } 
    
}


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
public class ProductAgentsTbPKDTO {
 int pagtSysid;
    int pagtUserSysid;
    int pagtBranchSysid;
    int pagtPbkSysid;
    int pagtPbkbSysid;
    int pagtPahSysid;
    int pagtPalLevelId;

    public ProductAgentsTbPKDTO() {
    }

    public ProductAgentsTbPKDTO(int pagtSysid, int pagtUserSysid, int pagtBranchSysid, int pagtPbkSysid, int pagtPbkbSysid, int pagtPahSysid, int pagtPalLevelId) {
        this.pagtSysid = pagtSysid;
        this.pagtUserSysid = pagtUserSysid;
        this.pagtBranchSysid = pagtBranchSysid;
        this.pagtPbkSysid = pagtPbkSysid;
        this.pagtPbkbSysid = pagtPbkbSysid;
        this.pagtPahSysid = pagtPahSysid;
        this.pagtPalLevelId = pagtPalLevelId;
    }
  
  

    public int getPagtSysid() {
        return pagtSysid;
    }

    public void setPagtSysid(int pagtSysid) {
        this.pagtSysid = pagtSysid;
    }

    public int getPagtUserSysid() {
        return pagtUserSysid;
    }

    public void setPagtUserSysid(int pagtUserSysid) {
        this.pagtUserSysid = pagtUserSysid;
    }

    public int getPagtBranchSysid() {
        return pagtBranchSysid;
    }

    public void setPagtBranchSysid(int pagtBranchSysid) {
        this.pagtBranchSysid = pagtBranchSysid;
    }

    public int getPagtPbkSysid() {
        return pagtPbkSysid;
    }

    public void setPagtPbkSysid(int pagtPbkSysid) {
        this.pagtPbkSysid = pagtPbkSysid;
    }

    public int getPagtPbkbSysid() {
        return pagtPbkbSysid;
    }

    public void setPagtPbkbSysid(int pagtPbkbSysid) {
        this.pagtPbkbSysid = pagtPbkbSysid;
    }

    public int getPagtPahSysid() {
        return pagtPahSysid;
    }

    public void setPagtPahSysid(int pagtPahSysid) {
        this.pagtPahSysid = pagtPahSysid;
    }

    public int getPagtPalLevelId() {
        return pagtPalLevelId;
    }

    public void setPagtPalLevelId(int pagtPalLevelId) {
        this.pagtPalLevelId = pagtPalLevelId;
    }

     @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}


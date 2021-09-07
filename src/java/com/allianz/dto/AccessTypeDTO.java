/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allianz.dto;

/**
 *
 * @author Bolaji
 */
public class AccessTypeDTO {
    
    private String mobile;
    private String custtype;

    public AccessTypeDTO() {
    }

    public AccessTypeDTO(String mobile, String custtype) {
        this.mobile = mobile;
        this.custtype = custtype;
    }
    
    

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCusttype() {
        return custtype;
    }

    public void setCusttype(String custtype) {
        this.custtype = custtype;
    }

    @Override
    public String toString() {
        return "AccessTypeDTO{" + "mobile=" + mobile + ", custtype=" + custtype + '}';
    }
    
}

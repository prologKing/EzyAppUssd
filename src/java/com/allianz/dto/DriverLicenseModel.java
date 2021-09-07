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
public class DriverLicenseModel {
    private String ResponseCode;
    private String Name;
    private String CertificateOfCompetence;
    private String DriverImage;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getCertificateOfCompetence() {
        return CertificateOfCompetence;
    }

    public void setCertificateOfCompetence(String CertificateOfCompetence) {
        this.CertificateOfCompetence = CertificateOfCompetence;
    }

    public String getDriverImage() {
        return DriverImage;
    }

    public void setDriverImage(String DriverImage) {
        this.DriverImage = DriverImage;
    }
    
    @Override
    public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }  
}

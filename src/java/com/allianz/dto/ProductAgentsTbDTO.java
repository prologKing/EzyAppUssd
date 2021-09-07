/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allianz.dto;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author Developer
 */
public class ProductAgentsTbDTO {
 private ProductAgentsTbPKDTO productAgentsTbPK; 
    private int pagtReportId; 
    private String pagtReportMgr; 
    private String pagtCode; 
    private String pagtSurname; 
    private String pagtOthname; 
    private String pagtGender; 
    private Date pagtDob; 
    private String pagtAddrs; 
    private String pagtMobile;
    private String pagtMomoNo;
    private String pagtStatus;
    private String pagtAcctNo;
    private Date pagtRegDt;
    private Date pagtCrtDt;
    private String pagtEmail;
    private String pagtPhoto; 
    private Date pagtExitDt; 
    private String pagtMaritalSts; 
    private Date pagtBfitStdt; 
    private String pagtEntSource;
    private String pagtUsed; 
    private String pagtLevel; 
    private String pagtHierarchy; 
    private int pagtSubbranchSysid;

    public ProductAgentsTbDTO() {
        this.productAgentsTbPK = new ProductAgentsTbPKDTO();
    }
    
   public ProductAgentsTbDTO(Object[] obj) {
        this.productAgentsTbPK = new ProductAgentsTbPKDTO((Integer)obj[0], (Integer)obj[1], (Integer)obj[2], (Integer)obj[5], (Integer)obj[6], (Integer)obj[26], (Integer)obj[29]);
        this.pagtCode = (String)obj[7];
        this.pagtReportId = (Integer)obj[3];
        this.pagtReportMgr = (String)obj[4];
    } 
  

    public ProductAgentsTbPKDTO getProductAgentsTbPK() {
        return productAgentsTbPK;
    }

    public void setProductAgentsTbPK(ProductAgentsTbPKDTO productAgentsTbPK) {
        this.productAgentsTbPK = productAgentsTbPK;
    }

    public int getPagtReportId() {
        return pagtReportId;
    }

    public void setPagtReportId(int pagtReportId) {
        this.pagtReportId = pagtReportId;
    }

    public String getPagtReportMgr() {
        return pagtReportMgr;
    }

    public void setPagtReportMgr(String pagtReportMgr) {
        this.pagtReportMgr = pagtReportMgr;
    }

    public String getPagtCode() {
        return pagtCode;
    }

    public void setPagtCode(String pagtCode) {
        this.pagtCode = pagtCode;
    }

    public String getPagtSurname() {
        return pagtSurname;
    }

    public void setPagtSurname(String pagtSurname) {
        this.pagtSurname = pagtSurname;
    }

    public String getPagtOthname() {
        return pagtOthname;
    }

    public void setPagtOthname(String pagtOthname) {
        this.pagtOthname = pagtOthname;
    }

    public String getPagtGender() {
        return pagtGender;
    }

    public void setPagtGender(String pagtGender) {
        this.pagtGender = pagtGender;
    }

    public Date getPagtDob() {
        return pagtDob;
    }

    public void setPagtDob(Date pagtDob) {
        this.pagtDob = pagtDob;
    }

    public String getPagtAddrs() {
        return pagtAddrs;
    }

    public void setPagtAddrs(String pagtAddrs) {
        this.pagtAddrs = pagtAddrs;
    }

    public String getPagtMobile() {
        return pagtMobile;
    }

    public void setPagtMobile(String pagtMobile) {
        this.pagtMobile = pagtMobile;
    }

    public String getPagtMomoNo() {
        return pagtMomoNo;
    }

    public void setPagtMomoNo(String pagtMomoNo) {
        this.pagtMomoNo = pagtMomoNo;
    }

    public String getPagtStatus() {
        return pagtStatus;
    }

    public void setPagtStatus(String pagtStatus) {
        this.pagtStatus = pagtStatus;
    }

    public String getPagtAcctNo() {
        return pagtAcctNo;
    }

    public void setPagtAcctNo(String pagtAcctNo) {
        this.pagtAcctNo = pagtAcctNo;
    }

    public Date getPagtRegDt() {
        return pagtRegDt;
    }

    public void setPagtRegDt(Date pagtRegDt) {
        this.pagtRegDt = pagtRegDt;
    }

    public Date getPagtCrtDt() {
        return pagtCrtDt;
    }

    public void setPagtCrtDt(Date pagtCrtDt) {
        this.pagtCrtDt = pagtCrtDt;
    }

    public String getPagtEmail() {
        return pagtEmail;
    }

    public void setPagtEmail(String pagtEmail) {
        this.pagtEmail = pagtEmail;
    }

    public String getPagtPhoto() {
        return pagtPhoto;
    }

    public void setPagtPhoto(String pagtPhoto) {
        this.pagtPhoto = pagtPhoto;
    }

    public Date getPagtExitDt() {
        return pagtExitDt;
    }

    public void setPagtExitDt(Date pagtExitDt) {
        this.pagtExitDt = pagtExitDt;
    }

    public String getPagtMaritalSts() {
        return pagtMaritalSts;
    }

    public void setPagtMaritalSts(String pagtMaritalSts) {
        this.pagtMaritalSts = pagtMaritalSts;
    }

    public Date getPagtBfitStdt() {
        return pagtBfitStdt;
    }

    public void setPagtBfitStdt(Date pagtBfitStdt) {
        this.pagtBfitStdt = pagtBfitStdt;
    }

    public String getPagtEntSource() {
        return pagtEntSource;
    }

    public void setPagtEntSource(String pagtEntSource) {
        this.pagtEntSource = pagtEntSource;
    }

    public String getPagtUsed() {
        return pagtUsed;
    }

    public void setPagtUsed(String pagtUsed) {
        this.pagtUsed = pagtUsed;
    }

    public String getPagtLevel() {
        return pagtLevel;
    }

    public void setPagtLevel(String pagtLevel) {
        this.pagtLevel = pagtLevel;
    }

    public String getPagtHierarchy() {
        return pagtHierarchy;
    }

    public void setPagtHierarchy(String pagtHierarchy) {
        this.pagtHierarchy = pagtHierarchy;
    }

    public int getPagtSubbranchSysid() {
        return pagtSubbranchSysid;
    }

    public void setPagtSubbranchSysid(int pagtSubbranchSysid) {
        this.pagtSubbranchSysid = pagtSubbranchSysid;
    }

     @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
   
}

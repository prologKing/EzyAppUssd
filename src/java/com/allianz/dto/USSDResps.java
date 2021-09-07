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
public class USSDResps {
    private String action;
    private String[] menus;
    private String title;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String[] getMenus() {
        return menus;
    }

    public void setMenus(String[] menus) {
        this.menus = menus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
     @Override
    public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
    
}

package com.example.azure.ad.demo;

import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "Users")
public class Users {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    private String oid;
    private String ipAddr;
    private String name;
    private String uniqueName;


    
    public String getIpAddr() {
        return ipAddr;
    }

    public String getName() {
        return name;
    }

    public String getUniqueName() {
        return uniqueName;
    }


    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }




    
    
    
}

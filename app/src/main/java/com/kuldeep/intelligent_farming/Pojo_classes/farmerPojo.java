package com.kuldeep.intelligent_farming.Pojo_classes;

/**
 * Created by kuldeep on 28-10-2017.
 */

public class farmerPojo {
    private String fname,gende,dob,address,pincode,cid,sid,coid,mobile,email;

    public farmerPojo(String fname, String gende, String dob, String address, String pincode, String cid, String sid, String coid, String mobile, String email) {
        this.fname = fname;
        this.gende = gende;
        this.dob = dob;
        this.address = address;
        this.pincode = pincode;
        this.cid = cid;
        this.sid = sid;
        this.coid = coid;
        this.mobile = mobile;
        this.email = email;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setGende(String gende) {
        this.gende = gende;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public farmerPojo() {
    }

    public String getFname() {
        return fname;
    }

    public String getGende() {
        return gende;
    }

    public String getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    public String getPincode() {
        return pincode;
    }

    public String getCid() {
        return cid;
    }

    public String getSid() {
        return sid;
    }

    public String getCoid() {
        return coid;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setCoid(String coid) {
        this.coid = coid;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package com.kuldeep.intelligent_farming.Pojo_classes;

/**
 * Created by kuldeep on 28-10-2017.
 */

public class farmerPojo {
    private String fname,mobile,email,password,gender;

    public farmerPojo(String fname, String mobile, String email, String password, String gender) {
        this.fname = fname;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "farmerPojo{" +
                "fname='" + fname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

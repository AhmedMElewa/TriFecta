package com.trifecta.mada.trifecta13.other;

/**
 * Created by Mada on 2/15/2017.
 */

public class User {
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private String password;
    private String country;
    private String gender;
    private String joinedDate;
    private String SignUpIp;
    private String lastLogin;
    private String lastLoginIp;
    private String profilePic;
    private Boolean store;
    private String bDate;
    private String address;


    public User() {
    }

    public User(String id, String name, String phoneNumber, String email, String password, String country,
                String gender, String joinedDate, String signUpIp, String lastLogin, String lastLoginIp,
                String profilePic, Boolean store, String bDate, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.country = country;
        this.gender = gender;
        this.joinedDate = joinedDate;
        SignUpIp = signUpIp;
        this.lastLogin = lastLogin;
        this.lastLoginIp = lastLoginIp;
        this.profilePic = profilePic;
        this.store = store;
        this.bDate = bDate;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public String getSignUpIp() {
        return SignUpIp;
    }

    public void setSignUpIp(String signUpIp) {
        SignUpIp = signUpIp;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Boolean getStore() {
        return store;
    }

    public void setStore(Boolean store) {
        this.store = store;
    }

    public String getbDate() {
        return bDate;
    }

    public void setbDate(String bDate) {
        this.bDate = bDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}


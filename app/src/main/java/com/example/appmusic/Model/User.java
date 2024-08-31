package com.example.appmusic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id_user")
    @Expose
    private int idUser; // Sửa kiểu dữ liệu từ String thành int
    @SerializedName("displayname")
    @Expose
    private String displayname;

    @SerializedName("passwords")
    @Expose
    private String passwords;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("mail")
    @Expose
    private String mail;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("role")
    @Expose
    private int role;
    @SerializedName("active")
    @Expose
    private  int active;
    public int getIdUser() { // Sửa getter
        return idUser;
    }

    public void setIdUser(int idUser) { // Sửa setter
        this.idUser = idUser;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getPasswords() {
        return passwords;
    }

    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}

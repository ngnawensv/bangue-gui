/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.douwe.banque.data;


/**
 *
 * @author Maurice PENKA
 */
public class User {
    //Attributs
    int id;
    String userName;
    String passWord;
    RoleType role;

    public User() {
    }

    public User(String userName, String passWord, RoleType role) {
        this.userName = userName;
        this.passWord = passWord;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

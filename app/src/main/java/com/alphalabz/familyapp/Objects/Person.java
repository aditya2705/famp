package com.alphalabz.familyapp.Objects;


import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Rushabh on 19-12-2015.
 */
public class Person {

    private String uniqueID, firstName, fatherID, motherID, birthDate, email;
    private char gender;
    private String mobile, phone, address;
    private LatLng location;
    private int treeLevel;
    private ArrayList<Person> children;
    private LinearLayout childrenLayout;
    private LinearLayout selfViewLayout;

    public Person(String uniqueID, String firstName, String fatherID, String motherID, String birthDate, String email,
                  char gender, String mobile, String phone, String address, LatLng location, int treeLevel, ArrayList<Person> children, LinearLayout childrenLayout, LinearLayout selfViewLayout) {
        this.uniqueID = uniqueID;
        this.firstName = firstName;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.birthDate = birthDate;
        this.email = email;
        this.gender = gender;
        this.mobile = mobile;
        this.phone = phone;
        this.address = address;
        this.location = location;
        this.treeLevel = treeLevel;
        this.children = children;
        this.childrenLayout = childrenLayout;
        this.selfViewLayout = selfViewLayout;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
    }

    public ArrayList<Person> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Person> children) {
        this.children = children;
    }

    public LinearLayout getChildrenLayout() {
        return childrenLayout;
    }

    public void setChildrenLayout(LinearLayout childrenLayout) {
        this.childrenLayout = childrenLayout;
    }

    public LinearLayout getSelfViewLayout() {
        return selfViewLayout;
    }

    public void setSelfViewLayout(LinearLayout selfViewLayout) {
        this.selfViewLayout = selfViewLayout;
    }

    public boolean isParent(Person person)
    {
        return uniqueID.equals(person.getFatherID());
    }

    public boolean isChild(Person person)
    {
        return fatherID.equals(person.getUniqueID());
    }

    public void addChild(Person person)
    {
        children.add(person);
    }

    public int getChildCount()
    {
        return children.size();
    }

    public Person getChildAt(int index)
    {
        if(index < children.size())
            return children.get(index);
        return null;

    }
}

package com.alphalabz.familyapp.Objects;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rushabh on 19-12-2015.
 */
public class Person {

    private String name, familyName, fatherName, motherName, birthdate, email;
    private char gender;
    private ArrayList<Person> children;
    private String mobile, phone, address;
    private LatLng location;
    private int treeLevel;




    public Person(String name, String familyName, String fatherName, String motherName, String birthdate, String email, char gender, String mobile, String phone, String address, LatLng location) {
        setName(name);
        setAddress(address);
        setFamilyName(familyName);
        setFatherName(fatherName);
        setMotherName(motherName);
        setBirthdate(birthdate);
        setEmail(email);
        setGender(gender);
        setPhone(phone);
        setMobile(mobile);
        setLocation(location);
        children = new ArrayList<>();
    }


    public boolean isParent(Person person)
    {
        return name.equals(person.getFatherName());
    }

    public boolean isChild(Person person)
    {
        return fatherName.equals(person.getName());
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

    public int getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
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


}

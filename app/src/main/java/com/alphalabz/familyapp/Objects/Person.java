package com.alphalabz.familyapp.Objects;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import java.util.ArrayList;


public class Person {

    String unique_id,first_name,last_name,mother_id,father_id,primary_parent,gender,spouse_id,birth_date,marriage_date,death_date,
            mobile_number,landline,address,latitude,longitude,email,image_url;
    private int treeLevel;
    private ArrayList<Person> children = new ArrayList<>();
    private LinearLayout childrenLayout = null;

    public Person(String unique_id, String first_name, String last_name, String mother_id, String father_id, String primary_parent, String gender, String spouse_id, String birth_date, String marriage_date,
                  String death_date, String mobile_number, String landline, String address, String latitude, String longitude, String email, String image_url, int treeLevel) {
        this.unique_id = unique_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mother_id = mother_id;
        this.father_id = father_id;
        this.primary_parent = primary_parent;
        this.gender = gender;
        this.spouse_id = spouse_id;
        this.birth_date = birth_date;
        this.marriage_date = marriage_date;
        this.death_date = death_date;
        this.mobile_number = mobile_number;
        this.landline = landline;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
        this.image_url = image_url;
        this.treeLevel = treeLevel;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMother_id() {
        return mother_id;
    }

    public void setMother_id(String mother_id) {
        this.mother_id = mother_id;
    }

    public String getFather_id() {
        return father_id;
    }

    public void setFather_id(String father_id) {
        this.father_id = father_id;
    }

    public String getPrimary_parent() {
        return primary_parent;
    }

    public void setPrimary_parent(String primary_parent) {
        this.primary_parent = primary_parent;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSpouse_id() {
        return spouse_id;
    }

    public void setSpouse_id(String spouse_id) {
        this.spouse_id = spouse_id;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getMarriage_date() {
        return marriage_date;
    }

    public void setMarriage_date(String marriage_date) {
        this.marriage_date = marriage_date;
    }

    public String getDeath_date() {
        return death_date;
    }

    public void setDeath_date(String death_date) {
        this.death_date = death_date;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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

    public RelativeLayout getPersonLayout() {
        return personLayout;
    }

    public void setPersonLayout(RelativeLayout personLayout) {
        this.personLayout = personLayout;
    }

    private RelativeLayout personLayout;


    public LinearLayout getChildrenLayout() {
        return childrenLayout;
    }

    public void setChildrenLayout(LinearLayout childrenLayout) {
        this.childrenLayout = childrenLayout;
    }

    public boolean isParent(Person person)
    {
        return unique_id.equals(person.getFather_id());
    }

    public boolean isChild(Person person)
    {
        return father_id.equals(person.getUnique_id());
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

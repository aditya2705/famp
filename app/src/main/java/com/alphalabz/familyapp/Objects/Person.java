package com.alphalabz.familyapp.Objects;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Person implements Serializable{

    String unique_id,generation,title,first_name,middle_name,last_name,nick_name
            ,gender,in_law,mother_id,mother_name,father_id,father_name,spouse_id,spouse_name,birth_date,marriage_date,death_date,
            mobile_number,alternate_number,residence_number,email1,email2,address_1,address_2,city,state_country,pincode
            ,designation,company,industry_special,image_url;

    int treeLevel;

    public Person(String unique_id, String generation, String title, String first_name, String middle_name, String last_name,
                  String nick_name, String gender, String in_law, String mother_id, String mother_name, String father_id,
                  String father_name, String spouse_id, String spouse_name, String birth_date, String marriage_date,
                  String death_date, String mobile_number, String alternate_number, String residence_number,
                  String email1, String email2, String address_1, String address_2, String city, String state_country,
                  String pincode, String designation, String company, String industry_special, String image_url, int treeLevel) {
        this.unique_id = unique_id;
        this.generation = generation;
        this.title = title;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.nick_name = nick_name;
        this.gender = gender;
        this.in_law = in_law;
        this.mother_id = mother_id;
        this.mother_name = mother_name;
        this.father_id = father_id;
        this.father_name = father_name;
        this.spouse_id = spouse_id;
        this.spouse_name = spouse_name;
        this.birth_date = birth_date;
        this.marriage_date = marriage_date;
        this.death_date = death_date;
        this.mobile_number = mobile_number;
        this.alternate_number = alternate_number;
        this.residence_number = residence_number;
        this.email1 = email1;
        this.email2 = email2;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.city = city;
        this.state_country = state_country;
        this.pincode = pincode;
        this.designation = designation;
        this.company = company;
        this.industry_special = industry_special;
        this.image_url = image_url;
        this.treeLevel = treeLevel;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIn_law() {
        return in_law;
    }

    public void setIn_law(String in_law) {
        this.in_law = in_law;
    }

    public String getMother_id() {
        return mother_id;
    }

    public void setMother_id(String mother_id) {
        this.mother_id = mother_id;
    }

    public String getMother_name() {
        return mother_name;
    }

    public void setMother_name(String mother_name) {
        this.mother_name = mother_name;
    }

    public String getFather_id() {
        return father_id;
    }

    public void setFather_id(String father_id) {
        this.father_id = father_id;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getSpouse_id() {
        return spouse_id;
    }

    public void setSpouse_id(String spouse_id) {
        this.spouse_id = spouse_id;
    }

    public String getSpouse_name() {
        return spouse_name;
    }

    public void setSpouse_name(String spouse_name) {
        this.spouse_name = spouse_name;
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

    public String getAlternate_number() {
        return alternate_number;
    }

    public void setAlternate_number(String alternate_number) {
        this.alternate_number = alternate_number;
    }

    public String getResidence_number() {
        return residence_number;
    }

    public void setResidence_number(String residence_number) {
        this.residence_number = residence_number;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState_country() {
        return state_country;
    }

    public void setState_country(String state_country) {
        this.state_country = state_country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIndustry_special() {
        return industry_special;
    }

    public void setIndustry_special(String industry_special) {
        this.industry_special = industry_special;
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
}

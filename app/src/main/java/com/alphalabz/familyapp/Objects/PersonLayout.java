package com.alphalabz.familyapp.Objects;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class PersonLayout extends Person{

    private ArrayList<PersonLayout> children = new ArrayList<>();
    private LinearLayout childrenLayout = null;
    private boolean childrenOpened = false;

    public PersonLayout(String unique_id, String generation, String title, String first_name,
                        String middle_name, String last_name, String nick_name, String gender,
                        String in_law, String mother_id, String mother_name, String father_id,
                        String father_name, String spouse_id, String spouse_name, String birth_date,
                        String marriage_date, String death_date, String mobile_number,
                        String alternate_number, String residence_number, String email1,
                        String email2, String address_1, String address_2, String city,
                        String state_country, String pincode, String designation, String company,
                        String industry_special, String image_url, int treeLevel) {

        super(unique_id, generation, title, first_name, middle_name, last_name, nick_name,
                gender, in_law, mother_id, mother_name, father_id, father_name, spouse_id,
                spouse_name, birth_date, marriage_date, death_date, mobile_number, alternate_number,
                residence_number, email1, email2, address_1, address_2, city, state_country, pincode,
                designation, company, industry_special, image_url, treeLevel);
    }

    public PersonLayout(Person p){
        super(p.getUnique_id(), p.getGeneration(), p.getTitle(), p.getFirst_name(), p.getMiddle_name(), p.getLast_name(),
                p.getNick_name(), p.getGender(), p.getIn_law(), p.getMother_id(), p.getMother_name(), p.getFather_id(),
                p.getFather_name(), p.getSpouse_id(), p.getSpouse_name(), p.getBirth_date(), p.getMarriage_date(), p.getDeath_date(),
                p.getMobile_number(), p.getAlternate_number(), p.getResidence_number(), p.getEmail1(), p.getEmail2(),
                p.getAddress_1(), p.getAddress_2(), p.getCity(), p.getState_country(), p.getPincode(), p.getDesignation(),
                p.getCompany(), p.getIndustry_special(), p.getImage_url(), p.getTreeLevel());
    }

    public ArrayList<PersonLayout> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<PersonLayout> children) {
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

    public boolean isParent(PersonLayout person)
    {
        return unique_id.equals(person.getFather_id());
    }

    public boolean isChild(PersonLayout person)
    {
        return father_id.equals(person.getUnique_id());
    }

    public void addChild(PersonLayout person)
    {
        children.add(person);
    }

    public int getChildCount()
    {
        return children.size();
    }

    public PersonLayout getChildAt(int index)
    {
        if(index < children.size())
            return children.get(index);
        return null;

    }

    public boolean isChildrenOpened() {
        return childrenOpened;
    }

    public void setChildrenOpened(boolean childrenOpened) {
        this.childrenOpened = childrenOpened;
    }
}


package com.example.pbl_project;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mName;
    private String mImageUrl;
    private  String mowner;
    private String mprice;
    private  String mkey;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String imageUrl,String owner_name,String price) {
        if (name.trim().equals("")) {
            name = "";
        }
        if (owner_name.trim().equals("")) {
            owner_name = "";
        }
        if (price.trim().equals("")) {
            price = "";
        }
        mName = name;
        mImageUrl = imageUrl;
        mowner =  owner_name;
        mprice = price;
    }



    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
    public String getMowner() {
        return mowner;
    }

    public void setMowner(String mowner) {
        this.mowner = mowner;
    }

    public String getMprice() {
        return mprice;
    }

    public void setMprice(String mprice) {
        this.mprice = mprice;
    }
    @Exclude
    public String getMkey() {
        return mkey;
    }
    @Exclude
    public void setMkey(String mkey) {
        this.mkey = mkey;
    }
}
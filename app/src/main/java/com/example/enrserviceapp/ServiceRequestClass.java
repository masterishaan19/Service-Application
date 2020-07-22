package com.example.enrserviceapp;

class ServiceRequestClass{
    private String name;
    private String mobileNumber;
    private String Address;
    private int image;
    private String worktype;
    private String email;
    public ServiceRequestClass(String name, String mobileNumber, String address, String worktype, String email) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.Address = address;
        this.worktype = worktype;
        this.email = email;
    }
    ServiceRequestClass(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(int image) {
        this.image = image;
    }
    public int getImage() {
        if(worktype.equals("Electrician"))
            return R.drawable.electrician;
        else if(worktype.equals("Plumbing"))
            return R.drawable.plumber;
        else if(worktype.equals("Gardener"))
            return R.drawable.farm;
        else if(worktype.equals("Generic Activities"))
            return R.drawable.generic;
        else
            return R.drawable.other;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMobileNumber() {
        return mobileNumber;
    }
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }
    public String getWorktype() {
        return worktype;
    }
    public void setWorktype(String worktype) {
        this.worktype = worktype;
    }
}

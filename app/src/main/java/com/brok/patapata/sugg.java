package com.brok.patapata;

public class sugg {
    private String cname;
    private String cemail;
    private String mobileNo;

    public sugg(String cname, String cemail, String mobileNo) {
        this.cname = cname;
        this.cemail = cemail;
        this.mobileNo = mobileNo;
    }

    public String getCname() {
        return cname;
    }

    public String getCemail() {
        return cemail;
    }

    public String getMobileNo() {
        return mobileNo;
    }
}

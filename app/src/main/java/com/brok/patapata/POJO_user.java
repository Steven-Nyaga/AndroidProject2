package com.brok.patapata;

import android.widget.EditText;

public class POJO_user {
    public String status;
    public String inputEmail;
    public String id;

    public POJO_user(){

    }

    public POJO_user(String status, String inputEmail, String id) {
        this.inputEmail = inputEmail;
        this.status = status;
        this.id= id;
    }


}

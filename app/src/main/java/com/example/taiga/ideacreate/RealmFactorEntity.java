package com.example.taiga.ideacreate;

import io.realm.RealmObject;

/**
 * Created by taiga on 2017/09/15.
 */

public class RealmFactorEntity extends RealmObject {

    private String text;

    public String getText(){
        return this.text;
    }

    public void setText(String text){
        this.text=text;
    }

}

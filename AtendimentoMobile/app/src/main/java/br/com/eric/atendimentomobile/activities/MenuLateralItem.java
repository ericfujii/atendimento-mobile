package br.com.eric.atendimentomobile.activities;

import android.content.Intent;

/**
 * Created by eric on 26-02-2015.
 */
public class MenuLateralItem {
    private String title;
    private int icon;
    private Intent intent;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public MenuLateralItem(){}

    public MenuLateralItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public MenuLateralItem(String title, int icon, String count, Intent intent){
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = true;
        this.count = count;
        this.intent = intent;
    }

    public MenuLateralItem(String title, int icon, Intent intent){
        this.title = title;
        this.isCounterVisible = false;
        this.icon = icon;
        this.intent = intent;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public String getCount(){
        return this.count;
    }

    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

}

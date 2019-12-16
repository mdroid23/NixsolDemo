package com.nixsol.mahesh.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FactResponse {

    @SerializedName("title")
    @Expose
    private String name;
    @SerializedName("rows")
    @Expose
    private ArrayList<Fact> factList;

    public FactResponse(String name, ArrayList<Fact> factList) {
        this.name = name;
        this.factList = factList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Fact> getFactList() {
        return factList;
    }

    public void setFactList(ArrayList<Fact> factList) {
        this.factList = factList;
    }
}

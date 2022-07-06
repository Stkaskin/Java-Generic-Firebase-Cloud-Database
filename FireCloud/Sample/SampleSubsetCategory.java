package com.stkaskin.dininghallsurvey.FireCloud.Sample;

import com.stkaskin.dininghallsurvey.FireCloud.IFirebase;

public class SampleSubsetCategory  implements IFirebase
{    String id;


    int displayRank;
    String imageid;
    String name;
    int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public int getDisplayRank() {
        return displayRank;
    }

    public void setDisplayRank(int displayRank) {
        this.displayRank = displayRank;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String TableName() {
        return "Survey/Category";
    }


}

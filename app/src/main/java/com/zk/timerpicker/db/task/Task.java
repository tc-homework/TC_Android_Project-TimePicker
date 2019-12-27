package com.zk.timerpicker.db.task;

import org.litepal.crud.LitePalSupport;

public class Task extends LitePalSupport {
    private int id;
    private String title;
    private String content;
    private String place;
    private long createTime;
    private long ddlTime;

    private Boolean isAWholeDayTask;
    private Boolean isCompleted;

    public int getId() {
        return id;
    }

    public Task setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Task setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Task setContent(String content) {
        this.content = content;
        return this;
    }

    public String getPlace() {
        return place;
    }

    public Task setPlace(String place) {
        this.place = place;
        return this;
    }



    public long getCreateTime() {
        return createTime;
    }

    public Task setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public long getDdlTime() {
        return ddlTime;
    }

    public Task setDdlTime(long ddlTime) {
        this.ddlTime = ddlTime;
        return this;
    }

    public Boolean getAWholeDayTask() {
        return isAWholeDayTask;
    }

    public Task setAWholeDayTask(Boolean AWholeDayTask) {
        isAWholeDayTask = AWholeDayTask;
        return this;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public Task setCompleted(Boolean completed) {
        isCompleted = completed;
        return this;
    }

    public String toString(){
        return (
                title + content + place + String.valueOf(createTime) + String.valueOf(ddlTime)
                );
    }


}

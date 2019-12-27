package com.zk.timerpicker.present;

import android.util.Log;

import com.zk.timerpicker.db.task.Task;
import com.zk.timerpicker.db.task.TaskHelper;
import com.zk.timerpicker.util.time.TimeUtil;

import androidx.annotation.NonNull;

public class TaskDetailPersent {
    private String title;
    private String place;
    private Boolean isAWholeDayTask;
    private Boolean isDone;
    private long ddlTime;
    private String content;
    private int id;
    private Task taskInstance;

    public TaskDetailPersent() {
        this.title = "";
        this.isAWholeDayTask = false;
        this.isDone = false;
        this.ddlTime = TimeUtil.getNowTime();
        this.content = "";
        taskInstance = TaskHelper.createTask(title, content, ddlTime, isAWholeDayTask, isDone);
        id = taskInstance.getId();
    }

    public TaskDetailPersent(int id) {
        this.id = id;
        this.taskInstance = TaskHelper.getTaskById(id);
        this.title = taskInstance.getTitle();
        this.content = taskInstance.getContent();
        this.place = taskInstance.getPlace();
        this.isAWholeDayTask = taskInstance.getAWholeDayTask();
        this.isDone = taskInstance.getCompleted();
        this.ddlTime = taskInstance.getDdlTime();
    }

    public void save() {
        taskInstance.setTitle(title);
        taskInstance.setPlace(place);
        taskInstance.setContent(content);
        taskInstance.setCompleted(isDone);
        taskInstance.setDdlTime(ddlTime);
        taskInstance.setAWholeDayTask(isAWholeDayTask);
        taskInstance.save();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public TaskDetailPersent setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPlace() {
        return place;
    }

    public TaskDetailPersent setPlace(String place) {
        this.place = place;
        return this;
    }

    public Boolean getAWholeDayTask() {
        return isAWholeDayTask;
    }

    public TaskDetailPersent setAWholeDayTask(Boolean AWholeDayTask) {
        isAWholeDayTask = AWholeDayTask;
        return this;
    }

    public Boolean getDone() {
        return isDone;
    }

    public TaskDetailPersent setDone(Boolean done) {
        isDone = done;
        return this;
    }

    public long getDdlTime() {
        return ddlTime;
    }

    public TaskDetailPersent setDdlTime(long ddlTime) {
        this.ddlTime = ddlTime;
        return this;
    }

    public String getContent() {
        return content;
    }

    public TaskDetailPersent setContent(String content) {
        this.content = content;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return(
                "标题: " + (title.equals("") ? "空" : title) + "\n"
                +"地点: " + (place.equals("") ? "空" : place) + "\n"
                +"创建日期: " + TimeUtil.timeToString("yyyy-MM-dd hh:mm:ss", taskInstance.getCreateTime()) + "\n"
                +"截止日期: " + TimeUtil.timeToString("yyyy-MM-dd hh:mm:ss", taskInstance.getDdlTime()) + "\n"
                +"详细: " + content
                );
    }
}

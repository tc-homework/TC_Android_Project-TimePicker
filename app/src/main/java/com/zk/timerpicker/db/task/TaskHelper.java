package com.zk.timerpicker.db.task;

import android.util.Log;

import com.zk.timerpicker.util.time.TimeUtil;

import org.litepal.LitePal;

import java.util.List;


public class TaskHelper {
    public static List<Task> getAllTasks() {
        List<Task> tt = LitePal.findAll(Task.class);
        return tt;
    };

    public static List<Task> getTasksInAMounth(String year, String mounth) {
        return getTasksBetweenTime(
                TimeUtil.getMounthStartTime(year, mounth),
                TimeUtil.getMounthEndTime(year, mounth)
        );
    }

    public static List<Task> getTasksInADay(String year, String mounth, String day) {
        return getTasksBetweenTime(
                TimeUtil.getDayStartTime(year, mounth, day),
                TimeUtil.getDayEndTime(year, mounth, day)
        );
    }

    public static List<Task> getTasksBetweenTime(long startTime, long endTime) {
        return LitePal.where(
                "ddlTime > ? and ddlTime < ?",
                Long.toString(startTime),
                Long.toString(endTime)
        ).order("ddlTime").find(Task.class);
    };

    public static Task getTaskById(int id){
        return LitePal.find(Task.class, id);
    }

    public static Task createTask(
            String title,
            String content,
            long ddlTime,
            Boolean isAWholeDayTask,
            Boolean isCompleted
    ){
        Task task = new Task()
                .setTitle(title)
                .setContent(content)
                .setCreateTime(TimeUtil.getNowTime())
                .setDdlTime(ddlTime)
                .setAWholeDayTask(isAWholeDayTask)
                .setCompleted(isCompleted);
        return task;
    }


    public static void delById(int id) {
        LitePal.delete(Task.class, id);
    }


}

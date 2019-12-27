package com.zk.timerpicker.util.http;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.zk.timerpicker.db.task.Task;
import com.zk.timerpicker.db.task.TaskHelper;
import com.zk.timerpicker.gson.GsonTempClassOne;
import com.zk.timerpicker.gson.GsonTempClassTwo;
import com.zk.timerpicker.present.MainActivityPersent;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SysncTaskHelper {
    private List<Integer> taskFormServerList;
    Runnable r;

    public SysncTaskHelper(Runnable r_) {
        taskFormServerList = new ArrayList<Integer>();
        r = r_;
    }

    public void start() {
        pullTaskList();
    }

    private void pullTaskList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://47.96.159.162/list").build();
                    String responseData = client.newCall(request).execute().body().string();
                    Gson gson = new Gson();
                    GsonTempClassTwo obj = gson.fromJson(responseData, GsonTempClassTwo.class);
                    taskFormServerList = obj.getData();
                } catch (Exception e) {
                    Log.e("M - Net Error", "error");
                } finally {
                    Sysnc();
                }
            }
        }).run();

    }

    public void Sysnc() {
        List<Integer> tasksFromLocalList = new ArrayList<Integer>();
        for(Task t : TaskHelper.getAllTasks()){
            tasksFromLocalList.add(t.getId());
        }

        List<Integer> needToPull = new ArrayList<Integer>();
        List<Integer> needToPush = new ArrayList<Integer>();

        for(int l_id : tasksFromLocalList) {
            if(!taskFormServerList.contains(l_id)) {
                push(l_id);
            }
        }
        for(int s_id : taskFormServerList) {
            if(!tasksFromLocalList.contains(s_id)) {
                pull(s_id);
            }
        }
        r.run();
    }

    public void push(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new Gson();
                    String tOthers = gson.toJson(TaskHelper.getTaskById(id));
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://47.96.159.162/upload?id=" + String.valueOf(id) + "&others=" + tOthers)
                            .build();
                    client.newCall(request).execute();
                } catch(Exception e) {
                    Log.e("M -Net", "push error");
                }
            }
        }).run();
    }

    public void pull (final int id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://47.96.159.162/get?id=" + String.valueOf(id) ).build();
                    String responseData = client.newCall(request).execute().body().string();
                    Gson gson = new Gson();
                    Task t = gson.fromJson(responseData, Task.class);
                    t.save();
                } catch (Exception e) {
                    Log.e("M - Net", "pull error");
                }
            }
        }).run();
    }
}

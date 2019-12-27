package com.zk.timerpicker.present;

import android.util.Log;

import com.zk.timerpicker.db.task.Task;
import com.zk.timerpicker.db.task.TaskHelper;
import com.zk.timerpicker.util.time.TimeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivityPersent {
    private String year;
    private String mounth;
    private int taskShowType;
    public static int SHOW_TODO_TASK = 0;
    public static int SHOW_DONE_TASK = 1;

    private List<List<Task>> todoTaskCardList;
    private List<List<Task>> doneTaskCardList;

    public MainActivityPersent(String year, String mounth, int taskShowType) {
        this.year = year;
        this.mounth = mounth;
        this.taskShowType = taskShowType;
        todoTaskCardList = new ArrayList<List<Task>>();
        doneTaskCardList = new ArrayList<List<Task>>();
    }

    public MainActivityPersent() {
        long nowTime = TimeUtil.getNowTime();
        this.year = TimeUtil.timeToString("yyyy", nowTime);
        this.mounth = TimeUtil.timeToString("MM", nowTime);
        this.taskShowType = SHOW_TODO_TASK;
        todoTaskCardList = new ArrayList<List<Task>>();
        doneTaskCardList = new ArrayList<List<Task>>();
    }

    public void updateTasks(){
        todoTaskCardList.clear();
        doneTaskCardList.clear();
        for(int d = TimeUtil.getDaysByYearMonth(year, mounth); d > 0; d--) {
            List<Task> tasks = TaskHelper.getTasksInADay(year, mounth, TimeUtil.DayToString(d));
            Log.d("M - tasks count in a day", String.valueOf(d * 100 + tasks.size()));
            if(tasks.size() == 0) {
                continue;
            }
            List<Task> todoTasks = new ArrayList<Task>();
            List<Task> doneTasks = new ArrayList<Task>();

            for(Task t : tasks) {
                if(!t.getCompleted()) {
                    todoTasks.add(t);
                } else {
                    doneTasks.add(t);
                }
            }
            if(todoTasks.size() != 0) {
                todoTaskCardList.add(todoTasks);
            }
            if(doneTasks.size() != 0) {
                doneTaskCardList.add(doneTasks);
            }
        }
        Log.d("M - todoTaskCardList Count", String.valueOf(todoTaskCardList.size()));
        Log.d("M - doneTaskCardList Count", String.valueOf(doneTaskCardList.size()));
    }

    public List<List<Task>> getTaskCardList() {
        return taskShowType == SHOW_TODO_TASK ? todoTaskCardList : doneTaskCardList;
    }

    public String getYear() {
        return year;
    }

    public MainActivityPersent setYear(String year) {
        this.year = year;
        return this;
    }

    public String getMounth() {
        return mounth;
    }

    public MainActivityPersent setMounth(String mounth) {
        this.mounth = mounth;
        return this;
    }

    public int getTaskShowType() {
        return taskShowType;
    }

    public MainActivityPersent setTaskShowType(int taskShowType) {
        this.taskShowType = taskShowType;
        return this;
    }

    public List<List<Task>> getTodoTaskCardList() {
        return todoTaskCardList;
    }

    public MainActivityPersent setTodoTaskCardList(List<List<Task>> todoTaskCardList) {
        this.todoTaskCardList = todoTaskCardList;
        return this;
    }

    public List<List<Task>> getDoneTaskCardList() {
        return doneTaskCardList;
    }

    public MainActivityPersent setDoneTaskCardList(List<List<Task>> doneTaskCardList) {
        this.doneTaskCardList = doneTaskCardList;
        return this;
    }

}

package com.zk.timerpicker.present.adapter;

import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zk.timerpicker.MainActivity;
import com.zk.timerpicker.R;
import com.zk.timerpicker.db.task.Task;
import com.zk.timerpicker.present.TaskDetailPersent;
import com.zk.timerpicker.ui.TaskDetailActivity;
import com.zk.timerpicker.util.time.TimeUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    public List<Task> mTaskList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle;
        TextView taskPlace;
        TextView taskDdlTime;
        ImageView divide;
        View view;
        public ViewHolder(View view) {
            super(view);
            taskTitle = (TextView) view.findViewById(R.id.taskitem_title);
            taskPlace = (TextView) view.findViewById(R.id.taskitem_place);
            taskDdlTime = (TextView) view.findViewById(R.id.taskitem_ddltime);
            divide = (ImageView) view.findViewById(R.id.taskitem_divider_line);
            this.view = view;
        }
    }
    public TaskAdapter(List<Task> taskList) {
        mTaskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_task, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Task task = mTaskList.get(position);
        holder.taskTitle.setText(task.getTitle());
        holder.taskPlace.setText(task.getPlace());
        if(position + 1 == getItemCount()) {
            holder.divide.setVisibility(View.INVISIBLE);
        }
        if(task.getAWholeDayTask()){
            holder.taskDdlTime.setText("全天任务");
        } else{
            holder.taskDdlTime.setText(TimeUtil.timeToString("HH:mm", task.getDdlTime()));
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TaskDetailActivity.class);
                intent.putExtra("taskId", task.getId());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }
}

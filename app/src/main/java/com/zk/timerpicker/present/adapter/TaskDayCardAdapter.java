package com.zk.timerpicker.present.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zk.timerpicker.R;
import com.zk.timerpicker.db.task.Task;
import com.zk.timerpicker.util.time.TimeUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TaskDayCardAdapter extends RecyclerView.Adapter<TaskDayCardAdapter.ViewHolder> {
    public List<List<Task>> mTaskCardList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskCardDate;
        TextView taskCardRemainningCount;
        RecyclerView taskCardRecycler;
        public ViewHolder(View view) {
            super(view);
            taskCardDate = (TextView) view.findViewById(R.id.task_day_card_date);
            taskCardRemainningCount = (TextView) view.findViewById(R.id.task_day_card_remaining);
            taskCardRecycler = (RecyclerView) view.findViewById(R.id.task_day_card_recycler);

            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
            taskCardRecycler.setLayoutManager(layoutManager);

        }
    }
    public TaskDayCardAdapter(List<List<Task>> taskCardList) {
        mTaskCardList = taskCardList;
    }

    @NonNull
    @Override
    public TaskDayCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_tasks_card, parent, false);
        TaskDayCardAdapter.ViewHolder holder = new TaskDayCardAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDayCardAdapter.ViewHolder holder, int position) {
        List<Task> tasks = mTaskCardList.get(position);
        holder.taskCardDate.setText( TimeUtil.timeToString("MM-dd", tasks.get(0).getDdlTime()) + "  " + TimeUtil.timeToWeek(tasks.get(0).getDdlTime()));
        holder.taskCardRemainningCount.setText(Integer.toString(tasks.size()));

        TaskAdapter adapter = new TaskAdapter(tasks);
        holder.taskCardRecycler.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mTaskCardList.size();
    }
}
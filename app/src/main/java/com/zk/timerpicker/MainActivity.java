package com.zk.timerpicker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.zk.timerpicker.gson.GsonTempClassOne;
import com.zk.timerpicker.present.MainActivityPersent;
import com.zk.timerpicker.present.adapter.TaskDayCardAdapter;
import com.zk.timerpicker.ui.LoginActivity;
import com.zk.timerpicker.ui.TaskDetailActivity;
import com.zk.timerpicker.util.http.SysncTaskHelper;
import com.zk.timerpicker.util.time.TimeUtil;
import com.zk.timerpicker.util.ui.uiUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Button homeButton;
    private Button changeTodoTaskButton;
    private Button changeDoneTaskButton;
    private MainActivityPersent mPersent;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private TextView headTitle;
    private TimePickerView pvTime;
    private Button avatarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decoView = getWindow().getDecorView();
            decoView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);


        registerDoms();

        registerDomListeners();


        updateSubTitle();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateRecycleView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateRecycleView();
        try {
            SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
            String userName;
            if(pref.getBoolean("is_login", false)){
                userName = pref.getString("user_name", "未登录");

            } else {
                userName = "未登录";
            }
            ((TextView) (
                    ((NavigationView) findViewById(R.id.main_nav_view)).getHeaderView(0)
            ).findViewById(R.id.nav_head_username_text)).setText(userName);
        } catch (Exception e) {
            Log.e("W - error", "fuckd up");
        }
    }

    public void registerDoms() {
        mPersent = new MainActivityPersent();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        recyclerView = (RecyclerView) findViewById(R.id.task_day_cards_recycler);
        homeButton = (Button) findViewById(R.id.main_title_home_btn);
        changeTodoTaskButton = (Button) findViewById(R.id.main_tab_todo_task);
        changeDoneTaskButton = (Button) findViewById(R.id.main_tab_done_task);
        fab = findViewById(R.id.fab);
        headTitle = (TextView) findViewById(R.id.main_head_title);
        avatarBtn = (Button) (
                ((NavigationView) findViewById(R.id.main_nav_view)).getHeaderView(0)
        ).findViewById(R.id.nav_avatar_btn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pvTime = new TimePickerBuilder(MainActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String year = TimeUtil.dateToString("yyyy", date);
                String mounth = TimeUtil.dateToString("MM", date);
                mPersent.setYear(year).setMounth(mounth);
                headTitle.setText(year + "." + mounth);
                updateRecycleView();
            }
        }).setRangDate(
                TimeUtil.newCalendarByYearMounthDay(2017, 1, 1),
                TimeUtil.newCalendarByYearMounthDay(2026, 1, 1)
        ).setType(new boolean[]{true, true, false, false, false, false})
                .setDate(Calendar.getInstance())
                .build();

        updateRecycleView();


    }

    public void registerDomListeners() {
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
                intent.putExtra("taskId", 0);
                startActivity(intent);
            }
        });

        changeTodoTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPersent.setTaskShowType(MainActivityPersent.SHOW_TODO_TASK);
                uiUtil.reverseChageButtonArrs(changeTodoTaskButton, changeDoneTaskButton);
                updateRecycleViewWithOutUpdateData();
            }
        });

        changeDoneTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPersent.setTaskShowType(MainActivityPersent.SHOW_DONE_TASK);
                uiUtil.reverseChageButtonArrs(changeDoneTaskButton, changeTodoTaskButton);
                updateRecycleViewWithOutUpdateData();
            }
        });

        headTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
//                SysncTaskHelper helper = new SysncTaskHelper(new Runnable() {
//                    @Override
//                    public void run() {
//                        updateRecycleView();
//                    }
//                });
//                helper.start();
            }
        });


        avatarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



    }

    public void updateRecycleView() {
        mPersent.updateTasks();
        updateSubTitle();
        Log.d("M - Update Main Recycler. Card", Integer.toString(mPersent.getTaskCardList().size()));
        recyclerView.setAdapter(new TaskDayCardAdapter(mPersent.getTaskCardList()));
    }

    public void updateRecycleViewWithOutUpdateData() {
        updateRecycleView();
        //recyclerView.setAdapter(new TaskDayCardAdapter(mPersent.getTaskCardList()));
    }

    public void updateSubTitle() {
        int todoTaskCount = mPersent.getTodoTaskCardList().size();
        int doneTaskCount = mPersent.getDoneTaskCardList().size();
        int allTaskCount = todoTaskCount + doneTaskCount;
        ((TextView) findViewById(R.id.main_sub_title_all_task_count)).setText(Integer.toString(allTaskCount));
        ((TextView) findViewById(R.id.main_sub_title_remain_task_count)).setText(Integer.toString(todoTaskCount));
        ((TextView) findViewById(R.id.main_sub_title_done_task_count)).setText(Integer.toString(doneTaskCount));
    }


}

package com.zk.timerpicker.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;
import com.zk.timerpicker.R;
import com.zk.timerpicker.db.task.TaskHelper;
import com.zk.timerpicker.present.TaskDetailPersent;
import com.zk.timerpicker.util.time.TimeUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import jp.wasabeef.richeditor.RichEditor;

public class TaskDetailActivity extends AppCompatActivity {
    private Button backBtn;
    private Button saveBtn;
    private EditText titleEditText;
    private EditText placeEditText;
    private CheckBox wholeDayTaskCheckBox;
    private CheckBox taskDoneChaeckBox;
    private Button timeButton;
    private TimePickerView pvTime;
    private TimePickerBuilder pvTimeBuilder;
    private TaskDetailPersent taskDetailPersent;
    private RichEditor richEditor;
    private Button menuImageBtn;
    private Button menuShareBtn;
    private Button menuDelBtn;
    final private int REQUEST_CODE_CHOOSE = 234;

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
        setContentView(R.layout.activity_task_detail);

        registerDoms();

        initializePvTime();

        registerListeners();

        Intent intent = getIntent();
        Boolean isNewTask = (intent.getIntExtra("taskId", -1) == 0);
        if(isNewTask) {
            taskDetailPersent = new TaskDetailPersent();
        } else {
            taskDetailPersent = new TaskDetailPersent(intent.getIntExtra("taskId", -1));
            placeEditText.setText(taskDetailPersent.getPlace());
            titleEditText.setText(taskDetailPersent.getTitle());
            wholeDayTaskCheckBox.setChecked(taskDetailPersent.getAWholeDayTask());
            taskDoneChaeckBox.setChecked(taskDetailPersent.getDone());
            richEditor.setHtml(taskDetailPersent.getContent());
        }
    }


    public void registerDoms() {
        backBtn = (Button) findViewById(R.id.task_detail_back);
        saveBtn = (Button) findViewById(R.id.task_detail_save);
        titleEditText = (EditText) findViewById(R.id.task_detail_title_editText);
        placeEditText = (EditText) findViewById(R.id.task_detail_place_editText);
        wholeDayTaskCheckBox = (CheckBox) findViewById(R.id.task_detail_whole_day_task_checkbox);
        taskDoneChaeckBox = (CheckBox) findViewById(R.id.task_detail_task_done_checkbox);
        timeButton = (Button) findViewById(R.id.task_detail_time_open_btn);
        richEditor = (RichEditor) findViewById(R.id.task_detail_rich_editor);
        menuDelBtn = (Button) findViewById(R.id.task_detail_menu_delete);
        menuImageBtn = (Button) findViewById(R.id.task_detail_menu_image);
        menuShareBtn = (Button) findViewById(R.id.task_detail_menu_share);

        richEditor.setEditorFontSize(22);
    }

    public void initializePvTime() {
        pvTimeBuilder = new TimePickerBuilder(TaskDetailActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                taskDetailPersent.setDdlTime(date.getTime());
                Toast.makeText(
                        TaskDetailActivity.this,
                        "Set Time: " + TimeUtil.dateToString("yyyy-MM-dd hh:mm:ss",
                                date
                        ), Toast.LENGTH_SHORT).show();
            }
        }).setRangDate(
                TimeUtil.newCalendarByYearMounthDay(2019, 1, 1),
                TimeUtil.newCalendarByYearMounthDay(2040, 1, 1)
        ).setType(new boolean[]{false, true, true, true, true, true})
                .setDate(Calendar.getInstance());
        pvTime = pvTimeBuilder.build();
    }

    public void registerListeners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskDetailActivity.this.finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskDetailPersent.setTitle(titleEditText.getText().toString());
                taskDetailPersent.setContent(richEditor.getHtml());
                taskDetailPersent.setPlace(placeEditText.getText().toString());
                taskDetailPersent.save();
                TaskDetailActivity.this.finish();
            }
        });


        wholeDayTaskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    pvTimeBuilder.setType(new boolean[]{false, true, true, false, false, false});
                } else {
                    pvTimeBuilder.setType(new boolean[]{false, true, true, true, true, true});
                }
                pvTime = pvTimeBuilder.build();

//                timeButton.setEnabled(!isChecked);
//                timeButton.setTextColor(
//                        isChecked
//                                ? TaskDetailActivity.this.getColor(R.color.mainThemeColorGray)
//                                : TaskDetailActivity.this.getColor(R.color.mainThemeColor)
//                );
                taskDetailPersent.setAWholeDayTask(isChecked);
            }
        });

        taskDoneChaeckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                taskDetailPersent.setDone(b);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });

        menuDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmDialog = new AlertDialog.Builder(TaskDetailActivity.this);
                confirmDialog.setTitle("确认");
                confirmDialog.setMessage("是否删除");
                confirmDialog.setPositiveButton("确定"
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TaskHelper.delById(taskDetailPersent.getId());
                                TaskDetailActivity.this.finish();
                            }
                        });
                confirmDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                confirmDialog.create().show();
            }
        });

        menuShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "share");
                intent.putExtra(Intent.EXTRA_TEXT, taskDetailPersent.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(
                        Intent.createChooser(intent, "share"));
            }
        });

        menuImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ISNav.getInstance().init(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, String path, ImageView imageView) {
                        Glide.with(context).load(path).into(imageView);
                    }
                });
                // 自由配置选项
                ISListConfig config = new ISListConfig.Builder()
                        .multiSelect(false)
                        .rememberSelected(true)
                        .btnBgColor(Color.GRAY)
                        .btnTextColor(Color.BLUE)
                        .statusBarColor(Color.parseColor("#008577"))
                        .title("图片")
                        .titleColor(Color.WHITE)
                        .titleBgColor(Color.parseColor("#3F51B5"))
                        // 裁剪大小。needCrop为true的时候配置
                        .cropSize(1, 1, 200, 200)
                        .needCrop(false)
                        .needCamera(false)
                        // 最大选择图片数量，默认9
                        .maxNum(9)
                        .build();

                ISNav.getInstance().toListActivity(this, config, REQUEST_CODE_CHOOSE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            for (String path : pathList) {
                Log.e("dfsfsdfsd", path);
            }
        }
    }
}

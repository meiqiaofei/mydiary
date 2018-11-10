package com.example.mei.diary;

/**
 * Created by mei on 2018/11/2.
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class EditActivity extends Activity {
    private Diary diary;// 日记实体类
    private TextView title_textview;
    private EditText edittext;
    private Button save_button;// 保存按钮
    private ImageButton time_button;// 时间按钮

    public static final int RETURN_CODE = 1;
    String[] month = { "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December" };
    String[] week = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.setTitle("周杰伦");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 取消屏幕顶标题栏
        setContentView(R.layout.activity_edit); // 加载日记详情布局

        initDiary();

        // 保存按钮做的只是将edittext的内容存储到成员变量diary中，在退出时将diary保存到之前的日记中
        save_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String diarytext = edittext.getText().toString();
                diary.setContent(diarytext);
                onBackPressed();
            }
        });

        // 添加当前时间
        time_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                String am_pm = calendar.get(Calendar.AM_PM) == 0 ? " 上午 "
                        : " 下午 ";
                edittext.getText().insert(edittext.getSelectionStart(),
                        " " + hour + ":" + minute + am_pm);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("diary_return", diary);
        setResult(RESULT_OK, intent);
        super.onBackPressed();// 必须在onbackpressed调用之前设置好要传回去的数据，否则onActivityResult收不到intent和resultcode
    }

    // 单纯地启动这个activity
    public static void startEditActivity(Activity context) {
        Intent intent = new Intent(context, EditActivity.class);
        context.startActivity(intent);
    }

    // 需要传递数据和返回数据的方式启动这个activity
    public static void startEditActivityForResult(Activity context, Diary diary) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra("editdiary", diary);
        context.startActivityForResult(intent, RETURN_CODE);
    }

    private void initDiary() {
        Intent intent = getIntent();
        diary = (Diary) intent.getSerializableExtra("editdiary");// 取出传过来的Diary对象
        title_textview = (TextView) findViewById(R.id.activity_edit_title_textview);
        edittext = (EditText) findViewById(R.id.activity_edit_edittext);
        save_button = (Button) findViewById(R.id.activity_edit_save_button);
        time_button =(ImageButton)findViewById(R.id.activity_edit_time_button);

        title_textview.setText(week[diary.getWeek()] + "/"
                + month[diary.getMonth() - 1] + " "
                + String.valueOf(diary.getDate()) + "/"
                + String.valueOf(diary.getYear()));
        if (diary.getWeek() == 0 ) {
            String text = title_textview.getText().toString();
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            ForegroundColorSpan colorspan = new ForegroundColorSpan(Color.RED);
            builder.setSpan(colorspan, 0, text.indexOf("/"),
                    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            title_textview.setText(builder);
        }
        edittext.setText(diary.getContent());
        edittext.setSelection(edittext.getText().length());// 设置edittext的光标指向文本末尾
    }
}
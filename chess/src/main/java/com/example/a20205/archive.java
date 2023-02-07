package com.example.a20205;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class archive extends AppCompatActivity {
    TableLayout table;
    Button _btnBack,_btnForward,_btnEsc;
    TableRow line;
    TextView _lbl1,_timer1,_timer2,_lblLang,_lbl2;
    ImageView cell;
    int[][] save_table = new int[8][8];
    ArrayList<int[][]> save = new ArrayList<>();
    ArrayList<String> lang,save_time1,save_time2;
    String s,ish,desc;
    FileInputStream FIS;
    ObjectInputStream OIS;
    archive_game archive;
    HorizontalScrollView _scroll;
    Timer timer= new Timer();
    Task1 task1= new Task1();

    int width,number=0,c=0;
    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        table=findViewById(R.id.table);
        _btnBack=findViewById(R.id.btnBack);
        _btnForward=findViewById(R.id.btnForward);
        _btnEsc=findViewById(R.id.btnEsc);
        _lbl1=findViewById(R.id.lbl1);
        _timer1=findViewById(R.id.timer1);
        _timer2=findViewById(R.id.timer2);
        _lblLang=findViewById(R.id.lblLang);
        _scroll=findViewById(R.id.scroll);
        _lbl2=findViewById(R.id.lbl2);

        try {
            FIS=openFileInput((String) getIntent().getExtras().get("game"));
            OIS=new ObjectInputStream(FIS);
            archive=(archive_game)OIS.readObject();
        } catch (Exception ignored) {}

        save= archive.save;
        lang= archive.lang;
        save_time1 = archive.save_time1;
        save_time2=archive.save_time2;
        ish=archive.ish;
        desc=archive.desc;
        save_table= save.get(0);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        table.setLayoutParams(new TableLayout.LayoutParams(width, width));
        table.setBackgroundResource(R.drawable.board);
        for (int i = 1; i < 9; i++) {
            line = new TableRow(getApplicationContext());
            line.setId(i);
            table.addView(line);
            for (int j = 1; j < 9; j++) {
                cell = new ImageView(getApplicationContext());
                cell.setLayoutParams(new TableRow.LayoutParams(width / 8, width / 8));
                cell.setId(i * 10 + j);
                cell.setImageResource(save_table[i-1][j-1]);
                cell.setScaleType(ImageView.ScaleType.FIT_XY);
                cell.setBackground(null);
                cell.setPadding(width / 64, width / 64, width / 64, width / 64);

                line.addView(cell);
            }
        }

        _timer1.setText(save_time1.get(0));
        _timer2.setText(save_time2.get(0));
        _lbl1.setText(ish);
        _lbl2.setText(desc);

        timer.schedule(task1,0,1);

        _btnBack.setOnClickListener(v -> {
                s="";
            try {
                save_table=save.get(number-1);
                number--;
            } catch (Exception ignored) {}
            for(int i=1;i<9;i++) {
                for(int j=1;j<9;j++){
                    cell=table.findViewById(i*10+j);
                    cell.setImageResource(save_table[i-1][j-1]);
                }
            }

            _timer1.setText(save_time1.get(number/2));
            _timer2.setText(save_time2.get((number+1)/2));

            for (int i=0;i<number;i++) {
                if(i%2==0) {
                    s+="("+(i/2+1)+") ";
                }
                s+=lang.get(i)+" ";
            }
            _lblLang.setText(s);
        });

        _btnForward.setOnClickListener(v -> {
            c=0;
            s="";
            try {
                save_table=save.get(number+1);
                number++;
            } catch (Exception ignored) {}

            for(int i=1;i<9;i++) {
                for(int j=1;j<9;j++){
                    cell=table.findViewById(i*10+j);
                    cell.setImageResource(save_table[i-1][j-1]);
                }
            }

            _timer1.setText(save_time1.get(number/2));
            _timer2.setText(save_time2.get((number+1)/2));

            for (int i=0;i<number;i++) {
                if(i%2==0) {
                    s+="("+(i/2+1)+") ";
                }
                s+=lang.get(i)+" ";
            }
            _lblLang.setText(s);

        });

        _btnEsc.setOnClickListener(v -> this.finish());

    }
    class Task1 extends TimerTask {

        @SuppressLint({"InflateParams", "SetTextI18n"})
        @Override
        public void run() {

            runOnUiThread(() -> {

                if(c<10) {
                    _scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    c++;
                }
            });
        }
    }
}
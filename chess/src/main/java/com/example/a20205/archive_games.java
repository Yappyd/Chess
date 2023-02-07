package com.example.a20205;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class archive_games extends AppCompatActivity {

    Button _btn1,_btn2,_btnEsc;
    RadioGroup _rad;
    RadioButton game;
    int but=0,width;
    File dir;
    File[] filelist;
    FileInputStream FIS;
    ObjectInputStream OIS;
    archive_game archive;
    int[][] save_table = new int[8][8];
    ArrayList<int[][]> save = new ArrayList<>();
    ArrayList<String> lang,save_time1,save_time2;
    TableLayout table;
    TableRow line;
    ImageView cell;
    TextView _lblLang,_lbl1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_games);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        _btn1=findViewById(R.id.btn1);
        _btn2=findViewById(R.id.btn2);
        _rad=findViewById(R.id.rad);
        table=findViewById(R.id.table);
        _lblLang =findViewById(R.id.lblLang);
        _btnEsc=findViewById(R.id.btnEsc);
        _lbl1=findViewById(R.id.lbl1);

        dir = new File(getFilesDir()+"/");
        filelist = dir.listFiles();

        if (filelist.length != 0) {
            for(int i=0;i<filelist.length;i++) {
                if(!filelist[i].getName().equals("game")) {
                    game=new RadioButton(getApplicationContext());
                    game.setId(i+1);
                    game.setTag(filelist[i].getName());
                    game.setText(filelist[i].getName());
                    game.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    game.setOnClickListener(click);
                    _rad.addView(game);
                }
            }
        }
        else {
            _btn1.setVisibility(View.INVISIBLE);
            _btn2.setVisibility(View.INVISIBLE);
            _lbl1.setText("У вас нет сохраненных партий");
        }

        _btn1.setOnClickListener(v -> {
            if(but!=0) {
                game=findViewById(but);
                Intent intent = new Intent(this,archive.class);
                intent.putExtra("game",String.valueOf(game.getTag()));
                startActivity(intent);
            }
        });

        _btn2.setOnClickListener(v -> {
            if(but!=0) {
                game=findViewById(but);
                deleteFile(String.valueOf(game.getTag()));

                _rad.clearCheck();
                _rad.removeAllViews();

                dir = new File(getFilesDir()+"/");
                filelist = dir.listFiles();

                if (filelist.length != 0) {
                    for(int i=0;i<filelist.length;i++) {
                        game=new RadioButton(getApplicationContext());
                        game.setId(i+1);
                        game.setTag(filelist[i].getName());
                        game.setText(filelist[i].getName());
                        game.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        game.setOnClickListener(click);
                        _rad.addView(game);
                    }
                }
                else {
                    _btn1.setVisibility(View.INVISIBLE);
                    _btn2.setVisibility(View.INVISIBLE);
                    _lbl1.setText("У вас нет сохраненных партий");
                }
                but=0;
            }
            table.removeAllViews();
            table.setBackground(null);
            _lblLang.setText("");
        });

        _btnEsc.setOnClickListener(v -> this.finish());
    }

    View.OnClickListener click = v -> {
        but=v.getId();
        game=findViewById(but);

        try {
            FIS=openFileInput(game.getTag().toString());
            OIS=new ObjectInputStream(FIS);
            archive=(archive_game)OIS.readObject();
        } catch (Exception ignored) {}
        save= archive.save;
        save_table=save.get(save.size()-1);
        lang=archive.lang;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        table.setLayoutParams(new TableLayout.LayoutParams(width, width));
        table.setBackgroundResource(R.drawable.board);
        table.removeAllViews();

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

        String l="";
        int a=0;
        for (String s: lang) {
            if(a%2==0) {
                l+="("+(a/2+1)+") ";
            }
            a++;
            l+=s+" ";
        }
        _lblLang.setText(l);
    };

}
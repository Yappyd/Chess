package com.example.a20205;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class endgame extends AppCompatActivity {

    TableLayout table;
    TableRow line;
    Button _btn1,_btn2;
    ImageView cell;
    TextView _lbl1,_lbl2,_lbl3;
    int[][] save_table;
    int width,a=0;
    ArrayList<String> lang;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        table=findViewById(R.id.table);
        _btn1=findViewById(R.id.btn1);
        _btn2=findViewById(R.id.btn2);
        _lbl1=findViewById(R.id.lbl1);
        _lbl2=findViewById(R.id.lbl2);
        _lbl3=findViewById(R.id.lblLang);

        save_table=(int[][])getIntent().getExtras().get("table");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        table.setLayoutParams(new TableLayout.LayoutParams(width,width));

        for (int i = 1; i < 9; i++) {
            line = new TableRow(getApplicationContext());
            line.setId(i);
            table.addView(line);
            for (int j = 1; j < 9; j++) {
                cell = new ImageView(this);
                cell.setLayoutParams(new TableRow.LayoutParams(width / 8, width / 8));
                cell.setId(i * 10 + j);
                cell.setImageResource(save_table[i-1][j-1]);
                cell.setScaleType(ImageView.ScaleType.FIT_XY);
                cell.setBackground(null);
                cell.setPadding(width / 64, width / 64, width / 64, width / 64);

                line.addView(cell);
            }
        }

        lang=(ArrayList<String>)getIntent().getExtras().get("lang");
        for (String s:lang) {
            if(a%2==0) {
                _lbl3.setText(_lbl3.getText()+"("+(a/2+1)+") ");
            }
            a++;
            _lbl3.setText(_lbl3.getText()+s+" ");
        }

        _lbl1.setText(getIntent().getExtras().get("color").toString());
        _lbl2.setText(getIntent().getExtras().get("desc").toString());

        _btn1.setOnClickListener(v -> {
            Intent intent = new Intent(this,archive.class);
            intent.putExtra("game",getIntent().getExtras().get("game_name").toString());
            startActivity(intent);
        });

        _btn2.setOnClickListener(v -> this.finish());

    }
}
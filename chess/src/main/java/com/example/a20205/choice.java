package com.example.a20205;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class choice extends AppCompatActivity {

    Button _btnEsc,_cbtn1,_cbtn2,_cbtn3,_cbtn4,_cbtn5,_ibtn1,_ibtn2,_ibtn3,_ibtn4,_ibtn5;
    int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        _btnEsc=findViewById(R.id.btnEsc);
        _cbtn1=findViewById(R.id.cbtn1);
        _cbtn2=findViewById(R.id.cbtn2);
        _cbtn3=findViewById(R.id.cbtn3);
        _cbtn4=findViewById(R.id.cbtn4);
        _cbtn5=findViewById(R.id.cbtn5);
        _ibtn1=findViewById(R.id.btnInfo1);
        _ibtn2=findViewById(R.id.btnInfo2);
        _ibtn3=findViewById(R.id.btnInfo3);
        _ibtn4=findViewById(R.id.btnInfo4);
        _ibtn5=findViewById(R.id.btnInfo5);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        _btnEsc.setOnClickListener(v -> this.finish());

        View.OnClickListener click = v -> {
            Intent intent = new Intent(this,Game.class);
            intent.putExtra("game_choice",Integer.parseInt(v.getTag().toString()));
            this.finish();
            startActivity(intent);
        };

        _cbtn1.setOnClickListener(click);
        _cbtn2.setOnClickListener(click);
        _cbtn3.setOnClickListener(click);
        _cbtn4.setOnClickListener(click);
        _cbtn5.setOnClickListener(click);

        _ibtn1.setOnClickListener(v -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("У каждого игрока есть 1 минута, чтобы завершить ход");
            builder.setPositiveButton("Ок",null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        _ibtn2.setOnClickListener(v -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("У каждого игрока есть 3 минуты на все ходы\nДополнительные 2 секунды добавляются за ход");
            builder.setPositiveButton("Ок",null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        _ibtn3.setOnClickListener(v -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("У каждого игрока есть 5 минут на все ходы");
            builder.setPositiveButton("Ок",null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        _ibtn4.setOnClickListener(v -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("У каждого игрока есть 10 минут на все ходы");
            builder.setPositiveButton("Ок",null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        _ibtn5.setOnClickListener(v -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("У каждого игрока есть 48 часов, чтобы сделать ход");
            builder.setPositiveButton("Ок",null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }
}
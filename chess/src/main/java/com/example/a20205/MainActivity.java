package com.example.a20205;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button _btn1,_btn2,_btnEsc;
    FileInputStream FIS;
    ObjectInputStream OIS;
    FileOutputStream FOS;
    ObjectOutputStream OOS;
    save_game game;
    long time;
    int game_time;
    String save_date,s;
    archive_game archive;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        _btn1=findViewById(R.id.btn1);
        _btn2=findViewById(R.id.btn2);
        _btnEsc=findViewById(R.id.btnEsc);

        try {
            FIS=openFileInput("game");
            OIS=new ObjectInputStream(FIS);
            game=(save_game)OIS.readObject();

        } catch (Exception ignored) {}

        if(game!=null) {
            time=System.currentTimeMillis();
            if(game.choice==1|| game.choice==5) {
                game_time=game.maxtime;
            }
            else {
                if(game.hod) {
                    game_time=game.time1;
                }
                else {
                    game_time=game.time2;
                }
            }
            if((time-game.system_time)/1000<game_time) {
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("У вас есть незавершенная партия");
                builder.setMessage("Хотите продолжить?");
                builder.setCancelable(false);
                builder.setPositiveButton("Да", (dialog, which) -> {
                    Intent intent3 = new Intent(this,Game.class);
                    intent3.putExtra("game",game);
                    startActivity(intent3);
                });
                builder.setNegativeButton("Нет", (dialog, which) -> {
                    archive = new archive_game();
                    archive.lang=game.lang;
                    archive.save=game.save;
                    if(game.hod) {
                        archive.desc="Белые решили сдаться";
                        s="Победа черных";
                        archive.ish=s;
                    }
                    else {
                        archive.desc="Черные решили сдаться";
                        s="Победа Белых";
                        archive.ish=s;
                    }
                    archive.save_time1=game.save_time1;
                    archive.save_time2=game.save_time2;
                    save_date=game.save_date;

                    try {
                        FOS= openFileOutput(s+save_date,MODE_PRIVATE);
                        OOS= new ObjectOutputStream(FOS);
                        OOS.writeObject(archive);
                        FOS.close();
                        OOS.close();
                    } catch (Exception ignored) {}
                    deleteFile("game");
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                archive = new archive_game();
                archive.lang=game.lang;
                archive.save=game.save;
                if(game.hod) {
                    archive.desc="У белых истекло время на ход";
                    s="Победа черных";
                    archive.ish=s;
                }
                else {
                    archive.desc="У черных истекло время на ход";
                    s="Победа Белых";
                    archive.ish=s;
                }
                archive.save_time1=game.save_time1;
                archive.save_time2=game.save_time2;
                save_date=game.save_date;

                try {
                    FOS= openFileOutput(s+save_date,MODE_PRIVATE);
                    OOS= new ObjectOutputStream(FOS);
                    OOS.writeObject(archive);
                    FOS.close();
                    OOS.close();
                } catch (Exception ignored) {}
                deleteFile("game");
            }
        }

        _btn1.setOnClickListener(v -> {
            Intent intent = new Intent(this,choice.class);
            startActivity(intent);
        });

        _btn2.setOnClickListener(v -> {
            Intent intent1 = new Intent(this,archive_games.class);
            startActivity(intent1);
        });

        _btnEsc.setOnClickListener(v -> finish());
    }
}
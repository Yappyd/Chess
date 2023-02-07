package com.example.a20205;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class  save_game implements Serializable {
    int time1,time2,maxtime,choice,vnp_pawn, vnp_stay, white_king, black_king, check_king;
    boolean check_check,black_check,white_check,hod, white_draw,black_draw;
    long system_time;
    String save_date,lbl1,lbl2;
    ArrayList<Integer> proverka,variants;
    Map<Integer,ArrayList<Integer>> nelzia;
    int[][] save_table;
    ArrayList<int[][]> save;
    String[][] save_tag;
    ArrayList<String> lang,save_time1,save_time2;

     void save(Game app) {
        time1=app.time1;
        time2=app.time2;
        maxtime=app.timeMax;
        choice=app.choice;
        vnp_pawn=app.vnp_pawn;
        vnp_stay=app.vnp_stay;
        white_king=app.white_king;
        black_king=app.black_king;
        check_king=app.check_king;
        check_check=app.check_check;
        black_check=app.black_check;
        white_check=app.white_check;
        hod=app.hod;
        white_draw=app.white_draw;
        black_draw=app.black_draw;
        save_date=app.save_date;
        system_time=app.system_time;
        proverka =app.proverka;
        variants=app.variants;
        nelzia=app.impsbl;
        save_table=app.save_table;
        save_tag=app.save_tag;
        save_time1=app.save_time1;
        save_time2=app.save_time2;
        save=app.save;
        lang=app.lang;
        lbl1=app._lblTimer1.getText().toString();
        lbl2=app._lblTimer2.getText().toString();

    }
}

package com.example.a20205;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends AppCompatActivity {

    Button _btnGp,_btnRt,_btnEsc;
    TextView _lbl1,_lbl2,_lblTimer1,_lblTimer2,timer;
    CheckBox _checkDw;
    TableLayout table;

    TableRow line;
    ImageButton cell;

    Timer _timer1;

    boolean n_click = false, black_check = false, white_check = false, hod = true, check_check,mate=false,stalemate=true, white_draw=false,black_draw=false;
    int vnp_pawn, vnp_stay, width, white_king = 85, black_king = 15, check_king=85,choice,time1,time2,timeMax;
    long system_time;
    String save_date,f,l,s,d,e="",lang_hod;
    ArrayList<String> lang = new ArrayList<>();
    ArrayList<int[][]> save = new ArrayList<>();
    ArrayList<String> save_time1= new ArrayList<>(),save_time2= new ArrayList<>();
    int[][] save_table = new int[8][8];
    String[][] save_tag = new String[8][8];
    FileOutputStream FOS;
    ObjectOutputStream OOS;
    save_game game = new save_game();

    Task1 _task1 = new Task1();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        _btnEsc=findViewById(R.id.btnEsc);
        _btnRt=findViewById(R.id.btnRt);
        _btnGp=findViewById(R.id.btnGp);
        _lbl1=findViewById(R.id.lbl1);
        _lbl2=findViewById(R.id.lblLang);
        _checkDw=findViewById(R.id.checkDw);
        table=findViewById(R.id.table);
        _lblTimer1=findViewById(R.id.lblTimer1);
        _lblTimer2=findViewById(R.id.lblTimer2);
        timer=_lblTimer2;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        Date date = new Date();
        SimpleDateFormat date_format = new SimpleDateFormat("\nyyyy.MM.dd hh:mm:ss");
        save_date=date_format.format(date);

        table.setLayoutParams(new TableLayout.LayoutParams(width, width));
        table.setBackgroundResource(R.drawable.board);
        
        if(getIntent().getExtras().get("game")==null)  {
            for (int i = 1; i < 9; i++) {
                line = new TableRow(getApplicationContext());
                line.setId(i);
                table.addView(line);
                for (int j = 1; j < 9; j++) {
                    cell = new ImageButton(getApplicationContext());
                    cell.setLayoutParams(new TableRow.LayoutParams(width / 8, width / 8));
                    cell.setId(i * 10 + j);
                    cell.setImageResource(Start.figure[i - 1][j - 1]);
                    cell.setScaleType(ImageView.ScaleType.FIT_XY);
                    cell.setBackground(null);
                    cell.setPadding(width / 64, width / 64, width / 64, width / 64);
                    cell.setTag(Start.name[i - 1][j - 1]);
                    line.addView(cell);

                    cell.setOnClickListener(click);
                }
            }

            choice= (int)getIntent().getExtras().get("game_choice");
            switch (choice) {
                case 1:
                    timeMax=60;
                    time1=60;
                    time2=60;
                    break;

                case 2:
                    time1=180;
                    time2=180;
                    break;

                case 3:
                    time1=300;
                    time2=300;
                    break;

                case 4:
                    time1=600;
                    time2=600;
                    break;

                case 5:
                    timeMax=172800;
                    time1=172800;
                    time2=172800;
                    break;
            }

            _timer1= new Timer();
            _timer1.schedule(_task1,1000,1000);
            _lblTimer1.setTag(time1);
            _lblTimer2.setTag(time2);
            _lblTimer1.setText(format(choice,time1));
            _lblTimer2.setText(format(choice,time2));
            save_time1.add(_lblTimer1.getText().toString());
            save_time2.add(_lblTimer2.getText().toString());

            for(int i=1;i<9;i++) {
                for(int j=1;j<9;j++) {
                    cell=table.findViewById(i*10+j);
                    if(cell.getTag()!=null) {
                        words=cell.getTag().toString().split(" ");
                        save_table[i-1][j-1]=getResources().getIdentifier("drawable/"+words[0]+"_"+words[1],null,getPackageName());
                        save_tag[i-1][j-1]=cell.getTag().toString();
                    }

                }
            }

            save.add(save_table);

        }
        else {
            save_game game = (save_game) getIntent().getExtras().get("game");
            black_check=game.black_check;
            white_check=game.white_check;
            hod=game.hod;
            check_check=game.check_check;
            white_draw=game.white_draw;
            black_draw=game.black_draw;
            vnp_pawn=game.vnp_pawn;
            vnp_stay =game.vnp_stay;
            white_king=game.white_king;
            black_king=game.black_king;
            check_king=game.check_king;
            choice=game.choice;
            time1=game.time1;
            time2=game.time2;
            timeMax=game.maxtime;
            save_date=game.save_date;
            save_tag=game.save_tag;
            save_table=game.save_table;
            save_time1=game.save_time1;
            save_time2=game.save_time2;
            save=game.save;
            proverka =game.proverka;
            impsbl =game.nelzia;
            variants=game.variants;
            lang=game.lang;

            for (int i = 1; i < 9; i++) {
                line = new TableRow(getApplicationContext());
                line.setId(i);
                table.addView(line);
                for (int j = 1; j < 9; j++) {
                    cell = new ImageButton(getApplicationContext());
                    cell.setLayoutParams(new TableRow.LayoutParams(width / 8, width / 8));
                    cell.setId(i * 10 + j);
                    cell.setImageResource(save_table[i-1][j-1]);
                    cell.setScaleType(ImageView.ScaleType.FIT_XY);
                    cell.setBackground(null);
                    cell.setPadding(width / 64, width / 64, width / 64, width / 64);
                    cell.setTag(save_tag[i-1][j-1]);
                    line.addView(cell);

                    cell.setOnClickListener(click);
                }
            }

            if(check_check) {
                _lbl2.setText("Шах");
                e+="+";
            }

            if(hod) {
                time2=game.time2- (int) ((System.currentTimeMillis()-game.system_time)/1000);
                hodcolor = "white";
                hod = true;
                _lbl1.setText("Ход белых");
                _checkDw.setChecked(white_draw);
                _lblTimer2.setText(format(choice,time2));
                _lblTimer1.setText(game.lbl1);

                timer=_lblTimer2;

            }
            else {
                time1=game.time1- (int) ((System.currentTimeMillis()-game.system_time)/1000);
                hodcolor = "black";
                hod = false;
                _lbl1.setText("Ход черных");
                _checkDw.setChecked(black_draw);
                _lblTimer1.setText(format(choice,time1));
                _lblTimer2.setText(game.lbl2);

                timer=_lblTimer1;
            }

            _lblTimer1.setTag(time1);
            _lblTimer2.setTag(time2);

            _timer1= new Timer();
            _timer1.schedule(_task1,1000,1000);

            if(time1<=0){
                timer.setText(format(choice,0));
                save_time2.add(timer.getText().toString());
                End(11);
            }

            if(time2<=0){
                timer.setText(format(choice,0));
                save_time2.add(timer.getText().toString());
                End(12);
            }

        }

        _btnRt.setOnClickListener(v -> {
            table.setRotation(Math.abs(table.getRotation()-180));
            for(int i=1;i<9;i++) {
                for(int j=1;j<9;j++) {
                    cell=table.findViewById(i*10+j);
                    cell.setRotation(Math.abs(cell.getRotation()-180));
                    if(table.getRotation()==180) table.setBackgroundResource(R.drawable.board2);
                    else table.setBackgroundResource(R.drawable.board);


                }
            }

            int exTag;
            String exTime;

            exTag =(int)_lblTimer1.getTag();
            exTime=(String)_lblTimer1.getText();

            _lblTimer1.setText(_lblTimer2.getText());
            _lblTimer1.setTag(_lblTimer2.getTag());
            _lblTimer2.setText(exTime);
            _lblTimer2.setTag(exTag);

            if(timer==_lblTimer1) {
                timer=_lblTimer2;
                _lblTimer2=_lblTimer1;
                _lblTimer1=timer;
            } else {
                timer=_lblTimer1;
                _lblTimer1=_lblTimer2;
                _lblTimer2=timer;
            }

        });

        _btnEsc.setOnClickListener(v -> {
            String win;
            if(hod) win="черным";
            else win="белым";
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Выход");
            builder.setMessage("Вы уверены что хотите выйти?\nПобеда будет засчитана "+win);
            builder.setPositiveButton("Отмена",null);
            builder.setNegativeButton("Да", (dialog, which) -> {
                if (hod) End(51);
                else End(52);
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        _checkDw.setOnClickListener(v -> {
            if(hod) white_draw=_checkDw.isChecked();
            else black_draw=_checkDw.isChecked();

            if(white_draw && black_draw) End(40);

        });

        _btnGp.setOnClickListener(v -> {
            String win;
            if(hod) win="черным";
            else win="белым";
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Выход");
            builder.setMessage("Вы уверены что хотите Сдаться?\nПобеда будет засчитана "+win);
            builder.setPositiveButton("Отмена",null);
            builder.setNegativeButton("Да", (dialog, which) -> {
                if(hod) End(51);
                else End(52);
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }

    public void onBackPressed() {
        String win;
        if(hod) win="черным";
        else win="белым";
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Выход");
        builder.setMessage("Вы уверены что хотите выйти?\nПобеда будет засчитана "+win);
        builder.setPositiveButton("Отмена",null);
        builder.setNegativeButton("Да", (dialog, which) -> {
            if(hod) End(51);
            else End(52);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    String[] words;
    String color1, figure1, hodcolor = "white";
    ArrayList<Integer> variants = new ArrayList<>();
    ImageButton click1, click2;
    int x1, x2, y1, y2, extra = 1, kingH;
    ArrayList<Integer> proverka;
    Map<Integer,ArrayList<Integer>> impsbl;
    View.OnClickListener click = new View.OnClickListener() {

        @SuppressLint({"ResourceType", "ShowToast", "SetTextI18n"})
        @Override

        public void onClick(View v) {
            if (!n_click) {

                impsbl = not_turn(table,check_king);

                if (v.getTag() != null && v.getTag().toString().contains(hodcolor)) {
                    click1 = (ImageButton) v;
                    variants = figure_turns(table, click1.getId(),check_check, proverka, impsbl);

                    for (int id : variants) {
                        cell = table.findViewById(id);
                        if (cell.getTag() == null) {
                            cell.setImageResource(R.drawable.dot);
                            cell.setPadding(width / 8 / 3, width / 8 / 3, width / 8 / 3, width / 8 / 3);
                        } else {
                            cell.setBackgroundResource(R.drawable.figuredot);
                        }
                    }
                    n_click = true;
                }
            } else {

                click2 = (ImageButton) v;
                for (int id : variants) {
                    cell = table.findViewById(id);

                    if (cell.getTag() == null) {
                        cell.setImageResource(0);
                        cell.setPadding(width / 64, width / 64, width / 64, width / 64);
                    } else {
                        cell.setBackgroundResource(0);
                    }

                }

                if (variants.contains(click2.getId())) {

                    words = click1.getTag().toString().split(" ");
                    color1 = words[0];
                    figure1 = words[1];
                    x1 = click1.getId() / 10;
                    y1 = click1.getId() % 10;
                    x2 = click2.getId() / 10;
                    y2 = click2.getId() % 10;

                    if(click2.getTag()!=null) d="x";

                    click2.setTag(click1.getTag());
                    click2.setImageDrawable(click1.getDrawable());

                    click1.setTag(null);
                    click1.setImageDrawable(null);

                    for(int i=1;i<9;i++) {
                        for(int j=1;j<9;j++) {
                            cell=table.findViewById(i*10+j);
                            if(cell.getTag()!=null) {
                                words=cell.getTag().toString().split(" ");
                                if(!words[0].equals(hodcolor) && words[1].equals("pawn") && words[3].equals("0")) {
                                    cell.setTag(words[0]+" pawn 0 1");
                                }
                            }
                        }
                    }

                    switch (figure1) {

                        case "pawn":

                            if(hodcolor.equals("white")) extra=-1;
                            else extra=1;
                            if (click2.getTag().equals(color1 + " " + figure1 + " " + 1 + " " + 1)) {
                                if(Math.abs(x1-x2)==2) {
                                    click2.setTag(color1 + " " + figure1 + " " + 0 + " " + 0);
                                }
                                else click2.setTag(color1 + " " + figure1 + " " + 0 + " " + 1);
                            }

                            if(click2.getId()== vnp_stay) {
                                try {
                                    ImageButton vnp = table.findViewById(vnp_pawn);
                                    words=vnp.getTag().toString().split(" ");
                                    vnp.setTag(null);
                                    vnp.setImageDrawable(null);
                                    vnp_pawn = 0;
                                    vnp_stay =0;
                                    e="e.p.";
                                } catch (Exception ignored) {}
                            }

                            //Смена фигуры

                            if (x2 == 8 || x2 == 1) {
                                Button _rook, _knight, _bishop, _queen;
                                AlertDialog.Builder builder = new AlertDialog.Builder(Game.this);
                                View customLayout = getLayoutInflater().inflate(R.layout.dialog, null);
                                builder.setView(customLayout);
                                builder.setCancelable(false);

                                _rook = customLayout.findViewById(R.id.rook);
                                _knight = customLayout.findViewById(R.id.knight);
                                _bishop = customLayout.findViewById(R.id.bishop);
                                _queen = customLayout.findViewById(R.id.queen);

                                _rook.setBackgroundResource(getResources().getIdentifier("drawable/" + color1 + "_rook", null, getPackageName()));
                                _knight.setBackgroundResource(getResources().getIdentifier("drawable/" + color1 + "_knight", null, getPackageName()));
                                _bishop.setBackgroundResource(getResources().getIdentifier("drawable/" + color1 + "_bishop", null, getPackageName()));
                                _queen.setBackgroundResource(getResources().getIdentifier("drawable/" + color1 + "_queen", null, getPackageName()));

                                _rook.setLayoutParams(new TableRow.LayoutParams(width / 8, width / 8));
                                _knight.setLayoutParams(new TableRow.LayoutParams(width / 8, width / 8));
                                _bishop.setLayoutParams(new TableRow.LayoutParams(width / 8, width / 8));
                                _queen.setLayoutParams(new TableRow.LayoutParams(width / 8, width / 8));

                                _rook.setPadding(width / 64, width / 64, width / 64, width / 64);
                                _knight.setPadding(width / 64, width / 64, width / 64, width / 64);
                                _bishop.setPadding(width / 64, width / 64, width / 64, width / 64);
                                _queen.setPadding(width / 64, width / 64, width / 64, width / 64);

                                AlertDialog dialog = builder.create();
                                dialog.show();

                                View.OnClickListener click = v1 -> {
                                    v.setTag(color1 + " " + v1.getTag());
                                    ((ImageButton) v).setImageDrawable(v1.getBackground());
                                    if (!hod) {
                                        switch (v1.getTag().toString()) {
                                            case "rook":
                                                e = "♜";
                                                break;
                                            case "knight":
                                                e = "♞";
                                                break;
                                            case "bishop":
                                                e = "♝";
                                                break;
                                            case "queen":
                                                e = "♛";
                                                break;
                                        }
                                        f = "♟";
                                    } else {
                                        switch (v1.getTag().toString()) {
                                            case "rook":
                                                e = "♖";
                                                break;
                                            case "knight":
                                                e = "♘";
                                                break;
                                            case "bishop":
                                                e = "♗";
                                                break;
                                            case "queen":
                                                e = "♕";
                                                break;
                                        }
                                        f = "♙";
                                    }


                                    dialog.dismiss();

                                    konec_coda_clicka();
                                };

                                _rook.setOnClickListener(click);
                                _knight.setOnClickListener(click);
                                _bishop.setOnClickListener(click);
                                _queen.setOnClickListener(click);
                                return;
                            }
                            break;

                        case "king":
                            ImageButton excell;
                            words=click2.getTag().toString().split(" ");
                            kingH=Integer.parseInt(words[2]);
                            if (kingH == 1) {

                                if (click2.getId() == x1 * 10 + 7) {
                                    cell = table.findViewById(x1 * 10 + 6);
                                    excell = table.findViewById(x1 * 10 + 8);

                                    cell.setImageDrawable(excell.getDrawable());
                                    words = excell.getTag().toString().split(" ");
                                    cell.setTag(words[0] + " " + words[1] + " " + 0);
                                    e="O-O";
                                    excell.setTag(null);
                                    excell.setImageDrawable(null);
                                }

                                if (click2.getId() == x1 * 10 + 3) {
                                    cell = table.findViewById(x1 * 10 + 4);
                                    excell = table.findViewById(x1 * 10 + 1);

                                    cell.setImageDrawable(excell.getDrawable());
                                    words = excell.getTag().toString().split(" ");
                                    cell.setTag(words[0] + " " + words[1] + " " + 0);
                                    e="O-O-O";
                                    excell.setTag(null);
                                    excell.setImageDrawable(null);
                                }
                            }

                            click2.setTag(color1 + " " + figure1 + " " + 0);

                            if (color1.equals("white")) white_king = click2.getId();
                            else black_king = click2.getId();

                            break;
                        case "rook":
                            click2.setTag(color1 + " " + figure1 + " " + 0);
                            break;
                    }

                    konec_coda_clicka();

                }

                variants = null;
                click1 = null;
                click2 = null;
                n_click = false;
            }
        }
    };

    @SuppressLint("ResourceType")

    ArrayList<Integer> figure_turns(TableLayout table, int id, boolean check_check, ArrayList<Integer> proverka, Map<Integer,ArrayList<Integer>> nelzia) {

        ArrayList<Integer> nelzia_variants = new ArrayList<>(),dop_variants, variants = new ArrayList<>(),prov = new ArrayList<>();
        ImageButton cell;
        String[] words;
        int[][][] motion;
        int x, y, extra = 1, pawn1, pawnH, kingH, rookH, schet = 0;
        String color1, color2, figure1, figure2;
        boolean prov_check=false;

        cell = table.findViewById(id);
        words = cell.getTag().toString().split(" ");
        color1 = words[0];
        figure1 = words[1];
        x = cell.getId() / 10;
        y = cell.getId() % 10;
        motion = Motion.getFigure(figure1);

        if(nelzia!=null && nelzia.containsKey(id)) {
            nelzia_variants=nelzia.get(id);
        } else {
            for (int i=1;i<9;i++) {
                for(int j=1;j<9;j++) {
                    nelzia_variants.add(i*10+j);
                }
            }
        }

        if(check_check) {
            prov=proverka;
            prov_check=true;
        } else {
            for(int i=1;i<9;i++) {
                for(int j=1;j<9;j++) {
                    prov.add(i*10+j);
                }
            }
        }

        switch (figure1) {
            case "pawn":
                if (color1.equals("white")) extra = -1;
                pawnH = Integer.parseInt(words[2]);
                try {
                    for (int i = 0; i <= pawnH; i++) {
                        cell = table.findViewById((x + motion[0][i][0] * extra) * 10 + y + motion[0][i][1]);

                        if (cell.getTag() == null && nelzia_variants.contains(cell.getId()) && prov.contains(cell.getId())) {
                            variants.add(cell.getId());
                        }
                        if (cell.getTag()!=null) break;
                    }
                } catch (Exception ignored) {}


                for (int i = 1; i >= -1; i = i - 2) {
                    try {
                        cell = table.findViewById((x + extra) * 10 + y + i);
                        words = cell.getTag().toString().split(" ");
                        color2 = words[0];
                        figure2 = words[1];

                        if (cell.getTag() != null && !figure2.equals("king") && !color1.equals(color2)&& nelzia_variants.contains(cell.getId()) && prov.contains(cell.getId()))
                            variants.add(cell.getId());

                    } catch (Exception ignored) {}
                }

                for (int i = 1; i >= -1; i = i - 2) {
                    try {
                        cell = table.findViewById(x * 10 + y + i);
                        words = cell.getTag().toString().split(" ");
                        pawn1 = Integer.parseInt(words[3]);

                        cell = table.findViewById((x + extra) * 10 + y + i);

                        if (pawn1 == 0 && cell.getTag() == null && nelzia_variants.contains(cell.getId()) && prov.contains(cell.getId()) && !words[0].equals(hodcolor)) {
                            variants.add(cell.getId());
                            vnp_pawn = x * 10 + y + i;
                            vnp_stay =cell.getId();
                        }
                    } catch (Exception ignored) {}
                }
                break;

            case "king":

                dop_variants = king_variants(table, id);

                for (int i = 0; i < motion.length; i++) {
                    for (int j = 0; j < motion[i].length; j++) {
                        try {
                            cell = table.findViewById((x + motion[i][j][0]) * 10 + y + motion[i][j][1]);

                            if (cell.getTag() == null && !dop_variants.contains(cell.getId())) {
                                variants.add(cell.getId());
                            } else {
                                words = cell.getTag().toString().split(" ");
                                color2 = words[0];
                                figure2 = words[1];

                                if (!color2.equals(color1) && !figure2.equals("king") && !dop_variants.contains(cell.getId()))
                                    variants.add(cell.getId());
                                break;
                            }
                        } catch (Exception e) {
                            break;
                        }
                    }
                }

                //Рокировка

                cell = table.findViewById(id);
                x=id/10;
                y=id%10;
                words = cell.getTag().toString().split(" ");
                kingH = Integer.parseInt(words[2]);
                if (kingH == 1 && !check_check) {


                    for (int i = y + 1; i < 8; i++) {
                        cell = table.findViewById(x * 10 + i);
                        if (cell.getTag() != null || dop_variants.contains(x * 10 + i)) {
                            schet++;
                        }
                    }

                    if (schet == 0) {
                        cell = table.findViewById(x * 10 + 8);

                        if(cell.getTag()!=null) {
                            words=cell.getTag().toString().split(" ");
                            if(words[0].equals(color1) && words[1].equals("rook")) {
                                rookH=Integer.parseInt(words[2]);
                                if(rookH==1) {
                                    variants.add(x*10+7);
                                }
                            }
                        }
                    }

                    schet = 0;

                    for (int i = y - 1; i > 1; i--) {
                        cell = table.findViewById(x * 10 + i);
                        if (cell.getTag() != null || dop_variants.contains(x * 10 + i)) {
                            schet++;
                        }
                    }

                    if (schet == 0) {
                        cell = table.findViewById(x * 10 + 1);

                        if(cell.getTag()!=null) {
                            words=cell.getTag().toString().split(" ");
                            if(words[0].equals(color1) && words[1].equals("rook")) {
                                rookH=Integer.parseInt(words[2]);
                                if(rookH==1) {
                                    variants.add(x*10+3);
                                }
                            }
                        }
                    }

                }

                break;

            default:
                for (int i = 0; i < motion.length; i++) {
                    for (int j = 0; j < motion[i].length; j++) {
                        try {
                            cell = table.findViewById((x + motion[i][j][0]) * 10 + y + motion[i][j][1]);

                            if (cell.getTag() == null && prov.contains(cell.getId()) && nelzia_variants.contains(cell.getId())) {
                                variants.add(cell.getId());
                            } else {
                                if(prov_check) {
                                    if(cell.getTag()!=null) {
                                        if(prov.contains(cell.getId()) && nelzia_variants.contains(cell.getId())) {
                                            variants.add(cell.getId());
                                        }
                                        break;
                                    }
                                }
                                else {
                                    words = cell.getTag().toString().split(" ");
                                    color2 = words[0];
                                    figure2 = words[1];
                                    if (!color2.equals(color1) && !figure2.equals("king") && nelzia_variants.contains(cell.getId()))
                                        variants.add(cell.getId());
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            break;
                        }
                    }
                }
                break;
        }
        return variants;
    }

    ArrayList<Integer> king_variants(TableLayout table, int id) {

        ArrayList<Integer> king_variants = new ArrayList<>();
        ImageButton king, cell;
        String[] words;
        String color1, color2, figure2;
        int[][][] motion;
        int extra = 1;

        king = table.findViewById(id);
        words = king.getTag().toString().split(" ");
        color1 = words[0];

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                cell = table.findViewById(i * 10 + j);
                if (cell.getTag() != null) {

                    words = cell.getTag().toString().split(" ");
                    color2 = words[0];
                    figure2 = words[1];
                    if (!color1.equals(color2)) {
                        if (figure2.equals("pawn")) {
                            if (color2.equals("white")) extra = -1;

                            for (int c = 1; c >= -1; c = c - 2) {
                                try {
                                    cell = table.findViewById((i + extra) * 10 + j + c);
                                    king_variants.add(cell.getId());

                                } catch (Exception ignored) {}
                            }
                        } else {
                            motion = Motion.getFigure(figure2);
                            for (int a = 0; a < motion.length; a++) {
                                for (int b = 0; b < motion[a].length; b++) {
                                    try {
                                        cell = table.findViewById((i + motion[a][b][0]) * 10 + j + motion[a][b][1]);
                                        if (cell.getTag() == null || cell==king) {
                                            if (!king_variants.contains(cell.getId())) king_variants.add(cell.getId());
                                        } else {
                                            if (!king_variants.contains(cell.getId())) king_variants.add(cell.getId());
                                            break;
                                        }
                                    } catch (Exception e) {
                                        break;
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        return king_variants;
    }

    ArrayList<Integer> figure_stay(TableLayout table, int id) {

        ArrayList<Integer> variants = new ArrayList<>();
        ImageButton king, cell;
        String[] words;
        String color1, color2, figure2;
        int[][][] motion;
        int extra = 1,check_schet=0;

        king = table.findViewById(id);
        words = king.getTag().toString().split(" ");
        color1 = words[0];

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                cell = table.findViewById(i * 10 + j);
                if (cell.getTag() != null) {

                    words = cell.getTag().toString().split(" ");
                    color2 = words[0];
                    figure2 = words[1];
                    if (!color1.equals(color2)) {
                        if (figure2.equals("pawn")) {
                            if (color2.equals("white")) extra = -1;

                            for (int c = 1; c >= -1; c = c - 2) {
                                try {
                                    cell = table.findViewById((i + extra) * 10 + j + c);
                                    if(cell==king) {
                                        check_schet++;
                                        variants.add(i*10+j);
                                        break;
                                    }

                                } catch (Exception ignored) {}
                            }
                        } else {
                            motion = Motion.getFigure(figure2);
                            for (int a = 0; a < motion.length; a++) {
                                for (int b = 0; b < motion[a].length; b++) {
                                    try {
                                        cell = table.findViewById((i + motion[a][b][0]) * 10 + j + motion[a][b][1]);
                                        if(cell.getTag()!=null) {
                                            if(cell==king) {
                                                check_schet++;
                                                variants.add(i*10+j);
                                                for(int q=0;q<motion[a].length;q++) {
                                                    try {
                                                        cell = table.findViewById((i + motion[a][q][0]) * 10 + j + motion[a][q][1]);
                                                        if(cell==king) break;
                                                        variants.add(cell.getId());
                                                    } catch (Exception e) {
                                                        break;
                                                    }
                                                }
                                            }
                                            break;
                                        }

                                    } catch (Exception e) {
                                        break;
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        if(check_schet>1) return null;
        return variants;

    }

    Map<Integer,ArrayList<Integer>> not_turn(TableLayout table, int id) {

        Map<Integer,ArrayList<Integer>> variants = new HashMap<>();
        ImageButton king, cell;
        String[] words;
        String color1, color2, figure2;
        int[][][] motion;
        int prohod;
        ArrayList<Integer> list_kotori_variants = new ArrayList<>();

        king = table.findViewById(id);
        words = king.getTag().toString().split(" ");
        color1 = words[0];

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                cell = table.findViewById(i * 10 + j);
                if (cell.getTag() != null) {

                    words = cell.getTag().toString().split(" ");
                    color2 = words[0];
                    figure2 = words[1];
                    if(!color1.equals(color2) && !figure2.equals("pawn") && !figure2.equals("king") && !figure2.equals("knight")) {
                        motion = Motion.getFigure(figure2);

                        for (int a = 0; a < motion.length; a++) {
                            prohod=0;
                            for (int b = 0; b < motion[a].length; b++) {
                                try {
                                    cell = table.findViewById((i + motion[a][b][0]) * 10 + j + motion[a][b][1]);
                                    if(cell.getTag()!=null) {
                                        if(prohod==0) prohod=cell.getId();
                                        else {
                                            if(cell==king) {
                                                for(int c =0;c<motion[a].length;c++) {
                                                    try {

                                                        cell=table.findViewById((i+motion[a][c][0])*10 + j + motion[a][c][1]);
                                                        list_kotori_variants.add(cell.getId());

                                                    } catch (Exception e) {
                                                        break;
                                                    }
                                                }
                                                list_kotori_variants.add(i*10+j);
                                                variants.put(prohod,list_kotori_variants);
                                            }
                                            break;
                                        }
                                    }
                                }
                                catch (Exception e) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return variants;
    }

    class Task1 extends TimerTask {

        @SuppressLint({"InflateParams", "SetTextI18n"})
        @Override
        public void run() {

            runOnUiThread(() -> {
                int a=(int)timer.getTag()-1;
                timer.setTag(a);
                timer.setText(format(choice,a));

                time1=(int)_lblTimer1.getTag();
                time2=(int)_lblTimer2.getTag();

                system_time= System.currentTimeMillis();

                game.save(Game.this);

                try {
                    FOS= openFileOutput("game",MODE_PRIVATE);
                    OOS= new ObjectOutputStream(FOS);
                    OOS.writeObject(game);
                    FOS.close();
                    OOS.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(a<=0) {
                    if(hod) End(11);
                    else End(12);
                }
            });
        }
    }

    @SuppressLint("DefaultLocale")
    String format(int choice, int time) {
        String text;

        if(choice==5) text=String.format("%02d:%02d:%02d",time/3600,time /60 %60,time%60);
        else  text=String.format("%02d:%02d",time /60,time%60);

        return text;
    }

    void End(int what) {
        _timer1.cancel();
        deleteFile("game");

        String win_color="",description="",s="";
        switch (what/10) {
            case 1:
                if(what%10==2) {
                    win_color = "Белые победили!";
                    description="У черных истекло время на ход";
                    s="Победа белых ";
                }
                else {
                    win_color = "Черные победили!";
                    description="У белых истекло время на ход";
                    s="Победа черных ";
                }
                break;
            case 2:
                if(what%10==2) {
                    win_color = "Белые победили!";
                    description="Белые поставили мат";
                    s="Победа белых ";
                }
                else {
                    win_color = "Черные победили!";
                    description="Черные поставили мат";
                    s="Победа черных ";
                }
                break;
            case 3:
                win_color="Ничья!";
                if(what%10==2) {
                    description="У черных не осталось ходов";
                }
                else {
                    description="У белых не осталось ходов";
                }
                s="Ничья ";
                break;
            case 4:
                win_color="Ничья!";
                description="Игроки согласились на ничью";
                s="Ничья ";
                break;
            case 5:
                if(what%10==2) {
                    win_color = "Белые победили!";
                    description="Черные решили сдаться";
                    s="Победа белых ";
                }
                else {
                    win_color = "Черные победили!";
                    description="Белые решили сдаться";
                    s="Победа черных ";
                }
                break;
        }

        archive_game archive = new archive_game();
        archive.lang=lang;
        archive.save=save;
        archive.desc=description;
        archive.ish=s;
        archive.save_time1=save_time1;
        archive.save_time2=save_time2;

        try {
            FOS= openFileOutput(s+save_date,MODE_PRIVATE);
            OOS= new ObjectOutputStream(FOS);
            OOS.writeObject(archive);
            FOS.close();
            OOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent= new Intent(this,endgame.class);
        intent.putExtra("color",win_color);
        intent.putExtra("desc",description);
        intent.putExtra("table", save_table);
        intent.putExtra("game_name",s+save_date);
        intent.putExtra("lang",lang);
        this.finish();
        startActivity(intent);
    }


    void setSave_table(){
        save_table= new int[8][8];
        for(int i=1;i<9;i++) {
            for(int j=1;j<9;j++) {
                save_table[i-1][j-1]=0;
                cell=table.findViewById(i*10+j);
                if(cell.getTag()!=null) {
                    words=cell.getTag().toString().split(" ");
                    save_table[i-1][j-1]=getResources().getIdentifier("drawable/"+words[0]+"_"+words[1],null,getPackageName());
                }
            }
        }
        save.add(save_table);
    }

    void setLang_hod(){
        if(!e.contains("O")) {
            words = click2.getTag().toString().split(" ");
            if(f==null){
                if (hod) {
                    switch (words[1]) {
                        case "pawn":
                            f = "♟";
                            break;
                        case "rook":
                            f = "♜";
                            break;
                        case "knight":
                            f = "♞";
                            break;
                        case "bishop":
                            f = "♝";
                            break;
                        case "queen":
                            f = "♛";
                            break;
                        case "king":
                            f = "♚";
                            break;
                    }
                } else {
                    switch (words[1]) {
                        case "pawn":
                            f = "♙";
                            break;
                        case "rook":
                            f = "♖";
                            break;
                        case "knight":
                            f = "♘";
                            break;
                        case "bishop":
                            f = "♗";
                            break;
                        case "queen":
                            f = "♕";
                            break;
                        case "king":
                            f = "♔";
                            break;
                    }
                }
            }

            s = String.valueOf(9 - x1);
            l = String.valueOf((char) (96 + y1));

            if (d == null) d = "—";
            lang_hod = f + l + s + d;
            d = null;
            f=null;
            s = String.valueOf(9 - x2);
            l = String.valueOf((char) (96 + y2));
            lang_hod += l + s + e;
        } else {
            lang_hod=e;
        }
        e = "";
    }

    void konec_coda_clicka() {
        if (!hod) {
            hodcolor = "white";
            hod = true;
            _lbl1.setText("Ход белых");
            _checkDw.setChecked(white_draw);

            save_time1.add(timer.getText().toString());
            if(choice==2||choice==3||choice==4) time1= (int)_lblTimer1.getTag();
            else time1=timeMax;

            if(choice==2) time1+=2;
            timer=_lblTimer2;
            _lblTimer1.setTag(time1);
            _lblTimer2.setText(format(choice,time2));
            variants = king_variants(table, white_king);
            if (variants.contains(white_king)) white_check = true;
            else white_check = false;

            check_check = white_check;
            check_king = white_king;

        }
        else {
            hodcolor = "black";
            hod = false;
            _lbl1.setText("Ход черных");
            _checkDw.setChecked(black_draw);

            save_time2.add(timer.getText().toString());
            if(choice==2||choice==3||choice==4) time2= (int)_lblTimer2.getTag();
            else time2=timeMax;

            if(choice==2) time2+=2;
            timer=_lblTimer1;
            _lblTimer2.setTag(time2);
            _lblTimer1.setText(format(choice,time1));
            variants = king_variants(table, black_king);
            if (variants.contains(black_king)) black_check = true;
            else black_check = false;

            check_check = black_check;
            check_king = black_king;
        }

        //мат

        if(check_check) {
            proverka = figure_stay(table,check_king);
            mate=true;
            for(int i=1;i<9;i++) {
                for (int j=1;j<9;j++) {
                    cell = table.findViewById(i * 10 + j);
                    if (cell.getTag() != null) {
                        words = cell.getTag().toString().split(" ");
                        if (!words[0].equals(color1)) {

                            variants = figure_turns(table, cell.getId(), check_check, proverka, impsbl);
                            if (!variants.isEmpty()) {
                                _lbl2.setText("Шах");
                                e+="+";
                                mate = false;
                            }
                        }
                    }
                    if (!mate) break;
                }
                if(!mate) break;
            }

            if (mate) {
                setSave_table();
                setLang_hod();
                lang_hod+="#";
                lang.add(lang_hod);
                if(hod) End(21);
                else End(22);
                return;
            }
        }
        else {

            stalemate=true;
            for(int i=1;i<9;i++) {
                for (int j=1;j<9;j++) {
                    cell = table.findViewById(i * 10 + j);
                    if (cell.getTag() != null) {
                        words = cell.getTag().toString().split(" ");
                        if (!words[0].equals(color1)) {

                            variants = figure_turns(table, cell.getId(), check_check, proverka, impsbl);
                            if (!variants.isEmpty()) {
                                stalemate = false;
                            }
                        }
                    }
                    if (!stalemate) break;
                }
                if(!stalemate) break;
            }

            if(stalemate) {
                setSave_table();
                setLang_hod();
                lang_hod+="½";
                lang.add(lang_hod);
                if(hod) End(31);
                else End(32);
                return;
            }
            else  _lbl2.setText("");

        }

        save_table= new int[8][8];
        for(int i=1;i<9;i++) {
            for(int j=1;j<9;j++) {
                save_tag[i-1][j-1]=null;
                cell=table.findViewById(i*10+j);
                if(cell.getTag()!=null) {
                    words=cell.getTag().toString().split(" ");
                    save_table[i-1][j-1]=getResources().getIdentifier("drawable/"+words[0]+"_"+words[1],null,getPackageName());
                    save_tag[i-1][j-1]=cell.getTag().toString();
                }
            }
        }

        setLang_hod();
        lang.add(lang_hod);

        _timer1.cancel();
        _timer1= new Timer();
        _task1= new Task1();
        _timer1.schedule(_task1,1000,1000);

        save.add(save_table);
        game.save(Game.this);
    }
}
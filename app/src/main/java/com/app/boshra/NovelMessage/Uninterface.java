package com.app.boshra.NovelMessage;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Uninterface extends AppCompatActivity {
    private InterstitialAd add;

    private int ac = 0;
    private int ts = 1;
    private String serial = "";
    private EditText m;
    private Handler handler;
    private LinearLayout tabmain;
    private TextWatcher forsize;
    private InputMethodManager imm;
    private int CurrentSize = 0;
    private int var = 0;
    private int pos = 0;
    private SpannedString a;
    private SharedPreferences pref;
    private int s;
    private int hicolor;
    private int e;
    private int hi = 0;
    private Button hichoic;
    private Button highlight;
    private Button snap;
    private RelativeLayout main;
    private Bitmap bitmap;
    private Window window;
    private Boolean  snapped =false;
    Database db ;
    SQLiteDatabase sql ;
    Cursor c ;
    ContentValues values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                00);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //FOR UPPER BARS
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_uninterface);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        db = new Database(Uninterface.this);
        sql = db.getWritableDatabase();
        c = sql.rawQuery("select * from " + Database.MTable, null);
        values = new ContentValues();

        handler = new Handler();//TO KEEP CURSOR FROM DISSAPEARING
        m = findViewById(R.id.mi);


        if (c.moveToFirst()) {//retrival


            String TEXT = c.getString(c.getColumnIndex(Database.Text));
            String seriald= c.getString(c.getColumnIndex(Database.RICHText));
            int size= (c.getInt(c.getColumnIndex(Database.size)));


            SpannableString RichText= SpannableString.valueOf(TEXT);
            if (!seriald.isEmpty()) {
                String[] commas = seriald.split(",");
                for (int i = 0; i < commas.length; i++) {
                    int left = Integer.parseInt(commas[i].substring(0, commas[i].indexOf(" ")));
                    int right = Integer.parseInt(commas[i].substring(commas[i].indexOf(" ") + 1, commas[i].indexOf(" ", commas[i].indexOf(" ") + 1)));


//search on types in the commas[i] and span accordingly.
                    if (commas[i].contains("BI")) {
                        RichText.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), left, right, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (commas[i].contains("B")) {
                        RichText.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), left, right, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (commas[i].contains("I")) {
                        RichText.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), left, right, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (commas[i].contains("U")) {
                        RichText.setSpan((new UnderlineSpan()), left, right, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (commas[i].contains("-")) {
                        int color = Integer.parseInt(commas[i].substring(commas[i].indexOf("-"), commas[i].indexOf(" ", commas[i].indexOf("-"))));
                        RichText.setSpan((new BackgroundColorSpan(color)), left, right, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (commas[i].contains("s")) {
                        int Size = Integer.parseInt(commas[i].substring(commas[i].indexOf('s') + 1, commas[i].indexOf(" ", commas[i].indexOf('s') + 1)));
                        RichText.setSpan(new AbsoluteSizeSpan(Size), left, right, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                }

            }
            m.setTextSize((float)size);
            m.setText(RichText);



        }

        //ads
        MobileAds.initialize(this, "ca-app-pub-6007761255192812~3818916897");
        add = new InterstitialAd(this);
        add.setAdUnitId("ca-app-pub-6007761255192812/3296172712");
        add.loadAd(new AdRequest.Builder().build());

        add.setAdListener(new AdListener() {

public void onAdClosed() {

    add.loadAd(new AdRequest.Builder().build());
}
                          }
        );

        main = findViewById(R.id.mainv);

        tabmain = findViewById(R.id.tabmain);//FOR BUTTOM TAB
        Button inc = findViewById(R.id.inc);
        Button dec = findViewById(R.id.dec);
        Button text = findViewById(R.id.text);
        Button Font = findViewById(R.id.Font);
        Button Orientation = findViewById(R.id.or);
      snap = findViewById(R.id.snap);

        Button bold = findViewById(R.id.bold);
        Button itallic = findViewById(R.id.itallic);
        Button underline = findViewById(R.id.underline);
        highlight = findViewById(R.id.highlight);
        hichoic = findViewById(R.id.hichoice);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= 21) {// NOTIFICAITON BAR COLOR
            window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.LIGHTGREY));
        }
        pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        if (pref.contains("typ")) {
            typface(pref.getInt("typ", 0), -1);
        }


        if (pref.contains("DFS")) {//for setting defaut size

            m.setTextSize((int) pref.getFloat("DFS", (float) 0.0));
        }

        getSupportActionBar().hide();
        tabmain.setVisibility(View.GONE);

        m.setCursorVisible(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                tabmain.setVisibility(View.VISIBLE);
                getSupportActionBar().show();
                main.setLayoutTransition(null);
            }
        }, 1100);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.setCursorVisible(true);
                selection();

            }
        });




        m.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handler.removeCallbacksAndMessages(null);
                main.setLayoutTransition(null);



                m.requestFocus();
                pos = m.getOffsetForPosition(event.getX(), event.getY());

                if (m.hasSelection()) {


                    handler.postDelayed(new Runnable() {//after pressing when there is a selection
                        @Override
                        public void run() {
                            if (!m.hasSelection()) {
                                m.setCursorVisible(false);
                            }else
                                selection();

                        }
                    }, 200);

                } else {


                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            m.setCursorVisible(false);
                     }
                    }, 1980);


                }
                return false;

            }
        });

snap.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        snapped=true;
        snap.setVisibility(View.GONE);
        if(tabmain.getVisibility()==View.VISIBLE){
            tabmain.setVisibility(View.GONE);
        }
        capture(snap.getText().equals("📤")?1:2);
        Handler handler4 = new Handler();
        handler4.postDelayed(new Runnable() {//after pressing when there is a selection
            @Override
            public void run() {
                snap.setVisibility(View.VISIBLE);
            }
        }, 401);

    }
});

        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                main.getWindowVisibleDisplayFrame(r);
                int screenHeight = main.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;



                if (keypadHeight > 0) { // after
                    // keyboard is opened

                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


                } else {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                }


            }
        });


        Font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoge(2);
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s = m.getSelectionStart();
                e = m.getSelectionEnd();
                dialoge(1);
            }
        });
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incdec(2);

            }
        });
        Orientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialoge(3);

            }
        });
        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Spanning(-1, autoselection()[0], autoselection()[1]);
            }
        });

        itallic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Spanning(-4, autoselection()[0], autoselection()[1]);
            }
        });
        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Spanning(-3, autoselection()[0], autoselection()[1]);
            }
        });
        highlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hicolor = ((ColorDrawable) highlight.getBackground()).getColor();
                Spanning(-2, autoselection()[0], autoselection()[1]);//move!

            }
        });
        hichoic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hi = 1;
                dialoge(0);

            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incdec(1);


            }
        });
    }
    private void incdec(int det) {//changing text size

        switch (det) {
            case 1:
                var = -6;
                break;
            case 2:
                var = 6;
                break;
        }
        Spanning(0, autoselection()[0], autoselection()[1]);
    }

    private void serialization(){
        serial="";
//initiation
        AbsoluteSizeSpan[] spannedsize = m.getText().getSpans(0, m.length(), AbsoluteSizeSpan.class);
        StyleSpan[] spannedsize2 = m.getText().getSpans(0, m.length(), StyleSpan.class);
        BackgroundColorSpan[] spannedsize3 = m.getText().getSpans(0, m.length(), BackgroundColorSpan.class);
        UnderlineSpan[] spannedsize4 = m.getText().getSpans(0, m.length(), UnderlineSpan.class);

        int totallength = spannedsize.length + spannedsize2.length + spannedsize3.length + spannedsize4.length;
for (int i=0;i<totallength;i++){
    int startspan=-1,endspan=-1,startspan2=-1,endspan2=-1,startspan3=-1,endspan3=-1,startspan4=-1,endspan4=-1;
        if (spannedsize.length>i){
           startspan=m.getText().getSpanStart(spannedsize[i]);
            endspan=m.getText().getSpanEnd(spannedsize[i]);

            serial=serial+startspan+" "+endspan+" "+"s"+spannedsize[i].getSize();

        }
        if (spannedsize2.length>i) {
            startspan2 = m.getText().getSpanStart(spannedsize2[i]);
            endspan2 = m.getText().getSpanEnd(spannedsize2[i]);
            if (startspan == startspan2 && endspan == endspan2) {
                switch (spannedsize2[i].getStyle()) {
                    case Typeface.ITALIC:
                        serial = serial +" "+"I";
                        break;
                    case Typeface.BOLD:
                        serial = serial +" "+ "B";
                        break;
                    case Typeface.BOLD_ITALIC:
                        serial = serial +" "+ "BI";
                        break;
                }

            } else {

                switch (spannedsize2[i].getStyle()) {
                    case Typeface.ITALIC:
                        serial = serial + (serial.isEmpty() || serial.charAt(serial.length() - 1) == ',' ? "" : " ,") + startspan2 + " " + endspan2 + " " + "I";
                        break;
                    case Typeface.BOLD:
                        serial = serial + (serial.isEmpty() || serial.charAt(serial.length() - 1) == ',' ? "" : " ,") + startspan2 + " " + endspan2 + " " + "B";
                        break;
                    case Typeface.BOLD_ITALIC:
                        serial = serial + (serial.isEmpty() || serial.charAt(serial.length() - 1) == ',' ? "" : " ,") + startspan2 + " " + endspan2 + " " + "BI";
                        break;
                }


            }
            if (startspan > -1) {
                totallength--;
            }
        }

        if (spannedsize3.length>i){
            startspan3=m.getText().getSpanStart(spannedsize3[i]);
            endspan3=m.getText().getSpanEnd(spannedsize3[i]);
            if( startspan3==startspan2&&endspan3==endspan2||startspan3==startspan&&endspan3==endspan){
                spannedsize3[i].getBackgroundColor();
                serial=serial+" "+spannedsize3[i].getBackgroundColor();

            }else{
                serial=serial+(serial.isEmpty()||serial.charAt(serial.length()-1)==','?"":" ,")+startspan3+" "+endspan3+" "+spannedsize3[i].getBackgroundColor();

            }
            if(startspan>-1||startspan2>-1){
                totallength--;
            }
        }
    if (spannedsize4.length>i){
        startspan4=m.getText().getSpanStart(spannedsize4[i]);
        endspan4=m.getText().getSpanEnd(spannedsize4[i]);
        if( startspan3==startspan4&&endspan3==endspan4||startspan4==startspan2&&endspan4==endspan2||startspan4==startspan&&endspan4==endspan){
            serial=serial+" "+"U";

        }else{
            serial=serial+(serial.isEmpty()||serial.charAt(serial.length()-1)==','?"":" ,")+startspan4+" "+endspan4+" "+"U";

        }

        if(startspan>-1||startspan2>-1||startspan3>-1){
            totallength--;
        }
    }

        serial=serial+" ,";
    }
    }
    private void Spanning(int size, int ss, int se) {//Styling
        int st = m.getSelectionStart();
        int end = m.getSelectionEnd();
        int ind = -1;
        int oldei = -1;

        m.clearComposingText();
        int maindiff = 0;
        int colororig = hicolor;

        int colorchang = 0;
        String separation = "0";

        SpannableString selection = new SpannableString(m.getText().subSequence(ss, se));//selected part
        SpannableString selectionl = new SpannableString("");
        SpannableString selectionr = new SpannableString("");
        SpannedString newparts = new SpannedString("");

        AbsoluteSizeSpan[] spannedsize = m.getText().getSpans(ss, se, AbsoluteSizeSpan.class);
        StyleSpan[] spannedsize2 = m.getText().getSpans(ss, se, StyleSpan.class);
        BackgroundColorSpan[] spannedsize3 = m.getText().getSpans(ss, se, BackgroundColorSpan.class);
        UnderlineSpan[] spannedsize4 = m.getText().getSpans(ss, se, UnderlineSpan.class);
        //abs size spans selection
        int v = -1;

        if (size < 0) {
            for (int xc = 0; size == -2 ? xc < spannedsize3.length : size == -3 ? xc < spannedsize4.length : xc < spannedsize2.length; xc++) {//check removal
                int sps = m.getText().getSpanStart(size == -2 ? spannedsize3[xc] : size == -3 ? spannedsize4[xc] : spannedsize2[xc]);
                int spe = m.getText().getSpanEnd(size == -2 ? spannedsize3[xc] : size == -3 ? spannedsize4[xc] : spannedsize2[xc]);//endinf index
                if (size == -2) {
                    colororig = spannedsize3[xc].getBackgroundColor();
                    if (colororig != hicolor) {
                        colorchang = 1;
                    }
                } else if (size == -4 || size == -1) {
                    int vcheck = spannedsize2[xc].getStyle();
                    if ((size == -4 && vcheck == Typeface.BOLD || size == -1 && vcheck == Typeface.ITALIC)) {

                        if ((ss == sps && se == spe)) {
                            ind = 1;
                        } else
                            ind = -2;// difference exists

                    }
                }
                if (!(ss == sps && se == spe)) {
                    if (ss < spe && sps < ss && se>spe ) {// left spanned part within selection
                        maindiff = maindiff + (spe - ss);
                    } else if (se < spe && sps < se && ss<sps ) {//right //
                        maindiff = maindiff + (se - sps);
                    } else if(se-ss>spe-sps){
                    maindiff = maindiff + (spe - sps);// in selection
                }else
                    maindiff = maindiff + (se - ss);// outside selection
                } else {
                    maindiff = (se - ss);
                    break;
                }

            }
        }
        int sstyle = 0, shigh = 0, sunder = 0;
        int style = 0, high = 0, under = 0, abs = 0;

        int totallength = spannedsize.length + spannedsize2.length + spannedsize3.length + spannedsize4.length;
        int actualtotallength = totallength;

        for (int sp = 0; sp < totallength; sp++) {//for inside word
            int pst = -1;
            int pend = -1;
            int subvar = -1;
            int si = -1, ei = -1, si1 = -1, ei1 = -1, si2 = -1, ei2 = -1, si3 = -1, ei3 = -1;
            if (spannedsize.length > (sp + (high + style + under))) {

                si = m.getText().getSpanStart(spannedsize[sp + (high + style + under)]);
                ei = m.getText().getSpanEnd(spannedsize[sp + (high + style + under)]);//endinf index


                CurrentSize = spannedsize[sp + (high + style + under)].getSize();


                pst = si;
                pend = ei;


            }
            if (spannedsize4.length > sunder + (sp + (high + style + abs))) {

                si3 = m.getText().getSpanStart(spannedsize4[sunder + (sp + (high + style + abs))]);
                ei3 = m.getText().getSpanEnd(spannedsize4[sunder + (sp + (high + style + abs))]);//endinf index

                if (ei3 < pend) {
                    pend = ei3;
                    pst = si3;
                } else if (ei3 == pend) {
                    subvar = switchspans(size, sstyle, shigh, sunder, sp, high, under, abs, style);//for getting the right index of the span

                    totallength = totallength - 1;
                } else if (pend == -1) {
                    pst = si3;
                    pend = ei3;
                }
            }

            if (spannedsize2.length > sstyle + (sp + (high + under + abs))) {


                si1 = m.getText().getSpanStart(spannedsize2[sstyle + (sp + (high + under + abs))]);
                ei1 = m.getText().getSpanEnd(spannedsize2[sstyle + (sp + (high + under + abs))]);//endinf index
                v = spannedsize2[sstyle + (sp + (high + under + abs))].getStyle();

                if (ei1 < pend) {
                    pend = ei1;
                    pst = si1;

                } else if (ei1 == pend) {
                    subvar = switchspans(size, sstyle, shigh, sunder, sp, high, under, abs, style);
                    totallength = totallength - 1;

                } else if (pend == -1) {
                    pst = si1;
                    pend = ei1;
                }
            }
            if (spannedsize3.length > shigh + (sp + (style + under + abs))) {


                si2 = m.getText().getSpanStart(spannedsize3[shigh + (sp + (style + under + abs))]);
                ei2 = m.getText().getSpanEnd(spannedsize3[shigh + (sp + (style + under + abs))]);//endinf index
                colororig = spannedsize3[shigh + (sp + (style + under + abs))].getBackgroundColor();


                if (ei2 < pend) {
                    pend = ei2;
                    pst = si2;
                } else if (ei2 == pend) {
                    subvar = switchspans(size, sstyle, shigh, sunder, sp, high, under, abs, style);
                    totallength = totallength - 1;
                } else if (pend == -1) {
                    pst = si2;
                    pend = ei2;
                }
            }
            // for selecting the main index, shifting and  shifting similarities

            if (pend == ei) {
                if (subvar == -1) {
                    subvar = sp + (high + style + under);
                }
                abs--;
            }
            if (pend == ei1) {
                if (pend != ei) {
                    if (subvar == -1) {
                        subvar = (sstyle) + (sp + (high + under + abs));
                    }
                    style--;
                } else {//if there is a similarity
                    sstyle++;
                }
            }
            if (pend == ei2) {
                if (pend != ei && pend != ei1) {
                    if (subvar == -1) {
                        subvar = (shigh) + (sp + (style + under + abs));
                    }
                    high--;
                } else {
                    shigh++;
                }
            }
            if (pend == ei3) {
                if (pend != ei && pend != ei1 && pend != ei2) {
                    if (subvar == -1) {
                        subvar = (sunder) + (sp + (high + style + abs));
                    }
                    under--;
                } else {
                    sunder++;
                }
            }

            if (pst < ss && ss < pend && pend < se) {


                if (ss < ei && si < ss && se > ei) {//abs left


                    selectionl = new SpannableString(m.getText().subSequence(si, ss));
                    selectionl.removeSpan(spannedsize[subvar]);
                    separation = "l";
                    selectionl.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    pst = ss;
                    si = pst;

                }
                if (ss < ei1 && si1 < ss && se > ei1) {//STYLE left
                    if (!separation.contains("l")) {
                        selectionl = new SpannableString(m.getText().subSequence(si1, ss));
                        selectionl.removeSpan(spannedsize2[subvar]);
                    }
                    separation = "l";
                    if (v == Typeface.ITALIC) {//fot itallic
                        selectionl.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (v == Typeface.BOLD_ITALIC) {
                        selectionl.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else
                        selectionl.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    pst = ss;
                    si1 = pst;
                }
                if (ss < ei2 && si2 < ss && se > ei2) {// highlight left

                    if (!separation.contains("l")) {
                        selectionl = new SpannableString(m.getText().subSequence(si2, ss));
                        selectionl.removeSpan(spannedsize3[subvar]);
                    }
                    separation = "l";

                    selectionl.setSpan((new BackgroundColorSpan(colororig)), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    pst = ss;
                    si2 = pst;
                }
                if (ss < ei3 && si3 < ss && se > ei3) {// underline left

                    if (!separation.contains("l")) {
                        selectionl = new SpannableString(m.getText().subSequence(si3, ss));
                        selectionl.removeSpan(spannedsize4[subvar]);
                    }
                    separation = "l";

                    selectionl.setSpan((new UnderlineSpan()), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    pst = ss;
                    si3 = pst;
                }
            } else if (se < pend && pst < se && ss < pst) {

                if (se < ei && si < se && ss < si) {//abs right
                    selectionr = new SpannableString(m.getText().subSequence(si, se));
                    selectionr.removeSpan(spannedsize[subvar]);
                    separation = "r";
                    selectionr.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    pend = se;
                    ei = pend;
                }
                if (se < ei1 && si1 < se && ss < si1) {//STYLE right
                    if (!separation.contains("r")) {
                        selectionr = new SpannableString(m.getText().subSequence(si1, se));
                        selectionr.removeSpan(spannedsize2[subvar]);
                    }
                    separation = "r";
                    if (v == Typeface.ITALIC) {//fot itallic
                        selectionr.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (v == Typeface.BOLD_ITALIC) {
                        selectionr.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else
                        selectionr.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    pend = se;
                    ei1 = pend;
                }
                if (se < ei2 && si2 < se && ss < si2) {// highlight right
                    if (!separation.contains("r")) {
                        selectionr = new SpannableString(m.getText().subSequence(si2, se));
                        selectionr.removeSpan(spannedsize3[subvar]);
                    }
                    separation = "r";

                    selectionr.setSpan((new BackgroundColorSpan(colororig)), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    pend = se;
                    ei2 = pend;
                    Toast.makeText(this, String.valueOf(selectionr), Toast.LENGTH_SHORT).show();

                }
                if (se < ei3 && si3 < se && ss < si3) {// underline right

                    if (!separation.contains("r")) {
                        selectionr = new SpannableString(m.getText().subSequence(si3, se));
                        selectionr.removeSpan(spannedsize4[subvar]);
                    }
                    separation = "r";

                    selectionr.setSpan((new UnderlineSpan()), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    pend = se;
                    ei3 = pend;
                    Toast.makeText(this, String.valueOf(selectionr), Toast.LENGTH_SHORT).show();

                }

            }
            //separation implementation

            if (separation.equals("l") && sp == 0 || separation.equals("r")) {

                a = (SpannedString) TextUtils.concat(a.subSequence(0, ss - selectionl.length()), selectionl, a.subSequence(ss, selectionr.toString().isEmpty() ? m.length() : se - selectionr.length()), selectionr, selectionr.toString().isEmpty() ? "" : a.subSequence(se, m.length()));

                m.setText(a);
                //separated so they can only come in right

            }


            if ((ss > si && se <= ei || ss >= si && se < ei) || (ss > si3 && se <= ei3 || ss >= si3 && se < ei3) || ((ss > si1 && se <= ei1 || ss >= si1 && se < ei1)) || ((ss > si2 && se <= ei2 || ss >= si2 && se < ei2))) {//inword


                separation = "w";

                if ((ss > si && se <= ei || ss >= si && se < ei)) {

                    selectionl = new SpannableString(m.getText().subSequence(si, ss));
                    selectionl.removeSpan(spannedsize[subvar]);

                    if (si != ss) {

                        selectionl.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (ei != se) {
                        selection.removeSpan(spannedsize[subvar]);
                        selection.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//to be done anyways
                    }
                    a = (SpannedString) TextUtils.concat(m.getText().subSequence(0, si), selectionl, selection, m.getText().subSequence(se, m.length()));

                    if (ss > si) {//shifting indexis
                        pst = ss;
                    }
                    if (se < ei) {
                        pend = se;
                        ei = pend;
                    }

                }
                if ((ss > si1 && se <= ei1 || ss >= si1 && se < ei1)) {//styling


                    if (selectionl.toString().isEmpty()) {
                        selectionl = new SpannableString(m.getText().subSequence(si1, ss));
                    } else
                        selectionl.removeSpan(spannedsize2[subvar]);

                    if (si1 != ss) {
                        if (v == Typeface.ITALIC) {
                            selectionl.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else if (v == Typeface.BOLD) {
                            selectionl.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else
                            selectionl.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (ei1 != se) {
                        selection.removeSpan(spannedsize2[subvar]);
                        if (v == Typeface.BOLD) {

                            selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        if (v == Typeface.ITALIC) {

                            selection.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        if (v == Typeface.BOLD_ITALIC) {

                            selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                    a = (SpannedString) TextUtils.concat(m.getText().subSequence(0, si1), selectionl, selection, m.getText().subSequence(se, m.length()));

                    if (ss > si1) {
                        pst = ss;
                    }
                    if (se < ei1) {
                        pend = se;
                        ei1 = pend;
                    }

                }
                if ((ss > si2 && se <= ei2 || ss >= si2 && se < ei2)) {//highlight


                    if (selectionl.toString().isEmpty()) {
                        selectionl = new SpannableString(m.getText().subSequence(si2, ss));
                    } else
                        selectionl.removeSpan(spannedsize3[subvar]);

                    if (si2 != ss) {
                        selectionl.setSpan((new BackgroundColorSpan(colororig)), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (ei2 != se) {
                        selection.removeSpan(spannedsize3[subvar]);
                        selection.setSpan((new BackgroundColorSpan(colororig)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    a = (SpannedString) TextUtils.concat(m.getText().subSequence(0, si2), selectionl, selection, m.getText().subSequence(se, m.length()));

                    if (ss > si2) {
                        pst = ss;
                    }
                    if (se < ei2) {
                        pend = se;
                        ei2 = pend;
                    }
                }
                if ((ss > si3 && se <= ei3 || ss >= si3 && se < ei3)) {//underline


                    if (selectionl.toString().isEmpty()) {
                        selectionl = new SpannableString(m.getText().subSequence(si3, ss));
                    } else
                        selectionl.removeSpan(spannedsize4[subvar]);

                    if (si3 != ss) {
                        selectionl.setSpan((new UnderlineSpan()), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    if (ei3 != se) {
                        selection.removeSpan(spannedsize4[subvar]);
                        selection.setSpan((new UnderlineSpan()), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    a = (SpannedString) TextUtils.concat(m.getText().subSequence(0, si3), selectionl, selection, m.getText().subSequence(se, m.length()));

                    if (ss > si3) {
                        pst = ss;
                    }
                    if (se < ei3) {
                        pend = se;
                        ei3 = pend;
                    }
                }


                m.setText(a);


            }

            if (separation.equals("r") || separation.equals("w")) {//resettinf for word and right part
                spannedsize4 = m.getText().getSpans(ss, se, UnderlineSpan.class);//abs size spans selection
                spannedsize3 = m.getText().getSpans(ss, se, BackgroundColorSpan.class);//abs size spans selection
                spannedsize2 = m.getText().getSpans(ss, se, StyleSpan.class);//abs size spans selection
                spannedsize = m.getText().getSpans(ss, se, AbsoluteSizeSpan.class);//abs size spans selection
                if (separation.equals("r")) {//for addingg +ve or -ve
                    int newtotallength = spannedsize.length + spannedsize2.length + spannedsize3.length + spannedsize4.length;
                    if (actualtotallength != newtotallength) {
                        subvar = subvar - (actualtotallength - newtotallength);
                    }
                }
            }
            if ((size == 0 || size > 0) && pend == ei || (size == -4 || size == -1) && pend == ei1 || size == -2 && pend == ei2 || size == -3 && pend == ei3) {// if span is same type


                m.getText().removeSpan(size == -2 && ((maindiff == (se - ss) || colorchang == 1)) ? spannedsize3[subvar] : size == -2 ? null : size == -3 && maindiff == (se - ss) ? spannedsize4[subvar] : size < 0 && (maindiff == (se - ss) || ind == -2) ? spannedsize2[subvar] : size < 0 ? null : spannedsize[subvar]);//for all possabilities plus inf whole span is selected for style spans it gets removed


            }

            if (oldei >= 0 && pst - oldei > 1) {//get middle//
                if (size == 0) {// FOR middle empty
                    CurrentSize = (int) (m.getTextSize() + var);// input size
                }
                selection = new SpannableString(m.getText().subSequence(oldei + 1, pst));
                selection = empty(size, selection);
                newparts = (SpannedString) TextUtils.concat(newparts, selection);
            }
            oldei = pend - 1;

            selection = new SpannableString(m.getText().subSequence(pst, pend));

            if ((ind > -1 || (size == -3 ? pend != ei3 : size == -2 ? pend != ei2 : size < 0 && pend != ei1))) {//if no spans in same type


                if (ind > -1) {
                    selection.removeSpan(spannedsize2[subvar]);//remove the prev abs size span
                    m.getText().removeSpan(spannedsize2[subvar]);
                    selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (size == -1) {

                    selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                } else if (size == -4) {


                    selection.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (size == -3) {//underlinspan
                    selection.setSpan((new UnderlineSpan()), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (size == -2) {//underlinspan
                    selection.setSpan((new BackgroundColorSpan(hicolor)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }


            } else if ((size == -4 || size == -1) && (ind == -2 || maindiff == (se - ss) && spannedsize2[subvar].getStyle() == Typeface.BOLD_ITALIC)) {
                if (size == -1 && spannedsize2[subvar].getStyle() == Typeface.BOLD_ITALIC) {//bold
                    if (ind == -2) {

                        selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else
                        selection.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (size == -4 && spannedsize2[subvar].getStyle() == Typeface.BOLD_ITALIC) {//bold
                    if (ind == -2) {
                        selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else
                        selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if ((size == -1 && spannedsize2[subvar].getStyle() == Typeface.ITALIC) || (size == -4 && spannedsize2[subvar].getStyle() == Typeface.BOLD)) {//bold

                    selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if ((size == -1 && spannedsize2[subvar].getStyle() == Typeface.BOLD && ind == -2)) {
                    selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if ((size == -4 && spannedsize2[subvar].getStyle() == Typeface.ITALIC && ind == -2)) {
                    selection.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if (size == -2 && colorchang == 1) {//SPANS HIGHLIGHT AND NON HIGHLIGHT SPANS
                selection.setSpan((new BackgroundColorSpan(hicolor)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (size == 0 || size > 0) {//SPANS ABS SIZE,TOTAL SIZE AND NO SIZE SPANS
                switch (size) {
                    case 0:
                        if (pend == ei) {
                            if (spannedsize[subvar].getSize() + var < 0) {
                                CurrentSize = 1;
                            } else
                                CurrentSize = spannedsize[subvar].getSize() + var;//adding or sub
                        } else if (m.getTextSize() + var < 0) {
                            CurrentSize = 1;
                        } else
                            CurrentSize = (int) (m.getTextSize() + var);// input size
                        break;
                    default:
                        CurrentSize = size;

                        break;
                }
                selection.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (size == 0) {// FOR middle empty
                CurrentSize = (int) (m.getTextSize() + var);// input size
            }


            newparts = (SpannedString) TextUtils.concat(newparts, selection);


            if (sp == 0 && pst > 0 && ss < pst) {
                selection = new SpannableString(m.getText().subSequence(ss, pst));

                selection = empty(size, selection);
                newparts = (SpannedString) TextUtils.concat(selection, newparts);
            }
            if (sp == totallength - 1 && se > pend) {

                selection = new SpannableString(m.getText().subSequence(pend, se));
                pend = se;//to catch last char
                selection = empty(size, selection);
                newparts = (SpannedString) TextUtils.concat(newparts, selection);
            }


            a = (SpannedString) TextUtils.concat(m.getText().subSequence(0, ss), newparts, m.getText().subSequence(pend, m.length())
            );


        }
        if (totallength == 0) {
            if (size == -1) {

                selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (size == -4) {

                selection.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (size == -3) {//underlinspan
                selection.setSpan((new UnderlineSpan()), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (size == -2) {
                selection.setSpan((new BackgroundColorSpan(hicolor)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                if (size == 0) {
                    if (m.getTextSize() + var < 0) {
                        CurrentSize = 1;
                    } else
                        CurrentSize = (int) (m.getTextSize() + var);
                } else
                    CurrentSize = size;


                selection.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            a = (SpannedString) TextUtils.concat(m.getText().subSequence(0, ss), selection, m.getText().subSequence(se, m.length()));
        }

        m.setText(a);

        m.setSelection(st, end);

    }

    private SpannableString empty(int size, SpannableString selection) {//for empty parts
        switch (size) {
            case -1:
                selection.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case -3:
                selection.setSpan((new UnderlineSpan()), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case -2:
                selection.setSpan((new BackgroundColorSpan(hicolor)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case -4:
                selection.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            default:
                selection.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        return selection;
    }

    private int switchspans(int size, int sstyle, int shigh, int sunder, int sp, int high, int under, int abs, int style) {

        int subvar;
        switch (size) {
            case -1:
            case -4:
                subvar = (sstyle) + (sp + (high + under + abs));
                break;
            case -3:
                subvar = (sunder) + (sp + (high + style + abs));
                break;
            case -2:
                subvar = (shigh) + (sp + (style + under + abs));
                break;

            default:
                subvar = sp + (high + style + under);
                break;
        }

        return subvar;
    }

    private void afterselection() {
        //if there is a selection
try {
    m.setSelection(pos);
}catch(IndexOutOfBoundsException r){

        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        tabmain.setVisibility(View.GONE);
        imm.hideSoftInputFromWindow(m.getWindowToken(), 0);//Hide keyboard
        m.clearFocus();
        m.setCursorVisible(false);

    }

    private void dialoge(final int c) {


        final Dialog d = new Dialog(Uninterface.this);
        d.setContentView(R.layout.dialog);


        final CheckBox cb = d.findViewById(R.id.checkBox);

        cb.setVisibility(View.GONE);
        LinearLayout typface = d.findViewById(R.id.typface);
        typface.setVisibility(View.GONE);
        LinearLayout backcol = d.findViewById(R.id.backcol);
        Button set = d.findViewById(R.id.set);
        Button reset = d.findViewById(R.id.reset);
        final EditText size = d.findViewById(R.id.sn);

        Button smallcaps = d.findViewById(R.id.ssc);
        Button cond = d.findViewById(R.id.cond);
        Button serif = d.findViewById(R.id.serif);

        if (c == 3) {
            reset.setVisibility(View.GONE);
            set.setVisibility(View.GONE);
            size.setVisibility(View.GONE);
            backcol.setVisibility(View.GONE);
            typface.setVisibility(View.VISIBLE);
            LinearLayout top = d.findViewById(R.id.topf);
            top.setVisibility(View.GONE);
            smallcaps.setText("Left");
            cond.setText("Middle");
            serif.setText("Right");
        }
        if (c == 1) {// view text editing
            backcol.setVisibility(View.GONE);
            d.show();
            d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        } else if (c == 2) {
            backcol.setVisibility(View.GONE);
            reset.setVisibility(View.GONE);
            set.setVisibility(View.GONE);
            size.setVisibility(View.GONE);
            typface.setVisibility(View.VISIBLE);
            cb.setVisibility(View.VISIBLE);

            Button casual = d.findViewById(R.id.casual);
            Button cursive = d.findViewById(R.id.cursive);
            Button mono = d.findViewById(R.id.mono);


            casual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (cb.isChecked()) {
                        typface(1, 1);
                    } else
                        typface(1, -1);
                    d.dismiss();
                }
            });
            cursive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cb.isChecked()) {
                        typface(2, 2);
                    } else
                        typface(2, -1);
                    d.dismiss();
                }
            });

            mono.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cb.isChecked()) {
                        typface(5, 5);
                    } else
                        typface(5, -1);
                    d.dismiss();
                }
            });


        } else if (c == 0) {//view background//
            if (hi==1) {
                reset.setVisibility(View.GONE);
                set.setVisibility(View.GONE);
            }else{
                reset.setText("Picture");
                set.setText("Camera");
            }
            size.setVisibility(View.GONE);
            Button yellow = d.findViewById(R.id.yellow);
            Button lightblue = d.findViewById(R.id.lightblue);
            Button lightgreen = d.findViewById(R.id.lightgreen);
            Button red = d.findViewById(R.id.red);
            Button white = d.findViewById(R.id.white);


            Button lightpurpel = d.findViewById(R.id.lightpurpel);
            Button lightorang = d.findViewById(R.id.lightorange);
            Button pink = d.findViewById(R.id.pink);


            yellow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorset(R.color.Yello);
                    d.dismiss();
                }
            });
            lightblue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorset(R.color.Lightblue);

                    d.dismiss();
                }
            });

            lightgreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorset(R.color.Lightgreen);

                    d.dismiss();
                }
            });

            red.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorset(R.color.redishh);

                    d.dismiss();
                }
            });
            white.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorset(R.color.White);

                    d.dismiss();
                }
            });
            lightpurpel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorset(R.color.Lightpurpel);

                    d.dismiss();
                }
            });

            lightorang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorset(R.color.Lightorange);

                    d.dismiss();
                }
            });

            pink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorset(R.color.pink);

                    d.dismiss();
                }
            });
        }
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        forsize = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!size.getText().toString().isEmpty()) {
           ts = (int) Float.parseFloat(size.getText().toString()) == 0 ? 1 : (int) Float.parseFloat(size.getText().toString());
                    if (e - s >= 1) {//set new span size to selection if text selected
                        m.setSelection(s, e);
                        Spanning(ts, s, e);
                    } else {

                        m.setTextSize((float) (ts/1.5));

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        size.addTextChangedListener(forsize);
// for singularity with orientation
        smallcaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

if (c==3){
    m.setGravity(Gravity.START);
}else {
    if (cb.isChecked()) {
        typface(6, 6);
    } else
        typface(6, -1);
    d.dismiss();
}
            }
        });
        cond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (c==3){
                    m.setGravity(Gravity.CENTER);
                }else {
                    if (cb.isChecked()) {
                        typface(4, 4);
                    } else
                        typface(4, -1);
                    d.dismiss();
                }
            }
        });
        serif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (c==3){
                    m.setGravity(Gravity.END);
                }else {
                    if (cb.isChecked()) {
                        typface(3, 3);
                    } else
                        typface(3, -1);
                    d.dismiss();
                }
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(c==0){
                    Intent img = new Intent (); //Your Intent
                    img.setAction(MediaStore.ACTION_IMAGE_CAPTURE); //the intents action to capture the image
                    startActivityForResult(img,2);
                    d.dismiss();
                    return;
                }
                m.setTextSize((float) (ts/1.5));
                pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putFloat("DFS", ts);
                editor.apply();
                m.removeTextChangedListener(forsize);

                d.dismiss();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
if(c==0){
    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
    photoPickerIntent.setType("image/*");
    startActivityForResult(photoPickerIntent,1);
    d.dismiss();
    return;
}

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putFloat("DFS", 75);
                editor.apply();

                m.setTextSize(75);

                d.dismiss();
            }
        });
        d.show();
    }

    private void typface(int n, int d) {

        switch (n) {
            case 1:

                m.setTypeface(Typeface.create("casual", Typeface.NORMAL));
                break;
            case 2:

                m.setTypeface(Typeface.create("cursive", Typeface.NORMAL));
                break;
            case 3:

                m.setTypeface(Typeface.create("serif", Typeface.NORMAL));
                break;
            case 4:

                m.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                break;
            case 5:

                m.setTypeface(Typeface.create("serif-monospace", Typeface.NORMAL));
                break;
            case 6:

                m.setTypeface(Typeface.create("normal", Typeface.NORMAL));
                break;
        }
        if (d != -1) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("typ", d);

            editor.apply();
        }

    }
private void colorset(int col){
    if (hi == 0) {
        if (Build.VERSION.SDK_INT < 23) {
            m.setBackgroundColor(getResources().getColor(col));
            tabmain.setBackgroundColor(getResources().getColor(col));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(col)));
        } else {
            m.setBackgroundColor(getColor(col));
            tabmain.setBackgroundColor(getColor(col));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor(col)));
        }


        if (android.os.Build.VERSION.SDK_INT >= 21) {

            window.setStatusBarColor(this.getResources().getColor(col));
        }

    } else {
        if (Build.VERSION.SDK_INT < 23) {
            hichoic.setBackgroundColor(getResources().getColor(col));
            highlight.setBackgroundColor(getResources().getColor(col));
        }else {
            hichoic.setBackgroundColor(getColor(col));
            highlight.setBackgroundColor(getColor(col));
        }
    }

}

    private int[] autoselection() {


        int selectionStart = m.getSelectionStart();
        int selectionEnd = m.getSelectionEnd();


        int lastIndex;
        int indexOf;
        int[] sel = new int[2];
        if (selectionStart < selectionEnd) {//choice is if there is a selection but I want that of the word
            sel[0] = selectionStart;
            sel[1] = selectionEnd;
            return sel;
        }
        lastIndex = m.getText().toString().lastIndexOf(" ", selectionStart - 1);
        int lastIndex2 = m.getText().toString().lastIndexOf('\n', selectionStart - 1);
        if (lastIndex == -1 || lastIndex2 > lastIndex) {
            lastIndex = lastIndex2;

        }
        indexOf = m.getText().toString().indexOf(" ", lastIndex + 1);
        int indexOf2 = m.getText().toString().indexOf('\n', lastIndex + 1);
        if (indexOf == -1 || indexOf2 < indexOf && indexOf2 != -1) {
            indexOf = indexOf2;
        }


        sel[0] = lastIndex + 1;
        sel[1] = indexOf == -1 ? m.getText().toString().length() : indexOf;
        return sel;

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.clear) {
            m.getText().clearSpans();
            m.setText("");
            pos=0;

        }

        if (id == R.id.select) {
            if (m.getText().toString().isEmpty()) {
                selection();
            } else {
                Editable text = m.getText();
                text.replace(0, 1, text.subSequence(0, 1), 0, 1);
                m.selectAll();
                selection();
            }
        }
        if (id == R.id.bg) {
            hi = 0;
            dialoge(0);
        }
        if (id == R.id.snapshot) {
            snap.setVisibility(View.VISIBLE);
            snap.setText("📷");
           afterselection();
        }
        if (id == R.id.HIDE) {

            afterselection();


        }
        if (id == R.id.share) {
            snap.setVisibility(View.VISIBLE);
            snap.setText("📤");
            afterselection();
        }
        if (id == R.id.ac) {
            if (ac == 0) {
                ac = 1;
                m.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_FILTER | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                Toast.makeText(this, "Disabled text autocorrection", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Enabled text autocorrection", Toast.LENGTH_SHORT).show();

                m.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                ac = 0;
            }
        }


        return super.onOptionsItemSelected(item);
    }

    private void selection() {//for selecting text
        tabmain.setVisibility(View.VISIBLE);


    }

    private void capture(final int d) {

        Handler handler3 = new Handler();

        handler3.postDelayed(new Runnable() {//after pressing when there is a selection
            @Override
            public void run() {
                View v1 = getWindow().getDecorView().getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap ss = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false);


                if (d == 1) {
                    savebit(ss);
                } else
                    shareBitmap(ss);
            }
        }, 400);

    }

    private void savebit(Bitmap bitmap) {

        try {


            String savedImageURL= MediaStore.Images.Media.insertImage(
                    getContentResolver(),
                    bitmap,""
                    , "");


            Toast.makeText(this, "Snapshot Saved in Gallery", Toast.LENGTH_LONG).show();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            Toast.makeText(this, "Cannot save without permission", Toast.LENGTH_LONG).show();

            e.printStackTrace();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    00);
        }
    }

    private void shareBitmap(Bitmap bitmap) {
        try {

            File file = new File(this.getExternalCacheDir(), "logicchip.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share image via"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onBackPressed() {


        if (tabmain.getVisibility() == View.VISIBLE && !getSupportActionBar().isShowing()) {//to view it all

            afterselection();

        } else if (tabmain.getVisibility() == View.GONE && !getSupportActionBar().isShowing()) {
            int  p= (int) (Math.random() * 101);
            if (add.isLoaded()&&snap.getVisibility() == View.VISIBLE&&snap.getText()==""&&snapped) {
                if (p <= 95) {
                    add.show();
                }
            }else
            if (add.isLoaded()&&snap.getVisibility() == View.VISIBLE&&snap.getText()=="[+]"&&snapped) {
                if(p<=65) {
                    add.show();
                }
            }else{
                if(p<=17&&snap.getVisibility() == View.GONE) {
                    add.show();
                }
            }
            snapped=false;
                snap.setVisibility(View.GONE);
            
            tabmain.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
        } else {// if both are shown
            if (tabmain.getVisibility() == View.VISIBLE ) {
                tabmain.setVisibility(View.GONE);


                // DATABASE saving
                serialization();

                values.put(Database.Text, m.getText().toString());
                values.put(Database.size, m.getTextSize());
                values.put(Database.RICHText, serial);
                if(c.getCount()==0) {
                    sql.insert(Database.MTable, null, values);
                }else
                    sql.update(Database.MTable, values, null, null);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();


        } else

                onBackPressed();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case 1:
                    Uri selectedImage = data.getData();
                    try {
                         bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
                case 2:
                    bitmap= (Bitmap)data.getExtras().get("data"); //get data and casts it into Bitmap photo
break;
            }
        Drawable pic=new BitmapDrawable(getResources(), bitmap);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            m.setBackground(pic);
        }
else
            m.setBackgroundDrawable(pic);

    }
}


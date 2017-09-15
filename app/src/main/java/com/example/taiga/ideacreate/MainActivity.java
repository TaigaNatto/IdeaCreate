package com.example.taiga.ideacreate;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    public static final int COLOR_NUM=7;
    int[] color;

    TextView mainText;
    LinearLayout linearLayout;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainText=(TextView)findViewById(R.id.text);
        linearLayout=(LinearLayout)findViewById(R.id.layout);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        color=new int[COLOR_NUM];
        color[0]=R.color.goodRed;
        color[1]=R.color.goodBlue;
        color[2]=R.color.goodGreen;
        color[3]=R.color.goodYellow;
        color[4]=R.color.goodPurple;
        color[5]=R.color.goodBrown;
        color[6]=R.color.goodBlack;

        mainText.setText(getResultText());
    }

    public void change(View v){
        //乱数を生成
        Random r=new Random();
        Resources res = getResources();
        linearLayout.setBackgroundColor(res.getColor(color[r.nextInt(COLOR_NUM)]));
        mainText.setText(getResultText());
    }

    public void settings(View v){
        Intent intent=new Intent(this,ResistActivity.class);
        startActivity(intent);
    }

    //データを２つ取得してランダムに合成して返すメソッド
    public String getResultText(){
        //検索用のメモクエリ作成
        RealmQuery<RealmFactorEntity> query = realm.where(RealmFactorEntity.class);
        //インスタンス生成し、検索されたメモを取得する
        RealmResults<RealmFactorEntity> results = query.findAll();

        if(results.size()!=0) {
            //乱数を生成
            Random r = new Random();

            String text1 = results.get(r.nextInt(results.size())).getText();
            String text2 = results.get(r.nextInt(results.size())).getText();

            return text1 + "\n × \n" + text2;
        }
        return "No Data";
    }
}

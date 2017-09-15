package com.example.taiga.ideacreate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ResistActivity extends AppCompatActivity {

    ListView listView;
    EditText editText;

    ArrayAdapter arrayAdapter;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resist);

        listView=(ListView)findViewById(R.id.list);
        editText=(EditText)findViewById(R.id.edit_text);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1);

        //検索用のメモクエリ作成
        RealmQuery<RealmFactorEntity> query = realm.where(RealmFactorEntity.class);
        //インスタンス生成し、検索されたメモを取得する
        RealmResults<RealmFactorEntity> results = query.findAll();

        for (int i=0;i<results.size();i++){
            arrayAdapter.add(results.get(i).getText());
        }

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(getDeleteListener());
    }

    public void add(View v){
        String text=editText.getText().toString();
        if(text.length()!=0) {
            //トランザクション開始
            realm.beginTransaction();
            //インスタンスを生成
            RealmFactorEntity model = realm.createObject(RealmFactorEntity.class);
            //書き込みたいデータをインスタンスに入れる
            model.setText(text);
            //トランザクション終了 (データを書き込む)
            realm.commitTransaction();

            arrayAdapter.add(text);
            listView.setAdapter(arrayAdapter);

            editText.setText("");
        }
        else{
            Toast.makeText(this,"Type text !",Toast.LENGTH_SHORT);
        }
    }

    private AdapterView.OnItemClickListener getDeleteListener(){
        final AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String deleteText=arrayAdapter.getItem(i).toString();

                // クエリを発行
                RealmQuery<RealmFactorEntity> delQuery  = realm.where(RealmFactorEntity.class);
                //消したいデータを指定 (以下の場合はmemoデータの「memo」が「test」のものを指定)
                delQuery.equalTo("text",deleteText);
                //指定されたデータを持つデータのみに絞り込む
                final RealmResults<RealmFactorEntity> delR = delQuery.findAll();
                // 変更操作はトランザクションの中で実行する必要あり
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        // すべてのオブジェクトを削除
                        delR.deleteAllFromRealm();
                    }
                });

                arrayAdapter.remove(deleteText);
                listView.setAdapter(arrayAdapter);
            }
        };
        return listener;
    }
}

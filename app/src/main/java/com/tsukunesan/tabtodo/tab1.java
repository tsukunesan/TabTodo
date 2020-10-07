package com.tsukunesan.tabtodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab1 extends Fragment {

    public tab1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // コンテキストを取得
        final Context context = container.getContext();
        // レイアウトファイル取得
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        // レイアウト定義
        Button btnName1 = view.findViewById(R.id.btnName1);
        final Button btnTab1 = view.findViewById(R.id.btnTab1);
        Button btnTab2 = view.findViewById(R.id.btnTab2);
        Button btnTab3 = view.findViewById(R.id.btnTab3);
        Button createButton = view.findViewById(R.id.createButton);
        final Button deleteButton = view.findViewById(R.id.deleteButton);
        final EditText todoEdit = view.findViewById(R.id.todoEdit);
        final ListView todoList = view.findViewById(R.id.todoList);
        final EditText txtTab1 = view.findViewById(R.id.txtTab1);
        // Todoの項目List定義
        final ArrayList<String> data = new ArrayList<>();
        // チェックリスト定義
        final ArrayList<Integer> chkIndex = new ArrayList<>();

        // データベースヘルパー
        final todoDatabaseHelper helper = new todoDatabaseHelper(context);
        // データベースからデータを取得
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cs = db.query("todos", null, null, null, null, null, null);
        if (cs.moveToFirst()) {
            if (cs.getInt(0) == 1) {
                data.add(cs.getString(1));
            }
            while (cs.moveToNext()) {
                if (cs.getInt(0) == 1) {
                    data.add(cs.getString(1));
                }
            }
        }

        // todoListにデータ挿入
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_multiple_choice, data);
        todoList.setAdapter(adapter);

        // ボタン名を設定
        //buttonデータベースから値を取得
        Cursor cs2 = db.query("btnnames", null, null, null, null, null, null);
        if(cs2.moveToFirst()){
            btnTab1.setText(cs2.getString(0));
            btnTab2.setText(cs2.getString(1));
            btnTab3.setText(cs2.getString(2));
            txtTab1.setText(cs2.getString(0));
        }

        // リストがクリックされたときのイベントリスナー
        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int count = 0;
                // List要素数だけループ
                for (Integer i = 0; i < adapter.getCount(); i++) {
                    // チェック項目確認
                    if (todoList.isItemChecked(i)) {
                        // deleteButtonを表示
                        deleteButton.setVisibility(View.VISIBLE);
                        count++;
                        if (!chkIndex.contains(i)) {
                            chkIndex.add(i);
                            Log.d("chkIndex", String.valueOf(chkIndex));
                        }
                    } else if (chkIndex.contains(i)) {
                        chkIndex.remove(i);
                        Log.d("chkIndex", String.valueOf(chkIndex));
                    }
                    // deleteButton非表示
                    if (count == 0) {
                        deleteButton.setVisibility(View.INVISIBLE);
                        count = 0;
                    }
                }
            }
        });

        // タブ名称変更ボタンのクリックイベントリスナー
        btnName1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // データベースにデータ挿入
                // EditTextに入力がないときはToast表示
                if (txtTab1.getText().toString().matches("")) {
                    Toast.makeText(context, "タブ名称が入力されていません", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = helper.getReadableDatabase();
                    ContentValues cv = new ContentValues();
                    //　contentviewオブジェクトに各カラムの値を保持
                    cv.put("btn1", txtTab1.getText().toString());
                    db.update("btnnames", cv, null,null);
                    Cursor cs2 = db.query("btnnames", null, null, null, null, null, null);
                    if(cs2.moveToFirst()){
                        btnTab1.setText(cs2.getString(0));
                    }
                }
            }
        });

        // createButtonのクリックイベントリスナー
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // データベースにデータ挿入
                // EditTextに入力がないときはToast表示
                if (todoEdit.getText().toString().matches("")) {
                    Toast.makeText(context, "Todoが入力されていません", Toast.LENGTH_SHORT).show();
                } else {
                    try (SQLiteDatabase db = helper.getWritableDatabase()) {
                        ContentValues cv = new ContentValues();
                        //　contentviewオブジェクトに各カラムの値を保持
                        cv.put("tab", 1);
                        cv.put("todo1", todoEdit.getText().toString());
                        db.insert("todos", null, cv);
                        Cursor cs = db.query("todos", null, null, null, null, null, null);
                        if (cs.moveToLast()) {
                            if (cs.getInt(0) == 1) {
                                data.add(cs.getString(1));
                            }
                        }
                    }
                    // リスト再描画
                    todoEdit.setText("", TextView.BufferType.NORMAL);
                    todoList.setAdapter(adapter);
                }

            }
        });

        // deleteButtonのクリックイベントリスナー
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chkIndexをソート（IndexOutOfBoundsExceptionエラーを避けるため）
                Collections.sort(chkIndex, Collections.<Integer>reverseOrder());
                for (int chkData : chkIndex) {
                    Log.d("chkData", String.valueOf(chkData));
                    // チェックマークを削除
                    todoList.setItemChecked(chkData, false);
                    // 削除するdataを取得＆削除
                    String deleteData = data.get(chkData);
                    String[] deletelist = new String[1];
                    deletelist[0] = deleteData;
                    try (SQLiteDatabase db = helper.getWritableDatabase()) {
                        db.delete("todos", "todo1=?", deletelist);
                    }
                    adapter.remove(deleteData);
                }
                // チェック項目リセット&deleteButton非表示
                chkIndex.clear();
                deleteButton.setVisibility(View.INVISIBLE);
            }
        });

        // タブ切り替え(Clickイベントリスナー)
        btnTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.tab1);
            }
        });

        btnTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.tab2);
            }
        });

        btnTab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.tab3);
            }
        });

        return view;
    }
}

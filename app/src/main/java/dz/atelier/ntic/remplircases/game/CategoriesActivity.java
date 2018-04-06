package dz.atelier.ntic.remplircases.game;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import dz.atelier.ntic.remplircases.game.adapter.MyAdapter;
import dz.atelier.ntic.remplircases.game.model.RowData;

public class CategoriesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView listview;
    List<RowData> rowDatas;
    String main_title[];
    TypedArray img_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        getSupportActionBar().setTitle("Categories");

        listview= (ListView)findViewById(R.id.listviewID);
        listview.setOnItemClickListener(this);
        rowDatas = new ArrayList<RowData>();
        main_title = getResources().getStringArray(R.array.main_title);
        img_title = getResources().obtainTypedArray(R.array.image_title);


        for(int i=0;i<main_title.length;i++){

            RowData rowDat = new RowData(main_title[i],img_title.getResourceId(i,-1));
            rowDatas.add(rowDat);
        }

        MyAdapter myAdapter = new MyAdapter(getApplicationContext(),rowDatas);
        myAdapter.notifyDataSetChanged();
        listview.setAdapter(myAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent myintent = new Intent(this,Level_Difficult.class);
        myintent.putExtra("data",rowDatas.get(i));
        startActivity(myintent);

    }
}

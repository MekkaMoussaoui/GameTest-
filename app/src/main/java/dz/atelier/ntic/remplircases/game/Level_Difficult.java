package dz.atelier.ntic.remplircases.game;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;


import dz.atelier.ntic.remplircases.game.model.RowData;

public class Level_Difficult extends AppCompatActivity {

    String arrayName []= {"Beginner",
                           "Moderate",
                            "Expert"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level__difficult);
        TextView textView = (TextView)findViewById(R.id.titleID);

        Bundle bundle= getIntent().getExtras();
        RowData rowData =(RowData)bundle.getSerializable("data");

        textView.setText(rowData.getMain_title() + " Quiz");


    }
}

package dz.atelier.ntic.remplircases.game.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dz.atelier.ntic.remplircases.game.R;
import dz.atelier.ntic.remplircases.game.model.RowData;

/**
 * Created by Mékkah on 22/03/2018.
 */

public class MyAdapter extends BaseAdapter {

    Context applicationContext;
    List<RowData>rowDatas;
    public MyAdapter(Context applicationContext, List<RowData>rowDatas){
        this.applicationContext=applicationContext;
        this.rowDatas=rowDatas;

    }
    @Override
    public int getCount() {
        return rowDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return rowDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class MyViewHolder{
        ImageView imageview;
        TextView textView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder myViewHolder= null;
        if(view == null){
            LayoutInflater layoutInflater=(LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view= layoutInflater.inflate(R.layout.row_data,null);
            myViewHolder= new MyViewHolder();
            myViewHolder.imageview=(ImageView) view.findViewById(R.id.list_image);
            myViewHolder.textView=(TextView) view.findViewById(R.id.title);
            RowData rowData= rowDatas.get(i);
            myViewHolder.imageview.setImageResource(rowData.getImg_title());
            myViewHolder.textView.setText(rowData.getMain_title());
            view.setTag(myViewHolder);
        }

        else {
            myViewHolder =(MyViewHolder)view.getTag();

        }
        return view;
    }
}

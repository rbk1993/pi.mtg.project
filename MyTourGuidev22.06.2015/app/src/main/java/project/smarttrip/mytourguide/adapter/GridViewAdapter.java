package project.smarttrip.mytourguide.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import project.smarttrip.mytourguide.R;

/**
 * Created by invite on 15/06/15.
 */
public class GridViewAdapter extends BaseAdapter {

    String[] result;
    Context context;
    int[] imageId;
    private LayoutInflater inflater=null;
    public GridViewAdapter(Activity mainActivity, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result= prgmNameList;
        context=mainActivity;
        imageId= prgmImages;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return result[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }



    public class Holder
    {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.program_list, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

        holder.tv.setText(result[position].replace("_"," "));
        holder.img.setImageResource(imageId[position]);

        return rowView;
    }



}

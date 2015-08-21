package com.sanafoundation.sanjaym.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanafoundation.sanjaym.R;
import com.sanafoundation.sanjaym.model.People;
import com.sanafoundation.sanjaym.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IM028 on 7/9/15.
 */
public class PeopleAdapter extends BaseAdapter {

    Fragment fragment;
    Context context;
    LayoutInflater inflater;
    ImageLoader imageLoader;
    private List<People> peopleList = null;
    private ArrayList<People> arraylist;

    public PeopleAdapter(Context context, List<People> peopleList) {
        this.context = context;
        this.peopleList = peopleList;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<People>();
        this.arraylist.addAll(peopleList);
        //imageLoader = new ImageLoader(context);
    }

    public class ViewHolder {
        TextView name;
        TextView message;
        ImageView proPic;
    }

    @Override
    public int getCount() {
        return peopleList.size();
    }

    @Override
    public Object getItem(int position) {
        return peopleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.people_row, null);

            holder.name = (TextView) view.findViewById(R.id.peopleName);
            holder.message = (TextView) view.findViewById(R.id.message);
            holder.proPic = (ImageView) view.findViewById(R.id.peoplePic);

            String objectValue;
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(peopleList.get(position).getName());
        holder.message.setText(peopleList.get(position).getLastMessage());

        /*if(peopleList.get(position).getProfilePic()!=null) {
            imageLoader.DisplayImage(peopleList.get(position).getProfilePic(), holder.proPic);
        }else{
            holder.proPic.setImageResource(R.drawable.place_holder);
        }*/

        holder.proPic.setImageBitmap(peopleList.get(position).getPic());
        //imageLoader.DisplayBitmap(peopleList.get(position).getPic(), holder.proPic);

        return view;
    }
}

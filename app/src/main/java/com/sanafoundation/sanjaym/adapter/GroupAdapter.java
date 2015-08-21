package com.sanafoundation.sanjaym.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanafoundation.sanjaym.R;
import com.sanafoundation.sanjaym.model.Group;
import com.sanafoundation.sanjaym.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IM028 on 7/9/15.
 */
public class GroupAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ImageLoader imageLoader;
    private List<Group> groupList = null;
    private ArrayList<Group> arraylist;

    public GroupAdapter(Context context, List<Group> groupList) {
        this.context = context;
        this.groupList = groupList;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<Group>();
        this.arraylist.addAll(groupList);
        //imageLoader = new ImageLoader(context);
    }

    public class ViewHolder {
        TextView title;
        ImageView group_icon;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupList.get(position);
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
            view = inflater.inflate(R.layout.group_row, null);

            holder.title= (TextView) view.findViewById(R.id.groupTitle);
            holder.group_icon= (ImageView) view.findViewById(R.id.groupPic);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        holder.title.setText(groupList.get(position).getGroup_name());
        holder.group_icon.setImageBitmap(groupList.get(position).getPic());
        //imageLoader.DisplayImage(groupList.get(position).getGroup_icon(),holder.group_icon);

        return view;
    }
}

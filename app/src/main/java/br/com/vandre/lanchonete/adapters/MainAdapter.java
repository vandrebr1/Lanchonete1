package br.com.vandre.lanchonete.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.vandre.lanchonete.R;

public class MainAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<String> values;

    public MainAdapter(Context context, ArrayList<String> values) {
        inflater = LayoutInflater.from(context);
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public String getItem(int index) {
        return values.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ItemSuporte itemHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.layout_item_mainmenu, parent,
                    false);

            itemHolder = new ItemSuporte();
            itemHolder.tvMenuItem = view.findViewById(R.id.mainmenu_tvMenuItem);

            view.setTag(itemHolder);

        } else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        itemHolder.tvMenuItem.setText(values.get(position));

        return view;
    }

    private class ItemSuporte {
        TextView tvMenuItem;
    }

}

package br.com.vandre.lanchonete.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.vandre.lanchonete.R;
import br.com.vandre.lanchonete.util.Funcoes;

public class PromocaoAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<String> values;

    public PromocaoAdapter(Context context, ArrayList<String> values) {
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
            view = inflater.inflate(R.layout.layout_item_promocao, parent,
                    false);

            itemHolder = new ItemSuporte();
            itemHolder.tvPromocao = view.findViewById(R.id.promocaoitem_tvPromocao);

            view.setTag(itemHolder);

        } else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        if (values.get(position) == "") {
            itemHolder.tvPromocao.setText("Não há promoções no momento");
        } else {
            itemHolder.tvPromocao.setText(Funcoes.fromHtml(values.get(position)));
        }

        return view;
    }

    private class ItemSuporte {
        TextView tvPromocao;
    }

}

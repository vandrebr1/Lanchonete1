package br.com.vandre.lanchonete.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.vandre.lanchonete.R;

public class GrupoAdapter extends BaseAdapter {

    static class ViewHolder {
        private TextView tvGrupo;
    }

    List<String> grupos;
    LayoutInflater layoutInflater;
    ViewHolder viewHolder;

    public GrupoAdapter() {
        super();
    }

    public GrupoAdapter(Context context, List<String> grupos) {
        layoutInflater = LayoutInflater.from(context);
        this.grupos = grupos;
    }

    @Override
    public int getCount() {
        return grupos.size();
    }

    @Override
    public Object getItem(int index) {
        return grupos.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int posicao, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_item_grupo, arg2, false);
            viewHolder = new ViewHolder();

            viewHolder.tvGrupo = convertView.findViewById(R.id.itemgrupo_tvGrupo);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String grupo = grupos.get(posicao);

        viewHolder.tvGrupo.setText(grupo);

        return convertView;
    }
}

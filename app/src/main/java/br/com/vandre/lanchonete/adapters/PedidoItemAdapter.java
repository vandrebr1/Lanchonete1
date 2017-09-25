package br.com.vandre.lanchonete.adapters;


import android.content.Context;
import android.graphics.Paint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import br.com.vandre.lanchonete.R;
import br.com.vandre.lanchonete.modelo.PedidoItem;
import br.com.vandre.lanchonete.util.Funcoes;

public class PedidoItemAdapter extends BaseAdapter {

    static class ViewHolder {
        private TextView tvProduto;
        private TextView tvQuantidade;
        private TextView tvIngrediente;
        private TextView tvPreco;
        private TextView tvPrecoPromocao;
    }

    SparseBooleanArray mSelectedItemsIds;
    LayoutInflater layoutInflater;
    ViewHolder viewHolder;
    List<PedidoItem> pedidoItens;

    public PedidoItemAdapter(Context context, List<PedidoItem> pedidoItens) {
        mSelectedItemsIds = new SparseBooleanArray();
        layoutInflater = LayoutInflater.from(context);
        this.pedidoItens = pedidoItens;
    }

    @Override
    public int getCount() {
        return pedidoItens.size();
    }

    @Override
    public Object getItem(int index) {
        return pedidoItens.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(final int posicao, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_item_pedido, arg2, false);
            viewHolder = new ViewHolder();

            viewHolder.tvProduto = convertView.findViewById(R.id.itempedido_tvProduto);
            viewHolder.tvQuantidade = convertView.findViewById(R.id.itempedido_tvQuantidade);
            viewHolder.tvIngrediente = convertView.findViewById(R.id.itempedido_tvIngredientes);
            viewHolder.tvPreco = convertView.findViewById(R.id.itempedido_tvPreco);
            viewHolder.tvPrecoPromocao = convertView.findViewById(R.id.itempedido_tvPrecoPromocao);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PedidoItem pedidoItem = pedidoItens.get(posicao);

        viewHolder.tvProduto.setText(pedidoItem.getProduto());
        viewHolder.tvQuantidade.setText("Qtde.: " + Funcoes.formatarDecimal(pedidoItem.getQuantidade(), 0));
        viewHolder.tvIngrediente.setText(pedidoItem.getIngredientes());
        viewHolder.tvPreco.setText(Funcoes.formatarMoeda(pedidoItem.getPreco()));

        precoPromocao(pedidoItem);
        pedidoItem.setPrecoPromocao(precoPromocao(pedidoItem));

        if (pedidoItem.getPrecoPromocao() == 0.0) {
            viewHolder.tvPrecoPromocao.setVisibility(View.GONE);
        } else {
            viewHolder.tvPreco.setPaintFlags(viewHolder.tvPreco.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        viewHolder.tvPrecoPromocao.setText(Funcoes.formatarMoeda(pedidoItem.getPrecoPromocao()));


        return convertView;
    }

    private double precoPromocao(PedidoItem pedidoItem) {
        boolean isLight;
        int porcoesDeCarne = 0;
        int porcoesDeQueijo = 0;

        Double precoLight;
        Double precoPromocaoCarne;
        Double precoPromocaoQueijo;
        Double precoPromocao = 0.0;

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(pedidoItem.getIngredientesJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        isLight = isLight(jsonArray);

        /*
        Parei aqui
        porcoesDeCarne = porcoesDeCarne(jsonArray);
        porcoesDeQueijo = porcoesDeQueijo(jsonArray);
        */

        if (isLight) {
            precoLight = (pedidoItem.getPreco() - (pedidoItem.getPreco() * 0.1));
            precoPromocao = precoLight;
        }

        return precoPromocao;
    }

    private boolean isLight(JSONArray jsonArray) {

        boolean isAlface;
        boolean isBacon;

        isAlface = jsonArray.toString().contains("1");
        isBacon = jsonArray.toString().contains("2");

        if (isBacon) {
            return false;
        } else if (isAlface) {
            return true;
        }

        return false;

    }

    private int porcoesDeCarne(JSONArray jsonArray) {

        return StringUtils.countMatches(jsonArray.toString(), "3");

    }

    private int porcoesDeQueijo(JSONArray jsonArray) {

        return StringUtils.countMatches(jsonArray.toString(), "5");

    }


}

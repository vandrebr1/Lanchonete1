package br.com.vandre.lanchonete.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.vandre.lanchonete.R;
import br.com.vandre.lanchonete.app.LanchoneteAplicacao;
import br.com.vandre.lanchonete.atividades.MonteSeuLancheActivity;
import br.com.vandre.lanchonete.modelo.Ingrediente;
import br.com.vandre.lanchonete.util.DynamicImageView;
import br.com.vandre.lanchonete.util.Funcoes;

public class MonteSeuLancheAdapter extends BaseAdapter {

    static class ViewHolder {
        private DynamicImageView ivImagem;
        private TextView tvProduto;
        private TextView tvPreco;
        private Button btnRemover;

    }

    List<Ingrediente> ingredientes;
    LayoutInflater layoutInflater;
    private final Picasso imageLoader;
    ViewHolder viewHolder;
    ListView lv;
    private MonteSeuLancheActivity monteSeuLancheActivity;
    LanchoneteAplicacao app;

    public MonteSeuLancheAdapter(Context context, MonteSeuLancheActivity monteSeuLancheActivity, List<Ingrediente> ingredientes, ListView lv, LanchoneteAplicacao app) {
        layoutInflater = LayoutInflater.from(context);
        this.ingredientes = ingredientes;
        this.imageLoader = Picasso.with(context);
        this.lv = lv;
        this.monteSeuLancheActivity = monteSeuLancheActivity;
        this.app = app;
    }

    @Override
    public int getCount() {
        return ingredientes.size();
    }

    @Override
    public Object getItem(int index) {
        return ingredientes.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(final int posicao, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_item_monteseulanche, arg2, false);
            viewHolder = new ViewHolder();

            viewHolder.ivImagem = convertView.findViewById(R.id.itemmonteseulanche_ivImagem);
            viewHolder.tvProduto = convertView.findViewById(R.id.itemmonteseulanche_tvProduto);
            viewHolder.tvPreco = convertView.findViewById(R.id.itemmonteseulanche_tvPreco);
            viewHolder.btnRemover = convertView.findViewById(R.id.itemmonteseulanche_btnRemover);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Ingrediente ingrediente = ingredientes.get(posicao);

        imageLoader.load(ingrediente.getImagem()).fit().centerCrop().placeholder(R.drawable.no_image).into(viewHolder.ivImagem);
        viewHolder.tvProduto.setText(ingrediente.getProduto());
        viewHolder.tvPreco.setText(Funcoes.formatarMoeda(ingrediente.getPreco()));

        viewHolder.btnRemover.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (monteSeuLancheActivity != null) {
                    monteSeuLancheActivity.apagarItemLista(posicao);
                }
            }
        });

        return convertView;
    }
}

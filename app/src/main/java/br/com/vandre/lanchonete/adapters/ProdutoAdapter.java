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
import br.com.vandre.lanchonete.atividades.CardapioActivity;
import br.com.vandre.lanchonete.modelo.Produto;
import br.com.vandre.lanchonete.util.DynamicImageView;
import br.com.vandre.lanchonete.util.Funcoes;

public class ProdutoAdapter extends BaseAdapter {

    static class ViewHolder {
        private DynamicImageView ivImagem;
        private TextView tvProduto;
        private TextView tvInformacoes;
        private TextView tvPreco;
        private Button btnEditarIngredientes;
        private Button btnQueroEste;

    }

    List<Produto> produtos;
    LayoutInflater layoutInflater;
    private final Picasso imageLoader;
    ViewHolder viewHolder;
    ListView lv;
    private int tamanhoInformacoes = 0;
    private CardapioActivity cardapioActivity;
    LanchoneteAplicacao app;

    public ProdutoAdapter(Context context, CardapioActivity cardapioActivity, List<Produto> produtos, ListView lv, LanchoneteAplicacao app) {
        layoutInflater = LayoutInflater.from(context);
        this.produtos = produtos;
        this.imageLoader = Picasso.with(context);
        this.lv = lv;
        this.cardapioActivity = cardapioActivity;
        this.app = app;
    }

    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Object getItem(int index) {
        return produtos.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(final int posicao, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_item_produto, arg2, false);
            viewHolder = new ViewHolder();

            viewHolder.ivImagem = convertView.findViewById(R.id.itemproduto_ivFoto);
            viewHolder.tvProduto = convertView.findViewById(R.id.itemproduto_tvProduto);
            viewHolder.tvPreco = convertView.findViewById(R.id.itemproduto_tvPreco);
            viewHolder.tvInformacoes = convertView.findViewById(R.id.itemproduto_tvInformacoes);
            viewHolder.btnEditarIngredientes = convertView.findViewById(R.id.itemproduto_btnEditarIngredientes);
            viewHolder.btnQueroEste = convertView.findViewById(R.id.itemproduto_btnQueroEste);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Produto produto = produtos.get(posicao);

        imageLoader.load(produto.getImagem()).fit().centerCrop().placeholder(R.drawable.no_image).into(viewHolder.ivImagem);
        viewHolder.tvProduto.setText(produto.getProduto());
        viewHolder.tvPreco.setText(Funcoes.formatarMoeda(produto.getPreco()));
        viewHolder.tvInformacoes.setText(produto.getProduto() + "\n" + produto.getIngredientes());

        if (!produto.getIngredientes().equals("")) {
            viewHolder.tvInformacoes.setVisibility(View.VISIBLE);

            if (produto.isAtivarScroll()) {
                lv.smoothScrollToPositionFromTop(posicao, tamanhoInformacoes, 500);
                produto.setAtivarScroll(false);

            }
        } else {
            viewHolder.tvInformacoes.setVisibility(View.GONE);
        }

        viewHolder.btnEditarIngredientes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cardapioActivity != null) {
                    cardapioActivity.editarIngredientes(produto);
                }
            }
        });

        viewHolder.btnQueroEste.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (cardapioActivity != null) {
                    cardapioActivity.adicionarProduto(produto);
                }
            }
        });

        return convertView;
    }
}

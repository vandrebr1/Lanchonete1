package br.com.vandre.lanchonete.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.vandre.lanchonete.R;
import br.com.vandre.lanchonete.atividades.MonteSeuLancheActivity;
import br.com.vandre.lanchonete.modelo.Ingrediente;
import br.com.vandre.lanchonete.util.DynamicImageView;
import br.com.vandre.lanchonete.util.Funcoes;

public class IngredientesAdapter extends RecyclerView.Adapter<IngredientesAdapter.MyViewHolder> {

    private List<Ingrediente> ingredientes;
    private final Picasso imageLoader;
    private MonteSeuLancheActivity monteSeuLancheActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private DynamicImageView ivImagem;
        private TextView tvProduto;
        private TextView tvPreco;
        private Button btnAdicionar;

        public MyViewHolder(View view) {
            super(view);
            ivImagem = view.findViewById(R.id.itemingrediente_ivImagem);
            tvProduto = view.findViewById(R.id.itemingrediente_tvProduto);
            tvPreco = view.findViewById(R.id.itemingrediente_tvPreco);
            btnAdicionar = view.findViewById(R.id.itemingrediente_btnAdicionar);

        }
    }


    public IngredientesAdapter(Context context, List<Ingrediente> ingredientes, MonteSeuLancheActivity monteSeuLancheActivity) {
        this.ingredientes = ingredientes;
        this.imageLoader = Picasso.with(context);
        this.monteSeuLancheActivity = monteSeuLancheActivity;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_ingrediente, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Ingrediente ingrediente = ingredientes.get(position);

        imageLoader.load(ingrediente.getImagem()).fit().centerCrop().placeholder(R.drawable.no_image).into(holder.ivImagem);
        holder.tvProduto.setText(ingrediente.getProduto());
        holder.tvPreco.setText(Funcoes.formatarMoeda(ingrediente.getPreco()));

        holder.btnAdicionar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (monteSeuLancheActivity != null) {
                    monteSeuLancheActivity.adicionarIngrediente(ingrediente);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return ingredientes.size();
    }
}
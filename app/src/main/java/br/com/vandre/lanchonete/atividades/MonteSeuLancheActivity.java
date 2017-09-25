package br.com.vandre.lanchonete.atividades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.vandre.lanchonete.R;
import br.com.vandre.lanchonete.adapters.IngredientesAdapter;
import br.com.vandre.lanchonete.adapters.MonteSeuLancheAdapter;
import br.com.vandre.lanchonete.app.Constantes;
import br.com.vandre.lanchonete.app.LanchoneteAplicacao;
import br.com.vandre.lanchonete.dblocal.LocalDao;
import br.com.vandre.lanchonete.dblocal.LocalFactoryDB;
import br.com.vandre.lanchonete.modelo.Ingrediente;
import br.com.vandre.lanchonete.modelo.PedidoItem;
import br.com.vandre.lanchonete.modelo.Produto;
import br.com.vandre.lanchonete.util.Funcoes;

public class MonteSeuLancheActivity extends AppCompatActivity {

    List<Ingrediente> ingredientes;
    RecyclerView rcvIngredientes;
    List<PedidoItem> pedidoItens;
    Produto produto;

    ListView lvIngredientesAdicionados;
    List<Ingrediente> ingredientesAdicionados;
    MonteSeuLancheAdapter monteSeuLancheAdapter;
    TextView tvTotal;

    LanchoneteAplicacao app;
    ProgressDialog dialog;
    Handler handler;

    Double total = 0.0;

    static boolean isClique = false;

    private void declarar() {
        app = (LanchoneteAplicacao) getApplication();
        handler = new Handler();
        rcvIngredientes = (RecyclerView) findViewById(R.id.ingredientes_rcvIngredientes);
        tvTotal = (TextView) findViewById(R.id.ingredientes_tvTotal);
        lvIngredientesAdicionados = (ListView) findViewById(R.id.ingredientes_lvIngredientesAdicionado);
        produto = new Produto();
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MonteSeuLancheActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rcvIngredientes.setLayoutManager(horizontalLayoutManagaer);

        pedidoItens = new ArrayList<>();


    }

    private void iniciar() {
        declarar();

        Intent intent = getIntent();

        ingredientesAdicionados = new ArrayList<>();


        if (intent != null) {
            pedidoItens = (ArrayList<PedidoItem>) getIntent().getSerializableExtra(Constantes.PEDIDOITENS);
            produto = (Produto) getIntent().getSerializableExtra(Constantes.PRODUTO);
        }

        if (pedidoItens == null) {
            pedidoItens = new ArrayList<>();

        }

        if (produto == null) {
            produto = new Produto();

        } else {
            procuraIngredientesPadrao();
        }

        monteSeuLancheAdapter = new MonteSeuLancheAdapter(MonteSeuLancheActivity.this, this, ingredientesAdicionados, lvIngredientesAdicionados, app);
        lvIngredientesAdicionados.setAdapter(monteSeuLancheAdapter);
        calcularTotal();

        procurar();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ingredientes);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.texturamadeiraescura));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        iniciar();
    }


    public void apagarItemLista(final int position) {

        AlertDialog.Builder builder = Funcoes.mensagem(MonteSeuLancheActivity.this, getResources().getString(R.string.exclusao), false);

        builder.setPositiveButton(R.string.sim,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dl, int which) {
                        ingredientesAdicionados.remove(position);
                        setMonteSeuLancheAdapter();
                    }
                });

        builder.setNegativeButton(R.string.nao, null);
        builder.create().show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_monteseucardapio, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.monteseupedido_salvar:
                if (!isClique) {
                    isClique = true;
                    salvar();
                    isClique = false;
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void salvar() {
        if (ingredientesAdicionados.size() > 0) {
            adicionarProduto();
        } else {
            Funcoes.mensagem(this, "Nenhum ingrediente adicionado", true);
        }
    }

    @Override
    public void onBackPressed() {
        if (pedidoItens != null) {
            Intent intent = new Intent();
            intent.putExtra(Constantes.PEDIDOITENS, (ArrayList<PedidoItem>) pedidoItens);
            setResult(RESULT_OK, intent);
            MonteSeuLancheActivity.this.finish();
        } else {
            MonteSeuLancheActivity.this.finish();

        }
    }


    private void atualizarTela() {
        handler.post(new Runnable() {
            @Override
            public void run() {

                setIngredienteAdapter();

                dialog.dismiss();
            }
        });
    }

    private void procurar() {
        dialog = Funcoes.mensagemAguarde(MonteSeuLancheActivity.this);

        new Thread() {
            @Override
            public void run() {
                procuraIngredientes();

                atualizarTela();
            }
        }.start();
    }

    private void procuraIngredientes() {

        StringBuilder comando = new StringBuilder();
        LocalDao<Ingrediente> ingredienteLocalDao = LocalFactoryDB.getDao(MonteSeuLancheActivity.this, Ingrediente.class);
        comando.append("SELECT * FROM ").append(ingredienteLocalDao.getTableName());

        String query = comando.toString();

        ingredientes = ingredienteLocalDao.selectAll(query);

    }


    private void procuraIngredientesPadrao() {

        StringBuilder comando = new StringBuilder();
        LocalDao<Ingrediente> ingredienteLocalDao = LocalFactoryDB.getDao(MonteSeuLancheActivity.this, Ingrediente.class);
        comando.append("SELECT * FROM ").append(ingredienteLocalDao.getTableName());

        if (produto.getIngredientes() != null) {
            if (!produto.getIngredientes().equals("")) {
                String ingredientes = "";

                ingredientes = produto.getIngredientes().replace(", ", "', '");
                comando.append(" WHERE produto IN('").append(ingredientes).append("')");
            }
        }


        String query = comando.toString();

        ingredientesAdicionados = ingredienteLocalDao.selectAll(query);

    }

    private void setIngredienteAdapter() {
        if (ingredientes != null) {
            if (ingredientes.size() > 0) {
                IngredientesAdapter ingredientesAdapter = new IngredientesAdapter(MonteSeuLancheActivity.this, ingredientes, this);
                rcvIngredientes.setAdapter(ingredientesAdapter);
            }
        }
    }

    public void adicionarIngrediente(final Ingrediente ingrediente) {

        ingredientesAdicionados.add(0, ingrediente);
        setMonteSeuLancheAdapter();
    }

    private void setMonteSeuLancheAdapter() {
        if (ingredientesAdicionados != null) {
            monteSeuLancheAdapter.notifyDataSetChanged();
            calcularTotal();

        }
    }

    private void calcularTotal() {
        total = 0.0;

        for (Ingrediente ingrediente : ingredientesAdicionados) {
            total = total + ingrediente.getPreco();
        }
        tvTotal.setText(Funcoes.formatarMoeda(total));
    }

    public void adicionarProduto() {
        LayoutInflater inflater = this.getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = inflater.inflate(R.layout.layout_dialog_adicionarmonteseulanche, null);

        final NumberPicker numberPicker = dialogView.findViewById(R.id.dialogadicionarmonteseulanche_numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(99999);

        builder.setTitle("Lanche personalizado")
                .setCancelable(false)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String ingredientesJson = "";
                        String ingredientesNome = "";

                        for (Ingrediente ingrediente : ingredientesAdicionados) {

                            if (ingredientesJson.equals("")) {
                                ingredientesJson = "[" + ingrediente.getCodigo();
                                ingredientesNome = ingrediente.getProduto();
                            } else {
                                ingredientesJson = ingredientesJson + ", " + ingrediente.getCodigo();
                                ingredientesNome = ingredientesNome + ", " + ingrediente.getProduto();
                            }

                        }

                        ingredientesJson = ingredientesJson + "]";

                        PedidoItem pedidoItem = new PedidoItem(Long.parseLong("99999"), "Personalizado", numberPicker.getValue(), ingredientesNome, total, ingredientesJson);
                        pedidoItens.add(pedidoItem);

                        onBackPressed();

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("Log", "Cancel");

                    }
                });

        builder.create().show();

    }


}
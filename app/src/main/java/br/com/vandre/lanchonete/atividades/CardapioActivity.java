package br.com.vandre.lanchonete.atividades;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.vandre.lanchonete.MainActivity;
import br.com.vandre.lanchonete.R;
import br.com.vandre.lanchonete.adapters.GrupoAdapter;
import br.com.vandre.lanchonete.adapters.ProdutoAdapter;
import br.com.vandre.lanchonete.adapters.PromocaoAdapter;
import br.com.vandre.lanchonete.app.Constantes;
import br.com.vandre.lanchonete.app.LanchoneteAplicacao;
import br.com.vandre.lanchonete.app.ProcuraConstantes;
import br.com.vandre.lanchonete.app.RequestConstantes;
import br.com.vandre.lanchonete.dblocal.LocalDao;
import br.com.vandre.lanchonete.dblocal.LocalFactoryDB;
import br.com.vandre.lanchonete.modelo.Grupo;
import br.com.vandre.lanchonete.modelo.PedidoItem;
import br.com.vandre.lanchonete.modelo.Produto;
import br.com.vandre.lanchonete.network.RequisicoesHTTP;
import br.com.vandre.lanchonete.util.FontAwesomeDrawable;
import br.com.vandre.lanchonete.util.Funcoes;

public class CardapioActivity extends AppCompatActivity {

    ArrayList<String> grupos;
    List<Produto> produtos;
    List<PedidoItem> pedidoItens;
    ProgressDialog dialog;
    Handler handler;
    ListView lvGrupo;
    ListView lvProduto;
    Grupo grupo;
    Menu menu;
    LanchoneteAplicacao app;

    private ArrayList<String> promocoes = new ArrayList<String>();

    static boolean isClique = false;

    private void declarar() {
        lvGrupo = (ListView) findViewById(R.id.cardapiogrupo_lvGrupo);
        lvProduto = (ListView) findViewById(R.id.cardapioproduto_lvProduto);
        pedidoItens = new ArrayList<>();
        handler = new Handler();
        grupo = new Grupo();
        grupos = new ArrayList<String>();
        app = (LanchoneteAplicacao) getApplicationContext();

        RequisicoesHTTP requisicoesHTTP = new RequisicoesHTTP(this);

        try {
            requisicoesHTTP.httpPromocoes(promocoes);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void iniciar() {
        declarar();

        lvGrupo.setOnItemClickListener(new LvGrupoItemClickListener());
        lvProduto.setOnItemClickListener(new LvProdutoItemClickListener());

        procurar(ProcuraConstantes.CAMPO_GRUPO);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cardapio);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.texturamadeiraescura));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iniciar();
    }

    private void procurar(final String tipoProcura) {
        dialog = Funcoes.mensagemAguarde(CardapioActivity.this);

        new Thread() {
            @Override
            public void run() {
                switch (tipoProcura) {
                    case ProcuraConstantes.CAMPO_GRUPO:
                        procuraGrupos();
                        break;

                    case ProcuraConstantes.CAMPO_PRODUTO:
                        procuraProdutos();
                        break;

                    case ProcuraConstantes.CAMPO_PROMOCOES:
                        procuraPromocoes();
                        break;
                }
                atualizarTela(tipoProcura);
            }
        }.start();
    }

    private void atualizarTela(final String tipoProcura) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                switch (tipoProcura) {
                    case ProcuraConstantes.CAMPO_GRUPO:
                        if (grupos != null) {
                            if (grupos.size() > 0) {
                                GrupoAdapter grupoadapter = new GrupoAdapter(CardapioActivity.this, grupos);
                                SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(grupoadapter);
                                animationAdapter.setAbsListView(lvGrupo);
                                lvGrupo.setAdapter(animationAdapter);

                            }
                        }
                        break;

                    case ProcuraConstantes.CAMPO_PRODUTO:
                        setProdutoAdapter();
                        break;

                    case ProcuraConstantes.CAMPO_PROMOCOES:
                        setPromocaoAdapter();
                        break;

                }
                dialog.dismiss();
            }
        });
    }

    private class LvGrupoItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String grupoNome = (String) parent.getItemAtPosition(position);
            if (!isClique) {
                isClique = true;
                if (grupoNome.equals(ProcuraConstantes.CAMPO_MONTESEULANCHE)) {

                    Intent intent = new Intent(CardapioActivity.this, MonteSeuLancheActivity.class);
                    intent.putExtra(Constantes.PEDIDOITENS, (ArrayList<PedidoItem>) pedidoItens);
                    startActivityForResult(intent, RequestConstantes.REQUEST_MONTESEUPEDIDDO);

                } else if (grupoNome.equals(ProcuraConstantes.CAMPO_PROMOCOES)) {
                    procurar(grupoNome);
                } else {
                    procurar(ProcuraConstantes.CAMPO_PRODUTO);
                }
                isClique = false;

            }
        }
    }

    private class LvProdutoItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    private void procuraGrupos() {
        grupos.add("Lanches");
        grupos.add(ProcuraConstantes.CAMPO_PROMOCOES);
        grupos.add(ProcuraConstantes.CAMPO_MONTESEULANCHE);

    }

    private void procuraPromocoes() {

        promocoes.size();

    }

    private void procuraProdutos() {
        StringBuilder comando = new StringBuilder();

        LocalDao<Produto> produtoLocalDao = LocalFactoryDB.getDao(CardapioActivity.this, Produto.class);

        comando.append("SELECT produtos.codigo AS codigo, produtos.produto AS produto, SUM(ingredientes.preco) AS preco,")
                .append(" GROUP_CONCAT(ingredientes.produto, ', ') AS ingredientes , produtos.imagem AS imagem,")
                .append(" produtos.ingredientes as ingredientesJson")
                .append(" FROM ").append(produtoLocalDao.getTableName())
                .append(" INNER JOIN produtosingredientes")
                .append(" ON produtos.codigo=produtosingredientes.produtoID")
                .append(" INNER JOIN ingredientes")
                .append(" ON ingredientes.codigo=produtosingredientes.ingredienteID")
                .append(" GROUP BY produtos.codigo, produtos.produto, produtos.imagem");

        String query = comando.toString();

        produtos = produtoLocalDao.selectAll(query);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cardapio, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        alterarIconeCarrinho();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.acItens:
                if (!isClique) {
                    isClique = true;
                    intent = new Intent(this, PedidoActivity.class);
                    intent.putExtra(Constantes.PEDIDOITENS, (ArrayList<PedidoItem>) pedidoItens);
                    startActivityForResult(intent, RequestConstantes.REQUEST_VISUALIZARCARRINHO);

                    isClique = false;
                    return true;

                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        LayoutInflater inflater = this.getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = inflater.inflate(R.layout.layout_dialog_senha, null);

        final EditText edtComplemento = dialogView.findViewById(R.id.dialogsenha_edtSenha);

        builder.setTitle("Configurações Avançadas")
                .setMessage("Digite a senha: 1234")
                .setCancelable(false)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (edtComplemento.getText().toString().equals("1234")) {
                            Intent intent = new Intent(CardapioActivity.this, MainActivity.class);
                            startActivityForResult(intent, RequestConstantes.REQUEST_MAINACTIVITE);

                        }

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("Log", "Cancel");

                    }
                });

        builder.create().show();
    }

    private void setProdutoAdapter() {
        if (produtos != null) {
            if (produtos.size() > 0) {
                ProdutoAdapter produtoAdapter = new ProdutoAdapter(CardapioActivity.this, this, produtos, lvProduto, app);
                ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(produtoAdapter);
                animationAdapter.setAbsListView(lvProduto);
                lvProduto.setAdapter(animationAdapter);

            }
        }
    }

    private void setPromocaoAdapter() {
        if (promocoes != null) {
            if (promocoes.size() > 0) {

                PromocaoAdapter promocaoAdapter = new PromocaoAdapter(CardapioActivity.this, promocoes);
                ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(promocaoAdapter);
                animationAdapter.setAbsListView(lvProduto);
                lvProduto.setAdapter(animationAdapter);

            }
        }
    }

    public void adicionarProduto(final Produto produto) {
        LayoutInflater inflater = this.getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = inflater.inflate(R.layout.layout_dialog_adicionar, null);

        final NumberPicker numberPicker = dialogView.findViewById(R.id.dialogadicionar_numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(99999);

        builder.setTitle(produto.getProduto().toUpperCase())
                .setCancelable(false)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        PedidoItem pedidoItem = new PedidoItem(produto.getCodigo(), produto.getProduto(), numberPicker.getValue(), produto.getIngredientes(), produto.getPreco(), produto.getIngredientesJson());
                        pedidoItens.add(pedidoItem);

                        alterarIconeCarrinho();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("Log", "Cancel");

                    }
                });

        builder.create().show();

    }

    public void editarIngredientes(Produto produto) {
        Intent intent = new Intent(CardapioActivity.this, MonteSeuLancheActivity.class);
        intent.putExtra(Constantes.PRODUTO, produto);
        intent.putExtra(Constantes.PEDIDOITENS, (ArrayList<PedidoItem>) pedidoItens);
        startActivityForResult(intent, RequestConstantes.REQUEST_MONTESEUPEDIDDO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isClique = false;
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();

            switch (requestCode) {
                case RequestConstantes.REQUEST_VISUALIZARCARRINHO:
                case RequestConstantes.REQUEST_MONTESEUPEDIDDO:
                    pedidoItens.clear();
                    pedidoItens = (ArrayList<PedidoItem>) bundle.getSerializable(Constantes.PEDIDOITENS);
                    alterarIconeCarrinho();

                    break;

                case RequestConstantes.REQUEST_MAINACTIVITE:
                    finish();
                    startActivity(new Intent(CardapioActivity.this, SplashActivity.class));

                    break;

            }
        }
    }

    private void alterarIconeCarrinho() {
        FontAwesomeDrawable drable;

        if (pedidoItens.size() <= 0) {
            drable = new FontAwesomeDrawable.DrawableAwesomeBuilder(CardapioActivity.this, R.string.icone_shoppingcart).build();
        } else {
            drable = new FontAwesomeDrawable.DrawableAwesomeBuilder(CardapioActivity.this, R.string.icone_shoppingcartplus).build();
        }

        menu.findItem(R.id.acItens).setIcon(drable);
    }

}
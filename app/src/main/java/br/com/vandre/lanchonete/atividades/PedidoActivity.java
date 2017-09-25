package br.com.vandre.lanchonete.atividades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.vandre.lanchonete.R;
import br.com.vandre.lanchonete.adapters.PedidoItemAdapter;
import br.com.vandre.lanchonete.app.Constantes;
import br.com.vandre.lanchonete.app.LanchoneteAplicacao;
import br.com.vandre.lanchonete.modelo.PedidoItem;
import br.com.vandre.lanchonete.network.RequisicoesHTTP;
import br.com.vandre.lanchonete.util.Funcoes;

public class PedidoActivity extends AppCompatActivity {

    List<PedidoItem> pedidoItens;
    ListView lvPedidoItens;
    PedidoItemAdapter pedidoItemAdapter;
    LanchoneteAplicacao app;
    ProgressDialog dialog;
    Handler handler;
    RequisicoesHTTP requisicoesHTTP;

    static boolean isClique = false;

    private void declarar() {
        app = (LanchoneteAplicacao) getApplication();
        handler = new Handler();
        lvPedidoItens = (ListView) findViewById(R.id.pedido_lvPedidoItem);

        requisicoesHTTP = new RequisicoesHTTP(this);

    }

    private void iniciar() {
        declarar();

        Intent intent = getIntent();

        if (intent != null) {
            pedidoItens = (ArrayList<PedidoItem>) getIntent().getSerializableExtra(Constantes.PEDIDOITENS);

            if (pedidoItens != null) {
                if (pedidoItens.size() > 0) {
                    pedidoItemAdapter = new PedidoItemAdapter(PedidoActivity.this, pedidoItens);

                    lvPedidoItens.setAdapter(pedidoItemAdapter);
                    lvPedidoItens.setOnItemLongClickListener(new ApagarItemLista());
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pedido);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.texturamadeiraescura));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        iniciar();
    }

    @Override
    public void onBackPressed() {
        if (pedidoItens != null) {
            Intent intent = new Intent();
            intent.putExtra(Constantes.PEDIDOITENS, (ArrayList<PedidoItem>) pedidoItens);
            setResult(RESULT_OK, intent);
            PedidoActivity.this.finish();
        }
    }

    private class ApagarItemLista implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       final int position, long arg3) {

            AlertDialog.Builder builder = Funcoes.mensagem(PedidoActivity.this, getResources().getString(R.string.exclusao), false);

            builder.setPositiveButton(R.string.sim,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dl, int which) {
                            pedidoItens.remove(position);
                            pedidoItemAdapter.notifyDataSetChanged();
                        }
                    });

            builder.setNegativeButton(R.string.nao, null);
            builder.create().show();

            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pedido, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.configuracoes_acPedir:
                if (!isClique) {
                    isClique = true;
                    pedir();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void pedir() {
        if (validar()) {
            dialog = Funcoes.mensagemAguarde(PedidoActivity.this);

            new Thread() {
                @Override
                public void run() {
                    try {
                        enviarPedidos();
                        atualizarTela(true, true, getResources().getString(
                                R.string.operacao_sucesso));

                    } catch (SQLException e) {
                        atualizarTela(true, false, e.getMessage());
                    }
                }
            }.start();
        } else {
            isClique = false;
        }
    }

    private boolean validar() {
        String erro = "";
        if (pedidoItens == null || pedidoItens.size() < 1) {
            erro = getResources().getString(R.string.campo_erro);

            Funcoes.mensagem(PedidoActivity.this, erro, true);

            return false;
        }

        return true;
    }

    private void atualizarTela(final boolean alert, final boolean sucesso, final String mensagem) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (alert) {
                    AlertDialog.Builder builder = Funcoes.mensagem(
                            PedidoActivity.this, mensagem, false);
                    builder.setNeutralButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    if (sucesso) {
                                        pedidoItens.clear();
                                        lvPedidoItens.setAdapter(null);
                                        pedidoItemAdapter.notifyDataSetChanged();
                                        onBackPressed();
                                    }
                                }
                            });

                    dialog.dismiss();
                    builder.create().show();
                }
            }
        });
    }

    private void enviarPedidos() throws SQLException {

        atualizarTela(false, true, this.getResources().getString(R.string.pedido_analisandopedidos));

        if (pedidoItens.size() > 0) {
            for (PedidoItem pedidoItem : pedidoItens) {
                if (pedidoItem.getProdutoID() == 99999) {
                    try {
                        requisicoesHTTP.httpEnviarProdutoPersonalizado(pedidoItem);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        requisicoesHTTP.httpEnviarProduto(pedidoItem);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        }
    }


}
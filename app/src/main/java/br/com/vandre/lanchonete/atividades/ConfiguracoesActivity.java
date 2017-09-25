package br.com.vandre.lanchonete.atividades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import br.com.vandre.lanchonete.R;
import br.com.vandre.lanchonete.app.LanchoneteAplicacao;
import br.com.vandre.lanchonete.dblocal.LocalDao;
import br.com.vandre.lanchonete.dblocal.LocalFactoryDB;
import br.com.vandre.lanchonete.modelo.Aplicacao;
import br.com.vandre.lanchonete.util.Funcoes;

public class ConfiguracoesActivity extends AppCompatActivity {

    EditText edtServidor;
    Aplicacao aplicacao;
    LocalDao<Aplicacao> dao;
    ProgressDialog dialog;
    Handler handler;

    private void declarar() {

        edtServidor = (EditText) findViewById(R.id.configuracoes_edtServidor);

        dao = LocalFactoryDB.getDao(ConfiguracoesActivity.this, Aplicacao.class);

        handler = new Handler();
    }

    private void carregarAplicacao() {
        aplicacao = dao.selectByCodigo("1");
    }

    private void carregarCampos() {
        edtServidor.setText(aplicacao.getServidor());
    }

    private void iniciar() {
        declarar();
        carregarAplicacao();
        carregarCampos();
    }

    private boolean validar() {
        if (edtServidor.getText().toString().trim().equals("")) {

            Funcoes.mensagem(ConfiguracoesActivity.this, this.getResources().getString(R.string.campo_erro), true);
            return false;
        }
        return true;
    }

    private void atualizarTela(final boolean sucesso, final String mensagem,
                               final boolean carregarCampos, final boolean sair) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (carregarCampos) {
                    carregarCampos();
                }

                AlertDialog.Builder builder = Funcoes.mensagem(
                        ConfiguracoesActivity.this, mensagem, false);

                builder.setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (sucesso) {
                                    if (sair) {
                                        LanchoneteAplicacao app = (LanchoneteAplicacao) getApplicationContext();
                                        app.setAplicacao(aplicacao);
                                        ConfiguracoesActivity.this.finish();
                                    }
                                }
                            }
                        });

                dialog.dismiss();
                builder.create().show();
            }
        });
    }

    private void salvar() {
        if (validar()) {
            dialog = Funcoes.mensagemAguarde(ConfiguracoesActivity.this);

            new Thread() {
                @Override
                public void run() {
                    try {
                        aplicacao.setServidor(edtServidor.getText().toString());

                        dao.update(aplicacao);
                        atualizarTela(true, getResources().getString(R.string.operacao_sucesso), false, true);
                    } catch (Exception e) {
                        atualizarTela(false, getResources().getString(R.string.campo_erro), false, false);
                    }
                }
            }.start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.configuracoes_acSalvar) {
            salvar();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_configuracoes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_configuracoes);
        iniciar();
    }
}
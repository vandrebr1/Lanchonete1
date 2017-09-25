package br.com.vandre.lanchonete.atividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import br.com.vandre.lanchonete.R;
import br.com.vandre.lanchonete.app.LanchoneteAplicacao;
import br.com.vandre.lanchonete.dblocal.LocalDao;
import br.com.vandre.lanchonete.dblocal.LocalFactoryDB;
import br.com.vandre.lanchonete.modelo.Aplicacao;
import br.com.vandre.lanchonete.network.RequisicoesHTTP;
import br.com.vandre.lanchonete.util.Funcoes;


public class SplashActivity extends Activity implements Runnable {
    ImageView[] pontos = new ImageView[3];

    static boolean blnFimThread;
    private static final int DELAY = 2000;
    private int animacao = 0;
    private static final String ICONE_CRIADO = "ICONE_CRIADO";

    private void declarar() {
        pontos[0] = findViewById(R.id.splash_ponto1);
        pontos[1] = findViewById(R.id.splash_ponto2);
        pontos[2] = findViewById(R.id.splash_ponto3);

    }

    private void iniciar() {
        declarar();
        blnFimThread = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_splash);

        iniciar();
        animacao();

        Handler handler = new Handler();
        handler.postDelayed(this, DELAY);
    }

    public void animacao() {
        pontos[0].setVisibility(View.INVISIBLE);
        pontos[1].setVisibility(View.INVISIBLE);
        pontos[2].setVisibility(View.INVISIBLE);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                while (!blnFimThread)
                    try {
                        Thread.sleep(500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (animacao > 2) {
                                    animacao = 0;

                                    pontos[0].setVisibility(View.INVISIBLE);
                                    pontos[1].setVisibility(View.INVISIBLE);
                                    pontos[2].setVisibility(View.INVISIBLE);
                                } else {
                                    pontos[animacao]
                                            .setVisibility(View.VISIBLE);
                                    animacao++;
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                    }
            }
        })).start();

    }

    @Override
    public void run() {
        RequisicoesHTTP requisicoesHTTP = new RequisicoesHTTP(this);

        LocalDao<Aplicacao> dao = LocalFactoryDB.getDao(SplashActivity.this, Aplicacao.class);

        LanchoneteAplicacao app = (LanchoneteAplicacao) getApplication();

        try {

            Aplicacao aplicacao = dao.selectByCodigo("1");
            if (aplicacao == null) {
                aplicacao = new Aplicacao();
                aplicacao.setCodigo(1);
                aplicacao.setServidor("http://192.168.1.2:8080/api/");
                dao.insert(aplicacao);
                aplicacao = dao.selectByCodigo("1");
                app.setAplicacao(aplicacao);
            }

            requisicoesHTTP.httpProdutos();
            requisicoesHTTP.httpIngredientes();


            startActivity(new Intent(this, CardapioActivity.class));
            finish();

        } catch (Exception e) {
            LocalFactoryDB.closeDatabase();

            AlertDialog.Builder builder = Funcoes.mensagem(SplashActivity.this,
                    e.getMessage(), false);

            builder.setNeutralButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

            builder.create().show();
        }

        blnFimThread = true;

    }

}

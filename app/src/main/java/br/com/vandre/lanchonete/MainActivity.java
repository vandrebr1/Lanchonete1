package br.com.vandre.lanchonete;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.vandre.lanchonete.adapters.MainAdapter;
import br.com.vandre.lanchonete.app.Constantes;
import br.com.vandre.lanchonete.app.LanchoneteAplicacao;
import br.com.vandre.lanchonete.atividades.ConfiguracoesActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvMensagem;
    Button btnSair;
    ListView lvLista;
    LanchoneteAplicacao app;

    private ArrayList<String> MENU_OPTIONS = new ArrayList<String>();

    private void declarar() {

        MENU_OPTIONS.add("Cardápio");
        MENU_OPTIONS.add("Configurações");
        MENU_OPTIONS.add("Voltar");

        tvMensagem = (TextView) findViewById(R.id.main_tvMensagem);
        btnSair = (Button) findViewById(R.id.main_btnSair);
        lvLista = (ListView) findViewById(R.id.main_lvLista);
        app = (LanchoneteAplicacao) getApplication();
    }

    private void iniciar() {

        declarar();

        btnSair.setOnClickListener(new BtnSairClickListener());
        lvLista.setOnItemClickListener(new LvListaItemClickListener());

        MainAdapter mainadapter = new MainAdapter(MainActivity.this, MENU_OPTIONS);
        lvLista.setAdapter(mainadapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getBooleanExtra(Constantes.EXIT, false)) {
            String mensagem = getIntent().getStringExtra(Constantes.MENSAGEM);
            if (mensagem != null) {
                if (mensagem.toString() != "") {
                    tvMensagem.setText(mensagem);
                    lvLista.setVisibility(View.GONE);
                    tvMensagem.setVisibility(View.VISIBLE);
                    btnSair.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        MainActivity.this.finish();

    }


    private class LvListaItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            switch (position) {
                case 0:
                    onBackPressed();
                    break;

                case 1:
                    startActivity(new Intent(MainActivity.this, ConfiguracoesActivity.class));
                    break;

                case 2:
                    onBackPressed();
                    break;

            }
        }
    }

    private class BtnSairClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

}

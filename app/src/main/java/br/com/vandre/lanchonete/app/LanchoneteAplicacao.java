package br.com.vandre.lanchonete.app;

import android.app.Application;

import br.com.vandre.lanchonete.dblocal.LocalDao;
import br.com.vandre.lanchonete.dblocal.LocalFactoryDB;
import br.com.vandre.lanchonete.modelo.Aplicacao;

public class LanchoneteAplicacao extends Application {

    private Aplicacao aplicacao;

    public Aplicacao getAplicacao() {

        if (this.aplicacao == null) {
            LocalDao<Aplicacao> aplicacaoLocalDao = LocalFactoryDB.getDao(LanchoneteAplicacao.this, Aplicacao.class);
            this.aplicacao = aplicacaoLocalDao.selectByCodigo("1");
        }

        return aplicacao;
    }

    public void setAplicacao(Aplicacao aplicacao) {
        this.aplicacao = aplicacao;
    }


}

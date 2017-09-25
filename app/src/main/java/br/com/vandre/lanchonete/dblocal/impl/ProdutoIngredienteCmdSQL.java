package br.com.vandre.lanchonete.dblocal.impl;

import java.util.ArrayList;
import java.util.List;

import br.com.vandre.lanchonete.dblocal.CmdSQL;

public class ProdutoIngredienteCmdSQL implements CmdSQL {

    @Override
    public List<String> create() {
        List<String> listaCreate = new ArrayList<String>();

        StringBuilder comando = new StringBuilder();
        comando.append("CREATE TABLE produtosingredientes (")
                .append("codigo INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,")
                .append("produtoID INTEGER NOT NULL ,")
                .append("ingredienteID INTEGER NOT NULL);");

        listaCreate.add(comando.toString());

        return listaCreate;
    }

    @Override
    public List<String> upgrade(int oldVersion, int newVersion) {
        List<String> listaUpgrades = new ArrayList<String>();

        return listaUpgrades;
    }
}

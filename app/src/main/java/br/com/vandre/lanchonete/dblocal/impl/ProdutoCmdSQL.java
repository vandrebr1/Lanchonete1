package br.com.vandre.lanchonete.dblocal.impl;

import java.util.ArrayList;
import java.util.List;

import br.com.vandre.lanchonete.dblocal.CmdSQL;

public class ProdutoCmdSQL implements CmdSQL {

    @Override
    public List<String> create() {
        List<String> listaCreate = new ArrayList<String>();

        StringBuilder comando = new StringBuilder();
        comando.append("CREATE TABLE produtos (")
                .append("codigo INTEGER NOT NULL PRIMARY KEY,")
                .append("produto NVARCHAR(120) NOT NULL,")
                .append("ingredientes TEXT ,")
                .append("imagem NVARCHAR(120));");

        listaCreate.add(comando.toString());

        comando = new StringBuilder();
        comando.append("CREATE INDEX produto ON produtos (produto ASC);");
        listaCreate.add(comando.toString());

        return listaCreate;
    }

    @Override
    public List<String> upgrade(int oldVersion, int newVersion) {
        List<String> listaUpgrades = new ArrayList<String>();

        return listaUpgrades;
    }
}

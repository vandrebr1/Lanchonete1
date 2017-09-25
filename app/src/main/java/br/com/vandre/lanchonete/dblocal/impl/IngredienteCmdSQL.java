package br.com.vandre.lanchonete.dblocal.impl;

import java.util.ArrayList;
import java.util.List;

import br.com.vandre.lanchonete.dblocal.CmdSQL;

public class IngredienteCmdSQL implements CmdSQL {

    @Override
    public List<String> create() {
        List<String> listaCreate = new ArrayList<String>();

        StringBuilder comando = new StringBuilder();
        comando.append("CREATE TABLE ingredientes (")
                .append("codigo INTEGER NOT NULL PRIMARY KEY,")
                .append("produto NVARCHAR(120) NOT NULL,")
                .append("preco FLOAT NOT NULL DEFAULT 0,")
                .append("imagem NVARCHAR(120));");

        listaCreate.add(comando.toString());

        comando = new StringBuilder();
        comando.append("CREATE INDEX produto ON ingredientes (produto ASC);");
        listaCreate.add(comando.toString());

        return listaCreate;
    }

    @Override
    public List<String> upgrade(int oldVersion, int newVersion) {
        List<String> listaUpgrades = new ArrayList<String>();

        return listaUpgrades;
    }
}

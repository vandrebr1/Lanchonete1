package br.com.vandre.lanchonete.dblocal.impl;

import java.util.ArrayList;
import java.util.List;

import br.com.vandre.lanchonete.dblocal.CmdSQL;


public class AplicacaoCmdSQL implements CmdSQL {

    @Override
    public List<String> create() {
        List<String> lista = new ArrayList<String>();
        StringBuilder comando = new StringBuilder();

        comando.append("CREATE TABLE sistema (")
                .append("codigo INTEGER NOT NULL PRIMARY KEY,")
                .append("servidor NVARCHAR(50));");

        lista.add(comando.toString());

        return lista;
    }

    @Override
    public List<String> upgrade(int oldVersion, int newVersion) {
        List<String> listaUpgrades = new ArrayList<String>();

        return listaUpgrades;
    }

}

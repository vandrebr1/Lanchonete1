package br.com.vandre.lanchonete.dblocal.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.vandre.lanchonete.dblocal.LocalDao;
import br.com.vandre.lanchonete.modelo.Aplicacao;

public class AplicacaoLocalDao implements LocalDao<Aplicacao> {

    private String tableName = "sistema";
    private SQLiteDatabase database;

    public AplicacaoLocalDao(SQLiteDatabase database) {
        super();
        this.database = database;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public void insert(Aplicacao... elementos) {
        for (Aplicacao aplicacao : elementos) {
            ContentValues values = new ContentValues();

            values.put("codigo", aplicacao.getCodigo());
            values.put("servidor", aplicacao.getServidor());

            database.insert(tableName, null, values);
        }
    }

    @Override
    public void update(Aplicacao... elementos) {
        for (Aplicacao aplicacao : elementos) {
            ContentValues values = new ContentValues();

            values.put("servidor", aplicacao.getServidor());

            database.update(tableName, values, "codigo = ?",
                    new String[]{String.valueOf(aplicacao.getCodigo())});
        }
    }

    @Override
    public void delete(Aplicacao... elementos) {
        if (elementos.length == 0) {
            database.delete(tableName, null, null);
        } else {
            for (Aplicacao aplicacao : elementos) {
                database.delete(tableName, "codigo = ?",
                        new String[]{String.valueOf(aplicacao.getCodigo())});
            }
        }
    }

    @Override
    public Aplicacao selectByCodigo(String codigo) {
        StringBuilder comando = new StringBuilder();

        comando.append("select * from ").append(tableName)
                .append(" where codigo = ?");

        return select(comando.toString(), codigo);
    }

    @Override
    public Aplicacao select(String sql, String... parametros) {
        Cursor cursor = database.rawQuery(sql, parametros);

        try {
            if (cursor.moveToFirst()) {
                return create(cursor);
            }
        } finally {
            cursor.close();
        }

        return null;
    }

    @Override
    public List<Aplicacao> selectAll() {
        return selectAll("select * from " + tableName);
    }

    @Override
    public List<Aplicacao> selectAll(String sql, String... parametros) {
        Cursor cursor = database.rawQuery(sql, parametros);

        try {
            if (cursor.moveToFirst()) {
                List<Aplicacao> resultado = new ArrayList<Aplicacao>();

                do {
                    resultado.add(create(cursor));
                } while (cursor.moveToNext());

                return resultado;
            }
        } finally {
            cursor.close();
        }

        return Collections.emptyList();
    }

    private Aplicacao create(Cursor cursor) {

        return new Aplicacao(
                cursor.getLong(cursor.getColumnIndex("codigo")),
                cursor.getString(cursor.getColumnIndex("servidor")));
    }
}

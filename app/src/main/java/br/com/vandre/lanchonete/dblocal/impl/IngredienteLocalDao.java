package br.com.vandre.lanchonete.dblocal.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.vandre.lanchonete.dblocal.LocalDao;
import br.com.vandre.lanchonete.modelo.Ingrediente;

public class IngredienteLocalDao implements LocalDao<Ingrediente> {

    private static final int QTD_REGISTROS_COMMIT = 500;

    private String tableName = "ingredientes";
    private SQLiteDatabase database;

    public IngredienteLocalDao(SQLiteDatabase database) {
        super();
        this.database = database;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public void insert(Ingrediente... elementos) {
        for (Ingrediente ingrediente : elementos) {
            ContentValues values = new ContentValues();

            values.put("codigo", ingrediente.getCodigo());
            values.put("produto", ingrediente.getProduto());
            values.put("preco", ingrediente.getPreco());
            values.put("imagem", ingrediente.getImagem());

            database.insert(tableName, null, values);
        }
    }

    public void insertStatement(Ingrediente... elementos) {

        StringBuilder comando = new StringBuilder();

        comando.append("insert into ")
                .append(tableName)
                .append(" (codigo, produto, preco, imagem)")
                .append(" values (?, ?, ?, ?)");

        String sqlInsert = comando.toString();

        SQLiteStatement insert = database.compileStatement(sqlInsert);

        int tamanho = elementos.length;
        int qtdCommit = tamanho / QTD_REGISTROS_COMMIT;
        int resto = tamanho % QTD_REGISTROS_COMMIT;
        if (resto > 0)
            qtdCommit++;
        int inicio = 0;
        int fim = QTD_REGISTROS_COMMIT - 1;

        for (int i = 1; i <= qtdCommit; i++) {
            if (i == qtdCommit)
                fim = tamanho - 1;
            try {
                database.beginTransaction();

                while (inicio <= fim) {
                    insert.bindLong(1, elementos[inicio].getCodigo());
                    insert.bindString(2, elementos[inicio].getProduto());
                    insert.bindDouble(3, elementos[inicio].getPreco());
                    insert.bindString(4, elementos[inicio].getImagem());

                    insert.execute();
                    inicio++;
                }

                database.setTransactionSuccessful();
                fim += QTD_REGISTROS_COMMIT;
            } finally {
                database.endTransaction();
            }
        }

        insert.close();
    }

    @Override
    public void update(Ingrediente... elementos) {
        for (Ingrediente ingrediente : elementos) {
            ContentValues values = new ContentValues();

            values.put("produto", ingrediente.getProduto());
            values.put("preco", ingrediente.getPreco());
            values.put("imagem", ingrediente.getImagem());

            database.update(tableName, values, "codigo = ?",
                    new String[]{ingrediente.getCodigo().toString()});
        }
    }

    @Override
    public void delete(Ingrediente... elementos) {
        if (elementos.length == 0) {
            database.delete(tableName, null, null);
        } else {
            for (Ingrediente ingrediente : elementos) {
                database.delete(tableName, "codigo = ?",
                        new String[]{ingrediente.getCodigo().toString()});
            }
        }
    }

    @Override
    public Ingrediente selectByCodigo(String codigo) {
        StringBuilder comando = new StringBuilder();

        comando.append("select * from ").append(tableName)
                .append(" where codigo = ?");

        return select(comando.toString(), codigo);
    }

    @Override
    public Ingrediente select(String sql, String... parametros) {
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
    public List<Ingrediente> selectAll() {
        return selectAll("select * from " + tableName);
    }

    @Override
    public List<Ingrediente> selectAll(String sql, String... parametros) {
        Cursor cursor = database.rawQuery(sql, parametros);

        try {
            if (cursor.moveToFirst()) {
                List<Ingrediente> resultado = new ArrayList<Ingrediente>();

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

    private Ingrediente create(Cursor cursor) {

        return new Ingrediente(cursor.getLong(cursor.getColumnIndex("codigo")),
                cursor.getString(cursor.getColumnIndex("produto")),
                cursor.getDouble(cursor.getColumnIndex("preco")),
                cursor.getString(cursor.getColumnIndex("imagem")));
    }

}

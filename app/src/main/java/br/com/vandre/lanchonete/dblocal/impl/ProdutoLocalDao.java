package br.com.vandre.lanchonete.dblocal.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.vandre.lanchonete.dblocal.LocalDao;
import br.com.vandre.lanchonete.modelo.Produto;

public class ProdutoLocalDao implements LocalDao<Produto> {

    private static final int QTD_REGISTROS_COMMIT = 500;

    private String tableName = "produtos";
    private SQLiteDatabase database;

    public ProdutoLocalDao(SQLiteDatabase database) {
        super();
        this.database = database;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public void insert(Produto... elementos) {
        for (Produto produto : elementos) {
            ContentValues values = new ContentValues();

            values.put("codigo", produto.getCodigo());
            values.put("produto", produto.getProduto());
            values.put("ingredientes", produto.getIngredientes());
            values.put("imagem", produto.getImagem());

            database.insert(tableName, null, values);
        }
    }

    public void insertStatement(Produto... elementos) {

        StringBuilder comando = new StringBuilder();

        comando.append("insert into ")
                .append(tableName)
                .append(" (codigo, produto, ingredientes, imagem)")
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
                    insert.bindString(3, elementos[inicio].getIngredientes());
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
    public void update(Produto... elementos) {
        for (Produto produto : elementos) {
            ContentValues values = new ContentValues();

            values.put("produto", produto.getProduto());
            values.put("ingredientes", produto.getIngredientes());
            values.put("imagem", produto.getImagem());

            database.update(tableName, values, "codigo = ?",
                    new String[]{produto.getCodigo().toString()});
        }
    }

    @Override
    public void delete(Produto... elementos) {
        if (elementos.length == 0) {
            database.delete(tableName, null, null);
        } else {
            for (Produto produto : elementos) {
                database.delete(tableName, "codigo = ?",
                        new String[]{produto.getCodigo().toString()});
            }
        }
    }

    @Override
    public Produto selectByCodigo(String codigo) {
        StringBuilder comando = new StringBuilder();

        comando.append("select * from ").append(tableName)
                .append(" where codigo = ?");

        return select(comando.toString(), codigo);
    }

    @Override
    public Produto select(String sql, String... parametros) {
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
    public List<Produto> selectAll() {
        return selectAll("select * from " + tableName);
    }

    @Override
    public List<Produto> selectAll(String sql, String... parametros) {
        Cursor cursor = database.rawQuery(sql, parametros);

        try {
            if (cursor.moveToFirst()) {
                List<Produto> resultado = new ArrayList<Produto>();

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

    private Produto create(Cursor cursor) {

        return new Produto(cursor.getLong(cursor.getColumnIndex("codigo")),
                cursor.getString(cursor.getColumnIndex("produto")),
                cursor.getDouble(cursor.getColumnIndex("preco")),
                cursor.getString(cursor.getColumnIndex("ingredientes")),
                cursor.getString(cursor.getColumnIndex("imagem")),
                cursor.getString(cursor.getColumnIndex("ingredientesJson")));
    }

}

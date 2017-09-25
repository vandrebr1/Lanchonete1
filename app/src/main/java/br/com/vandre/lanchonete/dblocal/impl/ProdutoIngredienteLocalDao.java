package br.com.vandre.lanchonete.dblocal.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.vandre.lanchonete.dblocal.LocalDao;
import br.com.vandre.lanchonete.modelo.ProdutoIngrediente;

public class ProdutoIngredienteLocalDao implements LocalDao<ProdutoIngrediente> {

    private static final int QTD_REGISTROS_COMMIT = 500;

    private String tableName = "produtosingredientes";
    private SQLiteDatabase database;

    public ProdutoIngredienteLocalDao(SQLiteDatabase database) {
        super();
        this.database = database;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public void insert(ProdutoIngrediente... elementos) {
        for (ProdutoIngrediente produtoIngrediente : elementos) {
            ContentValues values = new ContentValues();

            values.put("produtoID", produtoIngrediente.getProdutoID());
            values.put("ingredienteID", produtoIngrediente.getProdutoID());

            database.insert(tableName, null, values);
        }
    }

    public void insertStatement(ProdutoIngrediente... elementos) {

        StringBuilder comando = new StringBuilder();

        comando.append("insert into ")
                .append(tableName)
                .append(" (codigo, produtoID, ingredienteID)")
                .append(" values (NULL, ?, ?)");

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
                    insert.bindLong(1, elementos[inicio].getProdutoID());
                    insert.bindLong(2, elementos[inicio].getIngredienteID());

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
    public void update(ProdutoIngrediente... elementos) {
        for (ProdutoIngrediente produtoIngrediente : elementos) {
            ContentValues values = new ContentValues();

            values.put("produtoID", produtoIngrediente.getProdutoID());
            values.put("ingredienteID", produtoIngrediente.getIngredienteID());

            database.update(tableName, values, "codigo = ?",
                    new String[]{produtoIngrediente.getCodigo().toString()});
        }
    }

    @Override
    public void delete(ProdutoIngrediente... elementos) {
        if (elementos.length == 0) {
            database.delete(tableName, null, null);
            database.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='"
                    + tableName + "'");
        } else {
            for (ProdutoIngrediente produtoIngrediente : elementos) {
                database.delete(tableName, "codigo = ?",
                        new String[]{produtoIngrediente.getCodigo().toString()});
            }
        }
    }

    @Override
    public ProdutoIngrediente selectByCodigo(String codigo) {
        StringBuilder comando = new StringBuilder();

        comando.append("select * from ").append(tableName)
                .append(" where codigo = ?");

        return select(comando.toString(), codigo);
    }

    @Override
    public ProdutoIngrediente select(String sql, String... parametros) {
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
    public List<ProdutoIngrediente> selectAll() {
        return selectAll("select * from " + tableName);
    }

    @Override
    public List<ProdutoIngrediente> selectAll(String sql, String... parametros) {
        Cursor cursor = database.rawQuery(sql, parametros);

        try {
            if (cursor.moveToFirst()) {
                List<ProdutoIngrediente> resultado = new ArrayList<ProdutoIngrediente>();

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

    private ProdutoIngrediente create(Cursor cursor) {

        return new ProdutoIngrediente(cursor.getLong(cursor.getColumnIndex("codigo")),
                cursor.getLong(cursor.getColumnIndex("produtoID")),
                cursor.getLong(cursor.getColumnIndex("ingredienteID")));
    }

}

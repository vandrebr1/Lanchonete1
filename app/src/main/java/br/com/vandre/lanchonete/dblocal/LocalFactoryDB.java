package br.com.vandre.lanchonete.dblocal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.vandre.lanchonete.dblocal.impl.AplicacaoLocalDao;
import br.com.vandre.lanchonete.dblocal.impl.IngredienteLocalDao;
import br.com.vandre.lanchonete.dblocal.impl.ProdutoIngredienteLocalDao;
import br.com.vandre.lanchonete.dblocal.impl.ProdutoLocalDao;
import br.com.vandre.lanchonete.modelo.Aplicacao;
import br.com.vandre.lanchonete.modelo.Ingrediente;
import br.com.vandre.lanchonete.modelo.Produto;
import br.com.vandre.lanchonete.modelo.ProdutoIngrediente;

public class LocalFactoryDB {

    private static SQLiteDatabase database;
    private static final int DATABASE_VERSION = 1;

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new LocalDB(context, DATABASE_VERSION).getWritableDatabase();
        }

        return database;
    }

    public Cursor select(String sql, String... parametros) {
        if (database != null && database.isOpen()) {
            Cursor cursor = database.rawQuery(sql, parametros);
            return cursor;
        }

        return null;
    }

    public static void beginTransaction() {
        if (database != null && database.isOpen()) {
            database.beginTransaction();
        }
    }

    public static void setTransactionSuccessful() {
        if (database != null && database.isOpen()) {
            if (database.inTransaction()) database.setTransactionSuccessful();
        }
    }

    public static void endTransaction() {
        if (database != null && database.isOpen()) {
            if (database.inTransaction()) database.endTransaction();
        }
    }

    public static void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public static long proximoCodigo(String tabela, String campo) {
        if (database != null && database.isOpen()) {
            StringBuilder comando = new StringBuilder();

            comando.append("select max(")
                    .append(campo).append(") as ")
                    .append(campo).append(" from ")
                    .append(tabela);

            Cursor cursor = database.rawQuery(comando.toString(), null);

            try {
                if (cursor.moveToFirst()) {
                    int coluna = cursor.getColumnIndex(campo);
                    if (!cursor.isNull(coluna)) {
                        return cursor.getLong(cursor.getColumnIndex(campo)) + 1;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            } finally {
                cursor.close();
            }
        } else {
            return -1;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> LocalDao<T> getDao(Context context, Class<T> type) {
        if (Aplicacao.class.isAssignableFrom(type)) {
            return (LocalDao<T>) new AplicacaoLocalDao(getDatabase(context));
        }

        if (Produto.class.isAssignableFrom(type)) {
            return (LocalDao<T>) new ProdutoLocalDao(getDatabase(context));
        }

        if (Ingrediente.class.isAssignableFrom(type)) {
            return (LocalDao<T>) new IngredienteLocalDao(getDatabase(context));
        }

        if (ProdutoIngrediente.class.isAssignableFrom(type)) {
            return (LocalDao<T>) new ProdutoIngredienteLocalDao(getDatabase(context));
        }

        throw new IllegalArgumentException("Tipo inválido para obter o dao...");
    }
}

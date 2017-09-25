package br.com.vandre.lanchonete.dblocal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vandre.lanchonete.app.TagsConstantes;
import br.com.vandre.lanchonete.dblocal.impl.AplicacaoCmdSQL;
import br.com.vandre.lanchonete.dblocal.impl.IngredienteCmdSQL;
import br.com.vandre.lanchonete.dblocal.impl.ProdutoCmdSQL;
import br.com.vandre.lanchonete.dblocal.impl.ProdutoIngredienteCmdSQL;

public class LocalDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "lanchonete";
    private static final CmdSQL[] cmdsSQL = {new ProdutoCmdSQL(), new AplicacaoCmdSQL(),
            new ProdutoIngredienteCmdSQL(), new IngredienteCmdSQL()};

    public LocalDB(Context context, int version) {
        super(context, DATABASE_NAME, null, version);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        for (CmdSQL cmdSQL : cmdsSQL) {
            try {
                List<String> cmds = cmdSQL.create();
                for (String cmd : cmds) {
                    database.execSQL(cmd);
                }
            } catch (Exception e) {
                Log.e(TagsConstantes.DATABASE, "Erro criando bando de dados", e);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        for (CmdSQL cmdSQL : cmdsSQL) {
            try {
                List<String> cmds = cmdSQL.upgrade(oldVersion, newVersion);
                for (String cmd : cmds) {
                    database.execSQL(cmd);
                }
            } catch (Exception e) {
                Log.e(TagsConstantes.DATABASE,
                        "Erro atualizando bando de dados", e);
            }
        }

    }

}

package br.com.vandre.lanchonete.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.vandre.lanchonete.app.LanchoneteAplicacao;
import br.com.vandre.lanchonete.dblocal.LocalDao;
import br.com.vandre.lanchonete.dblocal.LocalFactoryDB;
import br.com.vandre.lanchonete.dblocal.impl.IngredienteLocalDao;
import br.com.vandre.lanchonete.dblocal.impl.ProdutoIngredienteLocalDao;
import br.com.vandre.lanchonete.dblocal.impl.ProdutoLocalDao;
import br.com.vandre.lanchonete.modelo.Ingrediente;
import br.com.vandre.lanchonete.modelo.PedidoItem;
import br.com.vandre.lanchonete.modelo.Produto;
import br.com.vandre.lanchonete.modelo.ProdutoIngrediente;
import br.com.vandre.lanchonete.modelo.Promocao;

public class RequisicoesHTTP {

    private LanchoneteAplicacao app;
    Context context;

    public RequisicoesHTTP(Context context) {
        super();
        this.app = (LanchoneteAplicacao) context.getApplicationContext();
        this.context = context;
    }

    public void httpProdutos() throws Exception, RuntimeException {
        String url = app.getAplicacao().getServidor() + "lanche";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            List<Produto> lstProduto = new ArrayList<Produto>();
                            List<ProdutoIngrediente> lstProdutoIngrediente = new ArrayList<ProdutoIngrediente>();

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                Produto produto = new Produto(
                                        jsonobject.getLong("id"),
                                        jsonobject.getString("name"),
                                        jsonobject.getString("ingredients"),
                                        jsonobject.getString("image"),
                                        jsonobject.getString("ingredients"));

                                lstProduto.add(produto);

                                JSONArray jsonArray = jsonobject.getJSONArray("ingredients");

                                for (int x = 0; x < jsonArray.length(); x++) {
                                    ProdutoIngrediente produtoIngrediente = new ProdutoIngrediente(
                                            produto.getCodigo(),
                                            jsonArray.getLong(x));

                                    lstProdutoIngrediente.add(produtoIngrediente);
                                }
                            }

                            LocalDao<Produto> produtoLocalDao = LocalFactoryDB.getDao(context, Produto.class);
                            LocalDao<ProdutoIngrediente> produtoIngredienteLocalDao = LocalFactoryDB.getDao(context, ProdutoIngrediente.class);

                            produtoLocalDao.delete();
                            produtoIngredienteLocalDao.delete();

                            ((ProdutoLocalDao) (produtoLocalDao)).insertStatement(lstProduto.toArray(new Produto[lstProduto.size()]));
                            ((ProdutoIngredienteLocalDao) (produtoIngredienteLocalDao)).insertStatement(lstProdutoIngrediente.toArray(new ProdutoIngrediente[lstProdutoIngrediente.size()]));
                        } catch (JSONException e) {
                            Log.e("Tag", e.toString());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest, "REQ");
    }

    public void httpIngredientes() throws Exception, RuntimeException {
        String url = app.getAplicacao().getServidor() + "ingrediente";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            List<Ingrediente> lstIngrediente = new ArrayList<Ingrediente>();

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                Ingrediente ingrediente = new Ingrediente(
                                        jsonobject.getLong("id"),
                                        jsonobject.getString("name"),
                                        jsonobject.getDouble("price"),
                                        jsonobject.getString("image"));

                                lstIngrediente.add(ingrediente);
                            }

                            LocalDao<Ingrediente> ingredienteLocalDao = LocalFactoryDB.getDao(context, Ingrediente.class);

                            ingredienteLocalDao.delete();

                            ((IngredienteLocalDao) (ingredienteLocalDao)).insertStatement(lstIngrediente.toArray(new Ingrediente[lstIngrediente.size()]));

                        } catch (JSONException e) {
                            Log.e("Tag", e.toString());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest, "REQ");
    }

    public void httpPromocoes(final ArrayList<String> promocoes) throws Exception, RuntimeException {
        String url = app.getAplicacao().getServidor() + "promocao";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                Promocao promocao = new Promocao(
                                        jsonobject.getLong("id"),
                                        jsonobject.getString("name"),
                                        jsonobject.getString("description"));
                                promocoes.add("<b>" + promocao.getNome() + ": " + "</b>" + promocao.getDescricao());

                            }

                        } catch (JSONException e) {
                            Log.e("Tag", e.toString());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest, "REQ");
    }


    public void httpEnviarProduto(final PedidoItem pedidoItem) throws Exception, RuntimeException {
        String url = app.getAplicacao().getServidor() + "pedido/" + pedidoItem.getProdutoID().toString();

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        VolleySingleton.getInstance(context).addToRequestQueue(putRequest, "REQ");
    }


    public void httpEnviarProdutoPersonalizado(final PedidoItem pedidoItem) throws Exception, RuntimeException {
        String url = app.getAplicacao().getServidor() + "pedido/" + pedidoItem.getProdutoID().toString();

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("extras", pedidoItem.getIngredientesJson());

                return params;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(putRequest, "REQ");
    }

}

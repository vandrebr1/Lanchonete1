package br.com.vandre.lanchonete.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import br.com.vandre.lanchonete.R;

public class VolleyErrorHelper {

    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.cancelar);
        } else if (isServerProblem(error)) {
            return handleServerError(error, context);
        } else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.nao);
        }
        return context.getResources().getString(R.string.nao);
    }


    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {
            switch (response.statusCode) {
                case 404:
                case 422:
                case 401:
                    try {

                        System.out.println("Error System... by VolleyErrorHelper");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return error.getMessage();

                default:
                    return context.getResources().getString(R.string.cancelar);
            }
        }
        return context.getResources().getString(R.string.cancelar);
    }
}
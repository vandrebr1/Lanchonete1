package br.com.vandre.lanchonete.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.vandre.lanchonete.R;

public class Funcoes {

    public static String formatarDataHora(Date data, String formato) {
        if (data == null)
            return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(formato,
                Locale.getDefault());

        return dateFormat.format(data);
    }

    public static Date formatarDataHora(String data, String formato) {
        if (data == null || data.equals(""))
            return null;

        Date retorno = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(formato,
                    Locale.getDefault());
            retorno = (Date) dateFormat.parse(data);
        } catch (ParseException e) {
            return null;
        }
        return retorno;
    }

    public static String formatarMoeda(double valor) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat
                .getCurrencyInstance();
        String symbol = decimalFormat.getCurrency().getSymbol();
        decimalFormat.setNegativePrefix(symbol + "-");
        decimalFormat.setNegativeSuffix("");
        return decimalFormat.format(valor);
    }


    public static String formatarMoeda(double valor, int qtdCasas) {
        return getCurrencySymbol() + formatarDecimal(valor, qtdCasas);
    }

    public static double formatarMoeda(String valor) {
        if (valor == null || valor.equals(""))
            return 0;

        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat
                .getCurrencyInstance();

        String valorLocal = valor.replace(decimalFormat.getCurrency()
                .getSymbol(), "");
        valorLocal = valorLocal.replace(String.valueOf(decimalFormat
                .getDecimalFormatSymbols().getGroupingSeparator()), "");
        valorLocal = valorLocal.replace(String.valueOf(decimalFormat
                .getDecimalFormatSymbols().getDecimalSeparator()), ".");

        return Double.parseDouble(valorLocal);
    }

    public static String formatarDecimal(double valor, int qtdCasas) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat
                .getCurrencyInstance();

        String decimalSeparator = String.valueOf(decimalFormat
                .getDecimalFormatSymbols().getDecimalSeparator());

        if (qtdCasas == 0) {
            return String.valueOf(valor).replace(".", decimalSeparator);
        } else {
            return String.format("%." + String.valueOf(qtdCasas) + "f", valor)
                    .replace(".", decimalSeparator);
        }
    }

    public static double formatarDecimal(String valor) {
        if (valor == null || valor.equals(""))
            return 0;

        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat
                .getCurrencyInstance();

        String valorLocal = valor.replace(decimalFormat.getCurrency()
                .getSymbol(), "");
        valorLocal = valorLocal.replace(String.valueOf(decimalFormat
                .getDecimalFormatSymbols().getGroupingSeparator()), "");
        valorLocal = valorLocal.replace(String.valueOf(decimalFormat
                .getDecimalFormatSymbols().getDecimalSeparator()), ".");

        return Double.parseDouble(valorLocal);
    }

    public static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getCurrencySymbol() {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat
                .getCurrencyInstance();
        return decimalFormat.getCurrency().getSymbol();
    }

    public static char getDecimalSeparator() {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat
                .getCurrencyInstance();
        return decimalFormat.getDecimalFormatSymbols().getDecimalSeparator();
    }

    public static AlertDialog.Builder mensagem(Context context,
                                               String mensagem, boolean show) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setCancelable(false);
        builder.setMessage(mensagem);
        if (show) {
            builder.setNeutralButton(android.R.string.ok, null);
            builder.create().show();
        }
        return builder;
    }

    public static AlertDialog.Builder adicionar(Context context, String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo);
        builder.setCancelable(false);

        return builder;
    }

    public static ProgressDialog mensagemAguarde(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(context.getResources().getString(R.string.aguarde));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

}
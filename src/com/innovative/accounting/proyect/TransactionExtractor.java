package com.innovative.accounting.proyect;

import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionExtractor {

    private static String regex;
    private static String[] keywordsDepositos;
    private static String[] keywordsRetiros;

    // Variables para almacenar saldos
    private static double saldoInicial = 0.00;
    private static double saldoFinal = 0.00;

    // Inicializar la configuración del banco
    public static void initialize(String bank) {
        regex = PropertyFileHelper.getProperty(bank, "regex");
        keywordsDepositos = PropertyFileHelper.getPropertyAsArray(bank, "depositos");
        keywordsRetiros = PropertyFileHelper.getPropertyAsArray(bank, "retiros");
    }

    // Extraer transacciones y saldos
    public static List<Transaction> extractTransactions(String texto) {
        List<Transaction> resultados = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(texto);

        while (matcher.find()) {
            String fecha = matcher.group(1);
            String descripcion = matcher.group(2).trim().replaceAll("\\s{2,}", " ");
            String monto1 = matcher.group(3) != null ? matcher.group(3) : "0.00";
            String monto2 = matcher.group(4) != null ? matcher.group(4) : "0.00";
            String monto3 = matcher.group(5) != null ? matcher.group(5) : "0.00";

            Transaction transaccion = clasificarTransaccion(fecha, descripcion, monto1, monto2, monto3);
            
            // Si es saldo inicial o final, actualizar variables
            if (descripcion.toLowerCase().contains("saldo anterior")) {
                saldoInicial = Double.parseDouble(monto1.replace(",", ""));
            } else if (descripcion.toLowerCase().contains("saldo final")) {
                saldoFinal = Double.parseDouble(monto1.replace(",", ""));
            }

            resultados.add(transaccion);
        }

        return resultados;
    }

    // Clasificación de la transacción como depósito o retiro
    private static Transaction clasificarTransaccion(String fecha, String descripcion, String monto1, String monto2, String monto3) {
        String montoDeposito = "0.00";
        String montoRetiro = "0.00";
        String saldo = "0.00";

        // Identificar si es depósito o retiro basado en palabras clave
        boolean esDeposito = false;
        boolean esRetiro = false;

        for (String keyword : keywordsDepositos) {
            if (descripcion.toLowerCase().contains(keyword)) {
                esDeposito = true;
                break;
            }
        }

        for (String keyword : keywordsRetiros) {
            if (descripcion.toLowerCase().contains(keyword)) {
                esRetiro = true;
                break;
            }
        }

        if (esDeposito) {
            montoDeposito = monto1;
            saldo = monto2;
        } else if (esRetiro) {
            montoRetiro = monto1;
            saldo = monto2;
        } else {
            montoRetiro = monto1;
            saldo = monto2;
        }

        return new Transaction(fecha, descripcion, montoRetiro, saldo, montoDeposito);
    }

    // Método para calcular el saldo basado en las transacciones
    public static double calcularSaldo(List<Transaction> transacciones) {
        double saldoCalculado = saldoInicial;

        for (Transaction transaccion : transacciones) {
            double montoDeposito = Double.parseDouble(transaccion.getMontoDeposito().replace(",", ""));
            double montoRetiro = Double.parseDouble(transaccion.getMontoRetiro().replace(",", ""));

            saldoCalculado += montoDeposito; // Sumar depósitos y restar retiros
            saldoCalculado -= montoRetiro;   
        }

        return saldoCalculado;
    }

    // Método para comparar el saldo calculado con el saldo final
    public static void verificarSaldo(double saldoCalculado) {
        if (Math.abs(saldoCalculado - saldoFinal) < 0.01) {
            System.out.println("El saldo calculado coincide con el saldo final.");
        } else {
            System.out.println("Error: El saldo calculado no coincide con el saldo final.");
            System.out.println("Saldo calculado: " + saldoCalculado);
            System.out.println("Saldo final en el estado de cuenta: " + saldoFinal);
        }
    }
}

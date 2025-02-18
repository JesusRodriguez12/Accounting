package com.innovative.accounting.dataextract;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.*;
import java.util.*;

public class DataExtractor {
    private String[] keywordsDeposits;
    private String[] keywordsWithdrawals;
    private String saldoInicialLabel;
    private String saldoFinalLabel;
    private String regexTransacciones;

    private String rfc;
    private String banco;
    private String mesAnio;
    private double saldoInicial;
    private double saldoFinal;

    public DataExtractor(ConfigLoader config, String banco) {
        this.banco = banco;
        this.regexTransacciones = config.getRegexTransacciones(banco);
        this.keywordsDeposits = config.getKeywordsDeposits(banco).toArray(new String[0]);
        this.keywordsWithdrawals = config.getKeywordsWithdrawals(banco).toArray(new String[0]);
        this.saldoInicialLabel = config.getSaldoInicialLabel(banco);
        this.saldoFinalLabel = config.getSaldoFinalLabel(banco);
        this.mesAnio = extraerMesAnio(); // Extrae el mes y año actual en formato "MMM-yyyy"
    }

    public List<String[]> extractData(String text) {
        List<String[]> results = new ArrayList<>();
        // Extraer información básica y agregar el saldo inicial solo una vez
       // extraerInformacionBasica(text, results); 
        results.add(new String[] { "SALDO ANTERIOR", "", "", " ","" }); //String.valueOf(saldoInicial)
        
        // Extraer transacciones y calcular el saldo final correctamente
        extraerTransacciones(text, results);

        // Solo agregamos el saldo final una vez al final de las transacciones
        results.add(new String[] { "SALDO FINAL", "", "", " ","" }); //String.valueOf(saldoFinal)
        return results;
    }

   private void extraerInformacionBasica(String text, List<String[]> results) {
        this.rfc = extraerRFC(text);
        // Extraer el saldo anterior basado en la palabra clave
        this.saldoInicial = extraerSaldo(text, "Saldo Anterior");
        System.out.println("Saldo Inicial: " + saldoInicial); 
        this.saldoFinal = extraerSaldo(text, "Saldo Final");  
        System.out.println("Saldo Final calculado: " + saldoFinal);  // Calculamos el saldo final y mandamos a imrimir el saldo inicai
   }
    private void extraerTransacciones(String text, List<String[]> results) {
        Pattern pattern = Pattern.compile(regexTransacciones, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String fecha = matcher.group(1).trim();
            String descripcion = matcher.group(2) != null ? matcher.group(2).trim() : "";
            String montoOperacion = matcher.group(3) != null ? matcher.group(3).replace(",", "").trim() : "0.00";
            String saldo = matcher.group(4) != null ? matcher.group(4).replace(",", "").trim() : "0.00";

            // Limpiar y formatear descripción
            descripcion = descripcion.replaceAll("\\n", " ").replaceAll("\\s{2,}", " ");

            String deposito = "0.00";
            String retiro = "0.00";
            String referencia = "";

            // Procesamiento personalizado por banco
            switch (banco) {
                case "Banorte":
                    deposito = esPalabraClaveDescripcion(descripcion, keywordsDeposits) ? montoOperacion : "0.00";
                    retiro = esPalabraClaveDescripcion(descripcion, keywordsWithdrawals) ? montoOperacion : "0.00";
                    results.add(new String[] { fecha, descripcion, deposito, retiro, saldo });
                    break;
                case "Inbursa":
                    // Inbursa tiene un proceso diferente
                    referencia = obtenerReferencia(descripcion);
                    String concepto = descripcion.replaceFirst(Pattern.quote(referencia), "").trim();
                    deposito = esPalabraClaveDescripcion(descripcion, keywordsDeposits) ? montoOperacion : "0.00";
                    retiro = esPalabraClaveDescripcion(descripcion, keywordsWithdrawals) ? montoOperacion : "0.00";
                    results.add(new String[] { fecha, referencia, concepto, retiro, deposito, saldo });
                    break;
                case "BBVA":
                    // incluir a BBVA
                    break;
                case "Bancomer":
                    // incluir a Bancomer
                    break;
                default:
                    break;
            }
        }

        // Recalcular el saldo final después de procesar todas las transacciones
        //saldoFinal = calcularSaldoFinal(results); 
        System.out.println("Saldo Final calculado después de transacciones: " + saldoFinal);
    }

    // Método auxiliar para extraer la referencia de la descripción
    private String obtenerReferencia(String descripcion) {
        String[] palabras = descripcion.split("\\s+");
        for (String palabra : palabras) {
            if (palabra.matches("[A-Z0-9]+")) {
                return palabra;
            }
        }
        return descripcion;
    }

    private String extraerRFC(String texto) {
        Pattern rfcPattern = Pattern.compile("\\b([A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3})\\b");
        Matcher matcher = rfcPattern.matcher(texto);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "RFCDesconocido";
    }

    private String extraerMesAnio() {
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy");
        return fechaActual.format(formatter).toUpperCase();
    }

    private double extraerSaldo(String texto, String palabraClave) {
        // Ajuste del patrón para buscar "Saldo Anterior" exacto, sin importar mayúsculas o minúsculas
        Pattern saldoPattern = Pattern.compile(palabraClave + "\\s*:?\\s*([\\d,]+\\.\\d{2})", Pattern.CASE_INSENSITIVE);
        Matcher saldoMatcher = saldoPattern.matcher(texto);

        if (saldoMatcher.find()) {
            String saldoString = saldoMatcher.group(1);
            return Double.parseDouble(saldoString.replace(",", ""));
        }
        return 0.0; // Si no se encuentra saldo, retornar 0.0
    }

   /*public double calcularSaldoFinal(List<String[]> transacciones) {
        double totalDepositos = 0.0;
        double totalRetiros = 0.0;

        // Iteramos sobre las transacciones para calcular los depósitos y retiros
        for (String[] transaccion : transacciones) {
            String deposito = transaccion[2]; // Columna de depósitos
            String retiro = transaccion[3]; // Columna de retiros

            // Validación de cadenas vacías antes de convertir
            if (!deposito.isEmpty()) {
                totalDepositos += Double.parseDouble(deposito.replace(",", ""));
            }
            if (!retiro.isEmpty()) {
                totalRetiros += Double.parseDouble(retiro.replace(",", ""));
            }
        }

        // Calculamos el saldo final, sumando los depósitos y restando los retiros al saldo inicial
        saldoFinal = saldoInicial + totalDepositos - totalRetiros;

        System.out.println("Total Depositos: " + totalDepositos);
        System.out.println("Total Retiros: " + totalRetiros);
        System.out.println("Saldo Final calculado: " + saldoFinal);

        return saldoFinal;
    }*/

    private boolean esPalabraClaveDescripcion(String descripcion, String[] keywords) {
        for (String keyword : keywords) {
            if (descripcion.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public String getRFC() {
        return rfc;
    }

    public String getBanco() {
        return banco;
    }

    public String getMesAnio() {
        return mesAnio;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }

    public double getSaldoFinal() {
        return saldoFinal;
    }
}

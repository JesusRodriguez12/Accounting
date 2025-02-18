package com.innovative.accounting.dataextract;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            ConfigLoader config = new ConfigLoader("config.json");
            
            PDFProcessor pdfProcessor = new PDFProcessor(config);
            String pdfText = pdfProcessor.loadPDF();

            // Detectar el banco en el textwzo del PDF
            String banco = detectarBanco(pdfText);
            
            DataExtractor dataExtractor = new DataExtractor(config, banco);
            List<String[]> transacciones = dataExtractor.extractData(pdfText);

            String rfc = dataExtractor.getRFC();
            String mesanio = dataExtractor.getMesAnio();

            CSVWrinter csvWriter = new CSVWrinter(config);
            csvWriter.writeCSV(transacciones, rfc, banco, mesanio);

            //SaveDB saveDB = new SaveDB(config); 
            //saveDB.saveTransactions(transacciones, rfc, banco, mesanio, config);  
            //System.out.println("Transacciones guardadas en la base de datos.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Se produjo un error durante la ejecución: " + e.getMessage());
        }
    }

    private static String detectarBanco(String texto) {
        String textoLower = texto.toLowerCase();
        if (textoLower.contains("grupo financiero inbursa") || textoLower.contains("estado de cuenta inbursa")) {
            return "Inbursa";
        } else if (textoLower.contains("banorte")) {
            return "Banorte";
        } else if (textoLower.contains("bbva")) {
            return "BBVA";
        } else if (textoLower.contains("bancomer")) {
            return "Bancomer";
        }
        return "Desconocido";
    }

}

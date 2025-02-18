package com.innovative.accounting.dataextract;

import java.io.FileWriter;
import java.util.List;

public class CSVWrinter {
    private String directory;

    public CSVWrinter(ConfigLoader config) {
        this.directory = config.getCsvDirectory();
    }

    public void writeCSV(List<String[]> transactions, String rfc, String banco, String mesAnio) {
        String fileName = rfc + "_" + banco + "_" + mesAnio + ".csv";
        String filePath = directory + fileName;
        try (FileWriter csvWriter = new FileWriter(filePath)) {
            
            // Ajuste de columnas según el banco
            if ("Banorte".equalsIgnoreCase(banco)) {
                csvWriter.append("Fecha,Descripción,Depósito,Retiro,Saldo\n");
            } else if ("Inbursa".equalsIgnoreCase(banco)) {
                csvWriter.append("Fecha,Referencia,Concepto,Cargo,Abono,Saldo\n");
            }

            // Escribir las transacciones
            for (String[] transaction : transactions) {
                // Asegurarse de que la transacción tenga suficientes columnas
                if (transaction.length >= 5) {  // Para Banorte
                    String descripcion = transaction[1].contains(",") ? "\"" + transaction[1] + "\"" : transaction[1];
                    csvWriter.append(String.join(",", transaction[0], descripcion, transaction[2], transaction[3], transaction[4])).append("\n");
                } else if (transaction.length >= 6 && "Inbursa".equalsIgnoreCase(banco)) {  // Para Inbursa
                    String descripcion = transaction[2].contains(",") ? "\"" + transaction[2] + "\"" : transaction[2];
                    csvWriter.append(String.join(",", transaction[0], descripcion, transaction[2], transaction[3], transaction[4], transaction[5])).append("\n");
                } else {
                    // Si la transacción no tiene la cantidad esperada de elementos
                    System.err.println("Transacción incompleta: " + String.join(",", transaction));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

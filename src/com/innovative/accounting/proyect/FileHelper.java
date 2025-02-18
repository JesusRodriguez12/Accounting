package com.innovative.accounting.proyect;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileHelper {

    // Método para escribir las transacciones en un archivo CSV
    public static void writeTransactionsToCSV(String filePath, List<Transaction> transacciones) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Escribir el encabezado
            writer.append("Fecha,Descripción,Monto Retiro,Saldo,Monto Depósito\n");

            // Escribir cada transacción
            for (Transaction transaccion : transacciones) {
                writer.append(transaccion.getFecha()).append(",");
                writer.append(transaccion.getDescripcion()).append(",");
                writer.append(transaccion.getMontoRetiro()).append(",");
                writer.append(transaccion.getSaldo()).append(",");
                writer.append(transaccion.getMontoDeposito()).append("\n");
            }
        }
    }
}

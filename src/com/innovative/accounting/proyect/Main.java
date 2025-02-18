package com.innovative.accounting.proyect;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String pdfPath = "D:/Escritorio/Trabajo_InnovativeSoft/Proyectos/EdoCtaBan.PDF";
        File pdfFile = new File(pdfPath);

        if (!pdfFile.exists()) {
            System.out.println("El archivo PDF no existe en la ruta	 especificada.");
            return;
        }

        try {
            // Procesar el archivo PDF para extraer el texto
            String textoDelPDF = PDFProcessor.extractTextFromPDF(pdfFile);

            // Identificar el banco en base al contenido del PDF
            String bankName = PropertyFileHelper.identifyBank(textoDelPDF);
            System.out.println("Banco identificado: " + bankName);

            // Inicializar el extractor de transacciones con las configuraciones del banco
            TransactionExtractor.initialize(bankName);

            // Extraer las transacciones desde el texto del PDF
            List<Transaction> transacciones = TransactionExtractor.extractTransactions(textoDelPDF);

            // Imprimir las transacciones extraídas
            for (Transaction transaccion : transacciones) {
                System.out.println(transaccion);
                System.out.println("------------");
            }

            // Calcular el saldo basado en las transacciones
            double saldoCalculado = TransactionExtractor.calcularSaldo(transacciones);

            // Verificar si el saldo calculado coincide con el saldo final del estado de cuenta
            TransactionExtractor.verificarSaldo(saldoCalculado);

        } catch (IOException e) {
            System.err.println("Error al procesar el archivo PDF: " + e.getMessage());
        }
    }
}

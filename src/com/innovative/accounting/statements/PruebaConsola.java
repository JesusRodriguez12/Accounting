package com.innovative.accounting.statements;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class PruebaConsola {
    public static void main(String[] args) {
        File pdfFile = new File("D:/Escritorio/Trabajo_InnovativeSoft/Proyectos/EdoCtaBan.PDF");

        if (!pdfFile.exists()) {
            System.out.println("El archivo PDF no existe en la ruta especificada.");
            return;
        }

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String textoDelPDF = pdfStripper.getText(document);

            String regex = "(\\d{2}-\\w{3}-\\d{2})\\s+(.*?)\\s+(\\d{1,3}(?:,\\d{3})*\\.\\d{2}|\\d+\\.\\d{2}|0\\.00)\\s+(\\d{1,3}(?:,\\d{3})*\\.\\d{2}|\\d+\\.\\d{2}|0\\.00)?\\s+(\\d{1,3}(?:,\\d{3})*\\.\\d{2}|\\d+\\.\\d{2}|0\\.00)?";

            Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(textoDelPDF);

            List<String[]> resultados = new ArrayList<>();

            // Palabras clave para diferenciar entre depósitos y retiros
            String[] keywordsDepositos = {"ordenante"};
            String[] keywordsRetiros = {"traspaso", "a la cuenta", "iva", "comisión", "compra"};

            while (matcher.find()) {
                String fecha = matcher.group(1);
                String descripcion = matcher.group(2).trim().replaceAll("\\s{2,}", " ");
                String monto1 = matcher.group(3) != null ? matcher.group(3) : "0.00";
                String monto2 = matcher.group(4) != null ? matcher.group(4) : "0.00";
                String monto3 = matcher.group(5) != null ? matcher.group(5) : "0.00";

                String montoDeposito = "0.00";
                String montoRetiro = "0.00";
                String saldo = "0.00";

                // Identificar si es saldo actual
                if (descripcion.toLowerCase().contains("saldo anterior") || descripcion.toLowerCase().contains("saldo final")) {
                    saldo = monto1;
                } else {
                    boolean esDeposito = descripcion.toLowerCase().contains("ordenante") || contienePalabraClave(descripcion, keywordsDepositos);
                    boolean esRetiro = contienePalabraClave(descripcion, keywordsRetiros);

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
                }

                resultados.add(new String[]{fecha, descripcion, montoRetiro, saldo, montoDeposito});
            }

            // Imprimir los resultados
            imprimirResultados(resultados);

        } catch (IOException e) {
            System.err.println("Error al procesar el archivo PDF: " + e.getMessage());
        }
    }

    // Método para verificar si la descripción contiene alguna palabra clave
    private static boolean contienePalabraClave(String descripcion, String[] palabrasClave) {
        for (String keyword : palabrasClave) {
            if (descripcion.toLowerCase().contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    // Método para imprimir los resultados
    private static void imprimirResultados(List<String[]> resultados) {
        for (String[] transaccion : resultados) {
            System.out.println("Fecha: " + transaccion[0]);
            System.out.println("Descripción/Establecimiento: " + transaccion[1]);
            System.out.println("Monto del Retiro: " + transaccion[2]);
            System.out.println("Saldo: " + transaccion[3]);
            System.out.println("Monto del Depósito: " + transaccion[4]);
            System.out.println("------------");
        }
    }
}

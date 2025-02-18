package com.innovative.accounting.statements;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PruebaDiv {
    public static void main(String[] args) {
        File pdfFile = new File("D:/Escritorio/Trabajo_InnovativeSoft/Proyectos/EdoCtaBan.PDF");

        if (!pdfFile.exists()) {
            System.out.println("El archivo PDF no existe en la ruta especificada.");
            return;
        }

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String textoDelPDF = pdfStripper.getText(document);

            // Extraer información de la empresa, RFC, banco, y mes/año
            String empresa = extraerEmpresa(textoDelPDF);
            String rfcEmpresa = extraerRFC(textoDelPDF);
            String banco = extraerBanco(textoDelPDF);
            String mesAnio = extraerMesAnio(textoDelPDF);

            // Extraer el saldo anterior antes de procesar las transacciones
            String saldoAnterior = extraerSaldoAnterior(textoDelPDF);

            // Ajuste del regex para capturar fechas y transacciones correctamente
            String regexTransacciones = "(\\d{2}-\\w{3}-\\d{2})\\s+([\\s\\S]*?)\\s+(\\d{1,3}(?:,\\d{3})*\\.\\d{2})\\s+(\\d{1,3}(?:,\\d{3})*\\.\\d{2})";
            Pattern patternTransacciones = Pattern.compile(regexTransacciones);
            Matcher matcherTransacciones = patternTransacciones.matcher(textoDelPDF);

            List<String[]> resultados = new ArrayList<>();
            double saldoActual = 0;

            // Agregar saldo anterior como primera fila
            if (saldoAnterior != null) {
                resultados.add(new String[] {
                    "", "\"Saldo Anterior\"", "", "", "$" + saldoAnterior // Añadir el saldo anterior con formato adecuado
                });
            }

            // Procesar las transacciones
            while (matcherTransacciones.find()) {
                String fecha = matcherTransacciones.group(1).trim();
                String descripcion = matcherTransacciones.group(2)
                        .replaceAll("\\n", " ") // Sustituimos los saltos de línea por espacios
                        .replaceAll("\\s{2,}", " ") // Eliminamos espacios múltiples
                        .trim();
                String monto = matcherTransacciones.group(3).replace(",", "").trim();
                String saldo = matcherTransacciones.group(4).replace(",", "").trim();

                double montoDouble = Double.parseDouble(monto);
                saldoActual = Double.parseDouble(saldo);

                double retiro = 0.0;
                double deposito = 0.0;

                // Detectamos si es depósito o retiro basado en palabras clave
                boolean esDeposito = descripcion.toLowerCase().contains("ordenante") || contienePalabraClave(descripcion, keywordsDepositos);
                boolean esRetiro = contienePalabraClave(descripcion, keywordsRetiros);

                if (esDeposito) {
                    deposito = montoDouble;
                } else if (esRetiro) {
                    retiro = montoDouble;
                } else {
                    // Si no se reconoce, asume que es un retiro
                    retiro = montoDouble;
                }

                // Asegurar que la descripción esté separada y formateada correctamente
                resultados.add(new String[] {
                    fecha,
                    "\"" + descripcion + "\"", // Descripción completa
                    deposito > 0 ? "$" + String.format("%.2f", deposito) : "",
                    retiro > 0 ? "$" + String.format("%.2f", retiro) : "",
                    "$" + String.format("%.2f", saldoActual)
                });
            }

            // Guardar el archivo CSV en la ruta específica
            String rutaDirectorio = "D:/Escritorio/Trabajo_InnovativeSoft/Proyectos/";
            String nombreArchivo = rutaDirectorio + rfcEmpresa + "_" + banco + "_" + mesAnio + ".csv";
            File csvFile = new File(nombreArchivo);
            try (FileWriter csvWriter = new FileWriter(csvFile)) {
                // Escribir el encabezado
                csvWriter.append("Fecha,Descripción,Depósito,Retiro,Saldo\n");
                for (String[] transaccion : resultados) {
                    csvWriter.append(String.join(",", transaccion));
                    csvWriter.append("\n");
                }
            }

            System.out.println("Archivo CSV generado: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al procesar el archivo PDF: " + e.getMessage());
        }
    }

    // Función auxiliar para extraer el saldo anterior
    private static String extraerSaldoAnterior(String texto) {
        Pattern saldoPattern = Pattern.compile("SALDO ANTERIOR\\s+(\\d{1,3}(?:,\\d{3})*\\.\\d{2})");
        Matcher matcher = saldoPattern.matcher(texto);
        if (matcher.find()) {
            return matcher.group(1).replace(",", "");
        }
        return null;
    }

    // Función auxiliar para detectar palabras clave
    private static boolean contienePalabraClave(String descripcion, String[] keywords) {
        for (String keyword : keywords) {
            if (descripcion.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // Arrays con palabras clave para depósitos y retiros
    private static String[] keywordsDepositos = { "ordenante" };
    private static String[] keywordsRetiros = { "retiro", "pago", "compra", "cargo" };

    // Funciones para extraer información (sin cambios)
    private static String extraerEmpresa(String texto) {
        Pattern empresaPattern = Pattern.compile("(?i)(?:empresa|comercializadora|nombre:\\s*)([\\w\\s]+)");
        Matcher matcher = empresaPattern.matcher(texto);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "EmpresaDesconocida";
    }

    private static String extraerRFC(String texto) {
        Pattern rfcPattern = Pattern.compile("\\b([A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3})\\b");
        Matcher matcher = rfcPattern.matcher(texto);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "RFCDesconocido";
    }

    private static String extraerBanco(String texto) {
        if (texto.toLowerCase().contains("bancomer")) {
            return "Bancomer";
        } else if (texto.toLowerCase().contains("santander")) {
            return "Santander";
        } else if (texto.toLowerCase().contains("banorte")) {
            return "Banorte";
        }
        return "BancoDesconocido";
    }

    private static String extraerMesAnio(String texto) {
        Pattern fechaPattern = Pattern.compile("\\b(\\d{2}-\\w{3}-\\d{2})\\b");
        Matcher matcher = fechaPattern.matcher(texto);
        if (matcher.find()) {
            String[] partesFecha = matcher.group(1).split("-");
            String mes = partesFecha[1].toUpperCase();
            String anio = "20" + partesFecha[2];
            return mes + "-" + anio;
        }
        return "FechaDesconocida";
    }
}

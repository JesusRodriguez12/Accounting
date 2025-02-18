package com.innovative.accounting.proyect;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.HashMap;

public class PropertyFileHelper {
    private static final Properties properties = new Properties();
    private static final Map<String, String> BANK_PATTERNS = new HashMap<>();

    static {
        try (InputStream input = PropertyFileHelper.class.getClassLoader().getResourceAsStream("transaction-extractor.properties")) {
            if (input == null) {
                System.out.println("Lo siento, no se pudo encontrar transaction-extractor.properties");
                // Maneja el error sin terminar la ejecución
            } else {
                properties.load(input);
                
                // Cargar patrones de identificación de bancos
                for (String key : properties.stringPropertyNames()) {
                    if (key.matches(".*\\.patterns")) { // Asegúrate de usar el sufijo correcto
                        String bankName = key.substring(0, key.indexOf('.'));
                        BANK_PATTERNS.put(bankName, properties.getProperty(key));
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Método para obtener propiedades específicas de transacciones
    public static String getProperty(String bank, String key) {
        String fullKey = bank + "." + key; // Concatenar el banco y la clave
        return properties.getProperty(fullKey);
    }

    public static String[] getPropertyAsArray(String bank, String key) {
        String value = getProperty(bank, key); // Utilizar la clave completa
        return value != null ? value.split(",") : new String[]{};
    }

    // Método para identificar el banco usando patrones cargados
    public static String identifyBank(String text) {
        for (Map.Entry<String, String> entry : BANK_PATTERNS.entrySet()) {
            if (text.matches("(?i).*" + entry.getValue() + ".*")) {
                return entry.getKey();
            }
        }
        return "Desconocido"; // O maneja el caso cuando no se identifica el banco
    }
}

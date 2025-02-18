package com.innovative.accounting.dataextract;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigLoader {
    private final JsonNode config;
    private final JsonNode bankConfig;

    public ConfigLoader(String filePath) throws IOException {
        // Carga el archivo de configuración
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new RuntimeException("Archivo de configuración no encontrado: " + filePath);
        }
        this.config = objectMapper.readTree(inputStream);
        this.bankConfig = config.get("bancos");
    }

    public String getPdfPath() {
        return config.get("pdfPath").asText();
    }

    public String getCsvDirectory() {
        return config.get("csvDirectory").asText();
    }

    public JsonNode getRethinkDBConfig() {
        return config.get("rethinkDB");
    }

    public String getRegexTransacciones(String banco) {
        return bankConfig.get(banco).get("regexTransacciones").asText();
    }

    public List<String> getKeywordsDeposits(String banco) {
        return getStringListFromJsonArray(bankConfig.get(banco).get("keywordsDeposits"));
    }

    public List<String> getKeywordsWithdrawals(String banco) {
        return getStringListFromJsonArray(bankConfig.get(banco).get("keywordsWithdrawals"));
    }

    public String getSaldoInicialLabel(String banco) {
        return bankConfig.get(banco).get("saldoInicialLabel").asText();
    }

    public String getSaldoFinalLabel(String banco) {
        return bankConfig.get(banco).get("saldoFinalLabel").asText();
    }

    public JsonNode getConfig() {
        return config;
    }

    // Método auxiliar para convertir un arreglo JSON a una lista de cadenas
    private List<String> getStringListFromJsonArray(JsonNode jsonArray) {
        List<String> list = new ArrayList<>();
        if (jsonArray != null && jsonArray.isArray()) {
            Iterator<JsonNode> elements = jsonArray.elements();
            while (elements.hasNext()) {
                list.add(elements.next().asText());
            }
        }
        return list;
    }
}

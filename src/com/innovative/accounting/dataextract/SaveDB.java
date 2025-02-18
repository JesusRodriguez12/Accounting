package com.innovative.accounting.dataextract;

import com.fasterxml.jackson.databind.JsonNode;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;



public class SaveDB {

    private static final RethinkDB r = RethinkDB.r;
    private final Connection connection;
    private final String dbName;
    private final String tableName = "transacciones";

    public SaveDB(ConfigLoader config) {
        JsonNode configNode = (JsonNode) config.getConfig(); // Cast explícito
        this.connection = createConnection(configNode);
        this.dbName = configNode.get("rethinkDB").get("database").asText();
        ensureDatabaseAndTableExist();
    }

    private Connection createConnection(JsonNode configNode) {
        JsonNode rethinkConfig = configNode.get("rethinkDB");
        if (rethinkConfig == null) {
            throw new IllegalArgumentException("La configuración de RethinkDB no se encuentra en el archivo de configuración.");
        }

        String host = rethinkConfig.get("host").asText();
        int port = rethinkConfig.get("port").asInt();

        return r.connection().hostname(host).port(port).connect();
    }

    private void ensureDatabaseAndTableExist() {
        // Obtener la lista de bases de datos
        Iterable<Object> databasesResult = r.dbList().run(connection); // Resultado iterable
        List<String> databaseNames = new ArrayList<>();
        
        // Iterar sobre los resultados y convertir cada objeto a String
        for (Object db : databasesResult) {
            databaseNames.add(db.toString()); // Convertir a String
        }

        // Verificar si la base de datos existe
        if (!databaseNames.contains(dbName)) {
            r.dbCreate(dbName).run(connection);
            System.out.println("Base de datos creada: " + dbName);
        }

        // Obtener la lista de tablas en la base de datos especificada
        Iterable<Object> tablesResult = r.db(dbName).tableList().run(connection); // Resultado iterable
        List<String> tableNames = new ArrayList<>();
        
        // Iterar sobre los resultados y convertir cada objeto a String
        for (Object table : tablesResult) {
            tableNames.add(table.toString()); // Convertir a String
        }

        // Verificar si la tabla existe
        if (!tableNames.contains(tableName)) {
            r.db(dbName).tableCreate(tableName).run(connection);
            System.out.println("Tabla creada: " + tableName);
        }
    }

    public void saveTransactions(List<String[]> transacciones, String rfc, String banco, String mesanio, ConfigLoader config) {
        // Obtener la configuración del banco
        JsonNode configNode = config.getConfig();  // Obtener la configuración completa
        JsonNode bancoConfig = configNode.get("bancos").get(banco);  // Obtener la configuración específica del banco

        if (bancoConfig == null) {
            throw new IllegalArgumentException("La configuración para el banco " + banco + " no se encuentra.");
        }

        String[] transactionKeys = getTransactionKeysForBank(bancoConfig); // Obtener las claves de la transacción

        for (String[] transaccion : transacciones) {
            // Crear un mapa para cada transacción
            Map<String, Object> document = new HashMap<>();
            document.put(transactionKeys[0], rfc);
            document.put(transactionKeys[1], banco);
            document.put(transactionKeys[2], mesanio);
            document.put(transactionKeys[3], transaccion[0]);  // Fecha
            document.put(transactionKeys[4], transaccion[1]);  // Descripción
            document.put(transactionKeys[5], transaccion[2]);  // Monto Depósito
            document.put(transactionKeys[6], transaccion[3]);  // Monto Retiro
            document.put(transactionKeys[7], transaccion[4]);  // Saldo

            // Insertar el documento en la tabla de RethinkDB
            r.db(dbName).table(tableName).insert(document).run(connection);
            System.out.println("Transacción guardada: " + transaccion[1]);
        }
    }
    private String[] getTransactionKeysForBank(JsonNode bancoConfig) {
        // Obtener el array "transactionKeys" desde la configuración del banco
        JsonNode transactionKeysNode = bancoConfig.get("transactionKeys");

        if (transactionKeysNode == null || !transactionKeysNode.isArray()) {
            throw new IllegalArgumentException("No se encontraron claves de transacción para el banco.");
        }

        // Convertir el array de JsonNode a un array de String
        List<String> transactionKeysList = new ArrayList<>();
        for (JsonNode keyNode : transactionKeysNode) {
            transactionKeysList.add(keyNode.asText());  // Convertir cada JsonNode a String
        }

        // Convertir la lista a un array de Strings
        return transactionKeysList.toArray(new String[0]);
    }

    public void close() {
        if (connection != null) {
            connection.close();
            System.out.println("Conexión a RethinkDB cerrada con éxito.");
        }
    }
}

package com.innovative.accounting.proyect;

public class Transaction {
    private String fecha;
    private String descripcion;
    private String montoRetiro;
    private String saldo;
    private String montoDeposito;

    public Transaction(String fecha, String descripcion, String montoRetiro, String saldo, String montoDeposito) {
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.montoRetiro = montoRetiro;
        this.saldo = saldo;
        this.montoDeposito = montoDeposito;
    }

    public String getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getMontoRetiro() {
        return montoRetiro;
    }

    public String getSaldo() {
        return saldo;
    }

    public String getMontoDeposito() {
        return montoDeposito;
    }

    @Override
    public String toString() {
        return "Fecha: " + fecha + "\nDescripción/Establecimiento: " + descripcion + "\nMonto del Retiro: " + montoRetiro + "\nSaldo: " + saldo + "\nMonto del Depósito: " + montoDeposito;
    }
}

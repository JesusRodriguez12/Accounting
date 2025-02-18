package com.innovative.accounting.statements;

public class Prueba {

	public static void main(String[] args) {
		String sql= "?,?,?,?,?,?,?,?)";
		System.out.println(sql);
	sql = sql.replaceAll(",\\?\\)", ")");
System.out.println(sql);
	}
}


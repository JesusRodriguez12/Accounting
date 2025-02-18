package com.innovative.accounting.statements;

import java.io.IOException;

public class Process {

	Process (String file) {
		ReadPdf r = new ReadPdf(file);
		ParseStatement p = new ParseStatement(r.getPdfText());
//		save(p.getStatement());
	}

	public static void main(String args[]) throws IOException {
		new Process("/work/EdoCtaBan.PDF");
	}

}

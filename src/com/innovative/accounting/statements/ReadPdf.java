package com.innovative.accounting.statements;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

import com.innovative.accounting.beans.Statement;
import com.innovative.utils.DateHelper;

public class ReadPdf {
	String pdfText;
	PDDocument document ;
	int month;
	int year;
	Statement sh= new Statement();
	DateHelper dh =new DateHelper();
	
	public ReadPdf(String filename) {
		openPdf(filename);
//		readPages();
		print();
		closePdf();
	}
	public void openPdf(String filename) {
		File file = new File(filename);
		try {
			document = Loader.loadPDF(file);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			pdfText = pdfStripper.getText(document);
		} catch (IOException e) {e.printStackTrace();}
	}
	public void print() {
		System.out.println(pdfText);
	}
	public void closePdf() {
		try {
			document.close();
		} catch (IOException e) {e.printStackTrace();}
	}	
	public void getPeriod() {
		int s = pdfText.indexOf("Periodo Del ") + 12;
		int e = s + 17;
		String d = pdfText.substring(s,e); d= dh.SpanishMonthToStringNumber(d );
		Date p = dh.stringToDate( d ,"dd/MM/yyyy");
		sh.setMonth( (dh.convertToLocalDateViaInstant(p).getMonth()).getValue());
		sh.setYear(dh.convertToLocalDateViaInstant(p).getYear());
	}
	public void readPages() {
		for (PDPage page : document.getPages())
		{
			System.out.println( page.getMetadata());
		}
	}
	
	public String getPdfText() {
		return pdfText;
	}
	public void setPdfText(String pdfText) {
		this.pdfText = pdfText;
	}
	   public static void main(String args[]) throws IOException {
		      //Loading an existing document
			   ReadPdf rp = new ReadPdf("/work/EdoCtaBan.PDF");
		   }
	   
}

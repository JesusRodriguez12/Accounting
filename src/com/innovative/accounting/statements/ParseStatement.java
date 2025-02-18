package com.innovative.accounting.statements;

import com.innovative.accounting.beans.Statement;
import com.innovative.utils.RegExpHelper;
import com.innovative.utils.RegExpPatterns;
import com.innovative.utils.beans.RegexMatches;

public class ParseStatement {

	private final String MENO_SOH = "INFORMACIÓN DEL PERIODO"; // MENO
	private final String MENO_EOH = "DETALLE DE MOVIMIENTOS";
	private final String MENO_SOM = "[0-9]{2}[-][A-Z]{3}[-][0-9]{2}"; //DD-MMM-YY
	private final String MENO_EOM = "[0-9,]+[\\.][0-9]{2}[ \t]+[0-9,]+[\\.][0-9]{2}";
	private final String MENO_H ="";
	private final String MENO_M ="";
	private String SOH; // Start of Header
	private String EOH; // End of Header
	private String SOM; // Star of movement
	private String EOM; // End of moevment
	private String H; // Header regex
	private String M; // Movement regex
	private String statementText, header=null, movement=null;
	Statement st = new Statement();
	String bic4;
	int currentPosition = 0, start, end;
	RegExpHelper rh =new RegExpHelper();
	RegExpPatterns rp = new RegExpPatterns();
	
	ParseStatement (String statement)
	{
		this.statementText=statement;
		if ( statementText == null) return;
		setPatterns();
		getHeader();
		getMovements();
	}
	public void setPatterns()
	{
		if (statementText.contains("banorte")) {
			this.SOH=MENO_SOH;
			this.EOH=MENO_EOH;
			this.SOM=MENO_SOM;
			this.EOM=MENO_SOM;
			this.H = MENO_H;
			this.M = MENO_M;
			this.bic4="MENO";
		}
	}
	public void getHeader() {
		if (statementText.indexOf(SOH) == -1 || statementText.indexOf(EOH) <= statementText.indexOf(SOH) ) return ;
		header=statementText.substring(statementText.indexOf(SOH), statementText.indexOf(EOH));
		if (bic4.equals("MENO"))
			createMENOHeader();
	}
	public void getMovements() {
			while (findNextMovement()) {
				if (bic4.equals("MENO"))
					addMENOMovement();
			};
	}
	public boolean findNextMovement() {
		boolean more = false;
		start = statementText.indexOf(SOM,currentPosition);
		end = statementText.indexOf(SOM,currentPosition);
		start = rh.indexMatches(statementText.substring(currentPosition),SOM);
		end =	rh.indexMatches(statementText.substring(start),EOM);
//		if ( start == -1 || start  <= end ) return more;
		movement=statementText.substring(statementText.indexOf(SOM,currentPosition), statementText.indexOf(EOM,currentPosition));
		currentPosition = statementText.indexOf(EOM,currentPosition);
		more = true;
		return more;
	}
	private void createMENOHeader()
	{
	}
	private void addMENOMovement()
	{
	}
	public static void main(String[] args)
	{
		
	}
}

package com.innovative.accounting.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Statement {
	int 
	statementId, 
	month,
	year,
	numberOfDebits,
	numberOfCredits,
	numberOfMovementsReconcilied;
	private String 
	senderBic, 
	accountId, 
	stReference, 
	currency, 
	receiverTaxId,
	holderName;
	private java.util.Date 
	openingBalanceDate, 
	closingBalanceDate,
	dateTimeBalance;
	BigDecimal 
	openingBalance, 
	closingBalance,
	totalDebitsAmount,
	totalCreditsAmount,	
	totalDebitAmountReconcilied,
	totalCreditAmountReconcilied;
	
	List <StatementMovement> lines;
	
	public void addLine(StatementMovement line) {
		if (lines == null) lines = new ArrayList();
		lines.add(line);
	}
	public List<StatementMovement> getLines() {
		return lines;
	}
	public void setLines(List<StatementMovement> lines) {
		this.lines = lines;
	}
	public int getStatementId() {
		return statementId;
	}
	public void setStatementId(int statementId) {
		this.statementId = statementId;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getNumberOfDebits() {
		return numberOfDebits;
	}
	public void setNumberOfDebits(int numberOfDebits) {
		this.numberOfDebits = numberOfDebits;
	}
	public int getNumberOfCredits() {
		return numberOfCredits;
	}
	public void setNumberOfCredits(int numberOfCredits) {
		this.numberOfCredits = numberOfCredits;
	}
	public int getNumberOfMovementsReconcilied() {
		return numberOfMovementsReconcilied;
	}
	public void setNumberOfMovementsReconcilied(int numberOfMovementsReconcilied) {
		this.numberOfMovementsReconcilied = numberOfMovementsReconcilied;
	}
	public String getSenderBic() {
		return senderBic;
	}
	public void setSenderBic(String senderBic) {
		this.senderBic = senderBic;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getStReference() {
		return stReference;
	}
	public void setStReference(String stReference) {
		this.stReference = stReference;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getReceiverTaxId() {
		return receiverTaxId;
	}
	public void setReceiverTaxId(String receiverTaxId) {
		this.receiverTaxId = receiverTaxId;
	}
	public String getHolderName() {
		return holderName;
	}
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}
	public java.util.Date getOpeningBalanceDate() {
		return openingBalanceDate;
	}
	public void setOpeningBalanceDate(java.util.Date openingBalanceDate) {
		this.openingBalanceDate = openingBalanceDate;
	}
	public java.util.Date getClosingBalanceDate() {
		return closingBalanceDate;
	}
	public void setClosingBalanceDate(java.util.Date closingBalanceDate) {
		this.closingBalanceDate = closingBalanceDate;
	}
	public java.util.Date getDateTimeBalance() {
		return dateTimeBalance;
	}
	public void setDateTimeBalance(java.util.Date dateTimeBalance) {
		this.dateTimeBalance = dateTimeBalance;
	}
	public BigDecimal getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(BigDecimal openingBalance) {
		this.openingBalance = openingBalance;
	}
	public BigDecimal getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(BigDecimal closingBalance) {
		this.closingBalance = closingBalance;
	}
	public BigDecimal getTotalDebitsAmount() {
		return totalDebitsAmount;
	}
	public void setTotalDebitsAmount(BigDecimal totalDebitsAmount) {
		this.totalDebitsAmount = totalDebitsAmount;
	}
	public BigDecimal getTotalCreditsAmount() {
		return totalCreditsAmount;
	}
	public void setTotalCreditsAmount(BigDecimal totalCreditsAmount) {
		this.totalCreditsAmount = totalCreditsAmount;
	}
	public BigDecimal getTotalDebitAmountReconcilied() {
		return totalDebitAmountReconcilied;
	}
	public void setTotalDebitAmountReconcilied(BigDecimal totalDebitAmountReconcilied) {
		this.totalDebitAmountReconcilied = totalDebitAmountReconcilied;
	}
	public BigDecimal getTotalCreditAmountReconcilied() {
		return totalCreditAmountReconcilied;
	}
	public void setTotalCreditAmountReconcilied(BigDecimal totalCreditAmountReconcilied) {
		this.totalCreditAmountReconcilied = totalCreditAmountReconcilied;
	}
	
	
}

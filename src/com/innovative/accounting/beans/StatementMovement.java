package com.innovative.accounting.beans;

import java.math.BigDecimal;
import java.util.Date;

public class StatementMovement {
	Date settlementDateTime;
	Date registerDateTime;
	String dcMark;
	BigDecimal debitAmount;
	BigDecimal creditAmount;
	BigDecimal balance;	
	String description;
	String transactionCode;
	String accountOwnerReference;
	String bankReference;
	String centralBankReference;
	String beneficiaryTaxId;
	String beneficiary;
	String invoiceSerie;
	String invoiceSequence;
	String beneficiaryReference;
	java.math.BigDecimal amountReconcilied;
	boolean isReconcilied;
	String reconciliationCriteria;
	public Date getSettlementDateTime() {
		return settlementDateTime;
	}
	public void setSettlementDateTime(Date settlementDateTime) {
		this.settlementDateTime = settlementDateTime;
	}
	public Date getRegisterDateTime() {
		return registerDateTime;
	}
	public void setRegisterDateTime(Date registerDateTime) {
		this.registerDateTime = registerDateTime;
	}
	public String getDcMark() {
		return dcMark;
	}
	public void setDcMark(String dcMark) {
		this.dcMark = dcMark;
	}
	public BigDecimal getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public String getAccountOwnerReference() {
		return accountOwnerReference;
	}
	public void setAccountOwnerReference(String accountOwnerReference) {
		this.accountOwnerReference = accountOwnerReference;
	}
	public String getBankReference() {
		return bankReference;
	}
	public void setBankReference(String bankReference) {
		this.bankReference = bankReference;
	}
	public String getCentralBankReference() {
		return centralBankReference;
	}
	public void setCentralBankReference(String centralBankReference) {
		this.centralBankReference = centralBankReference;
	}
	public String getBeneficiaryTaxId() {
		return beneficiaryTaxId;
	}
	public void setBeneficiaryTaxId(String beneficiaryTaxId) {
		this.beneficiaryTaxId = beneficiaryTaxId;
	}
	public String getBeneficiary() {
		return beneficiary;
	}
	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
	}
	public String getInvoiceSerie() {
		return invoiceSerie;
	}
	public void setInvoiceSerie(String invoiceSerie) {
		this.invoiceSerie = invoiceSerie;
	}
	public String getInvoiceSequence() {
		return invoiceSequence;
	}
	public void setInvoiceSequence(String invoiceSequence) {
		this.invoiceSequence = invoiceSequence;
	}
	public String getBeneficiaryReference() {
		return beneficiaryReference;
	}
	public void setBeneficiaryReference(String beneficiaryReference) {
		this.beneficiaryReference = beneficiaryReference;
	}
	public java.math.BigDecimal getAmountReconcilied() {
		return amountReconcilied;
	}
	public void setAmountReconcilied(java.math.BigDecimal amountReconcilied) {
		this.amountReconcilied = amountReconcilied;
	}
	public boolean isReconcilied() {
		return isReconcilied;
	}
	public void setReconcilied(boolean isReconcilied) {
		this.isReconcilied = isReconcilied;
	}
	public String getReconciliationCriteria() {
		return reconciliationCriteria;
	}
	public void setReconciliationCriteria(String reconciliationCriteria) {
		this.reconciliationCriteria = reconciliationCriteria;
	}
	
}

package org.aptech.t2303e.lab1.bankAccount.dao;

import org.aptech.t2303e.lab1.bankAccount.entity.BankAccount;

import java.util.List;

public interface BankAccountDao {
    boolean insertBankAcc(BankAccount bankAcc);
    List<BankAccount> getBankAccByIdCard(String idCard);
    List<BankAccount> getAllBankAcc();
    String getCardTypeByCardNo(String cardNo);
}

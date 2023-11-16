package org.aptech.t2303e.lab1.bankAccountDao;

import org.aptech.t2303e.lab1.BankAccount;

import java.util.List;

public interface BankAccountDao {
    boolean insertBankAcc(BankAccount bankAcc);
    List<BankAccount> getBankAccByIdCard(String idCard);
}

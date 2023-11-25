package org.aptech.t2303e.lab1.transAccount;

import org.aptech.t2303e.lab1.bankAccount.dao.BankAccountDao;
import org.aptech.t2303e.lab1.bankAccount.dao.impl.BankAccountDaoImpl;

public class TransAccountValid {
    BankAccountDao bankAcc = new BankAccountDaoImpl();

    public String validCardNoInSystem(String cardNo){

        for (int i = 0; i <bankAcc.getAllBankAcc().size() ; i++) {
            if(bankAcc.getAllBankAcc().get(i).getCardNo().equals(cardNo)){
                return bankAcc.getAllBankAcc().get(i).getCardType();
            }
        }
        return null;
    }
}

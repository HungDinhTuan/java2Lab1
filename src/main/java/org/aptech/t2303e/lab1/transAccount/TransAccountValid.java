package org.aptech.t2303e.lab1.transAccount;

import org.aptech.t2303e.lab1.bankAccount.bankAccountDao.BankAccountDao;
import org.aptech.t2303e.lab1.bankAccount.bankAccountDao.BankAccountDaoImpl;

public class TransAccountValid {
    BankAccountDao bankAcc = new BankAccountDaoImpl();

    public String validCardNoInSystem(TransAccount transAccount){

        for (int i = 0; i <bankAcc.getAllBankAcc().size() ; i++) {
            if(bankAcc.getAllBankAcc().get(i).getCardNo().equals(transAccount.getCardNo())){
                return bankAcc.getAllBankAcc().get(i).getCardType();
            }
        }
        return null;
    }
}

package org.aptech.t2303e.lab1.transAccount.transAccountDao;

import org.aptech.t2303e.lab1.transAccount.TransAccount;
import org.aptech.t2303e.lab1.transAccount.exceptionTransAcc.NotEnoughMoneyException;

public interface TransAccountDao {
    boolean insertTransAccount(TransAccount transAccount, double deposit, double withDraw) throws NotEnoughMoneyException;
}

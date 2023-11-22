package org.aptech.t2303e.lab1.transAccount.transAccountService;

import org.aptech.t2303e.lab1.transAccount.TransAccount;
import org.aptech.t2303e.lab1.transAccount.exceptionTransAcc.NotEnoughMoneyException;

public interface TransAccountService {
    double changeAmount(TransAccount transAccount, double deposit, double withDraw) throws NotEnoughMoneyException;
}

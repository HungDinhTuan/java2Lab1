package org.aptech.t2303e.lab1.transAccount.dao;

import org.aptech.t2303e.lab1.transAccount.entity.TransAccount;
import org.aptech.t2303e.lab1.transAccount.exception.NotEnoughMoneyException;

import java.util.List;

public interface TransAccountDao {
    boolean insertTransAccountDeposit(TransAccount transAccount, double depositNum);
    boolean insertTransAccountWithDraw(TransAccount transAccount, double withdrawNum) throws NotEnoughMoneyException;
    TransAccount findAmountWithTransIdMax(String cardNo);
    List<TransAccount> findAll(String cardType);
}

package org.aptech.t2303e.lab1.transAccount.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.aptech.t2303e.lab1.bankAccount.dao.BankAccountDao;
import org.aptech.t2303e.lab1.bankAccount.dao.impl.BankAccountDaoImpl;
import org.aptech.t2303e.lab1.transAccount.dao.TransAccountDao;
import org.aptech.t2303e.lab1.transAccount.dao.impl.HybridTransAccountDaoImpl;
import org.aptech.t2303e.lab1.transAccount.dao.impl.JcbTransAccountDaoImpl;
import org.aptech.t2303e.lab1.transAccount.exception.NotEnoughMoneyException;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class HybridTransAccount extends TransAccount{
    private static final String CARD_TYPE = "HYBRID";
    private static final double MIN_BALANCE = 50000;
    private static final double FEE_WITH_DRAW = 1000;

    @Override
    public double deposit(double depositNum) throws NotEnoughMoneyException{
        TransAccountDao hybridTransAccDao = new HybridTransAccountDaoImpl();
        BankAccountDao baDao = new BankAccountDaoImpl();

        if(hybridTransAccDao.findAmountWithTransIdMax(getCardNo()) == null && depositNum <= 0){
            throw new NotEnoughMoneyException("Can't make transaction with new card no : " + getCardNo() +
                    " and the with draw can't be the non-negative number : " + depositNum);
        }else if (hybridTransAccDao.findAmountWithTransIdMax(getCardNo()) == null &&
                !baDao.getCardTypeByCardNo(getCardNo()).equals("HYBRID")) {
            throw new NotEnoughMoneyException("Card no : " + getCardNo() + " is " +
                    baDao.getCardTypeByCardNo(getCardNo()) + " card type.");
        }else if (hybridTransAccDao.findAmountWithTransIdMax(getCardNo()) == null) {
            super.setAmount(depositNum);
            return getAmount();
        }
        super.setAmount(hybridTransAccDao.findAmountWithTransIdMax(getCardNo()).getAmount() + depositNum);
        return getAmount();
    }

    @Override
    public double withDraw(double withdrawNum) throws NotEnoughMoneyException {
        System.out.println("Withdraw from account " + getCardNo() + ", card type : " + CARD_TYPE);
        TransAccountDao hybridTransAccDao = new HybridTransAccountDaoImpl();
        BankAccountDao baDao = new BankAccountDaoImpl();

        if(hybridTransAccDao.findAmountWithTransIdMax(getCardNo()) == null && withdrawNum <= 0){
            throw new NotEnoughMoneyException("Can't make transaction with new card no : " + getCardNo() +
                    " and the with draw can't be the non-negative number : " + withdrawNum);
        } else if (hybridTransAccDao.findAmountWithTransIdMax(getCardNo()) == null &&
                !baDao.getCardTypeByCardNo(getCardNo()).equals("HYBRID")) {
            throw new NotEnoughMoneyException("Card no : " + getCardNo() + " is " +
                    baDao.getCardTypeByCardNo(getCardNo()) + " card type.");
        }else if (hybridTransAccDao.findAmountWithTransIdMax(getCardNo()) == null) {
            throw new NotEnoughMoneyException("Not enough money, card no : " + getCardNo() +
                    ", balance : " + getAmount() + ", with draw : " + withdrawNum +
                    ", time stamp : " + LocalDateTime.now());
        } else if (hybridTransAccDao.findAmountWithTransIdMax(getCardNo()).getAmount() - MIN_BALANCE - FEE_WITH_DRAW < withdrawNum) {
            throw new NotEnoughMoneyException("Not enough money, card no : " + getCardNo() +
                    ", balance : " + getAmount() + ", with draw : " + withdrawNum +
                    ", time stamp : " + LocalDateTime.now());
        }
        super.setAmount(hybridTransAccDao.findAmountWithTransIdMax(getCardNo()).getAmount() - withdrawNum - FEE_WITH_DRAW);
        return getAmount();
    }
}

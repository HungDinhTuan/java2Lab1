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
    public double deposit(double depositNum) {
        TransAccountDao hybridTransAccDao = new HybridTransAccountDaoImpl();
        BankAccountDao baDao = new BankAccountDaoImpl();

        if(hybridTransAccDao.findAmountWithTransIdMax(getCardNo()) == null && depositNum <= 0){
            System.err.println("Can't make transaction with new card no : " + getCardNo() +
                    " and the with draw can't be the non-negative number : " + depositNum);
            return hybridTransAccDao.findAmountWithTransIdMax(getCardNo()).deposit(depositNum);
        }else if (hybridTransAccDao.findAmountWithTransIdMax(getCardNo()) == null &&
                !baDao.getCardTypeByCardNo(getCardNo()).equals("HYBRID")) {
            System.err.println("Card no : " + getCardNo() + " is " +
                    baDao.getCardTypeByCardNo(getCardNo()) + " card type.");
            return hybridTransAccDao.findAmountWithTransIdMax(getCardNo()).deposit(depositNum);
        }else if (hybridTransAccDao.findAmountWithTransIdMax(getCardNo()) == null) {
            return depositNum;
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
            System.err.println("Can't make transaction with new card no : " + getCardNo() +
                    " and the with draw can't be the non-negative number : " + withdrawNum);
            return hybridTransAccDao.findAmountWithTransIdMax(getCardNo()).withDraw(withdrawNum);
        } else if (hybridTransAccDao.findAmountWithTransIdMax(getCardNo()) == null &&
                !baDao.getCardTypeByCardNo(getCardNo()).equals("JCB")) {
            System.err.println("Card no : " + getCardNo() + " is " +
                    baDao.getCardTypeByCardNo(getCardNo()) + " card type.");
            return hybridTransAccDao.findAmountWithTransIdMax(getCardNo()).withDraw(withdrawNum);
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

//    public static void main(String[] args) throws NotEnoughMoneyException {
//        HybridTransAccount hybridTransAccount = new HybridTransAccount();
//        hybridTransAccount.deposit(1500000);
//        hybridTransAccount.withDraw(1000000);
//        System.err.println(hybridTransAccount.getAmount());
//    }
}

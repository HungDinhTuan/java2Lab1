package org.aptech.t2303e.lab1.transAccount.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.aptech.t2303e.lab1.bankAccount.dao.BankAccountDao;
import org.aptech.t2303e.lab1.bankAccount.dao.impl.BankAccountDaoImpl;
import org.aptech.t2303e.lab1.transAccount.dao.TransAccountDao;
import org.aptech.t2303e.lab1.transAccount.dao.impl.JcbTransAccountDaoImpl;
import org.aptech.t2303e.lab1.transAccount.exception.NotEnoughMoneyException;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class JcbTransAccount extends TransAccount {
    private static final String CARD_TYPE = "JCB";
    private static final double MIN_BALANCE = 50000;
    private static final double FEE_WITH_DRAW = 500;

    @Override
    public double deposit(double depositNum) {
        TransAccountDao jcbTransAccDao = new JcbTransAccountDaoImpl();
        BankAccountDao baDao = new BankAccountDaoImpl();

        if(jcbTransAccDao.findAmountWithTransIdMax(getCardNo()) == null && depositNum <= 0){
            System.err.println("Can't make transaction with new card no : " + getCardNo() +
                    " and the with draw can't be the non-negative number : " + depositNum);
            return jcbTransAccDao.findAmountWithTransIdMax(getCardNo()).deposit(depositNum);
        }else if (jcbTransAccDao.findAmountWithTransIdMax(getCardNo()) == null &&
                !baDao.getCardTypeByCardNo(getCardNo()).equals("JCB")) {
            System.err.println("Card no : " + getCardNo() + " is " +
                    baDao.getCardTypeByCardNo(getCardNo()) + " card type.");
            return jcbTransAccDao.findAmountWithTransIdMax(getCardNo()).deposit(depositNum);
        }else if (jcbTransAccDao.findAmountWithTransIdMax(getCardNo()) == null) {
            return depositNum;
        }
        super.setAmount(jcbTransAccDao.findAmountWithTransIdMax(getCardNo()).getAmount() + depositNum);
        return getAmount();
    }

    @Override
    public double withDraw(double withdrawNum) throws NotEnoughMoneyException {
        System.out.println("Withdraw from account " + getCardNo() + ", card type : " + CARD_TYPE);
        TransAccountDao jcbTransAccDao = new JcbTransAccountDaoImpl();
        BankAccountDao baDao = new BankAccountDaoImpl();

        if(jcbTransAccDao.findAmountWithTransIdMax(getCardNo()) == null && withdrawNum <= 0){
            System.err.println("Can't make transaction with new card no : " + getCardNo() +
                    " and the with draw can't be the non-negative number : " + withdrawNum);
            return jcbTransAccDao.findAmountWithTransIdMax(getCardNo()).withDraw(withdrawNum);
        } else if (jcbTransAccDao.findAmountWithTransIdMax(getCardNo()) == null &&
                !baDao.getCardTypeByCardNo(getCardNo()).equals("JCB")) {
            System.err.println("Card no : " + getCardNo() + " is " +
                    baDao.getCardTypeByCardNo(getCardNo()) + " card type.");
            return jcbTransAccDao.findAmountWithTransIdMax(getCardNo()).withDraw(withdrawNum);
        }else if (jcbTransAccDao.findAmountWithTransIdMax(getCardNo()) == null) {
            throw new NotEnoughMoneyException("Not enough money, card no : " + getCardNo() +
                                                ", balance : " + getAmount() + ", with draw : " + withdrawNum +
                                                ", time stamp : " + LocalDateTime.now());
        } else if (jcbTransAccDao.findAmountWithTransIdMax(getCardNo()).getAmount() - MIN_BALANCE - FEE_WITH_DRAW < withdrawNum) {
            throw new NotEnoughMoneyException("Not enough money, card no : " + getCardNo() +
                    ", balance : " + getAmount() + ", with draw : " + withdrawNum +
                    ", time stamp : " + LocalDateTime.now());
        }
        super.setAmount(jcbTransAccDao.findAmountWithTransIdMax(getCardNo()).getAmount() - withdrawNum - FEE_WITH_DRAW);
        return getAmount();
    }

//    public static void main(String[] args) throws NotEnoughMoneyException {
//        JcbTransAccount jcbTransAccount = new JcbTransAccount();
//        jcbTransAccount.deposit(200000);
//        jcbTransAccount.withDraw(100000);
//        System.err.println(jcbTransAccount.getAmount());
//    }
}

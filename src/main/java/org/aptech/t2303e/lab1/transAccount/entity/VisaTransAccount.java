package org.aptech.t2303e.lab1.transAccount.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.aptech.t2303e.lab1.bankAccount.dao.BankAccountDao;
import org.aptech.t2303e.lab1.bankAccount.dao.impl.BankAccountDaoImpl;
import org.aptech.t2303e.lab1.transAccount.dao.TransAccountDao;
import org.aptech.t2303e.lab1.transAccount.dao.impl.HybridTransAccountDaoImpl;
import org.aptech.t2303e.lab1.transAccount.dao.impl.VisaTransAccountDaoImpl;
import org.aptech.t2303e.lab1.transAccount.exception.NotEnoughMoneyException;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class VisaTransAccount extends TransAccount{
    private static final String CARD_TYPE = "VISA";
    private static final double CREDIT_LIMIT = 10000000;

    @Override
    public double deposit(double depositNum) throws NotEnoughMoneyException{
        TransAccountDao visaTransAccDao = new VisaTransAccountDaoImpl();
        BankAccountDao baDao = new BankAccountDaoImpl();

        if(visaTransAccDao.findAmountWithTransIdMax(getCardNo()) == null && depositNum <= 0){
            throw new NotEnoughMoneyException("Can't make transaction with new card no : " + getCardNo() +
                    " and the with draw can't be the non-negative number : " + depositNum);
        }else if (visaTransAccDao.findAmountWithTransIdMax(getCardNo()) == null &&
                !baDao.getCardTypeByCardNo(getCardNo()).equals("VISA")) {
            throw new NotEnoughMoneyException("Card no : " + getCardNo() + " is " +
                    baDao.getCardTypeByCardNo(getCardNo()) + " card type.");
        }else if (visaTransAccDao.findAmountWithTransIdMax(getCardNo()) == null) {
            super.setAmount(depositNum);
            return getAmount();
        }
        super.setAmount(visaTransAccDao.findAmountWithTransIdMax(getCardNo()).getAmount() + depositNum);
        return getAmount();
    }

    @Override
    public double withDraw(double withdrawNum) throws NotEnoughMoneyException {
        System.out.println("With draw from account : " + getCardNo() + ", card type : " + CARD_TYPE);
        TransAccountDao visaTransAccDao = new VisaTransAccountDaoImpl();
        BankAccountDao baDao = new BankAccountDaoImpl();
        List<VisaTransAccount> visaTransAccs = VisaTransAccountDaoImpl.listVisaTransByCardNo(getCardNo());

        LocalDateTime cardActiveDate = visaTransAccs.get(0).getTransDateTime();
        LocalDateTime maxIndexDate = visaTransAccs.get(visaTransAccs.size()).getTransDateTime();

        int day = cardActiveDate.getDayOfMonth();
        int month = maxIndexDate.getMonthValue();
        int year = maxIndexDate.getYear();

        if(visaTransAccDao.findAmountWithTransIdMax(getCardNo()) == null && withdrawNum <= 0){
            throw new NotEnoughMoneyException("Can't make transaction with new card no : " + getCardNo() +
                    " and the with draw can't be the non-negative number : " + withdrawNum);
        } else if (visaTransAccDao.findAmountWithTransIdMax(getCardNo()) == null &&
                !baDao.getCardTypeByCardNo(getCardNo()).equals("VISA")) {
            throw new NotEnoughMoneyException("Card no : " + getCardNo() + " is " +
                    baDao.getCardTypeByCardNo(getCardNo()) + " card type.");
        }else if (visaTransAccDao.findAmountWithTransIdMax(getCardNo()) == null) {
            throw new NotEnoughMoneyException("Not enough money, card no : " + getCardNo() +
                    ", balance : " + getAmount() + ", with draw : " + withdrawNum +
                    ", time stamp : " + LocalDateTime.now());
        } else if (visaTransAccDao.findAmountWithTransIdMax(getCardNo()).getAmount() + CREDIT_LIMIT < withdrawNum) {
            throw new NotEnoughMoneyException("Not enough money..." );
        }
        super.setAmount(visaTransAccDao.findAmountWithTransIdMax(getCardNo()).getAmount() - withdrawNum);
        return getAmount();
    }
}

package org.aptech.t2303e.lab1.transAccount.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.aptech.t2303e.lab1.bankAccount.dao.BankAccountDao;
import org.aptech.t2303e.lab1.bankAccount.dao.impl.BankAccountDaoImpl;
import org.aptech.t2303e.lab1.transAccount.dao.TransAccountDao;
import org.aptech.t2303e.lab1.transAccount.dao.impl.JcbTransAccountDaoImpl;
import org.aptech.t2303e.lab1.transAccount.exception.NotEnoughMoneyException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class JcbTransAccount extends TransAccount {
    private static final String CARD_TYPE = "JCB";
    private static final double MIN_BALANCE = 50000;
    private static final double FEE_WITH_DRAW = 500;

    public static JcbTransAccount convert(DateTimeFormatter dateTimeFormatter, String header, String line) {
        if (StringUtils.isEmpty(line)) return null;
        if (line.trim().equalsIgnoreCase(header)) return null;
        String[] chars = line.split("\\|");
        String transDateTimeString = chars[3].replace("T", " ");
        LocalDateTime transDateTime = LocalDateTime.parse(transDateTimeString, dateTimeFormatter);
        return JcbTransAccount.builder()
                .transId(Long.valueOf(chars[0]))
                .cardNo(chars[1])
                .amount(Double.valueOf(chars[2]))
                .transDateTime(transDateTime)
                .build();
    }

    @Override
    public double deposit(double depositNum) throws NotEnoughMoneyException{
        TransAccountDao jcbTransAccDao = new JcbTransAccountDaoImpl();
        BankAccountDao baDao = new BankAccountDaoImpl();

        if(jcbTransAccDao.findAmountWithTransIdMax(getCardNo()) == null && depositNum <= 0){
            throw new NotEnoughMoneyException("Can't make transaction with new card no : " + getCardNo() +
                    " and the with draw can't be the non-negative number : " + depositNum);
        }else if (jcbTransAccDao.findAmountWithTransIdMax(getCardNo()) == null &&
                !baDao.getCardTypeByCardNo(getCardNo()).equals("JCB")) {
            throw new NotEnoughMoneyException("Card no : " + getCardNo() + " is " +
                    baDao.getCardTypeByCardNo(getCardNo()) + " card type.");
        }else if (jcbTransAccDao.findAmountWithTransIdMax(getCardNo()) == null) {
            super.setAmount(depositNum);
            return getAmount();
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
            throw new NotEnoughMoneyException("Can't make transaction with new card no : " + getCardNo() +
                    " and the with draw can't be the non-negative number : " + withdrawNum);
        } else if (jcbTransAccDao.findAmountWithTransIdMax(getCardNo()) == null &&
                !baDao.getCardTypeByCardNo(getCardNo()).equals("JCB")) {
            throw new NotEnoughMoneyException("Card no : " + getCardNo() + " is " +
                    baDao.getCardTypeByCardNo(getCardNo()) + " card type.");
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
}

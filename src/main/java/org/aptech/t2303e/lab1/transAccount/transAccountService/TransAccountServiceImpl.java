package org.aptech.t2303e.lab1.transAccount.transAccountService;

import org.aptech.t2303e.lab1.transAccount.*;
import org.aptech.t2303e.lab1.transAccount.exceptionTransAcc.NotEnoughMoneyException;

import java.time.LocalDateTime;

public class TransAccountServiceImpl implements TransAccountService{

    @Override
    public double changeAmount(TransAccount transAccount ,double deposit, double withDraw) throws NotEnoughMoneyException {

        TransAccountValid taValid = new TransAccountValid();
        JcbTransAccount jcbTransAcc = new JcbTransAccount();
        HybridTransAccount hybridTransAcc = new HybridTransAccount();
        VisaTransAccount visaTransAcc = new VisaTransAccount();

        transAccount.setAmount(transAccount.deposit(deposit));

        if(jcbTransAcc.CARD_TYPE.equals(taValid.validCardNoInSystem(jcbTransAcc))){
            transAccount.setAmount(jcbTransAcc.withDraw(withDraw));
        } else if (hybridTransAcc.CARD_TYPE.equals(taValid.validCardNoInSystem(hybridTransAcc))) {
            transAccount.setAmount(hybridTransAcc.withDraw(withDraw));
        } else if (visaTransAcc.CARD_TYPE.equals(taValid.validCardNoInSystem(visaTransAcc))) {
            transAccount.setAmount(visaTransAcc.withDraw(withDraw));
        }
        return transAccount.getAmount();
    }

    public static void main(String[] args) throws NotEnoughMoneyException {
        TransAccountService transAccountService = new TransAccountServiceImpl();
        JcbTransAccount jcbTransAcc =  JcbTransAccount.builder()
                .cardNo("123456789123")
                .amount(0)
                .transDateTime(LocalDateTime.now())
                .build();
        System.err.println(transAccountService.changeAmount(jcbTransAcc, 1000000, 100000));;
    }
}

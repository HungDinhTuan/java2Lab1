package org.aptech.t2303e.lab1.transAccount;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.aptech.t2303e.lab1.transAccount.exceptionTransAcc.NotEnoughMoneyException;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class VisaTransAccount extends TransAccount{
    public final String CARD_TYPE = "VISA";
    private static final double CREDIT_LIMIT = 10000000;

    @Override
    public double withDraw(double num) throws NotEnoughMoneyException {
        System.out.println("With draw from account : " + getCardNo() + ", card type : " + CARD_TYPE);

        if(getAmount() + CREDIT_LIMIT  < num){
            throw new NotEnoughMoneyException("The account has maxed out its credit limit, account number : " + getCardNo()
                    + ", balance : " + (getAmount() + CREDIT_LIMIT) + "with draw : " + num +
                    ", time stamp : " + LocalDateTime.now());
        }
        super.setAmount(getAmount() - num);
        return getAmount();
    }

    public static void main(String[] args) throws NotEnoughMoneyException {
        VisaTransAccount visaTransAccount = new VisaTransAccount();
        visaTransAccount.withDraw(50000000);
        System.err.println(visaTransAccount.getAmount());
    }
}

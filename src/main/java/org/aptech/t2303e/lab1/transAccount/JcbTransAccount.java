package org.aptech.t2303e.lab1.transAccount;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.aptech.t2303e.lab1.transAccount.exceptionTransAcc.NotEnoughMoneyException;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class JcbTransAccount extends TransAccount {

    public final String CARD_TYPE = "JCB";
    private static final double  MIN_BALANCE = 50000;

    @Override
    public double withDraw(double num) throws NotEnoughMoneyException {
        System.out.println("Withdraw from account " + getCardNo() + ", card type : " + CARD_TYPE);

        if(getAmount() - MIN_BALANCE - 500 < num){
            throw new NotEnoughMoneyException("Not enough money, account number : " + getCardNo()
                                            + ", balance : " + getAmount() + "with draw : " + num +
                                            ", time stamp : " + LocalDateTime.now());
        }
        super.setAmount(getAmount() - num - 500);
        return getAmount();
    }

    public static void main(String[] args) throws NotEnoughMoneyException {
        JcbTransAccount jcbTransAccount = new JcbTransAccount();
        jcbTransAccount.deposit(1000000);
        jcbTransAccount.withDraw(100000);
        System.err.println(jcbTransAccount.getAmount());
    }
}

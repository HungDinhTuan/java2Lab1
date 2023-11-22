package org.aptech.t2303e.lab1.transAccount;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.aptech.t2303e.lab1.transAccount.exceptionTransAcc.NotEnoughMoneyException;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class HybridTransAccount extends TransAccount{
    public final String CARD_TYPE = "HYBRID";
    private static final double MIN_BALANCE = 50000;

    @Override
    public double withDraw(double num) throws NotEnoughMoneyException {
        System.out.println("With draw from account : " + getCardNo() + ", card type : " + CARD_TYPE);

        if(getAmount() - MIN_BALANCE - 1000 < num){
            throw new NotEnoughMoneyException("Not enough money, account number : " + getCardNo()
                                            + ", balance : " + getAmount() + "with draw : " + num +
                                            ", time stamp : " + LocalDateTime.now());
        }
        super.setAmount(getAmount() - num - 1000);
        return getAmount();
    }

    public static void main(String[] args) throws NotEnoughMoneyException {
        HybridTransAccount hybridTransAccount = new HybridTransAccount();
        hybridTransAccount.deposit(1500000);
        hybridTransAccount.withDraw(1000000);
        System.err.println(hybridTransAccount.getAmount());
    }
}

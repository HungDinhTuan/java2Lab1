package org.aptech.t2303e.lab1.transAccount;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.aptech.t2303e.lab1.transAccount.exceptionTransAcc.NotEnoughMoneyException;

import java.time.LocalDateTime;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public abstract class TransAccount {
    private long transId;
    private String cardNo;
    private double amount;
    private LocalDateTime transDateTime;

    public double deposit(double num){
        this.amount += num;
        return getAmount();
    }

    public abstract double withDraw(double num) throws NotEnoughMoneyException;
}

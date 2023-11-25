package org.aptech.t2303e.lab1.transAccount.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.aptech.t2303e.lab1.transAccount.exception.NotEnoughMoneyException;

import java.time.LocalDateTime;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public abstract class TransAccount {
    private long transId;
    private String cardNo;
    private double amount = 0;
    private LocalDateTime transDateTime;

    public abstract double deposit(double depositNum);

    public abstract double withDraw(double num) throws NotEnoughMoneyException;
}

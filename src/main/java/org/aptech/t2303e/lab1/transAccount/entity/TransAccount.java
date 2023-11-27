package org.aptech.t2303e.lab1.transAccount.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.aptech.t2303e.lab1.transAccount.exception.NotEnoughMoneyException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public abstract class TransAccount {
    private long transId;
    private String cardNo;
    private double amount = 0;
    private LocalDateTime transDateTime;



    public abstract double deposit(double depositNum) throws NotEnoughMoneyException;

    public abstract double withDraw(double num) throws NotEnoughMoneyException;
}

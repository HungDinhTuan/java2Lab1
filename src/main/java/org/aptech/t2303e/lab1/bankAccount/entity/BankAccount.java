package org.aptech.t2303e.lab1.bankAccount.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@ToString(exclude = {"year", "month", "day"})
public class BankAccount {
    private long id;
    private String cardType;
    private String name;
    private String cardNo;
    private String idCard;
    private String tel;
    private String address;
    private Date dateOfBirth;
    private int year;
    private int month;
    private int day;
}

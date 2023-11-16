package org.aptech.t2303e.lab1;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BankAccount {
    private long id;
    private String cardType;
    private String name;
    private String cardNo;
    private String idCard;
    private String tel;
    private String address;
    private Date dateOfBirth;
}

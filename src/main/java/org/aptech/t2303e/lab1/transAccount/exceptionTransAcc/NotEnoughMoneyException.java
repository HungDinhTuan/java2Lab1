package org.aptech.t2303e.lab1.transAccount.exceptionTransAcc;

public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}

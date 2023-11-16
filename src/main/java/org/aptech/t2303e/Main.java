package org.aptech.t2303e;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File(".data/BankAccount.txt");
        System.err.println("File exits : " + file.exists());
    }
}
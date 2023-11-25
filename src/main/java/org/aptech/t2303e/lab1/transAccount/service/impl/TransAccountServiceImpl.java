package org.aptech.t2303e.lab1.transAccount.service.impl;

import org.aptech.t2303e.lab1.transAccount.entity.TransAccount;
import org.aptech.t2303e.lab1.transAccount.service.TransAccountService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TransAccountServiceImpl implements TransAccountService {
    @Override
    public void insertFile(List<TransAccount> transAccounts, String fileName) {
        try{
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new FileWriter(fileName, true)
            ));
            for(TransAccount transAcc : transAccounts){
                StringBuilder sb = new StringBuilder();
                sb.append(transAcc.getTransId()).append("|")
                        .append(transAcc.getCardNo()).append("|")
                        .append(transAcc.getAmount()).append("|")
                        .append(transAcc.getTransDateTime());
                out.println(sb.toString());
            }
            out.close();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}

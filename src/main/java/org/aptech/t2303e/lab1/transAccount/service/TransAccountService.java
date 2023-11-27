package org.aptech.t2303e.lab1.transAccount.service;

import org.aptech.t2303e.lab1.transAccount.entity.TransAccount;

import java.io.FileNotFoundException;
import java.util.List;

public interface TransAccountService {
    void insertFile(List<TransAccount> transAccounts, String fileName);
    List<TransAccount> readFile(String url) throws FileNotFoundException;
}

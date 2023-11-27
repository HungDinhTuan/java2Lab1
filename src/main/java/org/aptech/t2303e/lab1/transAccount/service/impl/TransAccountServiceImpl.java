package org.aptech.t2303e.lab1.transAccount.service.impl;

import org.aptech.t2303e.lab1.transAccount.entity.JcbTransAccount;
import org.aptech.t2303e.lab1.transAccount.entity.TransAccount;
import org.aptech.t2303e.lab1.transAccount.service.TransAccountService;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class TransAccountServiceImpl implements TransAccountService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String HEADER = "Trans_id|Card_no|Amount|Trans_date_time";

    @Override
    public void insertFile(List<TransAccount> transAccounts, String fileName) {
        try{
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new FileWriter(fileName, true)
            ));
            StringBuilder sbHeader = new StringBuilder();
            sbHeader.append(HEADER);
            out.println(sbHeader.toString());
            for(TransAccount transAcc : transAccounts){
                StringBuilder sb = new StringBuilder();
                sb.append(transAcc.getTransId()).append("|")
                        .append(transAcc.getCardNo()).append("|")
                        .append(transAcc.getAmount()).append("|")
                        .append(transAcc.getTransDateTime());
                out.println(sb.toString());
            }
            out.close();
            System.err.println("Insert file success in : " + fileName);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TransAccount> readFile(String url) {
        List<TransAccount> transAccs = new ArrayList<>();
        FileInputStream inputStream = null;
        Scanner scanner = null;
        try {
            inputStream = new FileInputStream(url);
            scanner =new Scanner(inputStream);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                JcbTransAccount transAcc = JcbTransAccount.convert(DATE_TIME_FORMATTER, HEADER, line);
                if(!Objects.isNull(transAcc)) transAccs.add(transAcc);
            }
        }catch (FileNotFoundException e) {
            System.err.println("File not found : "+e.getMessage());
        }catch (DateTimeParseException e){
            System.err.println("Parse student error : "+e.getMessage());
        }finally {
            scanner.close();
            try{
               inputStream.close();
            }catch (IOException e) {
                System.err.println("IOE exception : "+e.getMessage());
            }
        }
        transAccs.forEach(System.err::println);
        return transAccs;
    }

    public static void main(String[] args) throws FileNotFoundException {
        TransAccountService transAccountService = new TransAccountServiceImpl();
        transAccountService.readFile("./etc/trans_account_table.txt");
    }
}

package org.aptech.t2303e.lab1.transAccount.dao.impl;

import org.aptech.t2303e.config.properties.Datasource;
import org.aptech.t2303e.lab1.bankAccount.BankAccountValid;
import org.aptech.t2303e.lab1.bankAccount.dao.BankAccountDao;
import org.aptech.t2303e.lab1.bankAccount.dao.impl.BankAccountDaoImpl;
import org.aptech.t2303e.lab1.transAccount.TransAccountValid;
import org.aptech.t2303e.lab1.transAccount.dao.TransAccountDao;
import org.aptech.t2303e.lab1.transAccount.entity.JcbTransAccount;
import org.aptech.t2303e.lab1.transAccount.entity.TransAccount;
import org.aptech.t2303e.lab1.transAccount.exception.NotEnoughMoneyException;
import org.aptech.t2303e.lab1.transAccount.service.TransAccountService;
import org.aptech.t2303e.lab1.transAccount.service.impl.TransAccountServiceImpl;

import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JcbTransAccountDaoImpl implements TransAccountDao {

    public static void main(String[] args) throws NotEnoughMoneyException, FileNotFoundException {
        TransAccountDao jcbTransAccountDao = new JcbTransAccountDaoImpl();
        TransAccountService taService = new TransAccountServiceImpl();
//        System.err.println(jcbTransAccountDao.findAmountWithTransIdMax("123456789123"));
//        JcbTransAccount jcbTransAcc = JcbTransAccount.builder()
//                .cardNo("123456789123")
//                .build();
//        boolean result = jcbTransAccountDao.insertTransAccountDeposit(jcbTransAcc, 150000);
//        boolean result = jcbTransAccountDao.insertTransAccountWithDraw(jcbTransAcc, 150000);
//        if(result) System.err.println("Insert success.");
//        String cardType = "JCB";
//        String fileName = "./etc/" + cardType + "_trans_account_table.txt";
//        List<TransAccount> jcbTransAccs = jcbTransAccountDao.findAll("JCB");
//        taService.insertFile(jcbTransAccs, fileName);
//        taService.readFile("./etc/trans_account_table.txt");
//        String fileName = "./etc/trans_account_table.txt";
//        List<TransAccount> jcbTransAccs = jcbTransAccountDao.findAll();
//        taService.insertFile(jcbTransAccs, fileName);
        jcbTransAccountDao.insertTransAccountFromFile("./etc/trans_account_table.txt");
    }

    @Override
    public boolean insertTransAccountDeposit(TransAccount transAccount, double depositNum){
        BankAccountValid baValid = new BankAccountValid();
        TransAccountValid taValid = new TransAccountValid();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        if(!baValid.getValidNumCard(transAccount.getCardNo())) return false;
        if(taValid.validCardNoInSystem(transAccount.getCardNo()) == null){
            System.err.println("Card no : "+ transAccount.getCardNo() + "doesn't exist in the system.");
            return false;
        }

        PreparedStatement preSt;
        String sql = "Insert into trans_account_table(card_no, amount, trans_date_time)" +
                "values(?, ?, ?)";
        Connection conn = Datasource.getConn();
        try{
            preSt = conn.prepareStatement(sql);
            preSt.setString(1, transAccount.getCardNo());
            preSt.setDouble(2, transAccount.deposit(depositNum));
            // covert LocalDateTime to Timestamp for database insert
            preSt.setTimestamp(3, timestamp);
            preSt.execute();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertTransAccountWithDraw(TransAccount transAccount, double withdrawNum){
        BankAccountValid baValid = new BankAccountValid();
        TransAccountValid taValid = new TransAccountValid();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        if(!baValid.getValidNumCard(transAccount.getCardNo())) return false;
        if(taValid.validCardNoInSystem(transAccount.getCardNo()) == null){
            System.err.println("Card no : "+ transAccount.getCardNo() + "doesn't exist in the system.");
            return false;
        }

        PreparedStatement preSt;
        String sql = "Insert into trans_account_table(card_no, amount, trans_date_time)" +
                "values(?, ?, ?)";
        Connection conn = Datasource.getConn();
        try{
            preSt = conn.prepareStatement(sql);
            preSt.setString(1, transAccount.getCardNo());
            preSt.setDouble(2, transAccount.withDraw(withdrawNum));
            // covert LocalDateTime to Timestamp for database insert
            preSt.setTimestamp(3, timestamp);
            preSt.execute();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertTransAccountFromFile(String url) throws FileNotFoundException {
        TransAccountService taService = new TransAccountServiceImpl();
        List<TransAccount> transAccounts = taService.readFile(url);
        TransAccountValid taValid = new TransAccountValid();

        for (int i = 0; i < transAccounts.size(); i++) {
            if(taValid.validCardNoInSystem(transAccounts.get(i).getCardNo()) == null){
                System.err.println("Card no : " + transAccounts.get(i).getCardNo() + " doesn't exist in the system.");
                continue;
            }
            PreparedStatement preSt;
            String sql = "Insert into trans_account_table2(card_no, amount, trans_date_time)" +
                    "values(?, ?, ?)";
            Connection conn = Datasource.getConn();
            try{
                preSt = conn.prepareStatement(sql);
                preSt.setString(1, transAccounts.get(i).getCardNo());
                preSt.setDouble(2, transAccounts.get(i).getAmount());
                Timestamp timestamp = Timestamp.valueOf(transAccounts.get(i).getTransDateTime());
                preSt.setTimestamp(3, timestamp);
                preSt.execute();
                return true;
            }catch (SQLException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }

    @Override
    public TransAccount findAmountWithTransIdMax(String cardNo) {
        BankAccountDao baDao = new BankAccountDaoImpl();
        if(!baDao.getCardTypeByCardNo(cardNo).equals("JCB")){
            System.err.println("Card no : " + cardNo + " is " + baDao.getCardTypeByCardNo(cardNo) + " card type.");
            return null;
        }
        PreparedStatement preSt;
        String sql = "Select * from trans_account_table where" +
                " trans_id = (Select MAX(trans_id) from trans_account_table where card_no = ?)";
        List<JcbTransAccount> jcbTransAccs = new ArrayList<>();
        Connection conn = Datasource.getConn();
        try {
            preSt = conn.prepareStatement(sql);
            preSt.setString(1, cardNo);
            ResultSet rs = preSt.executeQuery();
            while (rs.next()){
                JcbTransAccount jcAcc = rowMapper(rs);
                if(!Objects.isNull(jcAcc)) jcbTransAccs.add(jcAcc);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (jcbTransAccs != null && jcbTransAccs.size() >0){
            return jcbTransAccs.get(0);
        }
        return null;
    }

    @Override
    public List<TransAccount> findAllByCardType(String cardType) {
        BankAccountValid baValid = new BankAccountValid();
        if(baValid.validCardType(cardType) == null) return null;
        PreparedStatement preSt;
        String sql = "Select trans_account_table.trans_id, trans_account_table.card_no, trans_account_table.amount, trans_account_table.trans_date_time, trans_account_table.card_no\n" +
                "from trans_account_table INNER JOIN bank_account_table ON trans_account_table.card_no=bank_account_table.card_no\n" +
                "WHERE bank_account_table.card_type = ?";
        List<TransAccount> jcbTransAccounts = new ArrayList<>();
        Connection conn = Datasource.getConn();
        try{
            preSt = conn.prepareStatement(sql);
            preSt.setString(1, cardType);
            ResultSet rs = preSt.executeQuery();
            while (rs.next()){
                TransAccount acc = rowMapper(rs);
                if (!Objects.isNull(acc)) jcbTransAccounts.add(acc);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return jcbTransAccounts;
    }

    @Override
    public List<TransAccount> findAll() {
        BankAccountValid baValid = new BankAccountValid();
        PreparedStatement preSt;
        String sql = "Select * from trans_account_table";
        List<TransAccount> jcbTransAccounts = new ArrayList<>();
        Connection conn = Datasource.getConn();
        try{
            preSt = conn.prepareStatement(sql);
            ResultSet rs = preSt.executeQuery();
            while (rs.next()){
                TransAccount acc = rowMapper(rs);
                if (!Objects.isNull(acc)) jcbTransAccounts.add(acc);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return jcbTransAccounts;
    }

    public static JcbTransAccount rowMapper(ResultSet rs) {
        JcbTransAccount jcbTransAcc = null;
        long transId = 0;
        try {
            transId = rs.getLong("trans_id");
            String cardNo = rs.getString("card_no");
            double amount = rs.getDouble("amount");
            // covert timestamp to LocalDateTime
            java.sql.Timestamp timestamp = rs.getTimestamp("trans_date_time");
            LocalDateTime transDateTime = timestamp.toLocalDateTime();
            jcbTransAcc = JcbTransAccount.builder()
                    .transId(transId)
                    .cardNo(cardNo)
                    .amount(amount)
                    .transDateTime(transDateTime)
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return jcbTransAcc;
    }
}

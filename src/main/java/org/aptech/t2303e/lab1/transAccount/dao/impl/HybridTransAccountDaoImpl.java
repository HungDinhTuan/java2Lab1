package org.aptech.t2303e.lab1.transAccount.dao.impl;

import org.aptech.t2303e.config.properties.Datasource;
import org.aptech.t2303e.lab1.bankAccount.BankAccountValid;
import org.aptech.t2303e.lab1.bankAccount.dao.BankAccountDao;
import org.aptech.t2303e.lab1.bankAccount.dao.impl.BankAccountDaoImpl;
import org.aptech.t2303e.lab1.transAccount.TransAccountValid;
import org.aptech.t2303e.lab1.transAccount.dao.TransAccountDao;
import org.aptech.t2303e.lab1.transAccount.entity.HybridTransAccount;
import org.aptech.t2303e.lab1.transAccount.entity.TransAccount;
import org.aptech.t2303e.lab1.transAccount.exception.NotEnoughMoneyException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HybridTransAccountDaoImpl implements TransAccountDao {

    public static void main(String[] args) throws NotEnoughMoneyException {
        TransAccountDao hybridTransAccDao = new HybridTransAccountDaoImpl();
        HybridTransAccount hybridTransAcc = HybridTransAccount.builder()
                .cardNo("123456789124")
                .build();
        boolean result1 = hybridTransAccDao.insertTransAccountDeposit(hybridTransAcc, 1000000);
        if(result1)  System.err.println("Insert success.");
        boolean result2 = hybridTransAccDao.insertTransAccountWithDraw(hybridTransAcc, 500000);
        if(result2)  System.err.println("Insert success.");
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
    public boolean insertTransAccountFromFile(String url) {
        return false;
    }

    @Override
    public HybridTransAccount findAmountWithTransIdMax(String cardNo) {
        BankAccountDao baDao = new BankAccountDaoImpl();
        if(!baDao.getCardTypeByCardNo(cardNo).equals("HYBRID")){
            System.err.println("Card no : " + cardNo + " is " + baDao.getCardTypeByCardNo(cardNo) + " card type.");
            return null;
        }
        PreparedStatement preSt;
        String sql = "Select * from trans_account_table where" +
                " trans_id = (Select MAX(trans_id) from trans_account_table where card_no = ?)";
        List<HybridTransAccount> hybridTransAccs = new ArrayList<>();
        Connection conn = Datasource.getConn();
        try {
            preSt = conn.prepareStatement(sql);
            preSt.setString(1, cardNo);
            ResultSet rs = preSt.executeQuery();
            while (rs.next()){
                HybridTransAccount htAcc = rowMapper(rs);
                if(!Objects.isNull(htAcc)) hybridTransAccs.add(htAcc);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (hybridTransAccs != null && hybridTransAccs.size() >0){
            return hybridTransAccs.get(0);
        }
        return null;
    }

    @Override
    public List<TransAccount> findAllByCardType(String cardType) {
        BankAccountValid baValid = new BankAccountValid();
        if(baValid.validCardType(cardType) == null) return null;
        PreparedStatement preSt;
        String sql = "Select trans_account_table.trans_id, trans_account_table.card_no, trans_account_table.amount, " +
                "trans_account_table.trans_date_time, trans_account_table.card_no " +
                "from trans_account_table INNER JOIN bank_account_table " +
                "ON trans_account_table.card_no=bank_account_table.card_no " +
                "WHERE bank_account_table.card_type = ?";
        List<TransAccount> hybridTransAccounts = new ArrayList<>();
        Connection conn = Datasource.getConn();
        try{
            preSt = conn.prepareStatement(sql);
            preSt.setString(1, cardType);
            ResultSet rs = preSt.executeQuery();
            while (rs.next()){
                TransAccount acc = rowMapper(rs);
                if (!Objects.isNull(acc)) hybridTransAccounts.add(acc);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return hybridTransAccounts;
    }

    @Override
    public List<TransAccount> findAll() {
        return null;
    }

    public static HybridTransAccount rowMapper(ResultSet rs){
        HybridTransAccount hybridTransAcc = null;
        long transId = 0;
        try{
            transId = rs.getLong("trans_id");
            String cardNo = rs.getString("card_no");
            double amount = rs.getDouble("amount");
            // covert timestamp to LocalDateTime
            java.sql.Timestamp timestamp = rs.getTimestamp("trans_date_time");
            LocalDateTime transDateTime = timestamp.toLocalDateTime();
            hybridTransAcc = HybridTransAccount.builder()
                    .transId(transId)
                    .cardNo(cardNo)
                    .amount(amount)
                    .transDateTime(transDateTime)
                    .build();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return hybridTransAcc;
    }
}

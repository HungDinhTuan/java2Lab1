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

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JcbTransAccountDaoImpl implements TransAccountDao {

    public static void main(String[] args) throws NotEnoughMoneyException {
        TransAccountDao jcbTransAccountDao = new JcbTransAccountDaoImpl();
//        System.err.println(jcbTransAccountDao.findAmountWithTransIdMax("123456789123"));
        JcbTransAccount jcbTransAcc = JcbTransAccount.builder()
                .cardNo("123456789123")
                .build();
//        boolean result = jcbTransAccountDao.insertTransAccountDeposit(jcbTransAcc, 150000);
        boolean result = jcbTransAccountDao.insertTransAccountWithDraw(jcbTransAcc, 150000);
        if(result) System.err.println("Insert success.");
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

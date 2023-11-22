package org.aptech.t2303e.lab1.transAccount.transAccountDao;

import org.aptech.t2303e.config.properties.Datasource;
import org.aptech.t2303e.lab1.bankAccount.BankAccountValid;
import org.aptech.t2303e.lab1.transAccount.JcbTransAccount;
import org.aptech.t2303e.lab1.transAccount.TransAccount;
import org.aptech.t2303e.lab1.transAccount.TransAccountValid;
import org.aptech.t2303e.lab1.transAccount.exceptionTransAcc.NotEnoughMoneyException;
import org.aptech.t2303e.lab1.transAccount.transAccountService.TransAccountService;
import org.aptech.t2303e.lab1.transAccount.transAccountService.TransAccountServiceImpl;

import java.sql.*;
import java.time.LocalDateTime;

public class TransAccountDaoImpl implements TransAccountDao{

    public static void main(String[] args) throws NotEnoughMoneyException {
        TransAccountDao transAcc = new TransAccountDaoImpl();
        JcbTransAccount jcbTransAcc =  JcbTransAccount.builder()
                        .cardNo("123456789123")
                        .amount(0)
                        .transDateTime(LocalDateTime.now())
                        .build();
        boolean result = transAcc.insertTransAccount(jcbTransAcc, 0, 1000000);
        if(result) System.err.println("Insert success.");
    }

    @Override
    public boolean insertTransAccount(TransAccount transAccount, double deposit, double withDraw) throws NotEnoughMoneyException {

        BankAccountValid baValid = new BankAccountValid();
        TransAccountValid taValid = new TransAccountValid();
        TransAccountService transAccService = new TransAccountServiceImpl();

        if( !baValid.getValidNumCard(transAccount.getCardNo())) return false;
        if( taValid.validCardNoInSystem(transAccount) == null){
            System.err.println("Card no doesn't exist in the system.");
            return false;
        }

        transAccService.changeAmount(transAccount, deposit, withDraw);

        PreparedStatement preSt;
        String sql = "Insert into trans_account_table(card_no, amount, trans_date_time)" +
                "values(?, ?, ?)";
        Connection conn = Datasource.getConn();
        try {
            preSt = conn.prepareStatement(sql);
            preSt.setString(1, transAccount.getCardNo());
            preSt.setDouble(2, transAccount.getAmount());
            // convert LocalDateTime to Timestamp for database insert
            Timestamp timestamp= Timestamp.valueOf(transAccount.getTransDateTime());
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

    public static TransAccount rowMapper(ResultSet rs){
        TransAccount transAccount = null;
        long transId = 0;
        try{
            transId = rs.getLong("trans_id");
            String cardNo = rs.getString("card_no");
            double amount = rs.getDouble("amount");
            // covert timestamp to LocalDateTime
            java.sql.Timestamp timestamp = rs.getTimestamp("trans_date_time");
            LocalDateTime transDateTime = timestamp.toLocalDateTime();
            transAccount = TransAccount.builder()
                    .transId(transId)
                    .cardNo(cardNo)
                    .amount(amount)
                    .transDateTime(transDateTime)
                    .build();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return transAccount;
    }
}

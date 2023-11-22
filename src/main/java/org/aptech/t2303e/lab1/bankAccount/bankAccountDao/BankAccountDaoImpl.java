package org.aptech.t2303e.lab1.bankAccount.bankAccountDao;

import org.aptech.t2303e.config.properties.Datasource;
import org.aptech.t2303e.lab1.bankAccount.BankAccount;
import org.aptech.t2303e.lab1.bankAccount.BankAccountValid;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BankAccountDaoImpl implements BankAccountDao{

    public static void main(String[] args) {
        
        BankAccountDao bankAcc = new BankAccountDaoImpl();
        bankAcc.getBankAccByIdCard("123456789123");
        boolean result = bankAcc.insertBankAcc(BankAccount.builder()
                .cardType("VISA")
                .name("Le Tuan Ha")
                .cardNo("123456789119")
                .idCard("123456789122")
                .tel("0123456788")
                .address("ha noi")
                .year(1993)
                .month(8)
                .day(16)
                .build());
        if(result) System.err.println("Insert success.");

//        System.err.println(bankAcc.getBankAccByIdCard("123456789122"));
    }

    @Override
    public boolean insertBankAcc(BankAccount bankAcc) {

        BankAccountValid baValid = new BankAccountValid();
        Date dateOfBirth = baValid.validDateOfBirth(bankAcc.getYear(), bankAcc.getMonth(), bankAcc.getDay());

        if( !baValid.getValidTel(bankAcc.getTel()) |
            !baValid.getValidNumCard(bankAcc.getCardNo()) |
            !baValid.getValidIdCard(bankAcc.getIdCard()) |
            baValid.validCardType(bankAcc.getCardType()) == null |
            !baValid.validBankAcc(bankAcc) )
            return false;

        PreparedStatement preSt;
        String sql = "Insert into bank_account_table(card_type, name, card_no, id_card," +
                " phone_number, address, date_of_birth)" +
                "values(?, ?, ?, ?, ?, ?, ?)";
        Connection conn = Datasource.getConn();
        try{
            preSt = conn.prepareStatement(sql);
            preSt.setString(1, bankAcc.getCardType());
            preSt.setString(2, bankAcc.getName());
            preSt.setString(3, bankAcc.getCardNo());
            preSt.setString(4, bankAcc.getIdCard());
            preSt.setString(5, bankAcc.getTel());
            preSt.setString(6, bankAcc.getAddress());
//            preSt.setDate(7, new java.sql.Date(bankAcc.getDateOfBirth().getTime()));
            // convert Date to Date in sql
            preSt.setDate(7, new java.sql.Date(dateOfBirth.getTime()));
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
    public List<BankAccount> getBankAccByIdCard(String idCard) {
        PreparedStatement preSt;
        String sql = "Select * from bank_account_table where id_card = ?";
        List<BankAccount> bankAccounts = new ArrayList<>();
        Connection conn = Datasource.getConn();
        try{
            preSt = conn.prepareStatement(sql);
            preSt.setString(1, idCard);
            ResultSet rs = preSt.executeQuery();
            while (rs.next()){
                BankAccount bankAcc = rowMapper(rs);
                if (!Objects.isNull(bankAcc)) bankAccounts.add(bankAcc);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return bankAccounts;
    }

    @Override
    public List<BankAccount> getAllBankAcc() {
        List<BankAccount> bankAccounts = new ArrayList<>();
        PreparedStatement preSt;
        String sql = "Select * from bank_account_table";
        Connection conn = Datasource.getConn();
        try{
            preSt = conn.prepareStatement(sql);
            ResultSet rs = preSt.executeQuery();
            while (rs.next()){
                BankAccount bankAcc = rowMapper(rs);
                if (!Objects.isNull(bankAcc)) bankAccounts.add(bankAcc);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return bankAccounts;
    }

    public static BankAccount rowMapper(ResultSet rs){
        BankAccount bankAcc = null;
        long id = 0;
        try{
            id = rs.getLong("id");
            String cardType = rs.getString("card_type");
            String name = rs.getString("name");
            String cardNo = rs.getString("card_no");
            String idCard = rs.getString("id_card");
            String tel = rs.getString("phone_number");
            String address = rs.getString("address");
            Date dateOfBirth = rs.getDate("date_of_birth");
            bankAcc = BankAccount.builder()
                    .id(id)
                    .cardType(cardType)
                    .name(name)
                    .cardNo(cardNo)
                    .idCard(idCard)
                    .tel(tel)
                    .address(address)
                    .dateOfBirth(dateOfBirth)
                    .build();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return bankAcc;
    }
}

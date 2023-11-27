package org.aptech.t2303e.lab1.bankAccount;

import org.aptech.t2303e.lab1.bankAccount.dao.BankAccountDao;
import org.aptech.t2303e.lab1.bankAccount.dao.impl.BankAccountDaoImpl;
import org.aptech.t2303e.lab1.bankAccount.entity.BankAccount;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankAccountValid {
    private static final String TEL_REGEX = "\\d{10}";
    private static final String ID_CARD_REGEX = "\\d{12}";
    private static final String NUM_CARD_REGEX = "\\d{12}";
    private static final String[] CARD_TYPES = {"VISA", "JCB", "HYBRID"};

    public String validCardType(String cardType){
        for(String allowedType : CARD_TYPES){
            if(allowedType.equalsIgnoreCase(cardType)){
                return cardType;
            }
        }
        System.err.println("Invalid card type. The card type must be one of the following types : VISA, JCB and HYBRID.");
        return null;
    }

    private boolean isValidFormat(String input, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public boolean getValidTel(String x){
        String tel = x;
        boolean valid = true;
        while (!isValidFormat(tel, TEL_REGEX)){
            valid = false;
            break;
        }
        if(!valid){
            System.err.println("Invalid cellphone number format. Please enter 10 digits with no letters.");
            return false;
        }else {
            return true;
        }
    }

    public boolean getValidNumCard(String x){
        String numCard = x;
        boolean valid = true;
        while (!isValidFormat(numCard, NUM_CARD_REGEX)){
            valid = false;
            break;
        }
        if(!valid){
            System.err.println("Invalid card number format. Please enter 12 digits with no letters.");
            return false;
        }else {
            return true;
        }
    }

    public boolean getValidIdCard(String x){
        String idCard = x;
        boolean valid = true;
        while (!isValidFormat(idCard, ID_CARD_REGEX)){
            valid = false;
            break;
        }
        if(!valid){
            System.err.println("Invalid id card format. Please enter 12 digits with no letters.");
            return false;
        }else {
            return true;
        }
    }

    public Date validDateOfBirth(int year, int month, int day){
        String stringDate = null;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        if(year + 15 > currentYear | year + 130 < currentYear){
            System.err.println("The age has to older than 15 years old and younger than 130 years old.");
            return null;
        }
        if(month < 1 | month > 12){
            System.err.println("The value of the month has to belong from 1 to 12.");
            return null;
        }
        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if (day < 1 | day > 31){
                    System.err.println("The number days in " + month + " isn't smaller than 1 and bigger than 31.");
                    return null;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if (day < 1 | day > 30){
                    System.err.println("The number days in " + month + " isn't smaller than 1 and bigger than 30.");
                    return null;
                }
                break;
            case 2:
                boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) | (year % 400 == 0);
                if(isLeapYear && (day < 1 | day > 29)){
                    System.err.println("The number days in " + month + " of the leap year isn't smaller than 1 and bigger than 29.");
                    return null;
                }else if (!isLeapYear && (day < 1 | day > 28)){
                    System.err.println("The number days in " + month + " of the non leap year isn't smaller than 1 and bigger than 28.");
                    return null;
                }
                break;
        }
        if(month > 9 && day > 9){
            stringDate = year + "-" + month + "-" + day;
        }else if(month < 9 && day > 9)  {
            stringDate = year + "-0" + month + "-" + day;
        }else if(month > 9 && day < 9){
            stringDate = year + "-" + month + "-0" + day;
        }else if (month < 9 && day < 9) {
            stringDate = year + "-0" + month + "-0" + day;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(stringDate);
            return date;
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public Date validHourMinuteSecond(int hour, int minute, int second){

        return null;
    }

    public boolean validBankAcc(BankAccount bankAccount){
        BankAccountDao bankAcc = new BankAccountDaoImpl();
        List<BankAccount> bankAccounts = bankAcc.getBankAccByIdCard(bankAccount.getIdCard());

        for (int i = 0; i < bankAccounts.size(); i++) {
            if(bankAccounts.get(i).getCardNo().equals(bankAccount.getCardNo())){
                System.err.println("Bank account number : " + bankAccount.getIdCard() + " has defined.");
                return false;
            }
        }

        for (int i = 0; i < bankAccounts.size(); i++) {
            if(bankAccounts.get(i).getIdCard().equals(bankAccount.getIdCard()) &&
                    !bankAccounts.get(i).getName().equals(bankAccount.getName())){
                System.err.println("ID Card : " + bankAccount.getIdCard() + " has defined with : " + bankAccounts.get(i).getName());
                return false;
            }
        }

        for (int i = 0; i < bankAccounts.size(); i++) {
            if(bankAccounts.get(i).getIdCard().equals(bankAccount.getIdCard()) &&
                    bankAccounts.get(i).getCardType().equals(bankAccount.getCardType()) &&
                    bankAccounts.get(i).getName().equals(bankAccount.getName())){
                System.err.println(bankAccount.getName() + " ID Card : " + bankAccount.getIdCard() + " has had the kind of the card : " + bankAccount.getCardType());
                return false;
            }
        }
        return true;
    }

}

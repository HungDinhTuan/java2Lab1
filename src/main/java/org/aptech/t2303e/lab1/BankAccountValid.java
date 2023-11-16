package org.aptech.t2303e.lab1;

import org.aptech.t2303e.lab1.bankAccountDao.BankAccountDao;
import org.aptech.t2303e.lab1.bankAccountDao.BankAccountDaoImpl;

import java.util.ArrayList;
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

    public boolean validBankAcc(String cardType, String name, String cardNo, String idCard){
        BankAccountDao bankAcc = new BankAccountDaoImpl();
        List<BankAccount> bankAccounts = bankAcc.getBankAccByIdCard(idCard);

        for (int i = 0; i < bankAccounts.size(); i++) {
            if(bankAccounts.get(i).getCardNo().equals(cardNo)){
                System.err.println("Bank account number : " + idCard + " has defined.");
                return false;
            }
        }

        for (int i = 0; i < bankAccounts.size(); i++) {
            if(bankAccounts.get(i).getIdCard().equals(idCard) &&
                    !bankAccounts.get(i).getName().equals(name)){
                System.err.println("ID Card : " + idCard + " has defined with : " + bankAccounts.get(i).getName());
                return false;
            }
        }

        for (int i = 0; i < bankAccounts.size(); i++) {
            if(bankAccounts.get(i).getIdCard().equals(idCard) &&
                    bankAccounts.get(i).getCardType().equals(cardType) &&
                    bankAccounts.get(i).getName().equals(name)){
                System.err.println(name + " ID Card : " + idCard + " has had the kind of the card : " + cardType);
                return false;
            }
        }
        return true;
    }
}

package com.example.blackcoinservice.utilities;

import org.springframework.stereotype.Component;

@Component
public class AmountUtility {

    public boolean validateAmount(String amount) {
        if (amount.matches("\\d+(\\.\\d+)?")) {
            double amountDouble = Double.parseDouble(amount);
            if (!(amountDouble >0)) {
                return false;
            }
            if (amount.contains(".")) {
                String[] amountSplit = amount.split("\\.");
                if (amountSplit[1].length()>3) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public String formatAmount(String amount) {
        if (!amount.contains(".")) {
            amount = amount+".000";
        }
        String[] amountSplit = amount.split("\\.");
        while (amountSplit[1].length()<3) {
            amountSplit[1] = amountSplit[1] + "0";
        }
        int firstElement = Integer.parseInt(amountSplit[0]);
        return firstElement + "." + amountSplit[1];
    }

}

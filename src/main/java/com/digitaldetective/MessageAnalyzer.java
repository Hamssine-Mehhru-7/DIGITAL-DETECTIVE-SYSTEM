package com.digitaldetective;

import java.util.Locale;

public class MessageAnalyzer extends Analyzer {

    @Override
    public String analyze(String content) {

        String text = content.toLowerCase(Locale.ROOT);

        StringBuilder result = new StringBuilder();

        result.append("MESSAGE ANALYSIS\n\n");

        if (text.contains("otp") || text.contains("password")) {
            result.append("- Requests sensitive information.\n");
        }

        if (text.contains("send money")
                || text.contains("make payment")) {

            result.append("- Requests a money transfer or payment.\n");
        }

        if (text.contains("winner")
                || text.contains("claim your prize")) {

            result.append("- Contains a prize or winning claim.\n");
        }

        if (text.contains("urgent")
                || text.contains("act now")) {

            result.append("- Uses pressure or urgency.\n");
        }

        if (result.length() == "MESSAGE ANALYSIS\n\n".length()) {
            result.append("No obvious warning signs found by these basic rules.\n");
        }

        result.append("\nNote: This is an educational rule-based analysis.");

        return result.toString();
    }
}

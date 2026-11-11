package com.digitaldetective;

import java.util.Locale;

public class EmailAnalyzer extends Analyzer {

    @Override
    public String analyze(String content) {

        String text = content.toLowerCase(Locale.ROOT);
        StringBuilder result = new StringBuilder();

        result.append("EMAIL ANALYSIS\n\n");

        if (text.contains("password") || text.contains("otp")) {
            result.append("- Requests sensitive login information.\n");
        }

        if (text.contains("urgent") || text.contains("immediately")) {
            result.append("- Uses urgent language.\n");
        }

        if (text.contains("verify your account")
                || text.contains("account suspended")) {
            result.append("- Contains an account-verification warning sign.\n");
        }

        if (result.length() == "EMAIL ANALYSIS\n\n".length()) {
            result.append("No obvious warning signs found by these basic rules.\n");
        }

        result.append("\nNote: This is an educational rule-based analysis.");

        return result.toString();
    }
}

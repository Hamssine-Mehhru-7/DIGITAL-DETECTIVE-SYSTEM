package com.digitaldetective;

import java.util.Locale;

public class EmailAnalyzer extends Analyzer {

    @Override
    public String analyze(String content) {

        if (content == null || content.trim().isEmpty()) {
            return "EMAIL ANALYSIS\n\nNo email content provided.";
        }

        String text = content.toLowerCase(Locale.ROOT);
        StringBuilder result = new StringBuilder();

        int riskScore = 0;

        result.append("EMAIL ANALYSIS\n\n");
        result.append("DETECTED WARNING SIGNS:\n");

        // 1. Sensitive information requests
        if (text.contains("password") || text.contains("otp")
                || text.contains("bank details")) {

            result.append("- Requests sensitive personal or financial information.\n");
            riskScore += 4;
        }

        // 2. Urgent language
        if (text.contains("urgent") || text.contains("immediately")
                || text.contains("act now")) {

            result.append("- Uses urgent or pressure-based language.\n");
            riskScore += 2;
        }

        // 3. Account verification warnings
        if (text.contains("verify your account")
                || text.contains("account suspended")) {

            result.append("- Contains account verification or suspension warnings.\n");
            riskScore += 3;
        }

        // 4. Guaranteed benefits
        if (text.contains("guaranteed certification")
                || text.contains("guaranteed academic certification")
                || text.contains("guaranteed internship")) {

            result.append("- Makes guaranteed certification or internship claims.\n");
            riskScore += 2;
        }

        // 5. Attractive stipend promises
        if (text.contains("stipend-based internship")
                || text.contains("guaranteed stipend")) {

            result.append("- Promises attractive internship or stipend benefits.\n");
            riskScore += 2;
        }

        // 6. Mandatory registration
        if (text.contains("mandatory")
                || text.contains("compulsory")) {

            result.append("- Uses mandatory or compulsory participation claims.\n");
            riskScore += 2;
        }

        // 7. Institutional endorsement claims
        if (text.contains("placement officers")
                || text.contains("placement cell")
                || text.contains("official endorsement")) {

            result.append("- Claims institutional or placement-cell endorsement that requires verification.\n");
            riskScore += 3;
        }

        // 8. Registration pressure
        if (text.contains("secure your allocation")
                || text.contains("limited seats")
                || text.contains("registration deadline")) {

            result.append("- Creates pressure to register before a deadline.\n");
            riskScore += 2;
        }

        // 9. External links
        if (text.contains("http://") || text.contains("https://")
                || text.contains("forms.gle")) {

            result.append("- Contains an external registration or web link. Its safety is not established by its presence.\n");
            riskScore += 1;
        }

        // 10. Institutional identity claims
        if (text.contains("indian institute of technology")
                || text.contains("iitm ds t&p cell")) {

            result.append("- Uses an IITM institutional identity claim. Sender authorization needs independent verification.\n");
            riskScore += 2;
        }

        // Final risk assessment
        result.append("\nRISK ASSESSMENT\n");
        result.append("-------------------------\n");
        result.append("Risk Score: ").append(riskScore).append("\n");

        if (riskScore >= 8) {

            result.append("Risk Level: HIGH\n");
            result.append("Verdict: Suspicious email. Further verification is required.\n");

        } else if (riskScore >= 4) {

            result.append("Risk Level: MEDIUM\n");
            result.append("Verdict: Some suspicious indicators detected.\n");

        } else {

            result.append("Risk Level: LOW\n");
            result.append("Verdict: No major warning signs detected by the current rules.\n");
        }

        result.append("\nRECOMMENDATION\n");
        result.append("-------------------------\n");
        result.append("Verify the sender and internship announcement through ");
        result.append("official institutional channels before submitting personal information.\n");

        result.append("\nNote: This is an educational rule-based analysis. ");
        result.append("It cannot independently confirm whether an email is genuine or fraudulent.");

        return result.toString();
    }
}

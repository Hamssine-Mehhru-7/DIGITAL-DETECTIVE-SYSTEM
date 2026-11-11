package com.digitaldetective;

import java.net.URI;
import java.util.Locale;

public class URLAnalyzer extends Analyzer {

    @Override
    public String analyze(String content) {

        String url = content.trim();
        String text = url.toLowerCase(Locale.ROOT);

        StringBuilder result = new StringBuilder();

        result.append("URL ANALYSIS\n\n");

        try {
            URI uri = URI.create(url);

            String scheme = uri.getScheme();

            if (scheme == null ||
                    !(scheme.equalsIgnoreCase("https")
                    || scheme.equalsIgnoreCase("http"))) {

                result.append("- URL has an unusual or missing protocol.\n");

            } else if (scheme.equalsIgnoreCase("http")) {

                result.append("- URL does not use HTTPS.\n");
            }

        } catch (IllegalArgumentException e) {

            result.append("- URL format appears invalid.\n");
        }

        if (text.contains("@")) {
            result.append("- Contains an @ symbol.\n");
        }

        if (text.contains("verify")
                || text.contains("login")
                || text.contains("free-gift")) {

            result.append("- Contains words that may need closer inspection.\n");
        }

        if (result.length() == "URL ANALYSIS\n\n".length()) {
            result.append("No obvious warning signs found by these basic rules.\n");
        }

        result.append("\nNote: This is an educational rule-based analysis.");

        return result.toString();
    }
}

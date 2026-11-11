package com.digitaldetective;

public class AnalyzerDemo {

    public static void main(String[] args) {

        Analyzer email = new EmailAnalyzer();
        Analyzer url = new URLAnalyzer();
        Analyzer message = new MessageAnalyzer();

        System.out.println(email.analyze(
            "Your account is suspended. Verify your password immediately."
        ));

        System.out.println("\n----------------------\n");

        System.out.println(url.analyze(
            "http://example.com/login"
        ));

        System.out.println("\n----------------------\n");

        System.out.println(message.analyze(
            "Congratulations! You are a winner. Send money now."
        ));
    }
}

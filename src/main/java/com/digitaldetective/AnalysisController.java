
package com.digitaldetective;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AnalysisController {

    @GetMapping("/email-analysis")
    public String emailPage(HttpSession session) {

        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        return "email-analysis";
    }

    @PostMapping("/email-analysis")
    public String analyzeEmail(
            @RequestParam String emailContent,
            HttpSession session,
            Model model) {

        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        if (emailContent == null || emailContent.isBlank()) {
            model.addAttribute("error", "Please enter email content.");
            return "email-analysis";
        }

        EmailAnalyzer analyzer = new EmailAnalyzer();

        String result = analyzer.analyze(emailContent);

        model.addAttribute("analysisResult", result);
        model.addAttribute("emailContent", emailContent);

        return "email-analysis";
    }

    // URL ANALYSIS

    @GetMapping("/url-analysis")
    public String urlPage(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        return "url-analysis";
    }

    @PostMapping("/url-analysis")
    public String analyzeUrl(
            @RequestParam String urlContent,
            HttpSession session,
            Model model) {

        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        if (urlContent == null || urlContent.isBlank()) {
            model.addAttribute("error", "Please enter a URL.");
            return "url-analysis";
        }

        URLAnalyzer analyzer = new URLAnalyzer();
        String result = analyzer.analyze(urlContent);

        model.addAttribute("analysisResult", result);
        model.addAttribute("urlContent", urlContent);

        return "url-analysis";
    }

    // MESSAGE ANALYSIS

    @GetMapping("/message-analysis")
    public String messagePage(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        return "message-analysis";
    }

    @PostMapping("/message-analysis")
    public String analyzeMessage(
            @RequestParam String messageContent,
            HttpSession session,
            Model model) {

        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        if (messageContent == null || messageContent.isBlank()) {
            model.addAttribute("error", "Please enter a message.");
            return "message-analysis";
        }

        MessageAnalyzer analyzer = new MessageAnalyzer();
        String result = analyzer.analyze(messageContent);

        model.addAttribute("analysisResult", result);
        model.addAttribute("messageContent", messageContent);

        return "message-analysis";
    }

}


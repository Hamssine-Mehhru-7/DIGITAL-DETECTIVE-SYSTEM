
package com.digitaldetective;

import com.digitaldetective.database.AnalysisDAO;
import com.digitaldetective.database.CaseDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

@Controller
public class RiskAssessmentController {

    @GetMapping("/risk-assessment")
    public String riskPage(HttpSession session, Model model) throws SQLException {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        model.addAttribute("cases", CaseDAO.getCaseTitlesByUserId(userId));
        return "risk-assessment";
    }

    @PostMapping("/risk-assessment/analyze")
    public String assess(@RequestParam int caseId,
                         @RequestParam String content,
                         HttpSession session,
                         Model model) throws SQLException {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Map<Integer, String> cases =
                CaseDAO.getCaseTitlesByUserId(userId);

        model.addAttribute("cases", cases);

        if (!cases.containsKey(caseId)) {
            model.addAttribute("error", "Invalid case selected.");
            return "risk-assessment";
        }

        if (content == null || content.isBlank()) {
            model.addAttribute("error", "Please enter content to analyze.");
            return "risk-assessment";
        }

        String text = content.toLowerCase(Locale.ROOT);
        int score = 0;
        StringBuilder result = new StringBuilder();

        result.append("RISK ASSESSMENT\n\n");
        result.append("Case: ").append(cases.get(caseId)).append("\n");

        String[] indicators = {
            "password", "otp", "urgent", "immediately",
            "verify your account", "account suspended",
            "click this link", "bank details",
            "personal information", "confidential information"
        };

        result.append("\nDetected indicators:\n");

        for (String indicator : indicators) {
            if (text.contains(indicator)) {
                score += 10;
                result.append("- ").append(indicator).append("\n");
            }
        }

        score = Math.min(score, 100);

        String level;
        if (score >= 60) {
            level = "HIGH";
        } else if (score >= 30) {
            level = "MEDIUM";
        } else {
            level = "LOW";
        }

        result.append("\nRisk Score: ").append(score).append("/100");
        result.append("\nRisk Level: ").append(level);
        result.append("\n\nThis is an educational rule-based assessment, ");
        result.append("not a guarantee that content is safe or malicious.");

        AnalysisDAO.saveAnalysis(
                caseId, "Risk Assessment", content, result.toString()
        );

        model.addAttribute("riskResult", result.toString());
        model.addAttribute("selectedCase", caseId);

        return "risk-assessment";
    }
}


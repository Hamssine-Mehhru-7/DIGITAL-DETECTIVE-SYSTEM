package com.digitaldetective;

import com.digitaldetective.database.ScanDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.*;

@RestController
public class AnalysisApiController {
    @PostMapping("/api/analyze")
    public Map<String, Object> analyze(@RequestBody Map<String, String> request, HttpSession session) throws SQLException {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return Map.of("error", "Your session has expired. Please log in again.");
        String type = request.getOrDefault("type", "").trim().toLowerCase(Locale.ROOT);
        String content = request.getOrDefault("content", "").trim();
        if (content.isBlank()) return Map.of("error", "Please enter content to analyze.");
        if (content.length() > 100000) return Map.of("error", "Please keep submitted content under 100,000 characters.");
        Analyzer analyzer; String label;
        switch (type) {
            case "email" -> { analyzer = new EmailAnalyzer(); label = "Email"; }
            case "url" -> { analyzer = new URLAnalyzer(); label = "URL"; }
            case "message" -> { analyzer = new MessageAnalyzer(); label = "Message"; }
            default -> { return Map.of("error", "Choose Email, URL or Message analysis."); }
        }
        String findings = analyzer.analyze(content);
        int indicators = 0;
        for (String line : findings.split("\\R")) if (line.trim().startsWith("- ")) indicators++;
        int score = Math.min(100, indicators * 25);
        String level = score >= 50 ? "HIGH" : score >= 25 ? "MEDIUM" : "LOW";
        String fullResult = findings + "\n\nRisk assessment: " + level + " (" + score + "/100)" +
            "\nIndicators identified: " + indicators +
            "\nMethod: local rule-based checks; no external threat-intelligence service was queried." +
            "\nImportant: this is a screening aid, not proof that content is safe or malicious.";
        int scanId = ScanDAO.saveScan(userId, label, content, fullResult, score, level);
        return Map.of("result", fullResult, "riskLevel", level, "riskScore", score, "indicators", indicators, "scanId", scanId);
    }
}

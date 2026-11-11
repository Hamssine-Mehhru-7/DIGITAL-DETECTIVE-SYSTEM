
package com.digitaldetective;

import com.digitaldetective.database.CaseDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Map;

@Controller
public class ReportController {

    @GetMapping("/reports")
    public String reportsPage(HttpSession session, Model model)
            throws SQLException {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        model.addAttribute(
                "cases", CaseDAO.getCaseTitlesByUserId(userId)
        );

        return "reports";
    }

    @PostMapping("/reports/generate")
    public ResponseEntity<byte[]> generateReport(
            @RequestParam int caseId,
            @RequestParam String reportType,
            HttpSession session) throws Exception {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        Map<Integer, String> cases =
                CaseDAO.getCaseTitlesByUserId(userId);

        if (!cases.containsKey(caseId)) {
            return ResponseEntity.status(403).build();
        }

        if (!reportType.equals("All")
                && !reportType.equals("Email")
                && !reportType.equals("URL")
                && !reportType.equals("Message")
                && !reportType.equals("Risk Assessment")) {
            return ResponseEntity.badRequest().build();
        }

        String filePath =
                ReportGenerator.generateReport(caseId, reportType);

        Path path = Path.of(filePath);
        byte[] fileContent = Files.readAllBytes(path);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + path.getFileName() + "\""
                )
                .contentType(MediaType.TEXT_PLAIN)
                .body(fileContent);
    }
}


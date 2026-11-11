
package com.digitaldetective;

import com.digitaldetective.database.CaseDAO;
import com.digitaldetective.database.EvidenceDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@Controller
public class EvidenceController {

    @GetMapping("/evidence")
    public String evidencePage(
            @RequestParam(required = false) Integer caseId,
            HttpSession session, Model model) throws SQLException {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Map<Integer, String> cases =
                CaseDAO.getCaseTitlesByUserId(userId);

        model.addAttribute("cases", cases);

        if (caseId != null && cases.containsKey(caseId)) {
            model.addAttribute(
                    "evidenceList",
                    EvidenceDAO.getEvidenceByCaseId(caseId)
            );
            model.addAttribute("selectedCase", caseId);
        }

        return "evidence";
    }

    @PostMapping("/evidence/add")
    public String addEvidence(
            @RequestParam int caseId,
            @RequestParam String evidenceType,
            @RequestParam String content,
            HttpSession session, Model model) throws SQLException {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Map<Integer, String> cases =
                CaseDAO.getCaseTitlesByUserId(userId);

        if (!cases.containsKey(caseId)) {
            return "redirect:/evidence?error";
        }

        if (evidenceType.isBlank() || content.isBlank()) {
            model.addAttribute("cases", cases);
            model.addAttribute("error", "Please complete all fields.");
            model.addAttribute("selectedCase", caseId);
            return "evidence";
        }

        EvidenceDAO.addEvidence(caseId, evidenceType, content);

        return "redirect:/evidence?caseId=" + caseId + "&added";
    }
}


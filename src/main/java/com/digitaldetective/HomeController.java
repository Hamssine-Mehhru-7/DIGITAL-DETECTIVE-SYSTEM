package com.digitaldetective;

import com.digitaldetective.database.ScanDAO;
import com.digitaldetective.database.UserDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String landingPage() { return "auth"; }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/";
        try {
            model.addAttribute("scanCount", ScanDAO.countScans(userId));
            model.addAttribute("caseCount", ScanDAO.countCases(userId));
            model.addAttribute("evidenceCount", ScanDAO.countEvidence(userId));
            model.addAttribute("recentScans", ScanDAO.getRecentScans(userId));
            model.addAttribute("userName", UserDAO.getProfile(userId).getOrDefault("name", "Investigator"));
        } catch (Exception ex) {
            model.addAttribute("scanCount", 0);
            model.addAttribute("caseCount", 0);
            model.addAttribute("evidenceCount", 0);
            model.addAttribute("recentScans", java.util.List.of());
            model.addAttribute("userName", "Investigator");
            model.addAttribute("dashboardError", "Dashboard data could not be loaded.");
            ex.printStackTrace();
        }
        return "index";
    }
}

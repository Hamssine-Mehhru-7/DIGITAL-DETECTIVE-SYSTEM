package com.digitaldetective;

import com.digitaldetective.database.UserDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class WorkspaceController {

    @GetMapping("/analysis-workspace")
    public String analysisWorkspace(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        try {
            Map<String, String> profile = UserDAO.getProfile(userId);
            model.addAttribute("userName", profile.getOrDefault("name", "Investigator"));
            model.addAttribute("userEmail", profile.getOrDefault("email", ""));
        } catch (Exception ex) {
            model.addAttribute("userName", "Investigator");
            model.addAttribute("userEmail", "");
            ex.printStackTrace();
        }

        return "analyzer-workspace";
    }

    @GetMapping("/report-workspace")
    public String reportWorkspace(HttpSession session) {
        return session.getAttribute("userId") == null
                ? "redirect:/login" : "redirect:/reports";
    }
}

DIGITAL DETECTIVE - READY-TO-OPEN JAVA PROJECT

1. Extract this ZIP.
2. Open the extracted digital-detective folder in VS Code (File > Open Folder).
3. Make sure JDK 25 and Maven are installed (the included pom.xml targets Java 25).
4. Open Terminal in VS Code and run: mvn spring-boot:run
5. Open http://localhost:8080
6. Register a user, log in, and use the dashboard.

The supplied Dashboard and Analyzer design are connected to this Spring Boot project. The landing, login, and registration pages all use this project’s own routes and database.
The Analyzer submits Email / URL / Message text to the Java backend at /api/analyze. Completed scans are stored against the signed-in user and shown on the dashboard.

IMPORTANT PROJECT NOTES
- The supplied frontend ZIP referenced dashboard-assets, analyzer-assets and report-assets folders, but did not include them. Small SVG fallback icons are included so there are no broken image links.
- The dashboard now shows only database-backed cases, evidence counts, and scans. The report navigation opens the database-backed report generator at /reports.
- The analysis rules are educational keyword/format checks, not a professional security scanner. They inspect the text submitted to this app; they do not query live threat-intelligence services or open URLs.
- Do not delete the original frontend folders included under frontend-reference; they are retained for reference.


AUTHENTICATION AND ANALYSIS UPDATE
- Open http://localhost:8080/ for this project's own landing page. LOGIN and REGISTER lead to this project's Java handlers.
- After login, the dashboard displays counts and recent scans saved for the signed-in user.
- Email, URL and message checks run locally in Java using transparent rule-based checks. This is not a live antivirus engine or external reputation lookup.
- A completed scan is saved in the project's SQLite database (user_scans table). A fresh database is created automatically on first start; register your own account.
- Stop any older Digital Detective server before starting this project so port 8080 is served by the new folder.

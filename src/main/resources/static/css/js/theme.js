
(function () {
    const savedTheme = localStorage.getItem("dd-theme") || "dark";

    function applyTheme(theme) {
        let actualTheme = theme;

        if (theme === "system") {
            actualTheme = window.matchMedia("(prefers-color-scheme: dark)").matches
                ? "dark" : "light";
        }

        document.documentElement.setAttribute("data-theme", actualTheme);
    }

    window.setAppTheme = function (theme) {
        localStorage.setItem("dd-theme", theme);
        applyTheme(theme);
    };

    applyTheme(savedTheme);

    window.matchMedia("(prefers-color-scheme: dark)")
        .addEventListener("change", function () {
            if (localStorage.getItem("dd-theme") === "system") {
                applyTheme("system");
            }
        });
})();

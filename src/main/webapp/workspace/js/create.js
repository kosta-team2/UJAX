(function () {
    "use strict";

    const WS_ENUMS = [
        "PYTHON3",
        "PYPY3",
        "C99",
        "JAVA11",
        "RUBY",
        "KOTLIN",
        "SWIFT",
        "TEXT",
        "CS",
        "NODE",
        "GO",
        "D",
        "RUST2018",
        "CPP17",
    ];

    const WS_LANGS = [
        {value: "PYTHON3", label: "Python 3"},
        {value: "PYPY3", label: "PyPy 3"},
        {value: "C99", label: "C99"},
        {value: "JAVA11", label: "Java 11"},
        {value: "RUBY", label: "Ruby"},
        {value: "KOTLIN", label: "Kotlin (JVM)"},
        {value: "SWIFT", label: "Swift"},
        {value: "TEXT", label: "Text"},
        {value: "CS", label: "C#"},
        {value: "NODE", label: "Node.js"},
        {value: "GO", label: "Go"},
        {value: "D", label: "D"},
        {value: "RUST2018", label: "Rust 2018"},
        {value: "CPP17", label: "C++17 (Clang)"},
    ];

    const form = document.getElementById("createForm");
    const wsName = document.getElementById("wsName");
    const langSelect = document.getElementById("langSelect");
    const modal = document.getElementById("createdModal");
    const modalOk = document.getElementById("modalOk");

    function injectOptions(list) {
        // 기존 placeholder 제외하고 모두 제거
        while (langSelect.options.length > 1) langSelect.remove(1);

        list.forEach(({value, label}) => {
            const opt = document.createElement("option");
            opt.value = value;
            opt.textContent = label;
            langSelect.appendChild(opt);
        });
    }

    function validate() {
        if (!form.checkValidity()) {
            form.reportValidity();
            return false;
        }
        const name = (nameInput.value || "").trim();
        if (!/^[\w\- ]{2,60}$/.test(name)) {
            alert("이름은 2~60자, 영문/숫자/하이픈/공백만 사용 가능합니다.");
            nameInput.focus();
            return false;
        }

        const lang = (langSelect.value || "").toUpperCase();
        if (!lang || !WS_ENUMS.includes(lang)) {
            alert("언어를 선택해주세요.");
            langSelect.focus();
            return false;
        }

        return true;
    }

    form.addEventListener("submit", (e) => {
        if (!validate()) {
            e.preventDefault();
            return;
        }
    });

    // 모달 열기
    // modal.classList.add('show');

    injectOptions(WS_LANGS);
})();
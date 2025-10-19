package org.kostaTeam2.domain.workspace;

import java.util.HashMap;
import java.util.Map;

public enum WorkspaceLanguage {
    PYTHON3("python3", "Python 3"),
    PYPY3("pypy3", "PyPy3"),
    C99("c99", "C99"),
    JAVA11("java11", "Java 11"),
    RUBY("ruby", "Ruby"),
    KOTLIN("kotlin", "Kotlin (JVM)"),
    SWIFT("swift", "Swift"),
    TEXT("text", "Text"),
    CS("cs", "C#"),
    NODE("node", "Node.js"),
    GO("go", "Go"),
    D("d", "D"),
    RUST2018("rust2018", "Rust 2018"),
    CPP17("cpp17", "C++17 (Clang)");

    private final String id;
    private final String label;

    private static final Map<String, WorkspaceLanguage> BY_ID = new HashMap<>();

    static {
        for (WorkspaceLanguage lang : values()) {
            BY_ID.put(lang.id, lang);
        }
    }

    WorkspaceLanguage(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    //  db에 소문자로 저장될 경우 대문자로 변경
    public static WorkspaceLanguage fromString(String value) {
        if (value == null) return null;
        return WorkspaceLanguage.valueOf(value.toUpperCase());
    }

    public static WorkspaceLanguage fromId(String id) {
        WorkspaceLanguage lang = BY_ID.get(id);
        if (lang == null) {
            throw new IllegalArgumentException("Unknown language id: " + id);
        }
        return lang;
    }

    @Override
    public String toString() {
        return id;
    }
}

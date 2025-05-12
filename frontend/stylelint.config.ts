module.exports = {
    extends: "stylelint-config-standard",
    rules: {
        "color-no-invalid-hex": true, // Ungültige Hex-Farben verhindern
        "declaration-colon-newline-after": "always-multi-line", // Erzwingt Zeilenumbrüche nach dem Doppelpunkt
        "block-opening-brace-space-before": "always", // Leerzeichen vor öffnendem Block
        "block-closing-brace-newline-before": "always", // Zeilenumbruch vor schließendem Block
        "property-no-unknown": [true, { ignore: ["v-bind"] }], // Verhindert unbekannte CSS-Eigenschaften, erlaubt jedoch v-bind
        "selector-pseudo-element-colon-notation": "double", // Doppelte Doppelpunktnotation für Pseudo-Elemente
        "no-duplicate-selectors": true, // Verhindert doppelte Selektoren
        "no-empty-source": true, // Leere Quellen verhindern
        indentation: 2, // Indentation auf 2 Leerzeichen setzen
        "max-empty-lines": 1, // Maximal 1 leere Zeile zwischen CSS-Blöcken
        "no-missing-end-of-source-newline": true, // Zeilenumbruch am Ende der Datei erzwingen
        "unit-allowed-list": ["em", "rem", "px", "vw", "vh", "%"], // Erlaubte Einheiten
        "selector-max-id": 0, // Verhindert die Verwendung von IDs als Selektoren
        "media-feature-name-no-unknown": true, // Verhindert unbekannte Media-Feature-Namen
        "at-rule-no-unknown": [
            true,
            {
                ignoreAtRules: ["import", "tailwind", "apply", "screen"], // Tailwind-Spezifische Regeln erlauben
            },
        ],
        "no-unknown-animations": true, // Verhindert unbekannte Animationen
    },
};


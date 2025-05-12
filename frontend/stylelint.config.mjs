export default {
    extends: "stylelint-config-standard",
    rules: {
        "color-no-invalid-hex": true,
        "declaration-colon-newline-after": "always-multi-line",
        "block-opening-brace-space-before": "always",
        "block-closing-brace-newline-before": "always",
        "property-no-unknown": [true, { ignore: ["v-bind"] }],
        "selector-pseudo-element-colon-notation": "double",
        "no-duplicate-selectors": true,
        "no-empty-source": true,
        indentation: 2,
        "max-empty-lines": 1,
        "no-missing-end-of-source-newline": true,
        "unit-allowed-list": ["em", "rem", "px", "vw", "vh", "%"],
        "selector-max-id": 0,
        "media-feature-name-no-unknown": true,
        "at-rule-no-unknown": [
            true,
            {
                ignoreAtRules: ["import", "tailwind", "apply", "screen"],
            },
        ],
        "no-unknown-animations": true,
    },
};


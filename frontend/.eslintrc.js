// .eslintrc.js
module.exports = {
    root: true,
    parser: "@typescript-eslint/parser",
    plugins: ["@typescript-eslint", "react", "react-hooks", "jsx-a11y", "import"],
    extends: [
        "eslint:recommended",
        "plugin:react/recommended",
        "plugin:react-hooks/recommended",
        "plugin:jsx-a11y/recommended",
        "plugin:@typescript-eslint/recommended",
        "plugin:import/errors",
        "plugin:import/warnings",
        "plugin:import/typescript",
        "next/core-web-vitals",
    ],
    rules: {
        "react/react-in-jsx-scope": "off",
        "import/order": [
            "warn",
            {
                groups: [["builtin", "external"], ["internal"], ["parent", "sibling", "index"]],
                "newlines-between": "always",
            },
        ],
    },
    settings: {
        react: {
            version: "detect",
        },
    },
};


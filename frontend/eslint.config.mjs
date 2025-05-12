import { dirname } from "path";
import { fileURLToPath } from "url";
import { FlatCompat } from "@eslint/eslintrc";

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// Für Kompatibilität mit bestehenden Plugins wie eslint-config-next
const compat = new FlatCompat({
    baseDirectory: __dirname,
});

const config = [
    {
        ignores: ["node_modules", ".next", "out", "public"],
    },
    ...compat.extends("next/core-web-vitals", "next/typescript"),
    {
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
    },
];

export default config;

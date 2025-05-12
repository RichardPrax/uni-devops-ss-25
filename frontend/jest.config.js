const nextJest = require("next/jest");

/** @type {import('jest').Config} */
const createJestConfig = nextJest({
    dir: "./",
});

const config = {
    coverageProvider: "v8",
    testEnvironment: "jsdom",
    preset: "ts-jest",
    setupFilesAfterEnv: ["<rootDir>/jest.setup.js"],

    collectCoverage: true,
    collectCoverageFrom: ["components/**/*.{js,jsx,ts,tsx}", "pages/**/*.{js,jsx,ts,tsx}", "lib/**/*.{js,jsx,ts,tsx}"],
    coverageDirectory: "coverage",
    coverageReporters: ["lcov", "cobertura", "text-summary"],

    reporters: [
        "default",
        [
            "jest-junit",
            {
                outputDirectory: "./reports/junit",
                outputName: "results.xml",
            },
        ],
    ],
};

module.exports = createJestConfig(config);


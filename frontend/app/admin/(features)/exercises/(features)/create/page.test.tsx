import React from "react";
import { render, screen } from "@testing-library/react";

import CreateGeneralExercisePage from "./page";

jest.mock("next/navigation", () => ({
    useRouter: jest.fn(),
}));

describe("CreateGeneralExercisePage Component", () => {
    it("renders the page title", () => {
        render(<CreateGeneralExercisePage />);

        // Check if the title is rendered
        expect(screen.getByText("Neuen Grundübung anlegen")).toBeInTheDocument();
    });

    it("applies the correct styling to the container", () => {
        render(<CreateGeneralExercisePage />);

        // Check if the container has the correct class names
        const container = screen.getByText("Neuen Grundübung anlegen").closest("div");
        expect(container).toHaveClass("mx-auto mt-10 p-6 rounded-lg shadow-lg bg-[var(--admin-card-background)] text-white border border-[var(--admin-card-border)]");
    });
});

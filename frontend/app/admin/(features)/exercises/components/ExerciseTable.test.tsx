import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import ExerciseTable from "./ExerciseTable";
import { useRouter } from "next/navigation";

// Mock the router
jest.mock("next/navigation", () => ({
    useRouter: jest.fn(),
}));

const mockRouterPush = jest.fn();
(useRouter as jest.Mock).mockReturnValue({ push: mockRouterPush });

describe("ExerciseTable Component", () => {
    const mockExercises = [
        {
            id: "0a5748ef-d3e9-4df7-8feb-efa174a520c2",
            name: "Bench Press",
            categories: ["CORE", "LOWER_BODY", "UPPER_BODY"],
            equipment: ["BENCH", "BALL", "MAT", "CHAIR"],
            shortDescription: "Eine grundlegende Übung für den Oberkörper",
            longDescription:
                "Liegestütze sind eine grundlegende Körpergewichtsübung, die hauptsächlich die Brust, Schultern und Trizeps trainiert. Sie beanspruchen auch die Rumpfmuskulatur für Stabilität.",
            directions:
                "1. Beginnen Sie in einer hohen Plank-Position. 2. Senken Sie Ihren Körper, bis Ihre Brust fast den Boden berührt. 3. Drücken Sie sich wieder nach oben. 4. Halten Sie Ihre Rumpfmuskulatur während der gesamten Übung angespannt.",
            video: "https://example.com/videos/push-up",
            thumbnailUrl: "https://example.com/videos/push-up",
        },
        {
            id: "1b5748ef-d3e9-4df7-8feb-efa174a520c3",
            name: "Squat",
            categories: ["LOWER_BODY"],
            equipment: ["MAT"],
            shortDescription: "Eine grundlegende Übung für die Beine",
            longDescription: "Kniebeugen sind eine grundlegende Übung, die die Beinmuskulatur stärkt.",
            directions: "1. Stellen Sie sich aufrecht hin. 2. Beugen Sie die Knie, bis Ihre Oberschenkel parallel zum Boden sind. 3. Drücken Sie sich wieder nach oben.",
            video: "https://example.com/videos/squat",
            thumbnailUrl: "https://example.com/videos/squat",
        },
    ];

    const mockToggleExerciseSelection = jest.fn();

    it("renders the table with exercises", () => {
        render(<ExerciseTable exercises={mockExercises} isDeleteMode={false} selectedExercises={[]} toggleExerciseSelection={mockToggleExerciseSelection} />);

        // Check if exercise rows are rendered
        expect(screen.getByText("Squat")).toBeInTheDocument();
        expect(screen.getByText("Bench Press")).toBeInTheDocument();

        // Check if categories and descriptions are rendered
        expect(screen.getByText("LOWER_BODY")).toBeInTheDocument();
        expect(screen.getByText("Eine grundlegende Übung für die Beine")).toBeInTheDocument();
    });

    it("navigates to the exercise detail page when a row is clicked (not in delete mode)", () => {
        render(<ExerciseTable exercises={mockExercises} isDeleteMode={false} selectedExercises={[]} toggleExerciseSelection={mockToggleExerciseSelection} />);

        // Click on the first row
        const secondRow = screen.getByText("Squat").closest("tr");
        fireEvent.click(secondRow!);

        // Check if the router push function was called with the correct URL
        expect(mockRouterPush).toHaveBeenCalledWith("/admin/exercises/1b5748ef-d3e9-4df7-8feb-efa174a520c3");
    });

    it("toggles exercise selection when a row is clicked in delete mode", () => {
        render(<ExerciseTable exercises={mockExercises} isDeleteMode={true} selectedExercises={[]} toggleExerciseSelection={mockToggleExerciseSelection} />);

        // Click on the first row
        const secondRow = screen.getByText("Squat").closest("tr");
        fireEvent.click(secondRow!);

        // Check if the toggleExerciseSelection function was called with the correct exercise
        expect(mockToggleExerciseSelection).toHaveBeenCalledWith(mockExercises[1]);
    });

    it("displays checkboxes in delete mode", () => {
        render(<ExerciseTable exercises={mockExercises} isDeleteMode={true} selectedExercises={[]} toggleExerciseSelection={mockToggleExerciseSelection} />);

        // Check if checkboxes are rendered
        const checkboxes = screen.getAllByRole("checkbox");
        expect(checkboxes.length).toBe(2);
    });

    it("checks the checkbox for selected exercises in delete mode", () => {
        render(<ExerciseTable exercises={mockExercises} isDeleteMode={true} selectedExercises={[mockExercises[0]]} toggleExerciseSelection={mockToggleExerciseSelection} />);

        // Check if the checkbox for the first exercise is checked
        const firstCheckbox = screen.getAllByRole("checkbox")[0];
        expect(firstCheckbox).toBeChecked();

        // Check if the checkbox for the second exercise is not checked
        const secondCheckbox = screen.getAllByRole("checkbox")[1];
        expect(secondCheckbox).not.toBeChecked();
    });

    it("prevents row click propagation when a checkbox is clicked", () => {
        render(<ExerciseTable exercises={mockExercises} isDeleteMode={true} selectedExercises={[]} toggleExerciseSelection={mockToggleExerciseSelection} />);

        // Click on the checkbox of the first row
        const firstCheckbox = screen.getAllByRole("checkbox")[0];
        fireEvent.click(firstCheckbox);

        // Check if the toggleExerciseSelection function was called with the correct exercise
        expect(mockToggleExerciseSelection).toHaveBeenCalledWith(mockExercises[0]);
    });
});

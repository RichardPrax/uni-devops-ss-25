import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import { useRouter } from "next/navigation";

import Exercises from "./Exercises";

jest.mock("next/navigation", () => ({
    useRouter: jest.fn(),
}));

const mockRouterPush = jest.fn();
(useRouter as jest.Mock).mockReturnValue({ push: mockRouterPush });

describe("Exercises Component", () => {
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

    const mockRefetchExercises = jest.fn();

    it("renders the search bar and exercise table", () => {
        render(<Exercises exercises={mockExercises} refetchExercises={mockRefetchExercises} />);

        // Check if the search bar is rendered
        expect(screen.getByPlaceholderText(/search/i)).toBeInTheDocument();

        // Check if the exercise table headers are rendered
        expect(screen.getByText("Name")).toBeInTheDocument();
        expect(screen.getByText("Kategorie")).toBeInTheDocument();
        expect(screen.getByText("Beschreibung")).toBeInTheDocument();
    });

    it("filters exercises based on search input", () => {
        render(<Exercises exercises={mockExercises} refetchExercises={mockRefetchExercises} />);

        // Type into the search bar
        const searchInput = screen.getByPlaceholderText(/search/i);
        fireEvent.change(searchInput, { target: { value: "Squat" } });

        // Check if only the filtered exercise is displayed
        expect(screen.getByText("Squat")).toBeInTheDocument();
        expect(screen.queryByText("Bench Press")).not.toBeInTheDocument();
    });

    it("display add and delete buttons when delete mode is off", () => {
        render(<Exercises exercises={mockExercises} refetchExercises={mockRefetchExercises} />);

        // Check if add and delete buttons are displayed
        expect(screen.getByText("Hinzufügen")).toBeInTheDocument();
        expect(screen.getByText("Entfernen")).toBeInTheDocument();
    });

    it("toggles delete mode and shows confirm/cancel buttons", () => {
        render(<Exercises exercises={mockExercises} refetchExercises={mockRefetchExercises} />);

        // Click the "Entfernen" button
        const deleteButton = screen.getByText("Entfernen");
        fireEvent.click(deleteButton);

        // Check if confirm and cancel buttons are displayed
        expect(screen.getByText("Bestätigen")).toBeInTheDocument();
        expect(screen.getByText("Abbrechen")).toBeInTheDocument();
    });

    it("navigates to the create exercise page when 'Hinzufügen' is clicked", () => {
        render(<Exercises exercises={mockExercises} refetchExercises={mockRefetchExercises} />);

        // Click the "Hinzufügen" button
        const addButton = screen.getByText("Hinzufügen");
        fireEvent.click(addButton);

        // Check if the router push function was called
        expect(mockRouterPush).toHaveBeenCalledWith("/admin/exercises/create");
    });

    it("shows the confirm deletion dialog when 'Bestätigen' is clicked", () => {
        render(<Exercises exercises={mockExercises} refetchExercises={mockRefetchExercises} />);

        // Enter delete mode
        fireEvent.click(screen.getByText("Entfernen"));

        // Click the "Bestätigen" button
        fireEvent.click(screen.getByText("Bestätigen"));

        // Check if the confirm deletion dialog is displayed
        expect(screen.getByText(/Bestätigen Sie das Löschen/i)).toBeInTheDocument();
    });
});

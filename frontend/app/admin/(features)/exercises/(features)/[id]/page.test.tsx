import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import ExerciseDetailPage from "./page";
import { useFetchGeneralExercise } from "../../hooks/useFetchGeneralExercise";
import { useRouter, useParams } from "next/navigation";

// Mock the router
jest.mock("next/navigation", () => ({
    useRouter: jest.fn(),
    useParams: jest.fn(),
}));

// Mock the fetch hook
jest.mock("../../hooks/useFetchGeneralExercise", () => ({
    useFetchGeneralExercise: jest.fn(),
}));

// Mock child components
jest.mock("./components/GeneralInfo", () => ({ exercise, onEdit }: any) => (
    <div data-testid="general-info">
        <p>{exercise.name}</p>
        <button onClick={onEdit}>Edit</button>
    </div>
));

jest.mock("./components/EditGeneralExerciseForm", () => ({ exercise, onCancel, onUpdateSuccess }: any) => (
    <div data-testid="edit-form">
        <p>Edit {exercise.name}</p>
        <button onClick={onCancel}>Cancel</button>
        <button onClick={onUpdateSuccess}>Save</button>
    </div>
));

describe("ExerciseDetailPage Component", () => {
    const mockRouterPush = jest.fn();
    const mockRefetch = jest.fn();

    beforeEach(() => {
        (useRouter as jest.Mock).mockReturnValue({ push: mockRouterPush });
        (useParams as jest.Mock).mockReturnValue({ id: "1" });
    });

    it("renders loading state", () => {
        (useFetchGeneralExercise as jest.Mock).mockReturnValue({
            exercise: null,
            loading: true,
            error: null,
            refetch: mockRefetch,
        });

        render(<ExerciseDetailPage />);

        expect(screen.getByText("Loading...")).toBeInTheDocument();
    });

    it("renders error state", () => {
        (useFetchGeneralExercise as jest.Mock).mockReturnValue({
            exercise: null,
            loading: false,
            error: "Error fetching exercise",
            refetch: mockRefetch,
        });

        render(<ExerciseDetailPage />);

        expect(screen.getByText("Error: Error fetching exercise")).toBeInTheDocument();
    });

    it("renders exercise details", () => {
        (useFetchGeneralExercise as jest.Mock).mockReturnValue({
            exercise: { id: "1", name: "Test Exercise" },
            loading: false,
            error: null,
            refetch: mockRefetch,
        });

        render(<ExerciseDetailPage />);

        const heading = screen.getByRole("heading", { level: 1 });
        expect(heading).toHaveTextContent("Test Exercise");
        expect(screen.getByTestId("general-info")).toBeInTheDocument();
    });

    it("toggles to edit mode", () => {
        (useFetchGeneralExercise as jest.Mock).mockReturnValue({
            exercise: { id: "1", name: "Test Exercise" },
            loading: false,
            error: null,
            refetch: mockRefetch,
        });

        render(<ExerciseDetailPage />);

        // Click the edit button
        fireEvent.click(screen.getByText("Edit"));

        // Check if the edit form is displayed
        expect(screen.getByTestId("edit-form")).toBeInTheDocument();
        expect(screen.getByText("Edit Test Exercise")).toBeInTheDocument();
    });

    it("cancels edit mode", () => {
        (useFetchGeneralExercise as jest.Mock).mockReturnValue({
            exercise: { id: "1", name: "Test Exercise" },
            loading: false,
            error: null,
            refetch: mockRefetch,
        });

        render(<ExerciseDetailPage />);

        // Enter edit mode
        fireEvent.click(screen.getByText("Edit"));

        // Click the cancel button
        fireEvent.click(screen.getByText("Cancel"));

        // Check if the general info is displayed again
        expect(screen.getByTestId("general-info")).toBeInTheDocument();
    });

    it("saves changes and exits edit mode", () => {
        (useFetchGeneralExercise as jest.Mock).mockReturnValue({
            exercise: { id: "1", name: "Test Exercise" },
            loading: false,
            error: null,
            refetch: mockRefetch,
        });

        render(<ExerciseDetailPage />);

        // Enter edit mode
        fireEvent.click(screen.getByText("Edit"));

        // Click the save button
        fireEvent.click(screen.getByText("Save"));

        // Check if refetch was called and edit mode exited
        expect(mockRefetch).toHaveBeenCalled();
        expect(screen.getByTestId("general-info")).toBeInTheDocument();
    });

    it("navigates back to the exercises list", () => {
        (useFetchGeneralExercise as jest.Mock).mockReturnValue({
            exercise: { id: "1", name: "Test Exercise" },
            loading: false,
            error: null,
            refetch: mockRefetch,
        });

        render(<ExerciseDetailPage />);

        // Click the back button
        fireEvent.click(screen.getByText("Zurück"));

        // Check if navigation occurred
        expect(mockRouterPush).toHaveBeenCalledWith("/admin/exercises");
    });
});

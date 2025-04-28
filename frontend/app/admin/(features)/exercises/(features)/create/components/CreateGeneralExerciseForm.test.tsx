import React from "react";
import { render, screen, fireEvent, act } from "@testing-library/react";
import CreateGeneralExerciseForm from "./CreateGeneralExerciseForm";
import { useCreateGeneralExercise } from "../../../hooks/useCreateGeneralExercise";
import { useRouter } from "next/navigation";

// Mock the router
jest.mock("next/navigation", () => ({
    useRouter: jest.fn(),
}));

// Mock the createGeneralExercise hook
jest.mock("../../../hooks/useCreateGeneralExercise", () => ({
    useCreateGeneralExercise: jest.fn(),
}));

const mockRouterPush = jest.fn();
(useRouter as jest.Mock).mockReturnValue({ push: mockRouterPush });

describe("CreateGeneralExerciseForm Component", () => {
    const mockCreateGeneralExercise = jest.fn();
    (useCreateGeneralExercise as jest.Mock).mockReturnValue({
        createGeneralExercise: mockCreateGeneralExercise,
        loading: false,
        error: null,
    });

    it("renders all form fields", () => {
        render(<CreateGeneralExerciseForm />);

        // Check if all input fields are rendered
        expect(screen.getByLabelText("Name")).toBeInTheDocument();
        expect(screen.getByText("Kategorien")).toBeInTheDocument();
        expect(screen.getByText("Ausrüstung")).toBeInTheDocument();
        expect(screen.getByLabelText("Kurzbeschreibung")).toBeInTheDocument();
        expect(screen.getByLabelText("Lange Beschreibung")).toBeInTheDocument();
        expect(screen.getByLabelText("Anweisungen")).toBeInTheDocument();
        expect(screen.getByLabelText("Video")).toBeInTheDocument();
        expect(screen.getByLabelText("Thumbnail")).toBeInTheDocument();
    });

    it("calls createGeneralExercise on form submission", async () => {
        render(<CreateGeneralExerciseForm />);

        // Fill out the form
        fireEvent.change(screen.getByLabelText("Name"), { target: { value: "Test Exercise" } });
        fireEvent.change(screen.getByLabelText("Kurzbeschreibung"), { target: { value: "Short description" } });
        fireEvent.change(screen.getByLabelText("Lange Beschreibung"), { target: { value: "Long description" } });
        fireEvent.change(screen.getByLabelText("Anweisungen"), { target: { value: "Directions" } });
        fireEvent.change(screen.getByLabelText("Video"), { target: { value: "http://example.com/video" } });
        fireEvent.change(screen.getByLabelText("Thumbnail"), { target: { value: "http://example.com/thumbnail" } });

        // Submit the form
        fireEvent.submit(screen.getByRole("form"));

        // Check if createGeneralExercise was called with the correct data
        expect(mockCreateGeneralExercise).toHaveBeenCalledWith({
            name: "Test Exercise",
            categories: [],
            equipment: [],
            shortDescription: "Short description",
            longDescription: "Long description",
            directions: "Directions",
            video: "http://example.com/video",
            thumbnailUrl: "http://example.com/thumbnail",
        });
    });

    it("navigates to the exercises page on successful submission", async () => {
        mockCreateGeneralExercise.mockResolvedValueOnce(true);

        render(<CreateGeneralExerciseForm />);

        await act(async () => {
            fireEvent.submit(screen.getByRole("form"));
        });

        // Wait for navigation
        expect(mockRouterPush).toHaveBeenCalledWith("/admin/exercises");
    });

    it("displays an error message if submission fails", async () => {
        (useCreateGeneralExercise as jest.Mock).mockReturnValue({
            createGeneralExercise: jest.fn().mockRejectedValueOnce("Error creating exercise"),
            loading: false,
            error: "Error creating exercise",
        });

        render(<CreateGeneralExerciseForm />);

        // Check if the error message is displayed
        expect(screen.getByText("Error creating exercise")).toBeInTheDocument();
    });

    it("disables the submit button when loading", () => {
        (useCreateGeneralExercise as jest.Mock).mockReturnValue({
            createGeneralExercise: jest.fn(),
            loading: true,
            error: null,
        });

        render(<CreateGeneralExerciseForm />);

        // Check if the submit button is disabled
        const submitButton = screen.getByText("Speichern...");
        expect(submitButton).toBeDisabled();
    });

    // TODO: add tests to check if modals open and close properly
});

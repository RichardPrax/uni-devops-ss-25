import React from "react";
import { render, screen, fireEvent, act } from "@testing-library/react";
import EditGeneralExerciseForm from "./EditGeneralExerciseForm";
import { useUpdateGeneralExercise } from "../../../hooks/useUpdateGeneralExercise";

// Mock the update hook
jest.mock("../../../hooks/useUpdateGeneralExercise", () => ({
    useUpdateGeneralExercise: jest.fn(),
}));

// Mock child components
jest.mock("../../create/components/CustomSelect", () => ({ options, value, onChange, onOpenModal }: any) => (
    <div data-testid="custom-select">
        <p>CustomSelect</p>
        <button onClick={onOpenModal}>Open Modal</button>
    </div>
));

jest.mock(
    "../../../components/CategoryModal",
    () =>
        ({ isOpen, onClose }: any) =>
            isOpen ? (
                <div data-testid="category-modal">
                    <button onClick={onClose}>Close Category Modal</button>
                </div>
            ) : null
);

jest.mock(
    "../../../components/EquipmentModal",
    () =>
        ({ isOpen, onClose }: any) =>
            isOpen ? (
                <div data-testid="equipment-modal">
                    <button onClick={onClose}>Close Equipment Modal</button>
                </div>
            ) : null
);

describe("EditGeneralExerciseForm Component", () => {
    const mockOnCancel = jest.fn();
    const mockOnUpdateSuccess = jest.fn();
    const mockUpdateGeneralExercise = jest.fn();

    const mockExercise = {
        id: "1",
        name: "Test Exercise",
        shortDescription: "Short description",
        longDescription: "Long description",
        directions: "Directions",
        categories: ["Strength"],
        equipment: ["Dumbbell"],
        video: "http://example.com/video",
        thumbnailUrl: "http://example.com/thumbnail.jpg",
    };

    beforeEach(() => {
        (useUpdateGeneralExercise as jest.Mock).mockReturnValue({
            updateGeneralExercise: mockUpdateGeneralExercise,
            loading: false,
            error: null,
        });
    });

    it("renders all form fields", () => {
        render(<EditGeneralExerciseForm exercise={mockExercise} onCancel={mockOnCancel} onUpdateSuccess={mockOnUpdateSuccess} />);

        // Check if all input fields are rendered
        expect(screen.getByLabelText("Name")).toBeInTheDocument();
        expect(screen.getByLabelText("Kurzbeschreibung")).toBeInTheDocument();
        expect(screen.getByLabelText("Langbeschreibung")).toBeInTheDocument();
        expect(screen.getByLabelText("Anweisungen")).toBeInTheDocument();
        expect(screen.getByLabelText("Video URL")).toBeInTheDocument();
        expect(screen.getByLabelText("Thumbnail URL")).toBeInTheDocument();

        // Check if CustomSelect components are rendered
        expect(screen.getAllByTestId("custom-select").length).toBe(2);
    });

    it("opens and closes the category modal", () => {
        render(<EditGeneralExerciseForm exercise={mockExercise} onCancel={mockOnCancel} onUpdateSuccess={mockOnUpdateSuccess} />);

        // Open the category modal
        fireEvent.click(screen.getAllByText("Open Modal")[0]);
        expect(screen.getByTestId("category-modal")).toBeInTheDocument();

        // Close the category modal
        fireEvent.click(screen.getByText("Close Category Modal"));
        expect(screen.queryByTestId("category-modal")).not.toBeInTheDocument();
    });

    it("opens and closes the equipment modal", () => {
        render(<EditGeneralExerciseForm exercise={mockExercise} onCancel={mockOnCancel} onUpdateSuccess={mockOnUpdateSuccess} />);

        // Open the equipment modal
        fireEvent.click(screen.getAllByText("Open Modal")[1]);
        expect(screen.getByTestId("equipment-modal")).toBeInTheDocument();

        // Close the equipment modal
        fireEvent.click(screen.getByText("Close Equipment Modal"));
        expect(screen.queryByTestId("equipment-modal")).not.toBeInTheDocument();
    });

    it("calls onCancel when the cancel button is clicked", () => {
        render(<EditGeneralExerciseForm exercise={mockExercise} onCancel={mockOnCancel} onUpdateSuccess={mockOnUpdateSuccess} />);

        // Click the cancel button
        fireEvent.click(screen.getByText("Abbrechen"));

        // Check if onCancel was called
        expect(mockOnCancel).toHaveBeenCalled();
    });

    it("calls updateGeneralExercise on form submission", async () => {
        render(<EditGeneralExerciseForm exercise={mockExercise} onCancel={mockOnCancel} onUpdateSuccess={mockOnUpdateSuccess} />);

        // Submit the form
        fireEvent.submit(screen.getByRole("form"));

        // Check if updateGeneralExercise was called with the correct data
        expect(mockUpdateGeneralExercise).toHaveBeenCalledWith(mockExercise);
    });

    it("calls onUpdateSuccess after successful update", async () => {
        mockUpdateGeneralExercise.mockResolvedValueOnce(true);

        render(<EditGeneralExerciseForm exercise={mockExercise} onCancel={mockOnCancel} onUpdateSuccess={mockOnUpdateSuccess} />);

        await act(async () => {
            fireEvent.submit(screen.getByRole("form"));
        });

        expect(mockOnUpdateSuccess).toHaveBeenCalled();
    });

    it("displays an error message if update fails", () => {
        (useUpdateGeneralExercise as jest.Mock).mockReturnValue({
            updateGeneralExercise: mockUpdateGeneralExercise,
            loading: false,
            error: "Error updating exercise",
        });

        render(<EditGeneralExerciseForm exercise={mockExercise} onCancel={mockOnCancel} onUpdateSuccess={mockOnUpdateSuccess} />);

        // Check if the error message is displayed
        expect(screen.getByText("Error updating exercise")).toBeInTheDocument();
    });

    it("disables the submit button when loading", () => {
        (useUpdateGeneralExercise as jest.Mock).mockReturnValue({
            updateGeneralExercise: mockUpdateGeneralExercise,
            loading: true,
            error: null,
        });

        render(<EditGeneralExerciseForm exercise={mockExercise} onCancel={mockOnCancel} onUpdateSuccess={mockOnUpdateSuccess} />);

        // Check if the submit button is disabled
        const submitButton = screen.getByText("Speichern...");
        expect(submitButton).toBeDisabled();
    });
});

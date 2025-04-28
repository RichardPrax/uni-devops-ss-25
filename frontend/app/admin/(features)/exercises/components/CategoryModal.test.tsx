import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import CategoryModal from "./CategoryModal";

describe("CategoryModal Component", () => {
    const mockOnClose = jest.fn();
    const mockOnCategoryChange = jest.fn();

    const mockCategoryOptions = [
        { value: "UPPER_BODY", label: "Oberkörper" },
        { value: "LOWER_BODY", label: "Unterkörper" },
        { value: "CORE", label: "Rumpf" },
        { value: "FULL_BODY", label: "Ganzkörper" },
    ];

    const mockCategories = ["UPPER_BODY"];

    it("does not render when isOpen is false", () => {
        render(<CategoryModal isOpen={false} onClose={mockOnClose} categories={mockCategories} categoryOptions={mockCategoryOptions} onCategoryChange={mockOnCategoryChange} />);

        // Modal should not be in the document
        expect(screen.queryByText("Kategorien auswählen")).not.toBeInTheDocument();
    });

    it("renders when isOpen is true", () => {
        render(<CategoryModal isOpen={true} onClose={mockOnClose} categories={mockCategories} categoryOptions={mockCategoryOptions} onCategoryChange={mockOnCategoryChange} />);

        // Modal should be in the document
        expect(screen.getByText("Kategorien auswählen")).toBeInTheDocument();

        // Check if category options are rendered
        expect(screen.getByLabelText("Oberkörper")).toBeInTheDocument();
        expect(screen.getByLabelText("Unterkörper")).toBeInTheDocument();
        expect(screen.getByLabelText("Rumpf")).toBeInTheDocument();
        expect(screen.getByLabelText("Ganzkörper")).toBeInTheDocument();
    });

    it("calls onClose when the modal is closed", () => {
        render(<CategoryModal isOpen={true} onClose={mockOnClose} categories={mockCategories} categoryOptions={mockCategoryOptions} onCategoryChange={mockOnCategoryChange} />);

        // Simulate closing the modal
        fireEvent.click(screen.getByText("✖"));

        // Check if onClose was called
        expect(mockOnClose).toHaveBeenCalled();
    });

    it("checks the correct checkboxes based on selected categories", () => {
        render(<CategoryModal isOpen={true} onClose={mockOnClose} categories={mockCategories} categoryOptions={mockCategoryOptions} onCategoryChange={mockOnCategoryChange} />);

        expect(screen.getByLabelText("Oberkörper")).toBeChecked();

        expect(screen.getByLabelText("Unterkörper")).not.toBeChecked();
        expect(screen.getByLabelText("Ganzkörper")).not.toBeChecked();
        expect(screen.getByLabelText("Rumpf")).not.toBeChecked();
    });

    it("calls onCategoryChange when a checkbox is toggled", () => {
        render(<CategoryModal isOpen={true} onClose={mockOnClose} categories={mockCategories} categoryOptions={mockCategoryOptions} onCategoryChange={mockOnCategoryChange} />);

        // Toggle the "Cardio" checkbox
        const lowerBodyCheckBox = screen.getByLabelText("Unterkörper");
        fireEvent.click(lowerBodyCheckBox);

        // Check if onCategoryChange was called with the updated categories
        expect(mockOnCategoryChange).toHaveBeenCalledWith(["UPPER_BODY", "LOWER_BODY"]);

        // Toggle the "Oberkörper" checkbox (uncheck it)
        const upperBodyCheckBox = screen.getByLabelText("Oberkörper");
        fireEvent.click(upperBodyCheckBox);

        // Check if onCategoryChange was called with the updated categories
        expect(mockOnCategoryChange).toHaveBeenCalledWith([]);
    });
});

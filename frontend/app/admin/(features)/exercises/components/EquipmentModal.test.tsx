import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";

import EquipmentModal from "./EquipmentModal";

describe("EquipmentModal Component", () => {
    const mockOnClose = jest.fn();
    const mockOnEquipmentChange = jest.fn();

    const mockEquipmentOptions = [
        { value: "CHAIR", label: "Stuhl" },
        { value: "MAT", label: "Matte" },
        { value: "BENCH", label: "Bank" },
        { value: "BALL", label: "Ball" },
    ];

    const mockEquipment = ["CHAIR"];

    it("does not render when isOpen is false", () => {
        render(<EquipmentModal isOpen={false} onClose={mockOnClose} equipment={mockEquipment} equipmentOptions={mockEquipmentOptions} onEquipmentChange={mockOnEquipmentChange} />);

        // Modal should not be in the document
        expect(screen.queryByText("Ausrüstung auswählen")).not.toBeInTheDocument();
    });

    it("renders when isOpen is true", () => {
        render(<EquipmentModal isOpen={true} onClose={mockOnClose} equipment={mockEquipment} equipmentOptions={mockEquipmentOptions} onEquipmentChange={mockOnEquipmentChange} />);

        // Modal should be in the document
        expect(screen.getByText("Ausrüstung auswählen")).toBeInTheDocument();

        // Check if equipment options are rendered
        expect(screen.getByLabelText("Stuhl")).toBeInTheDocument();
        expect(screen.getByLabelText("Matte")).toBeInTheDocument();
        expect(screen.getByLabelText("Bank")).toBeInTheDocument();
        expect(screen.getByLabelText("Ball")).toBeInTheDocument();
    });

    it("calls onClose when the modal is closed", () => {
        render(<EquipmentModal isOpen={true} onClose={mockOnClose} equipment={mockEquipment} equipmentOptions={mockEquipmentOptions} onEquipmentChange={mockOnEquipmentChange} />);

        // Simulate closing the modal
        fireEvent.click(screen.getByText("✖"));

        // Check if onClose was called
        expect(mockOnClose).toHaveBeenCalled();
    });

    it("checks the correct checkboxes based on selected equipment", () => {
        render(<EquipmentModal isOpen={true} onClose={mockOnClose} equipment={mockEquipment} equipmentOptions={mockEquipmentOptions} onEquipmentChange={mockOnEquipmentChange} />);

        // Check if the checkbox for "Stuhl" is checked
        expect(screen.getByLabelText("Stuhl")).toBeChecked();

        // Check if the checkboxes for the other values are not checked
        expect(screen.getByLabelText("Matte")).not.toBeChecked();
        expect(screen.getByLabelText("Bank")).not.toBeChecked();
        expect(screen.getByLabelText("Ball")).not.toBeChecked();
    });

    it("calls onEquipmentChange when a checkbox is toggled", () => {
        render(<EquipmentModal isOpen={true} onClose={mockOnClose} equipment={mockEquipment} equipmentOptions={mockEquipmentOptions} onEquipmentChange={mockOnEquipmentChange} />);

        // Toggle the "Barbell" checkbox
        const matCheckBox = screen.getByLabelText("Matte");
        fireEvent.click(matCheckBox);

        // Check if onEquipmentChange was called with the updated equipment
        expect(mockOnEquipmentChange).toHaveBeenCalledWith(["CHAIR", "MAT"]);

        // Toggle the "Dumbbell" checkbox (uncheck it)
        const chairCheckBox = screen.getByLabelText("Stuhl");
        fireEvent.click(chairCheckBox);

        // Check if onEquipmentChange was called with the updated equipment
        expect(mockOnEquipmentChange).toHaveBeenCalledWith([]);
    });
});

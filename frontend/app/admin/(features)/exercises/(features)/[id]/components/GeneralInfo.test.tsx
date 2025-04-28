import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import GeneralInfo from "./GeneralInfo";
import { GeneralExercise } from "@/app/entities/GeneralExercise";

describe("GeneralInfo Component", () => {
    const mockOnEdit = jest.fn();

    const mockExercise: GeneralExercise = {
        id: "1",
        name: "Test Exercise",
        shortDescription: "This is a short description.",
        longDescription: "This is a long description.",
        directions: "These are the directions.",
        categories: ["Strength", "Cardio"],
        equipment: ["Dumbbell", "Barbell"],
        video: "http://example.com/video",
        thumbnailUrl: "http://example.com/thumbnail.jpg",
    };

    it("renders all exercise details", () => {
        render(<GeneralInfo exercise={mockExercise} onEdit={mockOnEdit} />);

        // Check if all exercise details are rendered
        expect(screen.getByText("Grundlegende Informationen")).toBeInTheDocument();
        expect(screen.getByText("Name:")).toBeInTheDocument();
        expect(screen.getByText("Test Exercise")).toBeInTheDocument();
        expect(screen.getByText("Kurzbeschreibung:")).toBeInTheDocument();
        expect(screen.getByText("This is a short description.")).toBeInTheDocument();
        expect(screen.getByText("Langbeschreibung:")).toBeInTheDocument();
        expect(screen.getByText("This is a long description.")).toBeInTheDocument();
        expect(screen.getByText("Anweisungen:")).toBeInTheDocument();
        expect(screen.getByText("These are the directions.")).toBeInTheDocument();
        expect(screen.getByText("Kategorien:")).toBeInTheDocument();
        expect(screen.getByText("Strength, Cardio")).toBeInTheDocument();
        expect(screen.getByText("Ausrüstung:")).toBeInTheDocument();
        expect(screen.getByText("Dumbbell, Barbell")).toBeInTheDocument();
        expect(screen.getByText("Video:")).toBeInTheDocument();
        expect(screen.getByText("http://example.com/video")).toBeInTheDocument();
        expect(screen.getByAltText("Test Exercise Thumbnail")).toBeInTheDocument();
    });

    it("renders the video link with correct attributes", () => {
        render(<GeneralInfo exercise={mockExercise} onEdit={mockOnEdit} />);

        const videoLink = screen.getByText("http://example.com/video");
        expect(videoLink).toHaveAttribute("href", "http://example.com/video");
        expect(videoLink).toHaveAttribute("target", "_blank");
        expect(videoLink).toHaveAttribute("rel", "noopener noreferrer");
    });

    it("renders the thumbnail image with correct attributes", () => {
        render(<GeneralInfo exercise={mockExercise} onEdit={mockOnEdit} />);

        const thumbnail = screen.getByAltText("Test Exercise Thumbnail");
        expect(thumbnail).toHaveAttribute("src", "http://example.com/thumbnail.jpg");
        expect(thumbnail).toHaveAttribute("alt", "Test Exercise Thumbnail");
    });

    it("calls onEdit when the edit button is clicked", () => {
        render(<GeneralInfo exercise={mockExercise} onEdit={mockOnEdit} />);

        // Click the edit button
        const editButton = screen.getByText("Bearbeiten");
        fireEvent.click(editButton);

        // Check if onEdit was called
        expect(mockOnEdit).toHaveBeenCalled();
    });
});

import { GeneralExercise } from "@/app/entities/GeneralExercise";
import React from "react";

interface GeneralInfoProps {
    exercise: GeneralExercise;
    onEdit: () => void;
}

export default function GeneralInfo({ exercise, onEdit }: GeneralInfoProps) {
    return (
        <div className="bg-[var(--admin-card-background)] p-6 rounded-lg shadow-md flex flex-col border-[var(--admin-card-border)] border">
            <div>
                <div className="flex justify-between items-center mb-4">
                    <h2 className="text-2xl font-semibold mb-4">Grundlegende Informationen</h2>
                    {/* Buttons */}
                    <div>
                        <button className="px-4 py-2 bg-[var(--admin-button-background)] text-white rounded-md hover:bg-blue-700" onClick={onEdit}>
                            Bearbeiten
                        </button>
                    </div>
                </div>
                <div className="mb-4">
                    <strong>Name:</strong> {exercise.name}
                </div>
                <div className="mb-4">
                    <strong>Kurzbeschreibung:</strong> {exercise.shortDescription}
                </div>
                <div className="mb-4">
                    <strong>Langbeschreibung:</strong> {exercise.longDescription}
                </div>
                <div className="mb-4">
                    <strong>Anweisungen:</strong> {exercise.directions}
                </div>
                <div className="mb-4">
                    <strong>Kategorien:</strong> {exercise.categories.join(", ")}
                </div>
                <div className="mb-4">
                    <strong>Ausrüstung:</strong> {exercise.equipment.join(", ")}
                </div>
                <div className="mb-4">
                    <strong>Video:</strong>{" "}
                    <a href={exercise.video} target="_blank" rel="noopener noreferrer">
                        {exercise.video}
                    </a>
                </div>
                <div className="mb-4">
                    <strong>Thumbnail:</strong> <img src={exercise.thumbnailUrl} alt={`${exercise.name} Thumbnail`} className="max-w-full h-auto" />
                </div>
            </div>
        </div>
    );
}

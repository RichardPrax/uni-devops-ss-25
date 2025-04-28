import React from "react";
import { useRouter } from "next/navigation";
import { NewCustomExerciseDTO } from "@/app/entities/NewCustomExerciseDTO";

interface CustomExerciseTableProps {
    exercises: NewCustomExerciseDTO[];
    isDeleteMode: boolean;
    selectedExercises: NewCustomExerciseDTO[];
    toggleExerciseSelection: (exercise: NewCustomExerciseDTO) => void;
}

export default function CustomExerciseTable({ exercises, isDeleteMode, selectedExercises, toggleExerciseSelection }: CustomExerciseTableProps) {
    const router = useRouter();

    const handleRowClick = (exercise: NewCustomExerciseDTO) => {
        if (isDeleteMode) toggleExerciseSelection(exercise);
    };

    return (
        <div className="overflow-hidden rounded-b-lg">
            <table className="min-w-full text-white">
                <tbody>
                    {exercises.map((exercise) => (
                        <tr key={exercise.id} className="border-b border-gray-700 hover:bg-gray-700 hover:cursor-pointer" onClick={() => handleRowClick(exercise)}>
                            <td className="w-1/6 px-6 py-3 text-left">{exercise.generalExerciseId}</td>
                            <td className="w-1/6 px-6 py-3 text-left">{exercise.sets}</td>
                            <td className="w-1/6 px-6 py-3 text-left">{exercise.repetitions}</td>
                            <td className="w-1/6 px-6 py-3 text-left">{exercise.durationInMinutes}</td>
                            <td className="w-2/6 px-6 py-3 text-left">{exercise.tip}</td>
                            {isDeleteMode && (
                                <td>
                                    <input
                                        type="checkbox"
                                        checked={selectedExercises.includes(exercise)}
                                        onChange={(e) => {
                                            e.stopPropagation();
                                            toggleExerciseSelection(exercise);
                                        }}
                                        className="mr-4"
                                    />
                                </td>
                            )}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}


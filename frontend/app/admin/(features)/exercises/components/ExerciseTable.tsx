import React from "react";
import { GeneralExercise } from "@/app/entities/GeneralExercise";
import { useRouter } from "next/navigation";

interface ExerciseTableProps {
    exercises: GeneralExercise[];
    isDeleteMode: boolean;
    selectedExercises: GeneralExercise[];
    toggleExerciseSelection: (exercise: GeneralExercise) => void;
}

export default function ExerciseTable({ exercises, isDeleteMode, selectedExercises, toggleExerciseSelection }: ExerciseTableProps) {
    const router = useRouter();

    const handleRowClick = (exercise: GeneralExercise) => {
        if (isDeleteMode) {
            toggleExerciseSelection(exercise);
        } else {
            router.push(`/admin/exercises/${exercise.id}`);
        }
    };

    return (
        <div className="overflow-hidden rounded-b-lg">
            <table className="min-w-full text-white">
                <tbody>
                    {exercises.map((exercise) => (
                        <tr key={exercise.id} className="border-b border-gray-700 hover:bg-gray-700 hover:cursor-pointer" onClick={() => handleRowClick(exercise)}>
                            <td className="w-1/5 px-6 py-3 text-left">{exercise.name}</td>
                            <td className="w-1/5 px-6 py-3 text-left">{exercise.categories}</td>
                            <td className="w-3/5 px-6 py-3 text-left">{exercise.shortDescription}</td>
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

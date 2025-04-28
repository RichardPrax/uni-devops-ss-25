"use client";

import React, { useState } from "react";
import { useRouter, useParams } from "next/navigation";
import GeneralInfo from "./components/GeneralInfo";
import { useFetchGeneralExercise } from "../../hooks/useFetchGeneralExercise";
import EditGeneralExerciseForm from "./components/EditGeneralExerciseForm";

export default function ExerciseDetailPage() {
    const { id } = useParams() as { id: string };
    const { exercise, loading, error, refetch } = useFetchGeneralExercise(id);
    const [isEditing, setIsEditing] = useState(false);
    const router = useRouter();

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    const toggleEdit = () => {
        setIsEditing(!isEditing);
    };

    const handleUpdateSuccess = () => {
        refetch();
        setIsEditing(false);
    };

    return (
        exercise && (
            <div className="p-8">
                <h1 className="text-4xl font-bold mb-8">{exercise.name}</h1>
                {isEditing ? (
                    <div className="mx-auto mt-10 p-6 rounded-lg shadow-lg bg-[var(--admin-card-background)] text-white border border-[var(--admin-card-border)]">
                        <h1 className="text-2xl font-bold mb-4">Grundlegende Informationen bearbeiten</h1>
                        <EditGeneralExerciseForm exercise={exercise} onCancel={toggleEdit} onUpdateSuccess={handleUpdateSuccess} />
                    </div>
                ) : (
                    <>
                        <GeneralInfo exercise={exercise} onEdit={toggleEdit} />
                        <button className="mt-4 px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700" onClick={() => router.push("/admin/exercises")}>
                            Zurück
                        </button>
                    </>
                )}
            </div>
        )
    );
}

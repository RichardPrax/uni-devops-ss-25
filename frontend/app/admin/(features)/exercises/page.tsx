"use client";

import React from "react";
import Exercises from "./components/Exercises";
import { useFetchGeneralExercises } from "./hooks/useFetchGeneralExercises";

export default function GeneralExercisePage() {
    const { exercises, loading, error, refetch } = useFetchGeneralExercises();

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div>
            <h1 className="text-3xl font-bold mb-4">Übungskatalog</h1>
            <Exercises exercises={exercises} refetchExercises={refetch} />
        </div>
    );
}

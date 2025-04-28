"use client";

import React from "react";
import CreateGeneralExerciseForm from "./components/CreateGeneralExerciseForm";

export default function CreateGeneralExercisePage() {
    return (
        <div className="mx-auto mt-10 p-6 rounded-lg shadow-lg bg-[var(--admin-card-background)] text-white border border-[var(--admin-card-border)]">
            <h1 className="text-2xl font-bold mb-4">Neuen Grundübung anlegen</h1>
            <CreateGeneralExerciseForm />
        </div>
    );
}

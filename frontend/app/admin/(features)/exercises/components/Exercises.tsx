"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";

import { GeneralExercise } from "@/app/entities/GeneralExercise";
import ConfirmDeletionDialog from "@/app/admin/components/ConfirmDeletionDialog";
import SearchBar from "@/components/SearchBar";

import { useDeleteGeneralExercise } from "../hooks/useDeleteGeneralExercise";
import ExerciseTable from "./ExerciseTable";

interface ExercisesProps {
    exercises: GeneralExercise[];
    refetchExercises: () => void;
}

export default function Exercises({ exercises, refetchExercises }: ExercisesProps) {
    const router = useRouter();
    const [search, setSearch] = useState("");
    const [isDeleteMode, setIsDeleteMode] = useState(false);
    const [selectedExercises, setSelectedExercises] = useState<GeneralExercise[]>([]);
    const [showConfirmDialog, setShowConfirmDialog] = useState(false);

    const { deleteGeneralExercise } = useDeleteGeneralExercise();

    const filteredExercises = exercises.filter((exercise) => exercise.name.toLowerCase().includes(search.toLowerCase()));

    const handleDelete = async () => {
        setShowConfirmDialog(false);
        for (const exercise of selectedExercises) {
            await deleteGeneralExercise(exercise.id);
        }
        setIsDeleteMode(false);
        setSelectedExercises([]);
        refetchExercises();
    };

    const toggleExerciseSelection = (exercise: GeneralExercise) => {
        if (selectedExercises.includes(exercise)) {
            setSelectedExercises(selectedExercises.filter((e) => e !== exercise));
        } else {
            setSelectedExercises([...selectedExercises, exercise]);
        }
    };

    return (
        <div className="py-6">
            {/* Suchfeld */}
            <SearchBar searchTerm={search} setSearchTerm={setSearch} />

            <div className="mt-8 p-6 bg-[var(--admin-card-background)] rounded-lg shadow-lg border border-[var(--admin-card-border)]">
                <div className="flex justify-between items-center mb-4">
                    <h2 className="text-2xl font-semibold">Grundübungen</h2>
                    <div>
                        {isDeleteMode ? (
                            <>
                                <button
                                    className="bg-[var(--admin-button-background)] text-white px-4 py-2 rounded-lg mr-2 hover:bg-green-600"
                                    onClick={() => setShowConfirmDialog(true)}
                                >
                                    Bestätigen
                                </button>
                                <button className="bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600" onClick={() => setIsDeleteMode(false)}>
                                    Abbrechen
                                </button>
                            </>
                        ) : (
                            <>
                                <button
                                    className="bg-[var(--admin-button-background)] text-white px-4 py-2 rounded-lg mr-2 hover:bg-blue-600"
                                    onClick={() => router.push("/admin/exercises/create")}
                                >
                                    Hinzufügen
                                </button>
                                <button className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600" onClick={() => setIsDeleteMode(true)}>
                                    Entfernen
                                </button>
                            </>
                        )}
                    </div>
                </div>

                {/* Separater Table-Header für rundes Design */}
                <div className="bg-[var(--admin-card-header-background)] text-white rounded-lg p-4 flex">
                    <span className="w-1/5 font-semibold text-left px-6">Name</span>
                    <span className="w-1/5 font-semibold text-left px-6">Kategorie</span>
                    <span className="w-3/5 font-semibold text-left px-6">Beschreibung</span>
                </div>

                {/* Table Content */}
                <ExerciseTable exercises={filteredExercises} isDeleteMode={isDeleteMode} selectedExercises={selectedExercises} toggleExerciseSelection={toggleExerciseSelection} />
            </div>

            {showConfirmDialog && <ConfirmDeletionDialog setShowConfirmDialog={setShowConfirmDialog} selectedEntities={selectedExercises} handleDelete={handleDelete} />}
        </div>
    );
}


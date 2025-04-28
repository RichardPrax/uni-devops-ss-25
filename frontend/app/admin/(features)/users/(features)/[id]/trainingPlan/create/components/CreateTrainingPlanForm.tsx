import { NewTrainingPlanDTO } from "@/app/entities/NewTrainingPlanDTO";
import { useParams, useRouter } from "next/navigation";
import React, { useState } from "react";
import { useCreateTrainingPlan } from "../hooks/useCreateTrainingPlan";
import { NewCustomExerciseDTO } from "@/app/entities/NewCustomExerciseDTO";
import FormInput from "@/components/FormInput";
import AddExerciseModal from "./AddExerciseModal";
import CustomExerciseTable from "./CustomExerciseTable";
import { useFetchGeneralExercises } from "@/app/admin/(features)/exercises/hooks/useFetchGeneralExercises";
import ConfirmDeletionDialog from "@/app/admin/components/ConfirmDeletionDialog";
import { LucideRemoveFormatting } from "lucide-react";

export default function CreateTrainingPlanForm() {
    const [formData, setFormData] = useState<NewTrainingPlanDTO>({
        name: "",
        exercises: [],
        shortDescription: "",
        longDescription: "",
        tip: "",
        userId: "",
    });

    const { exercises: generalExercises, loading: exercisesLoading, error: exercisesError } = useFetchGeneralExercises();

    const router = useRouter();
    const { id } = useParams() as { id: string };

    const { createTrainingPlan, loading, error } = useCreateTrainingPlan();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        formData.userId = id;
        const newTrainingPlan = await createTrainingPlan(formData);

        if (newTrainingPlan) {
            navigateBackToUserDetailPage();
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const navigateBackToUserDetailPage = () => {
        router.push(`/admin/users/${id}`);
    };

    const [isModalOpen, setIsModalOpen] = useState(false);

    const handleAddExercise = (newExercise: NewCustomExerciseDTO) => {
        setFormData((prev) => ({
            ...prev,
            exercises: [...prev.exercises, newExercise],
        }));
    };

    const toggleExerciseSelection = (exercise: NewCustomExerciseDTO) => {
        if (selectedExercises.includes(exercise)) {
            setSelectedExercises(selectedExercises.filter((e) => e !== exercise));
        } else {
            setSelectedExercises([...selectedExercises, exercise]);
        }
    };

    // TODO: check logic to remove, change logic => direct insert custom exercise when pressing add, so we have an ID
    // TOOD: do we need to adjust somethin or does this shit work?
    const handleRemoveExercise = (exercise: NewCustomExerciseDTO) => {
        setFormData((prev) => ({
            ...prev,
            exercises: prev.exercises.filter((e) => e.generalExerciseId !== exercise.generalExerciseId),
        }));
        setShowConfirmDialog(false);
        setIsDeleteMode(false);
        setSelectedExercises([]);
    };

    const [isDeleteMode, setIsDeleteMode] = useState(false);
    const [selectedExercises, setSelectedExercises] = useState<NewCustomExerciseDTO[]>([]);
    const [showConfirmDialog, setShowConfirmDialog] = useState(false);

    if (exercisesLoading) {
        return <div>Loading ...</div>;
    }

    return (
        <div>
            <form onSubmit={handleSubmit} role="form" className="space-y-4">
                {/* Name */}
                <FormInput id="name" label="Name" value={formData.name} placeholder="Name des Trainingsplanes eingeben" onChange={handleChange} required />

                {/* short description */}
                <FormInput
                    id="shortDescription"
                    label="Kurzbeschreibung"
                    value={formData.shortDescription}
                    placeholder="Kurzbeschreibung des Trainingsplanes eingeben"
                    onChange={handleChange}
                    required
                    textarea
                />

                {/* long description */}
                <FormInput
                    id="longDescription"
                    label="Lange Beschreibung"
                    value={formData.longDescription}
                    placeholder="Lange Beschreibung des Trainingsplanes eingeben"
                    onChange={handleChange}
                    required
                    textarea
                />

                {/* Tips */}
                <FormInput id="tip" label="Hinweise" value={formData.tip} placeholder="Zusätzliche Hinweise eingeben" onChange={handleChange} required textarea />

                {/* Exercises Section */}
                <div>
                    <div className="flex justify-between items-center mb-4">
                        <h3 className="text-lg font-semibold">Übungen</h3>
                        <div>
                            {isDeleteMode ? (
                                <>
                                    <button
                                        type="button"
                                        className="bg-[var(--admin-button-background)] text-white px-4 py-2 rounded-lg mr-2 hover:bg-green-600"
                                        onClick={() => setShowConfirmDialog(true)}
                                    >
                                        Bestätigen
                                    </button>
                                    <button type="button" className="bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600" onClick={() => setIsDeleteMode(false)}>
                                        Abbrechen
                                    </button>
                                </>
                            ) : (
                                <>
                                    <button
                                        type="button"
                                        className="bg-[var(--admin-button-background)] text-white px-4 py-2 rounded-lg mr-2 hover:bg-blue-600"
                                        onClick={() => setIsModalOpen(true)}
                                    >
                                        Hinzufügen
                                    </button>
                                    <button type="button" className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600" onClick={() => setIsDeleteMode(true)}>
                                        Entfernen
                                    </button>
                                </>
                            )}
                        </div>
                    </div>

                    {/* Separater Table-Header für rundes Design */}
                    <div className="bg-[var(--admin-card-header-background)] text-white rounded-lg p-4 flex">
                        <span className="w-1/6 font-semibold text-left px-6">Grundübung</span>
                        <span className="w-1/6 font-semibold text-left px-6">Sätze</span>
                        <span className="w-1/6 font-semibold text-left px-6">Wiederholungen</span>
                        <span className="w-1/6 font-semibold text-left px-6">Dauer</span>
                        <span className="w-2/6 font-semibold text-left px-6">Hinweise</span>
                    </div>

                    <CustomExerciseTable
                        exercises={formData.exercises}
                        isDeleteMode={isDeleteMode}
                        selectedExercises={selectedExercises}
                        toggleExerciseSelection={toggleExerciseSelection}
                    />
                </div>

                {/* Add Exercise Modal */}
                {isModalOpen && <AddExerciseModal onClose={() => setIsModalOpen(false)} onAddExercise={handleAddExercise} generalExercises={generalExercises} />}

                {/* Fehleranzeige */}
                {error && <p className="text-red-500">{error}</p>}
                {exercisesError && <p className="text-red-500">{exercisesError}</p>}

                {/* Buttons */}
                <div className="flex justify-between">
                    <button type="button" onClick={navigateBackToUserDetailPage} className="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 hover:cursor-pointer">
                        Abbrechen
                    </button>
                    <button type="submit" className="px-4 py-2 bg-[var(--admin-submit-button)] text-white rounded-md hover:bg-blue-700 hover:cursor-pointer" disabled={loading}>
                        {loading ? "Speichern..." : "Neuen Trainingsplan anlegen"}
                    </button>
                </div>
            </form>

            {/* TODO: close deletion dialog when deleting was successful, reload page */}
            {showConfirmDialog && (
                <div className="fixed inset-0 bg-black/70 flex items-center justify-center">
                    <div className="bg-[var(--admin-modal-background)] p-6 rounded-lg shadow-lg">
                        <h2 className="text-xl font-semibold mb-4">Bestätigen Sie das Löschen</h2>
                        <p className="mb-4">Möchten Sie die folgenden Übungen wirklich löschen?</p>
                        <ul className="mb-4">
                            {selectedExercises.map((item) => (
                                <li key={item.id}>{item.generalExerciseId}</li>
                            ))}
                        </ul>
                        <div className="flex justify-end">
                            <button
                                className="bg-[var(--admin-button-background)] text-white px-4 py-2 rounded-lg mr-2 hover:bg-red-600"
                                onClick={() => {
                                    selectedExercises.map((exercise) => handleRemoveExercise(exercise));
                                }}
                            >
                                Löschen
                            </button>
                            <button className="bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600" onClick={() => setShowConfirmDialog(false)}>
                                Abbrechen
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}


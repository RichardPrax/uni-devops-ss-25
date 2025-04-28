import React, { useState } from "react";
import { NewCustomExerciseDTO } from "@/app/entities/NewCustomExerciseDTO";
import FormInput from "@/components/FormInput";
import Modal from "@/app/admin/components/Modal";
import { GeneralExercise } from "@/app/entities/GeneralExercise";

interface AddExerciseModalProps {
    onClose: () => void;
    onAddExercise: (newExercise: NewCustomExerciseDTO) => void;
    generalExercises: GeneralExercise[];
}

export default function AddExerciseModal({ onClose, onAddExercise, generalExercises }: AddExerciseModalProps) {
    const [data, setData] = useState<NewCustomExerciseDTO>({
        generalExerciseId: "",
        sets: 0,
        repetitions: 0,
        durationInMinutes: 0,
        tip: "",
        trainingPlanId: "",
        id: "",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setData({ ...data, [e.target.name]: e.target.value });
    };

    const handleSelectChange = (selected: { value: string; label: string } | null) => {
        if (selected) {
            setData({ ...data, generalExerciseId: selected.value });
        }
    };

    const handleAdd = () => {
        onAddExercise(data);
        onClose();
    };

    return (
        <Modal onClose={onClose}>
            <h2 className="text-xl font-semibold mb-4">Übung hinzufügen</h2>
            <div className="space-y-4">
                {/* General Exercise Select */}
                <div>
                    <label className="block text-white mb-2">Übung auswählen</label>
                    <select
                        value={data.generalExerciseId}
                        onChange={(e) => setData({ ...data, generalExerciseId: e.target.value })}
                        className="w-full p-2 bg-gray-700 text-white border border-gray-500 rounded-md"
                    >
                        <option value="" disabled>
                            Bitte eine Übung auswählen
                        </option>
                        {generalExercises.map((exercise) => (
                            <option key={exercise.id} value={exercise.id}>
                                {exercise.name}
                            </option>
                        ))}
                    </select>
                </div>

                {/* Sets */}
                <FormInput id="sets" label="Sets" type="number" value={data.sets} placeholder="Anzahl der Sätze eingeben" onChange={handleChange} required />

                {/* Reps */}
                <FormInput id="repetitions" label="Reps" type="number" value={data.repetitions} placeholder="Anzahl der Wiederholungen" onChange={handleChange} required />

                {/* Duration */}
                <FormInput
                    id="durationInMinutes"
                    label="Dauer (Minuten)"
                    type="number"
                    value={data.durationInMinutes}
                    placeholder="Dauer in Minuten"
                    onChange={handleChange}
                    required
                />

                {/* Tip */}
                <FormInput id="tip" label="Hinweise" value={data.tip} placeholder="Hinweise eingeben" onChange={handleChange} required />

                <div className="flex justify-end space-x-2">
                    <button type="button" onClick={onClose} className="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">
                        Abbrechen
                    </button>
                    <button type="button" onClick={handleAdd} className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600">
                        Hinzufügen
                    </button>
                </div>
            </div>
        </Modal>
    );
}


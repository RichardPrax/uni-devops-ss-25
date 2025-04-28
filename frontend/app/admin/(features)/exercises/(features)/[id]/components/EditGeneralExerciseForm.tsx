import React, { useState, useEffect } from "react";
import FormInput from "@/components/FormInput";
import { GeneralExercise } from "@/app/entities/GeneralExercise";
import { useUpdateGeneralExercise } from "../../../hooks/useUpdateGeneralExercise";
import CustomSelect from "../../create/components/CustomSelect";
import { NewGeneralExerciseDTO } from "@/app/entities/NewGeneralExerciseDTO";
import CategoryModal from "../../../components/CategoryModal";
import EquipmentModal from "../../../components/EquipmentModal";

interface EditGeneralExerciseFormProps {
    exercise: GeneralExercise;
    onCancel: () => void;
    onUpdateSuccess: () => void;
}

// TODO: should constants be hardcoded here or should they be imported from a API call?
const categoryOptions = [
    { value: "UPPER_BODY", label: "Oberkörper" },
    { value: "LOWER_BODY", label: "Unterkörper" },
    { value: "CORE", label: "Rumpf" },
    { value: "FULL_BODY", label: "Ganzkörper" },
];

const equipmentOptions = [
    { value: "CHAIR", label: "Stuhl" },
    { value: "MAT", label: "Matte" },
    { value: "BENCH", label: "Bank" },
    { value: "BALL", label: "Ball" },
];

export default function EditGeneralExerciseForm({ exercise, onCancel, onUpdateSuccess }: EditGeneralExerciseFormProps) {
    const [formData, setFormData] = useState<GeneralExercise>(exercise);

    const [isCategoryModalOpen, setCategoryModalOpen] = useState(false);
    const [isEquipmentModalOpen, setEquipmentModalOpen] = useState(false);

    const { updateGeneralExercise, loading, error } = useUpdateGeneralExercise();

    useEffect(() => {
        setFormData(exercise);
    }, [exercise]);

    const handleSelectChange = (selectedOptions: any, field: keyof NewGeneralExerciseDTO) => {
        setFormData({ ...formData, [field]: selectedOptions.map((option: any) => option.value) });
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const updatedExercise = await updateGeneralExercise(formData);
        if (updatedExercise) {
            onUpdateSuccess();
        }
    };

    return (
        <form onSubmit={handleSubmit} role="form" className="space-y-4">
            {/* Name */}
            <FormInput id="name" label="Name" value={formData.name} placeholder="Name der Übung eingeben" onChange={handleChange} required />

            {/* Kategorien Auswahl */}
            <div className="flex items-center space-x-4">
                <label className="w-1/8 text-md font-medium text-white">Kategorien</label>
                <CustomSelect
                    options={categoryOptions}
                    value={formData.categories}
                    onChange={(selected) => handleSelectChange(selected, "categories")}
                    onOpenModal={() => setCategoryModalOpen(true)}
                />
            </div>

            {/* Equipment Auswahl */}
            <div className="flex items-center space-x-4">
                <label className="w-1/8 text-md font-medium text-white">Ausrüstung</label>
                <CustomSelect
                    options={equipmentOptions}
                    value={formData.equipment}
                    onChange={(selected) => handleSelectChange(selected, "equipment")}
                    onOpenModal={() => setEquipmentModalOpen(true)}
                />
            </div>

            {/* Kurzbeschreibung */}
            <FormInput
                id="shortDescription"
                label="Kurzbeschreibung"
                value={formData.shortDescription}
                placeholder="Kurzbeschreibung der Übung eingeben"
                onChange={handleChange}
                required
            />

            {/* Langbeschreibung */}
            <FormInput
                id="longDescription"
                label="Langbeschreibung"
                value={formData.longDescription}
                placeholder="Langbeschreibung der Übung eingeben"
                onChange={handleChange}
                required
            />

            {/* Anweisungen */}
            <FormInput id="directions" label="Anweisungen" value={formData.directions} placeholder="Anweisungen zur Übung eingeben" onChange={handleChange} required />

            {/* Video */}
            <FormInput id="video" label="Video URL" type="url" value={formData.video} placeholder="URL des Videos eingeben" onChange={handleChange} required />

            {/* Thumbnail */}
            <FormInput
                id="thumbnailUrl"
                label="Thumbnail URL"
                type="url"
                value={formData.thumbnailUrl}
                placeholder="URL des Thumbnails eingeben"
                onChange={handleChange}
                required
            />

            {/* Category Modal */}
            <CategoryModal
                isOpen={isCategoryModalOpen}
                onClose={() => setCategoryModalOpen(false)}
                categories={formData.categories}
                categoryOptions={categoryOptions}
                onCategoryChange={(newCategories) => setFormData((prev) => ({ ...prev, categories: newCategories }))}
            />

            {/* Equipment Modal */}
            <EquipmentModal
                isOpen={isEquipmentModalOpen}
                onClose={() => setEquipmentModalOpen(false)}
                equipment={formData.equipment}
                equipmentOptions={equipmentOptions}
                onEquipmentChange={(newEquipment) => setFormData((prev) => ({ ...prev, equipment: newEquipment }))}
            />

            {/* Fehleranzeige */}
            {error && <p className="text-red-500">{error}</p>}

            <div className="flex justify-between">
                <button type="button" onClick={onCancel} className="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">
                    Abbrechen
                </button>
                <button type="submit" className="px-4 py-2 bg-[var(--admin-submit-button)] text-white rounded-md hover:bg-blue-700" disabled={loading}>
                    {loading ? "Speichern..." : "Änderungen speichern"}
                </button>
            </div>
        </form>
    );
}

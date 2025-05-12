import React, { useState } from "react";
import { useRouter } from "next/navigation";

import { NewGeneralExerciseDTO } from "@/app/entities/NewGeneralExerciseDTO";
import FormInput from "@/components/FormInput";

import CustomSelect from "./CustomSelect";
import { useCreateGeneralExercise } from "../../../hooks/useCreateGeneralExercise";
import CategoryModal from "../../../components/CategoryModal";
import EquipmentModal from "../../../components/EquipmentModal";

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

export default function CreateGeneralExerciseForm() {
    const [formData, setFormData] = useState<NewGeneralExerciseDTO>({
        name: "",
        categories: [],
        equipment: [],
        shortDescription: "",
        longDescription: "",
        directions: "",
        video: "",
        thumbnailUrl: "",
    });

    const { createGeneralExercise, loading, error } = useCreateGeneralExercise();
    const router = useRouter();

    const [isCategoryModalOpen, setCategoryModalOpen] = useState(false);
    const [isEquipmentModalOpen, setEquipmentModalOpen] = useState(false);

    const handleSelectChange = (selectedOptions: { value: string; label: string }[] | null, field: keyof NewGeneralExerciseDTO) => {
            setFormData({
                ...formData,
                [field]: selectedOptions ? selectedOptions.map((option) => option.value) : [],
            });
        };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const newExercise = await createGeneralExercise(formData);
        if (newExercise) {
            router.push("/admin/exercises");
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
                    onChange={(selected) => handleSelectChange(selected as { value: string; label: string }[] | null, "categories")}
                    onOpenModal={() => setCategoryModalOpen(true)}
                />
            </div>

            {/* Equipment Auswahl */}
            <div className="flex items-center space-x-4">
                <label className="w-1/8 text-md font-medium text-white">Ausrüstung</label>
                <CustomSelect
                    options={equipmentOptions}
                    value={formData.equipment}
                    onChange={(selected) => handleSelectChange(selected as { value: string; label: string }[] | null, "equipment")}
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
                textarea
            />

            {/* Lange Beschreibung */}
            <FormInput
                id="longDescription"
                label="Lange Beschreibung"
                value={formData.longDescription}
                placeholder="Lange Beschreibung der Übung eingeben"
                onChange={handleChange}
                required
                textarea
            />

            {/* Anweisungen */}
            <FormInput id="directions" label="Anweisungen" value={formData.directions} placeholder="Anweisungen zur Übung eingeben" onChange={handleChange} required textarea />

            {/* Video */}
            <FormInput id="video" label="Video" type="url" value={formData.video} placeholder="Video URL der Übung eingeben" onChange={handleChange} required />

            {/* Thumbnail */}
            <FormInput
                id="thumbnailUrl"
                label="Thumbnail"
                type="url"
                value={formData.thumbnailUrl}
                placeholder="Thumbnail URL der Übung eingeben"
                onChange={handleChange}
                required
            />

            {/* Fehleranzeige */}
            {error && <p className="text-red-500">{error}</p>}

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

            {/* Buttons */}
            <div className="flex justify-between">
                <button
                    type="button"
                    onClick={() => router.push("/admin/exercises")}
                    className="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 hover:cursor-pointer"
                >
                    Abbrechen
                </button>
                <button type="submit" className="px-4 py-2 bg-[var(--admin-submit-button)] text-white rounded-md hover:bg-blue-700 hover:cursor-pointer" disabled={loading}>
                    {loading ? "Speichern..." : "Neue Übung anlegen"}
                </button>
            </div>
        </form>
    );
}


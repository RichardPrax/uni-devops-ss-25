import React, { useState, useEffect } from "react";
import { useUpdateUser } from "@/app/admin/(features)/users/hooks/useUpdateUser";
import { User } from "@/app/entities/User";
import FormInput from "@/components/FormInput";

// TODO: change styling, use InputField component
interface EditUserFormProps {
    user: User;
    onCancel: () => void;
    onUpdateSuccess: () => void;
}

export default function EditUserForm({ user, onCancel, onUpdateSuccess }: EditUserFormProps) {
    const [formData, setFormData] = useState<User>(user);

    const { updateUser, loading, error } = useUpdateUser();

    useEffect(() => {
        setFormData(user);
    }, [user]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const updatedUser = await updateUser(formData);
        if (updatedUser) {
            onUpdateSuccess();
        }
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            {/* Vorname */}
            <FormInput id="firstName" label="Vorname" value={formData.firstName} placeholder="Vorname des Kunden eingeben" onChange={handleChange} required />

            {/* Nachname */}
            <FormInput id="lastName" label="Nachname" value={formData.lastName} placeholder="Nachname des Kunden eingeben" onChange={handleChange} required />

            {/* Geburtsdatum */}
            <FormInput id="birthdate" label="Geburtsdatum" type="date" value={formData.birthdate} placeholder="Geburtsdatum des Kunden eingeben" onChange={handleChange} required />

            {/* Email */}
            <FormInput id="email" label="Email" type="email" value={formData.email} placeholder="Email des Kunden eingeben" onChange={handleChange} required />

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

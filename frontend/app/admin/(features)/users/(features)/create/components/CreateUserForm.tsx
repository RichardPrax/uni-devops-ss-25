import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { useCreateUser } from "../../../hooks/useCreateUser";
import { NewUserDTO } from "@/app/entities/NewUserDTO";
import FormInput from "@/components/FormInput";

export default function CreateUserForm() {
    const [formData, setFormData] = useState<NewUserDTO>({
        firstName: "",
        lastName: "",
        birthdate: "",
        email: "",
        password: "",
    });

    const { createUser, loading, error } = useCreateUser();
    const router = useRouter();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const newUser = await createUser(formData);
        if (newUser) {
            router.push("/admin/users");
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

            {/* Passwort */}
            <FormInput id="password" label="Passwort" type="password" value={formData.password} placeholder="Initiales Passwort setzen" onChange={handleChange} required />

            {/* Fehleranzeige */}
            {error && <p className="text-red-500">{error}</p>}

            {/* Buttons */}
            <div className="flex justify-between">
                <button type="button" onClick={() => router.push("/admin/users")} className="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">
                    Abbrechen
                </button>
                <button type="submit" className="px-4 py-2 bg-[var(--admin-submit-button)] text-white rounded-md hover:bg-blue-700" disabled={loading}>
                    {loading ? "Speichern..." : "Neuen Kunden anlegen"}
                </button>
            </div>
        </form>
    );
}

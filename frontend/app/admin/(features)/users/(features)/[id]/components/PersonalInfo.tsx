import { User } from "@/app/entities/User";
import React from "react";

interface PersonalInfoProps {
    user: User;
    onEdit: () => void;
}

export default function PersonalInfo({ user, onEdit }: PersonalInfoProps) {
    return (
        <div className="bg-[var(--admin-card-background)] p-6 rounded-lg shadow-md flex flex-col border-[var(--admin-card-border)] border">
            <div>
                <div className="flex justify-between items-center mb-4">
                    <h2 className="text-2xl font-semibold mb-4">Persönliche Informationen</h2>
                    {/* Buttons */}
                    <div>
                        <button className="px-4 py-2 bg-[var(--admin-button-background)] text-white rounded-md hover:bg-blue-700" onClick={onEdit}>
                            Bearbeiten
                        </button>
                    </div>
                </div>
                <div className="mb-4">
                    <strong>Name:</strong> {user.firstName + " " + user.lastName}
                </div>
                <div className="mb-4">
                    <strong>Email:</strong> {user.email}
                </div>
                <div className="mb-4">
                    <strong>Geburtstag:</strong> {user.birthdate}
                </div>
            </div>
        </div>
    );
}

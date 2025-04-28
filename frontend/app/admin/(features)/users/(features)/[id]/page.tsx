"use client";

import React, { useState } from "react";
import { useRouter, useParams } from "next/navigation";
import { useFetchUser } from "../../hooks/useFetchUser";
import { useFetchTrainingPlans } from "../../hooks/useFetchTrainingPlans";
import PersonalInfo from "./components/PersonalInfo";
import EditUserForm from "./components/EditUserForm";
import TrainingPlans from "./components/TrainingPlans";
import Sessions from "./components/Sessions";
import { useFetchSessions } from "../../hooks/useFetchSessions";

export default function UserDetailPage() {
    const { id } = useParams() as { id: string };
    const { user, loading: userLoading, error: userError, refetch: refetchUser } = useFetchUser(id);
    const { trainingPlans, loading: plansLoading, error: plansError, refetch: refetchPlans } = useFetchTrainingPlans(id);
    const { sessions, loading: sessionsLoading, error: sessionsError, refetch: refetchSessions } = useFetchSessions(id);
    const [isEditing, setIsEditing] = useState(false);
    const router = useRouter();

    if (userLoading || plansLoading || sessionsLoading) return <div>Loading...</div>;
    if (userError) return <div>Error: {userError}</div>;
    if (plansError) return <div>Error: {plansError}</div>;
    if (sessionsError) return <div>Error: {sessionsError}</div>;

    const toggleEdit = () => {
        setIsEditing(!isEditing);
    };

    const handleUpdateSuccess = () => {
        refetchUser();
        setIsEditing(false);
    };

    return (
        user && (
            <div className="p-8">
                <h1 className="text-4xl font-bold mb-8">{user.firstName + " " + user.lastName}</h1>
                {isEditing ? (
                    <div className="mx-auto mt-10 p-6 rounded-lg shadow-lg bg-[var(--admin-card-background)] text-white border border-[var(--admin-card-border)]">
                        <h1 className="text-2xl font-bold mb-4">Persönliche Informationen bearbeiten</h1>
                        <EditUserForm user={user} onCancel={toggleEdit} onUpdateSuccess={handleUpdateSuccess} />
                    </div>
                ) : (
                    <>
                        <PersonalInfo user={user} onEdit={toggleEdit} />
                        <TrainingPlans plans={trainingPlans} />
                        <Sessions sessions={sessions} />
                        <button className="mt-4 px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700" onClick={() => router.push("/admin/users")}>
                            Zurück
                        </button>
                    </>
                )}
            </div>
        )
    );
}

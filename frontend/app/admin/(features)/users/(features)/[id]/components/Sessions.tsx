import { Session } from "@/app/entities/Session";
import React, { useState } from "react";
import SessionsTable from "./SessionTable";
import SearchBar from "@/components/SearchBar";

interface SessionsProps {
    sessions: Session[];
}

export default function Sessions({ sessions }: SessionsProps) {
    const [dateFilter, setDateFilter] = useState("");
    const [planFilter, setPlanFilter] = useState("");

    const filteredSessions = sessions.filter((session) => session.trainingPlan.name.toLowerCase().includes(planFilter.toLowerCase()) && session.date.includes(dateFilter));

    return (
        <div className="py-6">
            {/* Filterbereich */}
            <div className="flex space-x-4 mb-4">
                <SearchBar searchTerm={dateFilter} setSearchTerm={setDateFilter} placeholder="Seach by date" />
                <SearchBar searchTerm={planFilter} setSearchTerm={setPlanFilter} placeholder="Seach by training plan" />
                <button
                    className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600"
                    onClick={() => {
                        setDateFilter("");
                        setPlanFilter("");
                    }}
                >
                    Reset Filter
                </button>
            </div>

            <div className="mt-8 p-6 bg-[var(--admin-card-background)] rounded-lg shadow-lg border border-[var(--admin-card-border)]">
                <div className="flex justify-between items-center mb-4">
                    <h2 className="text-2xl font-semibold">Trainingseinheiten</h2>
                    <div>
                        <button
                            className="bg-[var(--admin-button-background)] text-white px-4 py-2 rounded-lg mr-2 hover:bg-blue-600"
                            onClick={() => alert("Noch nicht implementiert!")}
                        >
                            Hinzufügen
                        </button>
                        <button className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600" onClick={() => alert("Noch nicht implementiert!")}>
                            Entfernen
                        </button>
                    </div>
                </div>

                <SessionsTable sessions={filteredSessions} />
            </div>
        </div>
    );
}

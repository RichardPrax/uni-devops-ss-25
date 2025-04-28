import { TrainingPlan } from "@/app/entities/TrainingPlan";
import React, { useState } from "react";
import TrainingPlansTable from "./TrainingPlanTable";
import SearchBar from "@/components/SearchBar";
import { useParams, useRouter } from "next/navigation";

interface TrainingPlansProps {
    plans: TrainingPlan[];
}

export default function TrainingPlans({ plans }: TrainingPlansProps) {
    const [search, setSearch] = useState("");
    const { id } = useParams() as { id: string };
    const router = useRouter();

    const filteredPlans = plans.filter((plan) => plan.name.toLowerCase().includes(search.toLowerCase()));

    return (
        <div className="py-6">
            {/* Suchfeld */}
            <SearchBar searchTerm={search} setSearchTerm={setSearch} />

            <div className="mt-8 p-6 bg-[var(--admin-card-background)] rounded-lg shadow-lg border border-[var(--admin-card-border)]">
                <div className="flex justify-between items-center mb-4">
                    <h2 className="text-2xl font-semibold">Trainingspläne</h2>
                    <div>
                        <button
                            className="bg-[var(--admin-button-background)] text-white px-4 py-2 rounded-lg mr-2 hover:bg-blue-600"
                            onClick={() => {
                                router.push(`/admin/users/${id}/trainingPlan/create`);
                            }}
                        >
                            Hinzufügen
                        </button>
                        <button
                            className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600"
                            onClick={() => {
                                alert("Noch nicht implementiert!");
                            }}
                        >
                            Entfernen
                        </button>
                    </div>
                </div>

                {/* Separater Table-Header für rundes Design */}
                <div className="bg-[var(--admin-card-header-background)] text-white rounded-lg p-4 flex">
                    <span className="w-1/5 font-semibold text-left px-6">Name</span>
                    <span className="w-1/5 font-semibold text-left px-6">Anzahl Übungen</span>
                    <span className="w-3/5 font-semibold text-left px-6">Beschreibung</span>
                </div>

                {/* Table Content */}
                <TrainingPlansTable plans={filteredPlans} />
            </div>
        </div>
    );
}


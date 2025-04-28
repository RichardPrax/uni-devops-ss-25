import { Session } from "@/app/entities/Session";
import React from "react";

interface SessionsTableProps {
    sessions: Session[];
}

export default function SessionsTable({ sessions }: SessionsTableProps) {
    return (
        <div className="overflow-hidden rounded-b-lg">
            <table className="min-w-full text-white">
                <thead>
                    <tr className="bg-[var(--admin-card-header-background)] text-white">
                        <th className="w-1/5 px-6 py-3 text-left">Datum</th>
                        <th className="w-1/5 px-6 py-3 text-left">Uhrzeit</th>
                        <th className="w-1/5 px-6 py-3 text-left">Trainingsplan</th>
                        <th className="w-2/5 px-6 py-3 text-left">Bemerkungen</th>
                        <th className="w-1/5 px-6 py-3 text-left">Aktion</th>
                    </tr>
                </thead>
                <tbody>
                    {sessions.map((session) => (
                        <tr key={session.id} className="border-b border-gray-700 hover:bg-gray-700">
                            <td className="w-1/5 px-6 py-3 text-left">{session.date}</td>
                            <td className="w-1/5 px-6 py-3 text-left">{session.date}</td>
                            <td className="w-1/5 px-6 py-3 text-left">{session.trainingPlan.name}</td>
                            <td className="w-2/5 px-6 py-3 text-left">{session.notes}</td>
                            <td className="w-1/5 px-6 py-3 text-left flex">
                                <button className="bg-blue-500 text-white px-3 py-1 rounded-lg mr-2 hover:bg-blue-600">✏️</button>
                                <button className="bg-red-500 text-white px-3 py-1 rounded-lg hover:bg-red-600">🗑️</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

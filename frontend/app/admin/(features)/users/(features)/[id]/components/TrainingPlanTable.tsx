import React from "react";
import { TrainingPlan } from "@/app/entities/TrainingPlan";

interface TrainingPlansTableProps {
    plans: TrainingPlan[];
}

export default function TrainingPlansTable({ plans }: TrainingPlansTableProps) {
    return (
        <div className="overflow-hidden rounded-b-lg">
            <table className="min-w-full text-white">
                <tbody>
                    {plans.map((plan) => (
                        <tr key={plan.id} className="border-b border-gray-700 hover:bg-gray-700">
                            <td className="w-1/5 px-6 py-3 text-left">{plan.name}</td>
                            <td className="w-1/5 px-6 py-3 text-left">{plan.exercises.length}</td>
                            <td className="w-3/5 px-6 py-3 text-left">{plan.shortDescription}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

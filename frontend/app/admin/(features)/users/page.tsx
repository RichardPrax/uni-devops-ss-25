"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";

import SearchBar from "@/components/SearchBar";

import { UserTable } from "./components/UserTable";
import { useFetchUsers } from "./hooks/useFetchUsers";

export default function Page() {
    const [searchTerm, setSearchTerm] = useState("");
    const { users, loading, error } = useFetchUsers();
    const router = useRouter();

    const filteredUsers = users.filter(
        (user) =>
            user.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
            user.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
            user.email.toLowerCase().includes(searchTerm.toLowerCase())
    );

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div>
            <h1 className="text-3xl font-bold mb-4">Kundenübersicht</h1>

            <div className="flex justify-between mb-4">
                <SearchBar searchTerm={searchTerm} setSearchTerm={setSearchTerm} />
                <button className="px-4 py-2 bg-[var(--admin-button-background)] text-white rounded-md hover:bg-blue-700" onClick={() => router.push("/admin/users/create")}>
                    Hinzufügen
                </button>
            </div>

            <UserTable users={filteredUsers} />
        </div>
    );
}

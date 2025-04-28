import React from "react";

interface SearchBarProps {
    searchTerm: string;
    setSearchTerm: (searchTerm: string) => void;
    placeholder?: string;
}

export default function SearchBar({ searchTerm, setSearchTerm, placeholder = "Search" }: SearchBarProps) {
    return (
        <div className="relative w-1/5">
            <input
                type="text"
                placeholder={placeholder}
                className="w-full p-2 pl-10 bg-[var(--admin-card-header-background)] text-white rounded-lg outline-none border-[var(--admin-search-input-border)] border"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
            />
            <span className="absolute left-3 top-2 text-gray-400">🔍</span>
        </div>
    );
}

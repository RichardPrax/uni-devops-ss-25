import React from "react";

import Modal from "@/app/admin/components/Modal";

interface CategoryModalProps {
    isOpen: boolean;
    onClose: () => void;
    categories: string[];
    categoryOptions: { value: string; label: string }[];
    onCategoryChange: (newCategories: string[]) => void;
}

export default function CategoryModal({ isOpen, onClose, categories, categoryOptions, onCategoryChange }: CategoryModalProps) {
    if (!isOpen) return null;

    const handleCheckboxChange = (value: string) => {
        const newCategories = categories.includes(value) ? categories.filter((cat) => cat !== value) : [...categories, value];
        onCategoryChange(newCategories);
    };

    return (
        <Modal onClose={onClose}>
            <h2 className="text-white">Kategorien auswählen</h2>
            {categoryOptions.map((option) => {
                const inputId = `category-${option.value}`;
                return (
                    <div key={option.value} className="flex items-center">
                        <input id={inputId} type="checkbox" checked={categories.includes(option.value)} onChange={() => handleCheckboxChange(option.value)} />
                        <label htmlFor={inputId} className="text-white ml-2">
                            {option.label}
                        </label>
                    </div>
                );
            })}
        </Modal>
    );
}

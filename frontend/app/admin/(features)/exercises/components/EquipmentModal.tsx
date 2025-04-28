import React from "react";
import Modal from "@/app/admin/components/Modal";

interface EquipmentModalProps {
    isOpen: boolean;
    onClose: () => void;
    equipment: string[];
    equipmentOptions: { value: string; label: string }[];
    onEquipmentChange: (newEquipment: string[]) => void;
}

export default function EquipmentModal({ isOpen, onClose, equipment, equipmentOptions, onEquipmentChange }: EquipmentModalProps) {
    if (!isOpen) return null;

    const handleCheckboxChange = (value: string) => {
        const newEquipment = equipment.includes(value) ? equipment.filter((eq) => eq !== value) : [...equipment, value];
        onEquipmentChange(newEquipment);
    };

    return (
        <Modal onClose={onClose}>
            <h2 className="text-white">Ausrüstung auswählen</h2>
            {equipmentOptions.map((option) => {
                const inputId = `equipment-${option.value}`;
                return (
                    <div key={option.value} className="flex items-center">
                        <input id={inputId} type="checkbox" checked={equipment.includes(option.value)} onChange={() => handleCheckboxChange(option.value)} />
                        <label htmlFor={inputId} className="text-white ml-2">
                            {option.label}
                        </label>
                    </div>
                );
            })}
        </Modal>
    );
}

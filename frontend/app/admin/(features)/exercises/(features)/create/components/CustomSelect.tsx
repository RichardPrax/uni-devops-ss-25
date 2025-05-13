import React from "react";
import Select, { MultiValue, SingleValue } from "react-select";

interface Option {
    value: string;
    label: string;
}

interface CustomSelectProps {
    options: Option[];
    value: string[];
    onChange: (selected: MultiValue<Option> | SingleValue<Option>) => void;
    onOpenModal: () => void;
}

export default function CustomSelect({ options, value, onChange, onOpenModal }: CustomSelectProps) {
    return (
        <div className="flex items-center w-7/8 space-x-2">
            <Select
                isMulti
                options={options}
                value={options.filter((option) => value.includes(option.value))}
                onChange={onChange}
                className="flex-grow"
                styles={{
                    control: (base) => ({
                        ...base,
                        backgroundColor: "var(--admin-form-input-background)",
                        borderColor: "var(--admin-form-input-border)",
                        color: "white",
                    }),
                    menu: (base) => ({
                        ...base,
                        backgroundColor: "var(--admin-form-input-background)",
                        color: "white",
                    }),
                    option: (base, state) => ({
                        ...base,
                        backgroundColor: state.isSelected ? "var(--admin-form-input-border-focus)" : "var(--admin-form-input-background)",
                        color: "white",
                        ":hover": {
                            backgroundColor: "var(--admin-form-input-border-focus)",
                            color: "white",
                        },
                    }),
                    multiValue: (base) => ({
                        ...base,
                        backgroundColor: "var(--admin-button-background)",
                    }),
                    multiValueLabel: (base) => ({
                        ...base,
                        color: "white",
                    }),
                    multiValueRemove: (base) => ({
                        ...base,
                        color: "white",
                        ":hover": {
                            backgroundColor: "var(--admin-form-input-border-focus)",
                            color: "white",
                        },
                    }),
                }}
            />
            <button type="button" onClick={onOpenModal} className="bg-blue-500 p-2 rounded text-white">
                🔍
            </button>
        </div>
    );
}


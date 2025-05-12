import React from "react";

interface FormInputProps {
    id: string;
    label: string;
    type?: string;
    value?: string;
    placeholder: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
    required?: boolean;
    textarea?: boolean;
}

export default function FormInput({ id, label, type = "text", value, placeholder, onChange, required = false, textarea = false }: FormInputProps) {
    return (
        <div className="flex items-center space-x-4">
            <label htmlFor={id} className="block text-md font-medium text-white w-1/8">
                {label}
            </label>
            {textarea ? (
                <textarea id={id} name={id} placeholder={placeholder} value={value} onChange={onChange} className="form-input" required={required} />
            ) : (
                <input type={type} id={id} name={id} placeholder={placeholder} value={value} onChange={onChange} className="form-input" required={required} />
            )}
        </div>
    );
}


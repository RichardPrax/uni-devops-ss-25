import React from "react";

interface ModalProps {
    children: React.ReactNode;
    onClose: () => void;
}

export default function Modal({ children, onClose }: ModalProps) {
    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black/50 z-50">
            <div className="bg-[var(--admin-modal-background)] p-6 rounded-lg shadow-lg w-96 relative">
                <button onClick={onClose} className="absolute top-2 right-2 text-white text-xl">
                    ✖
                </button>
                <div className="text-white">{children}</div>
            </div>
        </div>
    );
}

import React from "react";

// TODO: is it smart to use any?
interface ConfirmDeletionDialogProps {
    setShowConfirmDialog: (show: boolean) => void;
    selectedEntities: any[];
    handleDelete: () => void;
}

// NOTE: we can use item.name here because every entity has a name property
export default function ConfirmDeletionDialog({ setShowConfirmDialog, selectedEntities, handleDelete }: ConfirmDeletionDialogProps) {
    return (
        <div className="fixed inset-0 bg-black/70 flex items-center justify-center">
            <div className="bg-[var(--admin-modal-background)] p-6 rounded-lg shadow-lg">
                <h2 className="text-xl font-semibold mb-4">Bestätigen Sie das Löschen</h2>
                <p className="mb-4">Möchten Sie die folgenden Übungen wirklich löschen?</p>
                <ul className="mb-4">
                    {selectedEntities.map((item) => (
                        <li key={item.id}>{item.name}</li>
                    ))}
                </ul>
                <div className="flex justify-end">
                    <button className="bg-[var(--admin-button-background)] text-white px-4 py-2 rounded-lg mr-2 hover:bg-red-600" onClick={handleDelete}>
                        Löschen
                    </button>
                    <button className="bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600" onClick={() => setShowConfirmDialog(false)}>
                        Abbrechen
                    </button>
                </div>
            </div>
        </div>
    );
}

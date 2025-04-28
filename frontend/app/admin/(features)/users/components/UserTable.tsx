import React from "react";
import { useRouter } from "next/navigation";
import { User } from "@/app/entities/User";

interface UserTableProps {
    users: User[];
}

export const UserTable: React.FC<UserTableProps> = ({ users }) => {
    const router = useRouter();

    const handleRowClick = (userId: string) => {
        router.push(`/admin/users/${userId}`);
    };

    return (
        <table className="min-w-full table-auto border-collapse rounded-lg overflow-hidden">
            <thead>
                <tr className="bg-[#323D4E] text-white">
                    <th className="px-4 py-2 text-left">Vorname</th>
                    <th className="px-4 py-2 text-left">Nachname</th>
                    <th className="px-4 py-2 text-left">Email</th>
                    <th className="px-4 py-2 text-left">Geburtstag</th>
                </tr>
            </thead>
            <tbody>
                {users.map((user, index) => (
                    <tr key={index} className="border-[#979797] border-b bg-[#273142] cursor-pointer" onClick={() => handleRowClick(user.id)}>
                        <td className="px-4 py-2">{user.firstName}</td>
                        <td className="px-4 py-2">{user.lastName}</td>
                        <td className="px-4 py-2">{user.email}</td>
                        <td className="px-4 py-2">{user.birthdate}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

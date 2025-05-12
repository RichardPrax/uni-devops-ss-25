import { useState } from "react";

import { UpdateUserDTO } from "@/app/entities/UpdateUserDTO";
import { User } from "@/app/entities/User";
import { apiFetch } from "@/lib/api";

export function useUpdateUser() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const updateUser = async (userData: User) => {
        setLoading(true);
        setError(null);

        // create new user data object because API expects different format => no password when updating user
        const newUserData: UpdateUserDTO = {
            firstName: userData.firstName,
            lastName: userData.lastName,
            email: userData.email,
            birthdate: userData.birthdate,
        };

        try {
            return await apiFetch<User>(`http://localhost:8080/api/v1/users/${userData.id}`, {
                method: "PUT",
                body: JSON.stringify(newUserData),
            });
        } catch (error: any) {
            setError(error.message);
            return null;
        } finally {
            setLoading(false);
        }
    };

    return { updateUser, loading, error };
}

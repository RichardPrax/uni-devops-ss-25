"use client";

import Image from "next/image";
import { useFetchUser } from "../(features)/users/hooks/useFetchUser";

const Topbar = () => {
    const id = localStorage.getItem("id");
    // user is not logged in correctly
    if (!id) return null;
    const { user, loading, error, refetch } = useFetchUser(id);

    return (
        user && (
            <header className="bg-[#273142] px-6 py-4 flex justify-between items-center">
                {/* Suchfeld */}
                <div className="flex-1 max-w-lg">
                    <input type="text" placeholder="Search" className="w-full p-2 bg-[#323D4E] border border-gray-700 rounded-lg" />
                </div>

                {/* Profil */}
                <div className="flex items-center gap-4">
                    <div className="relative w-10 h-10">
                        <Image src="/dummy_pb.jpg" alt="Admin Profile" layout="fill" className="rounded-full" />
                    </div>
                    <span className="text-sm">{user.firstName + " " + user.lastName}</span>
                </div>
            </header>
        )
    );
};

export default Topbar;

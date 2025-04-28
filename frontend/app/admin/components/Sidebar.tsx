"use client";
import Link from "next/link";
import Image from "next/image";
import { usePathname } from "next/navigation";

const Sidebar = () => {
    const pathname = usePathname();

    const navItems = [
        { name: "Dashboard", href: "/admin" },
        { name: "Kunden", href: "/admin/users" },
        { name: "Übungskatalog", href: "/admin/exercises" },
    ];

    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("id");
        window.location.href = "/login";
    };

    return (
        <aside className="w-64 bg-[#273142] p-6 flex flex-col justify-between h-full">
            {/* Logo */}
            <div>
                <div className="flex items-center mb-6 justify-center">
                    <Image src="/logo_invert.png" alt="Logo" width={150} height={150} />
                </div>

                {/* Navigation */}
                <nav className="space-y-2">
                    {navItems.map((item) => (
                        <Link
                            key={item.href}
                            href={item.href}
                            className={`block px-4 py-2 rounded-md text-lg font-extralight text-left ${pathname === item.href ? "bg-[#4880FF]" : "hover:bg-gray-700"}`}
                        >
                            {item.name}
                        </Link>
                    ))}
                </nav>
            </div>

            {/* Divider zwischen Navigation und Einstellungen/Logout */}

            {/* Einstellungen und Logout */}
            <div className="space-y-2">
                <div className="border-t border-gray-600 w-full"></div>

                <Link href="/admin/settings" className="block px-4 py-2 text-lg text-left hover:bg-gray-700 rounded-md">
                    Einstellungen
                </Link>
                <button className="block w-full text-left px-4 py-2 text-lg hover:bg-gray-700 rounded-md" onClick={logout}>
                    Abmelden
                </button>
            </div>
        </aside>
    );
};

export default Sidebar;

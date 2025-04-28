"use client";

import { useState } from "react";
import Image from "next/image";
import Link from "next/link";
import { Menu, X } from "lucide-react";

export default function Navbar() {
    const [isOpen, setIsOpen] = useState(false);

    return (
        <header className="bg-white text-[var(--primary-color)] shadow-md sticky top-0 left-0 w-full z-50">
            <div className="container mx-auto flex justify-between items-center p-4">
                {/* Logo mit responsiver Größe */}
                <Image src="/logo.png" alt="Körperschmiede Logo" width={221} height={189} className="w-40 sm:w-48 md:w-56 lg:w-64 xl:w-72" />

                {/* Hamburger Button für Mobile */}
                <button className="lg:hidden text-[var(--secondary-color)] relative z-50" onClick={() => setIsOpen(!isOpen)}>
                    {isOpen ? <X size={28} /> : <Menu size={28} />}
                </button>

                {/* Navigation (Desktop: sichtbar, Mobile: versteckt) */}
                <nav className="lg:flex hidden gap-6 text-[20px]">
                    <ul className="flex space-x-5">
                        {["START", "LEISTUNGEN", "ÜBER UNS", "TEAM", "KONTAKT", "JOBS"].map((item) => (
                            <li key={item}>
                                <Link href="#" className="hover:underline">
                                    {item}
                                </Link>
                            </li>
                        ))}
                        <li>
                            <Link href="#" className="bg-[var(--primary-color)] text-white px-3 py-1 rounded-lg font-semibold">
                                Kurse
                            </Link>
                        </li>
                    </ul>
                </nav>
            </div>

            {/* Mobile Navigation (nur sichtbar, wenn `isOpen === true`) */}
            {isOpen && (
                <nav className="lg:hidden bg-white absolute top-16 left-0 w-full p-4 shadow-md">
                    <ul className="flex flex-col space-y-4 text-center">
                        {["START", "LEISTUNGEN", "ÜBER UNS", "TEAM", "KONTAKT", "JOBS"].map((item) => (
                            <li key={item}>
                                <Link href="#" className="hover:underline block py-2">
                                    {item}
                                </Link>
                            </li>
                        ))}
                        <li>
                            <Link href="#" className="bg-[var(--secondary-color)] text-white px-3 py-2 rounded-lg block">
                                Kurse
                            </Link>
                        </li>
                    </ul>
                </nav>
            )}
        </header>
    );
}

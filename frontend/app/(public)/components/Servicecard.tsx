import Image from "next/image";
import React from "react";

interface ServiceCardProps {
    title: string;
    img: string;
}

export default function ServiceCard({ title, img }: ServiceCardProps) {
    return (
        <div className="relative w-64 h-40 rounded-lg overflow-hidden bg-gray-300">
            <Image src={img} alt={title} layout="fill" className="object-cover opacity-80" />
            <div className="absolute inset-0 flex flex-col items-center justify-center text-white font-semibold">
                <h3 className="text-xl">{title}</h3>
                <button className="mt-2 px-3 py-1 bg-black bg-opacity-50 rounded text-sm">Mehr erfahren</button>
            </div>
        </div>
    );
}


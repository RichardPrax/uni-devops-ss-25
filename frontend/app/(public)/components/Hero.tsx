import Image from "next/image";

export default function Hero() {
    return (
        <section className="relative bg-white px-6 py-24 text-center">
            {/* Abgedunkelter Hintergrund */}
            <div className="absolute inset-0 bg-black"></div>

            {/* Bild als Hintergrund */}
            <div className="absolute inset-0 opacity-50 z-0">
                <Image src="/hero_image.jpg" alt="Körperschmiede Team" layout="fill" objectFit="cover" />
            </div>

            {/* Textinhalt */}
            <div className="relative text-left max-w-3xl z-10">
                <h1 className="text-4xl sm:text-5xl md:text-6xl font-extrabold text-white leading-tight">Körperschmiede</h1>
                <h2 className="text-xl sm:text-2xl md:text-3xl font-semibold mt-4 text-white">Deine Gesundheit. Deine Stärke.</h2>
                <p className="text-base sm:text-lg md:text-xl text-white mt-6 leading-relaxed">
                    In der Körperschmiede vereinen wir moderne Physiotherapie mit individuell abgestimmten Behandlungsmethoden.
                </p>
            </div>
        </section>
    );
}

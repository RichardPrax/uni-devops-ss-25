import ServiceCard from "./Servicecard";

const services = [
    { title: "Physiotherapie", img: "/physiotherapie.jpg" },
    { title: "Ergotherapie", img: "/ergotherapie.jpg" },
    { title: "Massage", img: "/massage.jpg" },
];

export default function Services() {
    return (
        <section className="bg-[#c4a379] px-10 py-16 text-center">
            <h2 className="text-3xl font-semibold text-gray-900">Unsere Leistungen</h2>
            <div className="mt-8 flex gap-6 justify-center">
                {services.map((service, index) => (
                    <ServiceCard key={index} {...service} />
                ))}
            </div>
            <button className="mt-6 px-6 py-3 bg-green-500 text-white rounded-lg font-medium">Alle Leistungen im Überblick →</button>
        </section>
    );
}

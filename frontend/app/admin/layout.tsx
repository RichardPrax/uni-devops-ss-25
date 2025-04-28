import Sidebar from "./components/Sidebar";
import Topbar from "./components/Topbar";

export default function AdminLayout({ children }: { children: React.ReactNode }) {
    return (
        <div className="flex h-screen bg-[#1B2431] text-white">
            {/* Sidebar links */}
            <div className="sticky top-0 h-screen flex-shrink-0">
                <Sidebar />
            </div>

            {/* Hauptinhalt */}
            <div className="flex-1 flex flex-col overflow-hidden">
                {/* Obere Navigationsleiste */}
                <Topbar />

                {/* Seiteninhalt */}
                <main className="p-6 flex-1 overflow-auto">{children}</main>
            </div>
        </div>
    );
}

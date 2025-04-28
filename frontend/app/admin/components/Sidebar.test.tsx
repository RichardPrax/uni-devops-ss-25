import { render, screen } from "@testing-library/react";
import { usePathname } from "next/navigation";
import Sidebar from "./Sidebar";

jest.mock("next/navigation", () => ({
    usePathname: jest.fn(),
}));

jest.mock("next/router", () => ({
    useRouter: jest.fn(),
}));

describe("Sidebar Component", () => {
    beforeEach(() => {
        (usePathname as jest.Mock).mockReturnValue("/admin");
    });

    test("renders the Sidebar with navigation links", () => {
        render(<Sidebar />);

        expect(screen.getByAltText("Logo")).toBeInTheDocument();
        expect(screen.getByText("Dashboard")).toBeInTheDocument();
        expect(screen.getByText("Kunden")).toBeInTheDocument();
        expect(screen.getByText("Übungskatalog")).toBeInTheDocument();
        expect(screen.getByText("Einstellungen")).toBeInTheDocument();
        expect(screen.getByText("Abmelden")).toBeInTheDocument();
    });

    test("highlights active page", () => {
        render(<Sidebar />);
        const activeLink = screen.getByText("Dashboard");
        expect(activeLink).toHaveClass("bg-[#4880FF]");
    });
});

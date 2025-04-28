import { render, screen, waitFor } from "@testing-library/react";
import GeneralExercisePage from "./page";
import { useFetchGeneralExercises } from "./hooks/useFetchGeneralExercises";

jest.mock("next/navigation", () => ({
    useRouter: jest.fn(),
    usePathname: jest.fn(() => "/admin/exercises"),
}));

jest.mock("./hooks/useFetchGeneralExercises");

describe("GeneralExercisePage", () => {
    const mockExercises = [
        {
            id: "0a5748ef-d3e9-4df7-8feb-efa174a520c2",
            name: "Liegestütze",
            categories: ["CORE", "LOWER_BODY", "UPPER_BODY"],
            equipment: ["BENCH", "BALL", "MAT", "CHAIR"],
            shortDescription: "Eine grundlegende Übung für den Oberkörper",
            longDescription:
                "Liegestütze sind eine grundlegende Körpergewichtsübung, die hauptsächlich die Brust, Schultern und Trizeps trainiert. Sie beanspruchen auch die Rumpfmuskulatur für Stabilität.",
            directions:
                "1. Beginnen Sie in einer hohen Plank-Position. 2. Senken Sie Ihren Körper, bis Ihre Brust fast den Boden berührt. 3. Drücken Sie sich wieder nach oben. 4. Halten Sie Ihre Rumpfmuskulatur während der gesamten Übung angespannt.",
            video: "https://example.com/videos/push-up",
            thumbnailUrl: "https://example.com/videos/push-up",
        },
        {
            id: "1b5748ef-d3e9-4df7-8feb-efa174a520c3",
            name: "Kniebeugen",
            categories: ["LOWER_BODY"],
            equipment: ["MAT"],
            shortDescription: "Eine grundlegende Übung für die Beine",
            longDescription: "Kniebeugen sind eine grundlegende Übung, die die Beinmuskulatur stärkt.",
            directions: "1. Stellen Sie sich aufrecht hin. 2. Beugen Sie die Knie, bis Ihre Oberschenkel parallel zum Boden sind. 3. Drücken Sie sich wieder nach oben.",
            video: "https://example.com/videos/squat",
            thumbnailUrl: "https://example.com/videos/squat",
        },
    ];

    test("renders loading state", () => {
        (useFetchGeneralExercises as jest.Mock).mockReturnValue({
            exercises: [],
            loading: true,
            error: null,
            refetch: jest.fn(),
        });

        render(<GeneralExercisePage />);
        expect(screen.getByText("Loading...")).toBeInTheDocument();
    });

    test("renders error state", async () => {
        (useFetchGeneralExercises as jest.Mock).mockReturnValue({
            exercises: [],
            loading: false,
            error: "Fehler beim Laden",
            refetch: jest.fn(),
        });

        render(<GeneralExercisePage />);
        expect(screen.getByText("Error: Fehler beim Laden")).toBeInTheDocument();
    });

    test("renders exercise list", async () => {
        (useFetchGeneralExercises as jest.Mock).mockReturnValue({
            exercises: mockExercises,
            loading: false,
            error: null,
            refetch: jest.fn(),
        });

        render(<GeneralExercisePage />);

        await waitFor(() => {
            expect(screen.getByRole("heading", { level: 1 })).toHaveTextContent("Übungskatalog");
        });

        expect(screen.getByText("Liegestütze")).toBeInTheDocument();
        expect(screen.getByText("Kniebeugen")).toBeInTheDocument();
    });
});

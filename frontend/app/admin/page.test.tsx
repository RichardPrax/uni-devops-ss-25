import { render, screen } from "@testing-library/react";

import AdminDashboard from "./page";

test("load admin dashboard", () => {
    render(<AdminDashboard />);

    const greeting = screen.getByText("Dashboard");

    expect(greeting).toBeInTheDocument();
});

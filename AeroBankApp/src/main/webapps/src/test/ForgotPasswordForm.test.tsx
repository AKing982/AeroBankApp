
import {render, fireEvent, waitFor, screen} from "@testing-library/react";
import {MemoryRouter, Router} from "react-router-dom";
import ForgotPasswordForm from "../components/ForgotPasswordForm";


describe("ForgotPasswordForm", () => {
    it("displays error if fields are empty", async () => {
        render(
            <MemoryRouter initialEntries={['/forgot-password']}>
                <ForgotPasswordForm />
            </MemoryRouter>
        );

        const verifyButton = screen.getByText('Verify Username');

        fireEvent.click(verifyButton);

        await waitFor(() => screen.getByText("We couldn't find the username in our system. Please try again with another user."));
    });
})


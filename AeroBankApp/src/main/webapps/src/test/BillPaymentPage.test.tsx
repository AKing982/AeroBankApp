import { render, fireEvent, screen } from '@testing-library/react';
import BillPaymentPage from '../components_new/BillPaymentPage'; // Use your actual component path
import {MemoryRouter, Router} from 'react-router-dom';

describe("BillPaymentPage Component", () => {


    it("renders without crashing", () => {
        render(
            <MemoryRouter initialEntries={['/billPay/*']}>
                <BillPaymentPage />
            </MemoryRouter>
        );
        expect(screen.getByText(/Payments/i)).toBeInTheDocument();
        expect(screen.getByText(/Payees/i)).toBeInTheDocument();
        expect(screen.getByText(/Transfers/i)).toBeInTheDocument();
        expect(screen.getByText(/Calendar/i)).toBeInTheDocument();
    });

    it("navigates to the right path when a tab is clicked", async () => {
        render(
            <Router history={history}>
                <BillPaymentPage />
            </Router>
        );
        fireEvent.click(screen.getByText(/Payees/i));
        expect(history.location.pathname).toBe('/billPay/Payees');
    });

    // assuming axios is mocked
    it('fetches user profile data successfully', async () => {
        const mockData = {
            name: 'Test',
            email: 'test@email.com',
            lastLogin: '2024-06-24',
        };

        axios.get.mockImplementationOnce(() =>
            Promise.resolve({ data: mockData })
        );

        render(
            <Router history={history}>
                <BillPaymentPage />
            </Router>
        );

        // Check the resulting user profile data
        expect(await screen.findByText(mockData.name)).toBeInTheDocument();
        expect(screen.getByText(mockData.email)).toBeInTheDocument();
        expect(screen.getByText(`Last login: ${mockData.lastLogin}`)).toBeInTheDocument();
    });
});
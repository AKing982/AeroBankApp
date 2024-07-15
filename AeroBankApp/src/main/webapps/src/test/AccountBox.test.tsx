import { render, fireEvent, screen } from "@testing-library/react";
import AccountBox from "../components_new/AccountBox";

describe("AccountBox Component", () => {
    const defaultProps = {
        color: "#000000",
        accountCode: "AH123",
        accountName: "AH123 checking acount",
        balance: "200.00",
        pending: "50.00",
        available: "150.00"
    };

    it("renders correctly", () => {
        render(<AccountBox {...defaultProps}/>);
        expect(screen.getByText("AH123")).toBeInTheDocument();
        expect(screen.getByText("AH123 checking acount")).toBeInTheDocument();
        expect(screen.getByText("Balance: $200.00")).toBeInTheDocument();
        expect(screen.getByText("Pending: $50.00")).toBeInTheDocument();
        expect(screen.getByText("Available: $150.00")).toBeInTheDocument();
    });

    it("has correct styles for CircleBox", () => {
        render(<AccountBox {...defaultProps} />);
        const circleBox = screen.getByText("AH123");
        expect(circleBox).toHaveStyle({ backgroundColor: "#000000" }); // Compare with your passed color props
    });

    it("runs event handlers correctly", () => {
        render(<AccountBox {...defaultProps} />);
        const card = screen.getByRole("button");

        // Test handleClick
        console.log = jest.fn();
        fireEvent.click(card);
        expect(console.log).toHaveBeenCalledWith('Clicked');

        // Test handleKeyPress (Enter key)
        fireEvent.keyPress(card, { key: "Enter", code: "Enter" });
        expect(console.log).toHaveBeenCalledWith('Clicked');

        fireEvent.keyPress(card, { key: " ", code: "Space" });
        expect(console.log).toHaveBeenCalledWith('Clicked');
    });
});
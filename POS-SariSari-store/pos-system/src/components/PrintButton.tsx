import React from 'react';
import { printReceipt } from '../utils/printHelper';

const PrintButton: React.FC = () => {
    const handlePrint = () => {
        printReceipt();
    };

    return (
        <button onClick={handlePrint}>
            Print Receipt
        </button>
    );
};

export default PrintButton;
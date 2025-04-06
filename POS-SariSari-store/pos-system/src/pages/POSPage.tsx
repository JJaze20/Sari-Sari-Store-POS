import React, { useState } from 'react';
import PrintButton from '../components/PrintButton';
import Receipt from '../components/Receipt';
import { printReceipt } from '../utils/printHelper';

const POSPage: React.FC = () => {
    const [receiptData, setReceiptData] = useState({
        items: [
            { name: 'Item 1', price: 10.0, quantity: 1 },
            { name: 'Item 2', price: 15.0, quantity: 2 },
        ],
        total: 40.0,
    });

    const handlePrint = () => {
        printReceipt(receiptData);
    };

    return (
        <div className="pos-page">
            <h1>Point of Sale System</h1>
            <Receipt data={receiptData} />
            <PrintButton onClick={handlePrint} />
        </div>
    );
};

export default POSPage;
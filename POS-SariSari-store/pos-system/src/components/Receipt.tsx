import React from 'react';

interface ReceiptProps {
    items: Array<{
        name: string;
        quantity: number;
        price: number;
    }>;
    total: number;
}

const Receipt: React.FC<ReceiptProps> = ({ items, total }) => {
    return (
        <div className="receipt">
            <h2>Receipt</h2>
            <ul>
                {items.map((item, index) => (
                    <li key={index}>
                        {item.name} - {item.quantity} x ${item.price.toFixed(2)} = ${(item.quantity * item.price).toFixed(2)}
                    </li>
                ))}
            </ul>
            <h3>Total: ${total.toFixed(2)}</h3>
        </div>
    );
};

export default Receipt;
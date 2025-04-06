export const printReceipt = (receiptContent: string) => {
    const printWindow = window.open('', '_blank');
    if (printWindow) {
        printWindow.document.write(`
            <html>
                <head>
                    <title>Print Receipt</title>
                    <style>
                        body { font-family: Arial, sans-serif; }
                        .receipt { margin: 20px; }
                    </style>
                </head>
                <body>
                    <div class="receipt">${receiptContent}</div>
                </body>
            </html>
        `);
        printWindow.document.close();
        printWindow.print();
        printWindow.close();
    }
};
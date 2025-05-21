/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package my.posari;

import javax.swing.table.DefaultTableModel;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.text.MessageFormat;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.SwingConstants; 
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;  // For MouseListener

/**
 *
 * @author user
 */
import java.sql.*;
import java.text.*;
import java.util.*;

public class PosariMain extends javax.swing.JFrame {

    /**
     * Creates new form PosariMain
     */
    public Connection con;
    public Statement statement;
    public String sql;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/storeposlog1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public PosariMain() {
        initComponents();
        // Call the method to fetch data and display buttons
        fetchDataAndDisplayButtons();
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            statement=con.createStatement();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    private void toTransactionDatabase(String receiptNumber, int qtySold, Double revenue) {
    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        String query = "INSERT INTO transactionhistory (transact_date, receipt_id, prods_sold, total_revenue) VALUES (?, ?, ?, ?)";
        String qty = String.valueOf(qtySold);
        String rev = String.valueOf(revenue);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, generateTime()); // Generate Receipt #
            statement.setString(2, receiptNumber); // Set date of purchase
            statement.setString(3, qty);
            statement.setString(4, rev);
            statement.executeUpdate(); // Execute the insert query
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error uploading history: " + e.getMessage());
    }
}
    
    public String generateTime(){
        java.util.Date date = new java.util.Date();
        SimpleDateFormat Formatdate = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ",Locale.getDefault());
        String dateDisplay = Formatdate.format(date);
        return dateDisplay;
    }
    
    public String generateReceiptNumber(){
        int receiptNo = (int)(Math.random()*9999999);
        String receipt = Integer.toString(receiptNo);
        return receipt;
    }

 private void setupTable() {
    // Set up the JTable with columns for Item Name, Quantity, and Amount
    DefaultTableModel model = new DefaultTableModel(new Object[]{"Item Name", "Quantity", "Amount"}, 0);
    jTable1.setModel(model); // Make sure jTable1 is set with the model
}

private void fetchDataAndDisplayButtons() {
    // Clear any existing buttons first (if any)
    jPanelstuff.removeAll();

    // Set GridLayout to arrange buttons equally
    int numberOfRows = 5;  // Can be dynamically calculated based on the number of items
    int numberOfColumns = 3;  // Adjust as necessary
    jPanelstuff.setLayout(new GridLayout(numberOfRows, numberOfColumns, 10, 10));  // Optional gaps between buttons

    // Establish the database connection
    String query = "SELECT * FROM inventory";  // Fetch all columns from the inventory table

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        // Loop through the result set and create a button for each item
        while (rs.next()) {
            int id = rs.getInt("ID");  // Column name should match the actual column name in your database
            String name = rs.getString("Name");
            double price = rs.getDouble("Price");
            double tax = rs.getDouble("Tax");
            int quantity = rs.getInt("Qty");  // Correct column name based on your schema

            // Create a button for each product
            JButton productButton = new JButton(name);

            // Set the button text to be centered
            productButton.setHorizontalTextPosition(SwingConstants.CENTER);
            productButton.setVerticalTextPosition(SwingConstants.CENTER);

            // Add a MouseListener to detect right-click and show product details
            productButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {  // Detect right-click (BUTTON3)
                        // Show product details in a custom JOptionPane with Add button
                        Object[] options = {"OK", "Add"};
                        int option = JOptionPane.showOptionDialog(
                            jPanelstuff,
                            "Product ID: " + id + "\n" +
                            "Name: " + name + "\n" +
                            "Price: " + price + "\n" +
                            "Tax: " + tax + "\n" +
                            "Quantity: " + quantity,
                            "Product Details",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null, options, options[0]  // Default option is "OK"
                        );

                        if (option == 1) {  // If "Add" button is pressed
                            addToTable(name, price);  // Add to JTable
                            
                        }
                    } else if (e.getButton() == MouseEvent.BUTTON1) {  // Detect left-click (BUTTON1)
                        // Automatically add to JTable on left-click
                        addToTable(name, price);  // Add to JTable
                        
                    }
                }
            });

            // Add the button to the panel
            jPanelstuff.add(productButton);
        }

        // Refresh the UI to display the newly added buttons
        jPanelstuff.revalidate();
        jPanelstuff.repaint();

    } catch (SQLException e) {
        // Handle any SQL exceptions
        JOptionPane.showMessageDialog(this, "Error fetching data from the database: " + e.getMessage(),
                                      "Database Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace(); // Print the stack trace for debugging
    }
}

// Function to add item to JTable
private void addToTable(String name, double price) {
    // Assuming jTable1 already has a DefaultTableModel set up
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    
    double priceOfItem = price;

    // Prompt the user to enter the quantity
    String input = JOptionPane.showInputDialog(this, "Enter quantity for " + name + ":", "Quantity", JOptionPane.PLAIN_MESSAGE);
    if (input != null && !input.trim().isEmpty()) {
        try {
            int quantity = Integer.parseInt(input.trim());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calculate total price for the selected quantity
            double totalPrice = priceOfItem * quantity;

            // Format the price
            DecimalFormat df = new DecimalFormat("0.00");

            // Get the table model
            model = (DefaultTableModel) jTable1.getModel();

            // Check if the item already exists in the table
            boolean itemExists = false;
            for (int i = 0; i < model.getRowCount(); i++) {
                String existingItemName = model.getValueAt(i, 0).toString();
                if (existingItemName.equals(name)) {
                    // Update the quantity and price for the existing item
                    int existingQuantity = Integer.parseInt(model.getValueAt(i, 1).toString());
                    double existingPrice = Double.parseDouble(model.getValueAt(i, 2).toString());
                    int newQuantity = existingQuantity + quantity;
                    double newPrice = existingPrice + totalPrice;

                    model.setValueAt(newQuantity, i, 1); // Update quantity
                    model.setValueAt(df.format(newPrice), i, 2); // Update price
                    itemExists = true;
                    break;
                }
            }

            // If the item does not exist, add a new row
            if (!itemExists) {
                model.addRow(new Object[]{name, quantity, df.format(totalPrice)});
            }

            // Update the total cost
            ItemCost();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        // User canceled or entered nothing
        JOptionPane.showMessageDialog(this, "No quantity entered. Action canceled.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

}
    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jbtn9 = new javax.swing.JButton();
        jbtn7 = new javax.swing.JButton();
        jbtn8 = new javax.swing.JButton();
        jbtn6 = new javax.swing.JButton();
        jbtn5 = new javax.swing.JButton();
        jbtn4 = new javax.swing.JButton();
        jbtn1 = new javax.swing.JButton();
        jbtn2 = new javax.swing.JButton();
        jbtn3 = new javax.swing.JButton();
        jbtnClear = new javax.swing.JButton();
        jbtnDot = new javax.swing.JButton();
        jbtn0 = new javax.swing.JButton();
        jPanelstuff = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtxtTotal = new javax.swing.JTextField();
        jtxtSubTotal = new javax.swing.JTextField();
        jtxtTax = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jtxtChange = new javax.swing.JTextField();
        jtxtDisplay = new javax.swing.JTextField();
        jcboPayment = new javax.swing.JComboBox<>();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jbtnExit = new javax.swing.JButton();
        jbtnPay = new javax.swing.JButton();
        jbtnReset = new javax.swing.JButton();
        jbtnRemove1 = new javax.swing.JButton();
        jbtnRemove = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jbtn9.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtn9.setText("9");
        jbtn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn9ActionPerformed(evt);
            }
        });
        jPanel1.add(jbtn9, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 84, 100));

        jbtn7.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtn7.setText("7");
        jbtn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn7ActionPerformed(evt);
            }
        });
        jPanel1.add(jbtn7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 84, 100));

        jbtn8.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtn8.setText("8");
        jbtn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn8ActionPerformed(evt);
            }
        });
        jPanel1.add(jbtn8, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 84, 100));

        jbtn6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtn6.setText("6");
        jbtn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn6ActionPerformed(evt);
            }
        });
        jPanel1.add(jbtn6, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 84, 100));

        jbtn5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtn5.setText("5");
        jbtn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn5ActionPerformed(evt);
            }
        });
        jPanel1.add(jbtn5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 84, 100));

        jbtn4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtn4.setText("4");
        jbtn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn4ActionPerformed(evt);
            }
        });
        jPanel1.add(jbtn4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 84, 100));

        jbtn1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtn1.setText("1");
        jbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn1ActionPerformed(evt);
            }
        });
        jPanel1.add(jbtn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 250, 84, 100));

        jbtn2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtn2.setText("2");
        jbtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn2ActionPerformed(evt);
            }
        });
        jPanel1.add(jbtn2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 84, 100));

        jbtn3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtn3.setText("3");
        jbtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn3ActionPerformed(evt);
            }
        });
        jPanel1.add(jbtn3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 84, 100));

        jbtnClear.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtnClear.setText("C");
        jbtnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnClearActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 370, 84, 100));

        jbtnDot.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtnDot.setText(".");
        jbtnDot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDotActionPerformed(evt);
            }
        });
        jPanel1.add(jbtnDot, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 370, 84, 100));

        jbtn0.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtn0.setText("0");
        jbtn0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtn0ActionPerformed(evt);
            }
        });
        jPanel1.add(jbtn0, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 84, 100));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 300, 490));

        jPanelstuff.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanelstuffLayout = new javax.swing.GroupLayout(jPanelstuff);
        jPanelstuff.setLayout(jPanelstuffLayout);
        jPanelstuffLayout.setHorizontalGroup(
            jPanelstuffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelstuffLayout.createSequentialGroup()
                .addGap(0, Short.MAX_VALUE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelstuffLayout.setVerticalGroup(
            jPanelstuffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelstuffLayout.createSequentialGroup()
                .addGap(0, 238, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 238, Short.MAX_VALUE))
        );

        getContentPane().add(jPanelstuff, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, 850, 480));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Qty", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 270, 490));

        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel8.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 1340, 250));

        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel10.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 1340, 250));

        jPanel8.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 1340, 250));

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 1340, 250));

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel6.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 1340, 250));

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 1340, 250));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Total");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("SubTotal");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setText("Tax");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jtxtTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jtxtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtTotalActionPerformed(evt);
            }
        });
        jPanel2.add(jtxtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 220, 50));

        jtxtSubTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jtxtSubTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtSubTotalActionPerformed(evt);
            }
        });
        jPanel2.add(jtxtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 22, 220, 50));

        jtxtTax.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jPanel2.add(jtxtTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 220, 50));

        jPanel8.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 420, 200));

        jPanel12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel12.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 1340, 250));

        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel14.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 1340, 250));

        jPanel12.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 1340, 250));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel4.setText("Change");
        jPanel12.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel5.setText("Payment Method");
        jPanel12.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setText("Cash");
        jPanel12.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jtxtChange.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jPanel12.add(jtxtChange, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, 200, 50));

        jtxtDisplay.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jPanel12.add(jtxtDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 200, 50));

        jcboPayment.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jcboPayment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "G-Cash" }));
        jcboPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboPaymentActionPerformed(evt);
            }
        });
        jPanel12.add(jcboPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 12, 200, 50));

        jPanel8.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 420, 200));

        jPanel16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel16.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 1340, 250));

        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel18.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 1340, 250));

        jPanel16.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 1340, 250));

        jbtnExit.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtnExit.setText("Exit");
        jbtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnExitActionPerformed(evt);
            }
        });
        jPanel16.add(jbtnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 130, 190, 50));

        jbtnPay.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtnPay.setText("Pay");
        jbtnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPayActionPerformed(evt);
            }
        });
        jPanel16.add(jbtnPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 190, 50));

        jbtnReset.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtnReset.setText("Reset");
        jbtnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnResetActionPerformed(evt);
            }
        });
        jPanel16.add(jbtnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 190, 50));

        jbtnRemove1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtnRemove1.setText("Remove");
        jbtnRemove1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnRemove1ActionPerformed(evt);
            }
        });
        jPanel16.add(jbtnRemove1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 190, 50));

        jbtnRemove.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jbtnRemove.setText("Admin Panel");
        jbtnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnRemoveActionPerformed(evt);
            }
        });
        jPanel16.add(jbtnRemove, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 400, 50));

        jPanel8.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 30, 420, 200));

        getContentPane().add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, 1370, 250));

        jButton1.setFont(new java.awt.Font("Lucida Console", 3, 12)); // NOI18N
        jButton1.setText("Refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1370, 500, -1, 30));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
// fUNCTIONSZ for items===========================

    public void ItemCost() {

        double sum = 0;
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            sum = sum + Double.parseDouble(jTable1.getValueAt(i, 2).toString());
        }
        jtxtSubTotal.setText(Double.toString(sum));
        double cTotall = Double.parseDouble(jtxtSubTotal.getText());

        double cTax = (cTotall * 3.9) / 100;
        String iTaxTotal = String.format("₱ %.2f", cTax);
        jtxtTax.setText(iTaxTotal);

        String iSubTotal = String.format("₱ %.2f", cTotall);
        jtxtSubTotal.setText(iSubTotal);

        String iTotal = String.format("₱ %.2f", cTotall + cTax);
        jtxtTotal.setText(iTotal);

        //String BarCode = String.format("Total is %.2f", cTotall + cTax);
        //jtxtBarCode.setText(BarCode);
    }
//=======================================

// fUNCTIONSZ for Change ===========================
    public void Change() {
        double sum = 0;
        double tax = 3.9;
        double cash = Double.parseDouble(jtxtDisplay.getText());

        for (int i = 0; i < jTable1.getRowCount(); i++) {
            sum = sum + Double.parseDouble(jTable1.getValueAt(i, 2).toString());
        }

        double cTax = (sum * 3.9) / 100;
        double cChange = (cash - (sum + cTax));

        String ChangeGiven = String.format("₱ %.2f", cChange);
        jtxtChange.setText(ChangeGiven);
    }


    private void jbtn9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn9ActionPerformed

//code to Display the numbers
        String Enternumber = jtxtDisplay.getText();

        if (Enternumber == "") {
            jtxtDisplay.setText(jbtn9.getText());
        } else {
            Enternumber = jtxtDisplay.getText() + jbtn9.getText();
            jtxtDisplay.setText(Enternumber);
        }
    }//GEN-LAST:event_jbtn9ActionPerformed

    private void jbtn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn7ActionPerformed
        String Enternumber = jtxtDisplay.getText();

        if (Enternumber == "") {
            jtxtDisplay.setText(jbtn7.getText());
        } else {
            Enternumber = jtxtDisplay.getText() + jbtn7.getText();
            jtxtDisplay.setText(Enternumber);
        }

    }//GEN-LAST:event_jbtn7ActionPerformed

    private void jbtn8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn8ActionPerformed
        String Enternumber = jtxtDisplay.getText();

        if (Enternumber == "") {
            jtxtDisplay.setText(jbtn8.getText());
        } else {
            Enternumber = jtxtDisplay.getText() + jbtn8.getText();
            jtxtDisplay.setText(Enternumber);
        }
    }//GEN-LAST:event_jbtn8ActionPerformed

    private void jbtn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn6ActionPerformed
        String Enternumber = jtxtDisplay.getText();

        if (Enternumber == "") {
            jtxtDisplay.setText(jbtn6.getText());
        } else {
            Enternumber = jtxtDisplay.getText() + jbtn6.getText();
            jtxtDisplay.setText(Enternumber);
        }
    }//GEN-LAST:event_jbtn6ActionPerformed

    private void jbtn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn5ActionPerformed
        String Enternumber = jtxtDisplay.getText();

        if (Enternumber == "") {
            jtxtDisplay.setText(jbtn5.getText());
        } else {
            Enternumber = jtxtDisplay.getText() + jbtn5.getText();
            jtxtDisplay.setText(Enternumber);
        }
    }//GEN-LAST:event_jbtn5ActionPerformed

    private void jbtn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn4ActionPerformed
        String Enternumber = jtxtDisplay.getText();

        if (Enternumber == "") {
            jtxtDisplay.setText(jbtn4.getText());
        } else {
            Enternumber = jtxtDisplay.getText() + jbtn4.getText();
            jtxtDisplay.setText(Enternumber);
        }
    }//GEN-LAST:event_jbtn4ActionPerformed

    private void jbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn1ActionPerformed
        String Enternumber = jtxtDisplay.getText();

        if (Enternumber == "") {
            jtxtDisplay.setText(jbtn1.getText());
        } else {
            Enternumber = jtxtDisplay.getText() + jbtn1.getText();
            jtxtDisplay.setText(Enternumber);
        }
    }//GEN-LAST:event_jbtn1ActionPerformed

    private void jbtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn2ActionPerformed
        String Enternumber = jtxtDisplay.getText();

        if (Enternumber == "") {
            jtxtDisplay.setText(jbtn2.getText());
        } else {
            Enternumber = jtxtDisplay.getText() + jbtn2.getText();
            jtxtDisplay.setText(Enternumber);
        }
    }//GEN-LAST:event_jbtn2ActionPerformed

    private void jbtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn3ActionPerformed
        String Enternumber = jtxtDisplay.getText();

        if (Enternumber == "") {
            jtxtDisplay.setText(jbtn3.getText());
        } else {
            Enternumber = jtxtDisplay.getText() + jbtn3.getText();
            jtxtDisplay.setText(Enternumber);
        }
    }//GEN-LAST:event_jbtn3ActionPerformed

    private void jbtnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnClearActionPerformed
        jtxtDisplay.setText("");
        jtxtChange.setText("");
    }//GEN-LAST:event_jbtnClearActionPerformed

    private void jbtnDotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDotActionPerformed
        // TODO add your handling code here:
        if (!jtxtDisplay.getText().contains(".")) {
            jtxtDisplay.setText(jtxtDisplay.getText() + jbtnDot.getText());
        }
    }//GEN-LAST:event_jbtnDotActionPerformed

    private void jbtn0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtn0ActionPerformed
        String Enternumber = jtxtDisplay.getText();

        if (Enternumber == "") {
            jtxtDisplay.setText(jbtn0.getText());
        } else {
            Enternumber = jtxtDisplay.getText() + jbtn0.getText();
            jtxtDisplay.setText(Enternumber);
        }
    }//GEN-LAST:event_jbtn0ActionPerformed
    public String generateReceiptContent() {
        // Logic to generate receipt conten
        String receiptNumber = generateReceiptNumber();
        int qtySold = 0;
        StringBuilder receipt = new StringBuilder();
        
        //HEADER
        receipt.append("        ******** GOON SQUAD SARI-SARI STORE **********\n");
        receipt.append(String.format("%-40s\t %3s\t %10s\t\n", "Item", "Quantity", "Price"));
        receipt.append("        Receipt Number: " + receiptNumber + "\n");;
        receipt.append("----------------------------------------------------------------------------\n");
        

        // Iterate through the rows of the JTable
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String itemName = model.getValueAt(i, 0).toString(); // Item name
            String quantity = model.getValueAt(i, 1).toString(); // Quantity
            String price = model.getValueAt(i, 2).toString();    // Price
            
            qtySold += Integer.parseInt(quantity);

            // Append item details to the receipt
            receipt.append(String.format("%-25s\t %5s\t %10s\t\n", 
            itemName, 
            quantity, 
            price));
        }

         receipt.append("----------------------------------------------------------------------------\n");

        // Add total cost
        String totalText = jtxtTotal.getText().replaceAll("[^\\d.]", ""); // Clean ₱ if present
        double total = Double.parseDouble(totalText);
        receipt.append(String.format("Total:  ₱ %.2f\n", total));
        
        // Change (optional if present)
        String changeText = jtxtChange.getText().replaceAll("[^\\d.]", ""); // Clean ₱ if present
        if (!changeText.isEmpty()) {
        double change = Double.parseDouble(changeText);
        receipt.append(String.format("Change: ₱ %.2f\n", change));
        }
        
         // Footer
        receipt.append("****************************\n");
        receipt.append("Thank you for shopping with us!\n");
        
        //Upload data to Database
        try{
            toTransactionDatabase(receiptNumber,qtySold,total);
            JOptionPane.showMessageDialog(this, "Successfully uploaded the data!");
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Error uploading data to the database: " + ex.getMessage(),
                                      "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return receipt.toString();
    }
    private void jbtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnExitActionPerformed

        int confirmExit = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);

        if (confirmExit == JOptionPane.YES_OPTION) {
            System.exit(0);  // Exit the application
        }
    }//GEN-LAST:event_jbtnExitActionPerformed

    private void jbtnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnResetActionPerformed
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        jtxtChange.setText("");
        jtxtTax.setText("");
        jtxtTotal.setText("");
        jtxtDisplay.setText("");
        jtxtSubTotal.setText("");
        jtxtDisplay.setEditable(true);
    }//GEN-LAST:event_jbtnResetActionPerformed

    private void jbtnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPayActionPerformed
        if (jcboPayment.getSelectedItem().equals("Cash")) {
            try {
                if (jtxtDisplay.getText().trim().isEmpty() || jtxtTotal.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please ensure both cash and total fields are filled.");
                    return;
                }

                double cash = Double.parseDouble(jtxtDisplay.getText().trim());

                // FIX: Remove currency symbol before parsing
                String totalText = jtxtTotal.getText().replaceAll("[^\\d.]", "");
                double total = Double.parseDouble(totalText);

                double change = cash - total;

                if (change < 0) {
                    JOptionPane.showMessageDialog(this, "Insufficient balance. Please provide enough cash.");
                    return;
                }

                jtxtChange.setText(String.format("₱ %.2f", change)); // Add ₱ for display
                JOptionPane.showMessageDialog(this, "Payment successful! Change: " + String.format("₱ %.2f", change));

                Change();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values for cash and total.");
            }
        } else if (jcboPayment.getSelectedItem().equals("G-Cash")) {
            // If "GCash" is selected, open the GCash website
            try {
                Desktop.getDesktop().browse(new URI("https://www.gcash.com"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace(); // Handle any potential errors
            }
        } else {
            // Clear text fields for other payment methods
            jtxtChange.setText("");
            jtxtDisplay.setText("");
        }
        // TODO add your handling code here:      
        if (jtxtDisplay.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the cash amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Parse the cash and total amounts
            double cash = Double.parseDouble(jtxtDisplay.getText().trim());
            String totalText = jtxtTotal.getText().replaceAll("[^\\d.]", ""); // Remove currency symbols
            double total = Double.parseDouble(totalText);

            // Check if cash is sufficient
            if (cash < total) {
                JOptionPane.showMessageDialog(this, "Your money is not enough to buy the item.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generate and display the receipt
            JDialog receiptDialog = new JDialog(this, "Receipt Preview", true);
            receiptDialog.setSize(400, 600);
            receiptDialog.setLocationRelativeTo(this);

            // Create a JTextArea to display the receipt content
            JTextArea receiptArea = new JTextArea();
            receiptArea.setEditable(false);
            receiptArea.setText(generateReceiptContent()); // Generate receipt content dynamically
            JScrollPane scrollPane = new JScrollPane(receiptArea);

            // Add the JTextArea to the dialog
            receiptDialog.add(scrollPane);

            // Add a Print button to the dialog
            JButton printButton = new JButton("Print");
            printButton.addActionListener(e -> {
                MessageFormat header = new MessageFormat("Receipt");
                MessageFormat footer = new MessageFormat("Page {0, number, integer}");
                try {
                    receiptArea.print(header, footer);
                } catch (java.awt.print.PrinterException ex) {
                    JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                receiptDialog.dispose(); // Close the dialog after printing
            });

            // Add a Cancel button to the dialog
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> receiptDialog.dispose());

            // Add buttons to the dialog
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(printButton);
            buttonPanel.add(cancelButton);
            receiptDialog.add(buttonPanel, BorderLayout.SOUTH);

            // Show the dialog
            receiptDialog.setVisible(true);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values for cash.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        jtxtDisplay.setEditable(false);
    }//GEN-LAST:event_jbtnPayActionPerformed

    private void jtxtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtTotalActionPerformed

    private void jbtnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnRemoveActionPerformed
        AdminLog admin = new AdminLog();
        admin.setVisible(true);
        admin.pack();
        admin.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_jbtnRemoveActionPerformed

    private void jtxtSubTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtSubTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtSubTotalActionPerformed

    private void jcboPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboPaymentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcboPaymentActionPerformed

    private void jbtnRemove1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnRemove1ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
        
        if(selectedRow != -1){
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Selected item has been removed.");
        }
        else{
            JOptionPane.showMessageDialog(this, "Please select a row to remove.");
        }
    }//GEN-LAST:event_jbtnRemove1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        fetchDataAndDisplayButtons();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PosariMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PosariMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PosariMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PosariMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PosariMain().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelstuff;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton jbtn0;
    private javax.swing.JButton jbtn1;
    private javax.swing.JButton jbtn2;
    private javax.swing.JButton jbtn3;
    private javax.swing.JButton jbtn4;
    private javax.swing.JButton jbtn5;
    private javax.swing.JButton jbtn6;
    private javax.swing.JButton jbtn7;
    private javax.swing.JButton jbtn8;
    private javax.swing.JButton jbtn9;
    private javax.swing.JButton jbtnClear;
    private javax.swing.JButton jbtnDot;
    private javax.swing.JButton jbtnExit;
    private javax.swing.JButton jbtnPay;
    private javax.swing.JButton jbtnRemove;
    private javax.swing.JButton jbtnRemove1;
    private javax.swing.JButton jbtnReset;
    private javax.swing.JComboBox<String> jcboPayment;
    private javax.swing.JTextField jtxtChange;
    private javax.swing.JTextField jtxtDisplay;
    private javax.swing.JTextField jtxtSubTotal;
    private javax.swing.JTextField jtxtTax;
    private javax.swing.JTextField jtxtTotal;
    // End of variables declaration//GEN-END:variables
}

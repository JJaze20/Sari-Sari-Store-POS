/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package my.posari;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

/**
 *
 * @author rpand
 */

public class InventoryPanel extends javax.swing.JFrame {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/storeposlog1"; 
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "";

    public InventoryPanel() {
        initComponents();
        loadDataFromDatabase();
        txtSave.setEnabled(false);

        // Add list selection listener for your inventory table (already in your code)
        jInventory.getSelectionModel().addListSelectionListener(e -> {
            // Make sure we're not adjusting the selection while selecting
            if (!e.getValueIsAdjusting() && jInventory.getSelectedRow() != -1) {
                // Get the selected row index
                int selectedRow = jInventory.getSelectedRow();
                
                // Set the text fields with the selected row data
                txtID.setText(jInventory.getValueAt(selectedRow, 0).toString());
                txtName.setText(jInventory.getValueAt(selectedRow, 1).toString());
                txtPrice.setText(jInventory.getValueAt(selectedRow, 2).toString());
                jTax.setText(jInventory.getValueAt(selectedRow, 3).toString());
                jspinQuantity.setValue(Integer.parseInt(jInventory.getValueAt(selectedRow, 4).toString()));
            }
        });

        // Add listeners for field changes to enable/disable the Save button
        txtName.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                checkFields();  // Call checkFields whenever the text is changed
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                checkFields();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                checkFields();
            }
        });

        txtPrice.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                checkFields();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                checkFields();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                checkFields();
            }
        });

        jTax.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                checkFields();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                checkFields();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                checkFields();
            }
        });

        jspinQuantity.addChangeListener(e -> checkFields());  // Call checkFields when the quantity changes
    }

    public void loadDataFromDatabase() {
        // SQL query to fetch data from the inventory table
        String query = "SELECT * FROM inventory";

        // Initialize table model
        DefaultTableModel model = (DefaultTableModel) jInventory.getModel();

        // Clear existing rows in case you want to reload data
        model.setRowCount(0);

        // Establish connection to the database
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Iterate over the result set and add rows to the table model
            while (rs.next()) {
                int productID = rs.getInt("ID");
                String name = rs.getString("Name");
                double price = rs.getDouble("Price");
                double tax = rs.getDouble("Tax");
                int quantity = rs.getInt("Qty");

                // Add row to the table model
                model.addRow(new Object[] { productID, name, price, tax, quantity });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean validateFields() {
        if (txtName.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Name field cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void close() {
        this.dispose();
    }

    private void checkFields() {
        // Check if all fields have valid (non-empty) values and quantity is greater than 0
        if (!txtName.getText().trim().isEmpty() &&
            !txtPrice.getText().trim().isEmpty() &&
            !jTax.getText().trim().isEmpty() &&
            (int) jspinQuantity.getValue() > 0) {
            txtSave.setEnabled(true);  // Enable Save button
        } else {
            txtSave.setEnabled(false); // Disable Save button if any field is invalid
        }
    }

    private void saveItemToDatabase(String name, String txtID) {
        // Modify the query to include a tax column (no category anymore)
        String queryInventory = "INSERT INTO inventory (ID, Name, Qty, Price, Tax) VALUES (?, ?, ?, ?, ?)";
        int productID;

        // Check if txtID is empty
        if (txtID.isEmpty()) {
            productID = (int)(Math.random() * 9000000) + 1000000;
            int response = JOptionPane.showConfirmDialog(this, 
                "No Product ID entered. A random 7-digit ID will be assigned. Do you want to proceed?", 
                "Confirm ID Assignment", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.NO_OPTION) {
                return;
            }
        } else {
            try {
                productID = Integer.parseInt(txtID);
                if (String.valueOf(productID).length() != 7) {
                    JOptionPane.showMessageDialog(this, "Product ID must be a 7-digit number.", "Invalid ID", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid Product ID. Please enter a numeric 7-digit ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Get quantity from spinner
        int quantity = (int) jspinQuantity.getValue();

        // Get and validate price
        String priceInput = txtPrice.getText().trim();
        double price;
        try {
            price = Double.parseDouble(priceInput);
            DecimalFormat df = new DecimalFormat("0.##");
            txtPrice.setText(df.format(price)); // Optional: update field with formatted price
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid price format. Please enter a number like 9.99", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get and validate tax value from jTax
        String taxInput = jTax.getText().trim();
        double tax = 0.0;
        try {
            tax = Double.parseDouble(taxInput);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid tax format. Please enter a valid number like 1.23", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Database operations
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmtInventory = conn.prepareStatement(queryInventory)) {

            // Insert into inventory table
            stmtInventory.setInt(1, productID); // ID
            stmtInventory.setString(2, name);   // Name
            stmtInventory.setInt(3, quantity);  // Qty
            stmtInventory.setDouble(4, price);  // Price
            stmtInventory.setDouble(5, tax);    // Tax

            int result = stmtInventory.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Item added successfully! ID: " + productID, "Success", JOptionPane.INFORMATION_MESSAGE);
                
                DefaultTableModel model = (DefaultTableModel) jInventory.getModel();
                model.addRow(new Object[]{
                    productID,    // ID
                    name,         // Name
                    price,        // Price
                    tax,          // Tax
                    quantity      // Quantity
                });

            } else {
                JOptionPane.showMessageDialog(this, "Failed to add item", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jInventory = new javax.swing.JTable();
        Name = new java.awt.Label();
        txtName = new javax.swing.JTextField();
        txtSave = new javax.swing.JButton();
        txtUpdate = new javax.swing.JButton();
        txtReset = new javax.swing.JButton();
        Name1 = new java.awt.Label();
        txtID = new javax.swing.JTextField();
        Name2 = new java.awt.Label();
        txtPrice = new javax.swing.JTextField();
        Name3 = new java.awt.Label();
        jspinQuantity = new javax.swing.JSpinner();
        Name4 = new java.awt.Label();
        jTax = new javax.swing.JTextField();
        jRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Go back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 51, 51));

        jLabel1.setFont(new java.awt.Font("Lucida Console", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Inventory");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jInventory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Price", "Tax", "Qty"
            }
        ));
        jScrollPane1.setViewportView(jInventory);
        jInventory.getAccessibleContext().setAccessibleName("");

        Name.setFont(new java.awt.Font("Lucida Sans", 1, 18)); // NOI18N
        Name.setText("Product ID");

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        txtSave.setFont(new java.awt.Font("Lucida Console", 1, 12)); // NOI18N
        txtSave.setText("Save");
        txtSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaveActionPerformed(evt);
            }
        });

        txtUpdate.setFont(new java.awt.Font("Lucida Console", 1, 12)); // NOI18N
        txtUpdate.setText("Update");
        txtUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUpdateActionPerformed(evt);
            }
        });

        txtReset.setFont(new java.awt.Font("Lucida Console", 1, 12)); // NOI18N
        txtReset.setText("Reset");
        txtReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtResetActionPerformed(evt);
            }
        });

        Name1.setFont(new java.awt.Font("Lucida Sans", 1, 18)); // NOI18N
        Name1.setText("Name");

        txtID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDActionPerformed(evt);
            }
        });

        Name2.setFont(new java.awt.Font("Lucida Sans", 1, 18)); // NOI18N
        Name2.setText("Quantity");

        txtPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPriceActionPerformed(evt);
            }
        });

        Name3.setFont(new java.awt.Font("Lucida Sans", 1, 18)); // NOI18N
        Name3.setText("Tax");

        Name4.setFont(new java.awt.Font("Lucida Sans", 1, 18)); // NOI18N
        Name4.setText("Price");

        jTax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTaxActionPerformed(evt);
            }
        });

        jRefresh.setFont(new java.awt.Font("Segoe UI Black", 2, 14)); // NOI18N
        jRefresh.setText("Refresh");
        jRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jRefresh)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Name4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Name3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTax, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Name1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(txtID)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Name2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jspinQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(txtSave)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtUpdate)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtReset)
                                    .addComponent(jButton1))))
                        .addGap(18, 18, 18))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Name1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Name2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jspinQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Name3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Name4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtReset))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUpdate)
                            .addComponent(txtSave))
                        .addGap(33, 33, 33))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jRefresh))
                .addGap(19, 19, 19))
        );

        Name.getAccessibleContext().setAccessibleName("Name");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    
  public class AdminPanelSingleton {

    // The private static instance of AdminPanel
    private static AdminPanel instance;

    // Private constructor to prevent instantiation from outside
    private AdminPanelSingleton() {}

    // Public method to provide access to the single instance
    public static AdminPanel getInstance() {
        // Create a new instance if it doesn't exist
        if (instance == null) {
            instance = new AdminPanel();
        }
        return instance;
    }
}


    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        close();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaveActionPerformed
        String name = txtName.getText();
    String productID = txtID.getText().trim();
    
    // Validate fields (assuming you have a method for this)
    if (validateFields()) {
        // Save the item to the database, including name and productID
        saveItemToDatabase(name, productID); 
        
       
    }
    }//GEN-LAST:event_txtSaveActionPerformed

    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDActionPerformed

    private void txtPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPriceActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtPriceActionPerformed

    private void txtResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtResetActionPerformed
        // TODO add your handling code here:
          // Reset all input fields to their initial state
    txtName.setText("");          // Clear the product name text field
    txtID.setText("");            // Clear the product ID text field
    txtPrice.setText("");         // Clear the price text field
    jspinQuantity.setValue(0);    // Reset the quantity spinner to its default value (1)
    jTax.setText("");
    }//GEN-LAST:event_txtResetActionPerformed

    private void jTaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTaxActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTaxActionPerformed

    private void jRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRefreshActionPerformed
        // TODO add your handling code here:
        loadDataFromDatabase();
    }//GEN-LAST:event_jRefreshActionPerformed

    private void txtUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUpdateActionPerformed
        // TODO add your handling code here:
         // Retrieve updated values from text fields
    String productID = txtID.getText().trim();
    String name = txtName.getText().trim();
    String priceStr = txtPrice.getText().trim();
    String taxStr = jTax.getText().trim();
    int quantity = (int) jspinQuantity.getValue();

    // Validate input (e.g., check if price and tax are valid numbers)
    try {
        double price = Double.parseDouble(priceStr);
        double tax = Double.parseDouble(taxStr);

        // Update the database with the new data
        String updateQuery = "UPDATE inventory SET Name = ?, Price = ?, Tax = ?, Qty = ? WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            // Set the parameters for the prepared statement
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setDouble(3, tax);
            stmt.setInt(4, quantity);
            stmt.setString(5, productID);

            int result = stmt.executeUpdate();
            
            if (result > 0) {
                // If update is successful, update the table model too
                DefaultTableModel model = (DefaultTableModel) jInventory.getModel();
                int selectedRow = jInventory.getSelectedRow();
                model.setValueAt(name, selectedRow, 1);
                model.setValueAt(price, selectedRow, 2);
                model.setValueAt(tax, selectedRow, 3);
                model.setValueAt(quantity, selectedRow, 4);

                JOptionPane.showMessageDialog(this, "Record updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid number format for Price or Tax.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
        
    }//GEN-LAST:event_txtUpdateActionPerformed

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
            java.util.logging.Logger.getLogger(InventoryPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InventoryPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InventoryPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InventoryPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InventoryPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Label Name;
    private java.awt.Label Name1;
    private java.awt.Label Name2;
    private java.awt.Label Name3;
    private java.awt.Label Name4;
    private javax.swing.JButton jButton1;
    private javax.swing.JTable jInventory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTax;
    private javax.swing.JSpinner jspinQuantity;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JButton txtReset;
    private javax.swing.JButton txtSave;
    private javax.swing.JButton txtUpdate;
    // End of variables declaration//GEN-END:variables
}

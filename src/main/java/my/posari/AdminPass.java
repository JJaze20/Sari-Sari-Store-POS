package my.posari;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class AdminPass extends javax.swing.JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/storeposlog1"; 
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "";
    /**
     * Creates new form posariSignup
     */
    public AdminPass() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        LeftCoverPage = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        RightCoverPage = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPasswordField2 = new javax.swing.JPasswordField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(236, 236, 236));

        LeftCoverPage.setBackground(new java.awt.Color(25, 50, 92));

        jLabel6.setForeground(new java.awt.Color(242, 242, 242));
        jLabel6.setText("Copyright © RESIBO. All rights reserved.");

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setForeground(new java.awt.Color(53, 104, 190));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("copyright © RESIBO. All rights reserved.");

        jLabel1.setBackground(new java.awt.Color(25, 50, 92));
        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 27)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(25, 50, 92));
        jLabel1.setText("ADMIN PASSWORD");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(25, 50, 92));
        jLabel2.setText("Enter Password");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(25, 50, 92));
        jLabel3.setText("Confirm Password");

        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(25, 50, 92));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Enter");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(25, 50, 92));
        jLabel4.setText("Already have a password?");

        jButton2.setBackground(new java.awt.Color(242, 242, 242));
        jButton2.setForeground(new java.awt.Color(227, 48, 84));
        jButton2.setText("Login");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPasswordField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(25, 50, 92));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Show ");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(242, 242, 242));
        jButton4.setForeground(new java.awt.Color(227, 48, 84));
        jButton4.setText("Go Back");
        jButton4.setBorder(null);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout RightCoverPageLayout = new javax.swing.GroupLayout(RightCoverPage);
        RightCoverPage.setLayout(RightCoverPageLayout);
        RightCoverPageLayout.setHorizontalGroup(
            RightCoverPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightCoverPageLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(RightCoverPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RightCoverPageLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addGroup(RightCoverPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(RightCoverPageLayout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton2))
                        .addGroup(RightCoverPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jPasswordField2, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                            .addComponent(jPasswordField1))
                        .addComponent(jButton4)))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        RightCoverPageLayout.setVerticalGroup(
            RightCoverPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightCoverPageLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(RightCoverPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RightCoverPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout LeftCoverPageLayout = new javax.swing.GroupLayout(LeftCoverPage);
        LeftCoverPage.setLayout(LeftCoverPageLayout);
        LeftCoverPageLayout.setHorizontalGroup(
            LeftCoverPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftCoverPageLayout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(LeftCoverPageLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LeftCoverPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RightCoverPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        LeftCoverPageLayout.setVerticalGroup(
            LeftCoverPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftCoverPageLayout.createSequentialGroup()
                .addGroup(LeftCoverPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LeftCoverPageLayout.createSequentialGroup()
                        .addComponent(RightCoverPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LeftCoverPageLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel5)
                        .addGap(64, 64, 64)))
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LeftCoverPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(LeftCoverPage, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        AdminLog adminLogin = new AdminLog();
        adminLogin.setVisible(true);
        adminLogin.pack();
        adminLogin.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String AdminPassword = new String(jPasswordField1.getPassword()).trim(); // First password input
        String confirmPass = new String(jPasswordField2.getPassword()).trim(); // Confirmation password

        // Check if the username and password are not empty
        if (AdminPassword.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please your new password.");
            return;
        }

        // Check password length
        if (AdminPassword.length() < 8) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long!");
            return;
        }
        
        // Check if password matches confirmation
        if (!AdminPassword.equals(confirmPass)) {
        JOptionPane.showMessageDialog(this, "Passwords do not match!");
        return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,"Confirm Password? ",
        "Confirmation", 
        JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
        JOptionPane.showMessageDialog(this, "Password confirmation canceled. Please re-enter your password.");
        return;
        }

        // Hash the password
        String hashedPassword = PasswordUtil.hashPassword(AdminPassword);

   
        if (canAddMoreAdmins()) {
        saveAdminPasswordToDatabase(hashedPassword);
        JOptionPane.showMessageDialog(this, "Admin password added successfully!");
         AdminLog adminLogin = new AdminLog();
         adminLogin.setVisible(true);
         adminLogin.pack();
         adminLogin.setLocationRelativeTo(null);
         this.dispose();
         } else {
         JOptionPane.showMessageDialog(this, "Cannot add more than 2 admin passwords.");
         }
    }//GEN-LAST:event_jButton1ActionPerformed

    private boolean canAddMoreAdmins() {
    String query = "SELECT COUNT(*) FROM admin WHERE AdminPassword IS NOT NULL";

    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count < 2; // Only allow if less than 2 admins
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error checking admin count: " + e.getMessage());
    }

    return false; // Deny by default if something goes wrong
}
    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void jPasswordField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mousePressed(java.awt.event.MouseEvent evt) {
        jPasswordField1.setEchoChar((char) 0); // Show password
        jPasswordField2.setEchoChar((char) 0); 
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent evt) {
        jPasswordField1.setEchoChar('\u2022'); // Hide password with bullet character
        jPasswordField2.setEchoChar('\u2022');
    }
});
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        PosariMain mainFrame = new PosariMain();
            mainFrame.setVisible(true);
            mainFrame.pack();
            mainFrame.setLocationRelativeTo(null);
            this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed
      private void saveAdminPasswordToDatabase(String hashedPassword) {
    String query = "INSERT INTO admin2 (AdminPassword) VALUES (?)";

    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, hashedPassword);
        statement.executeUpdate();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error saving admin password: " + e.getMessage());
    }
}
    
    /**
     * @param args the command line arguments
     */
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel LeftCoverPage;
    private javax.swing.JPanel RightCoverPage;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    // End of variables declaration//GEN-END:variables
}


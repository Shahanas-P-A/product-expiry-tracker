import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductExpiryTracker extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtName, txtCategory, txtQuantity, txtExpiry;
    private JButton btnAdd, btnUpdate, btnDelete, btnFilter, btnRefresh;
    private JLabel lblStatus;
    private Connection conn;

    private static final String[] COLUMNS = {"ID", "Product Name", "Category", "Quantity", "Expiry Date", "Status", "Days Left"};

    public ProductExpiryTracker() {
        super("Product Expiry Tracker");
        connectDB();
        initUI();
        loadProducts();
    }

    private void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expiry_db", "root", "");
            createTableIfNotExists();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Connection Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS products ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(100) NOT NULL,"
                + "category VARCHAR(50),"
                + "quantity INT DEFAULT 1,"
                + "expiry_date DATE NOT NULL,"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        conn.createStatement().executeUpdate(sql);
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(26, 26, 46));
        header.setPadding(new Insets(16, 24, 16, 24));
        JLabel title = new JLabel("📦 Product Expiry Tracker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(245, 166, 35));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add / Edit Product"));
        inputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtName = new JTextField(15);
        txtCategory = new JTextField(12);
        txtQuantity = new JTextField(6);
        txtExpiry = new JTextField(10);
        txtExpiry.setToolTipText("Format: YYYY-MM-DD");

        addField(inputPanel, gbc, "Product Name:", txtName, 0);
        addField(inputPanel, gbc, "Category:", txtCategory, 1);
        addField(inputPanel, gbc, "Quantity:", txtQuantity, 2);
        addField(inputPanel, gbc, "Expiry Date (YYYY-MM-DD):", txtExpiry, 3);

        btnAdd = new JButton("Add Product");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnFilter = new JButton("Expiring Soon (30 days)");
        btnRefresh = new JButton("Show All");

        styleButton(btnAdd, new Color(39, 174, 96), Color.WHITE);
        styleButton(btnUpdate, new Color(41, 128, 185), Color.WHITE);
        styleButton(btnDelete, new Color(231, 76, 60), Color.WHITE);
        styleButton(btnFilter, new Color(245, 166, 35), Color.BLACK);
        styleButton(btnRefresh, new Color(100, 100, 100), Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 4; inputPanel.add(btnAdd, gbc);
        gbc.gridx = 1; inputPanel.add(btnUpdate, gbc);
        gbc.gridx = 2; inputPanel.add(btnDelete, gbc);
        gbc.gridx = 3; inputPanel.add(btnFilter, gbc);
        gbc.gridx = 4; inputPanel.add(btnRefresh, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(26, 26, 46));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setDefaultRenderer(Object.class, new StatusCellRenderer());
        table.getSelectionModel().addListSelectionListener(e -> populateFields());

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Status bar
        lblStatus = new JLabel("  Ready");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setBorder(BorderFactory.createEtchedBorder());
        add(lblStatus, BorderLayout.SOUTH);

        // Button actions
        btnAdd.addActionListener(e -> addProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnFilter.addActionListener(e -> loadExpiringSoon());
        btnRefresh.addActionListener(e -> loadProducts());

        // Set panels
        setLayout(new BorderLayout(10, 10));
        add(header, BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        center.add(inputPanel, BorderLayout.NORTH);
        center.add(new JScrollPane(table), BorderLayout.CENTER);
        center.add(lblStatus, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int col) {
        gbc.gridx = col * 2; gbc.gridy = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = col * 2 + 1;
        panel.add(field, gbc);
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void loadProducts() {
        loadQuery("SELECT id, name, category, quantity, expiry_date FROM products ORDER BY expiry_date");
        lblStatus.setText("  " + tableModel.getRowCount() + " products loaded.");
    }

    private void loadExpiringSoon() {
        String today = LocalDate.now().toString();
        String soon = LocalDate.now().plusDays(30).toString();
        loadQuery("SELECT id, name, category, quantity, expiry_date FROM products WHERE expiry_date BETWEEN '" + today + "' AND '" + soon + "' ORDER BY expiry_date");
        lblStatus.setText("  " + tableModel.getRowCount() + " products expiring within 30 days.");
    }

    private void loadQuery(String sql) {
        tableModel.setRowCount(0);
        try {
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                LocalDate expiry = rs.getDate("expiry_date").toLocalDate();
                LocalDate today = LocalDate.now();
                long daysLeft = ChronoUnit.DAYS.between(today, expiry);
                String status = getStatus(daysLeft);
                tableModel.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("name"), rs.getString("category"),
                    rs.getInt("quantity"), expiry.toString(), status, daysLeft
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private String getStatus(long daysLeft) {
        if (daysLeft < 0) return "EXPIRED";
        if (daysLeft <= 7) return "CRITICAL";
        if (daysLeft <= 30) return "WARNING";
        return "SAFE";
    }

    private void addProduct() {
        try {
            String sql = "INSERT INTO products (name, category, quantity, expiry_date) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtName.getText().trim());
            ps.setString(2, txtCategory.getText().trim());
            ps.setInt(3, Integer.parseInt(txtQuantity.getText().trim()));
            ps.setString(4, txtExpiry.getText().trim());
            ps.executeUpdate();
            clearFields();
            loadProducts();
            lblStatus.setText("  Product added successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateProduct() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a product to update."); return; }
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            String sql = "UPDATE products SET name=?, category=?, quantity=?, expiry_date=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtName.getText().trim());
            ps.setString(2, txtCategory.getText().trim());
            ps.setInt(3, Integer.parseInt(txtQuantity.getText().trim()));
            ps.setString(4, txtExpiry.getText().trim());
            ps.setInt(5, id);
            ps.executeUpdate();
            clearFields();
            loadProducts();
            lblStatus.setText("  Product updated successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a product to delete."); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this product?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) tableModel.getValueAt(row, 0);
                conn.createStatement().executeUpdate("DELETE FROM products WHERE id=" + id);
                clearFields();
                loadProducts();
                lblStatus.setText("  Product deleted.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void populateFields() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtName.setText(tableModel.getValueAt(row, 1).toString());
        txtCategory.setText(tableModel.getValueAt(row, 2).toString());
        txtQuantity.setText(tableModel.getValueAt(row, 3).toString());
        txtExpiry.setText(tableModel.getValueAt(row, 4).toString());
    }

    private void clearFields() {
        txtName.setText(""); txtCategory.setText(""); txtQuantity.setText(""); txtExpiry.setText("");
        table.clearSelection();
    }

    // Custom cell renderer for color-coded status
    class StatusCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
            Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
            String status = tableModel.getValueAt(row, 5).toString();
            if (!sel) {
                switch (status) {
                    case "EXPIRED":  c.setBackground(new Color(253, 231, 233)); break;
                    case "CRITICAL": c.setBackground(new Color(255, 243, 205)); break;
                    case "WARNING":  c.setBackground(new Color(255, 253, 219)); break;
                    default:         c.setBackground(new Color(232, 245, 233)); break;
                }
            }
            if (col == 5) {
                setHorizontalAlignment(CENTER);
                setFont(getFont().deriveFont(Font.BOLD));
                switch (status) {
                    case "EXPIRED":  setForeground(new Color(192, 57, 43)); break;
                    case "CRITICAL": setForeground(new Color(211, 84, 0)); break;
                    case "WARNING":  setForeground(new Color(183, 149, 11)); break;
                    default:         setForeground(new Color(39, 174, 96)); break;
                }
            } else {
                setForeground(Color.BLACK);
                setHorizontalAlignment(LEFT);
            }
            return c;
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new ProductExpiryTracker().setVisible(true));
    }
}

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class RestaurantBillingSystem extends JFrame {
    private JTextField customerNameField, customerPhoneField, customerAgeField, customerIdField;
    private JTextArea receiptArea;
    private DefaultListModel<String> orderListModel;
    private JComboBox<String> menuDropdown;
    private JSpinner quantitySpinner;
    private int totalBill = 0;

    public RestaurantBillingSystem() {
        // Set JFrame properties
        setTitle("Restaurant Billing System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Global Styles
        Font titleFont = new Font("SansSerif", Font.BOLD, 28);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 18);
        Font buttonFont = new Font("SansSerif", Font.BOLD, 16);
        Color bgColor = new Color(240, 248, 255);
        Color accentColor = new Color(30, 144, 255);
        Color buttonColor = new Color(0, 123, 255);

        getContentPane().setBackground(bgColor);

        // Title Label
        JLabel titleLabel = new JLabel("Welcome to Our Food Point");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(accentColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Customer Information Panel
        JPanel customerPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        customerPanel.setBackground(bgColor);
        customerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor, 2), "Customer Information", 0, 0, labelFont, accentColor));

        customerNameField = createStyledTextField();
        customerPhoneField = createStyledTextField();
        customerAgeField = createStyledTextField();
        customerIdField = createStyledTextField();

        customerPanel.add(createStyledLabel("Customer Name:"));
        customerPanel.add(customerNameField);
        customerPanel.add(createStyledLabel("Phone Number:"));
        customerPanel.add(customerPhoneField);
        customerPanel.add(createStyledLabel("Age:"));
        customerPanel.add(customerAgeField);
        customerPanel.add(createStyledLabel("Customer ID:"));
        customerPanel.add(customerIdField);

        // Menu and Order Panel
        JPanel menuPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        menuPanel.setBackground(bgColor);
        menuPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor, 2), "Menu and Orders", 0, 0, labelFont, accentColor));

        // Menu Selection Panel
        JPanel menuSelectionPanel = new JPanel(new FlowLayout());
        menuSelectionPanel.setBackground(bgColor);

        String[] menuItems = {
                "Burger - PKR 300", "Pizza - PKR 1200", "Cold Drink - PKR 50",
                "Fried Chicken - PKR 500", "French Fries - PKR 100",
                "Juices - PKR 150", "Biryani - PKR 500", "Samosas - PKR 100"
        };
        menuDropdown = new JComboBox<>(menuItems);
        menuDropdown.setFont(labelFont);
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantitySpinner.setFont(labelFont);

        JButton addButton = createStyledButton("Add to Order", buttonColor, Color.WHITE, buttonFont);
        menuSelectionPanel.add(createStyledLabel("Menu Item:"));
        menuSelectionPanel.add(menuDropdown);
        menuSelectionPanel.add(createStyledLabel("Quantity:"));
        menuSelectionPanel.add(quantitySpinner);
        menuSelectionPanel.add(addButton);

        // Order List Panel
        orderListModel = new DefaultListModel<>();
        JList<String> orderList = new JList<>(orderListModel);
        orderList.setFont(new Font("Monospaced", Font.PLAIN, 16));
        orderList.setBackground(Color.WHITE);
        JScrollPane orderScrollPane = new JScrollPane(orderList);
        orderScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor, 2), "Order Summary", 0, 0, labelFont, accentColor));

        menuPanel.add(menuSelectionPanel);
        menuPanel.add(orderScrollPane);

        // Receipt and Buttons Panel
        JPanel receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBackground(bgColor);

        receiptArea = new JTextArea(15, 30);
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        receiptArea.setBorder(BorderFactory.createLineBorder(accentColor, 2));
        JScrollPane receiptScrollPane = new JScrollPane(receiptArea);
        receiptScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(accentColor, 2), "Receipt", 0, 0, labelFont, accentColor));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(bgColor);

        JButton generateReceiptButton = createStyledButton("Generate Receipt", buttonColor, Color.WHITE, buttonFont);
        JButton clearButton = createStyledButton("Clear", new Color(220, 53, 69), Color.WHITE, buttonFont);
        JButton exitButton = createStyledButton("Exit", Color.GRAY, Color.WHITE, buttonFont);

        buttonPanel.add(generateReceiptButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        receiptPanel.add(receiptScrollPane, BorderLayout.CENTER);
        receiptPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panels to the frame
        add(customerPanel, BorderLayout.WEST);
        add(menuPanel, BorderLayout.CENTER);
        add(receiptPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addToOrder());
        generateReceiptButton.addActionListener(e -> generateReceipt());
        clearButton.addActionListener(e -> clearForm());
        exitButton.addActionListener(e -> System.exit(0));

        // Make the frame visible
        setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        return textField;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        return button;
    }

    private void addToOrder() {
        String selectedItem = (String) menuDropdown.getSelectedItem();
        int quantity = (int) quantitySpinner.getValue();

        String[] itemParts = selectedItem.split(" - PKR ");
        String itemName = itemParts[0];
        int itemPrice = Integer.parseInt(itemParts[1]);

        int totalItemPrice = itemPrice * quantity;
        totalBill += totalItemPrice;

        orderListModel.addElement(itemName + " x" + quantity + " - PKR " + totalItemPrice);
    }

    private void generateReceipt() {
        if (customerNameField.getText().isEmpty() || customerPhoneField.getText().isEmpty() ||
                customerAgeField.getText().isEmpty() || customerIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all customer information.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (orderListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items in the order.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder receipt = new StringBuilder();
        receipt.append("Customer Receipt\n");
        receipt.append("=================\n");
        receipt.append("Name: ").append(customerNameField.getText()).append("\n");
        receipt.append("Phone: ").append(customerPhoneField.getText()).append("\n");
        receipt.append("Age: ").append(customerAgeField.getText()).append("\n");
        receipt.append("Customer ID: ").append(customerIdField.getText()).append("\n\n");
        receipt.append("Items Ordered:\n");

        for (int i = 0; i < orderListModel.size(); i++) {
            receipt.append(orderListModel.getElementAt(i)).append("\n");
        }

        receipt.append("\nTotal Bill: PKR ").append(totalBill).append("\n");
        receipt.append("Thank you for your purchase!");

        receiptArea.setText(receipt.toString());

        try (FileWriter writer = new FileWriter("CustomerBill.txt")) {
            writer.write(receipt.toString());
            JOptionPane.showMessageDialog(this, "Receipt saved to CustomerBill.txt.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving receipt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        customerNameField.setText("");
        customerPhoneField.setText("");
        customerAgeField.setText("");
        customerIdField.setText("");
        receiptArea.setText("");
        orderListModel.clear();
        totalBill = 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RestaurantBillingSystem::new);
    }
}

package com.deepak.hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;

public class ItemReportWindow extends JFrame {
	private JTextField startDateField;
    private JTextField endDateField;
    private JTable dataTable;
    public ItemReportWindow() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setTitle("Item Count by Sales Date Range");

        // Prompt the user for start and end dates
        JPanel panel = new JPanel();
        panel.setBackground(new java.awt.Color(0,175,175));
       

        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        startDateField = new JTextField(10);
        panel.add(startDateLabel);
        panel.add(startDateField);

        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        endDateField = new JTextField(10);
        panel.add(endDateLabel);
        panel.add(endDateField);

        JButton generateButton = new JButton("Generate Report");
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					generateReport();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }

			
        });
        panel.add(generateButton);
        
        dataTable=new JTable();
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBackground(new java.awt.Color(0,175,175));
        panel.add(scrollPane);

        add(panel);

    }


    
    private void generateReport() throws ParseException {
		String startDateText = startDateField.getText();
        String endDateText = endDateField.getText();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = dateFormat.parse(startDateText);
            Date endDate = dateFormat.parse(endDateText);
		Connection connection = null;
        
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "Deepak@123");
            String sql = "SELECT serilizable FROM Invoice_bill WHERE sqlDate BETWEEN ? AND ?"; // Your SQL query to retrieve items within date range
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setDate(1, new java.sql.Date(startDate.getTime()));
            pst.setDate(2, new java.sql.Date(endDate.getTime()));

            ResultSet resultSet = pst.executeQuery();

            Map<String, Integer> itemCount = new HashMap<>(); // Map to store item and its count

            // Process the result set
            while (resultSet.next()) {
                String itemsString = resultSet.getString("serilizable");
                ArrayList<String> items = new ArrayList<>(Arrays.asList(itemsString.substring(1, itemsString.length() - 1).split(", ")));

                for (String item : items) {
                    itemCount.put(item, itemCount.getOrDefault(item, 0) + 1); // Count occurrences of each item
                }
            }

            // Close resources
            resultSet.close();
            pst.close();

            // Sort the items based on count in descending order
            List<Map.Entry<String, Integer>> sortedItems = new ArrayList<>(itemCount.entrySet());
            sortedItems.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

            // Create table model
            DefaultTableModel model = new DefaultTableModel();
            JTable table = new JTable(model);

            // Add headers to the table
            model.addColumn("Item");
            model.addColumn("Item Count");

            // Populate the table model with sorted data
            for (Map.Entry<String, Integer> entry : sortedItems) {
                model.addRow(new Object[]{entry.getKey(), entry.getValue()});
            }
            dataTable.setModel(model);
            // Display the table in a scrollable window
            JScrollPane scrollPane1 = new JScrollPane(table);
            getContentPane().add(scrollPane1, BorderLayout.CENTER);
        } catch (SQLException e) {
            e.printStackTrace();
        } 
            
        }
    


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ItemReportWindow itemCountWindow = new ItemReportWindow();
            itemCountWindow.setVisible(true);
        });
    }
}


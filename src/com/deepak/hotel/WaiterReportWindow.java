
package com.deepak.hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.util.Vector;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class WaiterReportWindow  extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private JTextField waiterNameField;
    private JTextField startDateField;
    private JTextField endDateField;
  
   private JTable dataTable;

    public WaiterReportWindow () {
    	
    	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 750);
        setLocationRelativeTo(null);
        setTitle("Waiter-wise Report");

        // Prompt the user for start and end dates
        JPanel panel = new JPanel();
        panel.setBackground(new java.awt.Color(0,175,175));
        JLabel waiterNameLabell = new JLabel("Waiter Name:");
        waiterNameLabell.setHorizontalAlignment(SwingConstants.CENTER);
        waiterNameField = new JTextField(10);

        JLabel startDateLabel = new JLabel("Start Date (YYYY-MM-DD):");
        startDateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        startDateField = new JTextField(10);

        JLabel endDateLabel = new JLabel("End Date (YYYY-MM-DD):");
        endDateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        endDateField = new JTextField(10);

        JButton generateButton = new JButton("Generate Report");
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchWaiterSales();
            }

			
        });
        
        dataTable=new JTable();
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBackground(new java.awt.Color(0,175,175));

        getContentPane().add(panel, BorderLayout.NORTH);
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(174)
        					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        						.addComponent(startDateLabel, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
        						.addComponent(waiterNameLabell, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
        						.addComponent(endDateLabel, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
        					.addGap(3)
        					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(waiterNameField)
        						.addComponent(endDateField)
        						.addComponent(startDateField, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(104)
        					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(260)
        					.addComponent(generateButton, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(623, Short.MAX_VALUE))
        );
        gl_panel.setVerticalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addGap(43)
        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(waiterNameLabell, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
        				.addComponent(waiterNameField, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(startDateLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
        				.addComponent(startDateField, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addComponent(endDateLabel, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
        				.addComponent(endDateField, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addComponent(generateButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(26))
        	);
        		
        
        panel.setLayout(gl_panel);

    }

    
 
	

   

    private void searchWaiterSales() {
        String waiterName = waiterNameField.getText();
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();

        java.sql.Date start = java.sql.Date.valueOf(startDate);
        java.sql.Date end = java.sql.Date.valueOf(endDate);

        DefaultTableModel model = new DefaultTableModel();
//        table.setModel(model);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "Deepak@123");
            String sqlQuery = "SELECT name, waiter, final_total, sqlDate, " +
                    "(SELECT SUM(final_total) FROM invoice_bill WHERE sqlDate BETWEEN ? AND ? AND waiter=?) AS total_sum " +
                    "FROM invoice_bill WHERE waiter=? AND sqlDate BETWEEN ? AND ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setDate(1, start);
            preparedStatement.setDate(2, end);
            preparedStatement.setString(3, waiterName);
            preparedStatement.setString(4, waiterName);
            preparedStatement.setDate(5, start);
            preparedStatement.setDate(6, end);

            ResultSet resultSet = preparedStatement.executeQuery();

            int columns = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= columns; i++) {
                model.addColumn(resultSet.getMetaData().getColumnName(i));
            }

            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columns; i++) {
                    row.add(resultSet.getObject(i));
                }
                model.addRow(row);
            }
            dataTable.setModel(model);

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
   
	

    // Methods for other tabs (addItemReportFunctionality, addCustomerSalesFunctionality)...

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	 WaiterReportWindow  waiterReportWindow = new  WaiterReportWindow();
            waiterReportWindow.setVisible(true);
        });
    }
}

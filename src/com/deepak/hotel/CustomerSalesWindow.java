package com.deepak.hotel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CustomerSalesWindow extends JFrame {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private JTextField startDateField;
    private JTextField endDateField;
    private JTable dataTable;
    private JButton button2;

    private Connection connection;

    public CustomerSalesWindow() {
        setTitle("Sales Report Generator");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        centerFrame();

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
                generateReport();
            }
        });
        panel.add(generateButton);
        
        dataTable=new JTable();
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBackground(new java.awt.Color(0,175,175));
        panel.add(scrollPane);

        add(panel);
        setVisible(true);
        
        button2=new JButton("Print");
        button2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dataTable.print();
				} catch (PrinterException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});

        // Initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "Deepak@123");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void generateReport() {
        String startDateText = startDateField.getText();
        String endDateText = endDateField.getText();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = dateFormat.parse(startDateText);
            Date endDate = dateFormat.parse(endDateText);

            // Fetch data from the database based on the date range
            fetchDataFromDatabase(startDate, endDate);
        } catch (ParseException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void fetchDataFromDatabase(Date startDate, Date endDate) throws SQLException {
        // Clear existing table data
    	DefaultTableModel model = new DefaultTableModel();
    	
    	
//        dataTable.setModel(model);
        // Prepare SQL query to fetch data between start and end dates
    	String sqlQuery = "SELECT name, final_total, sqlDate, " +
                "(SELECT SUM(final_total) FROM invoice_bill WHERE sqlDate BETWEEN ? AND ? ) AS total_sum " +
                "FROM invoice_bill WHERE sqlDate BETWEEN ? AND ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
        preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
        preparedStatement.setDate(3, new java.sql.Date(startDate.getTime()));
        preparedStatement.setDate(4, new java.sql.Date(endDate.getTime()));

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

       
    
}

    public void centerFrame() {
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation((size.width/2 - getWidth()/2),(size.height/2 - getHeight()/2));
    }

    

    public static void main(String[] args) {
    	
    	 java.awt.EventQueue.invokeLater(new Runnable() {
             public void run() {
                 new CustomerSalesWindow().setVisible(true);
             }
       
    	 });
    }
}
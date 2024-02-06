package com.deepak.hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class InvoiceReportGenerator extends JFrame {
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

    private Connection connection;

    public InvoiceReportGenerator() {
        setTitle("Invoice Report Generator");
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
    	
//        dataTable.setModel(model);
        // Prepare SQL query to fetch data between start and end dates
        String sql = "SELECT * FROM invoice_bill WHERE sqlDate BETWEEN ? AND ?";
        
        PreparedStatement pst = connection.prepareStatement(sql);
       pst.setDate(1, new java.sql.Date(startDate.getTime()));
        pst.setDate(2, new java.sql.Date(endDate.getTime()));

        ResultSet rs = pst.executeQuery();
//        pst = con.prepareStatement("select * from customer_bill");
       
        
        ResultSetMetaData rsd = rs.getMetaData();
       int c = rsd.getColumnCount();
        
        
       DefaultTableModel d = new DefaultTableModel(); // Initialize a new DefaultTableModel
       d.setColumnIdentifiers(new Object[]{"id", "tableno", "name", "total_iprice", "final_total"}); // Set column identifiers

       while (rs.next()) {
           Vector<Object> v2 = new Vector<>();
           v2.add(rs.getInt("id"));
           v2.add(rs.getString("tableno"));
           v2.add(rs.getString("name"));
           v2.add(rs.getString("total_iprice"));
           v2.add(rs.getString("final_total"));
           d.addRow(v2); // Add the filled Vector to the table model
       }

       dataTable.setModel(d);
        }
    public void centerFrame() {
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation((size.width/2 - getWidth()/2),(size.height/2 - getHeight()/2));
    }

    

    public static void main(String[] args) {
    	
    	 java.awt.EventQueue.invokeLater(new Runnable() {
             public void run() {
                 new InvoiceReportGenerator().setVisible(true);
             }
       
    	 });
    }
}


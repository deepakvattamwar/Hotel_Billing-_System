package com.deepak.hotel;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ReportGenerationTab extends JFrame {

    public ReportGenerationTab() {
        // Your existing code for tabs and functionality here...
    	setTitle("Reports");
    	setSize(600,600);
    	centerFrame();

        JButton waiterReportButton = new JButton("Waiter-wise Report");

        waiterReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WaiterReportWindow waiterReportWindow = new WaiterReportWindow();
                waiterReportWindow.setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel();
        JButton customerSalesButton = new JButton(" Customer and Sales Count");
        
                customerSalesButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        CustomerSalesWindow customerSalesWindow = new CustomerSalesWindow();
                        customerSalesWindow.setVisible(true);
                    }
                });
                JButton itemReportButton = new JButton("Item-wise Report");
                
                        itemReportButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                ItemReportWindow itemReportWindow = new ItemReportWindow();
                                itemReportWindow.setVisible(true);
                            }
                        });

        getContentPane().add(buttonPanel, BorderLayout.CENTER);
        
        JLabel lblNewLabel = new JLabel("Reports");
        Font currentFont = lblNewLabel.getFont();
        Font newFont = new Font("Roboto", Font.PLAIN, currentFont.getSize() + 30);
        lblNewLabel.setFont(newFont);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buttonPanel.setBackground(new java.awt.Color(0,175,175));
        GroupLayout gl_buttonPanel = new GroupLayout(buttonPanel);
        gl_buttonPanel.setHorizontalGroup(
        	gl_buttonPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_buttonPanel.createSequentialGroup()
        			.addGroup(gl_buttonPanel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_buttonPanel.createSequentialGroup()
        					.addGap(194)
        					.addGroup(gl_buttonPanel.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(waiterReportButton, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(itemReportButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(customerSalesButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        				.addGroup(gl_buttonPanel.createSequentialGroup()
        					.addGap(151)
        					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(152, Short.MAX_VALUE))
        );
        gl_buttonPanel.setVerticalGroup(
        	gl_buttonPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_buttonPanel.createSequentialGroup()
        			.addGap(42)
        			.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
        			.addGap(34)
        			.addComponent(customerSalesButton, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
        			.addGap(35)
        			.addComponent(itemReportButton, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
        			.addGap(36)
        			.addComponent(waiterReportButton, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(227, Short.MAX_VALUE))
        );
        buttonPanel.setLayout(gl_buttonPanel);
        
        
    }
    public void centerFrame() {
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation((size.width/2 - getWidth()/2),(size.height/2 - getHeight()/2));
    }

    // Your existing code for tabs and functionalities...

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReportGenerationTab reportGenerationTab = new ReportGenerationTab();
            reportGenerationTab.setVisible(true);
        });
    }
}
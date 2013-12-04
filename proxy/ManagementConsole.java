package com.proxy;

/*This is created from the DESIGN VIEW in eclipse so the code is HORRIBLE! Sorry...*/

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.Color;
import java.io.IOException;

import javax.swing.JList;

public class ManagementConsole extends JFrame {
	public BlockedList bl;
	private JPanel contentPane;
	JTextArea textPane = new JTextArea();
	JScrollPane scrollPane = new JScrollPane();
	// private JList addressList;
	private DefaultListModel management;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagementConsole frame = new ManagementConsole();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ManagementConsole() {
		management = new DefaultListModel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 537, 423);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Admin");
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("View blocklist\n");

		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Start Proxy");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ProxyServer pt = new ProxyServer();
				try {
					pt.main(null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// System.exit(1);

			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JPanel panel = new JPanel();
		panel.setBounds(5, 5, 527, 26);
		contentPane.add(panel);

		JLabel requestLbl = new JLabel("Current Request");
		panel.add(requestLbl);

		JLabel timeReceivedLbl = new JLabel("Time Received\n");
		panel.add(timeReceivedLbl);

		final JList addressList;
		management.addElement("208.80.152.201");

		bl = new BlockedList("208.80.152.201");// wiki

		addressList = new JList(management);
		addressList.setForeground(Color.RED);
		addressList.setBackground(Color.WHITE);
		addressList.setBounds(5, 72, 393, 265);
		addressList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// addressList.setBounds(121, 131, -20, 26);
		contentPane.add(addressList);

		final JButton backBtn = new JButton("Back\n");
		backBtn.setBounds(398, 292, 117, 29);

		backBtn.setBackground(Color.LIGHT_GRAY);
		contentPane.add(backBtn);
		/* The menu i have is not the one i want, change!! */
		final JButton addBtn = new JButton("Add");
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String name = JOptionPane.showInputDialog(
						ManagementConsole.this, "Enter IP Address");
				management.addElement(name);
				bl.addBadUrl(name);
				System.out.println(bl.toString());
				// addressList.ad//(addressTxtField.getText()); //.add(new
				// JMenuItem(addressTxtField.getText()));

			}
		});
		addBtn.setBounds(398, 67, 117, 29);
		contentPane.add(addBtn);

		final JButton removeBtn = new JButton("Remove");
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				bl.removeUrl(addressList.getSelectedValue().toString());
				management.removeElement(addressList.getSelectedValue());
				System.out.println(bl.toString());
			}
		});
		removeBtn.setBounds(398, 97, 117, 29);
		contentPane.add(removeBtn);

		textPane.setBounds(5, 31, 527, 343);
		contentPane.add(textPane);

		//scrollPane.setViewportView(textPane);

		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backBtn.setVisible(false);
				panel.setVisible(true);
				textPane.setVisible(true);
				addBtn.setVisible(false);
				removeBtn.setVisible(false);
				addressList.setVisible(false);

			}
		});

		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.setVisible(false);
				textPane.setVisible(false);
				backBtn.setVisible(true);
				addBtn.setVisible(true);
				removeBtn.setVisible(true);
				addressList.setVisible(true);

			}
		});

	}

	public void writeToScreen(String s) {
		textPane.append(s);
	}
}

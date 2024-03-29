package daltm;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
public class Server extends JFrame{
	JPanel pn,pn1,pn2;
	JLabel processing;
	public JTextArea output;
	JScrollPane sc;
	Stack stack;
	Stack stackResult;
	String number;
	ServerSocket server;
	public ArrayList<String> postFix;
	public static void main(String[] args) {
		Server sv = new Server(5000);
	}

	public Server(int port) {
		pn=new JPanel();
		pn1=new JPanel(new GridLayout(1,1));
		pn2=new JPanel(new FlowLayout());
		
		processing = new JLabel("Running");
		output = new JTextArea(20,40);
		sc = new JScrollPane(output);
		
		pn1.add(processing);
		pn2.add(sc);
		
		pn.add(pn1);
		pn.add(pn2);
		
		JFrame jf = new JFrame("Server");
		jf.add(pn);
		jf.setSize(500,350);
		jf.setLocation(600,400);	  
		jf.setVisible(true);
		jf.setResizable(false);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			server = new ServerSocket(port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Socket soc = null;
		while(true) {
			try {			
				soc = server.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			CalculateThread cal =new CalculateThread(soc,this);
			cal.start();
				
		}
	}
}

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
import javax.swing.JTextArea;
import javax.swing.JTextField;
public class Server extends JFrame implements ActionListener{
	JPanel pn,pn1,pn2;
	JLabel processing;
	JTextArea output;
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
		
		processing = new JLabel("Processing");
		output = new JTextArea(10,40);
		
		pn1.add(processing);
		pn2.add(output);
		
		pn.add(pn1);
		pn.add(pn2);
		
		JFrame jf = new JFrame("Server");
		jf.add(pn);
		jf.setSize(500,350);//set the size
		jf.setLocation(600,400);//set the location		  
		jf.setVisible(true);
		
		
		try {
			server = new ServerSocket(port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Socket soc = null;
		String s = "Waitting connection\n";
		output.setText(s);
		while(true) {
			try {			
				//output.setText("Waitting connection\n");
				soc = server.accept();				
				s+=  "Connected 1 device\n";
				output.setText(s);
				//System.out.print(output.getText() + "Connected 1 device\n");
//				DataInputStream in = new DataInputStream(soc.getInputStream());
//				DataOutputStream out = new  DataOutputStream(soc.getOutputStream());
//				while(true) {
//	//				String c = in.readUTF();
//	//				output.setText(output.getText() + "CLIENT: " + c + "\n");
//	//				this.goThrough(c);
//	//				printPostfix(postFix);
//	//				double result = this.result(this.postFix);
//	//				out.writeUTF(result+"");
//	//				output.setText(output.getText() + "SERVER: " + result + "\n");
//	//				postFix.clear();
//				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			new CalculateThread(soc).start();
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	//	reconnect
	// nhieu connect
	// reset server
}

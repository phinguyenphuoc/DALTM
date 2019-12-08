package daltm;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements ActionListener{
	JPanel pn1,pn2,pn3,pn4,pn;
	JButton btn1, btn2;
	JLabel request, respond;
	JTextField input;
	JTextArea output;
	Socket soc;
	public static void main(String[] args) {
		Client cl = new Client("localhost", 5000);

	}
	public Client(String ipaddr, int port) {
		JPanel pn=new JPanel();
		JPanel pn1=new JPanel();
		JPanel pn2=new JPanel();
		JPanel pn3=new JPanel();
		JPanel pn4=new JPanel();
		pn1.setLayout(new FlowLayout());

		pn2.setLayout(new FlowLayout());
		pn3.setLayout(new FlowLayout());
		pn4.setLayout(new FlowLayout());
		
		request = new JLabel("Request");
		respond = new JLabel("Respond");
		
		btn1=new JButton("Send");
		btn2=new JButton("Clean");
		
		btn1.addActionListener(this);
		btn2.addActionListener(this);
		
		input = new JTextField(30);
		output = new JTextArea(10, 40);
		
		pn1.add(request);
		pn3.add(respond);
		pn2.add(input);
		pn2.add(btn1);
		pn2.add(btn2);
		pn4.add(output);
		
		pn.add(pn1);
		pn.add(pn2);
		pn.add(pn3);
		pn.add(pn4);
		
		JFrame jf = new JFrame("Client");
		jf.add(pn);
		jf.setSize(500,350);//set the size
		jf.setLocation(600,400);//set the location		  
		jf.setVisible(true);
		try {
			soc = new Socket(ipaddr,port);
			output.setText("Connected to: localhost");
			DataInputStream in = new DataInputStream(soc.getInputStream());
			while(true){
				String result = in.readUTF();
				output.setText(output.getText() + "\nResult: " + result);
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn1) {
			try {
				DataOutputStream out = new  DataOutputStream(soc.getOutputStream());
				out.writeUTF(input.getText());
				output.setText(output.getText() + "\nRequest:" + input.getText());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource() == btn2) {
			input.setText(""); 
		}
		
	}

}

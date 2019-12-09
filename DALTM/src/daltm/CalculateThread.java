package daltm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CalculateThread extends Thread{
	Socket soc;
	Stack stack;
	Stack stackResult;
	Server server;
	public ArrayList<String> postFix;
	public CalculateThread(Socket clientSocket,Server server) {
		this.soc = clientSocket;
		stack = new Stack();
		stackResult = new Stack();
		postFix = new ArrayList();
		this.server = server;
	}
	public boolean isOperator(char c) {
		if(c=='+' || c=='-' || c=='/' || c=='*' || c=='^') {
			return true;
		}
		return false;
	}
	// rut gon dau +
	public String handleOperationPlus(String input) {
		String result = input;
		int i = 0;
		while(i<result.length()-1) {
			char c = result.charAt(i);
			if(i==0 && c=='+') {
				result = result.substring(i+1,result.length());
				continue;
			}
			if(c=='+' && result.charAt(i+1)=='+') {
				int j;
				for(j=i; j<result.length();j++) {
					if(result.charAt(j)!='+') {
						break;
					}
				}
				result = result.substring(0,i+1)+result.substring(j,result.length());
			}
			i++;
		}
		return result;	
	}
	// rut gon dau -
	public String handleOperationNegative(String input) {
		String result = input;
		int i = 0;
		while(i<result.length()) {
			char c = result.charAt(i);
			if(i!= 0 && c=='-' && isOperator(result.charAt(i-1))) {
				int j;
				for(j=i; j<result.length();j++) {
					if(isOperator(result.charAt(j))==false) {
						break;
					}
				}
				result = result.substring(0,i) + "(" +result.substring(i,j+1)+")"+result.substring(j+1,result.length());
			}
			i++;
		}
		i = 0;
		while(i<result.length()) {
			char c = result.charAt(i);        
		    if(i==0 && c=='-') {
		    	result= "0" + result;
		    	continue;
		    }
		    if(i!=0 && c=='-' && result.charAt(i-1)=='(') {
		    	result = result.substring(0,i) + "0" +result.substring(i,result.length());
		    }
		    i++;
		}
		return result;
	}
	public boolean isNumeric(String c) {
	    try {
	        int d = Integer.parseInt(c);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	// so sanh do uu tien op2: stackpeek
   public boolean hasPrecedence(String op1, String op2) 
    {
        if (op2.equals("(") || op2.equals(")")) 
            return false; 
        if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-"))) { 
            return false;
        }
        if (op1.equals("^") && (op2.equals("+") || op2.equals("-") || op2.equals("*") || op2.equals("/"))) 
            return false; 
        else
            return true; 
    }
   public boolean isTrigonometric(String s) {
	   if(s.equals("sin") || s.equals("cos") || s.equals("tan") || s.equals("cot") || s.equals("log") || s.equals("sqrt")) {
		   return true; 
	   }
	   return false;
   }
	  
	public void goThrough(String s) {
		String temp="";
		String trigonometric="";
		for (int i = 0; i < s.length(); i++){
		    char c = s.charAt(i);	    
		    if(isNumeric(String.valueOf(c))==true) {
		    	temp += c;
		    }else if(Character.isAlphabetic(c)==true) {
		    	trigonometric += c;
		    }else if(c==')') {
		    	if(temp!="") {
			    	postFix.add(temp);
			    	temp = "";
		    	}
		    	//pop het cho den khi gap (
		    	String t = String.valueOf(stack.pop());
		    	while(t!="(") {
		    		postFix.add(t);
		    		t =  String.valueOf(stack.pop());
		    	}
		    // toan tu hoac chay chet vong lap
		    }else {
		    	if(temp!="") {
			    	postFix.add(temp);
			    	temp = "";	
		    	}
		    	if(c=='+' || c=='-' || c=='/' || c=='*' || c=='^') {
		    		String a = "";
		    		String b = "";
		    		while(!stack.empty() && hasPrecedence(b = Character.toString(c), a=(String) stack.peek())==true) {
			    		postFix.add(String.valueOf(stack.pop()));
			    	}
			    	stack.push(String.valueOf(c));
		    	}else {
		    		if(isTrigonometric(trigonometric)){
			    		stack.push(trigonometric);
			    		trigonometric = "";
			    	}
			    	stack.push("(");
		    	}	    	
		    }
		    //het vong lap		    		    
	    	if(i ==s.length()-1) {
		    	if(temp!="") {
			    	postFix.add(temp);
		    	}
	    		while(!stack.empty()) {
	    			postFix.add(String.valueOf(stack.pop()));
	    		}
	    	}
		}
	}
	public double applyOp(String op, double a, double b) 
    { 
        switch (op) 
        { 
        case "+": 
            return b + a; 
        case "-": 
            return b - a; 
        case "*": 
            return b * a; 
        case "/": 
            try {
            	return b/a;
			} catch (Exception e) {
				e.printStackTrace();			
			}
            
        case "^":
        	return Math.pow(b,a);
        } 
        return 0; 
    } 
	public double result(ArrayList<String> p) {
		for (String s : p) {
		    if(isNumeric(s)) {
		    	stackResult.push(s);
		    }else {
		    	if(isTrigonometric(s)) {
		    		double a = Double.parseDouble(stackResult.pop().toString());
		    		switch (s) {
					case "sin":
						a = Math.PI/180*a;
			    		stackResult.push(Math.sin(a));
						break;
					case "cos":
						a = Math.PI/180*a;
			    		stackResult.push(Math.cos(a));
						break;
					case "tan":
						a = Math.PI/180*a;
			    		stackResult.push(Math.tan(a));
						break;
					case "cot":
						a = Math.PI/180*a;
			    		stackResult.push(1/(Math.tan(a)));
						break;
					case "log":
						stackResult.push(Math.log(a));
						break;
					case "sqrt":
						stackResult.push(Math.sqrt(a));
						break;
					}

		    	}else {
		    		int k = 0;
		    		double a = 0,b = 0;
		    		if(stackResult.isEmpty()==false) {
		    			String s1 = stackResult.pop().toString();
		    			a = Double.parseDouble(s1);
		    			k++;
		    		}else {
		    			return 9999999;
		    		}
		    		if(stackResult.isEmpty()==false) {
		    			String s2 = stackResult.pop().toString();
		    			b = Double.parseDouble(s2);
		    			k++;		    	
		    		}else {
		    			return 9999999;
		    		}	    		
			    	if(k==2) {
			    		double result = applyOp(s, a, b);
			    		stackResult.push(String.valueOf(result));
			    	}else {
			    		return 9999999;
			    	}
		    	}
		    }
		}
		return Double.parseDouble(stackResult.pop().toString());
	}
	public void printPostfix(ArrayList<String> p) {
		for (String s : p) {
		    System.out.print(s+" ");
		}
		System.out.println("");
	}
	
	
	
	public void run() {
		DataInputStream in = null;
		DataOutputStream out = null;
		try {
			in = new DataInputStream(soc.getInputStream());
			out = new  DataOutputStream(soc.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String input = "";
		String[] splitInput;
		String temp = "";
		while(true) {
			try {
				input = in.readUTF();
				System.out.println(input);
				splitInput = input.split(":");
				input = splitInput[1];				
				server.output.setText(server.output.getText()+splitInput[0]+": "+splitInput[1]+"\n");
				if(input.equals("hostip")) {
					String ip = InetAddress.getLocalHost().toString();
					out.writeUTF(ip);
				}else {
					temp = handleOperationPlus(input);
					temp = handleOperationNegative(temp);
					goThrough(temp.replaceAll("\\s+",""));
					System.out.println("get here ok");
					if(this.postFix.isEmpty()) {
						out.writeUTF("0");
					}else {
						System.out.println(this.postFix);
						double result = result(this.postFix);
						server.output.setText(server.output.getText() + "Result: " +result +"\n");
						Thread.sleep(100);
						if(result!=9999999) {
							out.writeUTF(result+"");
						}else {
							out.writeUTF("math error");
						}
						postFix.clear();
					}
				}
			} catch (Exception e) {
				
			}
		}
		
	}
}

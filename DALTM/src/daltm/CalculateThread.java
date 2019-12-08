package daltm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
	ServerSocket server;
	public ArrayList<String> postFix;
	public CalculateThread(Socket clientSocket) {
		this.soc = clientSocket;
		stack = new Stack();
		stackResult = new Stack();
		postFix = new ArrayList();
	}
	// Tien xu li chuoi
	public boolean isOperator(char c) {
		if(c=='+' || c=='-' || c=='/' || c=='*' || c=='^') {
			return true;
		}
		return false;
	}
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
	// so sanh do uu tien
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
		    	String t = String.valueOf(stack.pop());
		    	while(t!="(") {
		    		postFix.add(t);
		    		t =  String.valueOf(stack.pop());
		    	}	
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
			    	double a = Double.parseDouble(stackResult.pop().toString());
			    	double b = Double.parseDouble(stackResult.pop().toString());
			    	double result = applyOp(s, a, b);
			    	stackResult.push(String.valueOf(result));
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
		String temp = "";
		while(true) {
			try {
				input = in.readUTF();
				temp = handleOperationNegative(input);
				System.out.println(temp);
				goThrough(temp.replaceAll("\\s+",""));
				double result = result(this.postFix);
				Thread.sleep(100);
				out.writeUTF(this.postFix.toString());
				out.writeUTF(result+"");
				postFix.clear();
			} catch (Exception e) {
				
			}
		}
		
	}
}

package daltm;

public class test {
	public static boolean isOperator(char c) {
		if(c=='+' || c=='-' || c=='/' || c=='*' || c=='^') {
			return true;
		}
		return false;
	}
	public static boolean isTrigonometric(String s) {
		   if(s.equals("sin") || s.equals("cos") || s.equals("tan") || s.equals("cot") || s.equals("log") || s.equals("sqrt")) {
			   return true; 
		   }
		   return false;
	}
	public static boolean isNumeric(String c) {
	    try {
	        double d = Double.parseDouble(c);
	    } catch (NumberFormatException | NullPointerException nfe) {
	        return false;
	    }
	    return true;
	}
	public static void main(String[] args) {
		String result = "sin(90)+cos90+tan90+cot90+log90+sqrt90";
		int i = 0;
		String temp = "";
		while(i<result.length()-1) {
			char c = result.charAt(i);
			if(Character.isAlphabetic(c)==true) {
				temp += c;
			}else {
				i++;
				continue;
			}
			if(isTrigonometric(temp)==true) {
				if(result.charAt(i+1)!='(') {
					int j = i+1;
					for(;j<result.length();j++) {
						if(isNumeric(String.valueOf(result.charAt(j)))==true || result.charAt(j)=='.') {
							continue;
						}else {
							break;
						}
					}
					result = result.substring(0,i+1)+"("+result.substring(i+1,j)+")"+result.substring(j,result.length());
					temp="";
				}else {
					temp="";
				}
			}
			i++;
		}
		System.out.println(result);
	}

}

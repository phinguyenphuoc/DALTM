package daltm;

public class test {
	public static boolean isOperator(char c) {
		if(c=='+' || c=='-' || c=='/' || c=='*' || c=='^') {
			return true;
		}
		return false;
	}
	public static void main(String[] args) {
		String s = "2+-4/-2*-3";
		int i = 0;
		while(i<s.length()) {
			char c = s.charAt(i);
			if(c=='-' && isOperator(s.charAt(i-1))) {
				int j;
				for(j=i; j<s.length();j++) {
					if(isOperator(s.charAt(j))==false) {
						break;
					}
				}
				s = s.substring(0,i) + "(" +s.substring(i,j+1)+")"+s.substring(j+1,s.length());
			}
			i++;
		}
		System.out.println(s);
		int a = 2+(-4);
	}

}

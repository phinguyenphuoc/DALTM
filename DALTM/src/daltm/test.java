package daltm;

public class test {
	public static boolean isOperator(char c) {
		if(c=='+' || c=='-' || c=='/' || c=='*' || c=='^') {
			return true;
		}
		return false;
	}
	public static void main(String[] args) {
		String result = "++++1+++++++++++1";
		int i = 0;
		while(i<result.length()) {
			char c = result.charAt(i);
			if(i==0 && c=='+') {
				result = result.substring(i+1,result.length());
				System.out.println(result);
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
				System.out.println(result);
			}
			i++;
		}
		System.out.println(result);
	}

}

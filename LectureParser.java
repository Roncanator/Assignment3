/************************************************************************
 *
 * Example parser that was covered in the lecture for practice
 *
 * E -> T
 * E -> T E
 * T -> F
 * T -> F*
 * T -> F+T
 * F -> v
 * F -> (E)
 *
 ************************************************************************/


public class LectureParser {

    public static void main(String[] args){
	parse();
    }

    public static int j = 0;
    //Need to add '\0' at the end for end of line symbol
    public static char p[] = {'a', '*', 'b', '+', 'c', '\0'};
    public static char[] SPECIAL_CHARS = {'*', '+', '(', ')'};

    private static boolean isVocab(char x) {
	for (char s : SPECIAL_CHARS) {
	    if (s == x)
		return false;
	}
		
	return true;
    }

    public static void error(){
	System.out.println("Error");
    }
    
    public static void expression(){
	term();
	if(p[j] != '\0'){
	    expression();
	}
    }

    public static void term(){
	factor();
	if(p[j] == '*'){
	    j++;
	}else if(p[j] == '+'){
	    j++;
	    term();
	} return;
    }

    public static void factor(){
	if(isVocab(p[j])){
	    j++;
	} else {
	    if(p[j] == '('){
		j++;
		expression();
		if(p[j] == ')'){
		    j++;
		} else {
		    error();
		}
	    }
	    else {
	    	error();
	    }
	}
    }

    public static void parse(){
	expression();
	if(p[j] != '\0'){
	    error();
	}
    }
}

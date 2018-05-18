public class compile {
    public static int j = 0;
    public static char[] p;
    public static char[] notVocab = {'.', '*', '+', '?', '!', '(', ')', '[', ']'};
    public static int at = 0;
    
    public static void main(String [] args){
	try {
	    if(args.length == 1){
		//create a char array of the given regex expression
		p = args[0].toCharArray();	    
		expression();
		error("out of expression at top of code");
		if(p[j] != '\0'){
		    error("The String is not a complete Regex Pattern");
		    return;
		}
		error("have a full regex");
	    }
	    else{
		System.err.println("USAGE: Java REcompile \"Regex Pattern\" ");
		return;
	    }
	}
	catch(Exception e){
	    System.err.println(e.getMessage());
	}
	
    }
    public static void expression(){
	error("into expression");
	term();
	if(p[j] != '\0')
	    expression();
    }
    public static void term(){
	error("into term");
	factor();
	if(p[j] == '*')
	    j++;
	else if(p[j] == '+'){
	    j++;
	    term();
	}
    }
    public static void factor() {
	error("into factor");
	if (isVocab(p[j]))
	    j++;
	else if (p[j] == '('){
	    j++;
	    expression();
	    if(p[j] != ')')
		error("havent found matching brackets");
	    else
		j++;
	}
	else
	    error("factor is not apart of the vocab");
    }

    public static Boolean isVocab(char c){
	error("into isVocab");
	for(char n : notVocab){
	    if(c == n)
		return false;
	}
	at++;
	System.err.println(Integer.toString(at));
	return true;
    }
    
    //Prints to standard error the error message/line given
    public static void error(String line){
	System.err.println(line);
    }
    public static void error(char c){
	System.err.println(c);
    }
}

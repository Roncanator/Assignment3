/*******************************************************
 * 
 *          Alexander Bakx - 1283648
 *          Riley Cochrane - 1218251
 *
 *          Using the following rules:
 *          E = Expression
 *          T = Term
 *          D = Disjunction
 *          F = Factor
 *          L = List
 *          v = Vocab item (literal)
 *          
 *          E -> D
 *          E -> DE
 *          D -> T
 *          D -> T | D
 *          T -> F
 *          T -> F*
 *          T -> F+
 *          T -> F?
 *          F -> \v
 *          F -> v
 *          F -> . (wild card)
 *          F -> [L]
 *          F -> (E)
 *          L -> v
 *          L -> vL
 *
 *          Still to implement: ![L]!
 *
 *******************************************************/

//Need to import the data structures being used
import java.util.ArrayList;
import java.util.List;

public class REcompile {
    //Lists to store the FSM
    List<String> literal = new ArrayList<>();
    List<Integer> next1 = new ArrayList<>();
    List<Integer> next2 = new ArrayList<>();

    //Index of the symbol in p that is being parsed
    public int j;
    //Keeps track of the state currently being build
    public int state;

    //Char array containing the regexp
    public char[] p;
    //All special characters that are part of the regular grammar
    public char[] notVocab = {'|', '.', '*', '+', '?', '\\', '(', ')', '[', ']'};

    /*****************************************************
     *     Compiles the FSM
     *****************************************************/
    public void compile() throws IllegalArgumentException{
	int initial;

	//Set index of the symbol in p to be the first symbol
	j = 0;
	//Set the state currently being build to 1
	state = 1;

	//Placeholder state for the initial state
	setState(0, "NULL", 0, 0);
	initial = expression();
	setState(0, initial, initial);

	//If the index is not at the last position of the regexp
	if (j != p.length){
	    error();
	}

	//State to point to the start of the FSM
	setState(state, "NULL", 0, 0);
    }

    /*****************************************************
     *     EXPRESSION
     *****************************************************/
    private int expression() throws IllegalArgumentException{
	int r, end1, start2;

	r = disjunction();
	end1 = state-1;

	//Check if we've reached the end of the regexp
	if (j == p.length){
	    //Returns initial state for this machine
	    return r;
	}

	//If a valid char follows
	if (isVocab(p[j]) ||
	    p[j] == '\\' ||
	    p[j] == '(' ||
	    p[j] == '.' ||
	    p[j] == '['){
	    start2 = expression();
	    setState(end1, start2, start2);
	}
	//Returns initial state for this machine
	return r;
    }

    /*****************************************************
     *     Calculates a disjunction
     *****************************************************/
    private int disjunction() throws IllegalArgumentException{
	
	int r, start1, end1, start2, end2;

	//Get start state and end state for the term
	start1 = term();
	end1 = state-1;
	r = start1;

        //Check if we've reached the end of the regexp
	if (j == p.length){
	    //Returns initial state for this machine
	    return r;
	}

	//Check if it is a disjunction
	if (p[j] == '|'){
	    //Consume
	    j++;
	    //Branching state to the start of the term, and the start of the disjunction
	    setState(state, "NULL", start1, state+1);
	    r = state;
	    //A new state is created therefore state gets incremented
	    state++;

	    //Get start state and end state for the disjunction
	    start2 = disjunction();
	    end2 = state-1;

	    //Update the branching state to point to the start of the term and the new disjunction
	    setState(r, start1, start2);

	    //End state for this machine
	    setState(state, "NULL", state+1, state+1);
	    //A new state is created therefore state gets incremented
	    state++;

	    //Point both terms to the end state
	    setState(end1, state-1, state-1);
	    setState(end2, state-1, state-1); 
	}
        //Returns initial state for this machine
	return r;
    }

    /*****************************************************
     *     Calcualates a term
     *****************************************************/
    private int term() throws IllegalArgumentException{
	
	int r, start;

	//Get the starting state for this machine
	start = state-1;

	r = factor();

        //Check if we've reached the end of the regexp
	if (j == p.length){
	    //Returns initial state for this machine
	    return r;
	}

	//T -> F* (zero or more)
	if (p[j] == '*'){
	    //Branching state that points to the factor and the state after the factor
	    setState(state, "NULL", r, state+1);
	    r = state;
	    //A new state is created therefore state gets incremented
	    state++;

	    //Consume
	    j++;
	}

	//T -> F+ (one or more)
	else if (p[j] == '+'){
	    //Branching state that points to the faction, and the state after the faction
	    setState(state, "NULL", r, state+1);
	    //A new state is created therefore state gets incremented
	    state++;
	    //Consume
	    j++;
	}

	//T -> F? (zero or one)
	else if (p[j] == '?'){
	    //Branching state that points to the faction, and the state after the faction
	    setState(state, "NULL", r, state+1);
	    r = state;
	    //A new state is created therefore state gets incremented
	    state++;

	    //End state for this machine
	    setState(state, "NULL", state+1, state+1);
	    //A new state is created therefore state gets incremented
	    state++;

	    //Point the factor to the end state
	    setState(r, state-1, state-1);
	    r = state-1;
	    //Consume
	    j++;
	}

        //Returns initial state for this machine
	return r;

    }

    /*****************************************************
     *     Calculates a factor
     *****************************************************/
    private int factor() throws IllegalArgumentException {
	int r;
	r = state;

	//Escaped chars
	if (p[j] == '\\'){
	    //Consume
	    j++;

	    //Update state to contain caracters
	    setState(state, String.valueOf(p[j]), state+1, state+1);
	    //A new state is created therefore state gets incremented
	    state++;

	    //Consume
	    j++;
	}

	//Literals
	else if (isVocab(p[j])){
	    //Update state to contain character
	    setState(state, String.valueOf(p[j]), state+1, state+1);
	    //A new state is created therefore state gets incremented
	    state++;
	    //Consume
	    j++;
	}

	//Wild card
	else if (p[j] == '.'){
	    //Update state to contain character
	    setState(state, "WILD", state+1, state+1);
	    //A new state is created therefore state gets incremented
	    state++;
	    //Consume
	    j++;
	}

	//List of literals
	else if(p[j] == '['){
	    //Consume
	    j++;

	    //Get starting state of the list and build the rest of the list machine
	    r = state;
	    list();

	    //Branching state to catch all loose ends
	    setState(state, "NULL", state+1, state+1);
	    //A new state is created therefore state gets incremented
	    state++;

	    //Ensure that the list was valid
	    if (p[j] == ']'){
		//Consume
		j++;
	    } else {
		//If not a valid list, error
		error();
	    }
	}


	//Raise precedence
	else if (p[j] == '('){
	    //Consume
	    j++;
	    r = expression();
	    if (p[j] == ')'){
		//Consume
		j++; 
	    } else {
		//If no closing ), error
		error();
	    }
	}

	else{
	    error();
	}

        //Returns initial state for this machine
	return r;
    }

    /*****************************************************
     *     Calculates a list
     *****************************************************/
    private void list() throws IllegalArgumentException{
	//Ensure that the list is not empty
	if(p[j] != ']'){
	    //Check if we've reached the end of the regexp
	    if(j == p.length){
		error();
	    }

	    //Look ahead to see if it's the last item in the list
	    boolean last = p[j+1] == ']';

	    //Branching state to point to the literal and if necessary to the next branching state of the list
	    setState(state, "NULL", last ? state+1 : state+2, state+1);
	    //A new state is created therefore state gets incremented
	    state++;

	    //Keep track of the position of the literal
	    int literal = state;
	    //State to consume the literal
	    setState(state, String.valueOf(p[j]), -1, -1);
	    //A new state is created therefore state gets incremented
	    state++;
	    //Consume
	    j++;

	    //If it wasn't the last item in the list, call list() again
	    if (!last){
		list();
	    }

	    //Set the literal states end to the end of the list
	    setState(literal, state, state);
	}
    }

    /*****************************************************
     *     Sets a state 
     *****************************************************/
    private void setState(int state, String s, int n1, int n2){
        

	//The new state will replace an exisiting state
	if(state < literal.size()){
	    //Remove state
	    literal.remove(state);
	    next1.remove(state);
	    next2.remove(state);

	    //Add state at position where we removed state
	    literal.add(state, s);
	    next1.add(state, n1);
	    next2.add(state, n2);

	}
	//The new state is a new state
	else {
	    //Add state at the end of the list
	    literal.add(s);
	    next1.add(n1);
	    next2.add(n2);
	}
 
    }

    /*****************************************************
     *     Sets a state
     *****************************************************/
    private void setState(int state, int n1, int n2){
	//Keep track of state getting removed
	String removeLiteral = literal.remove(state);
        int removeNext1 = next1.remove(state);
        int removeNext2 = next2.remove(state);

	//If both new pointers point to the same state
	if (n1 == n2){
	    //If the previous two pointers point to the same state
	    if(removeNext1 == removeNext2){
		removeNext1 = n1;
		removeNext2 = n2;
	    }
	    //If they don't point to the same palce
	    else{
		removeNext2 = n1;
	    }
	} else {
	    removeNext1 = n1;
	    removeNext2 = n2;
	}

	//Add state at position where we removed state
	literal.add(state, removeLiteral);
	next1.add(state, removeNext1);
	next2.add(state, removeNext2);
    }

    
    /*****************************************************
     *     Main method
     *****************************************************/
    public static void main(String [] args){
	try {
	    //Check if only 1 argument was given
	    if(args.length == 1){
		//Create a char array of the given regex expression	    
		REcompile c = new REcompile(args[0]);
		c.compile();

		//Output the FSM 
		for(int i = 0; i < c.getLength(); i++){
		    System.out.printf("%s|%s|%s|%s\n", i, c.getLiteral(i), c.getNext1(i), c.getNext2(i));
		}
		//System.out.printf("%s%s%s%s\n", i, c.getLiteral(i), c.getNext1(i), c.getNext2(i));
	    }
	    //If more than one argument
	    else{
		//Display error message
		System.err.println("Usage: java REcompile \"Regex Pattern\"");
	    }
	}
	catch(Exception e){
	    System.err.println(e.getMessage());
	}
	
    }

    /*****************************************************
     *     Checks if a char is a valid vocab item
     *****************************************************/
    private Boolean isVocab(char c){
	for(char n : notVocab){
	    if(c == n)
		return false;
	}
	return true;
    }

    /*****************************************************
     *     Returns the literal at position i
     *****************************************************/
    public String getLiteral(int i){
	return literal.get(i);
    }

    /*****************************************************
     *     Returns the next state at position i
     *****************************************************/
    public int getNext1(int i){
	return next1.get(i);
    }

    /*****************************************************
     *     Returns the next two state at position i
     *****************************************************/
    public int getNext2(int i){
	return next2.get(i);
    }

    /*****************************************************
     *     Returns the length of the FSM
     *****************************************************/
    public int getLength(){
	return literal.size();
    }

    /*****************************************************
     *     Creates a new char array from a string
     *****************************************************/
    public REcompile(String exp){
	p = exp.toCharArray();
    }

    /*****************************************************
     *     Throws a new exception
     *****************************************************/
    private void error(){
	throw new IllegalArgumentException("Input is not a valid regexp.");
    }
}

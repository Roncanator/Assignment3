/*******************************************************
 * 
 *          Alexander Bakx - 1283648
 *          Riley Cochrane - 1218251
 *
 *******************************************************/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class REsearch {
    //Lists to store the FSM
    static List<Integer> stateNumber = new ArrayList<>();
    static List<String> literal = new ArrayList<>();
    static List<Integer> next1 = new ArrayList<>();
    static List<Integer> next2 = new ArrayList<>();
    
    public static void main(String[] args){
	//REsearch should have no arguments
	if(args.length!=1){
	    System.err.println("Usage: Java REcompile \"REGEX STRING\" | java REsearch file");
	    return;
	}
	//create a reader to read REcompile's output
	BufferedReader brRE = new BufferedReader(new InputStreamReader(System.in));
	//parse all the REcompile output into arrayLists
	try{
	    String line = brRE.readLine();
	    while(line != null){
	        String[] split = line.split("\\|");
		stateNumber.add(Integer.parseInt(split[0]));
		literal.add(split[1]);
		next1.add(Integer.parseInt(split[2]));
		next2.add(Integer.parseInt(split[3]));
		line = brRE.readLine();
	    }
	}
	catch(Exception e){
	    System.err.println(e.getMessage() + e.getStackTrace()[0].getLineNumber());
	    }
	//Get the absolute filepath of the given file
	String filepath = new File("").getAbsolutePath() +"/"+ args[0];
	try{
	    //Read each Line in the file
	    BufferedReader brFile = new BufferedReader(new FileReader(filepath));
	    String fileLine = brFile.readLine();
	    while(fileLine!=null){
	        StringReader reader = new StringReader(fileLine);
		int character = reader.read();
		String got = literal.get(1).toString();
		char gotten = got.charAt(0);
		while(character!= -1){
		    if(character == gotten){
			System.err.println(fileLine);
			break;
		    }
		    character = reader.read();
		}
		fileLine = brFile.readLine();
	    }
	}
	catch(Exception e){
	    System.err.println(e.getMessage() + e.getStackTrace()[0].getLineNumber());
	}
    }
}

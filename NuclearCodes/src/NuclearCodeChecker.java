//Nuclear Code Checker
//Paul M. Grossman 6/17/2018

//Create a lists of random active and stolen Launch codes.
//Find reversed stolen codes that exist in the Active code list
//Does not replicate codes.
//Checks for total combination limitation
//Stolen codes generation limited by available Active codes 


import java.util.*;


public class NuclearCodeChecker
{

	//Create HashMaps for quick validation
	
	static HashMap<String, String> activeCodes = new HashMap<>();
	static HashMap<String, String> stolenCodes = new HashMap<>();
	
	/*	Main
	Creates an Active Code list
	Creates a Stolen Code list
	Compares all reversed Stolen codes against the Active list
 */	

	
	public static void main(String[] args) {

		//unitTests();        
		
		//Length of the Launch code to generate
		final int CODE_LENGTH = 5;
		
		//Amount of Active Codes to generate
		final int ACTIVE_CODE_COUNT = 1000;
		
		//Amount of Stolen Codes to generate
		final int STOLEN_CODE_COUNT = 1000;
		
		
		//Create a list of Active Nuclear Codes
		generateLaunchCodeList (ACTIVE_CODE_COUNT, CODE_LENGTH); //Generate 10 codes 3 characters long
		print ("---------------------");
		
		//Create a list of Stolen Nuclear codes
		generateStolenLaunchCodeList (STOLEN_CODE_COUNT, CODE_LENGTH); //Generate 10 codes 3 characters long
		print ("---------------------");
		
		//check the active list for reverse stolen codes that are active  
		VerifyReverseLaunchCodeList();
	
	}



	
	//Supporting classes //
	
	public static void unitTests (){
	    
	    //generateCode (3);  //Generates a 3 digit launch code
		//generateLaunchCodeList (11, 1); //Generate more codes than possible combinations (Negative test) PASS
		//generateLaunchCodeList (101, 2); //Generate more codes than possible combinations (Negative test) PASS
		
		//Note: Failed (infinite loop) when run at end of Main()  
		//generateLaunchCodeList (1001, 3); //Generate more codes than possible combinations (Negative test) Pass
		
		//generateLaunchCodeList (10001, 4); //Generate more codes than possible combinations (Negative test) PASS
		//generateLaunchCodeList (100001, 5); //Generate more codes than possible combinations (Negative test) PASS
		//generateLaunchCodeList (1000001, 6); //Generate more codes than possible combinations (Negative test) PASS
		
		//Exceed combinations
		//generateLaunchCodeList (11, 1); //Request more than 10 codes 1 character long
		//generateStolenLaunchCodeList (11, 1); //Request more codes than can be stolen
	    
		
		
	}
	

	
    //Simplify print for code development
	public static void print (String output) {
		System.out.println(output);
	}
	
	public static void print (int output) {
		System.out.println(output);
	}
	
    
	/*	Create a list of LaunchCodes
		Dynamic in amount
		Dynamic on length
	*/
    public static void generateLaunchCodeList (int totalCodes, int length){
    	
    	String code = "";
    	
    	//Flag to indicate a unique code was generated
    	boolean unique = false;
    	
    	//Avoid requesting more codes than possible combinations
    	if (totalCodes > Math.pow(10, length)) {
    		
    		print (totalCodes + " requested Active codes exceed total possible combinations of " +  Math.pow(10, length));  
    		totalCodes =  (int) Math.pow(10, length);
    		totalCodes--; //Gurantee One Stolen code
    	}
    	
    	print ("Generate " + totalCodes + " Active Launch Codes");
    	
    	//Create as many codes as requested
    	for (int i = 0; i< totalCodes; i++ ) {
    		while (! unique) {
    			
	    		code = generateCode(length);
	    		if (! activeCodes.containsKey(code)) {
		    		
	    			//Assumption: No replicated Launch Codes inlist
	    			activeCodes.put(code, code);
	    			print (code);
	    	    	
	    			unique = true;
	    		}else {
	    			//print ("Duplicate Active code generated: " + code);
	    		}
    		}	
    		//Reset the unique flag
    		unique = false;
    	}
    	
    }
    

	/*	Create a list of Stolen Launch Codes
		Dynamic in amount
		Dynamic on length
		Non repeating in Active Launch Code list
	*/
    public static void generateStolenLaunchCodeList ( int totalCodes, int length){
    	int availableCodes = 0;
    	String code = "";
    	
    	//Flag to indicate a non-active code was generated
    	boolean unique = false; 
    	

    	//Avoid requesting more codes than possible combinations
    	availableCodes = (int) (Math.pow(10, length) - activeCodes.size());
    	if (availableCodes < 0) {
    		availableCodes = 0;
    		print ("Active codes: "+ activeCodes.size());
    	}
    	
    	if (totalCodes > availableCodes) {
    		print ("Available Active Codes: " + availableCodes);
    		print (totalCodes + " requested Stolen codes exceed total possible combinations of " +  availableCodes);  	
    		totalCodes = availableCodes;
    	}
    	
    	
    	print ("Generate " + totalCodes + " Unique Stolen Launch Codes");
    	
    	//Create as many codes as requested
    	for (int i = 0; i < totalCodes; i++ ) {
    		while (! unique) {
	    		code = generateCode(length);
    			
	    		//Assumption: stolen Launch Codes have been deactivated 
    			//and will not appear in the Active list
	    		
	    		if (! activeCodes.containsKey(code)) {
	    		
	    			stolenCodes.put(code, code);
	    			print (code);
	    	    		
	    			unique = true;
	    		}else {
	    			print ("Duplicate Stolen code generated: " + code);
	    		}		
    		}
    	
    		unique = false;
    	
    	}
    	
    }

	/*	Create a Launch Code
		used by:  generateLaunchCodeList
				  generateStolenLaunchCodeList
	 */
    public static String generateCode (int Length) {
    	
    	String code = "";
    	
    	char randomChar;
    	int randomNumber;
    	
    	//Random Characters 0 - 9
    	int min = 48; 
    	int max = 57;
    	
    	//Can be extended to generate AlphaNumeric and Special Characters
    	for (int i = 0; i< Length; i++ ) {
    		//Generate random numeric launch code string of any length
    		randomNumber = (int)  (min + Math.random() * ((max - min) + 1)); //Ascii 48 - 57 0-9
    			
    		randomChar = (char) randomNumber; 
    		code = code + randomChar;   
    				
    	}
    	
    	return code;
    	
    }
    

	/*	verify if any reversed stolen launch codes
	 	appear in the Active list
	*/
    
    public static void VerifyReverseLaunchCodeList (){

    	
    	print ("Find Active Reversed Codes");
    	
		stolenCodes.forEach ((codeKey, code) -> { 
				
		String reversedCode = reverseString (code);
		
		if (activeCodes.containsKey(reversedCode)) {
			print ("Reverse Active code detected:" + reversedCode);
		}
		
		/*else {
			print ("Inactive code :" + reversedCode);
		}
		*/
		
		}) ;
	}

    /*	returns the reverse of the string
	used by:  VerifyReverseLaunchCodeList
			  
*/	
    
    public static String reverseString (String sInput) {
    	return new StringBuilder(sInput).reverse().toString();
	}

	
}



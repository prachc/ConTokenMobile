package rit.contoken;

import java.util.Random;

/** 
 * Continuous Token Generator
 *
 * @author Prach Chaisatien
 * @version 1.0 Build 001 Sep 1, 2011.
 */
public class ConToken {
	/**
	 * Generate the next token in the Continuous Token sequence indicated by <code>PrevToken</code>
	 * @param PrevToken is used to determine the next token in the Continuous Token sequence
	 * @param GenFlag indicate whether to generate the next token or not 
	 * @return the next token in the Continuous Token sequence
	 * <li>the length of the result will be the same as <code>PrevToken</code>
	 * <li><code>GenFlag = true</code> : return the next token
	 * <li><code>GenFlag = false</code> : return <code>PrevToken</code>
	 **/
	public static String gen(String PrevToken, boolean GenFlag){
		if(!GenFlag)
			return PrevToken;
		ConToken contoken = new ConToken();
		Token token = contoken.new Token(PrevToken);
		int[] primes = Prime.pickPrimes(0, token.size()/2);
		token.setKey(primes);
		return token.getRandToken();
	}
	
	/**
	 * Random and return a String of characters from <code>ASCII_TABLE</code>, its length indicated by <code>length</code> from <code>ASCII_TABLE</code>
	 * @param length indicates how long is the random characters
	 * @return a String of random characters from <code>ASCII_TABLE</code>
	 **/
	public static String random(int length){
		return ASCIITable.random(length);
	}

	/** 
	 * Token object
	 **/
	protected class Token {
		/** 
		 * indicates the pattern of how the generated characters is placed in the next token
		 **/
		private static final int PLACEMENT_KEY = 1;
		/** 
		 * contains the original characters of the token
		 **/
		private char[] full_chars;
		/** 
		 * indicates
		 **/
		private char[] sel_chars;
		/** 
		 * indicates
		 **/
		private char[] enc_chars;
		/** 
		 * contains the generated characters of the token
		 **/
		private char[] full_enc_chars;
		/** 
		 * indicates whether the original characters in <code>full_char</code> are taken to the generation process or not
		 **/
		private boolean[] taken;
		/** 
		 * indicates
		 **/
		private boolean[] placed;
		/** 
		 * set of prime numbers picked from <code>Prime</Prime>
		 * @see Prime
		 **/
		private int[] keys;

		/** 
		 * Constructor, set <code>full_char</code> and initialize <code>taken</code>, <code>full_enc_chars</code>, <code>placed</code>
		 * @param str is the original characters which is converted to <code>full_char</code> array
		 **/
		public Token(String str){
			full_chars = str.toCharArray();
			taken = new boolean[full_chars.length];
			for (int i = 0; i < taken.length; i++) {
				taken[i] = false;
			}

			full_enc_chars = new char[full_chars.length];
			for (int i = 0; i < full_enc_chars.length; i++) {
				full_enc_chars[i] = '-';
			}
			placed = new boolean[full_enc_chars.length];
			for (int i = 0; i < placed.length; i++) {
				placed[i] = false;
			}
		}

		/** 
		 * Set token generator keys (picked up from <code>Prime</code>) and begin the next token generation process
		 **/
		public void setKey(int[] keys){
			this.keys = keys;
			sel_chars = selectChars();
			enc_chars = encrypt();
			placeChars2();
		}

		public char selectChar(int index){
			if(index<full_chars.length)
				if(!taken[index]){
					taken[index] = true;
					return full_chars[index];
				}else{
					return selectChar(index+PLACEMENT_KEY);
				}
			else
				return selectChar(index%full_chars.length);
		}

		private char[] encrypt(){
			char[] enc_chars = new char[sel_chars.length];
			for (int i = 0; i < sel_chars.length; i++) {
				enc_chars[i] = ASCIITable.shift(sel_chars[i], ASCIITable.findIndex(sel_chars[i]));
			}
			return enc_chars;
		}

		public void placeChar(char c,int index){
			if(index<full_enc_chars.length){
				if(!placed[index]){
					placed[index] = true;
					full_enc_chars[index]=c;
				}else{
					placeChar(c,index+PLACEMENT_KEY);
				}
			}else{
				placeChar(c,index%full_enc_chars.length);
			}
		}

		/*private void placeChars1(){
			for (int i = 0; i < keys.length; i++) {
				placeChar(enc_chars[i],keys[keys.length-i-1]-1);
			}
		}*/

		private void placeChars2(){
			for (int i = 0; i < keys.length; i++) {
				placeChar(enc_chars[i],keys[i]*2-1);
			}
		}

		public String getNextToken(){
			return new String(full_enc_chars);
		}

		public String getRandToken(){
			return new String(getRand());
		}

		private char[] getRand(){
			char[] rand = full_enc_chars;

			for (int i = 0; i < placed.length; i++) {
				if(!placed[i]){
					rand[i] = ASCIITable.random();
				}
			}

			return rand;
		}

		public String getSelToken(){
			char[] temp = full_chars;
			for (int i = 0; i < temp.length; i++) {
				if(!taken[i])
					temp[i] = '-';
			}
			return new String(temp);
		}

		public char[] getSelChars(){
			return sel_chars;
		}

		public char[] getEncChars(){
			return enc_chars;
		}

		private char[] selectChars(){
			char[] sel_chars = new char[keys.length];
			for (int i = 0; i < keys.length; i++) {
				sel_chars[i] = selectChar(keys[i]-1);
			}

			return sel_chars;
		}

		public int size(){
			return full_chars.length;
		}
	}
	
	/** 
	 * Prime numbers used as keys to calculate Continuous Token
	 **/
	protected static class Prime {
		/** 
		 * Array of prime numbers contains 168 prime numbers in [0-1000] number range
		 **/
		private static final int[] PRIME_NUMBERS = 
			{   2,   3,   5,   7,  11,  13,  17,  19,  23,  29, 
			   31,  37,  41,  43,  47,  53,  59,  61,  67,  71, 
			   73,  79,  83,  89,  97, 101, 103, 107, 109, 113, 
			  127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 
			  179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 
			  233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 
			  283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 
			  353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 
			  419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 
			  467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 
			  547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 
			  607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 
			  661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 
			  739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 
			  811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 
			  877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 
			  947, 953, 967, 971, 977, 983, 991, 997}; 
		
		/** 
		 * Pick up and return array of prime numbers from <code>PRIME_NUMBERS<code>
		 * @param key indicates the position of a number in <code>PRIME_NUMBERS<code> to pick
		 * @param bit indicates how many prime numbers to pick
		 * @return array of prime numbers picked from <code>PRIME_NUMBERS<code>
		 * <li>example: pickPrimes(1,2) returns {3,5}
		 **/
		
		public static int[] pickPrimes(int key,int bit){
			//0-164
			int[] primes = new int[bit];
			//System.out.println("PRIME_NUMBERS.length-bit="+(PRIME_NUMBERS.length-bit));
			if(key<(PRIME_NUMBERS.length-bit))
				for (int i = 0; i < primes.length; i++) {
					primes[i] = PRIME_NUMBERS[key+i];
				}
			else
				for (int i = 0; i < primes.length; i++) {
					//System.out.println("key+i="+(key+i));
					if((key+i)<PRIME_NUMBERS.length){
						//System.out.println("key+i="+(key+i));
						primes[i] = PRIME_NUMBERS[key+i];
					}else{
						//System.out.println("(key+i)%primes.length="+(key+i)%primes.length);
						primes[i] = PRIME_NUMBERS[(key+i)%primes.length];
					}
				}
			return primes;
		}
	}
	
	/** 
	 * ASCII character table used in Continuous Token
	 *
	 * @author Prach Chaisatien
	 * @version 1.0 Build 001 Sep 1, 2011.
	 */
	protected static class ASCIITable {
		/** 
		 * Array of ASCII Table 
		 * <code>0-9a-zA-z</code> = 10+26+26 = 62 characters 
		 **/
		private static char[] ASCII_TABLE = 
			{'0','1','2','3','4','5','6','7','8','9',
			 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			 'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
			 }; 
		
		/**
		 * Return shifted character to a position indicated by <code>pos</code>
		 * @param c a character to shift
		 * @param pos position to shift
		 * @return shifted character
		 **/
		public static char shift(char c,int pos){
			int index = findIndex(c);
			
			if((index+pos)<ASCII_TABLE.length)
				return ASCII_TABLE[index+pos];
			else
				return ASCII_TABLE[(index+pos)%ASCII_TABLE.length];
		}
		
		/**
		 * Find and return the index of <code>c</code> in the <code>ASCII_TABLE</code>
		 * @param c a character to find
		 * @return the index of <code>c</code> in the <code>ASCII_TABLE</code>
		 **/
		public static int findIndex(char c){
			if(c>='0'&&c<='9')
				return c-'0';
			else if(c>='a'&&c<='z')
				return 10+(c-'a');
			else if(c>='A'&&c<='Z')
				return 36+(c-'A');
			else return 0;
		}
		
		/**
		 * Random and return 1 character from <code>ASCII_TABLE</code>
		 * @return a random character from <code>ASCII_TABLE</code>
		 **/
		public static char random(){
			Random r = new Random(System.nanoTime());
			int rand = Math.abs(r.nextInt());
			return ASCII_TABLE[rand%ASCII_TABLE.length];
		}
		
		/**
		 * Random and return a String of characters from <code>ASCII_TABLE</code>, its length indicated by <code>length</code> from <code>ASCII_TABLE</code>
		 * @param length indicates how long is the random characters
		 * @return a String of random characters from <code>ASCII_TABLE</code>
		 **/
		public static String random(int length){
			char[] temp = new char[length];
			
			for (int i = 0; i < temp.length; i++) {
				Random r = new Random(System.nanoTime());
				int rand = Math.abs(r.nextInt());
				
				
				temp[i] = ASCII_TABLE[rand%ASCII_TABLE.length];
			}
			
			return String.valueOf(temp);
		}
		
		/**
		 * Verify and return true if all characters in <code>str</code> in <code>ASCII_TABLE</code>
		 * @param str the string to verify
		 * @return <code>true</code> if all characters in <code>str</code> in <code>ASCII_TABLE</code>, <code>false</code> if a character in <code>str</code> is not in <code>ASCII_TABLE</code>
		 **/
		public static boolean isMember(String str){
			char[] c = str.toCharArray();
			
			for (int i = 0; i < c.length; i++) {
				if((c[i]>='0'&&c[i]<='9')||(c[i]>='a'&&c[i]<='z')||(c[i]>='A'&&c[i]<='Z'))
					continue;
				else
					return false;
			}
			
			return true;
		}
	}

	/*public static boolean checkConToken(String PrevToken, String CurrToken){
		if(!ASCIITable.isMember(CurrToken))
			return false;

		Token token = new Token(PrevToken);
		int[] primes = Prime.pickPrimes(0, token.size()/2);
		token.setKey(primes);

		char[] CheckArray = token.getNextToken().toCharArray();
		char[] CurrArray = CurrToken.toCharArray();

		if(CheckArray.length!=CurrArray.length)
			return false;

		boolean check = true;

		for (int i = 0; i < CheckArray.length; i++) {
			if(CheckArray[i]=='-')
				continue;
			else if(CheckArray[i]!='-'&&CurrArray[i]==CheckArray[i])
				check&=true;
			else{
				check=false;
				break;
			}
		}

		return check;
	}

	public static void main(String[] args) {
		String prev = "abcdefgh";
		String next = ConToken.genConToken(prev);
		System.out.println(next);
		System.out.println(ConToken.checkConToken(prev,next));
		System.out.println(ConToken.checkConToken(prev,"AsAmAowA"));

		/*String input = "abcdefghijklmnop";
		//String input = "abcdefghijklmnopqrstuvwxyz012345";
		//String input = "abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz01";
		System.out.println(input);

		Token token = new Token(input);
		int[] primes = Prime.pickPrimes(0, token.size()/2);
		token.setKey(primes);
		System.out.println(token.getSelToken());

		printArray(token.getSelChars());
		printArray(token.getEncChars());
		System.out.println(token.getNextToken());
		String nextinput = token.getRandToken();

		System.out.println("----------------------------------------");

		System.out.println(nextinput);
		token = new Token(nextinput);
		token.setKey(primes);
		System.out.println(token.getSelToken());
		printArray(token.getSelChars());
		printArray(token.getEncChars());
		System.out.println(token.getNextToken());
		nextinput = token.getRandToken();
		System.out.println(nextinput);
	}

	public static void printArray(int[] array){
		for (int i = 0; i < array.length; i++) {
			System.out.print("["+array[i]+"] ");
		}
		System.out.println();
	}

	public static void printArray(char[] array){
		for (int i = 0; i < array.length; i++) {
			System.out.print("["+array[i]+"] ");
		}
		System.out.println();
	}*/
}

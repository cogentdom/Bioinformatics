import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.Scanner;
/**
 * The driver method for creating the BTree 
 * @author Dominik Huffield 
 * @author Hailee Kiesecker
 */
public class GeneBankCreateBTree {
	static BTree tree;
	static int subSize = 0; // sequence length
	static int degree = 0; // degree of tree
	static File file;
	static File dump;
	static int blockSize = 4096;
	static File file1;
	static boolean debug = false;
	/**
	 * The main method for constructing the BTree using the gene data 
	 * @param args  java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]
	 */ 
	public static void main(String args[]) {
		try {
			// Checks to see if the correct amount of parameters were presented 
			if (args.length < 4 || args.length > 6) {
				System.err.println("Incorrect number of arguments");
				printUsage();
			}
			//Initializes the cache
			if (Integer.parseInt(args[0]) == 0 || Integer.parseInt(args[0]) == 1) {
				if (Integer.parseInt(args[0]) == 1) {
					//Code for using cache 
				} else {
					//Code for not using Cache 
				}
			} else {
				System.err.println("Cache argument must be 0 or 1");
				printUsage();
			}
			if (args.length > 4) {
			//Add code to set cache size
			//Does not handle incorrect input for cache size
				if (args.length > 5) {
					if (Integer.parseInt(args[5]) == 1 || Integer.parseInt(args[5]) == 0) {
						if (Integer.parseInt(args[5]) == 1) {
							debug = true;
						}
					} else {
						System.err.println("Debug argument must be 0 or 1");
						printUsage();
					}
				}
			}
			//Sets the degree size for the nodes
			if (Integer.parseInt(args[1]) == 0) {
				degree = (blockSize-4)/32;
			} else {
				degree = Integer.parseInt(args[1]); // takes in degree t
			}
			try {
				file1 = new File(args[2]);
			} catch(Exception e) {
				System.err.println("File does not exist");
				printUsage();
			}
			if (Integer.parseInt(args[3]) >= 1 || Integer.parseInt(args[3]) <= 31) {
				subSize = Integer.parseInt(args[3]); // Substring length
			} else {
				System.err.println("The sequence length must be between 1 and 31 inclusively");
				printUsage();
			}
			file = new File(args[2]+".btree.data."+subSize+"."+degree); // takes in file name	
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			tree = new BTree(degree, raf);
			boolean foundStart = false;
			String subString = "";
			TreeObject obj;
			System.out.println("Constructing BTree...");
			StringBuilder sb = new StringBuilder();
			BufferedReader input = new BufferedReader(new FileReader(file1)); 
			String lineToken;
			while ((lineToken = input.readLine()) != null)  {
				Scanner lineScan = new Scanner(lineToken);
				String str2 = lineToken.replaceAll("\\s", "");
				String str = str2.replaceAll("\\d", "");
				if (str.equals("ORIGIN")) {
					foundStart = true;
				} else if (lineToken.equals("//")) {
					foundStart = false;
					sb = new StringBuilder();
				} else if (foundStart == true) {
					for (int i =0; i < str.length(); i++) {
						char token = str.charAt(i);							
						if (token == 'n' || token == 'N') {
							sb = new StringBuilder();
						} else if (token == 'a' || token == 't' || token == 'c' || token == 'g' || token == 'A' || token == 'T' || token == 'C' || token == 'G') {							
							sb.append(Character.toLowerCase(token));
						}						
						if (sb.length() > subSize) {
							String st = sb.toString();
							sb = new StringBuilder();
							sb.append(st.substring(1,subSize +1));
						}
						if (subSize == sb.length()) {
							long stream = toLong(sb.toString());						
							obj = new TreeObject(stream);
							tree.insert(obj);
							obj = new TreeObject(stream);
						}												
					}					
				}
				lineScan.close();
				}					
			tree.finish();
			System.out.println("BTree Complete");
			if (debug) {
				tree.traverseTree(args[2], degree, subSize);
			}
	} catch (FileNotFoundException f) {
		System.err.println("File does not exist");
		printUsage();
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println(
			"java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
	} 
	}//End of main method
	/**
	 * Converts a binary string to a long value 
	 * @param subString the string to be converted 
	 * @return the long value 
	 */
	private static long toLong(String subString) {
		String bineString = "";
		for (int i = 0 ; i < subSize ; i++) {
			if(subString.charAt(i) == 'a'||subString.charAt(i) == 'A') {
				bineString += "00";
				continue;
			} else if(subString.charAt(i) == 't'|| subString.charAt(i) == 'T') {
				bineString += "11";
				continue;
			} else if(subString.charAt(i) == 'c'|| subString.charAt(i) == 'C') {
				bineString += "01";
				continue;
			} else if(subString.charAt(i) == 'g'|| subString.charAt(i) == 'G') {
				bineString += "10";
				continue;
			}				
		}
		long stream = 0;
		int factor = 1;
		for (int i = bineString.length()-1; i >= 0; i--) {
			stream += ((int) bineString.charAt(i) - 48) * factor;
			factor = factor*2;
		}
		return stream;
	}//End of toLong method 
	private static void printUsage() {
		System.out.println("java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
		System.exit(1);
	}
}

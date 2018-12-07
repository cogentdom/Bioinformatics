/**
 * Stores the data for the objects in the tree
 * @author Hailee Kiesecker
 *
 */
public class TreeObject {
	public int freq;
	private long Value;
	public TreeObject(long stream) {
		this.freq = 1;
		this.Value = stream;
	}
	/**
	 * The constructor for the class
	 * @param stream the long value of the substring 
	 * @param frequency the frequency of the substring within the tree
	 */
	public TreeObject(long stream, int frequency) {
		this.freq = frequency;
		this.Value = stream;
	}
	/**
	 * @return long value for this object
	 */
	public long getValue() {
		long value = Value;
		return value;
	}
	/**
	 * @return the frequency of the substring within the tree 
	 */
	public int getFrequency() {
		return freq;
	}
	
	/**
	 * Increments the frequency for this object
	 */
	public void incrFreq() {
		this.freq++;
	}

}

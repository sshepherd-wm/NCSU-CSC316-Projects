/**
 * 
 */
package proj4;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Stephen Shepherd
 *
 */
public class HashTable {
	
	private static final double mRatio = 0.37;
	private static final double phi = (1 + Math.sqrt(5.0)) / 2;
	private static final int r = 33;
	
	private int numItems;
	private int countItems;
	private int m;
	private ArrayList<LinkedList> array;
	
	private int countLookUps;
	
	public HashTable(int numItems) {
		this.numItems = numItems;
		this.m = (int) (numItems * mRatio);
		this.array = new ArrayList<LinkedList>();
		// Fill array values with nulls
		for (int i = 0; i < m; i++) {
			array.add(i, null);
		}
		this.countLookUps = 0;
	}
	
	public int stringHash(String s) {
		
		int length = s.length();
		int count = 0;
		int sum = 0;
		while (count < length) {
			//System.out.println(s.charAt(count));
			int polyVal = (int) (((int) s.charAt(count)) * Math.pow((double) r, (double) (length - (count + 1))));
			sum += polyVal;
			count++;
		}
		
		// Golden ratio compression
		double inversePhiMult = sum * (1 / phi);
		return (int) Math.floor(m * (inversePhiMult - (Math.floor(inversePhiMult))));
	}
	
	public void insert(String element) {
		
		int hash = stringHash(element);
		if (array.get(hash) == null) {
			LinkedList newList = new LinkedList();
			newList.insert(element);
			array.set(hash, newList);
		} else {
			array.get(hash).insert(element);
		}
		countItems++;
	}
	
	public int lookUp(String element) {
		//countLookUps++;
		int hash = stringHash(element);
		if (array.get(hash) == null) {
			return 0;
		} else {
			countLookUps++;
			return array.get(hash).contains(element);
		}
		
	}
	
	public void printBucket(int key) {
		if (array.get(key) != null) {
			array.get(key).printList();
		}
	}
	
	public void printTable(int max) {
		int maxSize = Math.min(max, array.size());
		
		for (int i = 0; i < maxSize; i++) {
			System.out.print(i + ": ");
			if (array.get(i) != null) {
				array.get(i).printList();
			} else {
				System.out.println();
			}
		}
	}
	
	public int getItemCount() {
		return countItems;
	}

	public int getCountLookUps() {
		return countLookUps;
	}
	
	public void printDistributionReport() {
		// Initialize list
		ArrayList<Integer> sizeDict = new ArrayList<Integer>();
		for (int i = 0; i <= (countItems / m) * 4; i++) {
			sizeDict.add(i, 0);
		}
		// Iterate through array
		for (int i = 0; i < m; i++) {
			//System.out.println(i);
			// Get size of each bucket
			int size = 0;
			if (array.get(i) != null) {
				size = array.get(i).getSize();
			}
			if (size < sizeDict.size()) {
				int count = sizeDict.get(size) + 1;
				sizeDict.set(size, count);
				//System.out.println("Added: " + i + ", " + size);
			}
		}
		// Print the size dictionary
		System.out.println("Buckets in hash table:  " + m);
		System.out.println("Target avg bucket size: " + (countItems / m));
		System.out.println("Count of buckets by # elements in bucket:");
		for (int i = 0; i < sizeDict.size(); i++) {
			System.out.printf("%2d: ", i);
			System.out.printf("%4d", sizeDict.get(i));
			System.out.println();
		}
	}
	
	/*
	 * Checks word against hash table according to rules outlined in project doc
	 * Returns positive count of lookUps if word was found
	 * Returns negative count of lookUps if word was not found
	 */
	public int checkWord(String word) {
		
		int total = 0;
		
		// Count tracks most recent lookup result
		int count = lookUp(word);
		// Total tracks total # of comparisons in table so far
		total += Math.abs(count);
		if (count > 0) { return total; }
		
		// If here, was not found
		int length = word.length();
		
		// Handling capital first character
		count = checkCapital(word);
		total += Math.abs(count);
		if (count > 0) { return total; }
		
		// Handing "'s" at end
		count = checkApostrophe(word, length);
		total += Math.abs(count);
		if (count > 0) { return total; }
		
		// Handling "s" at the end
		count = checkS(word, length);
		total += Math.abs(count);
		if (count > 0) { return total; }
		
		// Handling "es" at the end
		count = checkEs(word, length);
		total += Math.abs(count);
		if (count > 0) { return total; }
		
		// Handling "ed" at the end
		count = checkEd(word, length);
		total += Math.abs(count);
		if (count > 0) { return total; }
		
		// Handling "d" at the end
		count = checkD(word, length);
		total += Math.abs(count);
		if (count > 0) { return total; }
		
		// Handling "er" at the end
		count = checkEr(word, length);
		total += Math.abs(count);
		if (count > 0) { return total; }
		
		// Handling "r" at the end
		count = checkR(word, length);
		total += Math.abs(count);
		if (count > 0) { return total; }
		
		// Handling "ing" at the end 1
		count = checkIng1(word, length);
		total += Math.abs(count);
		if (count > 0) { return total; }
		
		// Handling "ing" at the end 2
		count = checkIng2(word, length);
		total += Math.abs(count);
		if (count > 0) { return total; }
		
		// Handling "ly" at the end
		count = checkLy(word, length);
		total += Math.abs(count);
		if (count > 0) { return total; }
		
		// If here, word not found
		return -1 * total;
	}
	
	public int checkCurrent(String word) {
		return lookUp(word);
	}
	
	public int checkCapital(String word) {
		int count = 0;
		// Handling capital first character
		if (Character.isUpperCase(word.charAt(0))) {
			String first = Character.toString(Character.toLowerCase(word.charAt(0)));
			String rest = word.substring(1);
			String newWord = first + rest;
			count = lookUp(newWord);
		}
		return count;
	}
	
	public int checkApostrophe(String word, int length) {
		int count = 0;
		if (length <= 2) { return count;}
		// Handing "'s" at end
		if (word.substring(length - 2).equals("'s")) {
			String newWord = word.substring(0, length - 2);
			count = lookUp(newWord);
		}
		return count;
	}
	
	public int checkS(String word, int length) {
		int count = 0;
		if (length <= 1) { return count;}
		// Handling "s" at the end
		if (word.substring(length - 1).equals("s")) {
			String newWord = word.substring(0, length - 1);
			count = lookUp(newWord);
		}
		return count;
	}
	
	public int checkEs(String word, int length) {
		int count = 0;
		if (length <= 2) { return count;}
		// Handling "es" at the end
		if (word.substring(length - 2).equals("es")) {
			String newWord = word.substring(0, length - 2);
			count = lookUp(newWord);
		}
		return count;
	}
	
	public int checkEd(String word, int length) {
		int count = 0;
		if (length <= 2) { return count;}
		// Handling "ed" at the end
		if (word.substring(length - 2).equals("ed")) {
			String newWord = word.substring(0, length - 2);
			count = lookUp(newWord);
		}
		return count;
	}
	
	public int checkD(String word, int length) {
		int count = 0;
		if (length <= 1) { return count;}
		// Handling "d" at the end
		if (word.substring(length - 1).equals("d")) {
			String newWord = word.substring(0, length - 1);
			count = lookUp(newWord);
		}
		return count;
	}
	
	public int checkEr(String word, int length) {
		int count = 0;
		if (length <= 2) { return count;}
		// Handling "er" at the end
		if (word.substring(length - 2).equals("er")) {
			String newWord = word.substring(0, length - 2);
			count = lookUp(newWord);
		}
		return count;
	}
	
	public int checkR(String word, int length) {
		int count = 0;
		if (length <= 1) { return count;}
		// Handling "r" at the end
		if (word.substring(length - 1).equals("r")) {
			String newWord = word.substring(0, length - 1);
			count = lookUp(newWord);
		}
		return count;
	}
	
	public int checkIng1(String word, int length) {
		int count = 0;
		if (length <= 3) { return count;}
		// Handling "ing" at the end
		if (word.substring(length - 3).equals("ing")) {
			String newWord = word.substring(0, length - 3);
			count = lookUp(newWord);
		}
		return count;
	}
	
	public int checkIng2(String word, int length) {
		int count = 0;
		if (length <= 3) { return count;}
		// Handling "ing" at the end
		if (word.substring(length - 3).equals("ing")) {
			String newWord = word.substring(0, length - 3) + "e";
			count = lookUp(newWord);
		}
		return count;
	}
	
	public int checkLy(String word, int length) {
		int count = 0;
		if (length <= 2) { return count;}
		// Handling "ing" at the end
		if (word.substring(length - 2).equals("ly")) {
			String newWord = word.substring(0, length - 2);
			count = lookUp(newWord);
		}
		return count;
	}

}

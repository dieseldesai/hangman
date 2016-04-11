package hangman;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

	static CharProb[] cp = new CharProb[26];
	static List<WordFreq> wflist = new ArrayList<WordFreq>();
	static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static String wordInProgress;
	static String incorrect;
	
	public static void main(String[] args) {
		String line;
		int total = 0;
		String[] words;
		Scanner scanner;
		//read input file
		try {
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			line = br.readLine();
			while(line!=null){
				words = line.split(" ");
				wflist.add(new WordFreq(words[0], Integer.parseInt(words[1])));
				//keep running count of total words 
				total = total + Integer.parseInt(words[1]);
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		//set prior probability of each word
		for (WordFreq wf : wflist){
			wf.setProb((double) wf.getFreq()/total);
		}
		Collections.sort(wflist);
		
		//print 8 most common words
		top(8);
		
		//print 8 least common words
		top(-8);
		
		//setup is done, prior word probabilities are set. now to play the game
		//input known letters
		scanner = new Scanner(System.in);
		System.out.println("Enter word in progress with dashes for unknown letters");
		wordInProgress = scanner.next().toUpperCase();
		
		//input incorrect guesses
		System.out.println("Enter incorrect guesses as String or \"-\" for none");
		incorrect = scanner.next().toUpperCase();
		if (incorrect.equals("-")){
			incorrect = "";
		}
		scanner.close();
		
		//calculate all letter probabilities and output next best guess
		for(int i = 0; i < 26; i++){
			cp[i] = new CharProb(alphabet.charAt(i), prob(alphabet.charAt(i), wordInProgress, incorrect));
		}
		Arrays.sort(cp);
		Collections.sort(wflist);
		
		//print probabilities for each letter
		for(int i = 0; i < 26; i++){
			System.out.println(cp[i]);			
		}
	}
	public static void top(int num){
		//print top/bottom. If num is negative, print least frequent words. If num is positive, print most frequent
		if (num > 0){
			for(int i = 0; i < num; i++){
				System.out.println(wflist.get(i));
			}
		}
		else if (num < 0){
			for(int i = wflist.size()-1; i >= wflist.size()+num; i--){
				System.out.println(wflist.get(i));
			}
		}
	}	
	public static double prob(WordFreq word, String wip, String inc){
		//returns probability of the word given the evidence wip (word in progress) and incorrect character array
		int ev = matchEvidence(word.getWord(), wip, inc);
		double p = 0;
		if (ev == 0){
			return 0;
		}
		for(WordFreq wf : wflist){
			p = p + matchEvidence(wf.getWord(), wip, inc)*wf.getProb();
		}
		return word.getProb()/p;
	}
	public static double prob(char c, String wip, String inc){
		//returns probability of the character given the evidence wip (word in progress) and incorrect character array
		double p = 0;
		if(inc.indexOf(c)!=-1 || wip.indexOf(c)!=-1){
			return 0;
		}
		for(WordFreq wf : wflist){
			if (wf.getWord().indexOf(c) >= 0){
				p = p + prob(wf, wip, inc);
			}
		}
		return p;
	}
	public static int matchEvidence(String word, String wip, String inc){
		//checks whether the given evidence matches the word. Returns 1 if yes, 0 if no.
		for(int i = 0; i < word.length(); i++){
			//check any known letter matches
			if(word.charAt(i) != wip.charAt(i) && wip.charAt(i) != '-'){
				return 0;
			}
			//check any already guessed letter does not exist in the unknown spots
			if(wip.charAt(i) != '-'){
				for(int j = 0; j < word.length(); j++){
					if(wip.charAt(i) == word.charAt(j) && wip.charAt(j) != word.charAt(j)){
						return 0;
					}
				}
			}
		}
		//check any incorrect guess does not exist in the word
		for(int i = 0; i < inc.length(); i++){
			if(word.indexOf(inc.charAt(i)) != -1){
				return 0;
			}
		}
		return 1;
	}
}

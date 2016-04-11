package hangman;

public class CharProb implements Comparable<CharProb>{
	char c;
	double prob;
	public CharProb(char c, double p){
		this.c = c;
		this.prob = p;
	}
	public char getChar(){
		return c;
	}
	public double getProb(){
		return prob;
	}
	public int compareTo(CharProb c){
		if (this.prob < c.getProb()){
			return 1;
		}
		else if (this.prob > c.getProb()){
			return -1;
		}
		return 0;
	}
	public String toString(){
		return c + " " + prob;
	}
}

package hangman;

public class WordFreq implements Comparable<WordFreq>{
	String word;
	int freq;
	double prob;
	public WordFreq(String w, int f){
		this.word=w;
		this.freq=f;
		prob = 0;
	}
	public String getWord(){
		return word;
	}
	public int getFreq(){
		return freq;
	}
	public double getProb(){
		return prob;
	}
	public void setProb(double p){
		this.prob = p;
	}
	public int compareTo(WordFreq w){
		if (this.prob < w.getProb()){
			return 1;
		}
		else if (this.prob > w.getProb()){
			return -1;
		}
		return 0;
	}
	public String toString(){
		return word + " " + freq + " " + prob;
	}
}

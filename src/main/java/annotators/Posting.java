package annotators;

import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

import typesystems.Document;

/**
 * Use to sort final results
 * @author amaliujia
 *
 */
public class Posting implements Comparable<Posting>{
  public int id;
  public double score;
  public boolean isQuery;
  public int relevance;
  public String text;
  public HashMap<String, Integer> tokenList;
 
  public Posting(int ID, boolean isQuery, int relevance, String text){
    this.id = ID;
    //this.score = aScore;
    this.isQuery = isQuery;
    this.relevance = relevance;
    this.text = text;
    tokenList = new HashMap<String, Integer>();
  }
  
  public int compareTo(Posting o) {
    if(this.score == o.score) return 0;
    else if(this.score < o.score) return 1;
    else return -1;
  } 
}

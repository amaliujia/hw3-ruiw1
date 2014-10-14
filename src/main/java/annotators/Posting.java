package annotators;

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
  public Document doc;
 
  public Posting(int ID, double aScore, Document aDoc){
    this.id = ID;
    this.score = aScore;
    this.doc = aDoc;
  }
  
  public int compareTo(Posting o) {
    if(this.score == o.score) return 0;
    else if(this.score < o.score) return 1;
    else return -1;
  } 
}

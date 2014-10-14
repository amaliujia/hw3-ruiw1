package annotators;

import java.util.concurrent.ConcurrentMap;

import typesystems.Document;

/**
 * Use to sort final results
 * @author amaliujia
 *
 */
public class Posting implements Comparable<Posting>{
  private int id;
  private double score;
  private Document doc;
 
  public int compareTo(Posting o) {
    if(this.score == o.score) return 0;
    else if(this.score < o.score) return -1;
    else return 1;
  } 
}

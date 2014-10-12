

/* First created by JCasGen Sun Oct 12 16:07:38 EDT 2014 */
package typesystems;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sun Oct 12 16:07:38 EDT 2014
 * XML source: /Users/amaliujia/Documents/workspace/hw3-ruiw1/src/main/resources/descriptors/typesystems/VectorSpaceTypes.xml
 * @generated */
public class Query extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Query.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Query() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Query(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Query(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Query(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: queryID

  /** getter for queryID - gets 
   * @generated
   * @return value of the feature 
   */
  public String getQueryID() {
    if (Query_Type.featOkTst && ((Query_Type)jcasType).casFeat_queryID == null)
      jcasType.jcas.throwFeatMissing("queryID", "typesystems.Query");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Query_Type)jcasType).casFeatCode_queryID);}
    
  /** setter for queryID - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setQueryID(String v) {
    if (Query_Type.featOkTst && ((Query_Type)jcasType).casFeat_queryID == null)
      jcasType.jcas.throwFeatMissing("queryID", "typesystems.Query");
    jcasType.ll_cas.ll_setStringValue(addr, ((Query_Type)jcasType).casFeatCode_queryID, v);}    
   
    
  //*--------------*
  //* Feature: text

  /** getter for text - gets 
   * @generated
   * @return value of the feature 
   */
  public String getText() {
    if (Query_Type.featOkTst && ((Query_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "typesystems.Query");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Query_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setText(String v) {
    if (Query_Type.featOkTst && ((Query_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "typesystems.Query");
    jcasType.ll_cas.ll_setStringValue(addr, ((Query_Type)jcasType).casFeatCode_text, v);}    
  }

    
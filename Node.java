package Heaps;

public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {
	private T elem;
	private Node<T> fatherNode;
	private Node<T> rightSiblingNode;
	private Node<T> leftSiblingNode;
	private Node<T> childNode;
	private Boolean marked;
	private Integer degree;
	
	public static int SMALLER = -1;
	public static int HIGHER = 1;
	public static int EQUALS = 0;
	
	public Node(T newElem){
		elem=newElem;
		degree=0;
		marked=false;
	}
	
	public T getElem() {
		return elem;
	}
	
	public Node<T> getLeftSiblingNode() {
		return leftSiblingNode;
	}
	
	public Node<T> getRightSiblingNode() {
		return rightSiblingNode;
	}
	
	public Node<T> getChild(){
		return childNode;
	}
	
	public Node<T> getFather(){
		return fatherNode;
	}
	
 	public Integer getDegree() {
		return degree;
	}
	
 	public Boolean isMarked() {
 		return marked;
 	}
 	
 	@Override
	public int compareTo(Node<T> otherNode) {
		return elem.compareTo(otherNode.getElem());
	}
 	
 	protected void setFather(Node<T> newFatherNode) {
 		fatherNode=newFatherNode;
 	}
 	
	protected void setChild(Node<T> newChildNode) {
		childNode = newChildNode;
	}
	
	protected void setDegree(Integer newDegree) {
		degree = newDegree;
	}
	
	protected void setMarked(Boolean newValue) {
		marked = newValue;
	}

	protected void setElem(T newElem) {
		elem=newElem;
	}
	
	protected void setLeftSiblingNode(Node<T> newLeftSiblingNode) {
		leftSiblingNode=newLeftSiblingNode;
	}
	
	protected void setRightSiblingNode(Node<T> newRightSiblingNode) {
		rightSiblingNode=newRightSiblingNode;
	}
	
	protected void cleanFather() {
		fatherNode=null;
	}
}

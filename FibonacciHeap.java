package Heaps;

import java.util.ArrayList;

public class FibonacciHeap<T extends Comparable<T>> {
	private Node<T> min;
	private Integer numElems;

	public FibonacciHeap() {
		numElems=0;
	}

	public T getMin() {
		return min.getElem();
	}

	public void insert(T newElemToInsert) {
		Node<T> newNodeToInsert = new Node<T>(newElemToInsert);
		insertNode(newNodeToInsert);
	}

	public void union(FibonacciHeap<T> otherHeapToJoin) {
		Node<T> auxNodeToChange = otherHeapToJoin.getMinNode().getLeftSiblingNode();
		otherHeapToJoin.getMinNode().setLeftSiblingNode(min.getLeftSiblingNode());
		min.getLeftSiblingNode().setRightSiblingNode(otherHeapToJoin.getMinNode());
		min.setLeftSiblingNode(auxNodeToChange);
		auxNodeToChange.setRightSiblingNode(min);
		if(min==null || (otherHeapToJoin.getMinNode()!=null && otherHeapToJoin.getMin().compareTo(getMin())==Node.SMALLER)){
			min=otherHeapToJoin.getMinNode();
		}
		numElems += otherHeapToJoin.getNumElems();
	}

	public Node<T> extractMin() {
		Node<T> nodeToExtract = min;
		if(nodeToExtract != null) {
			numElems--;
			Node<T> firstChild = min.getChild();
			Node<T> nextNode = firstChild;
			while(nextNode!=null) {
				nextNode.cleanFather();
				Node<T> auxNode = next(nextNode, firstChild);
				insertNode(nextNode);
				nextNode =auxNode;
						
			}
			if(nodeToExtract == nodeToExtract.getRightSiblingNode()) {
				removeNodeFromRootList(nodeToExtract);
				min=null;
			}
			else {
				min = nodeToExtract.getRightSiblingNode();
				removeNodeFromRootList(nodeToExtract);
				consolidate();
			}
		}
		return nodeToExtract;
	}

	public Integer getNumElems() {
		return numElems;
	}

	public void decreaseKey(Node<T> nodeToChange,T newElem) throws ElemError {
		if(newElem.compareTo(nodeToChange.getElem())==Node.HIGHER) {
			throw new ElemError("El elemento introducido es mayor o igual que el elemento existente y por lo tanto no puede decrementarse");
		}
		nodeToChange.setElem(newElem);
		Node<T> fatherOfNode = nodeToChange.getFather();
		if(fatherOfNode!=null && nodeToChange.getElem().compareTo(fatherOfNode.getElem())==Node.SMALLER) {
			cut(nodeToChange,fatherOfNode);
			cascadingCut(fatherOfNode);
		}
		if(nodeToChange.getElem().compareTo(getMin())==Node.SMALLER) {
			min=nodeToChange;
		}
	}

	public void delete(Node<T> nodeToDelete) {
		try {
			decreaseKey(nodeToDelete,getMin());
			extractMin();
		} catch (ElemError e) {
			e.printStackTrace();
		}
	}

	public void showHeap() {
		if(min!=null) {
			System.out.print(" -> " + getMinNode().getElem());
			if(getMinNode().getRightSiblingNode()!=getMinNode()) {
				showHeap(getMinNode().getRightSiblingNode(),getMinNode());
			}
			if(getMinNode().getChild()!=null) {
				System.out.println("\nHijos del nodo con elemento " +getMinNode().getElem());
				System.out.print(" -> " + getMinNode().getChild().getElem());
				showHeap(getMinNode().getChild().getRightSiblingNode(),getMinNode().getChild());
			}
		}
	}

	public void insertNode(Node<T> newNodeToInsert) {
		if(min==null) {
			min=newNodeToInsert;
			min.setLeftSiblingNode(min);
			min.setRightSiblingNode(min);
		}
		else {
			newNodeToInsert.setRightSiblingNode(min);
			newNodeToInsert.setLeftSiblingNode(min.getLeftSiblingNode());
			min.getLeftSiblingNode().setRightSiblingNode(newNodeToInsert);
			min.setLeftSiblingNode(newNodeToInsert);			
			if(newNodeToInsert.getElem().compareTo(min.getElem())==Node.SMALLER) {
				min = newNodeToInsert;
			}
		}
		numElems++;
	}
	
	private void showHeap(Node<T> node,Node<T> endNode) {
		if(node!=endNode) {
			System.out.print(" -> " + node.getElem());
			if(node.getRightSiblingNode()!=node) {
				showHeap(node.getRightSiblingNode(),endNode);
			}
			if(node.getChild()!=null) {
				System.out.println("Hijos del nodo con elemento " +node.getElem());
				System.out.print(" -> " + node.getChild().getElem());
				showHeap(node.getChild().getRightSiblingNode(),node.getChild());
			}
		}
		else System.out.println();

	}

	private Node<T> getMinNode(){
		return min;
	}

	private void consolidate() {
		int size = (int) (Math.log10(numElems)/Math.log10(2)) +1;
		ArrayList<Node<T>> listOfHeaps = new ArrayList<Node<T>>();
		for(int i=0;i<size;++i) {
			listOfHeaps.add(null);
		}
		Node<T> firstChild = min;
		Node<T> nextNode = firstChild;
		while(nextNode!=null) {
			Integer degreeOfNode = nextNode.getDegree();
			Node<T> auxNextNode = next(nextNode, nextNode);
			removeNodeFromRootList(nextNode);
			while(listOfHeaps.get(degreeOfNode)!=null) {
				Node<T> auxNode = listOfHeaps.get(degreeOfNode);
				if(nextNode.getElem().compareTo(auxNode.getElem())==Node.HIGHER) {
					Node<T> nodeToExchange = auxNode;
					auxNode=nextNode;
					nextNode = nodeToExchange;
				}
				heapLink(auxNode,nextNode);
				listOfHeaps.set(degreeOfNode,null);
				degreeOfNode++;
			}
			listOfHeaps.set(degreeOfNode,nextNode);
			nextNode = auxNextNode;
		}
		min=null;
		for(int i=0; i<listOfHeaps.size();++i) {
			if(listOfHeaps.get(i)!=null) {
				insertNode(listOfHeaps.get(i));
				if(min==null || (listOfHeaps.get(i).getElem().compareTo(getMin())==Node.SMALLER)) {
					min=listOfHeaps.get(i);
				}
			}
		}
	}

	private void heapLink(Node<T> childNode, Node<T> fatherNode) {
		removeNodeFromRootList(childNode);
		if(fatherNode.getChild()!=null) {
			childNode.setLeftSiblingNode(fatherNode.getChild().getLeftSiblingNode());
			childNode.setRightSiblingNode(fatherNode.getChild());
			childNode.getLeftSiblingNode().setRightSiblingNode(childNode);
			childNode.getRightSiblingNode().setLeftSiblingNode(childNode);
		}
		else {
			fatherNode.setChild(childNode);
			childNode.setLeftSiblingNode(childNode);
			childNode.setRightSiblingNode(childNode);
		}
		fatherNode.setDegree(fatherNode.getDegree()+1);
		childNode.setMarked(false);
		childNode.setFather(fatherNode);
	}

	private void removeNodeFromRootList(Node<T> nodeToRemove) {
		nodeToRemove.getLeftSiblingNode().setRightSiblingNode(nodeToRemove.getRightSiblingNode());
		nodeToRemove.getRightSiblingNode().setLeftSiblingNode(nodeToRemove.getLeftSiblingNode());
		nodeToRemove.setLeftSiblingNode(nodeToRemove);
		nodeToRemove.setRightSiblingNode(nodeToRemove);
	}

	private Node<T> next(Node<T> actualNode, Node<T> firstChild){
		if(actualNode.getRightSiblingNode()!=firstChild) {
			return actualNode.getRightSiblingNode();
		}
		else return null;
	}

	private void cut(Node<T> nodeChanged, Node<T> fatherOfNodeChanged) {
		if(nodeChanged.getRightSiblingNode()!=nodeChanged) {
			nodeChanged.getRightSiblingNode().setLeftSiblingNode(nodeChanged.getLeftSiblingNode());
			nodeChanged.getLeftSiblingNode().setRightSiblingNode(nodeChanged.getRightSiblingNode());
			if(fatherOfNodeChanged.getChild()==nodeChanged) {
				fatherOfNodeChanged.setChild(nodeChanged.getRightSiblingNode());
			}
		}
		else {
			fatherOfNodeChanged.setChild(null);
		}
		fatherOfNodeChanged.setDegree(fatherOfNodeChanged.getDegree()-1);
		nodeChanged.cleanFather();
		insertNode(nodeChanged);
		nodeChanged.setMarked(false);
	}

	private void cascadingCut(Node<T> fatherOfNodeChanged) {
		Node<T> fatherOfFather = fatherOfNodeChanged.getFather();
		if(fatherOfFather!=null) {
			if(!fatherOfNodeChanged.isMarked()) {
				fatherOfNodeChanged.setMarked(true);
			}
			else {
				cut(fatherOfNodeChanged, fatherOfFather);
				cascadingCut(fatherOfFather);
			}
		}
	}
}

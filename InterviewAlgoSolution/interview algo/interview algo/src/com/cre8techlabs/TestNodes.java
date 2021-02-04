package com.cre8techlabs;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class TestNodes {
	/*
AAA   BBB
     CC DDD
  EEEE FF GGG
  EEEE    GGG
 HHHHHHHHHH
	 */
	public static void main(String[] args) {
		Node a = createNode(0, 0, 3, 1);
		Node b = createNode(6, 0, 3, 1);
		Node c = createNode(5, 1, 2, 1);
		Node d = createNode(8, 1, 3, 1);
		Node e = createNode(2, 2, 4, 2);
		Node f = createNode(7, 2, 2, 1);
		Node g = createNode(10, 2, 3, 2);
		Node h = createNode(1, 4, 10, 1);
		
		
		linkNodes(a, b, c, d, e, f, g, h);

		assertNodeLinked(a, new Node[] {}, new Node[] {e}, new Node[] {}, new Node[] {b});
		assertNodeLinked(b, new Node[] {}, new Node[] {c, d}, new Node[] {a}, new Node[] {});
		assertNodeLinked(c, new Node[] {b}, new Node[] {}, new Node[] {}, new Node[] {d});
		assertNodeLinked(d, new Node[] {b}, new Node[] {f, g}, new Node[] {}, new Node[] {});
		assertNodeLinked(e, new Node[] {c}, new Node[] {h}, new Node[] {}, new Node[] {f});
		assertNodeLinked(f, new Node[] {d}, new Node[] {h}, new Node[] {e}, new Node[] {g});
		assertNodeLinked(g, new Node[] {d}, new Node[] {}, new Node[] {f}, new Node[] {});
		assertNodeLinked(h, new Node[] {e, g}, new Node[] {}, new Node[] {}, new Node[] {});
		
		displayRecursivelyAllNodesFromParentNode(a);
		
	}
	
	private static  List<Node> getModList(Node[] nodes){
		List modList = new ArrayList();
		for(Node loopnodes : nodes) {
			modList.add(loopnodes);
			
		}
		return modList;
		
	}
	private static void linkNodes(Node...nodes) {
		// TODO to complete
		Node[] allNodes = nodes;
		double max= 0;
		for(Node node:allNodes) {
			if(node.getRect().x+ node.getRect().getWidth()> max) {
				max= node.getRect().x+node.getRect().getWidth();
			}
		}
		System.out.println("max: "+ max);
		
	
	//Vertical Line Sweep
	List<Node> activeListofNodes = new ArrayList<Node>();
	for(int i=0;i<=max;i++) {
		//for removing 
		List<Node> nodesRemoved= removeFromActiveNodesList(activeListofNodes, i);
		//for updating
		if(!nodesRemoved.isEmpty()) {
			updateRemovedNodes(nodesRemoved, activeListofNodes);
			nodesRemoved= null;
			
			}
		//check nodes for adding
		List<Node> nodeList= findNodesStartingOnSameVerticalLine(i, allNodes);
		if(!nodeList.isEmpty()) {
			//Line Sweep
			addToActiveNode(activeListofNodes, nodeList);
		}
		}
	//Find Left and Right
	RectangleComparatorYAxis comparatorYAxis = new RectangleComparatorYAxis();
	RectangleComparatorXAxis comparatorXAxis= new RectangleComparatorXAxis();
	List<Node> list= getModList(allNodes);
	//Sorting on Y axis
	Collections.sort(list, comparatorYAxis);
	Iterator<Node> iterator= list.iterator();
	List<Node> sameYaxisList= new ArrayList<Node>();
	int lastYval= 0;
	while(iterator.hasNext()) {
		Node node1=iterator.next();
		if(sameYaxisList.isEmpty()) {
				sameYaxisList.add(node1);
		lastYval= node1.getRect().y;
	}
	else {
		if(node1.getRect().y!=lastYval) {
			//sameYaxisList
			Collections.sort(sameYaxisList, comparatorXAxis);
			processSameYAxisList(sameYaxisList);
			//empty
			sameYaxisList.clear();
			// add new node
			sameYaxisList.add(node1);
			//update
			lastYval=node1.getRect().y;
		}
		else {
			sameYaxisList.add(node1);
		}
		}// while Ends
		Collections.sort(sameYaxisList, comparatorXAxis);
		processSameYAxisList(sameYaxisList);}
		
	}
	public static void processSameYAxisList(List<Node> sameYaxisList) {
		Node node= null;
		for(Node nodeNext: sameYaxisList) {
			while(node !=null) {
				node.setRightNodes(new HashSet<INode>());
				
				node.getRightNodes().add(nodeNext);
				
				nodeNext.setLeftNodes(new HashSet<INode>());
				nodeNext.getLeftNodes().add(node);
				
			}
			node= nodeNext;
		}
	}
	/*
	AAA   BBB
	     CC DDD
	  EEEE FF GGG
	  EEEE    GGG
	 HHHHHHHHHH
		 */
	private static void updateRemovedNodes(List<Node> nodesRemoved, List<Node> activeListofNodes) 
	{
		for(Node nodeRemoved :nodesRemoved) {
			int minimumHeightDown =0;
			int minimumHeightUp=0;
			for(Node activeNode : activeListofNodes) {
			
			//down node
				if(activeNode.getRect().y > nodeRemoved.getRect().y) {
			
				minimumHeightDown = activeNode.getRect().y - nodeRemoved.getRect().y;
				//checking if Down Node is null
				if(nodeRemoved.getDownNodes()==null) {
					//nodeRemoved.getDownNodes(new HashSet<INode>());
					nodeRemoved.setDownNodes(new HashSet<INode>());
					nodeRemoved.getDownNodes().add(activeNode);
					//updating Up Node
					updateActiveUpNodes(nodeRemoved, minimumHeightDown, activeNode);
					
				}
				else {
					//getting the first element 
					Node firstNode = (Node) nodeRemoved.getDownNodes().iterator().next();
					if(((firstNode.getRect().y +firstNode.getRect().height-1)-(nodeRemoved.getRect().y+ nodeRemoved.getRect().height-1))>minimumHeightDown) {
						nodeRemoved.getDownNodes().clear();
						nodeRemoved.getDownNodes().add(activeNode);
						
						updateActiveUpNodes(nodeRemoved, minimumHeightDown, activeNode);
					}
					else if(minimumHeightDown== firstNode.getRect().y - nodeRemoved.getRect().y) {
						nodeRemoved.getDownNodes().add(activeNode);
						updateActiveUpNodes(nodeRemoved, minimumHeightDown, activeNode);
					}
				}
				
				}
				//Condition for UP Node
				else {
					minimumHeightUp= nodeRemoved.getRect().y- activeNode.getRect().y;
					//check if empty
					if(nodeRemoved.getUpNodes()== null) {
						nodeRemoved.setUpNodes(new HashSet<INode>());
						
						nodeRemoved.getUpNodes().add(activeNode);
						
						//update active node Down Node
						updateActiveDownNodes(nodeRemoved, minimumHeightDown, activeNode);
						}
					else{
						//get first element because all elements will have same distance from active node
						Node firstNode = (Node) nodeRemoved.getUpNodes().iterator().next();
						if(((nodeRemoved.getRect().y + nodeRemoved.getRect().height - 1) - (firstNode.getRect().y + firstNode.getRect().height - 1)) > minimumHeightUp){
							nodeRemoved.getUpNodes().clear();
							nodeRemoved.getUpNodes().add(activeNode);
							//minHeightUp = removedNode.getRect().y - activeNode.getRect().y;
							//update Active Node's Down Node
							updateActiveDownNodes(nodeRemoved, minimumHeightUp, activeNode);
						}
					else if(minimumHeightUp == nodeRemoved.getRect().y- firstNode.getRect().y) {
						
					nodeRemoved.getUpNodes().add(activeNode);
					//update active node Down Node
					updateActiveDownNodes(nodeRemoved, minimumHeightDown, activeNode);
					}
					}
				}
			}
		}
	}
	
			
			
		
	
	
	private static void updateActiveDownNodes(Node nodeRemoved, int minimumHeightUp, Node activeNode) {
		
	// if empty
		if(activeNode.getDownNodes()==null) {
			activeNode.setDownNodes(new HashSet<INode>());
			
			activeNode.getDownNodes().add(nodeRemoved);
		}
			
		//Down Nodes
		else {
		Node firstNode =(Node) activeNode.getDownNodes().iterator().next();
		if(((firstNode.getRect().y +firstNode.getRect().height-1)-(activeNode.getRect().y+ activeNode.getRect().height-1))>minimumHeightUp) {//all its Down Nodes have more distance than removed node
			activeNode.getDownNodes().clear();
			activeNode.getDownNodes().add(nodeRemoved);
		}
			
		else if(minimumHeightUp ==((firstNode.getRect().y+ firstNode.getRect().height-1)-(activeNode.getRect().height-1))){
			activeNode.getDownNodes().add(nodeRemoved);
		}
		}
	}
	
	private static void updateActiveUpNodes(Node nodeRemoved, int minimumHeightDown, Node activeNode) {
		if(activeNode.getUpNodes()== null) {
			activeNode.setUpNodes(new HashSet<INode>());
			
			activeNode.getUpNodes().add(nodeRemoved);
			
		}
		//active node up nodes 
		else {
			// Getting first element as all the elements has same distance from active node.
			Node firstNode= (Node) activeNode.getUpNodes().iterator().next();
			if(((activeNode.getRect().y + activeNode.getRect().height-1)-(firstNode.getRect().y+firstNode.getRect().height-1))>minimumHeightDown){
				activeNode.getUpNodes().clear();
				activeNode.getUpNodes().add(nodeRemoved);
				
			}
			else if(minimumHeightDown==((activeNode.getRect().y+ activeNode.getRect().height-1)-(firstNode.getRect().y+ firstNode.getRect().height-1))) {
				activeNode.getUpNodes().add(nodeRemoved);
			}
		}
	}
	
	private static List<Node> removeFromActiveNodesList(List<Node> activeListofNodes, int i){
		List<Node> nodesRemoved = new ArrayList<Node>();
		Iterator<Node> iterator = activeListofNodes.iterator();
		while(iterator.hasNext()) {
			Node node= iterator.next();
			if((node.getRect().x+ node.getRect().getWidth())-i==0){
				nodesRemoved.add(node);
				iterator.remove();
			}
		}
		return nodesRemoved;
	}
			
			private static void addToActiveNode(List<Node> activeListofNodes, List<Node> nodesList) {
				for(Node nodes: nodesList) {
					activeListofNodes.add(nodes);
				}
			}
		
	private static List<Node> findNodesStartingOnSameVerticalLine(int i, Node[] allNodes){
		List<Node> nodeList= new ArrayList<Node>();
		for(Node node: allNodes) {
			if(node.getRect().x==i) {
				nodeList.add(node);
			
		}
			
		}
		return nodeList;
	}
	
	private static void displayRecursivelyAllNodesFromParentNode(Node a) {
		// TODO to complete
		
	}
	private static void assertNodeLinked(Node node, Node[] up, Node[] down, Node[] left, Node[] right) {
		if (!containsAll(node.getUpNodes(), Arrays.asList(up))) {
			throw new AssertionError("The conditions are not meet");
		}
		if (!containsAll(node.getDownNodes(), Arrays.asList(down))) {
			throw new AssertionError("The conditions are not meet");
		}
		if (!containsAll(node.getLeftNodes(), Arrays.asList(left))) {
			throw new AssertionError("The conditions are not meet");
		}
		if (!containsAll(node.getRightNodes(), Arrays.asList(right))) {
			throw new AssertionError("The conditions are not meet");
		}
	}

	private static boolean containsAll(Collection<INode> nodes, Collection<INode> nodes2) {
		if (nodes.size() != nodes2.size())
			return false;
		if (nodes.isEmpty() && nodes2.isEmpty())
			return true;
		if (nodes.containsAll(nodes2) && nodes2.containsAll(nodes)) {
			return true;
		}
		return false;
	}

	private static Node createNode(int x, int y, int width, int height) {
		Node result = new Node();
		result.setRect(new Rectangle(x, y, width, height));
		
		return result;
	}
	
     private static class RectangleComparatorYAxis implements Comparator
    {
    public int compare(Object firstObject, Object secondObject) {
	Node node1= (Node) firstObject;
	Rectangle rect1= node1.getRect();
	Node node2= (Node) secondObject;
	Rectangle rect2=node2.getRect();
	if(rect1.y< rect2.y) {
		return -1;
	}
	if(rect1.y>  rect2.y) {
		return 1;
	}
	else {
		return 1;
	}
	
}
}
	
	private static class RectangleComparatorXAxis implements Comparator{
		public int compare(Object firstObject, Object secondObject) {
			Node node1= (Node) firstObject;
			Rectangle rect1= node1.getRect();
			Node node2= (Node) secondObject;
			Rectangle rect2=node2.getRect();
			
			if(rect1.y< rect2.y) {
				return -1;
			}
			if(rect1.y>  rect2.y) {
				return 1;
			}
			else {
				return 1;
			}
			
		}
	}
}
	

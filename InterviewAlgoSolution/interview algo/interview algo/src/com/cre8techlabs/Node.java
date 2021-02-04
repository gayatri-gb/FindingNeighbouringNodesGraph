package com.cre8techlabs;

import java.awt.Rectangle;

import java.util.Set;

/**
 * Completed this class
 * @author lenderprice
 *
 */
public class Node implements INode {
	private Rectangle rec;
	private Set<INode> upNode;
	private Set<INode> downNode;
	private Set<INode> leftNode;
	private Set<INode> rightNode;

	@Override
	public Rectangle getRect() {
		// TODO Auto-generated method stub
		return rec;
	}

	@Override
	public Set<INode> getUpNodes() {
		// TODO Auto-generated method stub
		return upNode;
	}
	
public void setUpNodes(Set<INode> upNode) {
		this.upNode= upNode;
	}

	@Override
	public Set<INode> getDownNodes() {
		// TODO Auto-generated method stub
		return downNode;
	}

	//setDownNode
	public void setDownNodes(Set<INode> downNode) {
		this.downNode=downNode;
	}
	
	@Override
	public Set<INode> getLeftNodes() {
		// TODO Auto-generated method stub
		return leftNode;
	}
	
	//set Left Node
	public void setLeftNodes(Set<INode> leftNode) {
		this.leftNode=leftNode;
	}

	@Override
	public Set<INode> getRightNodes() {
		// TODO Auto-generated method stub
		return rightNode;
	}

	public void setRightNodes(Set<INode> rightNode) {
		this.rightNode=rightNode;
	}
	
	
	@Override
	public void setRect(Rectangle rec) {
		
		this.rec= rec;
		
	}


}

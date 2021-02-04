package com.cre8techlabs;

import java.awt.Rectangle;
import java.util.Set;

public interface INode {
	public Rectangle getRect();
	public void setRect(Rectangle rec);

	public Set<INode> getUpNodes();
	public Set<INode> getDownNodes();
	public Set<INode> getLeftNodes();
	public Set<INode> getRightNodes();
}

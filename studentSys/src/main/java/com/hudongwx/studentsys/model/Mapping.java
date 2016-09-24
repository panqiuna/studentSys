package com.hudongwx.studentsys.model;

import com.hudongwx.studentsys.common.BaseMapping;
import com.hudongwx.studentsys.util.TreeNode;

/**
 * Generated by JFinal.............wuhongxu
 */
@SuppressWarnings("serial")
public class Mapping extends BaseMapping<Mapping> implements TreeNode {
	public static final Mapping dao = new Mapping();
	private Mapping parent,leftChild,nextSibling;
	public Mapping(){
		init();
	}
	public Mapping(String icon,String title,String url){
		setIcon(icon);
		setTitle(title);
		setUrl(url);
		init();
	}
	public void init(){
		getParent();
		getLeftChild();
		getNextSibling();
		setChildCount(0);
		setParentId(0);
		setLeftChildId(0);
		setNextSiblingId(0);
	}
	@Override
	public Mapping getParent() {
		return parent;
	}

	@Override
	public void setParent(TreeNode treeNode) {
		parent = (Mapping) treeNode;
	}

	@Override
	public void setLeftChild(TreeNode treeNode) {
		leftChild = (Mapping)treeNode;
	}

	@Override
	public Mapping getLeftChild() {
		return leftChild;
	}

	@Override
	public void setNextSibling(TreeNode treeNode) {
		nextSibling = (Mapping) treeNode;
	}

	@Override
	public Mapping getNextSibling() {
		return nextSibling;
	}

	public boolean equals(Mapping mapping) {
		return mapping.getId().equals(getId()) && mapping.getTitle().equals(getTitle()) && mapping.getUrl().equals(getUrl());
	}
}

package com.hudongwx.studentsys.common;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseTestTagQuestion<M extends BaseTestTagQuestion<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setTagId(java.lang.Integer tagId) {
		set("tagId", tagId);
	}

	public java.lang.Integer getTagId() {
		return get("tagId");
	}

	public void setTestQuestionId(java.lang.Integer testQuestionId) {
		set("testQuestionId", testQuestionId);
	}

	public java.lang.Integer getTestQuestionId() {
		return get("testQuestionId");
	}

}

package com.hudongwx.studentsys.model;

import com.hudongwx.studentsys.common.BaseDomain;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Domain extends BaseDomain<Domain> {
	public static final Domain dao = new Domain();
	public static final String SEARCH_FROM_DOMAIN = "select * from stumanager_domain ";
	public static final String FROM_DQL = " from stumanager_domain ";

	private String tags;

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
}

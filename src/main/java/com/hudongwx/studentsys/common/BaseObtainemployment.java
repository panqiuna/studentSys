package com.hudongwx.studentsys.common;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseObtainemployment<M extends BaseObtainemployment<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setClassName(java.lang.String className) {
		set("className", className);
	}

	public java.lang.String getClassName() {
		return get("className");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setEmploymentStatus(java.lang.String employmentStatus) {
		set("employmentStatus", employmentStatus);
	}

	public java.lang.String getEmploymentStatus() {
		return get("employmentStatus");
	}

	public void setTrackTime(java.lang.Long trackTime) {
		set("trackTime", trackTime);
	}

	public java.lang.Long getTrackTime() {
		return get("trackTime");
	}

	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}

	public java.lang.String getRemark() {
		return get("remark");
	}

	public void setApproveStatus(java.lang.String approveStatus) {
		set("approveStatus", approveStatus);
	}

	public java.lang.String getApproveStatus() {
		return get("approveStatus");
	}

	public void setApprover(java.lang.String approver) {
		set("approver", approver);
	}

	public java.lang.String getApprover() {
		return get("approver");
	}

}

package com.hudongwx.studentsys.common;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSubsidyClassInfo<M extends BaseSubsidyClassInfo<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setClassId(java.lang.Integer classId) {
		set("classId", classId);
	}

	public java.lang.Integer getClassId() {
		return get("classId");
	}

	public void setClassName(java.lang.String className) {
		set("className", className);
	}

	public java.lang.String getClassName() {
		return get("className");
	}

	public void setRegionId(java.lang.Integer regionId) {
		set("regionId", regionId);
	}

	public java.lang.Integer getRegionId() {
		return get("regionId");
	}

	public void setStudentId(java.lang.Integer studentId) {
		set("studentId", studentId);
	}

	public java.lang.Integer getStudentId() {
		return get("studentId");
	}

	public void setStudentName(java.lang.String studentName) {
		set("studentName", studentName);
	}

	public java.lang.String getStudentName() {
		return get("studentName");
	}

	public void setSubsidyAmount(java.math.BigDecimal subsidyAmount) {
		set("subsidyAmount", subsidyAmount);
	}

	public java.math.BigDecimal getSubsidyAmount() {
		return get("subsidyAmount");
	}

	public void setBonus(java.math.BigDecimal bonus) {
		set("bonus", bonus);
	}

	public java.math.BigDecimal getBonus() {
		return get("bonus");
	}

	public void setResidualFrequency(java.lang.Integer residualFrequency) {
		set("residualFrequency", residualFrequency);
	}

	public java.lang.Integer getResidualFrequency() {
		return get("residualFrequency");
	}

	public void setStudentStatusId(java.lang.Integer studentStatusId) {
		set("studentStatusId", studentStatusId);
	}

	public java.lang.Integer getStudentStatusId() {
		return get("studentStatusId");
	}

	public void setStudentStatusName(java.lang.String studentStatusName) {
		set("studentStatusName", studentStatusName);
	}

	public java.lang.String getStudentStatusName() {
		return get("studentStatusName");
	}

	public void setApproveStatus(java.lang.Integer approveStatus) {
		set("approveStatus", approveStatus);
	}

	public java.lang.Integer getApproveStatus() {
		return get("approveStatus");
	}

	public void setApplicationDate(java.lang.Long applicationDate) {
		set("applicationDate", applicationDate);
	}

	public java.lang.Long getApplicationDate() {
		return get("applicationDate");
	}

	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}

	public java.lang.String getRemark() {
		return get("remark");
	}

	public void setChecked(java.lang.Integer checked) {
		set("checked", checked);
	}

	public java.lang.Integer getChecked() {
		return get("checked");
	}

	public void setApproveDate(java.lang.Long approveDate) {
		set("approveDate", approveDate);
	}

	public java.lang.Long getApproveDate() {
		return get("approveDate");
	}

	public void setOperaterId(java.lang.Integer operaterId) {
		set("operaterId", operaterId);
	}

	public java.lang.Integer getOperaterId() {
		return get("operaterId");
	}

	public void setOperater(java.lang.String operater) {
		set("operater", operater);
	}

	public java.lang.String getOperater() {
		return get("operater");
	}

}

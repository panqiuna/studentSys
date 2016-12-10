package com.hudongwx.studentsys.common;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseStudentEmployment<M extends BaseStudentEmployment<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
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

	public void setPhoneNumber(java.lang.String phoneNumber) {
		set("phoneNumber", phoneNumber);
	}

	public java.lang.String getPhoneNumber() {
		return get("phoneNumber");
	}

	public void setEmploymentStatus(java.lang.Integer employmentStatus) {
		set("employmentStatus", employmentStatus);
	}

	public java.lang.Integer getEmploymentStatus() {
		return get("employmentStatus");
	}

	public void setEducationBackground(java.lang.String educationBackground) {
		set("educationBackground", educationBackground);
	}

	public java.lang.String getEducationBackground() {
		return get("educationBackground");
	}

	public void setCompany(java.lang.String company) {
		set("company", company);
	}

	public java.lang.String getCompany() {
		return get("company");
	}

	public void setSalary(java.math.BigDecimal salary) {
		set("salary", salary);
	}

	public java.math.BigDecimal getSalary() {
		return get("salary");
	}

	public void setEmploymentTime(java.lang.Long employmentTime) {
		set("employmentTime", employmentTime);
	}

	public java.lang.Long getEmploymentTime() {
		return get("employmentTime");
	}

	public void setCounselorId(java.lang.Integer counselorId) {
		set("counselorId", counselorId);
	}

	public java.lang.Integer getCounselorId() {
		return get("counselorId");
	}

	public void setCounselorName(java.lang.String counselorName) {
		set("counselorName", counselorName);
	}

	public java.lang.String getCounselorName() {
		return get("counselorName");
	}

	public void setApproveStatus(java.lang.Integer approveStatus) {
		set("approveStatus", approveStatus);
	}

	public java.lang.Integer getApproveStatus() {
		return get("approveStatus");
	}

	public void setApproverId(java.lang.Integer approverId) {
		set("approverId", approverId);
	}

	public java.lang.Integer getApproverId() {
		return get("approverId");
	}

	public void setApprover(java.lang.String approver) {
		set("approver", approver);
	}

	public java.lang.String getApprover() {
		return get("approver");
	}

	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}

	public java.lang.String getRemark() {
		return get("remark");
	}

	public void setOperatorId(java.lang.Integer operatorId) {
		set("operatorId", operatorId);
	}

	public java.lang.Integer getOperatorId() {
		return get("operatorId");
	}

	public void setOperator(java.lang.String operator) {
		set("operator", operator);
	}

	public java.lang.String getOperator() {
		return get("operator");
	}

}

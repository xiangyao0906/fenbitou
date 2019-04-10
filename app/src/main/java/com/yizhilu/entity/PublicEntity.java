package com.yizhilu.entity;

import java.io.Serializable;

/**
 * 类说明: 公共的实体类
 */
public class PublicEntity implements Serializable {

	private String message;
	private boolean success;
	private EntityPublic entity;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public EntityPublic getEntity() {
		return entity;
	}
	public void setEntity(EntityPublic entity) {
		this.entity = entity;
	}
}

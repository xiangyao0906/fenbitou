package com.yizhilu.entity;


import java.util.List;

/**
 * 类说明: 公共的实体类
 */
public class PublicListEntity {

	private String message;
	private boolean success;
	private List<EntityPublic> entity;

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

	public List<EntityPublic> getEntity() {
		return entity;
	}

	public void setEntity(List<EntityPublic> entity) {
		this.entity = entity;
	}
}

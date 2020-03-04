/**
 * 
 */
package com.bmw.rest.model;

/**
 * @author QXZ0V7C
 *
 */
public class RestResponse {
	String messages;
	Result result;
	/**
	 * @return the messages
	 */
	public String getMessages() {
		return messages;
	}
	/**
	 * @param messages the messages to set
	 */
	public void setMessages(String messages) {
		this.messages = messages;
	}
	/**
	 * @return the result
	 */
	public Result getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(Result result) {
		this.result = result;
	}
	/**
	 * @param messages
	 * @param result
	 */
	public RestResponse(String messages, Result result) {
		this.messages = messages;
		this.result = result;
	}
	/**
	 * 
	 */
	public RestResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

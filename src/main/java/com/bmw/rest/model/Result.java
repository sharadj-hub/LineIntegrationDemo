/**
 * 
 */
package com.bmw.rest.model;

/**
 * @author QXZ0V7C
 *
 */
public class Result {
	String name;
    String alpha2_code;
    String alpha3_code;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the alpha2_code
	 */
	public String getAlpha2_code() {
		return alpha2_code;
	}
	/**
	 * @param alpha2_code the alpha2_code to set
	 */
	public void setAlpha2_code(String alpha2_code) {
		this.alpha2_code = alpha2_code;
	}
	/**
	 * @return the alpha3_code
	 */
	public String getAlpha3_code() {
		return alpha3_code;
	}
	/**
	 * @param alpha3_code the alpha3_code to set
	 */
	public void setAlpha3_code(String alpha3_code) {
		this.alpha3_code = alpha3_code;
	}
	/**
	 * @param name
	 * @param alpha2_code
	 * @param alpha3_code
	 */
	public Result(String name, String alpha2_code, String alpha3_code) {
		this.name = name;
		this.alpha2_code = alpha2_code;
		this.alpha3_code = alpha3_code;
	}
    
    
}

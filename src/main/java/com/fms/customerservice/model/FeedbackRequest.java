/**
 * @author quochq
 */
package com.fms.customerservice.model;

/**
 * @author quochq
 *
 */
public class FeedbackRequest {
	String content;
	Integer rating;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	
}

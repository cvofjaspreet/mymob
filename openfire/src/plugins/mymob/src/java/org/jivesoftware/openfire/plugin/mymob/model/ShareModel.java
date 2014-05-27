package org.jivesoftware.openfire.plugin.mymob.model;

/**
 * 
 * 
 * @author jassi
 *
 *
 *shareId        BIGINT        AUTO_INCREMENT,
shareDate      BIGINT        NOT NULL,
text           VARCHAR(2000)       NULL,
imageUrl       VARCHAR(200)       NULL,
videoUrl       VARCHAR(200)       NOT NULL,
jid            VARCHAR(200)       NOT NULL,
type           VARCHAR(200)       NOT NULL,
 */


public class ShareModel {
	private long id;
	private long date;
	private String text;
	private String imageUri;
	private String imageUrl;
	private String videoUri;
	private String videoUrl;
	private String jid;
	private String type;
	private String thumbUrl;
	private String thumbUri;
	
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	public String getThumbUri() {
		return thumbUri;
	}
	public void setThumbUri(String thumbUri) {
		this.thumbUri = thumbUri;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getImageUri() {
		return imageUri;
	}
	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getVideoUri() {
		return videoUri;
	}
	public void setVideoUri(String videoUri) {
		this.videoUri = videoUri;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
}

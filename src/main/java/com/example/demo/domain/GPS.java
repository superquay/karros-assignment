package com.example.demo.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class GPS {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String version;
	
	@Column
	private String creator;
	
	@Column
	private String name;
	
	@Column(columnDefinition = "varchar2(2000)")
	private String description;
	
	@Column
	private String author;
	
	@Column
	private String linkHref;
	
	@Column
	private String linkText;
	
	@Column
	private Date time;
	
	@Transient
	List<WayPoint> waypoints;
	
	@Transient
	List<Track> tracks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLinkHref() {
		return linkHref;
	}

	public void setLinkHref(String linkHref) {
		this.linkHref = linkHref;
	}

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public List<WayPoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<WayPoint> waypoints) {
		this.waypoints = waypoints;
	}

	public List<Track> getTracks() {
		return tracks;
	}

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}

}

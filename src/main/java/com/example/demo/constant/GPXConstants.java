package com.example.demo.constant;

public class GPXConstants {
	
	//Root node
	public static final String GPX_NODE = "gpx";
	public static final String GPX_NODE_CREATOR_NODE = "creator";
	public static final String GPX_NODE_VERSION_NODE = "version";
	
	//Metadata node
	public static final String METADATA_NODE = "metadata";
	public static final String METADATA_NODE_NAME_NODE = "name";
	public static final String METADATA_NODE_DESC_NODE = "desc";
	public static final String METADATA_NODE_AUTHOR_NODE = "author";
	public static final String METADATA_NODE_LINK_NODE = "link";
	public static final String METADATA_NODE_LINK_NODE_HREF_ATTR = "href";
	public static final String METADATA_NODE_LINK_NODE_TEXT = "text";
	public static final String METADATA_NODE_TIME_NODE = "time";
	
	//Waypoint node
	public static final String WPT_NODE = "wpt";
	public static final String WPT_NODE_LON_ATTR = "lon";
	public static final String WPT_NODE_LAT_ATTR = "lat";
	public static final String WPT_NODE_NAME_NODE = "name";
	public static final String WPT_NODE_SYM_NODE = "sym";
	
	//Track node
	public static final String TRACK_NODE = "trk";
	public static final String TRACK_SEQMENT_NODE = "trkseg";
	public static final String TRACK_PT_NODE = "trkpt";
	public static final String TRACK_NODE_LON_ATTR = "lon";
	public static final String TRACK_NODE_LAT_ATTR = "lat";
	public static final String TRACK_NODE_ELE_NODE = "ele";
	public static final String TRACK_NODE_TIME_NODE = "time";
	
}

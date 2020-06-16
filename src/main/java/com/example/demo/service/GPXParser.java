package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.example.demo.constant.GPXConstants;
import com.example.demo.domain.GPS;
import com.example.demo.domain.Track;
import com.example.demo.domain.WayPoint;

public class GPXParser {

	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	public static GPS parseGPXStream(InputStream is) throws ParserConfigurationException, IOException, ParseException, SAXException {
		DocumentBuilder builder;
		GPS gps = null;
		List<WayPoint> waypoints = new ArrayList<>();
		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(is);
		Node firstChild = document.getFirstChild();
		if (firstChild != null && GPXConstants.GPX_NODE.equals(firstChild.getNodeName())) {
			gps = new GPS();

			// Map GPX version & creator
			NamedNodeMap attrs = firstChild.getAttributes();
			gps.setVersion(getNodeValueAsString(attrs.getNamedItem(GPXConstants.GPX_NODE_VERSION_NODE)));
			gps.setCreator(getNodeValueAsString(attrs.getNamedItem(GPXConstants.GPX_NODE_CREATOR_NODE)));

			NodeList nodes = firstChild.getChildNodes();

			for (int idx = 0; idx < nodes.getLength(); idx++) {
				Node currentNode = nodes.item(idx);
				if (GPXConstants.METADATA_NODE.equals(currentNode.getNodeName())) {
					parseMetadataNode(gps, currentNode);
				} else if (GPXConstants.WPT_NODE.equals(currentNode.getNodeName())) {
					WayPoint wp = parseWaypoint(currentNode);
					if (wp != null) {
						waypoints.add(wp);
					}
				} else if (GPXConstants.TRACK_NODE.equals(currentNode.getNodeName())) {
					gps.setTracks(parseTrack(currentNode));
				}
			}
			gps.setWaypoints(waypoints);
		}

		return gps;
	}

	private static void parseMetadataNode(GPS gps, Node metadata) throws ParseException {
		if (metadata != null) {
			NodeList childNodes = metadata.getChildNodes();
			if (childNodes != null) {
				for (int idx = 0; idx < childNodes.getLength(); idx++) {
					Node currentNode = childNodes.item(idx);
					if (GPXConstants.METADATA_NODE_NAME_NODE.equals(currentNode.getNodeName())) {
						gps.setName(getNodeValueAsString(currentNode));
					} else if (GPXConstants.METADATA_NODE_DESC_NODE.equals(currentNode.getNodeName())) {
						gps.setDescription(getNodeValueAsString(currentNode));
					} else if (GPXConstants.METADATA_NODE_AUTHOR_NODE.equals(currentNode.getNodeName())) {
						gps.setAuthor(getNodeValueAsString(currentNode));
					} else if (GPXConstants.METADATA_NODE_LINK_NODE.equals(currentNode.getNodeName())) {
						parseMetadataLink(gps, currentNode);
					} else if (GPXConstants.METADATA_NODE_TIME_NODE.equals(currentNode.getNodeName())) {
						gps.setTime(getNodeValueAsDate(currentNode));
					}
				}
			}
		}
	}

	private static WayPoint parseWaypoint(Node wptNode) {
		WayPoint wp = new WayPoint();
		NamedNodeMap attrs = wptNode.getAttributes();
		wp.setLattitude(getNodeValueAsString(attrs.getNamedItem(GPXConstants.WPT_NODE_LAT_ATTR)));
		wp.setLongtitude(getNodeValueAsString(attrs.getNamedItem(GPXConstants.WPT_NODE_LON_ATTR)));
		NodeList childNodes = wptNode.getChildNodes();
		if (childNodes != null) {
			for (int idx = 0; idx < childNodes.getLength(); idx++) {
				Node currentNode = childNodes.item(idx);
				if (GPXConstants.WPT_NODE_NAME_NODE.equals(currentNode.getNodeName())) {
					wp.setName(getNodeValueAsString(currentNode));
				} else if (GPXConstants.WPT_NODE_SYM_NODE.equals(currentNode.getNodeName())) {
					wp.setSym(getNodeValueAsString(currentNode));
				}
			}
		}

		return wp;
	}
	
	private static List<Track> parseTrack(Node trackNode) throws ParseException {
		List<Track> tracks = new ArrayList<>();
		NodeList childNodes = trackNode.getChildNodes();
		if (childNodes != null) {
			for (int idx = 0; idx < childNodes.getLength(); idx++) {
				Node currentNode = childNodes.item(idx);
				if(GPXConstants.TRACK_SEQMENT_NODE.equals(currentNode.getNodeName())) {
					tracks.addAll(parseTrackSeq(currentNode.getChildNodes()));
				}
			}
		}
		return tracks;
	}
	
	private static List<Track> parseTrackSeq(NodeList items) throws ParseException {
		List<Track> tracks = new ArrayList<>();
		for (int idx = 0; idx < items.getLength(); idx++) {
			Node currentNode = items.item(idx);
			if(GPXConstants.TRACK_PT_NODE.equals(currentNode.getNodeName())) {
				Track t = parseTrackPoint(currentNode);
				tracks.add(t);
			}
		}
		return tracks;
	}
	
	private static Track parseTrackPoint(Node trackPntNode) throws ParseException {
		Track track = new Track();
		NamedNodeMap attrs = trackPntNode.getAttributes();
		track.setLattitude(getNodeValueAsString(attrs.getNamedItem(GPXConstants.TRACK_NODE_LAT_ATTR)));
		track.setLongtitude(getNodeValueAsString(attrs.getNamedItem(GPXConstants.TRACK_NODE_LON_ATTR)));
		NodeList childNodes = trackPntNode.getChildNodes();
		if (childNodes != null) {
			for (int idx = 0; idx < childNodes.getLength(); idx++) {
				Node currentNode = childNodes.item(idx);
				if (GPXConstants.TRACK_NODE_ELE_NODE.equals(currentNode.getNodeName())) {
					track.setElevation(getNodeValueAsString(currentNode));
				} else if (GPXConstants.TRACK_NODE_TIME_NODE.equals(currentNode.getNodeName())) {
					track.setTime(getNodeValueAsDate(currentNode));
				}
			}
		}
		return track;
	}

	private static void parseMetadataLink(GPS gps, Node linkNode) {
		if (linkNode == null) {
			return;
		}
		NamedNodeMap attrs = linkNode.getAttributes();
		gps.setLinkHref(getNodeValueAsString(attrs.getNamedItem(GPXConstants.METADATA_NODE_LINK_NODE_HREF_ATTR)));

		NodeList childNodes = linkNode.getChildNodes();
		if (childNodes != null) {
			for (int idx = 0; idx < childNodes.getLength(); idx++) {
				Node currentNode = childNodes.item(idx);
				if (GPXConstants.METADATA_NODE_LINK_NODE_TEXT.equals(currentNode.getNodeName())) {
					gps.setLinkText(getNodeValueAsString(currentNode));
				}
			}
		}
	}

	private static Date getNodeValueAsDate(Node node) throws ParseException {
		String nodeValue = getNodeValueAsString(node);
		if (nodeValue == null) {
			return null;
		}
		SimpleDateFormat formatter2 = new SimpleDateFormat(DATE_TIME_FORMAT);
		return formatter2.parse(nodeValue);
	}

	private static String getNodeValueAsString(Node node) {
		if (node == null) {
			return null;
		}

		return node.getFirstChild() != null ? node.getFirstChild().getNodeValue() : null;
	}
}

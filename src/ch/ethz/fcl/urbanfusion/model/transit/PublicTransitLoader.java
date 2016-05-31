/*
 * Copyright (C) 2010 - 2016 ETH Zurich
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Zeng Wei (zeng@arch.ethz.ch)
 */

package ch.ethz.fcl.urbanfusion.model.transit;

public class PublicTransitLoader {
	// private SAXParserFactory parserFactory = SAXParserFactory.newInstance();

	public PublicTransitLoader() {
		// try {
		//
		// // loadTransitSchedule(
		// // getClass().getResource("transitSchedule.xml").openStream());
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	// private void loadTransitSchedule(InputStream is) {
	// try {
	// SAXParser parser = parserFactory.newSAXParser();
	// parser.parse(is, new DefaultHandler() {
	// @Override
	// public InputSource resolveEntity(String publicId,
	// String systemId) throws org.xml.sax.SAXException,
	// java.io.IOException {
	// System.out
	// .println("Ignoring: " + publicId + ", " + systemId);
	// return new InputSource(new java.io.StringReader(""));
	// }
	//
	// TransitLine line = null;
	// TransitRoute route = null;
	//
	// boolean addMode = false;
	//
	// public void startElement(String uri, String localName,
	// String name, Attributes attributes)
	// throws SAXException {
	// if (name.equals("stopFacility")) {
	// data.getPublicTransit().addStops(attributes);
	// }
	// if (name.equals("transitLine"))
	// line = new TransitLine(data, data.getPublicTransit(),
	// attributes.getValue("id"));
	//
	// if (name.equals("transitRoute")) {
	// // MBTools.debug("route " + attributes.getValue("id"),
	// // false);
	// String routeId = attributes.getValue("id");
	//
	// if (routeId.contains("-p")
	// || (routeId.contains("daily")
	// && !routeId.contains("CC_daily"))
	// || routeId.contains("morning"))
	// route = null;
	// else
	// route = line.getRoute(attributes.getValue("id"));
	// }
	//
	// if (name.equals("transportMode") && route != null) {
	// addMode = true;
	// }
	//
	// if (name.equals("stop")) {
	// String stopid = attributes.getValue("refId");
	// String timeStr = attributes.getValue("arrivalOffset");
	// float time = TimerUtil.convertTimeFromString(timeStr);
	//
	// if (route != null) {
	// Stop stop = data.getPublicTransit()
	// .getStopByStopId(stopid);
	//
	// route.addStop(stop);
	// if (route.getMode().equals("subway"))
	// route.addStopTime(time);
	// }
	// }
	//
	// if (name.equals("link")) {
	// String refid = attributes.getValue("refId");
	// if (route != null)
	// route.addEdge(data.getScene().getEdge(refid));
	// }
	// }
	//
	// public void endElement(String uri, String localName,
	// String name) throws SAXException {
	// if (name.equals("transitRoute"))
	// if (line != null && route != null) {
	// line.addRoute(route);
	// }
	//
	// if (name.equals("transitLine"))
	// if (line != null)
	// data.getPublicTransit().addTransitLines(line);
	// }
	//
	// @Override
	// public void characters(char[] ch, int start, int length)
	// throws SAXException {
	// if (addMode) {
	// route.setMode(
	// String.copyValueOf(ch, start, length).trim());
	// addMode = false;
	// }
	// }
	//
	// });
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}

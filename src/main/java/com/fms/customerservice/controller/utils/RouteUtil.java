package com.fms.customerservice.controller.utils;

import java.awt.Point;

public class RouteUtil {
	/**
	 * Returns distance to segment
	 * 
	 * @param ss segment start point
	 * @param se segment end point
	 * @param p  point to found closest point on segment
	 * @return distance to segment
	 */
	public static double getDistanceToSegment(Point ss, Point se, Point p) {
		return getDistanceToSegment(ss.x, ss.y, se.x, se.y, p.x, p.y);
	}

	/**
	 * Returns distance to segment
	 * 
	 * @param sx1 segment x coord 1
	 * @param sy1 segment y coord 1
	 * @param sx2 segment x coord 2
	 * @param sy2 segment y coord 2
	 * @param px  point x coord
	 * @param py  point y coord
	 * @return distance to segment
	 */
	public static double getDistanceToSegment(int sx1, int sy1, int sx2, int sy2, int px, int py) {
		Point closestPoint = getClosestPointOnSegment(sx1, sy1, sx2, sy2, px, py);
		return getDistance(closestPoint.x, closestPoint.y, px, py);
	}

	/**
	 * Returns distance between two 2D points
	 * 
	 * @param point1 first point
	 * @param point2 second point
	 * @return distance between points
	 */
	public static double getDistance(Point point1, Point point2) {
		return getDistance(point1.x, point1.y, point2.x, point2.y);
	}

	/**
	 * Returns distance between two sets of coords
	 * 
	 * @param x1 first x coord
	 * @param y1 first y coord
	 * @param x2 second x coord
	 * @param y2 second y coord
	 * @return distance between sets of coords
	 */
	public static double getDistance(float x1, float y1, float x2, float y2) {
		// using long to avoid possible overflows when multiplying
		double dx = x2 - x1;
		double dy = y2 - y1;

		// return Math.hypot(x2 - x1, y2 - y1); // Extremely slow
		// return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); // 20 times faster than
		// hypot
		return Math.sqrt(dx * dx + dy * dy); // 10 times faster then previous line
	}

	/**
	 * Returns closest point on segment to point
	 * 
	 * @param ss segment start point
	 * @param se segment end point
	 * @param p  point to found closest point on segment
	 * @return closest point on segment to p
	 */
	public static Point getClosestPointOnSegment(Point ss, Point se, Point p) {
		return getClosestPointOnSegment(ss.x, ss.y, se.x, se.y, p.x, p.y);
	}

	/**
	 * Returns closest point on segment to point
	 * 
	 * @param sx1 segment x coord 1
	 * @param sy1 segment y coord 1
	 * @param sx2 segment x coord 2
	 * @param sy2 segment y coord 2
	 * @param px  point x coord
	 * @param py  point y coord
	 * @return closets point on segment to point
	 */
	public static Point getClosestPointOnSegment(int sx1, int sy1, int sx2, int sy2, int px, int py) {
		double xDelta = (sx2 - sx1)*0.1;
		double yDelta = (sy2 - sy1)*0.1;
		double zero = 0.0;
		final double THRESHOLD = .0001;
		if ((Math.abs(xDelta - zero) < THRESHOLD) && (Math.abs(yDelta - zero) < THRESHOLD)) {
			throw new IllegalArgumentException("Segment start equals segment end");
		}

		double u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

		final Point closestPoint;
		if (u < 0.0) {
			closestPoint = new Point(sx1, sy1);
		} else if (u > 1) {
			closestPoint = new Point(sx2, sy2);
		} else {
			closestPoint = new Point((int) Math.round(sx1 + u * xDelta), (int) Math.round(sy1 + u * yDelta));
		}

		return closestPoint;
	}
}

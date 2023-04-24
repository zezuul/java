//Julia Zezula listopad 2022

import java.util.*;

public class Lines implements LinesInterface {
    Graph graph = new Graph();

    public void addPoints(Set<LinesInterface.Point> points) {
        for (LinesInterface.Point px : points) {
            graph.addPoint(px);
        }
    }

    public void addSegments(Set<LinesInterface.Segment> segments) {
        //dodanie zbioru odcinkow laczacych punkty
        for (LinesInterface.Segment s : segments) {
            graph.connect(new PointClass(s.getEndpoint1()), new PointClass(s.getEndpoint2()));
        }
    }

    public Map<LinesInterface.Point, Set<LinesInterface.Segment>> getMapEndpointToSegments() {
        Map<LinesInterface.Point, Set<LinesInterface.Segment>> result = new HashMap<LinesInterface.Point, Set<LinesInterface.Segment>>();

        for (LinesInterface.Point point : graph.graphMap.keySet()) {
            HashSet<LinesInterface.Segment> r = new HashSet();
            for (LinesInterface.Point end : graph.graphMap.get(point)) {
                r.add(new Lines.Segment(point, end));
            }
            result.put(point, r);
        }

        return result;
    }

    public List<LinesInterface.Segment> findConnection(Point startIf, Point endIf) {

        List<LinesInterface.Segment> foundConnections = new ArrayList<LinesInterface.Segment>();
        Stack<PointClass> visited = new Stack<>();
        HashSet<PointClass> visitedAll = new HashSet();
        PointClass start = new PointClass(startIf);
        PointClass end = new PointClass(endIf);

        visited.push(start);
        visitedAll.add(start);

        while (true) {
            HashSet<PointClass> nextPoints = graph.graphMap.get(start);
            boolean madeStep = false;
            if (nextPoints != null) {
                for (PointClass nextPoint : nextPoints) {
                    if (visitedAll.contains(nextPoint)) {
                        continue;
                    } else if (nextPoint.equals(end)) {
                        visited.push(nextPoint);
                        madeStep = true;

                        PointClass before = null;
                        for (PointClass p : visited) {
                            if (before != null) {
                                foundConnections.add(new Segment(before, p));
                            }
                            before = p;
                        }
                        return foundConnections;
                    } else {
                        visited.push(nextPoint);
                        visitedAll.add(nextPoint);
                        start = nextPoint;
                        madeStep = true;
                        break;
                    }
                }
                if (!madeStep) {
                    if (visitedAll.size() < graph.graphPoints.size() && visited.size() > 1) {
                        visited.pop();
                        start = visited.pop();
                        visited.push(start);
                    } else {
                        return foundConnections;
                    }
                }
            } else {
                return foundConnections;
            }
        }
    }

    protected void discoverPaths(PointClass start, Map<Integer, Set<LinesInterface.Point>> result, int depth, final HashSet<LinesInterface.Point> visitedAll) {

        if (depth == 4) return;
        Integer key = Integer.valueOf(++depth);
        Set<LinesInterface.Point> res = result.get(key);

        if (res == null) {
            res = new HashSet();
            result.put(key, res);
        }

        for (PointClass p : graph.graphMap.get(start)) {
            if (!visitedAll.contains(p)) {
                HashSet<LinesInterface.Point> visitedAll2 = (HashSet<LinesInterface.Point>) visitedAll.clone();

                visitedAll2.add(p);
                res.add(p);
                discoverPaths(p, result, depth, visitedAll2);
            } else {
                continue;
            }
        }
    }

    public Map<LinesInterface.Point, Map<Integer, Set<LinesInterface.Point>>> getReachableEndpoints() {
        Map<LinesInterface.Point, Map<Integer, Set<LinesInterface.Point>>> result = new HashMap<>();
        for (PointClass start : graph.graphPoints) {
            HashMap<Integer, Set<LinesInterface.Point>> res = new HashMap();
            result.put(start, res);
            HashSet<LinesInterface.Point> visitedAll = new HashSet();
            visitedAll.add(start);
            discoverPaths(start, res, 0, visitedAll);
            for (int i=1; i<=4;i++)
            {
             Integer iInt = Integer.valueOf(i);
             if (res.get(iInt) == null)
             {
                 res.put(iInt, new HashSet());
             }
            }

        }


        return result;
    }

    static class PointClass implements LinesInterface.Point, Comparable {
        String pointName;

        public PointClass(String name) {
            pointName = name;
        }

        public PointClass(Point p) {
            pointName = p.getName();
        }

        public String getName() {
            //zwroc nazwe odcinka
            return pointName;
        }

        @Override
        public int hashCode() {
            return pointName.hashCode();
        }

        @Override
        public boolean equals(Object p) {
            if (p != null && p instanceof Point) {
                return ((Point) p).getName().equals(this.getName());
            } else {
                return false;
            }
        }

        public int compareTo(Object p) {
            if (p != null && p instanceof Point) {
                return ((Point) p).getName().compareTo(this.getName());
            } else {
                return 1;
            }

        }

    }

    static class Segment implements LinesInterface.Segment {

        Point point1;
        Point point2;

        public Segment(Point firstPoint, Point secondPoint) {
            point1 = firstPoint;
            point2 = secondPoint;
        }

        public Point getEndpoint1() {
            //pierwszy kraniec odcinka
            return point1;
        }

        public Point getEndpoint2() {
            //drugi kraniec odcicnka
            return point2;
        }
    }

    static class Graph {
        ArrayList<PointClass> graphPoints = new ArrayList<PointClass>();
        HashMap<PointClass, HashSet<PointClass>> graphMap = new HashMap();

        public Map<PointClass, HashSet<PointClass>> connect(PointClass a, PointClass b) {
            if (!graphPoints.contains(a)) {
                System.out.println("Point: " + a.getName() + " is not defined via setPoints methods!");
                //  throw new NullPointerException();
                return graphMap;
            }
            if (!graphPoints.contains(b)) {
                System.out.println("Point: " + b.getName() + " is not defined via setPoints methods!");
                // throw new NullPointerException();
                return graphMap;
            }

            addConnection(graphMap, a, b);
            return graphMap;
        }

        protected void addConnection(HashMap<PointClass, HashSet<PointClass>> graphMap, PointClass a, PointClass b) {
            addConnectionFromTo(graphMap, a, b);
            addConnectionFromTo(graphMap, b, a);
        }

        protected void addConnectionFromTo(HashMap<PointClass, HashSet<PointClass>> graphMap, PointClass from, PointClass to) {
            if (from == null) {
                throw new NullPointerException("This method needs a valid from Point!");
            }


            HashSet<PointClass> na = addPoint(from);
            if (to != null && na != null) {
                na.add(to);
            }
        }

        public HashSet<PointClass> addPoint(LinesInterface.Point px) {
            Lines.PointClass p = new Lines.PointClass(px);
            HashSet<PointClass> na = graphMap.get(p);
            if (!graphPoints.contains(p)) {
                graphPoints.add(p);
            }

            if (na == null) {
                na = new HashSet<PointClass>();
                graphMap.put(p, na);
            }
            return na;
        }
    }
}

import java.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sound.sampled.Line;

public class LinesTester{

    Lines linesUnderTest;
    LinesInterface.Point a = new Lines.PointClass("A");
    LinesInterface.Point b = new Lines.PointClass("B");
    LinesInterface.Point c = new Lines.PointClass("C");
    LinesInterface.Point d = new Lines.PointClass("D");
    LinesInterface.Point e = new Lines.PointClass("E");
    LinesInterface.Point f = new Lines.PointClass("F");
    LinesInterface.Point g = new Lines.PointClass("G");
    LinesInterface.Point h = new Lines.PointClass("H");
    LinesInterface.Point i = new Lines.PointClass("I");

    //experimenting with tests - nie dodamy do testu
    LinesInterface.Point v = new Lines.PointClass("V");

    LinesInterface.Point t = new Lines.PointClass("T");
    LinesInterface.Point r = new Lines.PointClass("R");
    LinesInterface.Point p = new Lines.PointClass("P");
    LinesInterface.Point l = new Lines.PointClass("L");

    @Test
    public void compareTest()
    {
        Assert.assertEquals(a,a);
        Assert.assertNotEquals(a,b);
        LinesInterface.Point a2 = new Lines.PointClass("A");
        Assert.assertEquals(a,a2);
    }

    @Test
    public void testAddConnectionFromTo()
    {

        Set<LinesInterface.Point> points = new HashSet<LinesInterface.Point>();
        points.add (a);
        points.add (b);
        points.add (c);

        //exp pt 2
        points.add(v);
        points.add(v);
        points.add(t);
        points.add(r);
        points.add(p);
        points.add(l);

        linesUnderTest.addPoints(points);
        Lines.Graph g = linesUnderTest.graph;
        g.connect((Lines.PointClass)a,(Lines.PointClass)c);
        Map<Lines.PointClass, HashSet<Lines.PointClass>> graphMap = g.connect((Lines.PointClass)a,(Lines.PointClass)b);
        Assert.assertNotNull(graphMap);
        Assert.assertNotNull(graphMap.get(a));
        Assert.assertTrue(graphMap.get(a).contains(b));
        Assert.assertTrue(graphMap.get(a).contains(c));
        Assert.assertNotNull(graphMap.get(b));
        Assert.assertTrue(graphMap.get(b).contains(a));
        Assert.assertNotNull(graphMap.get(c));
        Assert.assertTrue(graphMap.get(c).contains(a));
        Assert.assertFalse(graphMap.get(c).contains(b));
    }

    @Test
    public void testAddPoints(){
        Set<LinesInterface.Point> points = new HashSet<LinesInterface.Point>();
        points.add (a);
        //points.add (a);
        points.add (b);

        //exp pt 2
        points.add(v);
        points.add(t);
        points.add(r);
        points.add(p);
        points.add(l);

        linesUnderTest.addPoints(points);
        Assert.assertEquals(linesUnderTest.graph.graphPoints.size(),7);
        Assert.assertTrue(linesUnderTest.graph.graphPoints.contains(a));
        Assert.assertTrue(linesUnderTest.graph.graphPoints.contains(b));
        Assert.assertFalse(linesUnderTest.graph.graphPoints.contains(c));
        Assert.assertNotNull(linesUnderTest.graph.graphMap.get(a));
        Assert.assertNotNull(linesUnderTest.graph.graphMap.get(b));
    }

    @Before
    public void setUp() {
        linesUnderTest = new Lines();
    }

    public void addTestSegments() {
        Set<LinesInterface.Segment> segments = new HashSet<LinesInterface.Segment>();

        Set<LinesInterface.Point> points = new HashSet<LinesInterface.Point>();
        points.add (a);
        points.add (b);
        points.add (c);
        linesUnderTest.addPoints(points);

        LinesInterface.Segment s1 = new Lines.Segment(a,b);
        LinesInterface.Segment s2 = new Lines.Segment(c,b);
        segments.add(s1);
        segments.add(s2);
        linesUnderTest.addSegments(segments);
    }

    @Test
    public void testAddSegments(){
        addTestSegments();
        Assert.assertNotNull(linesUnderTest.graph.graphMap.get(a));
        Assert.assertNotNull(linesUnderTest.graph.graphMap.get(b));
        Assert.assertNotNull(linesUnderTest.graph.graphMap.get(c));
        Assert.assertTrue(linesUnderTest.graph.graphMap.get(a).contains(b));
        Assert.assertFalse(linesUnderTest.graph.graphMap.get(a).contains(c));
        Assert.assertFalse(linesUnderTest.graph.graphMap.get(a).contains(a));
        Assert.assertTrue(linesUnderTest.graph.graphMap.get(b).contains(a));
        Assert.assertTrue(linesUnderTest.graph.graphMap.get(b).contains(c));
        Assert.assertFalse(linesUnderTest.graph.graphMap.get(b).contains(b));

    }

    public void helperTestSegments(Set<LinesInterface.Segment> segs, LinesInterface.Point[] exp) {
        Assert.assertEquals(segs.size(), exp.length);
        for(LinesInterface.Segment s : segs)
        {
            Assert.assertTrue(Arrays.asList(exp).contains(s.getEndpoint2()));
        }
    }
    @Test
    public void testGetMapEndpointToSegments(){
        addTestSegments();
        Map<LinesInterface.Point, Set<LinesInterface.Segment>> res = linesUnderTest.getMapEndpointToSegments();
        Assert.assertEquals(res.size(),3);
        LinesInterface.Point[] expA = {b};
        LinesInterface.Point[] expB = {a,c};
        LinesInterface.Point[] expC = {b};
        helperTestSegments(res.get(a), expA);
        helperTestSegments(res.get(b), expB);
        helperTestSegments(res.get(c), expC);
    }

    @Test
    public void testFindConnection()
    {
        addTestSegments();
        List<LinesInterface.Segment> res = linesUnderTest.findConnection(a,c);
        //a,b i b-c
        Assert.assertNotNull(res);
        Assert.assertEquals(res.size(),2);
        Assert.assertEquals(res.get(0).getEndpoint1(),a);
        Assert.assertEquals(res.get(0).getEndpoint2(),b);
        Assert.assertEquals(res.get(1).getEndpoint1(),b);
        Assert.assertEquals(res.get(1).getEndpoint2(),c);
    }

    @Test
    public void testFindConnectionEmptyPoint()
    {
        addTestSegments();
        Set<LinesInterface.Point> points = new HashSet<LinesInterface.Point>();
        points.add (i);
        points.add (e);
        linesUnderTest.addPoints(points);
        List<LinesInterface.Segment> res = linesUnderTest.findConnection(a,c);
        //a,b i b-c
        Assert.assertNotNull(res);
        Assert.assertEquals(res.size(),2);
        Assert.assertEquals(res.get(0).getEndpoint1(),a);
        Assert.assertEquals(res.get(0).getEndpoint2(),b);
        Assert.assertEquals(res.get(1).getEndpoint1(),b);
        Assert.assertEquals(res.get(1).getEndpoint2(),c);
    }

    @Test
    public void testFindConnectionNoConnection()
    {
        List<LinesInterface.Segment> res = linesUnderTest.findConnection(a,c);
        //a,b i b-c
        Assert.assertNotNull(res);
        Assert.assertEquals(res.size(),0);
    }

    @Test
    public void testFindConnectionComplex1() {

        Set<LinesInterface.Point> points = new HashSet<LinesInterface.Point>();
        points.add (a);
        points.add (b);
        points.add (c);
        points.add (d);
        points.add (e);
        points.add (f);
        points.add (g);
        linesUnderTest.addPoints(points);

        Set<LinesInterface.Segment> segments = new HashSet<LinesInterface.Segment>();

        LinesInterface.Segment s1 = new Lines.Segment(a,b);
        LinesInterface.Segment s2 = new Lines.Segment(b,a);

        LinesInterface.Segment s3 = new Lines.Segment(c,g);
        LinesInterface.Segment s4 = new Lines.Segment(c,d);
        LinesInterface.Segment s5 = new Lines.Segment(e,d);
        LinesInterface.Segment s6 = new Lines.Segment(d,e);
        LinesInterface.Segment s7 = new Lines.Segment(d,f);

        //szukamy zapetlenia trpl
        LinesInterface.Segment s8 = new Lines.Segment(t, r);
        LinesInterface.Segment s9 = new Lines.Segment(r, p);
        LinesInterface.Segment s10 = new Lines.Segment(p, l);
        LinesInterface.Segment s11 = new Lines.Segment(l, t);
        segments.add(s8);
        segments.add(s9);
        segments.add(s10);
        segments.add(s11);


        segments.add(s1);
        segments.add(s2);
        segments.add(s3);
        segments.add(s4);
        segments.add(s5);
        segments.add(s6);
        segments.add(s7);

        segments.add(s8);

        linesUnderTest.addSegments(segments);
        Map<LinesInterface.Point, Map<Integer, Set<LinesInterface.Point>>> res = linesUnderTest.getReachableEndpoints();
        //a,b i b-c
        Assert.assertNotNull(res);
        //Assert.assertEquals(res.get(0).getEndpoint1(),a);
        //Assert.assertEquals(res.get(res.size()-1).getEndpoint2(),f);

        for(LinesInterface.Point point : res.keySet()) {
            Map<Integer, Set<LinesInterface.Point>> testMap = res.get(point);
            //System.out.println ("Segment: " + seg.getEndpoint1().getName() + " - " + seg.getEndpoint2().getName());
            System.out.println("Print for point " + point.getName());
            for(Integer key : testMap.keySet()){
                Set<LinesInterface.Point> testSet = testMap.get(key);
                System.out.print("key " + key + ": ");
                for(LinesInterface.Point p : testSet){
                    System.out.print(p.getName() +", ");
                }
                System.out.println("");
            }
        }
        linesUnderTest.getMapEndpointToSegments();
        Assert.assertEquals(linesUnderTest.findConnection(a,c).size(),0);
        //System.out.println("TEst" + res);

    }

}
public class TestDraw {

    public static void out(int[][] p) {
        for (int j = p.length - 1; j >= 0; j--) {

            for (int i = 0; i < p.length; i++) {
                System.out.print(" | " + p[i][j]); // + ":" + i + ":" + j);
            }
            System.out.println(" |");
        }
    }

    public static void main(String[] args) {
        Test g = new Test(13, 3, 6);
        TestSegment a = new TestSegment(1, 3, 1);
        TestSegment b = new TestSegment(1, 3, 2);
        TestSegment c = new TestSegment(2, 4, 3);
        TestSegment e = new TestSegment(-1, 5, 4);
        TestSegment f = new TestSegment(-2, 2, 5);
        TestSegment z = new TestSegment(-2, 13, 7);

        Drawing d = new Drawing();
        d.setCanvasGeometry(g);
        d.draw(a);
        d.draw(b);
        d.draw(c);
        //d.draw(e);
        //d.draw(f);
        //d.draw(z);

        out(d.getPainting());
        // d.clear();
        // out(d.getPainting());
    }
}

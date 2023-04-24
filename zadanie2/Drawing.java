
//Julia Zezula 22.10.2022
import java.util.Arrays;

class Drawing implements SimpleDrawing {

    private int canvas[][];
    private int x, y;

    public void setCanvasGeometry(Geometry input) {
        canvas = new int[input.getSize()][input.getSize()];
        clear();
        x = input.getInitialFirstCoordinate();
        y = input.getInitialSecondCoordinate();

    };

    protected int getNextPoint(int direction, int length) {
        if (length <= 1)
            return 0;
        switch (direction) {
            case 1:
                if (x+1 < canvas.length)
                    x++;
                break;
            case 2:
                if (y+1 < canvas.length)
                    y++;
                break;
            case -1:
                if (x > 0)
                    x--;
                break;
            case -2:
                if (y > 0)
                    y--;
                break;
        }

        return length - 1;
    }

    protected void paint(int color) {
        if (x >= 0 && y >= 0 && x < canvas.length && y < canvas[0].length) {
            canvas[x][y] = color;
        }
    }

    public void draw(Segment segment) {
        int len = segment.getLength();

        while (len > 0) {
            paint(segment.getColor());
            len = getNextPoint(segment.getDirection(), len);
        }

    };

    public int[][] getPainting() {
        return canvas;
    };

    public void clear() {
        if (canvas == null) return;
        for (int i = 0; i < canvas.length; i++) {
            Arrays.fill(canvas[i], 0);
        }
    };

}
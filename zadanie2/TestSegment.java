class TestSegment implements Segment{

    private int direction, length, color;

    public TestSegment(int direction,int length, int color)
    {
        this.direction = direction;
        this.length = length;
        this.color = color;
    }

    public int getDirection(){
        return direction;
    };
    public int getLength(){
        return length;
    };
    public int getColor(){
        return color;
    };
}

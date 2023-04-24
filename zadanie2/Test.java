class Test implements Geometry{
    
    private int size, x, y;

    public Test(int size, int x, int y)
    {
        this.size = size;
        this.x = x;
        this.y = y;
    }
    
    public int getSize()
    {
        return size;
    }
    public int getInitialFirstCoordinate()
    {
        return x;
    }
    public int getInitialSecondCoordinate()
    {
        return y;
    }
}

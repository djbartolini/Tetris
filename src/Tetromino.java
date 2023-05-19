import java.util.Random;

public class Tetromino {

    // Enum for our different Tetromino shapes
    public enum Shape {
        NO_SHAPE,
        Z_SHAPE,
        S_SHAPE,
        I_SHAPE,
        T_SHAPE,
        SQUARE_SHAPE,
        REVERSE_L_SHAPE,
        L_SHAPE
    }

    private Shape pieceShape;
    private int coords[][];
    private int[][][] coordsTable;

    // Constructor function to initialize a shape
    public Tetromino() {
        initShape();
    }

    // Define shapes using a 3d array to represent each pieces shape
    private void initShape() {
        // initiate a new shape with size 4 x 2
        coords = new int[4][2];

        coordsTable = new int[][][] {
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
                { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
                { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
                { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
        };

        setShape(Shape.NO_SHAPE);
    }

    // Use the ordinal value for each shape of the Shape enum to get shape coordinates
    protected void setShape(Shape shape) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                coords[i][j] = coordsTable[shape.ordinal()][i][j];
            }
        }
        pieceShape = shape;
    }

    // Getter and setter methods
    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    public int getX(int index) {
        return coords[index][0];
    }

    public int getY(int index) {
        return coords[index][1];
    }

    public Shape getShape() {
        System.out.println("Shape: " + pieceShape);
        return pieceShape;
    }

    // Get a random shape from the Shape enum
    public void setRandomShape() {

        var r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;

        Shape[] values = Shape.values();
        setShape(values[x]);
    }

    public int minX() {

        int m = coords[0][0];

        for (int i=0; i < 4; i++) {

            m = Math.min(m, coords[i][0]);
        }

        return m;
    }


    public int minY() {

        int m = coords[0][1];

        for (int i=0; i < 4; i++) {

            m = Math.min(m, coords[i][1]);
        }

        return m;
    }

    public Tetromino rotateLeft() {

        if (pieceShape == Shape.SQUARE_SHAPE) {
            return this;
        }

        var result = new Tetromino();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {

            result.setX(i, getY(i));
            result.setY(i, -getX(i));
        }

        return result;
    }

    public Tetromino rotateRight() {

        if (pieceShape == Shape.SQUARE_SHAPE) {
            return this;
        }

        var result = new Tetromino();
        result.pieceShape = pieceShape;


        for (int i = 0; i < 4; ++i) {

            result.setX(i, -getY(i));
            result.setY(i, getX(i));
        }

        return result;
    }
}

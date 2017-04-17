/**
 * FILE FloodFill.java
 * AUTH Tim Hall
 * DATE 4/16/2017.
 * DESC Showcases basic implementation of flood fill algorithm as well as recursion.
 */
public class FloodFIll {

    private static int[][] pixels = {
            {0,0,0,0,2,0,0,0,0,0},
            {0,0,0,0,2,0,0,0,0,0},
            {0,0,0,0,2,0,0,0,0,0},
            {0,0,0,0,2,0,0,0,0,0},
            {2,2,2,2,2,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0}
    };

    /** Tests if the current pixel is changeable and changes it. Then recursively checks
     * that pixels neighbors.
     */
    public static void fill(int x, int y, int oldPixel, int newPixel, int[][] pixels){
        if(x < 0 || y < 0) return;
        if(x >= pixels[0].length || y >= pixels.length) return;

        if(pixels[y][x] == newPixel) return;
        if(pixels[y][x] != oldPixel) return;

        pixels[y][x] = newPixel;

        fill(x - 1, y, oldPixel, newPixel, pixels);
        fill(x + 1, y, oldPixel, newPixel, pixels);
        fill(x, y - 1, oldPixel, newPixel, pixels);
        fill(x, y + 1, oldPixel, newPixel, pixels);
    }


    public static void printArray(int[][] pixels){
        for(int r = 0; r < pixels.length; r++){
            for(int c = 0; c < pixels[0].length; c++){
                System.out.print(pixels[r][c] + ",");
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args){
        printArray(pixels);
        System.out.println("********************");

        fill(7, 0, 0, 5, pixels);
        printArray(pixels);

        System.out.println("********************");
        fill(0,0,0, 3, pixels);
        printArray(pixels);
    }
}

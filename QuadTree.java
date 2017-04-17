import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * FILE QuadTree.java
 * AUTH Tim Hall
 * DATE 7/21/2016.
 * DESC Divides a 2D space into smaller spaces for more efficient parsing of rectangles inside of space.
 */
public class QuadTree {
    private final int MAX_OBJECTS = 20; //max amount of rectangles per leaf
    private final int MAX_LEVELS = 4; //max levels of splitting

    private int level;
    private LinkedList<Rectangle> objects;
    private Rectangle bounds;
    private QuadTree[] leafs;


    public QuadTree(int level, Rectangle bounds){
        this.level = level;
        this.bounds = bounds;
        this.objects = new LinkedList<>();
        leafs = new QuadTree[4];
    }

    /**Splits the leaf into 4 sub-leafs*/
    private void split(){
        int subWidth = bounds.width / 2;
        int subHeight = bounds.height / 2;
        int x = (int)bounds.getX();
        int y = (int)bounds.getY();

        leafs[0] = new QuadTree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        leafs[1] = new QuadTree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        leafs[2] = new QuadTree(level + 1, new Rectangle(x, y + subHeight, subWidth,subHeight));
        leafs[3] = new QuadTree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    /** Determins which leaf the object belongs to (starting from bottom left moving clockwise). result of -1 means
     * object cannot completely fit in a child leaf and is thus part of parent leaf.
     */
    private int getIndex(Rectangle rec){
        int index = -1;
        int verticalMidPoint = (int)(bounds.getX() + (bounds.getWidth() / 2));
        int horizontalMidPoint = (int)(bounds.getY() + (bounds.getHeight() / 2));

        boolean topQuad = (rec.getY() > horizontalMidPoint); //object completely fits top quadrant
        boolean bottomQuad = (rec.getY() + rec.getHeight() < horizontalMidPoint); //object completely fits bottom quadrant
        //object fits in left quad
        if(rec.getX() + rec.getWidth() < verticalMidPoint){
            if(topQuad) index = 1;
            else if(bottomQuad) index = 2;
        } else if(rec.getX() > verticalMidPoint){ //object fits right quad
            if(topQuad) index = 0;
            else if(bottomQuad) index = 3;
        }

        return index;
    }

    /** Inserts a rectangle all the way down to the lowest level leaf*/
    private void insert(Rectangle rec){
        if(leafs[0] != null){
            int index = getIndex(rec);
            if(index != -1){
                leafs[index].insert(rec);

                return;
            }
        }
        objects.add(rec);

        if(objects.size() > MAX_OBJECTS && level < MAX_LEVELS){
            if(leafs[0] == null) split();

            int i = 0;
            while(i < objects.size()){
                int index = getIndex(objects.get(i));
                if(index != -1){
                    leafs[index].insert(objects.remove(i));
                }
                else i++;
            }
        }
    }

    public void insertAll(ArrayList<Rectangle> recs){
        for(Rectangle r : recs){
            insert(r);
        }
    }

    public void clear(){
        objects.clear();
        for(int i = 0; i < leafs.length; i++){
            leafs[i] = null;
        }
    }

    /** Returns the rectangles that are in the same leaves as the given rectangle*/
    public ArrayList<Rectangle> retrieve(ArrayList<Rectangle> returnList, Rectangle r){
        int index = getIndex(r);
        if(index != -1 && leafs[0] != null) leafs[index].retrieve(returnList, r);

        returnList.addAll(objects);

        return returnList;
    }

    public void printLeafNodes(){
        if(leafs[0] != null){
            for(int i = 0; i < leafs.length; i++){
                leafs[i].printLeafNodes();
            }
        }

        System.out.println("Level: " + level + " [" + objects.size() + "]");
    }


    //******************************************************************************************************************
    //      Test Case
    //******************************************************************************************************************

    public static void main(String[] args){
        Rectangle baseRectangle = new Rectangle(0,0,500, 500);
        QuadTree tree = new QuadTree(0, baseRectangle);

        //make up some rectangles to go in tree
        ArrayList<Rectangle> tempRecList = new ArrayList<>();
        Random r = new Random(123456);
        int numOfRecs = 320;

        int minWidth = 10;
        int maxWidth = 40;
        int minHeight = 5;
        int maxHeight = 20;

        for(int i = 0; i < numOfRecs; i++){
            int x = r.nextInt((int)(baseRectangle.getWidth() - maxWidth));
            int y = r.nextInt((int)(baseRectangle.getHeight() - maxHeight));
            int w =  minWidth + r.nextInt(maxWidth - minWidth);
            int h =  minHeight + r.nextInt(maxHeight - minHeight);
            tempRecList.add(new Rectangle(x, y, w, h));
        }

        tree.insertAll(tempRecList);
        tree.printLeafNodes();
    }
}

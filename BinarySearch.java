/*
    FILE BinarySearch.java
    AUTH Timothy
    DATE 4/29/2017
    DESC demonstration of a simple binary search.
 */
import java.util.Arrays;
import java.util.Random;

public class BinarySearch {

    public static boolean binarySearch(int[] list, int target){
        int low = 0;
        int high = list.length - 1;

        while(low < high){
            int middle = (high + low) / 2;

            if(list[middle] == target)return true;

            if(list[middle] < target){
                low = middle + 1;
            }

            if(list[middle] > target){
                high = middle - 1;
            }
        }
        return false;
    }

    public static void main(String[] args) {
	    Random r = new Random();

        int target = r.nextInt(100);

        //create list
        int[] list = new int[100];
        for(int i = 0; i < list.length; i++){
            list[i] = 1 + r.nextInt(100);
        }

        //sort it
        Arrays.sort(list);

        //show list
        for(int j = 0; j < list.length; j++){
            System.out.print(list[j] + ", ");
            if(j > 1 && j % 10 == 0) System.out.print("\n");
        }

        System.out.println("\nList contains(" + target + "): " + binarySearch(list, target));
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Row{
    public List<Integer> items;
    public int size;

    /* Constructor */
    Row (Integer[] arr){
        items = Arrays.asList(arr);
        size = arr.length;
    }

    Row (int num) {
        items = new ArrayList<>();
        size = 0;
    }

    public void add(int i){
        items.add(i);
        size++;
    }

    public void addAll(List<Integer> rowItems){
        items.addAll(rowItems);
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Column {
    public String name;
    public List<Integer> items;
    public int size;

    public Column(String n, int num) {
        name = n;
        items = new ArrayList<>();
        size = 0;
    }

    public Column(String n, Integer[] ints) {
        name = n;
        items = new ArrayList<>();
        items.addAll(Arrays.asList(ints));
        size = items.size();
    }

    public void add(int item){
        items.add(item);
        size ++;
    }

}

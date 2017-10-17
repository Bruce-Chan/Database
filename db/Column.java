package db;

import java.util.ArrayList;
import java.util.List;

public class Column <T extends Comparable>{
    public String name;
    public Class type;
    public List<T> items;
    public int size;


    /* Constructor */
    Column(String n, Class ty) {
        name = n;
        items = new ArrayList<>();
        size = 0;
        type = ty;
    }

    void add(T item){
        items.add(item);
        size ++;
    }

    void remove(int i){
        items.remove(i);
        size --;
    }

    static Column plus(Column c1, Column c2, String name) throws Exception{
        Class t1 = c1.type;
        Class t2 = c2.type;
        Column newCol;
        if(t1.equals(String.class) && t2.equals(String.class)) {
            newCol = new Column(name,c1.type);
        } else if(t1.equals(Integer.class) && t2.equals(Integer.class)) {
            newCol = new Column(name, c1.type);
        } else {
            newCol = new Column(name,Float.class);
        }

        for(int i = 0; i < c1.size; i++){
            if(t1.equals(String.class) && t2.equals(String.class)){ //c1 and c2 are both String
                String str1 = (String)c1.items.get(i);
                String str2 = (String)c2.items.get(i);
                String str = str1 + str2;
                newCol.add(str);
            } else if(t1.equals(String.class) ){ //only c1 String
                throw new Exception("only one class is string");
            } else if(t1.equals(String.class) ){ // only c2 String
                throw new Exception("only one class is string");
            } else if(t1.equals(Integer.class) && t2.equals(Integer.class) ){
                // c1 and c2 are both integer
                Integer int1 = (Integer)c1.items.get(i);
                Integer int2 = (Integer)c2.items.get(i);
                Integer newInt = int1 + int2;
                newCol.add(newInt);
            }else {
                float f1 = (float) c1.items.get(i);
                float f2 = (float) c2.items.get(i);
                float f = f1 + f2;
                newCol.add(f);
            }
        }

        return newCol;
    }

    static Column typeCheck(Column c1, Column c2, String name){
        Class t1 = c1.type;
        Class t2 = c2.type;
        Column newCol;
        if(t1.equals(Integer.class) && t2.equals(Integer.class)) {
            newCol = new Column(name, c1.type);
        } else {
            newCol = new Column(name,Float.class);
        }
        return newCol;
    }

    static Column sub(Column c1, Column c2, String name) {
        Column newCol = typeCheck(c1, c2, name);
        for (int i = 0; i < c1.size; i++) {
            if (newCol.type.equals(Integer.class)) {
                // c1 and c2 are both integer
                Integer int1 = (Integer) c1.items.get(i);
                Integer int2 = (Integer) c2.items.get(i);
                Integer newInt = int1 - int2;
                newCol.add(newInt);
            } else {
                float f1 = (float) c1.items.get(i);
                float f2 = (float) c2.items.get(i);
                float f = f1 - f2;
                newCol.add(f);
            }
        }
        return newCol;
    }

    static Column multipy(Column c1, Column c2, String name) {
        Column newCol = typeCheck(c1, c2, name);
        for (int i = 0; i < c1.size; i++) {
            if (newCol.type.equals(Integer.class)) {
                // c1 and c2 are both integer
                Integer int1 = (Integer) c1.items.get(i);
                Integer int2 = (Integer) c2.items.get(i);
                Integer newInt = int1 * int2;
                newCol.add(newInt);
            } else {
                float f1 = (float) c1.items.get(i);
                float f2 = (float) c2.items.get(i);
                float f = f1 * f2;
                newCol.add(f);
            }
        }
        return newCol;
    }

    static Column div(Column c1, Column c2, String name) {
        Column newCol = typeCheck(c1, c2, name);
        for (int i = 0; i < c1.size; i++) {
            if (newCol.type.equals(Integer.class)) {
                // c1 and c2 are both integer
                Integer int1 = (Integer) c1.items.get(i);
                Integer int2 = (Integer) c2.items.get(i);
                Integer newInt = int1 / int2;
                newCol.add(newInt);
            } else {
                float f1 = (float) c1.items.get(i);
                float f2 = (float) c2.items.get(i);
                float f = f1 / f2;
                newCol.add(f);
            }
        }
        return newCol;
    }
}

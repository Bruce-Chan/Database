import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class TestJoin {

    public void tableCompare(Table tExcept, Table tActual){
        Assert.assertEquals(tExcept.columnsNum,tActual.columnsNum);
        Assert.assertEquals(tExcept.rowsNum,tActual.rowsNum);

        for(int cIndex = 0; cIndex < tExcept.columnsNum; cIndex++){
            for(int rIndex = 0; rIndex < tExcept.rowsNum; rIndex++){
                int except = tExcept.rows.get(rIndex).items.get(cIndex);
                int actual = tActual.rows.get(rIndex).items.get(cIndex);
                Assert.assertEquals(except,actual);
            }
        }
    }

    @Test
    public void jointTest() {
        /*  Fish Dogs Cats
             1    1    3
             2    1    4
             2    3    5
         */
        Column c0 = new Column("Fish",new Integer[]{1,2,2});
        Column c1 = new Column("Dogs",new Integer[]{1,1,3});
        Column c2 = new Column("Cats",new Integer[]{3,4,5});
        List<Column> cols1 = new ArrayList<>();
        cols1.add(c0);
        cols1.add(c1);
        cols1.add(c2);
        Table t1 = new Table(cols1);

        /*  Fish Dogs Pigs
             2    2    30
             2    3    40
            13    4    50
         */

        Column c5 = new Column("Fish",new Integer[]{2,2,13});
        Column cAct = new Column("Fish",new Integer[]{2,2,13});
        Column c3 = new Column("Dogs",new Integer[]{2,3,4});
        Column c4 = new Column("Pigs",new Integer[]{30,40,50});
        List<Column> cols2 = new ArrayList<>();
        cols2.add(c3);
        cols2.add(c4);
        cols2.add(c5);
        Table t2 = new Table(cols2);

        /*  Fish Dogs Cats Pigs
             2    3    5   30
         */

        c0 = new Column("Fish",new Integer[]{2});
        c1 = new Column("Dogs",new Integer[]{3});
        c2 = new Column("Cats",new Integer[]{5});
        c4 = new Column("Pigs",new Integer[]{40});
        List<Column> cols3 = new ArrayList<>();
        cols3.add(c0);
        cols3.add(c1);
        cols3.add(c2);
        cols3.add(c4);
        Table t3 = new Table(cols3);
        Table tActual = Table.join(t1,t2);
        tableCompare(t3,tActual);


        Integer[] arr = {3, 4, 7, 80};
        Row r1 = new Row(arr);

        t3.addRow(r1);
        tActual.addRow(r1);
        tableCompare(t3,tActual);

    }

}

package db;

import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestUnit{

    public void tableCompare(Table tExcept, Table tActual){
        Assert.assertEquals(tExcept.columnsNum(),tActual.columnsNum());
        Assert.assertEquals(tExcept.rowsNum(),tActual.rowsNum());

        for(int cIndex = 0; cIndex < tExcept.columnsNum(); cIndex++){
            for(int rIndex = 0; rIndex < tExcept.rowsNum(); rIndex++){
                Object except = tExcept.rows.get(rIndex).items.get(cIndex);
                Object actual = tActual.rows.get(rIndex).items.get(cIndex);
                Assert.assertEquals(except,actual);
            }
        }
    }

    @Test
    public void testJoin1(){
        Column<Integer> c1 = new Column<>("x", int.class);
        c1.add(2);
        c1.add(8);
        c1.add(13);

        Column<Integer> c2 = new Column<>("y", int.class);
        c2.add(5);
        c2.add(3);
        c2.add(7);

        List<Column> cols1 = new ArrayList<>();
        cols1.add(c1);
        cols1.add(c2);
        Table t1 = new Table("t1",cols1);

        Column<Integer> c3 = new Column<>("x", int.class);
        c3.add(2);
        c3.add(8);
        c3.add(10);

        Column<Integer> c4 = new Column<>("z", int.class);
        c4.add(4);
        c4.add(9);
        c4.add(1);

        List<Column> cols2 = new ArrayList<>();
        cols2.add(c3);
        cols2.add(c4);
        Table t2 = new Table("t2",cols2);

        Table t3 = Table.join(t1,t2, "t3");

        Column<Integer> c5 = new Column<>("x", int.class);
        c5.add(2);
        c5.add(8);

        Column<Integer> c6 = new Column<>("y", int.class);
        c6.add(5);
        c6.add(3);

        Column<Integer> c7 = new Column<>("z", int.class);
        c7.add(4);
        c7.add(9);

        List<Column> cols3 = new ArrayList<>();
        cols3.add(c5);
        cols3.add(c6);
        cols3.add(c7);

        Table t3Except = new Table("t3 except",cols3);

        tableCompare(t3Except,t3);
    }

    @Test
    public void TestEval(){
        String line =  "create table team (Name string, Wins int)";
        Database db = new Database();
        String result = db.transact(line);
        String except = "Name string,Wins int";
        Assert.assertEquals(except,result);
    }

}

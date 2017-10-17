package db;

import java.util.ArrayList;
import java.util.List;

import static db.TableHelper.*;
public class Table {


    public String name;
    public List<String> columnNames; // the names of columns
    public List<Column> columns; // all column
    public List<Row> rows; // all rows

    public int columnsNum(){
        return columns.size();
    }

    public int rowsNum(){
        return rows.size();
    }

    /*
     * Constructor
     */
    public Table(String tableName, List<Column> cols){
        name = tableName;
        columns = cols;
        columnNames = new ArrayList<>();
        rows = new ArrayList<>();

        for (int i = 0; i < cols.get(0).size; i+=1) { // create new rows
            rows.add(new Row());
        }

        for (int i = 0; i < columnsNum(); i+=1) {
            Column col = cols.get(i);
            columnNames.add(col.name); // insert column name into columnNames
            expandItem(rows,col);
        }
    }

    public void addRow(Row r) throws Exception{
        rows.add(r);
        expandItem(columns, r);
    }

    private String changeLine(String str){
        str = str.substring(0,str.length() -1);
        str+="\n";
        return str;
    }

    @Override
    public String toString() {
        String result = "";
        for(int i=0; i<columnsNum(); i++){
            String colName = columnNames.get(i);
            Class clazz = columns.get(i).type;
            if(clazz==Integer.class){
                clazz = int.class;
            }
            result+=(colName+" "+clazz.getSimpleName().toLowerCase()+",");
        }
        result = changeLine(result);
        for(Row r : rows){
            for(Object item : r.items){
                result+=(item.toString() + ",");
            }
            result = changeLine(result);
        }
        result = result.substring(0,result.length() -1);
        return result;
    }


    public static Table join(Table t1, Table t2, String newName){

        List<Integer> t1SharedIndex = new ArrayList<>();
        List<Integer> t1UnsharedIndex = new ArrayList<>();
        List<Integer> t2UnsharedIndex = new ArrayList<>();
        List<String> newColsName = new ArrayList<>();

        for(int i=0; i<t1.columnNames.size(); i++){
            String name = t1.columnNames.get(i);
            if(t2.columnNames.contains(name)){ // name exists in T1,T2
                t1SharedIndex.add(i);
                newColsName.add(name);
            }
        }
        for(int i=0; i<t1.columnNames.size(); i++){
            String name = t1.columnNames.get(i);
            if(newColsName.contains(name)==false){ // name only exist in T1
                t1UnsharedIndex.add(i);
                newColsName.add(name);
            }
        }
        for(int i=0; i<t2.columnNames.size(); i++){
            String name = t2.columnNames.get(i);
            if(newColsName.contains(name)==false){ // name only exist in T2
                t2UnsharedIndex.add(i);
                newColsName.add(name);
            }
        }
        SameNamePairs SNpairs = new SameNamePairs(t1,t2);
        int repeatedNameNum = SNpairs.RepeatedNum;
        List<Row> newRows = new ArrayList<>();
        for(int t1RowIndex = 0; t1RowIndex< t1.rowsNum(); t1RowIndex+=1){
            for(int t2RowIndex = 0; t2RowIndex< t2.rowsNum(); t2RowIndex+=1){
                int matchNum = 0;
                for(ColumnPair cp: SNpairs.repeatedNameColPair){
                    if(cp.col1.items.get(t1RowIndex).equals(cp.col2.items.get(t2RowIndex))){
                        //should be equals not '==' here
                        matchNum++;
                    }
                }
                if(matchNum == repeatedNameNum){ // all correspond elements are same in both two targeted column
                    Row curr = new Row(); // create a new Row
                    Row t1Row = t1.rows.get(t1RowIndex);
                    Row t2Row = t2.rows.get(t2RowIndex);
                    curr.add(t1Row, t1SharedIndex); // All shared columns come first
                    curr.add(t1Row, t1UnsharedIndex); // The unshared columns from the left table come next
                    curr.add(t2Row, t2UnsharedIndex); // The unshared columns from the right table come last

                    newRows.add(curr);
                }
            }
        }
        if(newRows.size()==0){
            return null;
        }
        List<Column> newCols = rowsToColumns(newRows,newColsName);
        if(newCols.size()==0){
            return null;
        }
        Table result = new Table(newName,newCols);
        return result;
    }
}

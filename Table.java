
import java.util.ArrayList;
import java.util.List;

public class Table {

    private static class ColumnPair{
        private String nameOfPair;
        private Column col1;
        private Column col2;

        ColumnPair(Column c1, Column c2){
            col1 = c1;
            col2 = c2;
            nameOfPair = c1.name;
        }
    }

    private static class SameNamePairs{
        private int RepeatedNum = 0;
        private List<ColumnPair> repeatedNameColPair;

        SameNamePairs(Table t1, Table t2){
            repeatedNameColPair = new ArrayList<>();
            List<String> t1Names = t1.columnNames;
            List<String> t2Names = t2.columnNames;
            List<Column> cols1 = t1.columns;
            List<Column> cols2 = t2.columns;
            for(int i1 =0; i1 < t1.columnsNum; i1+=1){
                for(int i2 =0; i2 < t2.columnsNum; i2+=1){
                    if(cols1.get(i1).name==cols2.get(i2).name){
                        ColumnPair colPair = new ColumnPair(cols1.get(i1),cols2.get(i2));
                        repeatedNameColPair.add(colPair);
                        RepeatedNum++;
                    }
                }
            }
        }
    }

    public List<String> columnNames;
    public List<Column> columns;
    public List<Row> rows;
    public int columnsNum;
    public int rowsNum;

    private static List<Column> rowsToColumns(List<Row> rows, List<String> colNames){
        List<Column> cols = new ArrayList<>();
        for(int colIndex = 0; colIndex < colNames.size(); colIndex++){
            Column currCol = new Column(colNames.get(colIndex),rows.size());
            for(Row r: rows){
                currCol.add(r.items.get(colIndex));
            }
            cols.add(currCol);
        }
        return cols;
    }

    public Table(List<Column> cols){
        columns = cols;
        columnsNum = cols.size();
        rowsNum = cols.get(0).items.size();
        columnNames = new ArrayList<>();
        rows = new ArrayList<>();

        for (int i = 0; i < rowsNum; i+=1) {
            rows.add(new Row(columnsNum));
        }

        for (int i = 0; i < columnsNum; i+=1) {
            Column col = cols.get(i);
            columnNames.add(col.name);
            for (int j = 0; j < rowsNum; j += 1){
                rows.get(j).add(col.items.get(j));
            }
        }
    }

    public void addRow(Row r) {
        rows.add(r);
        rowsNum++;
        for(int i = 0; i < columnsNum; i++){
            columns.get(i).add(r.items.get(i));
        }
    }

    public static Table join(Table t1, Table t2){

        List<Integer> unrepeatedNameIndexInT2 = new ArrayList<>();
        List<String> newColsName = new ArrayList<>();
        newColsName.addAll(t1.columnNames);
        List<String> t2ColsName = t2.columnNames;
        for(int i = 0; i < t2ColsName.size(); i+=1){
            if(t1.columnNames.contains(t2ColsName.get(i))==false){
                newColsName.add(t2ColsName.get(i));
                unrepeatedNameIndexInT2.add(i);
            }
        }
        SameNamePairs SNpairs = new SameNamePairs(t1,t2);
        int repeatedNameNum = SNpairs.RepeatedNum;
        List<Row> newRows = new ArrayList<>();
        for(int t1RowIndex = 0; t1RowIndex< t1.rowsNum; t1RowIndex+=1){
            for(int t2RowIndex = 0; t2RowIndex< t2.rowsNum; t2RowIndex+=1){
                int matchNum = 0;
                for(ColumnPair cp: SNpairs.repeatedNameColPair){
                    if(cp.col1.items.get(t1RowIndex)==cp.col2.items.get(t2RowIndex)){
                        matchNum++;
                    }
                }
                if(matchNum == repeatedNameNum){ // all correspond elements are same in both two targeted column
                    Row curr = new Row(newColsName.size()); // create a new Row
                    curr.addAll(t1.rows.get(t1RowIndex).items); //inserts items from table 1 matched Row to new row
                    for(int index: unrepeatedNameIndexInT2){
                        curr.add(t2.rows.get(t2RowIndex).items.get(index)); // insert items from table 2 matched Row
                    }
                    newRows.add(curr);
                }
            }
        }

        List<Column> newCols = rowsToColumns(newRows,newColsName);
        if(newCols.size()==0){
            return null;
        }
        Table result = new Table(newCols);
        return result;
    }

}

/*
 * TODO: 1. fix the join (Done)
 *       2. Condition (Done)
 *       3. Operator
 *       4. Check select and Create select table
 *       5. repeated column name and column name to column (map)
 */

package db;


import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

import static db.ConditionSelect.conditionEval;
import static db.ConditionSelect.operationEval;
import static db.Parse.eval;


public class Database {
    private static String DATAADDRESS = "out/production/proj2/examples//";
    private static final String REST  = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND   = "\\s+and\\s+";

    private static final String INTEGER = "\\s*(\\d+)\\s*";
    private static final String STRING = "\'.*\'";
    private static final String FLOAT = "^\\s*([+-]?\\d*\\.\\d*)\\s*$";
    private static final Pattern INTEGER_TYPE = Pattern.compile(INTEGER);
    private static final Pattern STRING_TYPE = Pattern.compile(STRING);
    private static final Pattern FLOAT_TYPE = Pattern.compile(FLOAT);

    private static Map<String,Table> tableMap;

    public Database() {
        tableMap = new HashMap<>();
    }

    public String transact(String query) {
        String result = eval(query);
        return result;
    }

    static Table createNewTable(String name, String[] colsString) throws Exception {
        List<Column> cols = new ArrayList<>();
        for(String str : colsString){
            String[] args = str.split("\\s+");
            Column col;
            int argsLen = args.length;
            if (argsLen!=2){
                throw new Exception(String.format(
                        "Wrong arguments number for %s, except 2, get %d",args[0],argsLen));
            } else{
                String colName = args[0];
                String colType = args[1];
                if(colType.equals("integer")||colType.equals("int")){
                    col = new Column<Integer>(colName,Integer.class);
                } else if(colType.equals("string")){
                    col = new Column<String>(colName, String.class);
                } else if(colType.equals("float")){
                    col = new Column<Float>(colName, Float.class);
                } else {
                    throw new Exception(String.format(String.format("unknown class type: %s", colType)));
                }
            }
            cols.add(col);
        }
        Table newTable = new Table(name, cols);
        tableMap.put(name,newTable);
        return newTable;
    }
    /*
     * convert string to correspond type object
     */
    static Comparable strToObj(String str){
        Matcher m;
        if((m=INTEGER_TYPE.matcher(str)).matches()){
            return Integer.parseInt(m.group(1));
        } else if((m=FLOAT_TYPE.matcher(str)).matches()){
            return Float.parseFloat(m.group(1));
        } else{
            return str;
        }
    }
    /*
     * convert row line string to row
     */
    private static Row rowStrToRow(String line){
        Row r = new Row();
        String[] rowItemsStr = line.split(",");
        for(String itemStr : rowItemsStr){
            r.add(strToObj(itemStr));
        }
        return r;
    }

    static String loadTable(String name) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(DATAADDRESS + name + ".tbl"));
            String line;
            line = br.readLine();
            String[] colsStr = line.split(","); // get column name and number from first line
            Table newTable;
            try {
                newTable = createNewTable(name, colsStr); // read first line and create table
            } catch (Exception e) {
                return "" + e;
            }
            while ((line = br.readLine()) != null) {
                Row r = rowStrToRow(line);
                try {
                    newTable.addRow(r); // add rows
                } catch (Exception e) {
                    return ""+e;
                }
            }
            return "";
        } catch (IOException e) {
            return String.format("ERROR: TBL file not found: %s.tbl", name);
        }
    }

    static String storeTable(String name) {
        try{
            PrintWriter pw = new PrintWriter(DATAADDRESS+name+".tbl","UTF-8");
            if(tableMap.containsKey(name)) {
                String[] lines = tableMap.get(name).toString().split("\n");
                for(String line : lines){
                    pw.println(line);
                }
                pw.close();
                return "";
            } else{
                return "ERROR: No such table: "+name;
            }
        } catch (IOException e){
            return ""+e;
        }
    }

    static String dropTable(String name) {
        if(tableMap.containsKey(name)) {
            tableMap.remove(name);
            return "";
        } else {
            return "ERROR: Table not found: "+name;
        }
    }

    static String insertRow(String name, String line) {
        if(tableMap.containsKey(name)) {
            Row r = rowStrToRow(line);
            Table t = tableMap.get(name);
            if(t.columnsNum()==r.size){
                try{
                    t.addRow(r);
                } catch (Exception e){
                    return ""+e;
                }
                return "";
            }
            return "ERROE: the provided values doesn't match the column of the table: "+name;
        } else {
            return "ERROR: Table not found: "+name;
        }

    }

    static String printTable(String name){
        if(tableMap.containsKey(name)){
            return tableMap.get(name).toString();
        } else {
            return "ERROR: Table not found: "+name;
        }
    }

    static Table select(String name, String exprs, String tableNames, String condStrs) throws Exception {
        String[] colStrs = exprs.split(COMMA);
        String[] tables = tableNames.split(COMMA);
        String[] conds;
        if(condStrs==null){
            conds = null;
        } else {
            conds = condStrs.split(AND);
        }

        List<Table> tbs = new ArrayList<>();
        for(String tbStr : tables){
            if(tableMap.containsKey(tbStr)){
                tbs.add(tableMap.get(tbStr));
            } else {
                throw new Exception(String.format("Error: table %s doesn't exist",tbStr));
            }
        }
        Table newTable;
        if(tbs.size()>=1){
            newTable = tbs.remove(0); // get the first table
        } else {
            throw new Exception("Error: didn't select table");
        }
        for(Table tb : tbs){ // rest tables of the list
            newTable = Table.join(newTable,tb,"__");  // create a new table joined by a list of table
        }

        if (newTable == null){
            throw new Exception("they do not have any match item in same name columns");
        }

        List<Column> cols = new ArrayList<>();

        cols = operationEval(newTable.columns,colStrs,newTable.columnNames);

        cols = conditionEval(cols,conds);
        Table newTB = new Table(name,cols);

        if(name.equals("__")==false){
            tableMap.put(name,newTB);
        }
        return newTB;
    }

}


package db;

import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import db.Database;

public class Parse {
    private static final String REST  = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND   = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD   = Pattern.compile("load " + REST),
            STORE_CMD  = Pattern.compile("store " + REST),
            DROP_CMD   = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD  = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\(\\s*(\\S+\\s+\\S+\\s*" +
            "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                    "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                    "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" +
                    "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
            CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+" +
                    SELECT_CLS.pattern()),
            INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                    "\\s*(?:,\\s*.+?\\s*)*)");

    static String eval(String query) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            return createTable(m.group(1));
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            return loadTable(m.group(1));
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            return storeTable(m.group(1));
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            return dropTable(m.group(1));
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            return insertRow(m.group(1));
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            return printTable(m.group(1));
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            return select(m.group(1));
        } else {
            return String.format("Malformed query: %s\n", query);
        }
    }

    private static String createTable(String expr) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            return createNewTable(m.group(1), m.group(2).split(COMMA));
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            return createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            return String.format("Malformed create: %s\n", expr);
        }
    }

    private static String createNewTable(String name, String[] cols) {
        String output;
        try {
            output = Database.createNewTable(name, cols).toString();
        } catch (Exception e) {
            output = "" + e;
        }
        return output;
    }


    private static String createSelectedTable(String name, String exprs, String tables, String conds) {
        Table newTable;
        try {
            newTable = Database.select(name, exprs, tables, conds);
        } catch(Exception e){
            return e+"";
        }

        return "";
    }

    private static String loadTable(String name) {
        String output = Database.loadTable(name);
        if(output.equals("")){
            return "";
        } else {
            return output;
        }
    }

    private static String storeTable(String name) {
        return Database.storeTable(name);
    }

    private static String printTable(String name) {
        return Database.printTable(name);
    }

    private static String insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            return String.format("Malformed insert: %s\n", expr);
        } else {
            return Database.insertRow( m.group(1), m.group(2));
        }

        //"trying to insert the row \"%s\" into the table %s\n", m.group(2), m.group(1));
    }
    private static String dropTable(String name) {
        return Database.dropTable(name);
    }

    private static String select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            return String.format("Malformed select: %s\n", expr);
        }
        Table newTable;
        try {
            newTable = Database.select("__", m.group(1), m.group(2), m.group(3));
        } catch(Exception e){
            return e+"";
        }
        return newTable.toString();
    }
}

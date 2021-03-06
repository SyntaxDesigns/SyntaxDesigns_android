package edu.uni.cs.syntaxdesigns.table;

import android.provider.BaseColumns;
import edu.uni.cs.syntaxdesigns.table.column.ColumnType;
import edu.uni.cs.syntaxdesigns.table.column.TableColumn;

public final class RecipeTable extends DatabaseTable {

    public static final String TABLE_NAME = "recipes";

    public static final class Columns implements BaseColumns {
        public static final TableColumn COLUMN_NAME = new TableColumn("name", ColumnType.TEXT, true);
        public static final TableColumn COLUMN_YUMMLY_URL = new TableColumn("yummlyUrl", ColumnType.TEXT);
        public static final TableColumn COLUMN_FAVORITE = new TableColumn("favorite", ColumnType.INTEGER, false, "0");
        public static final TableColumn COLUMN_IS_ENABLED_IN_GROCERY_LIST = new TableColumn("isEnabledInGroceryList", ColumnType.INTEGER, false, "1");
    }

    public static final String getCreateQuery() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
               Columns._ID + " INTEGER PRIMARY KEY," +
               Columns.COLUMN_NAME + "," +
               Columns.COLUMN_YUMMLY_URL + "," +
               Columns.COLUMN_FAVORITE + "," +
               Columns.COLUMN_IS_ENABLED_IN_GROCERY_LIST + ")";
    }

    public static final String getDeleteQuery() {
        return "DELETE TABLE IF EXISTS " + TABLE_NAME;
    }

}

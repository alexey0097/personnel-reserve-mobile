package ru.iworking.personnel.reserve.mobile.util;

public class SqlProperties {

    public static final Integer DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "personnel_reserve_mobile";

    private static final String CREATE_TABLE_TEMPLATE = "CREATE TABLE IF NOT EXISTS %s (%s)";
    private static final String DROP_TABLE_TEMPLATE = "DROP TABLE IF EXISTS %s";

    public static class Resume {

        public static final String TABLE_NAME = "RESUME";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_LAST_NAME = "LAST_NAME";
        public static final String COLUMN_FIRST_NAME = "FIRST_NAME";
        public static final String COLUMN_MIDDLE_NAME = "MIDDLE_NAME";
        public static final String COLUMN_PROFESSION = "PROFESSION";
        public static final String COLUMN_ABOUT_ME = "ABOUT_ME";
        public static final String COLUMN_AVATAR = "AVATAR";

        private static final String CREATE_TABLE_COLUMNS_TEMPLATE =
                String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s BLOB",
                        COLUMN_ID, COLUMN_LAST_NAME, COLUMN_FIRST_NAME, COLUMN_MIDDLE_NAME, COLUMN_PROFESSION, COLUMN_ABOUT_ME, COLUMN_AVATAR);

        public static final String SELECT_TABLE = String.format("SELECT * FROM %s", TABLE_NAME);
        public static final String SELECT_TABLE_WHERE_TEMPLATE = String.format("%s WHERE", SELECT_TABLE);
        public static final String SELECT_TABLE_WHERE_ID = String.format("%s AS obj WHERE obj.ID = ?", SELECT_TABLE);

        public static final String DELETE_FROM_TABLE = String.format("DELETE FROM %s", TABLE_NAME);
        public static final String DELETE_FROM_TABLE_WHERE_TEMPLATE = String.format("%s WHERE", DELETE_FROM_TABLE);
        public static final String DELETE_FROM_TABLE_WHERE_ID = String.format("%s WHERE ID = ?", DELETE_FROM_TABLE);

        public static final String CREATE_TABLE = String.format(CREATE_TABLE_TEMPLATE, TABLE_NAME, CREATE_TABLE_COLUMNS_TEMPLATE);
        public static final String DROP_TABLE = String.format(DROP_TABLE_TEMPLATE, TABLE_NAME);
    }

}

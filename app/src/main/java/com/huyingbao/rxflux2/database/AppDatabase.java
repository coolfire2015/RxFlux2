package com.huyingbao.rxflux2.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * DBFlow数据库
 * Created by liujunfeng on 2016/9/29.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "HybDm"; // we will add the .db extension

    public static final int VERSION = 5;

//    /**
//     * 版本4,向表MessageItem中增加toVoipAccount
//     */
//    @Migration(version = 4, database = AppDatabase.class)
//    public static class AddToVoipAccountToMessageItemMigration extends AlterTableMigration<Comment> {
//        public AddToVoipAccountToMessageItemMigration(Class<Comment> table) {
//            super(table);
//        }
//
//        @Override
//        public void onPreMigrate() {
//            addColumn(SQLiteType.TEXT, "toVoipAccount");
//        }
//    }
//
//    /**
//     * 版本5,向表MessageItem中增加fromVoipAccount
//     */
//    @Migration(version = 5, database = AppDatabase.class)
//    public static class AddFromVoipAccountToMessageItemMigration extends AlterTableMigration<Comment> {
//        public AddFromVoipAccountToMessageItemMigration(Class<Comment> table) {
//            super(table);
//        }
//
//        @Override
//        public void onPreMigrate() {
//            addColumn(SQLiteType.TEXT, "fromVoipAccount");
//        }
//    }
}
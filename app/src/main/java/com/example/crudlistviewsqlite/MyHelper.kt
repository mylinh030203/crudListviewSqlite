package com.example.crudlistviewsqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyHelper(context:Context) :SQLiteOpenHelper(context,"TUHOCDB",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        //tạo tb
        db?.execSQL("Create table tuhoc1 (_id integer primary key autoincrement, user TEXT, email TEXT)")
        // thêm data vào csdl
        db?.execSQL("insert into tuhoc1(user,email) values('mot','mot@gmail.com')")
        db?.execSQL("insert into tuhoc1(user,email) values('hai','hai@gmail.com')")
        db?.execSQL("insert into tuhoc1(user,email) values('ba','ba@gmail.com')")


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}
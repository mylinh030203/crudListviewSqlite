package com.example.crudlistviewsqlite

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.contentValuesOf
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
   lateinit var db:SQLiteDatabase
   lateinit var rs:Cursor
   lateinit var adapter: SimpleCursorAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var helper = MyHelper(applicationContext)
        db = helper.readableDatabase
        rs = db.rawQuery("select * from tuhoc1 limit 20",null)
        //sql:String chuỗi câu lệnh sql
        //section: chọn lọc dữ liệu với điều kiện mảng
        //đưa dữ liệu dòng đầu lên edtuset và edtemail
//        if(rs.moveToFirst()){//row đầu tiên
//            edtUsers.setText(rs.getString(1))
//            edtEmail.setText(rs.getString(2))
//        }
        click()
        clickCRUD()
    }

    fun click(){
        btnFirst.setOnClickListener {
            if(rs.moveToFirst()){//row đầu tiên
                edtUsers.setText(rs.getString(1))
                edtEmail.setText(rs.getString(2))
            }else{
                Toast.makeText(applicationContext, "no data found", Toast.LENGTH_SHORT).show()
            }
        }
        btnNext.setOnClickListener {
            if(rs.moveToNext()){
                edtUsers.setText(rs.getString(1))
                edtEmail.setText(rs.getString(2))
            }else if(rs.moveToFirst()){//để nó quay trở về first
                edtUsers.setText(rs.getString(1))
                edtEmail.setText(rs.getString(2))
            }
            else{
                Toast.makeText(applicationContext, "no data found", Toast.LENGTH_SHORT).show()
            }
        }

        btnPrev.setOnClickListener {
            if(rs.moveToPrevious()){
                edtUsers.setText(rs.getString(1))
                edtEmail.setText(rs.getString(2))
            }else if(rs.moveToLast()){
                edtUsers.setText(rs.getString(1))
                edtEmail.setText(rs.getString(2))
            }
            else{
                Toast.makeText(applicationContext, "no data found", Toast.LENGTH_SHORT).show()
            }
        }
        btnLast.setOnClickListener {
            if(rs.moveToLast()){
                edtUsers.setText(rs.getString(1))
                edtEmail.setText(rs.getString(2))
            } else{
                Toast.makeText(applicationContext, "no data found", Toast.LENGTH_SHORT).show()
            }
        }

        adapter = SimpleCursorAdapter(applicationContext,
        android.R.layout.simple_list_item_2, rs,
        arrayOf("user","email"), intArrayOf(android.R.id.text1,android.R.id.text2),0
        )
        lvFull.adapter = adapter

        btnViewAll.setOnClickListener {
            searchView.visibility = View.VISIBLE
            lvFull.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
            searchView.queryHint = "Tìm kiếm trong ${rs.count} bản ghi"
        }
        search()

    }
    fun search(){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rs = db.rawQuery("select * from tuhoc1 where user like '%${newText}' or email like '%${newText}' ",null)
                adapter.changeCursor(rs)
                return true
            }
        })
    }

    fun clickCRUD(){
        btnInsert.setOnClickListener {
            var cv = ContentValues()
            cv.put("user",edtUsers.text.toString())
            cv.put("email",edtEmail.text.toString())
            db.insert("tuhoc1",null,cv)
            rs.requery()
            adapter.notifyDataSetChanged()
            var ad = AlertDialog.Builder(this)
            ad.setTitle("Add record")
            ad.setMessage("Add success")
            ad.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                edtUsers.setText("")
                edtEmail.setText("")
                edtUsers.requestFocus()
            }).show()
        }
        btnUpdate.setOnClickListener {
            var cv = ContentValues()
            cv.put("user",edtUsers.text.toString())
            cv.put("email",edtEmail.text.toString())
            db.update("tuhoc1",cv, "_id = ?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()
            var ad = AlertDialog.Builder(this)
            ad.setTitle("Update record")
            ad.setMessage("Update success")
            ad.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                edtUsers.setText("")
                edtEmail.setText("")
                edtUsers.requestFocus()
            }).show()
        }
        btnClear.setOnClickListener {
            edtUsers.setText("")
            edtEmail.setText("")
            edtUsers.requestFocus()
        }
        btnDelete.setOnClickListener {
            db.delete("tuhoc1","_id = ?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()
            var ad = AlertDialog.Builder(this)
            ad.setTitle("Delete record")
            ad.setMessage("Delete success")
            ad.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                if(rs.moveToFirst()){
                edtUsers.setText("")
                edtEmail.setText("")
                edtUsers.requestFocus()
                }
                else{
                    edtUsers.setText("No data found")
                    edtEmail.setText("No data found")
                }
            }).show()
        }
        //contextmenu cho list view
        registerForContextMenu(lvFull)

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(100,11,1,"delete")
        menu?.setHeaderTitle("Removing data")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if(item.itemId == 11){
            db.delete("tuhoc1","_id = ?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()
            var ad = AlertDialog.Builder(this)
            ad.setTitle("Delete record")
            ad.setMessage("Delete success")
            ad.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                if(rs.moveToFirst()){
                    edtUsers.setText("")
                    edtEmail.setText("")
                    edtUsers.requestFocus()
                }
                else{
                    edtUsers.setText("No data found")
                    edtEmail.setText("No data found")
                }
            }).show()
            searchView.queryHint = "Tìm kiếm trong ${rs.count} bản ghi"
        }
        return super.onContextItemSelected(item)
    }





}


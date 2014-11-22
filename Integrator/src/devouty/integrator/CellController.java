package devouty.integrator;


import java.io.ByteArrayOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.SimpleCursorAdapter;

public class CellController {


	
	//database
	static CellDB cellDB;
	SQLiteDatabase cellDbRead;
	static SQLiteDatabase cellDbWrite;
	//adapter
	SimpleCursorAdapter cellDatabaseAdapter = null;
	
	public void onCreate(Activity activity)
	{
        //database
		cellDB = new CellDB(activity);
        cellDbRead =cellDB.getReadableDatabase();
  
        cellDbWrite = cellDB.getWritableDatabase();
        cellDatabaseAdapter = new SimpleCursorAdapter(activity, R.layout.activity_cell_edit, null, new String[]{CellDB.COLUMN_NAME,CellDB.COLUMN_CONTENT}, new int[]{R.id.etName,R.id.etContent});
        
        
        
	}
	@SuppressLint("NewApi") public static void addCell(MainActivity activity, String name, String content)
	{
		if(!(name.isEmpty()))
		{
			ContentValues cv = new ContentValues();
			
			cv.put(CellDB.COLUMN_NAME, name);
		    cv.put(CellDB.COLUMN_CONTENT, content);
		    cv.put(CellDB.COLUMN_PARENTID, -1);
		    cv.put(CellDB.COLUMN_FLAG, 2);
			System.out.println("Add cell successful.");
			cellDbWrite.insert(CellDB.TABLE_NAME, null, cv);
		}	
	}
	@SuppressLint("NewApi") public static void addCell(GroupActivity activity, String name, String content)
	{
		if(!(name.isEmpty()))
		{
			ContentValues cv = new ContentValues();
			cv.put(CellDB.COLUMN_NAME, name);
		    cv.put(CellDB.COLUMN_CONTENT, content);
		    cv.put(CellDB.COLUMN_PARENTID, activity.getId());
		    cv.put(CellDB.COLUMN_FLAG, 2);
			cellDbWrite.insert(CellDB.TABLE_NAME, null, cv);
		    System.out.println("Add cell successful.ParentID:"+activity.getId()+" Count:"+cellDB.getReadableDatabase().query(CellDB.TABLE_NAME, null, null, null, null, null, null).getCount());
		
		}
	}
	public SimpleCursorAdapter getCellDatabaseAdapter()
	{
		return cellDatabaseAdapter;
	}
	public SQLiteDatabase getCellDbRead()
	{
		return cellDbRead;
	}
	public SQLiteDatabase getCellDbWrite()
	{
		return cellDbWrite;
	}
	public void save(String name, String content, int parent_id)
	{
		
	}
}

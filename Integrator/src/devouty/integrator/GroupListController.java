package devouty.integrator;

import java.io.ByteArrayOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.SimpleCursorAdapter;
public class GroupListController {
	
	//database
	static GroupDB groupDB;
	SQLiteDatabase groupDbRead,groupDbWrite;
	//adapter
    private SimpleCursorAdapter groupDatabaseAdapter = null;
    
	@SuppressWarnings("deprecation")
	public void onCreate(Activity activity)
	{
		//database
        groupDB = new GroupDB(activity);
        groupDbRead =groupDB.getReadableDatabase();
        groupDbWrite = groupDB.getWritableDatabase();
        groupDatabaseAdapter = new SimpleCursorAdapter(activity, R.layout.activity_group, null, new String[]{GroupDB.COLUMN_NAME}, new int[]{R.id.list_item});
        
        
        //database
        //Cursor cc = groupDbRead.query(GroupDB.TABLE_NAME, new String[]{""}, selection, selectionArgs, groupBy, having, orderBy)
        Cursor c = groupDbRead.query(GroupDB.TABLE_NAME,null,null,null,null,null,null);
        if(!c.moveToNext())
        {
        	ContentValues cv = new ContentValues();
        	cv.put(GroupDB.COLUMN_GROUP_ID, -1);
	        cv.put(GroupDB.COLUMN_NAME, "");
	        cv.put(GroupDB.COLUMN_PARENTID, -2);
	        cv.put(GroupDB.COLUMN_FLAG, 0);
	        groupDbWrite.insert(GroupDB.TABLE_NAME, null, cv);
        }
	}
	
	

	@SuppressLint("NewApi") public void addGroup(MainActivity activity, String string) {
		if(!(string.isEmpty()))
		{
			
			ContentValues cv = new ContentValues();
			
	        cv.put(GroupDB.COLUMN_NAME, string);
	        cv.put(GroupDB.COLUMN_PARENTID, activity.getId());
	        cv.put(GroupDB.COLUMN_FLAG, 1);
	        //cv.put(GroupDB.COLUMN_GROUP_ID, (int) (Math.random() * 1000000000));
	        groupDbWrite.insert(GroupDB.TABLE_NAME, null, cv);
			System.out.println("Add group successful.");
			
			
		}
	}
	@SuppressLint("NewApi") public void addGroup(GroupActivity activity, String string) {
		if(!(string.isEmpty()))
		{
			
				ContentValues cv = new ContentValues();
				
				cv.put(GroupDB.COLUMN_NAME, string);
		        cv.put(GroupDB.COLUMN_PARENTID, activity.getId());
		        cv.put(GroupDB.COLUMN_FLAG, 1);
		        //cv.put(GroupDB.COLUMN_GROUP_ID, (int) (Math.random() * 1000000000));
		        groupDbWrite.insert(GroupDB.TABLE_NAME, null, cv);
				System.out.println("Add group successful.");
				
		}
	}
	public SimpleCursorAdapter getGroupDatabaseAdapter()
	{
		return groupDatabaseAdapter;
	}
	public SQLiteDatabase getGroupDbRead()
	{
		return groupDbRead;
	}
	public SQLiteDatabase getGroupDbWrite()
	{
		return groupDbWrite;
	}
}

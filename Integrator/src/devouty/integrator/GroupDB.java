package devouty.integrator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GroupDB extends SQLiteOpenHelper {

	 
	
	
	public GroupDB(Context context) {
		super(context, "GroupDB", null, 1);
		}
	
	
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
		db.execSQL("CREATE TABLE "+TABLE_NAME+"("+
				COLUMN_GROUP_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
				COLUMN_PARENTID+" INTEGER NOT NULL DEFAULT \"\"," +
				COLUMN_NAME+" TEXT NOT NULL DEFAULT \"\"," +
				COLUMN_FLAG+" INTEGER NOT NULL DEFAULT \"\"" +
								")"
						);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//通过db.execSQL(SQLCommand)来执行没有返回值的SQL语言，将表格删除
		db.execSQL("DROP TABLE IF EXISTS GroupDB");
		onCreate(db);
	}

	
	//public static final String
	public static final String TABLE_NAME = "group_db";
	public static final String COLUMN_PARENTID = "parent_id";
	public static final String COLUMN_GROUP_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_FLAG = "flag";
	
}

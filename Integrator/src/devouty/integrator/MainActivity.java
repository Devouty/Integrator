package devouty.integrator;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends ListActivity {

	//define
	//button
	//private OnClickListener btnAddGroup_clickHandler,btnAddCell_clickHandler;
	
	//flag
    
    public static final int REQUEST_CODE_ADD_CELL = 2;
    public static final int REQUEST_CODE_EDIT_GROUP = 3;
    public static final int REQUEST_CODE_EDIT_CELL = 4;
    private int flag = 0;
    //dialog
    AlertDialog newGroupDialog;
    //controller
    static GroupListController groupListController;
    static CellController cellController;
    EditText groupName;
    //id
    private int id = -1;
    //adapter
    SimpleCursorAdapter adapter;
    //database
    SQLiteDatabase groupDbRead,cellDbRead;
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id = getIntent().getIntExtra("id", -1);
        
        
        //controller
        groupListController = new GroupListController();
        groupListController.onCreate(this);
        cellController = new CellController();
        cellController.onCreate(this);
        
        //database
        groupDbRead =  groupListController.getGroupDbRead();
        cellDbRead = cellController.getCellDbRead();
        
        //refrashListView();
        //adapter
        adapter = new SimpleCursorAdapter(this, R.layout.list_cell_view, null, new String[]{GroupDB.COLUMN_NAME}, new int[]{R.id.tvName});
        setListAdapter(adapter);
        
        
        //listview
        getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Cursor c = adapter.getCursor();
		    	c.moveToPosition(arg2);
		    	
		    	if(c.getInt(c.getColumnIndex(GroupDB.COLUMN_FLAG)) == 1)
		    	{
			    	Intent i = new Intent(MainActivity.this, GroupActivity.class);
			    	i.putExtra(GroupDB.COLUMN_GROUP_ID, c.getInt(c.getColumnIndex(GroupDB.COLUMN_GROUP_ID)));
			    	i.putExtra(GroupDB.COLUMN_NAME, c.getString(c.getColumnIndex(GroupDB.COLUMN_NAME)));
			    	i.putExtra(GroupDB.COLUMN_FLAG, c.getInt(c.getColumnIndex(GroupDB.COLUMN_FLAG)));
			    	i.putExtra(GroupDB.COLUMN_PARENTID, c.getInt(c.getColumnIndex(GroupDB.COLUMN_PARENTID)));
					
			    	startActivityForResult(i, MainActivity.this.getId());
		    	}
		    	else
		    	{
		    		Intent i = new Intent(MainActivity.this, CellEditActivity.class);
			    	i.putExtra(CellDB.COLUMN_CELL_ID, c.getInt(c.getColumnIndex(CellDB.COLUMN_CELL_ID)));
			    	i.putExtra(CellDB.COLUMN_CONTENT, c.getString(c.getColumnIndex(CellDB.COLUMN_CONTENT)));
			    	i.putExtra(CellDB.COLUMN_FLAG, c.getInt(c.getColumnIndex(CellDB.COLUMN_FLAG)));
			    	i.putExtra(CellDB.COLUMN_NAME, c.getString(c.getColumnIndex(CellDB.COLUMN_NAME)));
			    	i.putExtra(CellDB.COLUMN_PARENTID, c.getInt(c.getColumnIndex(CellDB.COLUMN_PARENTID)));
			    	startActivityForResult(i, MainActivity.this.getId());
		    	}
			}
        	
		});
        
        
        refrashListView();
    }
    @SuppressLint("UseValueOf") public void refrashListView()
    {
    	Cursor[] cursors = new Cursor[2];  
    	cursors[0] = groupDbRead.query(GroupDB.TABLE_NAME, null, "parent_id=?", new String[]{(new Integer(getId())).toString()}, null, null, null);
    	cursors[1] = cellDbRead.query(CellDB.TABLE_NAME, null, "parent_id=?", new String[]{(new Integer(getId())).toString()}, null, null, null);
    	Cursor merCur = new MergeCursor(cursors);
    	adapter.changeCursor(merCur);
    	
    }
    public Activity getParentActivity()
    {
    	return this;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId())
        {
        case R.id.btnAddCell:
        	startActivityForResult(new Intent(MainActivity.this, CellEditActivity.class), REQUEST_CODE_ADD_CELL);
        	break;
        case R.id.btnAddGroup:
        	MainActivity.this.groupName = new EditText(MainActivity.this);
			newGroupDialog = new AlertDialog.Builder(MainActivity.this).setTitle("请输入").setIcon(
				android.R.drawable.ic_dialog_info).setView(
				MainActivity.this.groupName).setPositiveButton("确定", new DialogInterface.OnClickListener() { 
	                
	               @Override 
	               public void onClick(DialogInterface dialog, int which) {
	            	   groupListController.addGroup(MainActivity.this,MainActivity.this.groupName.getText().toString());
	            	   refrashListView();
	               } 
	           }).setNegativeButton("取消", null).show();
        	break;
        default:
        	break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
    	if(arg1 == RESULT_OK)
    		cellController.addCell(this, arg2.getStringExtra("name"), arg2.getStringExtra("content"));
    	
    	refrashListView();
    	super.onActivityResult(arg0, arg1, arg2);
    }
    
    //backspace twice time to exit
    private long lastClickTime = 0;
    @Override
    public void onBackPressed() 
    {
    	if(lastClickTime<=0)
    	{
    		Toast.makeText(this, "再按一次后退键退出", Toast.LENGTH_SHORT).show();
    		lastClickTime = System.currentTimeMillis();
    	}
    	else
    	{
    		long currentClickTime = System.currentTimeMillis();
    		if(currentClickTime - lastClickTime < 1000)
    		{
    			finish();
    		}
    		else
    		{
        		Toast.makeText(this, "再按一次后退键退出", Toast.LENGTH_SHORT).show();
        		lastClickTime = currentClickTime;
    		}
    	}
    }
    
    public int getId()
    {
    	return id;
    }
    public static GroupListController getGroupListControler()
    {
    	return groupListController;
    }
    public static CellController getCellControler()
    {
    	return cellController;
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Cursor c = adapter.getCursor();
    	c.moveToPosition(position);
    	super.onListItemClick(l, v, position, id);
    }
    @Override
    protected void onDestroy() {
    	groupListController.getGroupDbRead().close();
    	groupListController.getGroupDbWrite().close();
    	cellController.getCellDbRead().close();
    	cellController.getCellDbWrite().close();
    	super.onDestroy();
    }
}

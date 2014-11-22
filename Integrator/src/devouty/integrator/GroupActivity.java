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
import android.telephony.CellLocation;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class GroupActivity extends ListActivity {
	
	//define
	//activity
	ListActivity parentActivity;
    //id
	private int id = -1;
	
    //controller
	GroupListController groupListController;
	CellController cellController;
    EditText groupName;
    //flag
    public static final int REQUEST_CODE_ADD_CELL = 2;
    public static final int REQUEST_CODE_EDIT_GROUP = 3;
    public static final int REQUEST_CODE_EDIT_CELL = 4;
    //dialog
	AlertDialog newGroupDialog;
	
    //adapter
	SimpleCursorAdapter adapter;
	
    //database
	SQLiteDatabase groupDbRead,cellDbRead;
	
    //listview
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        
        Intent i = getIntent();
        //title
        setTitle(i.getStringExtra("name"));
        //id
        id = i.getIntExtra("_id", -1);
        //System.out.println("ID is"+id);
        //controller
        groupListController = MainActivity.getGroupListControler();
        cellController = MainActivity.getCellControler();
        //database
        groupDbRead =  groupListController.getGroupDbRead();
        cellDbRead = cellController.getCellDbRead();
        //System.out.println(dbRead.hashCode());
        
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
			    	Intent i = new Intent(GroupActivity.this, GroupActivity.class);
			    	i.putExtra(GroupDB.COLUMN_GROUP_ID, c.getInt(c.getColumnIndex(GroupDB.COLUMN_GROUP_ID)));
			    	i.putExtra(GroupDB.COLUMN_NAME, c.getString(c.getColumnIndex(GroupDB.COLUMN_NAME)));
			    	i.putExtra(GroupDB.COLUMN_FLAG, c.getInt(c.getColumnIndex(GroupDB.COLUMN_FLAG)));
			    	i.putExtra(GroupDB.COLUMN_PARENTID, c.getInt(c.getColumnIndex(GroupDB.COLUMN_PARENTID)));
					
			    	startActivityForResult(i, GroupActivity.this.getId());
		    	}
		    	else
		    	{
		    		Intent i = new Intent(GroupActivity.this, CellEditActivity.class);
		    		i.putExtra(CellDB.COLUMN_CELL_ID, c.getInt(c.getColumnIndex(CellDB.COLUMN_CELL_ID)));
			    	i.putExtra(CellDB.COLUMN_CONTENT, c.getString(c.getColumnIndex(CellDB.COLUMN_CONTENT)));
			    	i.putExtra(CellDB.COLUMN_FLAG, c.getInt(c.getColumnIndex(CellDB.COLUMN_FLAG)));
			    	i.putExtra(CellDB.COLUMN_NAME, c.getString(c.getColumnIndex(CellDB.COLUMN_NAME)));
			    	i.putExtra(CellDB.COLUMN_PARENTID, c.getInt(c.getColumnIndex(CellDB.COLUMN_PARENTID)));

			    	startActivityForResult(i, GroupActivity.this.getId());
		    	}
			}
        	
		});
        refrashListView();
        
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
        	Intent i = new Intent(GroupActivity.this, CellEditActivity.class);
        	i.putExtra("parentId", GroupActivity.this.getId());
        	i.putExtra("flag", 1);
        	startActivityForResult(i, REQUEST_CODE_ADD_CELL);
        	break;
        case R.id.btnAddGroup:
        	GroupActivity.this.groupName = new EditText(GroupActivity.this);
			newGroupDialog = new AlertDialog.Builder(GroupActivity.this).setTitle("请输入").setIcon(
				android.R.drawable.ic_dialog_info).setView(
						GroupActivity.this.groupName).setPositiveButton("确定", new DialogInterface.OnClickListener() { 
	                
	               @Override 
	               public void onClick(DialogInterface dialog, int which) {
	            	   groupListController.addGroup(GroupActivity.this,GroupActivity.this.groupName.getText().toString());
	            	   refrashListView();
	               } 
	           }).setNegativeButton("取消", null).show();
        	break;
        default:
        	break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @SuppressLint("UseValueOf") @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	//Cursor c = dbRead.query(CellDB.TABLE_NAME, new String[]{CellDB.COLUMN_CELL_ID}, "_id=?", new String[]{new Integer(data.getIntExtra("_id", -1)).toString()}, null, null, null);
    	//System.out.println(data.getIntExtra("flag", -1));
    	//System.out.println(data.getStringExtra("name")+"  "+ data.getStringExtra("content"));
    	if(resultCode == RESULT_OK)
    		cellController.addCell(this, data.getStringExtra("name"), data.getStringExtra("content"));
    	
    	refrashListView();
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    
    public void refrashListView()
    {
    	Cursor[] cursors = new Cursor[2];  
    	cursors[0] = groupDbRead.query(GroupDB.TABLE_NAME, null, "parent_id=?", new String[]{(new Integer(getId())).toString()}, null, null, null);
    	cursors[1] = cellDbRead.query(CellDB.TABLE_NAME, null, "parent_id=?", new String[]{(new Integer(getId())).toString()}, null, null, null);
    	  
    	Cursor merCur = new MergeCursor(cursors);  
//    	System.out.println("ID= "+getId()+" "+cursors[0].getCount()+" "+cursors[1].getCount()+" "+merCur.getCount());
    	adapter.changeCursor(merCur);
//    	ListView lv = getListView();
//    	for(int i = 0;i <merCur.getCount();i++)
//    	{
//    		merCur.moveToPosition(i);
//    		if(merCur.getInt(merCur.getColumnIndex(GroupDB.COLUMN_FLAG)) == 1)
//    		{
//    			adapter.getView(i, lv, lv).setBackgroundColor(0);
//    		}
//    	}
    }
    public int getId()
    {
    	return id;
    }
    public Activity getParentActivity()
    {
    	return this;
    }
}

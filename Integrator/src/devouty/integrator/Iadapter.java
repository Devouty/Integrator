package devouty.integrator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Iadapter extends BaseAdapter {

	
	private Context context;
	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout ll = null;
		
		if (convertView != null)
		{
			ll =(LinearLayout) convertView;
		}else
		{
			ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.list_cell_view,null) ;
		}
		
		ImageView iv = (ImageView) ll.findViewById(R.id.image);
		TextView tv = (TextView) ll.findViewById(R.id.tvName);
		/*Cursor c = getCursor();
		tv.setText(c.getString(c.getColumnIndex(GroupDB.COLUMN_NAME)));
		if(c.getInt(c.getColumnIndex(GroupDB.COLUMN_FLAG)) == 1)
		{
			iv.setImageResource(R.drawable.image_file);
		}
		else
		{
			iv.setImageResource(R.drawable.image_text);
		}*/
		return ll;
		//return super.getView(position, convertView, parent);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}

package devouty.integrator;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CellEditActivity extends Activity {

	//database
	private SQLiteDatabase dbRead,dbWrite;
	private CellController controller;
	
	//edittext
	private EditText etName, etContent;
	
	//flag
	public static final String EXTRA_FLAG = "flag";
	public static final String EXTRA_NAME = "name";
	public static final String EXTRA_CONTENT = "content";
	
	//id
	private int flag = -1;
	
	//listener
	private OnClickListener btnClickHander;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cell_edit);
		
		//database
		controller = new CellController();
		controller.onCreate(this);
		dbRead = controller.getCellDbRead();
		dbWrite = controller.getCellDbWrite();
		
		//edittext
		etName = (EditText) findViewById(R.id.etName);
		etContent = (EditText) findViewById(R.id.etContent);
		
		etName.setText(getIntent().getStringExtra(EXTRA_NAME));
		etContent.setText(getIntent().getStringExtra(EXTRA_CONTENT));
		
		
		//listener
		btnClickHander = new OnClickListener() {
				@Override
					public void onClick(View v) {

						switch (v.getId()) {
						case R.id.btnSave:
							Intent it = new Intent();
							it.putExtra(EXTRA_NAME, etName.getText().toString());
							it.putExtra(EXTRA_CONTENT, etContent.getText().toString());
							it.putExtra(EXTRA_FLAG, flag);
							setResult(RESULT_OK,it);
							finish();
							break;
						case R.id.btnCancel:
							setResult(RESULT_CANCELED);
							finish();
							break;
						default:
							break;
						}
					}
				};
		findViewById(R.id.btnSave).setOnClickListener(btnClickHander);
		findViewById(R.id.btnCancel).setOnClickListener(btnClickHander);
		
	}
	
	
	
	
	
	
	@Override
	protected void onDestroy() {
		dbRead.close();
		dbWrite.close();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		super.onDestroy();
	}
	
}

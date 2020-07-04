package sas.part.time.job.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**This class helps copying database in the device**/
public class DataBaseHelper extends SQLiteOpenHelper implements DatabaseInfo {
	
	@SuppressLint("SdCardPath")
	private final String DB_PATH = "/data/data/sas.part.time.job/databases/";
	
	private final Context myContext;

	public DataBaseHelper(Context context) {
		super(context, COUNTRY_DB, null, 1);
		this.myContext = context;
	}

	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		
		if (!dbExist) {
			try {
				getReadableDatabase();
				copyDataBase();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**Checks if the database already exists**/
	private boolean checkDataBase() {
	    File dbFile = new File(DB_PATH + COUNTRY_DB);
	    return dbFile.exists();
	}

	/**This function copies the database***/
	private void copyDataBase() throws IOException {
		InputStream myInput = myContext.getAssets().open(COUNTRY_DB);
		String outFileName = DB_PATH + COUNTRY_DB;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	@Override
	public synchronized void close() {
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
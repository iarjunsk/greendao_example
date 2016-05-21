package com.codekrypt.greendao;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codekrypt.greendao.db.DaoMaster;
import com.codekrypt.greendao.db.DaoSession;
import com.codekrypt.greendao.db.LOG;
import com.codekrypt.greendao.db.LOGDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Dao --> Data Access Object
    private LOGDao log_dao; // Sql access object
    private LOG temp_log_object; // Used for creating a LOG Object

    String log_text="";  //Entered text data is save in this variable

    private  final String DB_NAME ="logs-db" ;  //Name of Db file in the Device

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise DAO
        log_dao=setupDb();

        //Setting up form elements
        Button textSave= (Button) findViewById(R.id.textSave);
        Button textTop= (Button) findViewById(R.id.textTop);
        final TextView textData=(TextView) findViewById(R.id.textData);

        assert textSave != null;
        textSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                log_text=textData.getText().toString();
                temp_log_object=new LOG(null,log_text);// Class Object, Id is auto increment

                SaveToSQL(temp_log_object);
            }
        });

        assert textTop != null;
        textTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textData.setText( getFromSQL() );
            }
        });
    }

    //---------------------------------SQL QUERY Functions-----------------------------------------//
    public String getFromSQL(){
        List<LOG> log_list = log_dao.queryBuilder().orderDesc(LOGDao.Properties.Id).build().list();
        //Get the list of all LOGS in Database in descending order

        if(log_list.size()>0) {  //if list is not null

            return log_list.get(0).getText();
            //get(0)--> 1st object
            // getText() is the function in LOG class
        }
        return "";
    }

    public void SaveToSQL(LOG log_object) {
        log_dao.insert(log_object);
    }
    //----------------------------***END SQL QUERY***---------------------------------------------//


    //-------------------------------DB Setup Functions---------------------------------------------//

    //Return the Configured LogDao Object
    public LOGDao setupDb(){
        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(this, DB_NAME, null); //create database db file if not exist
        SQLiteDatabase db = masterHelper.getWritableDatabase();  //get the created database db file
        DaoMaster master = new DaoMaster(db);//create masterDao
        DaoSession masterSession=master.newSession(); //Creates Session session
        return masterSession.getLOGDao();
    }
    //-------------------------***END DB setup Functions***---------------------------------------//

}

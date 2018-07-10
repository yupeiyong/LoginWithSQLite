package login.first.yong.loginwithsqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SQLiteOpenHelper sqLiteOpenHelper;

    private final String DATABASE_NAME="dbForTest.db";
    private final String TABLE_NAME="T_User";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqLiteOpenHelper=new BaseDao(this,DATABASE_NAME,null,0);
        init();
    }

    private void init(){
        final EditText editTextUserName=findViewById(R.id.txtUserName);
        final EditText editTextPassword=findViewById(R.id.txtPassword);
        Button btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result=login(editTextUserName.getText().toString(),editTextPassword.getText().toString());
            }
        });
        Button btnRegister=findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result=register(editTextUserName.getText().toString(),editTextPassword.getText().toString());
            }
        });
        Button btnLogout=findViewById(R.id.btnRegister);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //退出系统
                System.exit(0);
            }
        });
    }

    private boolean register(String name,String password){
        if(name==null ||name.length()==0)
            return false;
        if(password.isEmpty())
            return false;

        try{
            SQLiteDatabase readableDatabase = sqLiteOpenHelper.getReadableDatabase();
            String sql="Select AccountName,Password From "+TABLE_NAME+" where AccountName=?";
            Cursor cr=readableDatabase.rawQuery(sql,new String[]{name});
            if(cr.moveToFirst()) {
                short showTime=10;
                Toast toast = Toast.makeText(this, "此帐号已经注册", showTime);
                toast.show();
                return false;
            }
            SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
            String sql1 = "insert into " + TABLE_NAME + " (AccountName,Password) values("+name+","+password+");";
            db.execSQL(sql1);
            return true;
        }catch (Exception ex){
            short showTime=10;
            Toast toast = Toast.makeText(this, ex.getMessage(), showTime);
            toast.show();
            return false;
        }
    }

    private boolean login(String name,String password){
        try{
            SQLiteDatabase readableDatabase = sqLiteOpenHelper.getReadableDatabase();
            String sql="Select AccountName,Password From "+TABLE_NAME+" where AccountName=?";
            Cursor cr=readableDatabase.rawQuery(sql,new String[]{name});
            if(cr.moveToFirst()) {
                String pwd=cr.getString(cr.getColumnIndex("Password"));
                if(pwd.equals(password))
                    return true;
            }
            return false;
        }catch (Exception ex){
            short showTime=10;
            Toast toast = Toast.makeText(this, ex.getMessage(), showTime);
            toast.show();
            return false;
        }
    }


}

package com.example.anudeep.contentproviders;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_click;
    private static int RECORD_REQUEST_CODE =1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);
        if(permission != PackageManager.PERMISSION_GRANTED){
            Log.i("DDD","permission to record denied");
            makeRequest();

        }
        btn_click = (Button) findViewById(R.id.btn_click);
        btn_click.setOnClickListener(this);
    }

    private void makeRequest() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},RECORD_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_click:

                contents();


                break;
        }
    }

    private void contents() {
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        StringBuffer output = new StringBuffer();
        String phoneNumber;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
// Iterate every contact in the phone
        if (cursor.getCount() > 0) {
// counter = 0;
            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
                if (hasPhoneNumber > 0) {
                    output.append("\n First Name:" + name);
//This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);
                    }
                    phoneCursor.close();
                }
            }
        }
        Toast.makeText(getApplicationContext(),output.toString(),Toast.LENGTH_LONG).show();
    }


}

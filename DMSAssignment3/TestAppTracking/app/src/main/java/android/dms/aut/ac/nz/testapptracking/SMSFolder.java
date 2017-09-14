package android.dms.aut.ac.nz.testapptracking;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class SMSFolder extends AppCompatActivity implements OnClickListener
{
    Button btnSent, btnInbox, btnDraft;
    ImageButton newSMSButton, deleteSMSButton;
    ListView lvMsg;
    ArrayList<String> smsMessagesList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsfolder);

        newSMSButton = (ImageButton) findViewById(R.id.newSMSButton);
        newSMSButton.setOnClickListener(this);
        deleteSMSButton = (ImageButton) findViewById(R.id.deleteSMSButton);
        deleteSMSButton.setOnClickListener(this);

        btnInbox = (Button) findViewById(R.id.inboxButton);
        btnInbox.setOnClickListener(this);

        btnSent = (Button) findViewById(R.id.sentButton);
        btnSent.setOnClickListener(this);

        btnDraft = (Button) findViewById(R.id.draftButton);
        btnDraft.setOnClickListener(this);

        lvMsg = (ListView) findViewById(R.id.SMSdisplay);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsMessagesList);
    }

    @Override
    public void onClick(View v)
    {
        if (v == btnInbox)
        {
            adapter.clear();
            lvMsg.setAdapter(adapter);

            ContentResolver contentResolver = getContentResolver();
            Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);

            int senderIndex = smsInboxCursor.getColumnIndex("address");
            int messageIndex = smsInboxCursor.getColumnIndex("body");

            if (messageIndex < 0 || !smsInboxCursor.moveToFirst()) return;

            adapter.clear();

            do
            {
                String sender = smsInboxCursor.getString(senderIndex);
                String message = smsInboxCursor.getString(messageIndex);
                String formattedText = String.format(getResources().getString(R.string.sms_message), sender, message);
                adapter.add(Html.fromHtml(formattedText).toString());
            } while (smsInboxCursor.moveToNext());

            lvMsg.setAdapter(adapter);

            lvMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(SMSFolder.this);
                    adb.setTitle("Delete?");
                    adb.setMessage("Are you sure you want to delete this message?");

                    final int positionToRemove = position;

                    adb.setNegativeButton("NO", null);
                    adb.setPositiveButton("YES", new AlertDialog.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                            smsMessagesList.remove(positionToRemove);
                            adapter.notifyDataSetChanged();
                        }
                    });

                    adb.show();
                }
            });
        }

        if (v == btnSent)
        {
            adapter.clear();
            lvMsg.setAdapter(adapter);

            ContentResolver contentResolver = getContentResolver();
            Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/sent"), null, null, null, null);

            int senderIndex = smsInboxCursor.getColumnIndex("address");
            int messageIndex = smsInboxCursor.getColumnIndex("body");

            if (messageIndex < 0 || !smsInboxCursor.moveToFirst()) return;

            adapter.clear();

            do
            {
                String sender = smsInboxCursor.getString(senderIndex);
                String message = smsInboxCursor.getString(messageIndex);
                String formattedText = String.format(getResources().getString(R.string.sms_message), sender, message);
                adapter.add(Html.fromHtml(formattedText).toString());
            } while (smsInboxCursor.moveToNext());

            lvMsg.setAdapter(adapter);
        }

        if (v == btnDraft)
        {
            adapter.clear();
            lvMsg.setAdapter(adapter);

            try
            {
                ContentResolver contentResolver = getContentResolver();
                Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/draft"), null, null, null, null);

                int senderIndex = smsInboxCursor.getColumnIndex("address");
                int messageIndex = smsInboxCursor.getColumnIndex("body");

                if (messageIndex < 0 || !smsInboxCursor.moveToFirst()) return;

                adapter.clear();

                do
                {
                    String sender = smsInboxCursor.getString(senderIndex);
                    String message = smsInboxCursor.getString(messageIndex);
                    String formattedText = String.format(getResources().getString(R.string.sms_message), sender, message);
                    adapter.add(Html.fromHtml(formattedText).toString());
                } while (smsInboxCursor.moveToNext());

                lvMsg.setAdapter(adapter);
            } catch (Exception e) {
            }
        }

        if (v == newSMSButton)
        {
            startActivity(new Intent(this, SMSActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }
}

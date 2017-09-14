package android.dms.aut.ac.nz.testapptracking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SMSActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText composeMsg,text;
    SmsManager smsManager = SmsManager.getDefault();
    private static SMSActivity smsActivity;
    String getNumber, getInputMsg;
    TextView messageTextViewSent;

    public static SMSActivity instance() {
        return smsActivity;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        smsActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        text = (EditText)findViewById(R.id.phoneNoField);
        composeMsg = (EditText) findViewById(R.id.composeMsg);
        messageTextViewSent = (TextView) findViewById(R.id.messageTextViewSent);
    }


    public void onSendClick(View view)
    {
        setGetNumber();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again!",
                    Toast.LENGTH_LONG).show();
        } else {
            try
            {
                smsManager.sendTextMessage(getGetNumber(), null, composeMsg.getText().toString(), null, null);
                Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show();

                setComposeMsg();
                messageTextViewSent.setText("You sent a message:\n'" + getGetInputMsg() + "'\nTO: " + getGetNumber());
                composeMsg.setText("");
                text.setText("");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS failed to send, please try again.\nOR Check number and message again.",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public void setGetNumber()
    {
        getNumber = text.getText().toString();
    }

    public String getGetNumber()
    {
        return getNumber;
    }

    public String getGetInputMsg()
    {
        return getInputMsg;
    }

    public void setComposeMsg()
    {
        getInputMsg  = composeMsg.getText().toString();
    }

    public void clickBackInbox(View v)
    {
        startActivity(new Intent(this, SMSFolder.class));
        finish();
    }

    public void onBackPressed()
    {
        onClick(findViewById(View.generateViewId()));
    }

    @Override
    public void onClick(View v)
    {
        startActivity(new Intent(this, SMSFolder.class));
        finish();
    }

}

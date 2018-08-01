package com.google.developer.colorvalue;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.developer.colorvalue.data.Card;
import com.google.developer.colorvalue.service.CardService;
import com.google.developer.colorvalue.ui.ColorView;

import static com.google.developer.colorvalue.data.CardSQLite.COLUMN_HEXNAME;
import static com.google.developer.colorvalue.data.CardSQLite.COLUMN_ID;
import static com.google.developer.colorvalue.data.CardSQLite.COLUMN_NAME;

public class CardActivity extends AppCompatActivity {

    private Uri uri;
    private TextView colornameTV, hexnameTV;
    private ColorView colorView;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        colornameTV = findViewById(R.id.colorname);
        hexnameTV = findViewById(R.id.hexname);
        colorView = findViewById(R.id.colorDisplay);


        if (getIntent().getExtras() != null)
        {
            uri = Uri.parse(getIntent().getExtras().getString("uri"));
            id = uri.getPathSegments().get(1);
            loadCard(uri);
        }
    }

    private void loadCard(Uri uri) {

        String[] projection = {
                COLUMN_ID,
                COLUMN_HEXNAME,
                COLUMN_NAME
        };

        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            Card card = new Card(cursor);
            int _id = card.getID();
            String _name = card.getName();
            String _hex = card.getHex();
            int _color = card.getColorInt();

            /*String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String hexname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HEXNAME));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));*/

            colornameTV.setText(_name);
            hexnameTV.setText(_hex);
            colorView.setBackgroundColor(_color);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_delete:
                CardService.deleteCard(CardActivity.this, uri);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

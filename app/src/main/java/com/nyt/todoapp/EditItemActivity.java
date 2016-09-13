package com.nyt.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends AppCompatActivity {

    EditText etEditItem;
    Button btnSave;
    Intent intentReturn;
    String curItemData;
    Integer itemIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        intentReturn = new Intent();
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        btnSave = (Button) findViewById(R.id.btnSaveItem);

        /* Get passed data from previous activity */
        curItemData = getIntent().getStringExtra("item_data");
        itemIndex = getIntent().getIntExtra("item_index",-1);

        /* Just in case no data was passed to */
        if (curItemData == null || itemIndex == -1){
            Toast.makeText(EditItemActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }

        etEditItem.setText(curItemData);
        etEditItem.setSelection(etEditItem.getText().length());
        etEditItem.requestFocus();

    }

    public void onSaveSubmit(View v){
        String strNewItemData = etEditItem.getText().toString().trim();
        intentReturn.putExtra("item_index",itemIndex);
        intentReturn.putExtra("item_new_data",strNewItemData);
        setResult(RESULT_OK,intentReturn);
        this.finish();
    }
}

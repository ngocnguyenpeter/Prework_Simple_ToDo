package com.nyt.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /* Variables */
    ArrayList<String> todoItems;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    EditText etNewItem;

    /* Request codes */
    private final int REQUEST_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        populateArrayItems();
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((resultCode==RESULT_OK) && (requestCode==REQUEST_EDIT)){
            String newItemData = data.getExtras().getString("item_new_data","");
            int position = data.getExtras().getInt("item_index",-1);

            if (position != -1){
                if (newItemData.isEmpty()){
                    todoItems.remove(position);
                    Toast.makeText(MainActivity.this, "Item removed!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    todoItems.set(position,newItemData);
                }
                itemsAdapter.notifyDataSetChanged();
                writeItems();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Invalid result", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(MainActivity.this, "Edit item canceled!", Toast.LENGTH_SHORT).show();
        }
    }

    public void populateArrayItems(){
        todoItems = new ArrayList<String>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,todoItems);
    }

    public void readItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir,"todo.txt");
        try {
            todoItems=new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir,"todo.txt");
        try {
            FileUtils.writeLines(file,todoItems);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupListViewListener(){
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = new Intent(MainActivity.this,EditItemActivity.class);
                editIntent.putExtra("item_index",position);
                editIntent.putExtra("item_data",todoItems.get(position));
                startActivityForResult(editIntent,REQUEST_EDIT);
            }
        });
    }

    public void onAddItem(View view) {
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        itemsAdapter.add(etNewItem.getText().toString());
        etNewItem.setText("");
        writeItems();
    }


}

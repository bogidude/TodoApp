package todotest.com.todoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * Need to add saving tasks to a file so that it can withstand being wiped from Recent Apps
 */
public class TodoPage extends ActionBarActivity {
    static final String TASKS = "Users Tasks"; //for saving data in the SavedInstanceBundle thingie
    private ArrayList<taskTemplate> tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Help", "First thing");//this is like system.out.println the first argument is a tag, followed by the actual message
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_page);
        //Get View Objects
        ListView taskList = (ListView) findViewById(R.id.taskListView);
        EditText addTaskText = (EditText) findViewById(R.id.addTaskField);

        //Set up task list
        if(savedInstanceState == null){//Never opened before
            Log.d("Save State", "No save state");
            tasks = new ArrayList<taskTemplate>();
        }else{ //Bring up the user's previous tasks
            tasks = savedInstanceState.getParcelableArrayList(TASKS);
        }
        tasks.add(new taskTemplate("first"));//add a random task
        //Set up the visual portrayal of the design
        final ArrayAdapter<taskTemplate> taskAdapter = new ArrayAdapter<taskTemplate>(this,android.R.layout.simple_expandable_list_item_1, tasks);
        taskList.setAdapter(taskAdapter);
        if (addTaskText != null) {
            addTaskText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    boolean handled = false;
                    Log.d("Edit task","Reached listener");
                    if(i == EditorInfo.IME_ACTION_DONE){
                        //add Task
                        taskTemplate temp = new taskTemplate(textView.getText().toString());
                        tasks.add(temp);
                        handled = true;
                        textView.setText("");
                        taskAdapter.notifyDataSetChanged();//let the listview know the data has changed
                    }
                    Log.d("Edit task",""+handled);
                    return handled;
                }
            });
        }else {
            Log.d("Add Task", "No editText exists");
        }


        //Handle items being clicked (this should expand the item...)
        AdapterView.OnItemClickListener mItemClicked = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        };
        //Handle items being long clicked (this should go to a task edit page)
        AdapterView.OnItemLongClickListener mItemLongClicked = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        };
        taskList.setOnItemClickListener(mItemClicked);
        taskList.setOnItemLongClickListener(mItemLongClicked);

        TextView empty = new TextView(this);
        empty.setText("Add tasks here");
        taskList.setEmptyView(empty);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop(){
        //Save data to internal file here!
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current tasks
        savedInstanceState.putParcelableArrayList(TASKS, tasks);
        Log.d("Saving State", "Should have saved tasks");
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
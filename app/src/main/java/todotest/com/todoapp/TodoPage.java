package todotest.com.todoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Need to add saving tasks to a file so that it can withstand being wiped from Recent Apps
 */
public class TodoPage extends ActionBarActivity {
    static final String TASKS = "Users Tasks"; //for saving data in the SavedInstanceBundle thingie
    private final String mFile = "todoappData";
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
        if(savedInstanceState == null){//Not already open
            Log.d("Save State", "No save state");
            tasks = new ArrayList<taskTemplate>();
            try {//try to open a file holding previously entered tasks
                FileInputStream fis = openFileInput(mFile); //open file stream
                byte[] Bytie = new byte[(int) fis.getChannel().size()];//hopefully we will never have a file that is more than INT_MAX bytes
                fis.read(Bytie,0,Bytie.length);
                fis.close(); //close file stream

                Parcel mParcel = Parcel.obtain();
                mParcel.unmarshall(Bytie,0,Bytie.length); //turn bytes back into a Parcel
                mParcel.setDataPosition(0); //make sure to start reading items in the Parcel from the beginning
                tasks = mParcel.readArrayList(taskTemplate.class.getClassLoader());//Should fill tasks?
                mParcel.recycle(); //clears away parcel
                Log.d("File Size",""+Bytie.length);
            } catch (FileNotFoundException e) {
                Log.e("File Opening", "File not found");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("File Opening", "Failed to read from file");
                e.printStackTrace();
            }
        }else{ //Bring up the user's tasks from previous session
            tasks = savedInstanceState.getParcelableArrayList(TASKS);
        }
        //Set up the visual portrayal of the design
        final TaskArrayAdapter taskAdapter = new TaskArrayAdapter(this,R.layout.min_task_item, tasks);
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
        /*AdapterView.OnItemClickListener mItemClicked = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tasks.get(position).toggle_comp();
                Log.d("TESTING","Is it here "+ position);
                CheckBox check = (CheckBox) view.findViewById(R.id.checkBox);
                TextView name = (TextView) view.findViewById(R.id.taskName);
                TextView date = (TextView) view.findViewById(R.id.taskDate);
                check.setChecked(tasks.get(position).getCompletion());
                if(tasks.get(position).getCompletion()){
                    view.setBackgroundColor(Color.GRAY);
                }else{
                    view.setBackgroundColor(Color.WHITE);
                }
                taskAdapter.notifyDataSetChanged();
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
        taskList.setOnItemLongClickListener(mItemLongClicked);*/
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
        Log.d("Saving data","Starting");
        Parcel mParcel = Parcel.obtain();
        mParcel.writeList(tasks);
        byte[] mByte = mParcel.marshall();//turns the parcel into a byte array
        mParcel.recycle(); //clean up the parcel
        try { //Save task list to file on the phone
            FileOutputStream fos = openFileOutput(mFile,Context.MODE_PRIVATE);
            fos.write(mByte);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("File Saving", "File not Found");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("File Saving", "Falied to write to file");
        }
        Log.d("Saving data","Finished");
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

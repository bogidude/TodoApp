package todotest.com.todoapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Bogdan on 7/27/2015.
 * Meant for using our own custom view for each task...
 */
public class TaskArrayAdapter extends ArrayAdapter<taskTemplate>{
    private ArrayList<taskTemplate> tasks;
    public TaskArrayAdapter(Context context, int resource, ArrayList<taskTemplate> objects) {
        super(context, resource, objects);
        tasks = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView; //View to change
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.min_task_item, null);
        }
        final taskTemplate i = tasks.get(position);
        Log.d("TESTING","WONDERING "+ i.toString());
        if(i != null){
            CheckBox check = (CheckBox) v.findViewById(R.id.checkBox);
            TextView name = (TextView) v.findViewById(R.id.taskName);
            TextView date = (TextView) v.findViewById(R.id.taskDate);
            if(check != null){//Set up check box
                check.setChecked(i.getCompletion());
                /*check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        i.toggle_comp();
                        Log.d("TESTING","CMON");
                        //check.setChecked(i.getCompletion());
                    }
                });*/
            }
            if(name != null){//Set up the name field
                name.setText(i.getName());
            }
            if(date != null){
                GregorianCalendar dueDate = i.getDate();
                date.setText("Due: " + i.convertMonth() + " " + i.getDate().get(Calendar.DATE) +", " + i.getDate().get(Calendar.YEAR));
            }
        }
        return v;
    }
}

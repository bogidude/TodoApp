package todotest.com.todoapp;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Bogdan on 7/26/2015.
 * Parcelable allows us to keep data between opening and closing our app
 * we write all of the values of this task template to a parcel using the writeToParcel() method
 * we can then read values out of a parcel passed to us in the same order the values were added.
 */
public class taskTemplate implements Parcelable {
    private String name = "Insert Name here";
    private GregorianCalendar due_date;
    private Boolean completion = false;

    //just the name of the task
    public taskTemplate(String n){
        name = n;
        due_date = new GregorianCalendar();
        due_date.add(Calendar.DATE,1);//Set the due date for tomorrow

    }
    //just the date of the task is pased on
    public taskTemplate(GregorianCalendar d){
        due_date  = d;
    }
    //name and date
    public taskTemplate(String n, GregorianCalendar d){
        name = n;
        due_date = d;
    }

    public taskTemplate(Parcel in){
        name = in.readString();
        Log.d("NAME", name+" " +in.dataAvail());
        due_date = new GregorianCalendar();
        due_date.setTimeInMillis(in.readLong());
        completion = (in.readInt() == 1 ? true : false);
    }

    //Set a new date
    public void setDate(GregorianCalendar d){
        due_date = d;
    }
    //Rename a date
    public void setName(String n){
        name = n;
    }
    //Get the date
    public GregorianCalendar getDate(){
        return due_date;
    }
    //Get the name
    public String getName(){
        return name;
    }
    //Complete Task
    public void complete(){
        completion = true;
    }
    //Toggle Completion of task
    public void toggle_comp(){
        completion = !completion;
    }
    //Turns our task in to a string
    public String toString(){
        String month;
        switch(due_date.get(Calendar.MONTH)){
            case 1:
                month = "January";
                break;
            case 2:
                month = "February";
                break;
            case 3:
                month = "March";
                break;
            case 4:
                month = "April";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "June";
                break;
            case 7:
                month = "July";
                break;
            case 8:
                month = "August";
                break;
            case 9:
                month = "September";
                break;
            case 10:
                month = "October";
                break;
            case 11:
                month = "November";
                break;
            default:
                month = "December";
                break;
        }
        return name +", Due by " +month+" " + due_date.get(Calendar.DATE) + ", "+ due_date.get(Calendar.YEAR);
    }
    //Needed for Parcelable implementation. No idea what we would change this to so I'm just leaving it
    @Override
    public int describeContents() {
        return 0;
    }
    //This is how turn this class into something that can be saved
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeLong(due_date.getTimeInMillis());
        parcel.writeInt(completion ? 1 : 0);
    }
    /*This is needed apparently but I am not sure what all it does necessarily. Looks like
    * this allows to turn a parcel into a taskTemplate
    */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public taskTemplate createFromParcel(Parcel in) {
            return new taskTemplate(in);
        }

        public taskTemplate[] newArray(int size) {
            return new taskTemplate[size];
        }
    };
}

package com.example.dairys.ViewModel;

import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeViewModel extends ViewModel {

    public String date;

    public void setDate(Calendar cal){
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        date =  day + " " + month_name + " " + year;
    }

    public long setDateCard() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        Date dateToParse = format.parse(date.toString());
        Calendar c = Calendar.getInstance();
        c.setTime(dateToParse);
        String dateToSend = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(sdf.parse(dateToSend).getTime());
        return date.getTime();
    }

}

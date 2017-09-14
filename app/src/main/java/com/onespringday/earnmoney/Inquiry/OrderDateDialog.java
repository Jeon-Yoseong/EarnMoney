package com.onespringday.earnmoney.Inquiry;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.onespringday.earnmoney.R;

/**
 * Created by yoseong on 2017-08-22.
 */

public class OrderDateDialog extends Dialog {

    private Context context;
    private Button selectDateBtn;
    private Button cancel;
    private DatePicker datePicker;

    private String year;
    private String month;
    private String day;
    private String selectDate;
    public OrderDateDialog(Context context) {
        super(context);
        this.context = context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_datepicker);

        selectDateBtn = (Button) findViewById(R.id.date_select_btn);
        cancel = (Button) findViewById(R.id.date_cancel);
        datePicker = (DatePicker) findViewById(R.id.date_picker);


        selectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = String.valueOf(datePicker.getYear());
                month = String.valueOf(datePicker.getMonth()+1);
                day = String.valueOf(datePicker.getDayOfMonth());

                if (month.length() == 1) {
                    month = "0" + month;
                }
                if (day.length() == 1) {
                    day = "0" + day;
                }
                selectDate = year+"/"+month+"/"+day;
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public String getSelectDate() {
        return selectDate;
    }

    public void setSelectDate(String selectDate) {
        this.selectDate = selectDate;
    }
}

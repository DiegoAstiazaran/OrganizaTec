package itesm.mx.organizatec;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class NewNoteDetailFragment extends Fragment {

    OnSaveListener mCallBack;

    TextInputEditText etName;
    TextInputEditText etTopic;
    TextInputEditText etDate;

    Spinner spinnerPartial;

    Calendar calendarNote;

    public NewNoteDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_note_detail, container, false);

        setHasOptionsMenu(true);

        etName = (TextInputEditText) view.findViewById(R.id.edit_content_name);
        etTopic = (TextInputEditText) view.findViewById(R.id.edit_content_topic);
        spinnerPartial = (Spinner) view.findViewById(R.id.spinner_partial);
        etDate = (TextInputEditText) view.findViewById(R.id.edit_date);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.partials));

        spinnerPartial.setAdapter(adapter);

        calendarNote = Calendar.getInstance();
        calendarNote.setTime(new Date());

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendarNote.set(Calendar.YEAR, year);
            calendarNote.set(Calendar.MONTH, monthOfYear);
            calendarNote.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
            }

        };

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date,
                        calendarNote.get(Calendar.YEAR), calendarNote.get(Calendar.MONTH), calendarNote.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        updateLabel();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_nav_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu_button:
                return saveMaterialContent();

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void updateLabel() {
        String newDate = getDateFormatView(calendarNote.getTime());

        etDate.setText(newDate);
    }

    private boolean saveMaterialContent() {
        if (!checkTextField(etName))
            return false;

        if (!checkTextField(etTopic))
            return false;

        if (!checkTextField(etDate))
            return false;


        mCallBack.saveNewNote(etName.getText().toString(), etTopic.getText().toString(), spinnerPartial.getSelectedItem().toString(), calendarNote.getTime().toString() );

        return true;
    }

    private String getDateFormatView(Date date) {
        String myFormat = "EEEE, MMMM dd yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat);

        return dateFormat.format(date);
    }

    private boolean checkTextField(EditText view) {
        String text = view.getText().toString().trim();

        if(text.length() == 0) {
            Toast.makeText(getContext(), "No puedes dejar ningun campo vacío", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public interface OnSaveListener {
        public void saveNewNote(String name, String topic, String partial, String date);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity) {
            activity = (Activity) context;
            try {
                mCallBack = (OnSaveListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnSaveListener");
            }
        }
    }


}

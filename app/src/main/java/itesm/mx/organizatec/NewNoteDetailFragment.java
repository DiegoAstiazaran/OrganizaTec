package itesm.mx.organizatec;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class NewNoteDetailFragment extends Fragment {

    public static final String MATERIAL_OBJECT = "material_object";

    OnSaveListener mCallBack;

    TextInputEditText etName;
    TextInputEditText etTopic;
    TextInputEditText etDate;

    Spinner spinnerPartial;

    Calendar calendarNote;

    Material material;

    public NewNoteDetailFragment() {
        // Required empty public constructor
    }

    public static NewNoteDetailFragment newInstance(Material material) {
        NewNoteDetailFragment fragment = new NewNoteDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(MATERIAL_OBJECT, material);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            material = (Material) bundle.getSerializable(MATERIAL_OBJECT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_note_detail, container, false);

        setHasOptionsMenu(true);

        etName = (TextInputEditText) view.findViewById(R.id.edit_content_name);
        etTopic = (TextInputEditText) view.findViewById(R.id.edit_content_topic);
        spinnerPartial = (Spinner) view.findViewById(R.id.spinner_partial);
        etDate = (TextInputEditText) view.findViewById(R.id.edit_date);

        ArrayAdapter<String> adapter = new ArrayAdapter<>
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

        if (material.getId() != 0) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            String[] topics = getResources().getStringArray(R.array.partials);

            Integer spinnerPosition = Arrays.asList(topics).indexOf(material.getPartial());

            etName.setText(material.getName());
            etTopic.setText(material.getTopic());
            spinnerPartial.setSelection(spinnerPosition);
            calendarNote.setTime(getStringAsDate(material.getDate()));
        }

        updateLabel();

//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_nav_bar, menu);
        if (material.getId() != 0) {
            inflater.inflate(R.menu.delete_nav_bar, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu_button:
                return saveMaterialContent();

            case R.id.delete_menu_button:
                mCallBack.deleteNoteFromDetail();
                return true;

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

        material.setName(etName.getText().toString());
        material.setTopic(etTopic.getText().toString());
        material.setPartial(spinnerPartial.getSelectedItem().toString());
        material.setDate(etDate.getText().toString());

        mCallBack.saveNewNote(material);

        return true;
    }

    private String getDateFormatView(Date date) {
        String myFormat = "EEEE, MMMM dd yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat);

        return dateFormat.format(date);
    }

    private Date getStringAsDate(String date) {
        String myFormat = "EEEE, MMMM dd yyyy";
        DateFormat dateFormat = new SimpleDateFormat(myFormat);

        try {
            return dateFormat.parse(date);
        } catch (Exception e) {
            return new Date();
        }

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
        void saveNewNote(Material material);
        void deleteNoteFromDetail ();
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

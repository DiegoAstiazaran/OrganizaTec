package itesm.mx.organizatec;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewDocumentVideoActivity extends AppCompatActivity {

    public static final String MATERIAL_TYPE = "material_type";
    public static final String CONTENT_TYPE = "content_type";

    MaterialOperations dbOperations;

    private String materialType;
    private String contentType;

    TextInputEditText etName;
    TextInputEditText etTopic;
    TextInputEditText etDocumentUrl;
    TextInputEditText etDate;

    Spinner spinnerPartial;

    Calendar calendarNote;

    Material originalMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_document_video);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            originalMaterial = bundle.getParcelable("material_object");
            materialType = bundle.getString(MATERIAL_TYPE);
            contentType = bundle.getString(CONTENT_TYPE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etName = (TextInputEditText) findViewById(R.id.edit_content_name);
        etTopic = (TextInputEditText) findViewById(R.id.edit_content_topic);
        etDocumentUrl = (TextInputEditText) findViewById(R.id.edit_content_document_url);
        spinnerPartial = (Spinner) findViewById(R.id.spinner_partial);
        etDate = (TextInputEditText) findViewById(R.id.edit_date);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.partials));

        spinnerPartial.setAdapter(adapter);

        calendarNote = Calendar.getInstance();
        calendarNote.setTime(new Date());

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
                calendarNote.set(Calendar.YEAR, year);
                calendarNote.set(Calendar.MONTH, monthOfYear);
                calendarNote.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }
        };

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewDocumentVideoActivity.this, date,
                        calendarNote.get(Calendar.YEAR), calendarNote.get(Calendar.MONTH), calendarNote.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etDocumentUrl.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    findViewById(R.id.save_menu_button).performClick();
                    return true;
                }
                return false;
            }
        });

        if (originalMaterial != null) {
            String titleContentType = originalMaterial.getContentType().equals("Video") ? "Video" : "Documento";
            getSupportActionBar().setTitle(titleContentType + getMaterialTypeActionBarTitle(originalMaterial.getMaterialType()));

            String[] topics = getResources().getStringArray(R.array.partials);

            Integer spinnerPosition = Arrays.asList(topics).indexOf(originalMaterial.getPartial());

            etName.setText(originalMaterial.getName());
            etTopic.setText(originalMaterial.getTopic());
            spinnerPartial.setSelection(spinnerPosition);
            calendarNote.setTime(getStringAsDate(originalMaterial.getDate()));
            etDocumentUrl.setText(originalMaterial.getContent());

        } else  {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
            String titleContentType = contentType.equals("Video") ? "video" : "documento";
            getSupportActionBar().setTitle("Nuevo " + titleContentType + getMaterialTypeActionBarTitle(materialType));

        }

        updateLabel();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_nav_bar, menu);
        return true;
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

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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

        if (!checkTextField(etDocumentUrl))
            return false;

        Material material = new Material(materialType, contentType, etName.getText().toString(), etTopic.getText().toString(), spinnerPartial.getSelectedItem().toString(), calendarNote.getTime().toString(), etDocumentUrl.getText().toString() );

        try {
            dbOperations = new MaterialOperations(getApplicationContext());
            dbOperations.open();

            dbOperations.addMaterial(material);
        } catch (Exception e) {
            return false;
        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putParcelable(MaterialListFragment.MATERIAL_OBJECT, material);

        intent.putExtras(bundle);

        setResult(RESULT_OK, intent);

        finish();

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
            Toast.makeText(getApplicationContext(), "No puedes dejar ningun campo vacío", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private String getMaterialTypeActionBarTitle (String materialType) {
        String title = "";
        if (materialType.equals("Practice") ) {
            title = " de práctica";
        } else if (materialType.equals("Theory")) {
            title = " de teoría";
        }

        return title;
    }

}

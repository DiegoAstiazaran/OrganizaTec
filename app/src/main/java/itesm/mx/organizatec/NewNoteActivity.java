package itesm.mx.organizatec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NewNoteActivity extends AppCompatActivity implements NewNoteContentFragment.OnContinueListener, NewNoteDetailFragment.OnSaveListener{

    public static final String MATERIAL_TYPE = "material_type";
    public static final String CONTENT_TYPE = "content_type";

    private String materialType;
    private String contentType;

    Material material;

    MaterialOperations dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            materialType = bundle.getString(MATERIAL_TYPE);
            contentType = bundle.getString(CONTENT_TYPE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);

        material = new Material();

        getSupportActionBar().setTitle("Nueva nota" + getMaterialTypeActionBarTitle(materialType));

        NewNoteContentFragment fragment = new NewNoteContentFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment, "NewNoteContentFragment").addToBackStack(null).commit();

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @SuppressLint("RestrictedApi")
    public void continueNewNote(ArrayList<byte[]> images, String noteContent) {
        material.setImages(images);
        material.setContent(noteContent);

        NewNoteDetailFragment fragment = new NewNoteDetailFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "NewNoteDetailFragment").addToBackStack(null).commit();

    }

    public void saveNewNote(String name, String topic, String partial, String date) {

        material.setName(name);
        material.setTopic(topic);
        material.setPartial(partial);
        material.setDate(date);
        material.setMaterialType(materialType);
        material.setContentType(contentType);

        dbOperations = new MaterialOperations(getApplicationContext());
        dbOperations.open();

        try {
            dbOperations.addMaterial(material);
        } catch (Exception e) {

        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putParcelable(MaterialListFragment.MATERIAL_OBJECT, material);

        intent.putExtras(bundle);

        setResult(RESULT_OK, intent);

        finish();

    }

    @Override
    public void onBackPressed() {

        Fragment contentFragment = getSupportFragmentManager().findFragmentByTag("NewNoteContentFragment");

        if(contentFragment != null && contentFragment.isVisible()) {
            finish();
        }

        Fragment detailFragment = getSupportFragmentManager().findFragmentByTag("NewNoteDetailFragment");

        if(detailFragment != null && detailFragment.isVisible()) {
            super.onBackPressed();
        }

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

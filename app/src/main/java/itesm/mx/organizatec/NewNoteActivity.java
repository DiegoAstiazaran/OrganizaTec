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

    public static final String MATERIAL_OBJECT = "material_object";
    public static final String MATERIAL_TYPE = "material_type";
    public static final String CONTENT_TYPE = "content_type";

    private String materialType;
    private String contentType;

    Material originalMaterial;
    Material material;

    MaterialOperations dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            originalMaterial = bundle.getParcelable(MATERIAL_OBJECT);
            materialType = bundle.getString(MATERIAL_TYPE);
            contentType = bundle.getString(CONTENT_TYPE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NewNoteContentFragment fragment;

        if (originalMaterial != null) {
            getSupportActionBar().setTitle("Nota" + getMaterialTypeActionBarTitle(originalMaterial.getMaterialType()));

            material = originalMaterial;

        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
            getSupportActionBar().setTitle("Nueva nota" + getMaterialTypeActionBarTitle(materialType));

            material = new Material();

        }

        fragment = NewNoteContentFragment.newInstance(material);

        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment, "NewNoteContentFragment").addToBackStack(null).commit();

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @SuppressLint("RestrictedApi")
    public void continueNewNote(Material paramMaterial) {
        NewNoteDetailFragment fragment = NewNoteDetailFragment.newInstance(material);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "NewNoteDetailFragment").addToBackStack(null).commit();

    }

    public void saveNewNote(Material paramMaterial) {

        if(material.getId() == 0) {
            material.setMaterialType(materialType);
            material.setContentType(contentType);

            try {
                dbOperations = new MaterialOperations(getApplicationContext());
                dbOperations.open();
                dbOperations.addMaterial(material);
            } catch (Exception e) {

            }
        } else {
            try {
                dbOperations = new MaterialOperations(getApplicationContext());
                dbOperations.open();
                dbOperations.updateMaterial(material);
            } catch (Exception e) {

            }

        }

        Intent intent = new Intent();

        Bundle bundle = new Bundle();
//        bundle.putParcelable(MaterialListFragment.MATERIAL_OBJECT, material);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);

        finish();

    }

    public void deleteNoteFromContent() {
        deleteMaterial();
    }
    public void deleteNoteFromDetail() {
        deleteMaterial();
    }

    public void deleteMaterial() {
        try {
            dbOperations = new MaterialOperations(getApplicationContext());
            dbOperations.open();

            dbOperations.deleteMaterial(originalMaterial);
        } catch (Exception e) {
            return;
        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putLong(MaterialListFragment.DELETED_MATERIAL_OBJECT_ID, originalMaterial.getId());

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

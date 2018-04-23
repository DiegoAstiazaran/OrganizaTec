package itesm.mx.organizatec;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NewNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ADD_NOTE_IMAGE_CODE = 1;

    NoteImageAdapter imageAdapter;

    GridView gvImages;
    Button btnAddImage;
    EditText etNoteContent;
    ArrayList<Bitmap> noteImages;

    Material material;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);

        gvImages = (GridView) findViewById(R.id.gridView_note_images);
        btnAddImage = (Button) findViewById(R.id.btn_add_note_image);
        etNoteContent = (EditText) findViewById(R.id.edit_note_content);

        noteImages = new ArrayList<>();

        btnAddImage.setOnClickListener(this);

        imageAdapter = new NoteImageAdapter(this, noteImages);

        gvImages.setAdapter(imageAdapter);

        material = new Material();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_note_image:
                Intent intent = new Intent();

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), ADD_NOTE_IMAGE_CODE);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.continue_nav_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.continue_menu_button:
                String noteContent = etNoteContent.getText().toString();

                if (noteContent.trim().length() == 0 && noteImages.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Please add some text or images.", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<byte[]> images = new ArrayList<>();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    for(Bitmap bitmap: noteImages){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        images.add(byteArray);
                    }

                    material.setImages(images);
                    material.setContent(noteContent);

//                    Intent intent = new Intent(getApplicationContext(), NewNoteDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable(NewNoteDetailActivity.MATERIAL, material);
//                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), "CONTINUE!", Toast.LENGTH_SHORT).show();
                }

                return true;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode==RESULT_OK) {
            switch(requestCode){
                case ADD_NOTE_IMAGE_CODE:
                    if (data != null) {
                        Uri contentURI = data.getData();
                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                            noteImages.add(imageBitmap);
                            imageAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    break;
            }

        }
    }

}

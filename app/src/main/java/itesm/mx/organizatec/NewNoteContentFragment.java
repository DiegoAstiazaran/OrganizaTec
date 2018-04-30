package itesm.mx.organizatec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class NewNoteContentFragment extends Fragment implements View.OnClickListener {

    private static final int ADD_NOTE_IMAGE_CODE = 1;

    public static final String MATERIAL_OBJECT = "material_object";

    private String materialType;

    OnContinueListener mCallBack;

    NoteImageAdapter imageAdapter;

    GridView gvImages;
    Button btnAddImage;
    EditText etNoteContent;
    ArrayList<Bitmap> noteImages;

    Material material;

    public NewNoteContentFragment() {
        // Required empty public constructor
    }

    public static NewNoteContentFragment newInstance(Material material) {
        NewNoteContentFragment fragment = new NewNoteContentFragment();
        Bundle args = new Bundle();
        args.putParcelable(MATERIAL_OBJECT, material);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            material = bundle.getParcelable(MATERIAL_OBJECT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_note_content, container, false);

        setHasOptionsMenu(true);

        gvImages = (GridView) view.findViewById(R.id.gridView_note_images);
        btnAddImage = (Button) view.findViewById(R.id.btn_add_note_image);
        etNoteContent = (EditText) view.findViewById(R.id.edit_note_content);

        noteImages = new ArrayList<>();

        btnAddImage.setOnClickListener(this);

        imageAdapter = new NoteImageAdapter(getContext(), noteImages);

        gvImages.setAdapter(imageAdapter);

        return view;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.continue_nav_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.continue_menu_button:
                String noteContent = etNoteContent.getText().toString();

                if (noteContent.trim().length() == 0 && noteImages.size() == 0) {
                    Toast.makeText(getContext(), "Please add some text or images.", Toast.LENGTH_SHORT).show();
                    return true;
                }

                ArrayList<byte[]> images = new ArrayList<>();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                for(Bitmap bitmap: noteImages){
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    images.add(byteArray);
                }

                mCallBack.continueNewNote(images, noteContent);

                Toast.makeText(getContext(), "CONTINUE!", Toast.LENGTH_SHORT).show();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ADD_NOTE_IMAGE_CODE) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    noteImages.add(imageBitmap);
                    imageAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public interface OnContinueListener {
        public void continueNewNote (ArrayList<byte[]> images, String noteContent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity) {
            activity = (Activity) context;
            try {
                mCallBack = (OnContinueListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnContinueListener");
            }
        }
    }

}

package itesm.mx.organizatec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class NewNoteContentFragment extends Fragment implements View.OnClickListener {

    private static final int ADD_NOTE_IMAGE_CODE = 1;

    public static final String MATERIAL_OBJECT = "material_object";

    OnContinueListener mCallBack;

    NoteImageAdapter imageAdapter;

    GridView gvImages;
    Button btnAddImage;
    EditText etNoteContent;
    ArrayList<String> noteImages;

    Material material;

    MaterialOperations dbOperations;

    public NewNoteContentFragment() {
        // Required empty public constructor
    }

    public static NewNoteContentFragment newInstance(Material material) {
        NewNoteContentFragment fragment = new NewNoteContentFragment();
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

        dbOperations = new MaterialOperations(getContext());
        dbOperations.open();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_note_content, container, false);

        setHasOptionsMenu(true);

        gvImages = (GridView) view.findViewById(R.id.gridView_note_images);
        btnAddImage = (Button) view.findViewById(R.id.btn_add_note_image);
        etNoteContent = (EditText) view.findViewById(R.id.edit_note_content);

        btnAddImage.setOnClickListener(this);

        noteImages = new ArrayList<>();

        if (material.getId() != 0) {
            etNoteContent.setText(material.getContent());

//            ArrayList<String> listByte = material.getImages();

            noteImages = material.getImages();

//            for(Uri uri: listByte)
//            {
////                Bitmap bm = BitmapFactory.decodeByteArray(array, 0, array.length); //use android built-in functions
//                noteImages.add(uri);
//            }

        }

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
        if (material.getId() != 0) {
            inflater.inflate(R.menu.delete_nav_bar, menu);
        }
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

                material.setContent(noteContent);
                material.setImages(noteImages);

                mCallBack.continueNewNote(material);

                return true;
            case R.id.delete_menu_button:
                mCallBack.deleteNoteFromContent();
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
                noteImages.add(contentURI.toString());
                imageAdapter.notifyDataSetChanged();

            }

        }
    }

    public interface OnContinueListener {
        void continueNewNote (Material material);
        void deleteNoteFromContent ();
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

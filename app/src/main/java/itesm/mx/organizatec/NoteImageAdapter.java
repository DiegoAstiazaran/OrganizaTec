package itesm.mx.organizatec;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

public class NoteImageAdapter extends ArrayAdapter<Bitmap> {

    private ArrayList<Bitmap> images;

    public NoteImageAdapter(Context context, ArrayList<Bitmap> images) {
        super(context, 0, images);
        this.images = images;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Bitmap imageBitmap = getItem(position);

        // convertView --> view to be reused, created if null
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_image_square, parent, false);
        }

        ImageView ivNoteImage = (ImageView) convertView.findViewById(R.id.image_note_image);
        ImageButton ibDeleteImage = (ImageButton) convertView.findViewById(R.id.btn_delete_note_image);

        ivNoteImage.setImageBitmap(imageBitmap);

        ibDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                images.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}

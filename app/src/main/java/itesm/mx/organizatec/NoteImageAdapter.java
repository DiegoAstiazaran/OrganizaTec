package itesm.mx.organizatec;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NoteImageAdapter extends ArrayAdapter<String> {

    private ArrayList<String> images;
    private Context context;

    public NoteImageAdapter(Context context, ArrayList<String> images) {
        super(context, 0, images);
        this.images = images;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Uri currentUri = Uri.parse(getItem(position));

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_image_square, parent, false);
        }

        ImageView ivNoteImage = (ImageView) convertView.findViewById(R.id.image_note_image);
        ImageButton ibDeleteImage = (ImageButton) convertView.findViewById(R.id.btn_delete_note_image);

        try {
            Glide.with(context)
                    .load(currentUri)
                    .into(ivNoteImage);


//            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), currentUri);

//            ivNoteImage.setImageBitmap(imageBitmap);

        } catch (Exception e) {
            Drawable imageDrawable = context.getResources().getDrawable(R.drawable.noimage);

            ivNoteImage.setImageDrawable(imageDrawable);
        }

        ibDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                images.remove(position);
                notifyDataSetChanged();
            }
        });

        ivNoteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(currentUri, "image/*");
                context.startActivity(intent);
            }
        });

        return convertView;
    }


}

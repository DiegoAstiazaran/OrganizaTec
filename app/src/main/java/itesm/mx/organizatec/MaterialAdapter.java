package itesm.mx.organizatec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MaterialAdapter extends ArrayAdapter<Material> {

    public MaterialAdapter(Context context, ArrayList<Material> appliances) {
        super(context, 0, appliances);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {

        Material material = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.material_row, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.text_material_name);

        tvName.setText(material.getName());

        return convertView;
    }
}
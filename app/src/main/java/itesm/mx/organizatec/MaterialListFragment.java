package itesm.mx.organizatec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Arrays;

public class MaterialListFragment extends Fragment {

    MaterialOperations dbOperations;

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final Integer NEW_MATERIAL_FRAGMENT_KEY = 2;
    public static final String MATERIAL_OBJECT = "material_object";

    Spinner spinnerTopic;
    Spinner spinnerPartial;
    RadioGroup radioGroupOrderBy;
    RadioGroup radioGroupSortOrder;
    ListView listViewMaterial;

    ArrayList<Material> materials;

    MaterialAdapter materialAdapter;

    public MaterialListFragment() {
    }

    public static MaterialListFragment newInstance(int sectionNumber) {
        MaterialListFragment fragment = new MaterialListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbOperations = new MaterialOperations(getContext());
        dbOperations.open();

        materials = dbOperations.getAllMaterials();

        materialAdapter = new MaterialAdapter(getContext(), materials);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_material_list, container, false);

        spinnerTopic = (Spinner) rootView.findViewById(R.id.spinner_topic);
        spinnerPartial = (Spinner) rootView.findViewById(R.id.spinner_partial);
        radioGroupOrderBy = (RadioGroup) rootView.findViewById(R.id.radio_group_order_by);
        radioGroupSortOrder = (RadioGroup) rootView.findViewById(R.id.radio_group_sort_order);
        listViewMaterial = (ListView) rootView.findViewById(R.id.list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.partials));

        spinnerPartial.setAdapter(adapter);

        spinnerPartial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }

        });

        ArrayList<String> topicList = new ArrayList<>();
        topicList.add("Primera tema");
        topicList.add("Segundo tema");
        topicList.add("Tercer tema");
        topicList.add("Cuarto tema");
        topicList.add("Quinto tema");
        topicList.add("Sexto tema");
        topicList.add("Septimo tema");
        topicList.add("Gran Tema");

        ArrayAdapter<String> topicAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, topicList);

        spinnerTopic.setAdapter(topicAdapter);

        spinnerTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }

        });

        listViewMaterial.setAdapter(materialAdapter);

        return rootView;
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_button_order_by_name:
                if (checked)
                    break;
            case R.id.radio_button_order_by_date:
                if (checked)
                    break;
            case R.id.radio_button_sort_order_desc:
                if (checked)
                    break;
            case R.id.radio_button_sort_order_asc:
                if (checked)
                    break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == NEW_MATERIAL_FRAGMENT_KEY) {
            Material newMaterial = data.getExtras().getParcelable(MATERIAL_OBJECT);

            materials.add(newMaterial);

            materialAdapter.notifyDataSetChanged();


        }

    }


}
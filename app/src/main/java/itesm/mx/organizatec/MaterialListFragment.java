package itesm.mx.organizatec;

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

    private static final String ARG_SECTION_NUMBER = "section_number";

    Spinner spinnerTopic;
    Spinner spinnerPartial;
    RadioGroup radioGroupOrderBy;
    RadioGroup radioGroupSortOrder;
    ListView listViewMaterial;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_material_list, container, false);

        spinnerTopic = (Spinner) rootView.findViewById(R.id.spinner_topic);
        spinnerPartial = (Spinner) rootView.findViewById(R.id.spinner_partial);
        radioGroupOrderBy = (RadioGroup) rootView.findViewById(R.id.radio_group_order_by);
        radioGroupSortOrder = (RadioGroup) rootView.findViewById(R.id.radio_group_sort_order);
        listViewMaterial = (ListView) rootView.findViewById(R.id.list_view);

        // TODO: Define proper list of partials and topics - get them from DB

        ArrayList<String> partialList = new ArrayList<>();
        partialList.add("Primer parcial");
        partialList.add("Segundo parcial");
        partialList.add("Tercer parcial");
        partialList.add("Final");

        ArrayAdapter<String> partialAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_dropdown_item, partialList);

        spinnerPartial.setAdapter(partialAdapter);

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

        ArrayList<String> materialList = new ArrayList<>(Arrays.asList("Nota 1", "Nota 2","Nota 3","Nota 4","Nota 5","Nota 6","Nota 7","Nota 8", "Nota 9", "Nota 10", "Nota 11", "Nota 12", "Nota 13"));

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_list_item_1, materialList);

        listViewMaterial.setAdapter(listViewAdapter);

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

}
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MaterialListFragment extends Fragment implements AdapterView.OnItemClickListener {

    MaterialOperations dbOperations;

    private static final String MATERIAL_TYPE = "material_type";
    private static final String CONTENT_TYPE = "content_type";
    public static final Integer NEW_MATERIAL_FRAGMENT_KEY = 2;
    public static final Integer EDIT_MATERIAL_FRAGMENT_KEY = 3;
    public static final String MATERIAL_OBJECT = "material_object";
    public static final String DELETED_MATERIAL_OBJECT_ID = "deleted_material_object_id";

    private String materialType;
    private String contentType;

    Spinner spinnerTopic;
    Spinner spinnerPartial;
    RadioGroup radioGroupOrderBy;
    RadioGroup radioGroupSortOrder;
    ListView listViewMaterial;

    ArrayList<Material> materials;
    ArrayList<String> topicList;
    ArrayAdapter<String> topicAdapter;

    MaterialAdapter materialAdapter;

    public MaterialListFragment() {
    }

    public static MaterialListFragment newInstance(String materialType, String contentType) {
        MaterialListFragment fragment = new MaterialListFragment();
        Bundle args = new Bundle();
        args.putString(CONTENT_TYPE, contentType);
        args.putString(MATERIAL_TYPE, materialType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            materialType = bundle.getString(MATERIAL_TYPE);
            contentType = bundle.getString(CONTENT_TYPE);
        }

        dbOperations = new MaterialOperations(getContext());
        dbOperations.open();

        materials = new ArrayList<>();

        materialAdapter = new MaterialAdapter(getContext(), materials);

        topicList = new ArrayList<>();

        topicAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, topicList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_material_list, container, false);

        spinnerTopic = (Spinner) rootView.findViewById(R.id.spinner_topic);
        spinnerPartial = (Spinner) rootView.findViewById(R.id.spinner_partial);
        radioGroupOrderBy = (RadioGroup) rootView.findViewById(R.id.radio_group_order_by);
        radioGroupSortOrder = (RadioGroup) rootView.findViewById(R.id.radio_group_sort_order);
        listViewMaterial = (ListView) rootView.findViewById(R.id.list_view);

        radioGroupOrderBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean desc = radioGroupSortOrder.getCheckedRadioButtonId() == R.id.radio_button_sort_order_desc;

                if (checkedId == R.id.radio_button_order_by_name) {
                    sortByName(desc);
                } else if (checkedId == R.id.radio_button_order_by_date) {
                    sortByDate(desc);
                }

            }
        });

        radioGroupSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean desc = checkedId == R.id.radio_button_sort_order_desc;

                if (radioGroupOrderBy.getCheckedRadioButtonId() == R.id.radio_button_order_by_name) {
                    sortByName(desc);
                } else if (radioGroupOrderBy.getCheckedRadioButtonId() == R.id.radio_button_order_by_date) {
                    sortByDate(desc);
                }

            }
        });

        final ArrayList<String> partials = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.partials)));

        partials.add(0, "Todos los parciales");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, partials);

        spinnerPartial.setAdapter(adapter);

        spinnerPartial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                updateMaterials();
                materialAdapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }

        });


        updateTopics();

        spinnerTopic.setAdapter(topicAdapter);

        spinnerTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                updateMaterials();
                materialAdapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }

        });

        listViewMaterial.setAdapter(materialAdapter);

        listViewMaterial.setOnItemClickListener(this);

        updateMaterials();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == NEW_MATERIAL_FRAGMENT_KEY) {

            updateMaterials();

            updateTopics();

        } else
        if (resultCode == -1 && requestCode == EDIT_MATERIAL_FRAGMENT_KEY) {

            updateMaterials();

            updateTopics();

        }

    }

    private void updateTopics() {
        ArrayList<String> topics = dbOperations.getTopics(materialType, contentType);
        Collections.sort(topics);
        Set<String> hs = new HashSet<>(topics);
        ArrayList<String> uniqueTopics = new ArrayList<>(hs);
        uniqueTopics.add(0, "Todos los temas");

        topicList.clear();
        topicList.addAll(uniqueTopics);

        topicAdapter.notifyDataSetChanged();

    }

    private void updateMaterials() {
        String partial = spinnerPartial.getSelectedItem().toString();
        String topic = spinnerTopic.getSelectedItem().toString();

        ArrayList<Material> materialArrayList = dbOperations.getQueriedMaterials(materialType, contentType, topic, partial);

        materials.clear();
        materials.addAll(materialArrayList);

        sortList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Material material = materials.get(position);
        String contentType = material.getContentType();
        if ( contentType.equals("Document") || contentType.equals("Video")) {
            Intent intent = new Intent(getContext(), NewDocumentVideoActivity.class);

            Bundle bundle = new Bundle();
            bundle.putParcelable(NewDocumentVideoActivity.MATERIAL_OBJECT, material);

            intent.putExtras(bundle);

            startActivityForResult(intent, EDIT_MATERIAL_FRAGMENT_KEY);

        } else {
            Intent intent = new Intent(getContext(), NewNoteActivity.class);

            Bundle bundle = new Bundle();
            bundle.putLong(NewNoteActivity.MATERIAL_OBJECT_ID, material.getId());

            intent.putExtras(bundle);

            startActivityForResult(intent, EDIT_MATERIAL_FRAGMENT_KEY);
        }

    }

    private void sortByName (boolean desc) {
        Collections.sort(materials, Material.nameComparator);

        if (desc)
            Collections.reverse(materials);

        materialAdapter.notifyDataSetChanged();
    }

    private void sortByDate (boolean desc) {
        Collections.sort(materials, Material.dateComparator);

        if (desc)
            Collections.reverse(materials);

        materialAdapter.notifyDataSetChanged();
    }

    private void sortList() {
        boolean desc = radioGroupSortOrder.getCheckedRadioButtonId() == R.id.radio_button_sort_order_desc;

        if (radioGroupOrderBy.getCheckedRadioButtonId() == R.id.radio_button_order_by_name) {
            sortByName(desc);
        } else if (radioGroupOrderBy.getCheckedRadioButtonId() == R.id.radio_button_order_by_date) {
            sortByDate(desc);
        }
    }

}
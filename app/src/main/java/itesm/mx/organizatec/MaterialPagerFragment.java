package itesm.mx.organizatec;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MaterialPagerFragment extends Fragment {

    final static String MATERIAL_TYPE = "material_type";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private String materialType;

    FloatingActionButton fabAdd;

    public MaterialPagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            materialType = args.getString(MATERIAL_TYPE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_material, container, false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mViewPager.setCurrentItem(1);

        fabAdd = (FloatingActionButton)view.findViewById(R.id.fab_add_material);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemId = mViewPager.getCurrentItem();

                String fragmentTag = "android:switcher:" + R.id.view_pager + ":" + itemId;

                Fragment fragment = getChildFragmentManager().findFragmentByTag(fragmentTag);

                if(itemId == 2) {
                    Intent intent = new Intent(getContext(), NewNoteActivity.class);

                    Bundle bundle = new Bundle();

                    bundle.putString(NewNoteActivity.MATERIAL_TYPE, materialType);
                    bundle.putString(NewNoteActivity.CONTENT_TYPE, getContentType(itemId));
                    intent.putExtras(bundle);

                    fragment.startActivityForResult(intent, MaterialListFragment.NEW_MATERIAL_FRAGMENT_KEY);
                } else {
                    Intent intent = new Intent(getContext(), NewDocumentVideoActivity.class);

                    Bundle bundle = new Bundle();

                    bundle.putString(NewDocumentVideoActivity.MATERIAL_TYPE, materialType);
                    bundle.putString(NewDocumentVideoActivity.CONTENT_TYPE, getContentType(itemId));
                    intent.putExtras(bundle);

                    fragment.startActivityForResult(intent, MaterialListFragment.NEW_MATERIAL_FRAGMENT_KEY);
                }
            }
        });

        String title = "Material de ";

        String materialTypeForTitle = materialType.equals("Practice") ? "práctica" : "teoría";

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title + materialTypeForTitle);

        return view;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a MaterialListFragment
            return MaterialListFragment.newInstance(materialType, getContentType(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    private String getContentType (int position) {
        return position == 0 ? "Video" : position == 1 ? "Document" : "Note";

    }

}

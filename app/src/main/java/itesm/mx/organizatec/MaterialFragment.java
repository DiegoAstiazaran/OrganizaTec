package itesm.mx.organizatec;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MaterialFragment extends Fragment {

    final static String MATERIAL_TYPE = "material";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    FloatingActionButton fabAdd;

    public MaterialFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_material, container, false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(1);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        fabAdd = (FloatingActionButton)view.findViewById(R.id.fab_add_material);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemId = mViewPager.getCurrentItem();
                if(itemId == 2) {
                    Intent intent = new Intent(getContext(), NewNoteActivity.class);
                    startActivity(intent);
                } else {
                    String materialType = itemId == 0 ? "Video" : "Document";
                    Intent intent = new Intent(getContext(), NewDocumentVideoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", materialType);
                    startActivity(intent);
                }
            }
        });

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
            return MaterialListFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

}

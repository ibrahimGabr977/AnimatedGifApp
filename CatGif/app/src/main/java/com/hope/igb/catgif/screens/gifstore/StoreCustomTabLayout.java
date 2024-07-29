package com.hope.igb.catgif.screens.gifstore;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.screens.comman.views.BaseObservableMvc;
import java.util.HashMap;
import java.util.Objects;

public class StoreCustomTabLayout extends BaseObservableMvc<StoreCustomTabLayout.MyTabSelectedListener>
        implements View.OnClickListener {

    private final LinearLayout tabLayout;

    private final HashMap<Integer, TextView> tabs;
    private static int last_selected_tab = R.id.store_tab_all;



    public interface MyTabSelectedListener {
        void onTabSelected(String tab_name);
    }


    public StoreCustomTabLayout(View rootView) {

        setRootView(rootView);


        tabLayout = findViewById(R.id.customTabLayout);
        tabs = new HashMap<>();



        initTabs();

    }



    private void initTabs(){
        tabs.put(R.id.store_tab_all,  tabLayout.findViewById(R.id.store_tab_all));
        tabs.put(R.id.store_tab_funny,  tabLayout.findViewById(R.id.store_tab_funny));
        tabs.put(R.id.store_tab_cute,  tabLayout.findViewById(R.id.store_tab_cute));
        tabs.put(R.id.store_tab_top,  tabLayout.findViewById(R.id.store_tab_top));



        for (Integer tab_id: tabs.keySet())
            Objects.requireNonNull(tabs.get(tab_id)).setOnClickListener(this);


    }



    private void tabSelectedUiChanges(int selected_tab){
        if (selected_tab != last_selected_tab){

            TextView tab = tabs.get(selected_tab);

            if (tab != null){
                tab.setBackgroundResource(R.drawable.z_selected_tabbg);
                tab.setTextColor(getContext().getResources().getColor(R.color.white_color));
            }


            lastSelectedTabUiChanges();

            last_selected_tab = selected_tab;

        }
    }


    private void lastSelectedTabUiChanges(){
        TextView tab = tabs.get(last_selected_tab);

        if (tab != null){
            tab.setBackgroundResource(R.drawable.z_tablayoutbg);
            tab.setTextColor(getContext().getResources().getColor(R.color.black_color));
        }
    }


    @Override
    public void onClick(View v) {


        tabSelectedUiChanges(v.getId());


        for (MyTabSelectedListener listener : getListeners())
            listener.onTabSelected(String.valueOf(v.getId()).split("store_tab_")[1]);


    }


}

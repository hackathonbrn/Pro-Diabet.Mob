package raitoningu.pro_diabet.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import raitoningu.pro_diabet.View.DrawerMenu.DrawerAdapter;
import raitoningu.pro_diabet.View.DrawerMenu.DrawerItem;
import raitoningu.pro_diabet.View.DrawerMenu.SimpleItem;
import raitoningu.pro_diabet.View.DrawerMenu.SpaceItem;
import raitoningu.pro_diabet.R;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    /* Menu Dependencies */
    private static final int POS_HOME = 0;
    private static final int POS_NOTES = 1;
    private static final int POS_CHARTS = 2;
    private static final int POS_PRODUCTS = 3;
    private static final int POS_ACCOUNT = 4;
    private static final int POS_LOGOUT = 6;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    Fragment fragment;
    Toolbar toolbar;
    TextView titleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        titleToolbar = toolbar.findViewById(R.id.textToolbar);

        /*Menu*/
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(200)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withRootViewScale(1f)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_HOME).setChecked(true),
                createItemFor(POS_NOTES),
                createItemFor(POS_CHARTS),
                createItemFor(POS_PRODUCTS),
                createItemFor(POS_ACCOUNT),
                new SpaceItem(156),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_HOME);
        titleToolbar.setText("Главная");
        /*Menu end*/
    }

    /* Menu functions */
    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }
        slidingRootNav.closeMenu();
        switch (position) {
            case POS_HOME: {
                fragment = HomeFragment.newInstance();
                titleToolbar.setText("Главная");
                break;
            }
            case POS_NOTES: { // Notes
                titleToolbar.setText("Измерения");
                break;
            }
            case POS_CHARTS: { //Graphs and charts
                fragment = ChartsFragment.newInstance();
                titleToolbar.setText("Графики");
                break;
            }
            case POS_PRODUCTS: { //Products
                titleToolbar.setText("Продукты");
                break;
            }
            case POS_ACCOUNT: { //Account
                titleToolbar.setText("Аккаунт");
                break;
            }
        }
        showFragment(fragment);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.greyColor))
                .withTextTint(color(R.color.greyColor))
                .withSelectedIconTint(color(R.color.primaryColor))
                .withSelectedTextTint(color(R.color.primaryColor));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}

package ddwu.mobile.finalproject.ma01_20180988;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment fragmentHome, fragmentRecipe, fragmentStore, fragmentMyPage, fragmentTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentHome = new FragmentHome();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragmentHome).commit();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_home);
        navView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        String str = getIntent().getStringExtra("notiTimer");
        if(str !=null)
        {
            if(str.equals("notiTimer"))
            {
                if (fragmentTimer == null) {
                    fragmentTimer = new FragmentTimer();
                    fragmentManager.beginTransaction().add(R.id.frameLayout, fragmentTimer).commit();
                }

                if (fragmentHome != null)  fragmentManager.beginTransaction().hide(fragmentHome).commit();
                if (fragmentStore != null)  fragmentManager.beginTransaction().hide(fragmentStore).commit();
                if (fragmentMyPage != null)  fragmentManager.beginTransaction().hide(fragmentMyPage).commit();
                if (fragmentRecipe != null)  fragmentManager.beginTransaction().hide(fragmentRecipe).commit();
                if (fragmentTimer != null)  fragmentManager.beginTransaction().show(fragmentTimer).setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commit();

                navView.setSelectedItemId(R.id.navigation_timer);
            }
        }
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId())
            {
                case R.id.navigation_home:
                    if (fragmentHome == null) {
                        fragmentHome = new FragmentHome();
                        fragmentManager.beginTransaction().add(R.id.frameLayout, fragmentHome).commit();
                    }

                    if (fragmentHome != null)  fragmentManager.beginTransaction().show(fragmentHome).setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commit();
                    if (fragmentStore != null)  fragmentManager.beginTransaction().hide(fragmentStore).commit();
                    if (fragmentMyPage != null)  fragmentManager.beginTransaction().hide(fragmentMyPage).commit();
                    if (fragmentRecipe != null)  fragmentManager.beginTransaction().hide(fragmentRecipe).commit();
                    if (fragmentTimer != null)  fragmentManager.beginTransaction().hide(fragmentTimer).commit();
                    break;
                case R.id.navigation_recipe:
                    if (fragmentRecipe == null) {
                        fragmentRecipe = new FragmentRecipe();
                        fragmentManager.beginTransaction().add(R.id.frameLayout, fragmentRecipe).commit();
                    }

                    if (fragmentHome != null)  fragmentManager.beginTransaction().hide(fragmentHome).commit();
                    if (fragmentStore != null) fragmentManager.beginTransaction().hide(fragmentStore).commit();
                    if (fragmentMyPage != null)  fragmentManager.beginTransaction().hide(fragmentMyPage).commit();
                    if (fragmentRecipe != null)  fragmentManager.beginTransaction().show(fragmentRecipe).setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commit();
                    if (fragmentTimer != null)  fragmentManager.beginTransaction().hide(fragmentTimer).commit();
                    break;
                case R.id.navigation_store:
                    fragmentStore = new FragmentStore();
                    fragmentManager.beginTransaction().add(R.id.frameLayout, fragmentStore).commit();
                    break;
                case R.id.navigation_my_page:
                    if (fragmentMyPage == null) {
                        fragmentMyPage = new FragmentMyPage();
                        fragmentManager.beginTransaction().add(R.id.frameLayout, fragmentMyPage).commit();
                    }

                    if (fragmentHome != null)  fragmentManager.beginTransaction().hide(fragmentHome).commit();
                    if (fragmentStore != null)  fragmentManager.beginTransaction().hide(fragmentStore).commit();
                    if (fragmentMyPage != null)  fragmentManager.beginTransaction().show(fragmentMyPage).setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commit();
                    if (fragmentRecipe != null)  fragmentManager.beginTransaction().hide(fragmentRecipe).commit();
                    if (fragmentTimer != null)  fragmentManager.beginTransaction().hide(fragmentTimer).commit();
                    break;
                case R.id.navigation_timer:
                    if (fragmentTimer == null) {
                        fragmentTimer = new FragmentTimer();
                        fragmentManager.beginTransaction().add(R.id.frameLayout, fragmentTimer).commit();
                    }

                    if (fragmentHome != null)  fragmentManager.beginTransaction().hide(fragmentHome).commit();
                    if (fragmentStore != null)  fragmentManager.beginTransaction().hide(fragmentStore).commit();
                    if (fragmentMyPage != null)  fragmentManager.beginTransaction().hide(fragmentMyPage).commit();
                    if (fragmentRecipe != null)  fragmentManager.beginTransaction().hide(fragmentRecipe).commit();
                    if (fragmentTimer != null)  fragmentManager.beginTransaction().show(fragmentTimer).setCustomAnimations(R.anim.fade_in, R.anim.fade_out).commit();
                    break;
            }
            return true;
        }
    }
}
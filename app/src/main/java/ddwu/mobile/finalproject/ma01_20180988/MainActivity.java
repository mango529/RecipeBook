package ddwu.mobile.finalproject.ma01_20180988;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ddwu.mobile.finalproject.R;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentHome fragmentHome = new FragmentHome();
    private FragmentRecipe fragmentRecipe = new FragmentRecipe();
    private FragmentMap fragmentMap = new FragmentMap();
    private FragmentMyPage fragmentMyPage = new FragmentMyPage();
    private FragmentTimer fragmentTimer = new FragmentTimer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_home);
        navView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(item.getItemId())
            {
                case R.id.navigation_home:
                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                            .replace(R.id.frameLayout, fragmentHome)
                                            .commitAllowingStateLoss();
                    break;
                case R.id.navigation_recipe:
                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                            .replace(R.id.frameLayout, fragmentRecipe)
                                            .commitAllowingStateLoss();
                    break;
                case R.id.navigation_map:
                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                            .replace(R.id.frameLayout, fragmentMap)
                                            .commitAllowingStateLoss();
                    break;
                case R.id.navigation_my_page:
                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                            .replace(R.id.frameLayout, fragmentMyPage)
                                            .commitAllowingStateLoss();
                    break;
                case R.id.navigation_timer:
                    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                                            .replace(R.id.frameLayout, fragmentTimer)
                                            .commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}
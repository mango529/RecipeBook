package ddwu.mobile.finalproject.ma01_20180988;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import noman.googleplaces.PlaceType;

public class NewRecipeActivity extends AppCompatActivity {
    private static final String TAG = "NewRecipeActivity";

    private ImageView ivNewRcpImg;
    private TextView tvNewStep;
    private EditText etNewRcpName, etNewRcpDate, etNewRcpMemo, etNewRcpHashtag;
    private RatingBar rbNewRating;
    private ImageButton btnNewIngredient, btnNewManual;
    private Button btnComNewRcp, btnCancelNewRcp;
    private ListView lvNewIngre;
    private ViewPager vpNewManual;
    private ConstraintLayout ingreDialog, manualDialog;

    private ArrayList<String> ingredients;
    private ArrayList<Manual> manuals;
    private ArrayAdapter ingredientAdapter;
    private ManualAdapter manualAdapter;
    private Calendar calendar;
    private String dateFormat;
    private SimpleDateFormat sdf;
    private TextView dialogTitle;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        calendar = Calendar.getInstance();
        dateFormat = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(dateFormat, Locale.KOREA);

        tvNewStep = findViewById(R.id.tvNewStep);
        ivNewRcpImg = findViewById(R.id.ivNewRcpImg);
        etNewRcpName = findViewById(R.id.etNewRcpName);
        etNewRcpDate = findViewById(R.id.etNewRcpDate);
        etNewRcpMemo = findViewById(R.id.etNewRcpMemo);
        etNewRcpHashtag = findViewById(R.id.etNewRcpHashtag);
        rbNewRating = findViewById(R.id.rbNewRating);
        btnNewIngredient = findViewById(R.id.btnNewIngredient);
        btnNewManual = findViewById(R.id.btnNewManual);
        btnComNewRcp = findViewById(R.id.btnComNewRcp);
        btnCancelNewRcp = findViewById(R.id.btnCancelNewRcp);
        lvNewIngre = findViewById(R.id.lvNewIngre);
        vpNewManual = findViewById(R.id.vpNewManual);

        ingreDialog = (ConstraintLayout) View.inflate(this, R.layout.dialog_add_ingredient, null);
        manualDialog = (ConstraintLayout) View.inflate(this, R.layout.dialog_add_manual, null);

        ingredients = new ArrayList<>();
        manuals = new ArrayList<>();
        ingredientAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredients);
        manualAdapter = new ManualAdapter(this, manuals);
        lvNewIngre.setAdapter(ingredientAdapter);
        vpNewManual.setAdapter(manualAdapter);
        vpNewManual.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tvNewStep.setText(String.valueOf(manuals.get(position).getStep()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        etNewRcpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(NewRecipeActivity.this, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            etNewRcpDate.setText(sdf.format(calendar.getTime()));
        }
    };


    public void onClick(View v) {
        dialogTitle = new TextView(this);
        dialogTitle.setIncludeFontPadding(false);
        dialogTitle.setTypeface(ResourcesCompat.getFont(this, R.font.notosanskr_medium));
        dialogTitle.setGravity(Gravity.CENTER);
        dialogTitle.setPadding(10, 70, 10, 70);
        dialogTitle.setTextSize(20F);
        dialogTitle.setBackgroundResource(R.color.pink_100);

        switch (v.getId()) {
            case R.id.btnNewIngredient:
                dialogTitle.setText("재료 추가하기");
                if (ingreDialog.getParent() != null)
                    ((ViewGroup) ingreDialog.getParent()).removeView(ingreDialog);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCustomTitle(dialogTitle)
                        .setView(ingreDialog)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText etNewIngredient = ingreDialog.findViewById(R.id.etNewIngredient);
                                ingredients.add(etNewIngredient.getText().toString());
                                ingredientAdapter.notifyDataSetChanged();
                                etNewIngredient.setText("");
                            }
                        }).setNegativeButton("취소", null)
                        .show();
                break;
            case R.id.btnNewManual:
                dialogTitle.setText("방법 추가하기");
                if (manualDialog.getParent() != null)
                    ((ViewGroup) manualDialog.getParent()).removeView(manualDialog);
                builder = new AlertDialog.Builder(this);
                builder.setCustomTitle(dialogTitle)
                        .setView(manualDialog)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText etNewStep = manualDialog.findViewById(R.id.etNewStep);
                                EditText etNewManual = manualDialog.findViewById(R.id.etNewManual);
                                ImageView ivNewManualImg = manualDialog.findViewById(R.id.ivNewManualImg);

                                if (stepIsInManual(Integer.parseInt(etNewStep.getText().toString()))) {
                                    Toast.makeText(NewRecipeActivity.this, "이미 입력된 단계입니다!", Toast.LENGTH_SHORT).show();
                                    etNewStep.setText("");
                                    etNewManual.setText("");
                                    return;
                                }
                                else {
                                    manuals.add(new Manual(Integer.parseInt(etNewStep.getText().toString()), etNewManual.getText().toString(), null));
                                    Collections.sort(manuals,new ManualComparator());
                                    manualAdapter.notifyDataSetChanged();
                                    tvNewStep.setText(String.valueOf(manuals.get(0).getStep()));
                                    etNewStep.setText("");
                                    etNewManual.setText("");
                                }
                            }
                        }).setNegativeButton("취소", null)
                        .show();
                break;
                case R.id.btnComNewRcp:
                    Recipe recipe = new Recipe();

                break;
            case R.id.btnCancelNewRcp:
                finish();
                break;
        }
    }

    public boolean stepIsInManual(int s) {
        for (Manual m : manuals) {
            if (m.getStep() == s) return true;
        }
        return false;
    }

    class ManualComparator implements Comparator<Manual> {
        @Override
        public int compare(Manual a,Manual b){
            if(a.getStep()>b.getStep()) return 1;
            if(a.getStep()<b.getStep()) return -1;
            return 0;
        }
    }
}

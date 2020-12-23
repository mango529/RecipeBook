package ddwu.mobile.finalproject.ma01_20180988;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class NewRecipeActivity extends AppCompatActivity {
    private static final String TAG = "NewRecipeActivity";
    final static int PERMISSION_REQ_CODE = 100;
    private static final int RECIPE_PICK_FROM_ALBUM = 1;
    private static final int MANUAL_PICK_FROM_ALBUM = 2;

    private ImageView ivNewRcpImg;
    private TextView tvNewStep;
    private EditText etNewRcpName, etNewRcpDate, etNewRcpMemo, etNewRcpHashtag;
    private RatingBar rbNewRating;
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
    private File tempFile;
    private ImageView ivNewManualImg;
    private String selectedRcpImg;
    private int selManual;

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
        lvNewIngre = findViewById(R.id.lvNewIngre);
        vpNewManual = findViewById(R.id.vpNewManual);

        ingreDialog = (ConstraintLayout) View.inflate(this, R.layout.dialog_add_ingredient, null);
        manualDialog = (ConstraintLayout) View.inflate(this, R.layout.dialog_add_manual, null);
        ivNewManualImg = manualDialog.findViewById(R.id.ivNewManualImg);
        ivNewManualImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    goToAlbum(MANUAL_PICK_FROM_ALBUM);
                }
            }
        });

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
                selManual = position;
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

        ivNewRcpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    goToAlbum(RECIPE_PICK_FROM_ALBUM);
                }
            }
        });

        lvNewIngre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView dialogTitle = new TextView(NewRecipeActivity.this);
                dialogTitle.setIncludeFontPadding(false);
                dialogTitle.setText("재료 삭제");
                dialogTitle.setTypeface(ResourcesCompat.getFont(NewRecipeActivity.this, R.font.notosanskr_medium));
                dialogTitle.setGravity(Gravity.CENTER);
                dialogTitle.setPadding(10, 70, 10, 70);
                dialogTitle.setTextSize(20F);
                dialogTitle.setBackgroundResource(R.color.pink_100);
                AlertDialog.Builder builder = new AlertDialog.Builder(NewRecipeActivity.this);
                builder.setCustomTitle(dialogTitle)
                        .setMessage(ingredients.get(position) + "를 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ingredients.remove(position);
                                ingredientAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("취소", null)
                        .show();
            }
        });
    }

    private void goToAlbum(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, requestCode);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(tempFile != null) {
            deleteFile();
        }
    }

    public void onClick(View v) {
        dialogTitle = new TextView(this);
        dialogTitle.setIncludeFontPadding(false);
        dialogTitle.setTypeface(ResourcesCompat.getFont(this, R.font.notosanskr_medium));
        dialogTitle.setGravity(Gravity.CENTER);
        dialogTitle.setPadding(10, 70, 10, 70);
        dialogTitle.setTextSize(20F);
        dialogTitle.setBackgroundResource(R.color.pink_100);
        builder = new AlertDialog.Builder(this);

        switch (v.getId()) {
            case R.id.btnDeleteNewManual:
                if (manuals.size() == 0) return;
                dialogTitle.setText("방법 삭제");
                builder.setCustomTitle(dialogTitle)
                        .setMessage(manuals.get(selManual).getStep() + " 단계를 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                manuals.remove(selManual);
                                manualAdapter.notifyDataSetChanged();
                                if (selManual == 0) tvNewStep.setText(String.valueOf(manuals.get(0).getStep()));
                                else tvNewStep.setText(String.valueOf(manuals.get(selManual).getStep()));
                            }
                        }).setNegativeButton("취소", null)
                        .show();
                break;
            case R.id.btnNewIngredient:
                dialogTitle.setText("재료 추가하기");
                if (ingreDialog.getParent() != null)
                    ((ViewGroup) ingreDialog.getParent()).removeView(ingreDialog);
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
                builder.setCustomTitle(dialogTitle)
                        .setView(manualDialog)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(tempFile != null) {
                                    deleteFile();
                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new CustomListener(alertDialog));
                break;
                case R.id.btnComNewRcp:
                    Recipe recipe = new Recipe();
                    if (etNewRcpName.getText() == null || etNewRcpName.getText().toString().replace(" ", "").equals("") || selectedRcpImg == null) {
                        Toast.makeText(this, "사진과 요리 이름은 필수 입력 항목입니다!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (ingredients.size() == 0) {
                        Toast.makeText(this, "재료를 하나 이상 입력하세요!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (manuals.size() == 0) {
                        Toast.makeText(this, "방법을 하나 이상 입력하세요!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    recipe.setImageLink(selectedRcpImg);
                    recipe.setRating(rbNewRating.getRating());
                    recipe.setDate(etNewRcpDate.getText().toString());
                    recipe.setName(etNewRcpName.getText().toString());
                    recipe.getIngredients().addAll(ingredients);
                    recipe.getManuals().addAll(manuals);
                    if (!etNewRcpMemo.getText().toString().isEmpty()) {
                        recipe.setMemo(etNewRcpMemo.getText().toString());
                    }
                    if (!etNewRcpHashtag.getText().toString().isEmpty()) {
                        if (etNewRcpHashtag.getText().toString().contains("#")) {
                            recipe.setHashtag(etNewRcpHashtag.getText().toString().substring(1));
                        }
                        else {
                            recipe.setHashtag(etNewRcpHashtag.getText().toString());
                        }
                    }

                    RecipeDBManager manager = new RecipeDBManager(this);
                    manager.addNewRecipe(recipe);
                    finish();
                break;
            case R.id.btnCancelNewRcp:
                if(tempFile != null) {
                    deleteFile();
                }
                finish();
                break;
        }
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            EditText etNewStep = dialog.findViewById(R.id.etNewStep);
            EditText etNewManual = dialog.findViewById(R.id.etNewManual);
            ivNewManualImg = dialog.findViewById(R.id.ivNewManualImg);

            if (etNewStep.getText().toString().isEmpty() || etNewManual.getText().toString().isEmpty()) {
                Toast.makeText(NewRecipeActivity.this, "단계와 설명을 입력하세요!", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                if (stepIsInManual(Integer.parseInt(etNewStep.getText().toString()))) {
                    Toast.makeText(NewRecipeActivity.this, "이미 입력된 단계입니다!", Toast.LENGTH_SHORT).show();
                    etNewStep.setText("");
                    return;
                }
                else {
                    String content = etNewManual.getText().toString();
                    content = content.replace("\n", "");
                    if (tempFile != null) {
                        manuals.add(new Manual(Integer.parseInt(etNewStep.getText().toString()), content, tempFile.getAbsolutePath()));
                        deleteFile();
                    }
                    else {
                        manuals.add(new Manual(Integer.parseInt(etNewStep.getText().toString()), content, null));
                    }
                    Collections.sort(manuals,new ManualComparator());
                    tvNewStep.setText(String.valueOf(manuals.get(0).getStep()));
                    etNewStep.setText("");
                    etNewManual.setText("");
                    ivNewManualImg.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
                    dialog.dismiss();
                    manualAdapter.notifyDataSetChanged();
                }
            }
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

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "앱 실행을 위해 권한 허용이 필요함", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                deleteFile();
            }
            return;
        }
        if (requestCode == RECIPE_PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();
            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            selectedRcpImg = tempFile.getAbsolutePath();
            setPic(ivNewRcpImg, tempFile.getAbsolutePath());
        } else if (requestCode == MANUAL_PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();
            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setPic(ivNewManualImg, tempFile.getAbsolutePath());
        }

    }

    private void setPic(ImageView view, String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(path, options);
        view.setImageBitmap(originalBm);
    }

    private void deleteFile() {
        if (tempFile.exists()) {
            tempFile.delete();
            tempFile = null;
        }
    }
}

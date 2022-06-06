package com.example.dairys.Fragment;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.UI_MODE_SERVICE;

import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dairys.Access.LoginActivity;
import com.example.dairys.Database.AppDatabase;
import com.example.dairys.R;
import com.example.dairys.Fragment.StatisticsFragment;
import com.google.gson.Gson;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    Switch darkMode;
    ImageView buttonStyle;
    TextView fontStyleToModify;
    LinearLayout layoutLanguage;
    LinearLayout layoutChartColor;
    AppDatabase db;
    LinearLayout layoutLogout;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getInstance(getContext());

        darkMode = getView().findViewById(R.id.switchDarkMode);
        buttonStyle = getView().findViewById(R.id.buttonStyle);
        fontStyleToModify = getView().findViewById(R.id.fontStyleToModify);
        layoutLanguage = getView().findViewById(R.id.layoutLanguage);
        layoutLogout = getView().findViewById(R.id.layoutLogout);
        layoutChartColor = getView().findViewById(R.id.layoutChartColor);

        if(getIfIsInDarkMode(getContext())){
            darkMode.setChecked(true);
        }

        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                writeDarkMode(b);
            }
        });

        buttonStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getActivity(), buttonStyle);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.note_style_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Typeface typeface = null;
                        switch (item.getItemId()){
                            case R.id.baby_dolf:
                                typeface = ResourcesCompat.getFont(getContext(), R.font.baby_doll);
                                writeFontStyle(R.font.baby_doll);
                                break;
                            case R.id.dudu:
                                typeface = ResourcesCompat.getFont(getContext(), R.font.duducalligraphy);
                                writeFontStyle(R.font.duducalligraphy);
                                break;
                            case R.id.kg:
                                typeface = ResourcesCompat.getFont(getContext(), R.font.kg_starting_over);
                                writeFontStyle(R.font.kg_starting_over);
                                break;
                            case R.id.defaultFont:
                                typeface = ResourcesCompat.getFont(getContext(), R.font.sans_serif);
                                writeFontStyle(typeface.getStyle());
                                break;
                        }
                        fontStyleToModify.setTypeface(typeface);
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        layoutLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.radio_language);
                dialog.show();

                RadioGroup radioGroup = dialog.findViewById(R.id.radioGroupLanguage);
                TextView save = dialog.findViewById(R.id.saveLanguage);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selectedId = radioGroup.getCheckedRadioButtonId();

                        switch (selectedId){
                            case R.id.italian:
                                writeLanguage("it");
                                break;
                            case R.id.english:
                                writeLanguage("en");
                                break;
                        }
                        Toast.makeText(getContext(), R.string.restart, Toast.LENGTH_SHORT).show();
                        setLocale(getContext());
                        dialog.dismiss();
                    }

                });
            }
        });

        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.userDao().setAllNotLogged();
                goToLogin();
            }
        });

        layoutChartColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.chart_color);
                dialog.show();

                LinearLayout vordiplom = dialog.findViewById(R.id.vordiplom);
                LinearLayout material = dialog.findViewById(R.id.material);
                LinearLayout liberty = dialog.findViewById(R.id.liberty);
                LinearLayout colorful = dialog.findViewById(R.id.colorful);
                LinearLayout pastel = dialog.findViewById(R.id.pastel);
                LinearLayout joyful = dialog.findViewById(R.id.joyful);

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()){
                            case R.id.vordiplom:
                                writeColorChart(StatisticsFragment.ColorChart.VORDIPLOM);
                                break;
                            case R.id.material:
                                writeColorChart(StatisticsFragment.ColorChart.MATERIAL);
                                break;
                            case R.id.liberty:
                                writeColorChart(StatisticsFragment.ColorChart.LIBERTY);
                                break;
                            case R.id.colorful:
                                writeColorChart(StatisticsFragment.ColorChart.COLORFUL);
                                break;
                            case R.id.pastel:
                                writeColorChart(StatisticsFragment.ColorChart.PASTEL);
                                break;
                            case R.id.joyful:
                                writeColorChart(StatisticsFragment.ColorChart.JOYFUL);
                                break;
                        }
                        dialog.dismiss();
                    }
                };

                vordiplom.setOnClickListener(listener);
                material.setOnClickListener(listener);
                liberty.setOnClickListener(listener);
                pastel.setOnClickListener(listener);
                joyful.setOnClickListener(listener);
                colorful.setOnClickListener(listener);
            }
        });
    }

    private void writeDarkMode(boolean isOnDarkMode){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putBoolean("darkMode", isOnDarkMode);
        myEdit.commit();
    }

    private void writeColorChart(StatisticsFragment.ColorChart color){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(color);

        myEdit.putString("colorChart", json);
        myEdit.commit();
    }

    private void writeFontStyle(int fontStyle){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putInt("fontStyle", fontStyle);
        myEdit.commit();
    }

    private void writeLanguage(String language){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("language", language);
        myEdit.commit();
    }

    public static boolean getIfIsInDarkMode(Context context){
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

        return sh.getBoolean("darkMode", false);
    }

    public static int readFontStyle(Context context){
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

        return sh.getInt("fontStyle", 0);
    }

    public static String readLanguage(Context context){
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

        return sh.getString("language", context.getResources().getConfiguration().locale.getCountry());
    }

    public static StatisticsFragment.ColorChart readColor(Context context){
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sh.getString("colorChart", "");
        return gson.fromJson(json, StatisticsFragment.ColorChart.class);
    }

    public static void setLocale(Context context){
        Resources resources = context.getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        Configuration configuration = resources.getConfiguration();


        configuration.locale = new Locale(readLanguage(context));
        resources.updateConfiguration(configuration, metrics);
    }

    private void goToLogin(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
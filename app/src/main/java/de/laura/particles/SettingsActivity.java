package de.laura.particles;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    EditText etparticles, etspace, etants, etantsteps;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etparticles = findViewById(R.id.settings_etparticles);
        etparticles.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = 100000;
                try {
                    i = Integer.parseInt(s.toString());
                    assert i != 0;
                } catch (NumberFormatException | AssertionError ignored) {
                    Toast.makeText(SettingsActivity.this, "Invalid number: " + s, Toast.LENGTH_SHORT).show();
                    etparticles.setText("100000");
                }

                getSharedPreferences("settings", MODE_PRIVATE).edit().putInt("particle_count", i).apply();
            }
        });
        etparticles.setText(String.valueOf(getSharedPreferences("settings", MODE_PRIVATE).getInt("particle_count", 100000)));

        etspace = findViewById(R.id.settings_etspace);
        etspace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = 1;
                try {
                    i = Integer.parseInt(s.toString());
                    assert i != 0;
                } catch (NumberFormatException | AssertionError ignored) {
                    Toast.makeText(SettingsActivity.this, "Invalid number: " + s, Toast.LENGTH_SHORT).show();
                    etspace.setText("1");
                }

                getSharedPreferences("settings", MODE_PRIVATE).edit().putInt("grid_space", i).apply();
            }
        });
        etspace.setText(String.valueOf(getSharedPreferences("settings", MODE_PRIVATE).getInt("grid_space", 1)));

        etants = findViewById(R.id.settings_etants);
        etants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = 1;
                try {
                    i = Integer.parseInt(s.toString());
                    assert i != 0;
                } catch (NumberFormatException | AssertionError ignored) {
                    Toast.makeText(SettingsActivity.this, "Invalid number: " + s, Toast.LENGTH_SHORT).show();
                    etants.setText("1");
                }

                getSharedPreferences("settings", MODE_PRIVATE).edit().putInt("ant_count", i).apply();
            }
        });
        etants.setText(String.valueOf(getSharedPreferences("settings", MODE_PRIVATE).getInt("ant_count", 100)));

        etantsteps = findViewById(R.id.settings_etantsteps);
        etantsteps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = 1;
                try {
                    i = Integer.parseInt(s.toString());
                    assert i != 0;
                } catch (NumberFormatException | AssertionError ignored) {
                    Toast.makeText(SettingsActivity.this, "Invalid number: " + s, Toast.LENGTH_SHORT).show();
                    etantsteps.setText("1");
                }

                getSharedPreferences("settings", MODE_PRIVATE).edit().putInt("ant_steps", i).apply();
            }
        });
        etantsteps.setText(String.valueOf(getSharedPreferences("settings", MODE_PRIVATE).getInt("ant_steps", 1000)));
    }
}
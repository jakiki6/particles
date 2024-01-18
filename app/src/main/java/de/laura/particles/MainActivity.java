package de.laura.particles;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btparticles, btsettings, btgrid, btant;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.loadLibrary("native");

        btparticles = findViewById(R.id.main_btparticles);
        btsettings = findViewById(R.id.main_btsettings);
        btgrid = findViewById(R.id.main_btgrid);
        btant = findViewById(R.id.main_btant);

        btparticles.setOnClickListener(this);
        btsettings.setOnClickListener(this);
        btgrid.setOnClickListener(this);
        btant.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_btparticles) {
            Intent i = new Intent(this, ParticleActivity.class);
            startActivity(i);
        } else if (v.getId() == R.id.main_btsettings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        } else if (v.getId() == R.id.main_btgrid) {
            Intent i = new Intent(this, GridActivity.class);
            startActivity(i);
        } else if (v.getId() == R.id.main_btant) {
            Intent i = new Intent(this, AntActivity.class);
            startActivity(i);
        }
    }
}
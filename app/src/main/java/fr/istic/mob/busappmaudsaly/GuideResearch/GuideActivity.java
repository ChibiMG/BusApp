package fr.istic.mob.busappmaudsaly.GuideResearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import fr.istic.mob.busappmaudsaly.R;

import android.os.Bundle;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment1 fragment1 = new Fragment1();
        fragmentTransaction.add(R.id.fragment_frame, fragment1);
        fragmentTransaction.commit();

    }
}

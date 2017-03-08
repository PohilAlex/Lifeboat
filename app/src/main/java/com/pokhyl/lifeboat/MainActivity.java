package com.pokhyl.lifeboat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pokhyl.lifeboat.SettingsFragment.GameSettings;
import com.pokhyl.lifeboat.model.Person;
import com.pokhyl.lifeboat.model.PersonRelation;
import com.pokhyl.lifeboat.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final int SETTINGS_REQUEST_CODE = 1;

    private Map<Person, PersonRelation> relationMap;
    private RelationStorage relationStorage;
    private PersonAdapter personAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        relationStorage = new RelationStorage(this);
        relationMap = relationStorage.getRelations();
        if (relationMap == null) {
            generateNewGame(GameSettings.builder()
                    .playerNumber(6)
                    .useRandom(true)
                    .build());
        } else {
            personAdapter.setData(relationMap);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_refresh:
                /*relationMap = RelationGenerator.generate(personList);
                relationStorage.storeRelation(relationMap);*/
                return true;
            case R.id.menu_item_settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_REQUEST_CODE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            GameSettings settings = data.getParcelableExtra(SettingsFragment.EXTRA_GAME_SETTING);
            if (settings != null) {
                generateNewGame(settings);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.person_grid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        personAdapter = new PersonAdapter();
        recyclerView.setAdapter(personAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PersonRelation relation = relationMap.get(personAdapter.getItem(position));
                startActivity(RelationActivity.createIntent(MainActivity.this, relation));
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    private void generateNewGame(@NonNull GameSettings settings) {
        List<Person> newPersonList = new ArrayList<>();
        if (settings.useRandom()) {
            Random random = new Random();
            ArrayList<Person> persons = new ArrayList<>(Arrays.asList(Person.values()));
            for (int i = 0; i < settings.playerNumber(); i++) {
                newPersonList.add(persons.remove(random.nextInt(persons.size())));
            }
        } else {
            newPersonList = settings.personList();
        }

        relationMap = RelationGenerator.generate(newPersonList);
        relationStorage.storeRelation(relationMap);
        personAdapter.setData(relationMap);
    }



}
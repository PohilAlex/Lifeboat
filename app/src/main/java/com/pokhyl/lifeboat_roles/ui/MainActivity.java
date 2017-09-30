package com.pokhyl.lifeboat_roles.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.pokhyl.lifeboat_roles.R;
import com.pokhyl.lifeboat_roles.RelationGenerator;
import com.pokhyl.lifeboat_roles.model.GameSettings;
import com.pokhyl.lifeboat_roles.model.Person;
import com.pokhyl.lifeboat_roles.model.PersonRelation;
import com.pokhyl.lifeboat_roles.storage.RelationStorage;
import com.pokhyl.lifeboat_roles.storage.SettingsStorage;
import com.pokhyl.lifeboat_roles.utils.RecyclerItemClickListener;

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
    private ShowcaseMonitor showcaseMonitor;
    private SettingsStorage settingsStorage;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.title_main_activity);

        initViews();

        relationStorage = new RelationStorage(this);
        relationMap = relationStorage.getRelations();
        if (relationMap == null) {
            generateNewGame(GameSettings.builder().build());
        } else {
            personAdapter.setData(relationMap);
        }
        showcaseMonitor = new ShowcaseMonitor(this);
        settingsStorage = new SettingsStorage(this);

        showUserTips();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new_game:
                startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_REQUEST_CODE);
                showcaseMonitor.hideSettingsTips();
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
        recyclerView = (RecyclerView) findViewById(R.id.person_grid);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (personAdapter.getItemCount() % 2 == 1 && position == personAdapter.getItemCount() - 1) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        personAdapter = new PersonAdapter();
        recyclerView.setAdapter(personAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PersonRelation relation = relationMap.get(personAdapter.getItem(position));
                startActivity(RelationActivity.createIntent(MainActivity.this, relation));
                showcaseMonitor.hideRoleTips();
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

    private void showUserTips() {
        switch (settingsStorage.getAppTourProgress()) {
            case NOT_SHOW_YET:
                showcaseMonitor.showIntoTips();
                break;
            case ROLE_TIPS_SHOWN:
                showcaseMonitor.showSettingsTips();
                break;
        }
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (settingsStorage.getAppTourProgress() == ShowcaseMonitor.AppTourProgress.INTRO_SHOWN) {
                    showcaseMonitor.showRoleTips();
                }
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

}
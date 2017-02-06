package com.pokhyl.lifeboat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pokhyl.lifeboat.model.Person;
import com.pokhyl.lifeboat.model.PersonRelation;
import com.pokhyl.lifeboat.utils.RecyclerItemClickListener;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Map<Person, PersonRelation> relationMap;
    List<Person> personList = Arrays.asList(Person.values());
    private RelationStorage relationStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relationStorage = new RelationStorage(this);
        relationMap = relationStorage.getRelations();
        if (relationMap == null) {
            relationMap = RelationGenerator.generate(personList);
            relationStorage.storeRelation(relationMap);
        }
        initViews();
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
                relationMap = RelationGenerator.generate(personList);
                relationStorage.storeRelation(relationMap);
                return true;
            case R.id.menu_item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.person_grid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        PersonAdapter personAdapter = new PersonAdapter(personList);
        recyclerView.setAdapter(personAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PersonRelation relation = relationMap.get(personList.get(position));
                startActivity(RelationActivity.createIntent(MainActivity.this, relation));
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

}

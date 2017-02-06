package com.pokhyl.lifeboat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.pokhyl.lifeboat.model.PersonRelation;

public class RelationActivity extends Activity {

    private static final String TAG_RELATION = "TAG_RELATION";

    public static Intent createIntent(Context context, PersonRelation relation) {
        Intent intent = new Intent(context, RelationActivity.class);
        intent.putExtra(TAG_RELATION, relation);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PersonRelation relation = getIntent().getParcelableExtra(TAG_RELATION);
        setContentView(R.layout.activity_relation);
        ImageView friendImage = (ImageView) findViewById(R.id.img_friend);
        ImageView enemyImage = (ImageView) findViewById(R.id.img_enemy);

        friendImage.setImageResource(PersonResourceManager.getImage(relation.friend()));
        enemyImage.setImageResource(PersonResourceManager.getImage(relation.enemy()));

    }
}

package com.pokhyl.lifeboat.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pokhyl.lifeboat.PersonResourceManager;
import com.pokhyl.lifeboat.R;
import com.pokhyl.lifeboat.model.Person;
import com.pokhyl.lifeboat.model.PersonRelation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {


    private final List<Person> personList;

    public PersonAdapter() {
        personList = new ArrayList<>();
    }

    public PersonAdapter(Map<Person, PersonRelation> relationMap) {
        this.personList = getPersonForDisplay(relationMap);
    }

    public void setData(Map<Person, PersonRelation> relationMap) {
        this.personList.clear();
        this.personList.addAll(getPersonForDisplay(relationMap));
        notifyDataSetChanged();
    }

    public Person getItem(int position) {
        return personList.get(position);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        Person person = personList.get(position);
        holder.icon.setImageResource(PersonResourceManager.getImage(person));
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    private List<Person> getPersonForDisplay(Map<Person, PersonRelation> relationMap) {
        List<Person> persons = new ArrayList<>(relationMap.keySet());
        Collections.sort(persons, (o1, o2) -> o1.strength - o2.strength);
        return persons;
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;

        public PersonViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.person_item_img);
        }
    }

}

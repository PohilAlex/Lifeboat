package com.pokhyl.lifeboat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pokhyl.lifeboat.model.Person;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    final List<Person> personList;

    public PersonAdapter(List<Person> personList) {
        this.personList = personList;
    }

    public void setData(List<Person> personList) {
        this.personList.clear();
        this.personList.addAll(personList);
        notifyDataSetChanged();
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

    static class PersonViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;

        public PersonViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.person_item_img);
        }
    }

}

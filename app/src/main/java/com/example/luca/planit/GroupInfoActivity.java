package com.example.luca.planit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class GroupInfoActivity extends AppCompatActivity {
    private TextView groupEvent;
    private TextView numNumbers;
    private List<String> dataset = new LinkedList<>();
    private ArrayAdapter<String> adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        listView = (ListView) findViewById(R.id.people_list);
        numNumbers = (TextView) findViewById(R.id.num_invited);
        int num = SelectedGroup.getSelectedGroup().getPeopleInGroup().size();
        if(num >1){
            numNumbers.setText(num+" members");
        }else {
            numNumbers.setText("You are alone!");
        }
        for(Person person : SelectedGroup.getSelectedGroup().getPeopleInGroup()){
            dataset.add(person.getName()+ " "+person.getSurname());
        }

        adapter = new ArrayAdapter<>(this,R.layout.person_in_group_item,R.id.person_text_view,dataset);

        listView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        groupEvent = (TextView) findViewById(R.id.group_name);
        this.groupEvent.setText(SelectedGroup.getSelectedGroup().getNameGroup());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Invite to group", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}

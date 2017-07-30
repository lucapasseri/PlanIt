package com.example.luca.planit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = (ListView) findViewById(R.id.people_list);
        numNumbers = (TextView) findViewById(R.id.num_invited);
        int num = SelectedGroup.getSelectedGroup().getPeopleInGroup().size();
        if(num >1){
            numNumbers.setText(num+" "+getString(R.string.member));
        }else {
            numNumbers.setText(R.string.alone);
        }
        for(Person person : SelectedGroup.getSelectedGroup().getPeopleInGroup()){
            dataset.add(person.getName()+ " "+person.getSurname());
        }

        adapter = new ArrayAdapter<>(this,R.layout.person_in_group_item,R.id.person_text_view,dataset);

        listView.setAdapter(adapter);
        groupEvent = (TextView) findViewById(R.id.group_name);
        this.groupEvent.setText(SelectedGroup.getSelectedGroup().getNameGroup());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog custom = new CustomDialog(GroupInfoActivity.this);
                custom.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent intent = new Intent(GroupInfoActivity.this, GroupActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        if(SelectedInvite.getSelectedInvite()!= null){
                            String toPass = SelectedInvite.getSelectedInvite().isMailGroupWrapper()?
                                    SelectedInvite.getSelectedInvite().getEmail():SelectedInvite.getSelectedInvite().getUsername();
                            intent.putExtra("TASK",getString(R.string.invite_to)+" "+toPass+" "+getString(R.string.sended));
                            GroupInfoActivity.this.startActivity(intent);
                        }

                    }
                });
                custom.show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

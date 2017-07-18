package com.donga.examples.boomin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.donga.examples.boomin.R;
import com.donga.examples.boomin.Singleton.ProSingleton;
import com.donga.examples.boomin.listviewAdapter.ProSubListViewAdapter;
import com.donga.examples.boomin.retrofit.retrofitProfessor.Professor;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rhfoq on 2017-02-17.
 */
public class ProSubActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar_prosub)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout_prosub)
    DrawerLayout drawer;
    @BindView(R.id.nav_view_prosub)
    NavigationView navigationView;
    @BindView(R.id.list_prosub)
    ListView listView;
    @BindView(R.id.pro_sub)
    TextView pro_sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prosub);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Intent i1 = getIntent();

        navigationView.setNavigationItemSelectedListener(this);
        pro_sub.setText(i1.getStringExtra("name"));

        ProSubListViewAdapter adapter = new ProSubListViewAdapter();
        listView.setAdapter(adapter);

        ArrayList<Professor> professorArrayList = ProSingleton.getInstance().getProfessorArrayList();

        for (int i = 0; i < professorArrayList.size(); i++) {
            if (professorArrayList.get(i).getEmail().equals(".")) {
                if(professorArrayList.get(i).getTel().equals(".")){
                    // 둘 다 없을 때
                    adapter.addItem(professorArrayList.get(i).getName(), professorArrayList.get(i).getMajor(),
                            getResources().getDrawable(R.drawable.call_gone), getResources().getDrawable(R.drawable.phone_gone),
                            professorArrayList.get(i).getTel(), professorArrayList.get(i).getEmail());
                }else{
                    // 이메일은 없고 전화번호가 있을 때
                    adapter.addItem(professorArrayList.get(i).getName(), professorArrayList.get(i).getMajor(),
                            getResources().getDrawable(R.drawable.call), getResources().getDrawable(R.drawable.phone_gone),
                            professorArrayList.get(i).getTel(), professorArrayList.get(i).getEmail());
                }
            } else if (professorArrayList.get(i).getTel().equals(".")) {
                if(professorArrayList.get(i).getEmail().equals(".")){
                    // 둘 다 없을 때
                    adapter.addItem(professorArrayList.get(i).getName(), professorArrayList.get(i).getMajor(),
                            getResources().getDrawable(R.drawable.call_gone), getResources().getDrawable(R.drawable.phone_gone),
                            professorArrayList.get(i).getTel(), professorArrayList.get(i).getEmail());
                }else{
                    // 전화번호는 없고 이메일이 있을 때
                    adapter.addItem(professorArrayList.get(i).getName(), professorArrayList.get(i).getMajor(),
                            getResources().getDrawable(R.drawable.call_gone), getResources().getDrawable(R.drawable.tomail),
                            professorArrayList.get(i).getTel(), professorArrayList.get(i).getEmail());
                }
            } else {
                // 둘 다 있을 때
                adapter.addItem(professorArrayList.get(i).getName(), professorArrayList.get(i).getMajor(),
                        getResources().getDrawable(R.drawable.call), getResources().getDrawable(R.drawable.tomail),
                        professorArrayList.get(i).getTel(), professorArrayList.get(i).getEmail());
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_prosub);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(getBaseContext(), ProActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_res) {
            Intent intent = new Intent(getApplicationContext(), ResKActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_room) {
            Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pro) {
            Intent intent = new Intent(getApplicationContext(), ProActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_stu) {
            Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_empty) {
            Intent intent = new Intent(getApplicationContext(), EmptyActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_wisper) {
            Intent intent = new Intent(getApplicationContext(), WisperActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_site) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.donga.ac.kr"));
            startActivity(intent);
        } else if (id == R.id.nav_noti) {
            Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.SFLAG), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(getApplicationContext(), ManageLoginActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_prosub);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

package com.donga.examples.boomin.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.donga.examples.boomin.AppendLog;
import com.donga.examples.boomin.R;
import com.donga.examples.boomin.listviewAdapter.EmptyListViewAdapter;
import com.donga.examples.boomin.retrofit.retrofitEmpty.Empty;
import com.donga.examples.boomin.retrofit.retrofitEmpty.Interface_Empty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rhfoq on 2017-02-09.
 */
public class EmptyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressDialog mProgressDialog;
    AppendLog log = new AppendLog();

    BottomSheetBehavior behavior;
    EmptyListViewAdapter adapter;

    @BindView(R.id.toolbar_empty)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout_empty)
    DrawerLayout drawer;
    @BindView(R.id.nav_view_empty)
    NavigationView navigationView;
    @BindView(R.id.fab_empty)
    FloatingActionButton fab;
    @BindView(R.id.list_empty)
    ListView listview;
    @BindView(R.id.bottom)
    GridLayout bottom;

    @BindView(R.id.empty_day)
    Spinner empty_day;
    @BindView(R.id.empty_clock1)
    Spinner empty_clock1;
    @BindView(R.id.empty_clock2)
    Spinner empty_clock2;
    @BindView(R.id.bottom_button2)
    Button bottom_button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        List<String> empty_day_list = Arrays.asList(getResources().getStringArray(R.array.empty_day));
        final ArrayList<String> empty_clock_list = new ArrayList<>();
        for(int i = 3; i<=20; i++){
            empty_clock_list.add(String.valueOf(i));
        }
        String[] empty_array = getResources().getStringArray(R.array.empty_clock);
        final ArrayList<String> empty_array2 = new ArrayList(Arrays.asList(empty_array));
        HashMap<ArrayList<String>, ArrayList<String>> empty_hash = new HashMap<>();
        empty_hash.put(empty_clock_list, empty_array2);

        ArrayAdapter<String> empty_day_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,
                empty_day_list);
        ArrayAdapter<String> empty_clock_adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,
                empty_array2);
        empty_day.setAdapter(empty_day_adapter);
        empty_clock1.setAdapter(empty_clock_adapter);
        empty_clock2.setAdapter(empty_clock_adapter);

        empty_clock1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> empty_clock2_list = new ArrayList<String>();
                int selected = Integer.parseInt(parent.getSelectedItem().toString().split("\\(")[0]);
                for (int i = selected; i <= 20; i++) {
                    empty_clock2_list.add(empty_array2.get(i-3));
                }
                ArrayAdapter<String> empty_clock2_adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.spinner_item, empty_clock2_list);
                empty_clock2.setAdapter(empty_clock2_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        bottom = (GridLayout) findViewById(R.id.bottom);
        behavior = BottomSheetBehavior.from(bottom);
        behavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250.f, getResources().getDisplayMetrics()));
        behavior.setHideable(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    fab.setImageResource(R.drawable.bo);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    fab.setImageResource(R.drawable.uup);
                }
            }
        });
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c1e1eb")));
    }

    @OnClick(R.id.fab_empty)
    void onFab_EmptyClicked() {
        if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @OnClick(R.id.bottom_button2)
    void onBottomButtonClicked() {
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        fab.setImageResource(R.drawable.uup);

        String empty_day_day = empty_day.getSelectedItem().toString();

        switch (empty_day_day) {
            case "월":
                empty_day_day = "1";
                break;
            case "화":
                empty_day_day = "2";
                break;
            case "수":
                empty_day_day = "3";
                break;
            case "목":
                empty_day_day = "4";
                break;
            case "금":
                empty_day_day = "5";
                break;
        }

        showProgressDialog();

        //retrofit 통신
        Retrofit client = new Retrofit.Builder().baseUrl(getString(R.string.retrofit_url))
                .addConverterFactory(GsonConverterFactory.create()).build();
        Interface_Empty empty = client.create(Interface_Empty.class);
        Call<com.donga.examples.boomin.retrofit.retrofitEmpty.Master> call = empty.getEmpty(empty_day_day,
                empty_clock1.getSelectedItem().toString(), empty_clock2.getSelectedItem().toString());
        call.enqueue(new Callback<com.donga.examples.boomin.retrofit.retrofitEmpty.Master>() {
            @Override
            public void onResponse(Call<com.donga.examples.boomin.retrofit.retrofitEmpty.Master> call, Response<com.donga.examples.boomin.retrofit.retrofitEmpty.Master> response) {
                if (response.body().getResult_code() == 1) {
                    ArrayList<Empty> getResultBody = response.body().getResult_body();
                    adapter = new EmptyListViewAdapter();
                    listview.setAdapter(adapter);
                    for (int i = 0; i < getResultBody.size(); i++) {
                        adapter.addItem(getResultBody.get(i).getRoom_no());
                        hideProgressDialog();
                    }
                } else {
                    Toasty.error(getApplicationContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                    log.appendLog("inEmptyActivity code not matched");
                }
            }

            @Override
            public void onFailure(Call<com.donga.examples.boomin.retrofit.retrofitEmpty.Master> call, Throwable t) {
                hideProgressDialog();
                log.appendLog("inEmptyActivity failure");
                Toasty.error(getApplicationContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_empty);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(getBaseContext(), HomeActivity.class);
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
        } else if (id == R.id.nav_change) {
            Intent intent = new Intent(getApplicationContext(), ChangeActivity.class);
            startActivity(intent);
        }   else if (id == R.id.nav_help) {
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_empty);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
        }
    }
}

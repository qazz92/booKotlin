package com.donga.examples.boomin.activity;

import android.app.ProgressDialog;
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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.donga.examples.boomin.AppendLog;
import com.donga.examples.boomin.R;
import com.donga.examples.boomin.retrofit.retrofitChangePushPermit.Interface_changePushPermit;
import com.donga.examples.boomin.retrofit.retrofitLogin.Interface_login;
import com.donga.examples.boomin.retrofit.retrofitLogin.Master;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by horse on 2017. 3. 6..
 */

public class PushActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    AppendLog log = new AppendLog();
    private ProgressDialog mProgressDialog;

    @BindView(R.id.toolbar_push)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout_push)
    DrawerLayout drawer;
    @BindView(R.id.nav_view_push)
    NavigationView navigationView;
    @BindView(R.id.switch_push)
    Switch switch_push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        final SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.SFLAG), Context.MODE_PRIVATE);

        switch_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showProgressDialog();
                //retrofit 통신
                Retrofit client = new Retrofit.Builder().baseUrl(getString(R.string.retrofit_url))
                        .addConverterFactory(GsonConverterFactory.create()).build();
                Interface_changePushPermit changePushPermit = client.create(Interface_changePushPermit.class);
                Call<com.donga.examples.boomin.retrofit.retrofitChangePushPermit.Master> call =
                        changePushPermit.changePushPermit(String.valueOf(sharedPreferences.getInt("ID", 99999)));
                call.enqueue(new Callback<com.donga.examples.boomin.retrofit.retrofitChangePushPermit.Master>() {
                    @Override
                    public void onResponse(Call<com.donga.examples.boomin.retrofit.retrofitChangePushPermit.Master> call, Response<com.donga.examples.boomin.retrofit.retrofitChangePushPermit.Master> response) {
                        if(response.body().getResult_code() == 1){
                            if(response.body().getResult_body() == 0){
                                //푸쉬 허용
                                Toasty.success(getApplicationContext(), "푸쉬 알림이 허용되었습니다.", Toast.LENGTH_SHORT).show();
                            }else if(response.body().getResult_body() == 1){
                                //푸쉬 거부
                                Toasty.warning(getApplicationContext(), "푸쉬 알림이 거부되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                            hideProgressDialog();
                        }else{
                            log.appendLog("inPushActivity code not matched");
                            Toasty.error(getApplicationContext(), "통신 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<com.donga.examples.boomin.retrofit.retrofitChangePushPermit.Master> call, Throwable t) {
                        hideProgressDialog();
                        log.appendLog("inPushActivity failure");
                        Toasty.error(getApplicationContext(), "통신 실패", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_push);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(getBaseContext(), HelpActivity.class);
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_push);
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

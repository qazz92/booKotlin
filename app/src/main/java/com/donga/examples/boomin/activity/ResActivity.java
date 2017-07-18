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
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.donga.examples.boomin.AppendLog;
import com.donga.examples.boomin.R;
import com.donga.examples.boomin.retrofit.retrofitMeal.Interface_meal;
import com.donga.examples.boomin.retrofit.retrofitMeal.Master3;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rhfoq on 2017-02-08.
 */
public class ResActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    AppendLog log = new AppendLog();


    private ProgressDialog mProgressDialog;
    public int count = 0;

    @BindView(R.id.toolbar_res)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout_res)
    DrawerLayout drawer;
    @BindView(R.id.nav_view_res)
    NavigationView navigationView;
    @BindView(R.id.date_text)
    TextView date_text;
    @BindView(R.id.pre_res)
    ImageView pre_res;
    @BindView(R.id.next_res)
    ImageView next_res;

    @BindView(R.id.guk)
    TextView guk;
    @BindView(R.id.gang)
    TextView gang;
    @BindView(R.id.bumin)
    TextView bumin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        SimpleDateFormat msimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentTime = new Date();
        String now = msimpleDateFormat.format(currentTime);


        retrofit(now);

        date_text.setText(now);
//        date_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment newFragment = new CalendarFragment();
//                newFragment.show(getSupportFragmentManager(), "Date Picker");
//            }
//        });

        pre_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = count - 1;

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, count); // +1은 내일

                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                String pre = date.format(cal.getTime());
                retrofit(pre);
                date_text.setText(pre);
            }
        });
        next_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = count + 1;

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, count); // +1은 내일

                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                String next = date.format(cal.getTime());

                retrofit(next);
                date_text.setText(next);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_res);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_res);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void retrofit(String getTime) {

        showProgressDialog();

        Retrofit client = new Retrofit.Builder().baseUrl(getString(R.string.retrofit_url))
                .addConverterFactory(GsonConverterFactory.create()).build();
        Interface_meal meal = client.create(Interface_meal.class);
        retrofit2.Call<Master3> call3 = meal.getMeal(getTime);
        call3.enqueue(new Callback<Master3>() {
            @Override
            public void onResponse(Call<Master3> call, Response<Master3> response) {
                if (response.body().getResult_code() == 1) {
                    String source_guk = response.body().getResult_body().getInter();
                    guk.setText(Html.fromHtml(source_guk));
                    guk.setMovementMethod(LinkMovementMethod.getInstance());

                    String source_bumin = response.body().getResult_body().getBumin_kyo();
                    bumin.setText(Html.fromHtml(source_bumin));
                    bumin.setMovementMethod(LinkMovementMethod.getInstance());

                    String source_gang = response.body().getResult_body().getGang();
                    gang.setText(Html.fromHtml(source_gang));
                    gang.setMovementMethod(LinkMovementMethod.getInstance());

                    hideProgressDialog();
                } else {
                    log.appendLog("inResActivity code not matched");
                    Toasty.error(getApplicationContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<Master3> call, Throwable t) {
                log.appendLog("inResActivity failure");
                hideProgressDialog();
                Toasty.error(getApplicationContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
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
package com.donga.examples.boomin.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseOptions;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.donga.examples.boomin.AppendLog;
import com.donga.examples.boomin.MarketVersionChecker;
import com.donga.examples.boomin.R;
import com.donga.examples.boomin.Singleton.ChangeSingleton;
import com.donga.examples.boomin.Singleton.InfoSingleton;
import com.donga.examples.boomin.listviewAdapter.SelectListViewAdapter;
import com.donga.examples.boomin.retrofit.retrofitGetCircle.Interface_getCircle;
import com.donga.examples.boomin.retrofit.retrofitGetCircle.Master;
import com.donga.examples.boomin.retrofit.retrofitSetCircle.Interface_setCircle;
import com.donga.examples.boomin.retrofit.retrofitSetCircle.JsonRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rhfoq on 2017-02-07.
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    Bundle bundle = null;
    AppendLog log = new AppendLog();
    private ProgressDialog mProgressDialog;

    String device_version, store_version = null;

    ArrayList<String> circleIds = null;
    SelectListViewAdapter adapter;

    ListView listView;
    @BindView(R.id.toolbar_home)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout_home)
    DrawerLayout drawer;
    @BindView(R.id.nav_view_home)
    NavigationView navigationView;

    @OnClick(R.id.menu_res)
    void menu_res() {
        Intent intent = new Intent(getApplicationContext(), ResKActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu_room)
    void menu_room() {
        Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu_empty)
    void menu_empty() {
        Intent intent = new Intent(getApplicationContext(), EmptyActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu_stu)
    void menu_stu() {
        Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu_prof)
    void menu_prof() {
        Intent intent = new Intent(getApplicationContext(), ProActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu_wisper)
    void menu_site() {
        Intent intent = new Intent(getApplicationContext(), WisperActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                store_version = MarketVersionChecker.getMarketVersion(getPackageName());
                InfoSingleton.getInstance().setStore_version(store_version);
                try{
                    device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                    InfoSingleton.getInstance().setDevice_version(device_version);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        t.start();

        try {
            t.join();

            if(store_version.compareTo(device_version)>0){
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
                alert_confirm.setMessage("새 버전이 출시되었습니다. 업데이트 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(100);
        for( int i=0; i < taskList.size(); i++){
            Log.d("INFO","base="+taskList.get(i).baseActivity.getPackageName()+",top="+taskList.get(i).topActivity.getPackageName());
            Log.d("INFO","base="+taskList.get(i).baseActivity.getClassName()+",top="+taskList.get(i).topActivity.getClassName());
        }

        try {
            bundle = getIntent().getExtras();
        } catch (Exception e){
            bundle = null;
        }
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.SFLAG), Context.MODE_PRIVATE);

        if (bundle!=null) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            int pushCount = sharedPreferences.getInt("pushCount", 0);
            pushCount++;
            editor.putInt("pushCount", pushCount);
            editor.commit();
            ShortcutBadger.applyCount(getApplicationContext(), pushCount);

            Intent i = new Intent(getApplicationContext(), WisperActivity.class);
            startActivity(i);
        }else{

            int stuID = sharedPreferences.getInt("stuID", 0);
            Log.i("HomeActivity", "" + stuID);

            int check = sharedPreferences.getInt("checkCircle", 0);
            if(check == 0){
                ArrayList<String> list_major = new ArrayList<String>();
                list_major.add("경영정보학과");
                list_major.add("경영학과");
                boolean wrapInScrollView = false;
                final MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .customView(R.layout.activity_select_dialog, wrapInScrollView)
                        .build();

                View view = dialog.getCustomView();
                listView = (ListView)view.findViewById(R.id.list_select);
                final MaterialSpinner select_spinner = (MaterialSpinner)view.findViewById(R.id.select_spinner);
                select_spinner.setItems(list_major);
                select_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        Logger.d(""+select_spinner.getItems().get(position));
                        getCircle(select_spinner.getItems().get(position).toString());
                    }
                });

                getCircle(select_spinner.getItems().get(0).toString());

                Button select_btn_ok = (Button)view.findViewById(R.id.select_btn_ok_btn);
                select_btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showProgressDialog();
                        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.SFLAG), Context.MODE_PRIVATE);

                        Retrofit client = new Retrofit.Builder().baseUrl(getString(R.string.retrofit_url))
                                .addConverterFactory(GsonConverterFactory.create()).build();
                        Interface_setCircle setCircle = client.create(Interface_setCircle.class);
                        ArrayList<String> selectArray = ChangeSingleton.getInstance().getDialogArray();
                        Call<com.donga.examples.boomin.retrofit.retrofitSetCircle.Master> call = setCircle.setCircle("application/json",
                                new JsonRequest(sharedPreferences.getInt("ID", 0), selectArray));
                        Logger.d(sharedPreferences.getInt("ID", 0)+","+selectArray.toString());
                        call.enqueue(new Callback<com.donga.examples.boomin.retrofit.retrofitSetCircle.Master>() {
                            @Override
                            public void onResponse(Call<com.donga.examples.boomin.retrofit.retrofitSetCircle.Master> call, Response<com.donga.examples.boomin.retrofit.retrofitSetCircle.Master> response) {
                                if(response.body().getResult_code() == 1){
                                    hideProgressDialog();
                                    dialog.dismiss();
                                }else{
                                    getCircle(String.valueOf(select_spinner.getSelectedIndex()));
                                    hideProgressDialog();
                                    Toasty.error(HomeActivity.this, "동아리 설정 실패", Toast.LENGTH_SHORT).show();
                                    log.appendLog("inHomeActivity setCircle code not matched");
                                }
                            }

                            @Override
                            public void onFailure(Call<com.donga.examples.boomin.retrofit.retrofitSetCircle.Master> call, Throwable t) {
                                hideProgressDialog();
                                Toasty.error(HomeActivity.this, "동아리 설정 실패", Toast.LENGTH_SHORT).show();
                                log.appendLog("inHomeActivity setCircle failure");
                                t.printStackTrace();
                            }
                        });
                    }
                });
                dialog.show();
            }
        }
    }

    public void getCircle(String major){
        showProgressDialog();
        Retrofit client = new Retrofit.Builder().baseUrl(getString(R.string.retrofit_url))
                .addConverterFactory(GsonConverterFactory.create()).build();
        final Interface_getCircle getCircle = client.create(Interface_getCircle.class);
        retrofit2.Call<Master> call = getCircle.getCircle(major);
        circleIds = new ArrayList<>();
        call.enqueue(new Callback<Master>() {
            @Override
            public void onResponse(Call<Master> call, Response<Master> response) {
                if(response.body().getResult_code() == 1){
                    adapter = new SelectListViewAdapter();
                    for(int i = 0; i<response.body().getResult_body().size(); i++){
                        circleIds.add(response.body().getResult_body().get(i).getId());
                        adapter.addItem(response.body().getResult_body().get(i).getName(), response.body().getResult_body().get(i).getId());
                        adapter.notifyDataSetChanged();
                    }
                    listView.setAdapter(adapter);
                    hideProgressDialog();
                }else{
                    hideProgressDialog();
                    log.appendLog("inSelectDialog code not matched");
                    Toasty.error(HomeActivity.this, "불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Master> call, Throwable t) {
                hideProgressDialog();
                log.appendLog("inSelectDialog failure");
                Toasty.error(HomeActivity.this, "불러오기 실패", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {    //뒤로가기 버튼 두 번 누르면 종료
            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)  //연속 누를 때 2초 안에 안누르면 종료 x
            {
                super.onBackPressed();
            } else    //종료
            {
                backPressedTime = tempTime;
                Toasty.normal(getApplicationContext(), "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_home);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
//        finish();
        super.onPause();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(HomeActivity.this);
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
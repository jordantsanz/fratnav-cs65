package com.example.fratnav;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.PertDataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pert;
import com.anychart.charts.Pie;
import com.anychart.core.pert.Milestones;
import com.anychart.core.pert.Tasks;
import com.anychart.core.ui.Tooltip;
import com.anychart.enums.TreeFillingMethod;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.forum.Forum;
import com.example.fratnav.houses.HousesSearch;
import com.example.fratnav.models.User;
import com.example.fratnav.onboarding.Authentication;
import com.example.fratnav.profile.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;
    public static final String USER_HOUSE_BOOL = "userbool";
    boolean isHouse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("home", "home");
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();



        if (currentUser == null){
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
            finish();
        }

        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                isHouse = user.house;
            }
        });

        try {
            Log.d("cu", currentUser.toString());
        }
        catch(Exception e){
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
            finish();
        }

        // checks to make sure the user is currently logged in; otherwise, send to authentication
        if (currentUser == null) {
            Log.d("null", "sup");
            startActivity(new Intent(this, Authentication.class));
            return;
        }
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.home);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.houses) {
                    Log.d("swtich", "houses");
                    startActivity(new Intent(MainActivity.this, HousesSearch.class));
                    finish();
                    return true;
                } else if (item.getItemId()==R.id.profile) {
                    Log.d("swtich", "profile");
                    Intent intent = new Intent(MainActivity.this, Profile.class);
                    intent.putExtra(MainActivity.USER_HOUSE_BOOL, isHouse);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (item.getItemId()==R.id.forum) {
                    Log.d("swtich", "forum");
                    startActivity(new Intent(MainActivity.this, Forum.class));
                    finish();
                    return true;
                }
                return false;
            }

        });

//        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
//        Pie pie = AnyChart.pie();
//        List<DataEntry> data = new ArrayList<>();
//        data.add(new ValueDataEntry("Jordan", 50));
//        data.add(new ValueDataEntry("Will", 30));
//        data.add(new ValueDataEntry("Melisa ", 20));
//        pie.data(data);
//        anyChartView.setChart(pie);
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        Pert pert = AnyChart.pert();

        List<DataEntry> data = new ArrayList<>();
        data.add(new CustomPertDataEntry("1", "start talking to brother of houses you are intersted in", "21S", "Spring Term"));
        data.add(new CustomPertDataEntry("2","continue talking to brothers, attend rush events of houses that you are interested in", "21X",  new String[]{"1"} ,"Summer Term"));
        data.add(new CustomPertDataEntry("3", "attend information sessions and fill out rush interest from sent out by the IFC", "21F", new String[]{"2"}, "Fall Term"));
        data.add(new CustomPertDataEntry("4","meet as many people as you can, try getting to know people outside of the typical basement scene", "22W", new String[]{"3"}, "Winter Term"));

        pert.data(data, TreeFillingMethod.AS_TABLE);

       pert.padding(50d, 0d, 0d, 50d);


        pert.title().enabled(true);
        pert.title()
                .text("General Rush Flow");

        Tasks tasks = pert.tasks();
        tasks.upperLabels().format(
                "function() {\n" +
                        "    return this.item.get('fullName');\n" +
                        "  }");

        tasks.lowerLabels().format("advice: {%description}");

        Tooltip tooltip = tasks.tooltip();
        tooltip.separator(true)
                .titleFormat(
                        "function() {\n" +
                                "      return this.item.get('fullName');\n" +
                                "    }");


        Milestones milestones = pert.milestones();
        milestones.color("#2C81D5")
                .size("6.5%");
        milestones.hovered().fill("#2C81D5", 0.75d);
        milestones.tooltip().format("" +
                "function() {\n" +
                "  var result = '';\n" +
                "  var i = 0;\n" +
                "  if (this['successors'] && this['successors'].length) {\n" +
                "    result += 'Successors:';\n" +
                "    for (i = 0; i < this['successors'].length; i++) {\n" +
                "      result += '\\n - ' + this['successors'][i].get('fullName');\n" +
                "    }\n" +
                "    if (this['predecessors'] && this['predecessors'].length)\n" +
                "      result += '\\n\\n';\n" +
                "  }\n" +
                "  if (this['predecessors'] && this['predecessors'].length) {\n" +
                "    result += 'Predecessors:';\n" +
                "    for (i = 0; i < this['predecessors'].length; i++) {\n" +
                "      result += '\\n - ' + this['predecessors'][i].get('fullName');\n" +
                "    }\n" +
                "  }\n" +
                "  return result;\n" +
                "}");

        Milestones critMilestones = pert.criticalPath().milestones();
        critMilestones.labels().format(
                "function() {\n" +
                        "    return this['creator'] ? this['creator'].get('name') : this['isStart'] ? 'Start' : 'Finish';\n" +
                        "  }");
        critMilestones.color("#E24B26");


        anyChartView.setChart(pert);

    }
    //from GitHubRep
    private static class CustomPertDataEntry extends PertDataEntry {
        CustomPertDataEntry(String id, String description, String name, String fullName) {
            super(id, name, fullName);
            setValue("description", description);
        }

        CustomPertDataEntry(String id, String description, String name, String[] dependsOn, String fullName) {
            super(id, name, fullName, dependsOn);
            setValue("description", description);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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



    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
    }
//    if using menu on toolbar
//    public void logout(MenuItem item) {
//        FirebaseAuth.getInstance().signOut();
//        finishAffinity();
//    }
}

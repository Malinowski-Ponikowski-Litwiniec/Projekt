package com.google.firebase.quickstart.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphActivity extends AppCompatActivity {
    public ListView listView;
    Toolbar myToolbar;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public String dateFormat;
    GraphView graph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Window window = GraphActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(GraphActivity.this.getResources().getColor(R.color.md_black_1000));

        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat = simpleDateFormat.format(date);

        graph = (GraphView) findViewById(R.id.graph);

 setToGraph();
    }


    public void setToGraph(){
        myRef.child("lista").child(mAuth.getUid()).child("dataSnapshot").addValueEventListener(new ValueEventListener() {
          Map<String,String> map = new HashMap<>();
            List<DataPoint> list = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    map.put(noteDataSnapshot.getKey(),String.valueOf(noteDataSnapshot.getValue()));
                }
                for (Map.Entry<String, String> entry : map.entrySet()) {


                    String pom = entry.getValue();
                    pom = pom.substring(pom.lastIndexOf('=') + 1);
                    pom = pom.substring(0,pom.lastIndexOf('}'));

                    String datePom = entry.getKey();
                    datePom = datePom.substring(datePom.lastIndexOf('-' ) + 1);
                    datePom = datePom.substring(datePom.lastIndexOf('-' ) + 1);
                    System.out.println("!!!!! key " + datePom + " value " + pom);
               DataPoint dataPoint = new DataPoint(Integer.valueOf(datePom), Double.parseDouble(pom));

   list.add(dataPoint);
                }
                DataPoint[] array = new DataPoint[list.size()];
                for(int i=0;i<array.length;i++){
                    array[i]=list.get(i);
                }
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(array);


                graph.addSeries(series);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

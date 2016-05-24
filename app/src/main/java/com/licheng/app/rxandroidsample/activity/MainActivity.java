package com.licheng.app.rxandroidsample.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.licheng.app.rxandroidsample.data.ExampleActivityAndName;
import com.licheng.app.rxandroidsample.adapter.ExampleAdapter;
import com.licheng.app.rxandroidsample.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupExampleList();
    }

    private void setupExampleList() {
        RecyclerView exampleList = (RecyclerView) findViewById(R.id.example_list);
        exampleList.setHasFixedSize(true);
        exampleList.setLayoutManager(new LinearLayoutManager(this));
        exampleList.setAdapter(new ExampleAdapter(this, getExamples()));
    }

    private static List<ExampleActivityAndName> getExamples() {
        List<ExampleActivityAndName> exampleActivityAndNames = new ArrayList<>();
        exampleActivityAndNames.add(new ExampleActivityAndName(
                Example1Activity.class,
                "Example 1: Simple List"));
        exampleActivityAndNames.add(new ExampleActivityAndName(
                Example2Activity.class,
                "Example 2: Get Net BeautyList"));
        exampleActivityAndNames.add(new ExampleActivityAndName(
                Example3Activity.class,
                "Example 3: Get City"));
        exampleActivityAndNames.add(new ExampleActivityAndName(
                Example4Activity.class,
                "Example 4: Get Net BeautyList by Map"));
        return exampleActivityAndNames;
    }
}

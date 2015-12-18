package com.example.raj.pro1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    Button createNew,listAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNew=(Button)findViewById(R.id.newProduct);
        listAll=(Button)findViewById(R.id.listAll);
    }

    public void f1(View v){
        Intent in = new Intent(getApplicationContext(),AllProductsActivity.class);
        startActivity(in);
    }

    public void f2(View v){
        Intent in = new Intent(getApplicationContext(),NewProductActivity.class);
        startActivity(in);
    }


}

package com.example.testesmartstock;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.util.Scanner;

public class TermosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos);

        TextView textView = findViewById(R.id.textTermos);

        // Lê o arquivo raw
        InputStream inputStream = getResources().openRawResource(R.raw.termos_uso);
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        String termos = scanner.hasNext() ? scanner.next() : "";
        textView.setText(termos);
    }
}

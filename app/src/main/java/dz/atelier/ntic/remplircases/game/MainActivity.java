package dz.atelier.ntic.remplircases.game;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Intent inte;
    EditText id, login , mdp;
    Button bcreation;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            login = (EditText)findViewById(R.id.login);
            mdp = (EditText)findViewById(R.id.mdp);
        mAuth = FirebaseAuth.getInstance();


        bcreation = (Button)findViewById(R.id.logID);
    }

    public void userLogin() {
        String username = login.getText().toString().trim();
        String password = mdp.getText().toString().trim();

        if (username.isEmpty()){
            login.setError("Email is required");
            login.requestFocus() ;
            return;
        }



        if (password.isEmpty()){
            mdp.setError("Password is required");
            mdp.requestFocus() ;
            return;
        }

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent myintent= new Intent(MainActivity.this,CategoriesActivity.class);
                    startActivity(myintent);

                }else{
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void LoginClick(View view) {
        userLogin();
    }

    public void SignUpClick(View view) {
        Intent myintent= new Intent(this,SignUp.class);
        startActivity(myintent);
    }
}

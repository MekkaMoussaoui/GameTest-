package dz.atelier.ntic.remplircases.game;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    EditText editTextEmail, editTextMdp ;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = (EditText)findViewById(R.id.emailId);
        editTextMdp = (EditText)findViewById(R.id.mdpID);

        mAuth = FirebaseAuth.getInstance();
    }

    public void SignUp(View view) {
        registerUser();
//        Intent intent= new Intent(this,MainActivity.class);
//        startActivity(intent);

    }
    public void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextMdp.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus() ;
            return;
        }



        if (password.isEmpty()){
            editTextMdp.setError("Password is required");
            editTextMdp.requestFocus() ;
            return;
        }
        if (password.length()<6){
            editTextMdp.setError("Minimum lenght of password should be 6");
            editTextMdp.requestFocus();
            return;
        }
         mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"User registered succesfull",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                }
                else
                    Toast.makeText(getApplicationContext(),"some error accured",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

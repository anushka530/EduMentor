package com.edumentor.edumentor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText inputEmail, inputPassword;
    Button btnLogin;
    TextView createNewAccount, forgetpassword;
    ProgressDialog mLoadingBar;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();


        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.pass);
        btnLogin = findViewById(R.id.button_login);
        createNewAccount = findViewById(R.id.newAccount);
        forgetpassword = findViewById(R.id.forgotpassword);

        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(this);
        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AtemptLogin();
            }
        });

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, FrontPageActivity.class);
            startActivity(intent);
        }

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText((v.getContext()));
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Receive Password Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Reset Link Is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                passwordResetDialog.create().show();
            }
        });


    }


    private void AtemptLogin() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if(email.isEmpty() || !email.contains("@gmail")){
            shoeError(inputEmail, "Email is not valid");
        }
        else if(password.isEmpty() || password.length()<5){
            shoeError(inputPassword, "Password should be greater than 5 digits");
        }else{
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("please wait");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        mLoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Login is successful",Toast.LENGTH_SHORT).show() ;
                        Intent intent = new Intent(LoginActivity.this,FrontPageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        mLoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, task.getException().toString(),Toast.LENGTH_SHORT).show();

                    }


                }
            });

        }
    }




    private void shoeError(EditText field, String text) {
        field.setError(text);
        field.requestFocus();



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
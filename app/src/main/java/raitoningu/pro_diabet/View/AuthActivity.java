package raitoningu.pro_diabet.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import raitoningu.pro_diabet.R;

public class AuthActivity extends AppCompatActivity implements  View.OnFocusChangeListener{
    LinearLayout authLayout;
    TextInputLayout mUsernameLayout;
    EditText mEmail;
    EditText mPassword;
    ProgressBar logProgress;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_screen);

        authLayout = findViewById(R.id.linearLayout);
        mUsernameLayout = findViewById(R.id.email_layout);
        mEmail = findViewById(R.id.auth_email);
        mPassword = findViewById(R.id.auth_pass);
        logProgress = findViewById(R.id.login_progress);
        mEmail.setOnFocusChangeListener(this);
        mPassword.setOnFocusChangeListener(this);
        authLayout.setVisibility(View.VISIBLE);
        logProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v != mEmail && mEmail.getText().toString().isEmpty()) {
            mUsernameLayout.setErrorEnabled(true);
            mUsernameLayout.setError("Поле не может быть пустым");
        } else {
            mUsernameLayout.setErrorEnabled(false);
        }
    }

    public void onClickLogin(View view) {
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();
        if (email.trim().isEmpty() || password.trim().isEmpty())
            Toast.makeText(this, "Не введен Email либо пароль", Toast.LENGTH_SHORT).show();
        else if (email.equals("kozlovalexander3@gmail.com") && password.equals("12345")) {
            //TODO: POST AUTH
            authLayout.setVisibility(View.INVISIBLE);
            logProgress.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else Toast.makeText(this, "Email либо пароль введены неверно", Toast.LENGTH_SHORT).show();
    }
    public void onClickForgot(View view) {
        //TODO: Ссылка на васстановление пароля.
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(browserIntent);
    }
    public void onClickReg(View view) {
        //TODO: Ссылка на регистрацию.
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://10.8.0.36:3000/register"));
        startActivity(browserIntent);
    }
    @Override
    public void onBackPressed(){
        this.finish();
    }
}

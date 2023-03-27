package com.example.samsungprojectjava;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.yandex.mapkit.MapKitFactory;

import java.util.ArrayList;

public class MainActivity extends androidx.fragment.app.FragmentActivity {
    static boolean mapkit_loaded = false;
    DBLoginClass mDBConnector;
    CheckBox check_login;
    Context mContext;

    private FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
    void clearRemember(){
        ArrayList<LoginClass> arr = mDBConnector.selectAll(true);
        for(int i = 0; i < arr.size(); i++) {
            arr.get(i).remember = 0;
            mDBConnector.update(arr.get(i));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.deleteDatabase("login.db");
        //this.deleteDatabase("schedule.db");
        mContext = this;
        mAuth = FirebaseAuth.getInstance();
        if (!mapkit_loaded){
            MapKitFactory.setApiKey("89d02c92-aaa2-415f-95a3-9f6a31933bb6");
            MapKitFactory.initialize(this);
            mapkit_loaded = true;
        }
        setContentView(R.layout.activity_login);
        mDBConnector = new DBLoginClass(this);
        check_login = findViewById(R.id.check_login);
        Button btn_login = findViewById(R.id.btn_login);
        Button btn_login_fire = findViewById(R.id.btn_login_fire);
        Button btn_signup_fire = findViewById(R.id.btn_signup_fire);
        EditText ed_login = findViewById(R.id.edit_login);
        EditText ed_password = findViewById(R.id.edit_password);
        //btn_login.setVisibility(View.INVISIBLE);
        try {
            LoginClass data = mDBConnector.select_by_remember(1);
            ed_login.setText(data.login);
            ed_password.setText(data.password);
            check_login.setChecked(true);
        }
        catch (android.database.CursorIndexOutOfBoundsException ex){
            ;
        }
        btn_login_fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(ed_login.getText().toString(), ed_password.getText().toString())
                        .addOnCompleteListener((MainActivity)mContext, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                } else {
                                    Object ex = task.getException().getClass();
                                    if (ex != NullPointerException.class) {
                                        if (ex == FirebaseTooManyRequestsException.class)
                                            Toast.makeText(mContext, "Слишком много запросов, подождите",
                                                    Toast.LENGTH_SHORT).show();
                                        else if (ex == FirebaseAuthInvalidCredentialsException.class)
                                            Toast.makeText(mContext, "Проверьте правильность данных",
                                                    Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(mContext, task.getException().getClass().toString(),
                                                    Toast.LENGTH_SHORT).show();
                                    }
                                    return;
                                }
                                btn_login.callOnClick();
                            }
                        });
            }
        });
        btn_signup_fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(ed_login.getText().toString(), ed_password.getText().toString())
                        .addOnCompleteListener((MainActivity)mContext, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //Toast.makeText(mContext, "Sign up successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Object ex = task.getException().getClass();
                                    if (ex != NullPointerException.class) {
                                        if (ex == FirebaseAuthWeakPasswordException.class)
                                            Toast.makeText(mContext, "Пароль должен быть минимум 6 символов",
                                                    Toast.LENGTH_SHORT).show();
                                        else if (ex == FirebaseAuthInvalidCredentialsException.class)
                                            Toast.makeText(mContext, "Проверьте правильность почты",
                                                    Toast.LENGTH_SHORT).show();
                                        else if (ex == FirebaseAuthUserCollisionException.class)
                                            Toast.makeText(mContext, "Такой пользователь уже существует",
                                                    Toast.LENGTH_SHORT).show();
                                        else if (ex == FirebaseTooManyRequestsException.class)
                                            Toast.makeText(mContext, "Слишком много запросов, подождите",
                                                    Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(mContext, task.getException().getClass().toString(),
                                                    Toast.LENGTH_SHORT).show();
                                    }
                                    return;
                                }
                                Toast.makeText(mContext, "Аккаунт успешно создан",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int h_tasks, m_tasks, l_tasks;
                int rem = check_login.isChecked() ? 1 : 0;
                if (ed_login.getText().toString().equals("") || ed_password.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Заполните поля логина и пароля", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    LoginClass data = mDBConnector.select_by_login(ed_login.getText().toString());
                    if (!data.password.equals(ed_password.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Неверный пароль к существующему логину", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    clearRemember();
                    mDBConnector.update(new LoginClass(data.id, data.login, data.password, rem, data.low_tasks, data.medium_tasks, data.high_tasks));
                    h_tasks = data.high_tasks;
                    m_tasks = data.medium_tasks;
                    l_tasks = data.low_tasks;
                }
                catch (android.database.CursorIndexOutOfBoundsException ex){
                    clearRemember();
                    mDBConnector.insert(ed_login.getText().toString(), ed_password.getText().toString(), rem, 120, 60, 30);
                    h_tasks = 120;
                    m_tasks = 60;
                    l_tasks = 30;
                }
                Intent i;
                i = new Intent(MainActivity.this, LaaActivity.class);
                i.putExtra("login", ed_login.getText().toString());
                i.putExtra("password", ed_password.getText().toString());
                i.putExtra("remember", rem);
                i.putExtra("h_tasks", h_tasks);
                i.putExtra("m_tasks", m_tasks);
                i.putExtra("l_tasks", l_tasks);
                setResult(RESULT_OK, i);
                finish();
                startActivity(i);
            }
        });
    }

}
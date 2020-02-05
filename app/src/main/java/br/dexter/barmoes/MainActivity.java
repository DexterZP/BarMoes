package br.dexter.barmoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;

import br.dexter.barmoes.MenuPrincipal.menuPrincipal;

public class MainActivity extends AppCompatActivity
{
    private FirebaseUser user;
    private FirebaseAuth auth;
    private TextInputLayout inputEmail, inputSenha;
    private TextInputEditText Email, Senha;
    private Button Entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Login administrativo");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        inputEmail = findViewById(R.id.InputEmail);
        inputSenha = findViewById(R.id.InputSenha);

        Email = findViewById(R.id.Email);
        Senha = findViewById(R.id.Senha);

        Entrar = findViewById(R.id.Entrar);

        Button();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if(user != null) {
            Intent intent = new Intent(this, menuPrincipal.class);
            startActivity(intent);
            finish();
        }
    }

    public void Button()
    {
        Entrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                assert Email.getText() != null;
                assert Senha.getText() != null;

                if(Email.getText().toString().isEmpty()) {
                    inputEmail.setError("Não pode deixar em branco");
                } else if(Senha.getText().toString().isEmpty()) {
                    inputSenha.setError("Não pode deixar em branco");
                } else {
                    Login(Email.getText().toString(), Senha.getText().toString());
                }
            }
        });
    }

    public void Login(String email, String senha)
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(false);
        dialog.show();

        auth.signInWithEmailAndPassword(email, senha)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    dialog.dismiss();

                    Intent intent = new Intent(MainActivity.this, menuPrincipal.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    dialog.dismiss();

                    Toast.makeText(MainActivity.this, "Falha no login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
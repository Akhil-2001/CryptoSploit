package com.example.cryptosploit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Base64;

public class Decrypt extends AppCompatActivity {

    TextView algoText, cryptoResult;
    Spinner spin1;
    Button encAction;
    EditText cText;

    private byte[] S = new byte[256];
    private byte[] T = new byte[256];
    private int keylen;

    void RC4(final byte[] key) {
        if (key.length < 1 || key.length > 256) {
            throw new IllegalArgumentException(
                    "key must be between 1 and 256 bytes");
        } else {
            keylen = key.length;
            for (int i = 0; i < 256; i++) {
                S[i] = (byte) i;
                T[i] = key[i % keylen];
            }
            int j = 0;
            byte tmp;
            for (int i = 0; i < 256; i++) {
                j = (j + S[i] + T[i]) & 0xFF;
                tmp = S[j];
                S[j] = S[i];
                S[i] = tmp;
            }
        }
    }

    public byte[] encryptRC4(final byte[] plaintext) {
        final byte[] ciphertext = new byte[plaintext.length];
        int i = 0, j = 0, k, t;
        byte tmp;
        for (int counter = 0; counter < plaintext.length; counter++) {
            i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;
            tmp = S[j];
            S[j] = S[i];
            S[i] = tmp;
            t = (S[i] + S[j]) & 0xFF;
            k = S[t];
            ciphertext[counter] = (byte) (plaintext[counter] ^ k);
        }
        return ciphertext;
    }

    public byte[] decryptRC4(final byte[] ciphertext) {
        return encryptRC4(ciphertext);
    }

    String rot13(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'N' && c <= 'Z') c -= 13;
            sb.append(c);
        }
        return sb.toString();
    }

    String decryptVigenere(String text, final String key)
    {
        String res = "";
        text = text.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z')
                continue;
            res += (char) ((c - key.charAt(j) + 26) % 26 + 'A');
            j = ++j % key.length();
        }
        return res;
    }

    String encryptDecryptXOR(String inputString)
    {
        // Define XOR key
        // Any character value will work
        char xorKey = 'P';

        // Define String to store encrypted/decrypted String
        String outputString = "";

        // calculate length of input string
        int len = inputString.length();

        // perform XOR operation of key
        // with every caracter in string
        for (int i = 0; i < len; i++)
        {
            outputString = outputString +
                    Character.toString((char) (inputString.charAt(i) ^ xorKey));
        }

        System.out.println(outputString);
        return outputString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);

        algoText = (TextView) findViewById(R.id.textAlgorithmDec);
        Intent intent = getIntent();
        String encAlgo = intent.getStringExtra("algorithm");
        algoText.setText(encAlgo);

        encAction = findViewById(R.id.decBtn);
        cText =  findViewById(R.id.editCipherTextDec);
        cryptoResult = findViewById(R.id.decResult);

        encAction.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String cipherText = cText.getText().toString();
                if(encAlgo.equals("ROT13")) {
                    String result = rot13(cipherText);
                    cryptoResult.setText(result);
                }
                if(encAlgo.equals("BASE64")) {
                    Base64.Decoder decoder = Base64.getDecoder();
                    String result = new String(decoder.decode(cipherText));
                    cryptoResult.setText(result);
                }
                if(encAlgo.equals("VIGENERE")) {
                    String result = decryptVigenere(cipherText,"android");
                    cryptoResult.setText(result);
                }
                if(encAlgo.equals("XOR")) {
                    String result = encryptDecryptXOR(cipherText);
                    cryptoResult.setText(result);
                }
                if(encAlgo.equals("RC4")) {
                    byte[] key = "1234".getBytes();
                    RC4(key);
                    String result = new String(encryptRC4(cipherText.getBytes()));
                    cryptoResult.setText(result);
                }
            }
        });

    }
}
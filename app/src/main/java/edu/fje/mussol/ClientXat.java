package edu.fje.mussol;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.dynamic.IFragmentWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientXat extends AppCompatActivity {

    private Button boto;
    private TextView missatges;
    private EditText entrada;
    private PrintWriter sortida;
    private Socket socol = null;
    private BufferedReader input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boto = findViewById(R.id.boto);
        missatges = findViewById(R.id.missatges);
        entrada = findViewById(R.id.entrada);
        byte[] addr = new byte[4];
        addr[0] = (byte) 192;
        addr[1] = (byte) 168;
        addr[2] = (byte) 1;
        addr[3] = (byte) 40;

        try {
            InetAddress adreca = InetAddress.getByAddress(addr);
            socol = new Socket();
            System.out.println(socol);
            try {
                socol.connect(new InetSocketAddress(adreca.getHostAddress(),
                        8189), 2000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LecturaFil lectura = new LecturaFil(socol);
            lectura.start();
            boto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        OutputStream outStream = socol.getOutputStream();
                        PrintWriter sortida = new PrintWriter(outStream, true);
                        sortida.println(entrada.getText().toString());
                    } catch (UnknownHostException e) {
                        System.out.println("host desconegut");
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println("problemes E/S");
                    }
                }
            });
        } catch (UnknownHostException ex) {
            System.out.println("problemes amb el servidor");
        } catch (IOException e) {
            System.out.println("problemes amb la connexio");
        }
    }

    class LecturaFil extends Thread {
        public LecturaFil(Socket s) {
            socol = s;
        }

        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socol.getInputStream()));
                final boolean[] fet = {false};
                while (!fet[0]) {
                    final String resposta = input.readLine();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (resposta == null || resposta.equals("SERVIDOR PLE")){
                                missatges.setText("Si es vol tornar a conectar, pot sortir i tornar a entrar");
                                try { socol.close(); fet[0] = true;}
                                catch (IOException e) { e.printStackTrace(); }
                            } else {
                                missatges.append(resposta + "\n");
                            }
                        }
                    });
                }
            }
            catch (UnknownHostException e) {
                System.out.println("host desconegut");
                e.printStackTrace();
            }
            catch (IOException e) { System.out.println("problemes E/S"); }
            catch (Exception e) {}
        }
    }
}



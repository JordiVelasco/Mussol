package edu.fje.mussol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.SQLOutput;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ClientXat extends AppCompatActivity {

    private Button boto;
    private TextView missatges, missatges2;
    private EditText entrada;
    private PrintWriter sortida;
    private Socket socol = null;
    private BufferedReader input;
    private KeyPair parClaves;
    private PrivateKey clauPrivada;
    private PublicKey clauPublica;
    private XifratgeAsimetric xifratgeAsimetric;
    private Cipher cifrador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        boto = findViewById(R.id.boto);
        missatges = findViewById(R.id.missatges);
        missatges2 = findViewById(R.id.missatges);
        entrada = findViewById(R.id.entrada);
        xifratgeAsimetric = new XifratgeAsimetric();
        try {
            cifrador = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
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
                        String mensaje = entrada.getText().toString();
                        /*parClaves = xifratgeAsimetric.crearClaus();
                        clauPrivada = parClaves.getPrivate();
                        clauPublica = parClaves.getPublic();
                        byte[] x = xifratgeAsimetric.xifratgeASimetric(mensaje, clauPublica, cifrador);
                        sortida.println(x.toString());*/
                        sortida.println("User 1: " + entrada.getText().toString());
                    } catch (UnknownHostException e) {
                        System.out.println("host desconegut");
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println("problemes E/S");
                    } /*catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    }*/
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
                while (true) {
                    final String resposta = input.readLine();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            missatges.append(resposta + "\n");
                            /*byte[] x = resposta.getBytes();
                            System.out.println(x + "supuesto cifrado");
                            String mensaje = null;
                            try {
                                mensaje = xifratgeAsimetric.desxifraASimetric(x,clauPrivada,cifrador);
                            } catch (InvalidKeyException e) {
                                e.printStackTrace();
                            } catch (BadPaddingException e) {
                                e.printStackTrace();
                            } catch (IllegalBlockSizeException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //System.out.println("\nSERVER> " + mensaje);
                            //missatges.append(mensaje + "\n");*/
                        }
                    });
                    //Log.v("DAM2","\nSERVER> " + resposta);
                }
            }
            catch (UnknownHostException e) { System.out.println("host desconegut"); e.printStackTrace(); }
            catch (IOException e) { System.out.println("problemes E/S"); }
            catch (Exception e) {}
        }
    }
}



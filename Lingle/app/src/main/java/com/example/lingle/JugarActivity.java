package com.example.lingle;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Robyn
 */
public class JugarActivity extends AppCompatActivity implements View.OnClickListener {

    //para finalizar la Activity desde otras
    public static Activity jugarActivity;

    //variables para establecer la casilla en la que nos encontramos
    int countTile = 1;
    int countRow = 1;

    //variable de acierto
    String guess = ""; //instancia el intento

    //prioridad de cambio de color de teclas, inicialmente prioridad de tecla negra, 2 será de amarilla, 1 de verde
    int prQ = 3, prW = 3, prE = 3, prR = 3, prT = 3, prY = 3, prU = 3, prI = 3, prO = 3, prP = 3,
            prA = 3, prS = 3, prD = 3, prF = 3, prG = 3, prH = 3, prJ = 3, prK = 3, prL = 3,
            prEnye = 3, prZ = 3, prX = 3, prC = 3, prV = 3, prB = 3, prN = 3, prM = 3;

    //valores para cambiar o no el color de fondo de cada fila, según si se han rellenado ya o no
    boolean isRow1Filled = false, isRow2Filled = false, isRow3Filled = false, isRow4Filled = false,
            isRow5Filled = false, isRow6Filled = false;

    //para modificar el color de fondo
    ConstraintLayout layoutJugar;
    //Switch switchDarkMode;
    /**
    //preparación de casillas a rellenar
    TextView tile1Row1 = null;
    TextView tile2Row1 = null;
    TextView tile3Row1 = null;
    TextView tile4Row1 = null;
    TextView tile5Row1 = null;
    TextView tile1Row2 = null;
    TextView tile2Row2 = null;
    TextView tile3Row2 = null;
    TextView tile4Row2 = null;
    TextView tile5Row2 = null;
    TextView tile1Row3 = null;
    TextView tile2Row3 = null;
    TextView tile3Row3 = null;
    TextView tile4Row3 = null;
    TextView tile5Row3 = null;
    TextView tile1Row4 = null;
    TextView tile2Row4 = null;
    TextView tile3Row4 = null;
    TextView tile4Row4 = null;
    TextView tile5Row4 = null;
    TextView tile1Row5 = null;
    TextView tile2Row5 = null;
    TextView tile3Row5 = null;
    TextView tile4Row5 = null;
    TextView tile5Row5 = null;
    TextView tile1Row6 = null;
    TextView tile2Row6 = null;
    TextView tile3Row6 = null;
    TextView tile4Row6 = null;
    TextView tile5Row6 = null;
     */

    //prepración de botones teclado
    Button buttonQ;
    Button buttonW;
    Button buttonE;
    Button buttonR;
    Button buttonT;
    Button buttonY;
    Button buttonU;
    Button buttonI;
    Button buttonO;
    Button buttonP;
    Button buttonA;
    Button buttonS;
    Button buttonD;
    Button buttonF;
    Button buttonG;
    Button buttonH;
    Button buttonJ;
    Button buttonK;
    Button buttonL;
    Button buttonEnye;
    Button buttonZ;
    Button buttonX;
    Button buttonC;
    Button buttonV;
    Button buttonB;
    Button buttonN;
    Button buttonM;

    //array de TextViews por cada fila para simplificar el código
    TextView[] row1 = new TextView[5];
    TextView[] row2 = new TextView[5];
    TextView[] row3 = new TextView[5];
    TextView[] row4 = new TextView[5];
    TextView[] row5 = new TextView[5];
    TextView[] row6 = new TextView[5];

    //instanciamos una clase Lingle
    Lingle lingle = null;

    //String guess = ""; //variable introducida por el usuario
    int num_of_tries = 0; //almacena el número de intentos

    //diccionario elegido, para mostrar en el ResultadoActivity
    String chosen_dictionary = "";

    //resultado final, pasado al ResultadoActivity
    StringBuilder resultadoLingle = new StringBuilder();

    //número de intentos
    int numIntentos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);
        layoutJugar = (ConstraintLayout) findViewById(R.id.layoutJugar);

        jugarActivity = this; //activity, que se finalizará en otras
        //AssetManager am = getApplicationContext().getAssets();

        InputStream dictionary5;
        InputStream diccionario5;
        InputStream bothDictionaries5;
        try {
            dictionary5 = getAssets().open("dictionary_5.txt");
            diccionario5 = getAssets().open("diccionario_5_noTildes.txt");
            bothDictionaries5 = getAssets().open("both_dictionaries_5.txt");
        } catch (IOException e) {
            Log.i("IOException", "Excepción IO a la hora de inicializar diccionarios.");
            throw new RuntimeException(e);
        }

        //File dictionary5 = new File("src/main/assets/dictionary_5.txt"); //diccionario inglés
        //File diccionario5 = new File(this.getFilesDir(), "diccionario_5_noTildes.txt"); //diccionario español sin tildes
        //File bothDictionaries5 = new File(this.getFilesDir(), "both_dictionaries_5.txt"); //diccionarios juntos

        //Scanner scanner = new Scanner(System.in);
        int length_dictionary = 0; //por algún motivo, length() da valores extraños

        lingle = new Lingle(bothDictionaries5); //inicializa lingle con el diccionario
        if (chooseDictionary() == 0) {
            length_dictionary = 8937;
            lingle.setCorrect_word(dictionary5, length_dictionary);
            chosen_dictionary = "inglés";
        }
        else {
            length_dictionary = 5537;
            lingle.setCorrect_word(diccionario5, length_dictionary);
            chosen_dictionary = "español";
        }
        Log.i("PALABRA CORRECTA", "La palabra correcta, del "+chosen_dictionary+", es: "+lingle.getCorrect_word());

        //preparación de botones del teclado
        buttonQ = (Button) findViewById(R.id.buttonQ);
        buttonQ.setOnClickListener(this); //llamada al método onClick
        buttonW = (Button) findViewById(R.id.buttonW);
        buttonW.setOnClickListener(this);
        buttonE = (Button) findViewById(R.id.buttonE);
        buttonE.setOnClickListener(this);
        buttonR = (Button) findViewById(R.id.buttonR);
        buttonR.setOnClickListener(this);
        buttonT = (Button) findViewById(R.id.buttonT);
        buttonT.setOnClickListener(this);
        buttonY = (Button) findViewById(R.id.buttonY);
        buttonY.setOnClickListener(this);
        buttonU = (Button) findViewById(R.id.buttonU);
        buttonU.setOnClickListener(this);
        buttonI = (Button) findViewById(R.id.buttonI);
        buttonI.setOnClickListener(this);
        buttonO = (Button) findViewById(R.id.buttonO);
        buttonO.setOnClickListener(this);
        buttonP = (Button) findViewById(R.id.buttonP);
        buttonP.setOnClickListener(this);
        buttonA = (Button) findViewById(R.id.buttonA);
        buttonA.setOnClickListener(this);
        buttonS = (Button) findViewById(R.id.buttonS);
        buttonS.setOnClickListener(this);
        buttonD = (Button) findViewById(R.id.buttonD);
        buttonD.setOnClickListener(this);
        buttonF = (Button) findViewById(R.id.buttonF);
        buttonF.setOnClickListener(this);
        buttonG = (Button) findViewById(R.id.buttonG);
        buttonG.setOnClickListener(this);
        buttonH = (Button) findViewById(R.id.buttonH);
        buttonH.setOnClickListener(this);
        buttonJ = (Button) findViewById(R.id.buttonJ);
        buttonJ.setOnClickListener(this);
        buttonK = (Button) findViewById(R.id.buttonK);
        buttonK.setOnClickListener(this);
        buttonL = (Button) findViewById(R.id.buttonL);
        buttonL.setOnClickListener(this);
        buttonEnye = (Button) findViewById(R.id.buttonEnye);
        buttonEnye.setOnClickListener(this);
        buttonZ = (Button) findViewById(R.id.buttonZ);
        buttonZ.setOnClickListener(this);
        buttonX = (Button) findViewById(R.id.buttonX);
        buttonX.setOnClickListener(this);
        buttonC = (Button) findViewById(R.id.buttonC);
        buttonC.setOnClickListener(this);
        buttonV = (Button) findViewById(R.id.buttonV);
        buttonV.setOnClickListener(this);
        buttonB = (Button) findViewById(R.id.buttonB);
        buttonB.setOnClickListener(this);
        buttonN = (Button) findViewById(R.id.buttonN);
        buttonN.setOnClickListener(this);
        buttonM = (Button) findViewById(R.id.buttonM);
        buttonM.setOnClickListener(this);
        //estos botones realizarán funciones más específicas
        Button buttonEnter = (Button) findViewById(R.id.buttonEnter);
        buttonEnter.setOnClickListener(this);
        Button buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(this);
        ImageView imageViewInfo = (ImageView) findViewById(R.id.imageViewReglas);
        imageViewInfo.setOnClickListener(this);
        ImageView imageViewBack = (ImageView) findViewById(R.id.imageViewBackButton);
        imageViewBack.setOnClickListener(this);
        ImageView imageViewReset = (ImageView) findViewById(R.id.imageViewResetButton);
        imageViewReset.setOnClickListener(this);

        ImageView moon = (ImageView) findViewById(R.id.imageViewMoon);
        ImageView logo = (ImageView) findViewById(R.id.imageViewLingle);
        ImageView info = (ImageView) findViewById(R.id.imageViewReglas);
        //este switch activa o desactiva el modo oscuro si se se le pulsa
        Switch switchDarkMode = (Switch) findViewById(R.id.switchDarkMode);
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchDarkMode.isChecked()) {
                    layoutJugar.setBackgroundResource(R.color.primary);
                    moon.setImageResource(R.drawable.dark_mode_white);
                    logo.setImageResource(R.drawable.lingle_text_white);
                    info.setImageResource(R.drawable.android_information_icon_white);
                    imageViewBack.setImageResource(R.drawable.back_button_light);
                    imageViewReset.setImageResource(R.drawable.restart_button_light);
                    for(int i=0;i<5;i++){
                        if (!isRow1Filled){
                            row1[i].setBackground(getDrawable(R.drawable.border));
                            row1[i].setTextColor(getResources().getColor(R.color.white));
                        }
                        if (!isRow2Filled) {
                            row2[i].setBackground(getDrawable(R.drawable.border));
                            row2[i].setTextColor(getResources().getColor(R.color.white));
                        }
                        if (!isRow3Filled) {
                            row3[i].setBackground(getDrawable(R.drawable.border));
                            row3[i].setTextColor(getResources().getColor(R.color.white));
                        }
                        if (!isRow4Filled) {
                            row4[i].setBackground(getDrawable(R.drawable.border));
                            row4[i].setTextColor(getResources().getColor(R.color.white));
                        }
                        if (!isRow5Filled) {
                            row5[i].setBackground(getDrawable(R.drawable.border));
                            row5[i].setTextColor(getResources().getColor(R.color.white));
                        }
                        if (!isRow6Filled) {
                            row6[i].setBackground(getDrawable(R.drawable.border));
                            row6[i].setTextColor(getResources().getColor(R.color.white));
                        }
                    }
                }
                else {
                    layoutJugar.setBackgroundResource(R.color.primaryLight);
                    moon.setImageResource(R.drawable.dark_mode_black);
                    logo.setImageResource(R.drawable.lingle_text);
                    info.setImageResource(R.drawable.android_information_icon);
                    imageViewBack.setImageResource(R.drawable.back_button_dark);
                    imageViewReset.setImageResource(R.drawable.restart_button_dark);
                    for(int i=0;i<5;i++){
                        if (!isRow1Filled){
                            row1[i].setBackground(getDrawable(R.drawable.border_light));
                            row1[i].setTextColor(getResources().getColor(R.color.black));
                        }
                        if (!isRow2Filled) {
                            row2[i].setBackground(getDrawable(R.drawable.border_light));
                            row2[i].setTextColor(getResources().getColor(R.color.black));
                        }
                        if (!isRow3Filled) {
                            row3[i].setBackground(getDrawable(R.drawable.border_light));
                            row3[i].setTextColor(getResources().getColor(R.color.black));
                        }
                        if (!isRow4Filled) {
                            row4[i].setBackground(getDrawable(R.drawable.border_light));
                            row4[i].setTextColor(getResources().getColor(R.color.black));
                        }
                        if (!isRow5Filled) {
                            row5[i].setBackground(getDrawable(R.drawable.border_light));
                            row5[i].setTextColor(getResources().getColor(R.color.black));
                        }
                        if (!isRow6Filled) {
                            row6[i].setBackground(getDrawable(R.drawable.border_light));
                            row6[i].setTextColor(getResources().getColor(R.color.black));
                        }
                    }
                }
            }
        });

        //inicialización de arrays de TextViews por cada fila
        for (int i=0;i<5;i++)
            row1[i] = new TextView(this);
        for (int i=0;i<5;i++)
            row2[i] = new TextView(this);
        for (int i=0;i<5;i++)
            row3[i] = new TextView(this);
        for (int i=0;i<5;i++)
            row4[i] = new TextView(this);
        for (int i=0;i<5;i++)
            row5[i] = new TextView(this);
        for (int i=0;i<5;i++)
            row6[i] = new TextView(this);

        //preparación de casillas a rellenar
        row1[0] = (TextView) findViewById(R.id.textViewEj1_1);
        row1[1] = (TextView) findViewById(R.id.textViewTile2Row1);
        row1[2] = (TextView) findViewById(R.id.textViewTile3Row1);
        row1[3] = (TextView) findViewById(R.id.textViewTile4Row1);
        row1[4] = (TextView) findViewById(R.id.textViewTile5Row1);
        row2[0] = (TextView) findViewById(R.id.textViewTile1Row2);
        row2[1] = (TextView) findViewById(R.id.textViewTile2Row2);
        row2[2] = (TextView) findViewById(R.id.textViewTile3Row2);
        row2[3] = (TextView) findViewById(R.id.textViewTile4Row2);
        row2[4] = (TextView) findViewById(R.id.textViewTile5Row2);
        row3[0] = (TextView) findViewById(R.id.textViewTile1Row3);
        row3[1] = (TextView) findViewById(R.id.textViewTile2Row3);
        row3[2] = (TextView) findViewById(R.id.textViewTile3Row3);
        row3[3] = (TextView) findViewById(R.id.textViewTile4Row3);
        row3[4] = (TextView) findViewById(R.id.textViewTile5Row3);
        row4[0] = (TextView) findViewById(R.id.textViewTile1Row4);
        row4[1] = (TextView) findViewById(R.id.textViewTile2Row4);
        row4[2] = (TextView) findViewById(R.id.textViewTile3Row4);
        row4[3] = (TextView) findViewById(R.id.textViewTile4Row4);
        row4[4] = (TextView) findViewById(R.id.textViewTile5Row4);
        row5[0] = (TextView) findViewById(R.id.textViewTile1Row5);
        row5[1] = (TextView) findViewById(R.id.textViewTile2Row5);
        row5[2] = (TextView) findViewById(R.id.textViewTile3Row5);
        row5[3] = (TextView) findViewById(R.id.textViewTile4Row5);
        row5[4] = (TextView) findViewById(R.id.textViewTile5Row5);
        row6[0] = (TextView) findViewById(R.id.textViewTile1Row6);
        row6[1] = (TextView) findViewById(R.id.textViewTile2Row6);
        row6[2] = (TextView) findViewById(R.id.textViewTile3Row6);
        row6[3] = (TextView) findViewById(R.id.textViewTile4Row6);
        row6[4] = (TextView) findViewById(R.id.textViewTile5Row6);
    }

    //0 selecciona un diccionario, 1 selecciona el otro
    public static int chooseDictionary(){
        int rng = (int)(Math.random() * 2);
        return rng;
    }

    public void setTilesChar (String c) { //muestra el caracter pulsado en su correspondiente casilla
        if (countRow == 1){
            if (countTile == 1) {
                row1[0].setText(c);
                countTile++;
            } else if (countTile == 2) {
                row1[1].setText(c);
                countTile++;
            } else if (countTile == 3) {
                row1[2].setText(c);
                countTile++;
            } else if (countTile == 4) {
                row1[3].setText(c);
                countTile++;
            } else if (countTile == 5) {
                row1[4].setText(c);
                countTile++; //aquí pasaría a 6, por lo que no se añadiría ninguna letra hasta que no se borre una
            }
        } else if (countRow == 2) {
            if (countTile == 1) {
                row2[0].setText(c);
                countTile++;
            } else if (countTile == 2) {
                row2[1].setText(c);
                countTile++;
            } else if (countTile == 3) {
                row2[2].setText(c);
                countTile++;
            } else if (countTile == 4) {
                row2[3].setText(c);
                countTile++;
            } else if (countTile == 5) {
                row2[4].setText(c);
                countTile++;
            }
        } else if (countRow == 3) {
            if (countTile == 1) {
                row3[0].setText(c);
                countTile++;
            } else if (countTile == 2) {
                row3[1].setText(c);
                countTile++;
            } else if (countTile == 3) {
                row3[2].setText(c);
                countTile++;
            } else if (countTile == 4) {
                row3[3].setText(c);
                countTile++;
            } else if (countTile == 5) {
                row3[4].setText(c);
                countTile++;
            }
        } else if (countRow == 4) {
            if (countTile == 1) {
                row4[0].setText(c);
                countTile++;
            } else if (countTile == 2) {
                row4[1].setText(c);
                countTile++;
            } else if (countTile == 3) {
                row4[2].setText(c);
                countTile++;
            } else if (countTile == 4) {
                row4[3].setText(c);
                countTile++;
            } else if (countTile == 5) {
                row4[4].setText(c);
                countTile++;
            }
        } else if (countRow == 5) {
            if (countTile == 1) {
                row5[0].setText(c);
                countTile++;
            } else if (countTile == 2) {
                row5[1].setText(c);
                countTile++;
            } else if (countTile == 3) {
                row5[2].setText(c);
                countTile++;
            } else if (countTile == 4) {
                row5[3].setText(c);
                countTile++;
            } else if (countTile == 5) {
                row5[4].setText(c);
                countTile++;
            }
        } else if (countRow == 6) {
            if (countTile == 1) {
                row6[0].setText(c);
                countTile++;
            } else if (countTile == 2) {
                row6[1].setText(c);
                countTile++;
            } else if (countTile == 3) {
                row6[2].setText(c);
                countTile++;
            } else if (countTile == 4) {
                row6[3].setText(c);
                countTile++;
            } else if (countTile == 5) {
                row6[4].setText(c);
                countTile++;
            }
        }
    }

    public void deleteTile() { //borra una letra introducida
        if (countRow == 1){
            //no hará nada si countTile vale 1, ya que estaría toda la fila vacía
            if (countTile == 2) {
                row1[0].setText("");
                countTile--;
            } else if (countTile == 3) {
                row1[1].setText("");
                countTile--;
            } else if (countTile == 4) {
                row1[2].setText("");
                countTile--;
            } else if (countTile == 5) {
                row1[3].setText("");
                countTile--;
            } else if (countTile == 6) {
                row1[4].setText("");
                countTile--; //aquí pasaría a 5, por lo que se podría añadir una última letra de nuevo
            }
        } else if (countRow == 2) {
            if (countTile == 2) {
                row2[0].setText("");
                countTile--;
            } else if (countTile == 3) {
                row2[1].setText("");
                countTile--;
            } else if (countTile == 4) {
                row2[2].setText("");
                countTile--;
            } else if (countTile == 5) {
                row2[3].setText("");
                countTile--;
            } else if (countTile == 6) {
                row2[4].setText("");
                countTile--;
            }
        } else if (countRow == 3) {
            if (countTile == 2) {
                row3[0].setText("");
                countTile--;
            } else if (countTile == 3) {
                row3[1].setText("");
                countTile--;
            } else if (countTile == 4) {
                row3[2].setText("");
                countTile--;
            } else if (countTile == 5) {
                row3[3].setText("");
                countTile--;
            } else if (countTile == 6) {
                row3[4].setText("");
                countTile--;
            }
        } else if (countRow == 4) {
            if (countTile == 2) {
                row4[0].setText("");
                countTile--;
            } else if (countTile == 3) {
                row4[1].setText("");
                countTile--;
            } else if (countTile == 4) {
                row4[2].setText("");
                countTile--;
            } else if (countTile == 5) {
                row4[3].setText("");
                countTile--;
            } else if (countTile == 6) {
                row4[4].setText("");
                countTile--;
            }
        } else if (countRow == 5) {
            if (countTile == 2) {
                row5[0].setText("");
                countTile--;
            } else if (countTile == 3) {
                row5[1].setText("");
                countTile--;
            } else if (countTile == 4) {
                row5[2].setText("");
                countTile--;
            } else if (countTile == 5) {
                row5[3].setText("");
                countTile--;
            } else if (countTile == 6) {
                row5[4].setText("");
                countTile--;
            }
        } else if (countRow == 6) {
            if (countTile == 2) {
                row6[0].setText("");
                countTile--;
            } else if (countTile == 3) {
                row6[1].setText("");
                countTile--;
            } else if (countTile == 4) {
                row6[2].setText("");
                countTile--;
            } else if (countTile == 5) {
                row6[3].setText("");
                countTile--;
            } else if (countTile == 6) {
                row6[4].setText("");
                countTile--;
            }
        }
    }

    public void pressEnter() { //a realizar al pulsar enter
        if (countTile == 6) { //si no tiene 5 letras, no se comprueba si está en el diccionario
            if (countRow == 1) //seleccionamos las casilla por cada fila
                guess = (String) row1[0].getText() + row1[1].getText() + row1[2].getText()
                        + row1[3].getText() + row1[4].getText(); //une cada letra de cada casilla
            else if (countRow == 2)
                guess = (String) row2[0].getText() + row2[1].getText() + row2[2].getText()
                        + row2[3].getText() + row2[4].getText();
            else if (countRow == 3)
                guess = (String) row3[0].getText() + row3[1].getText() + row3[2].getText()
                        + row3[3].getText() + row3[4].getText();
            else if (countRow == 4)
                guess = (String) row4[0].getText() + row4[1].getText() + row4[2].getText()
                        + row4[3].getText() + row4[4].getText();
            else if (countRow == 5)
                guess = (String) row5[0].getText() + row5[1].getText() + row5[2].getText()
                        + row5[3].getText() + row5[4].getText();
            else if (countRow == 6)
                guess = (String) row6[0].getText() + row6[1].getText() + row6[2].getText()
                        + row6[3].getText() + row6[4].getText();
            Log.i("GUESS", "El valor de guess es: "+guess);

            if (lingle.isInDictionary(guess)) { //si la palabra está en el diccionario, la procesamos
                lingle.setGuess(guess);
                String yellowGreen = lingle.returnYellowGreen(); //recogemos el procesamiento de la palabra
                numIntentos++; //aumenta el número de intentos por cada intento válido, hasta llegar a 6
                //animación para dar la vuelta a las casillas cuando se completa una fila
                Animation animationFlip = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip_in); //animación inicial
                Animation animationFlipOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flip_out); //animación que revela la fila de nuevo
                //establecemos el Intent para iniciar la actividad de victoria
                Intent intentResultadoAct = new Intent(getApplicationContext(), ResultadoActivity.class);
                if (countRow == 1) {
                    animationFlip.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) { //se ajusta que al acabar el primer flip, se rellenen los colores y se dé la vuelta
                            for (int i=0;i<5;i++){
                                if (yellowGreen.charAt(i) == 'X') {
                                    row1[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.greenTile));
                                    resultadoLingle.append("\uD83D\uDFE9"); //emoji de cuadrado verde
                                    setKeysColor(guess.charAt(i), 1);
                                }
                                else if (yellowGreen.charAt(i) == '/') {
                                    row1[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.yellowTile));
                                    setKeysColor(guess.charAt(i), 2);
                                    resultadoLingle.append("\uD83D\uDFE8"); //emoji de cuadrado amarillo
                                }

                                else if (yellowGreen.charAt(i) == '-') {
                                    row1[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.blackTile));
                                    setKeysColor(guess.charAt(i), 3);
                                    resultadoLingle.append("⬛"); //emoji de cuadrado negro
                                }
                                row1[i].setTextColor(getResources().getColor(R.color.white));
                                row1[i].startAnimation(animationFlipOut);
                            }
                            resultadoLingle.append("\n"); //salto de línea para la siguiente fila de emojis
                            if (lingle.getCorrect_word().equals(guess))
                                Toast.makeText(JugarActivity.this, "¡Buen trabajo!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    for (int i=0;i<5;i++) //se inicia la animación para todas las filas
                        row1[i].startAnimation(animationFlip);
                    isRow1Filled = true; //se pasa a true para que no se cambie el color de esta fila al cambiar el modo oscuro
                    Log.i("Info resultado", resultadoLingle.toString());
                } else if (countRow == 2) {
                    animationFlip.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            for (int i=0;i<5;i++){
                                if (yellowGreen.charAt(i) == 'X') {
                                    row2[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.greenTile));
                                    setKeysColor(guess.charAt(i), 1);
                                    resultadoLingle.append("\uD83D\uDFE9");
                                }
                                else if (yellowGreen.charAt(i) == '/') {
                                    row2[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.yellowTile));
                                    setKeysColor(guess.charAt(i), 2);
                                    resultadoLingle.append("\uD83D\uDFE8");
                                }
                                else if (yellowGreen.charAt(i) == '-') {
                                    row2[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.blackTile));
                                    setKeysColor(guess.charAt(i), 3);
                                    resultadoLingle.append("⬛");
                                }
                                row2[i].setTextColor(getResources().getColor(R.color.white));
                                row2[i].startAnimation(animationFlipOut);
                            }
                            resultadoLingle.append("\n");
                            if (lingle.getCorrect_word().equals(guess))
                                Toast.makeText(JugarActivity.this, "¡Buen trabajo!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    for (int i=0;i<5;i++)
                        row2[i].startAnimation(animationFlip);
                    isRow2Filled = true;
                    Log.i("Info resultado", resultadoLingle.toString());
                } else if (countRow == 3) {
                    animationFlip.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            for (int i=0;i<5;i++){
                                if (yellowGreen.charAt(i) == 'X') {
                                    row3[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.greenTile));
                                    setKeysColor(guess.charAt(i), 1);
                                    resultadoLingle.append("\uD83D\uDFE9");
                                }
                                else if (yellowGreen.charAt(i) == '/') {
                                    row3[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.yellowTile));
                                    setKeysColor(guess.charAt(i), 2);
                                    resultadoLingle.append("\uD83D\uDFE8");
                                }
                                else if (yellowGreen.charAt(i) == '-') {
                                    row3[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.blackTile));
                                    setKeysColor(guess.charAt(i), 3);
                                    resultadoLingle.append("⬛");
                                }
                                row3[i].setTextColor(getResources().getColor(R.color.white));
                                row3[i].startAnimation(animationFlipOut);
                            }
                            resultadoLingle.append("\n");
                            if (lingle.getCorrect_word().equals(guess))
                                Toast.makeText(JugarActivity.this, "¡Buen trabajo!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    for (int i=0;i<5;i++)
                        row3[i].startAnimation(animationFlip);
                    isRow3Filled = true;
                } else if (countRow == 4) {
                    animationFlip.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            for (int i=0;i<5;i++){
                                if (yellowGreen.charAt(i) == 'X') {
                                    row4[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.greenTile));
                                    setKeysColor(guess.charAt(i), 1);
                                    resultadoLingle.append("\uD83D\uDFE9");
                                }
                                else if (yellowGreen.charAt(i) == '/') {
                                    row4[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.yellowTile));
                                    setKeysColor(guess.charAt(i), 2);
                                    resultadoLingle.append("\uD83D\uDFE8");
                                }
                                else if (yellowGreen.charAt(i) == '-') {
                                    row4[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.blackTile));
                                    setKeysColor(guess.charAt(i), 3);
                                    resultadoLingle.append("⬛");
                                }
                                row4[i].setTextColor(getResources().getColor(R.color.white));
                                row4[i].startAnimation(animationFlipOut);
                            }
                            resultadoLingle.append("\n");
                            if (lingle.getCorrect_word().equals(guess))
                                Toast.makeText(JugarActivity.this, "¡Buen trabajo!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    for (int i=0;i<5;i++)
                        row4[i].startAnimation(animationFlip);
                    isRow4Filled = true;
                } else if (countRow == 5) {
                    animationFlip.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            for (int i=0;i<5;i++){
                                if (yellowGreen.charAt(i) == 'X') {
                                    row5[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.greenTile));
                                    setKeysColor(guess.charAt(i), 1);
                                    resultadoLingle.append("\uD83D\uDFE9");
                                }
                                else if (yellowGreen.charAt(i) == '/') {
                                    row5[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.yellowTile));
                                    setKeysColor(guess.charAt(i), 2);
                                    resultadoLingle.append("\uD83D\uDFE8");
                                }
                                else if (yellowGreen.charAt(i) == '-') {
                                    row5[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.blackTile));
                                    setKeysColor(guess.charAt(i), 3);
                                    resultadoLingle.append("⬛");
                                }
                                row5[i].setTextColor(getResources().getColor(R.color.white));
                                row5[i].startAnimation(animationFlipOut);
                            }
                            resultadoLingle.append("\n");
                            if (lingle.getCorrect_word().equals(guess))
                                Toast.makeText(JugarActivity.this, "¡Buen trabajo!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    for (int i=0;i<5;i++)
                        row5[i].startAnimation(animationFlip);
                    isRow5Filled = true;
                } else if (countRow == 6) {
                    animationFlip.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            for (int i=0;i<5;i++){
                                if (yellowGreen.charAt(i) == 'X') {
                                    row6[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.greenTile));
                                    setKeysColor(guess.charAt(i), 1);
                                    resultadoLingle.append("\uD83D\uDFE9");
                                }
                                else if (yellowGreen.charAt(i) == '/') {
                                    row6[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.yellowTile));
                                    setKeysColor(guess.charAt(i), 2);
                                    resultadoLingle.append("\uD83D\uDFE8");
                                }
                                else if (yellowGreen.charAt(i) == '-') {
                                    row6[i].setBackgroundColor(ContextCompat.getColor(JugarActivity.this, R.color.blackTile));
                                    setKeysColor(guess.charAt(i), 3);
                                    resultadoLingle.append("⬛");
                                }
                                row6[i].setTextColor(getResources().getColor(R.color.white));
                                row6[i].startAnimation(animationFlipOut);
                            }
                            resultadoLingle.append("\n");
                            if (lingle.getCorrect_word().equals(guess))
                                Toast.makeText(JugarActivity.this, "¡Buen trabajo!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    for (int i=0;i<5;i++)
                        row6[i].startAnimation(animationFlip);
                    isRow6Filled = true;
                }
                if (lingle.getCorrect_word().equals(guess)) {
                    countRow = 0; //si la palabra es correcta, ya no se podrá escribir más

                    //añadimos datos a pasar al nuevo activity
                    intentResultadoAct.putExtra("numTries_key", ""+numIntentos);
                    intentResultadoAct.putExtra("lossNotification_key", ""); //no se pasa nada si no se pierde
                    intentResultadoAct.putExtra("correctWord_key", guess);
                    intentResultadoAct.putExtra("textView_key", "La palabra correcta era "+guess+
                            ", del diccionario "+chosen_dictionary);
                    //nos aseguramos de que se pasa el último valor al StringBuilder
                    for (int i=0;i<5;i++){
                        if (yellowGreen.charAt(i) == 'X')
                            resultadoLingle.append("\uD83D\uDFE9");
                        else if (yellowGreen.charAt(i) == '/')
                            resultadoLingle.append("\uD83D\uDFE8");
                        else if (yellowGreen.charAt(i) == '-')
                            resultadoLingle.append("⬛");
                    }
                    intentResultadoAct.putExtra("lingleResult_key", resultadoLingle.toString());
                    if (chosen_dictionary == "español") //si la palabra es española busca significado, si no busca meaning
                        intentResultadoAct.putExtra("wordSearch_key", guess + "+significado");
                    else intentResultadoAct.putExtra("wordSearch_key", guess + "+meaning");

                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intentResultadoAct);
                        }
                    };
                    handler.postDelayed(runnable, 2000);
                    /**
                    //PendingIntent para darle un tiempo antes de cambiar de activity
                    PendingIntent pIntentResultadoAct = PendingIntent.getActivity(getApplicationContext(), 10, intentResultadoAct, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmResultadoAct = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
                    //el Intent se iniciará pasado un segundo (1000 milisegundos)
                    alarmResultadoAct.setExact(AlarmManager.RTC, 1000, pIntentResultadoAct);
                     */
                }
                else {
                    countRow++; //pasamos a la siguiente fila
                    countTile = 1; //restauramos a la primera posición de la fila
                }
                if (numIntentos == 6 && !lingle.getCorrect_word().equals(guess)){
                    Toast.makeText(JugarActivity.this, "¡Otra vez será!", Toast.LENGTH_SHORT).show();
                    //si se terminan los intentos sin acertar, se pasan otros datos al Intent
                    intentResultadoAct.putExtra("numTries_key", "X"); //se pasa X como número de intentos al perder
                    intentResultadoAct.putExtra("lossNotification_key", "¡Una pena!"); //mensaje al perder
                    intentResultadoAct.putExtra("correctWord_key", lingle.getCorrect_word());
                    intentResultadoAct.putExtra("textView_key", "La palabra correcta era "+lingle.getCorrect_word()+
                            ", del diccionario "+chosen_dictionary);
                    //nos aseguramos de que se pasa el último valor al StringBuilder
                    for (int i=0;i<5;i++){
                        if (yellowGreen.charAt(i) == 'X')
                            resultadoLingle.append("\uD83D\uDFE9");
                        else if (yellowGreen.charAt(i) == '/')
                            resultadoLingle.append("\uD83D\uDFE8");
                        else if (yellowGreen.charAt(i) == '-')
                            resultadoLingle.append("⬛");
                    }
                    intentResultadoAct.putExtra("lingleResult_key", resultadoLingle.toString());
                    if (chosen_dictionary == "español") //si la palabra es española busca significado, si no busca meaning
                        intentResultadoAct.putExtra("wordSearch_key", lingle.getCorrect_word() + "+significado");
                    else intentResultadoAct.putExtra("wordSearch_key", lingle.getCorrect_word() + "+meaning");

                    //handler para darle un pequeño delay de 2 segundos antes de iniciar el activity final
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intentResultadoAct);
                        }
                    };
                    handler.postDelayed(runnable, 2000);
                    /**
                    //PendingIntent para darle un tiempo antes de cambiar de activity
                    PendingIntent pIntentResultadoAct = PendingIntent.getActivity(getApplicationContext(), 10, intentResultadoAct, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmResultadoAct = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
                    //el Intent se iniciará pasado un segundo (1000 milisegundos)
                    alarmResultadoAct.setExact(AlarmManager.RTC, 1000, pIntentResultadoAct);
                     */
                }
            } else {
                //animación de parpadeo cuando la palabra no está en el diccionario
                blinkWrongAnswer();
                Toast.makeText(this, "La palabra no está en el diccionario", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            //animación de parpadeo cuando no se rellena la fila entera
            blinkWrongAnswer();
            Toast.makeText(this, "No es una palabra de 5 letras", Toast.LENGTH_SHORT).show();
        }
    }

    private void blinkWrongAnswer() { //animación para cada fila cuando se introduce una palabra incorrecta
        Animation animationBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        if (countRow == 1) { //animación dependiendo de la fila
            for(int i=0;i<5;i++)
                row1[i].startAnimation(animationBlink);
        }
        else if (countRow == 2){
            for(int i=0;i<5;i++)
                row2[i].startAnimation(animationBlink);
        }
        else if (countRow == 3) {
            for(int i=0;i<5;i++)
                row3[i].startAnimation(animationBlink);
        }
        else if (countRow == 4) {
            for(int i=0;i<5;i++)
                row4[i].startAnimation(animationBlink);
        }
        else if (countRow == 5) {
            for(int i=0;i<5;i++)
                row5[i].startAnimation(animationBlink);
        }
        else if (countRow == 6) {
            for(int i=0;i<5;i++)
                row6[i].startAnimation(animationBlink);
        }
    }

    public void setKeysColor(char letter, int priority) { //toma una letra del teclado y su prioridad para cambiar su color
        //switch para cada letra del teclado
        switch (letter) {
            case 'Q':
                if (priority == 2 && prQ > 2)
                    prQ = 2;
                else if (priority == 1)
                    prQ = 1;
                setKeysColorByPriority(buttonQ, prQ);
                break;
            case 'W':
                if (priority == 2 && prW > 2)
                    prW = 2;
                else if (priority == 1)
                    prW = 1;
                setKeysColorByPriority(buttonW, prW);
                break;
            case 'E':
                if (priority == 2 && prE > 2)
                    prE = 2;
                else if (priority == 1)
                    prE = 1;
                setKeysColorByPriority(buttonE, prE);
                break;
            case 'R':
                if (priority == 2 && prR > 2)
                    prR = 2;
                else if (priority == 1)
                    prR = 1;
                setKeysColorByPriority(buttonR, prR);
                break;
            case 'T':
                if (priority == 2 && prT > 2)
                    prT = 2;
                else if (priority == 1)
                    prT = 1;
                setKeysColorByPriority(buttonT, prT);
                break;
            case 'Y':
                if (priority == 2 && prY > 2)
                    prY = 2;
                else if (priority == 1)
                    prY = 1;
                setKeysColorByPriority(buttonY, prY);
                break;
            case 'U':
                if (priority == 2 && prU > 2)
                    prU = 2;
                else if (priority == 1)
                    prU = 1;
                setKeysColorByPriority(buttonU, prU);
                break;
            case 'I':
                if (priority == 2 && prI > 2)
                    prI = 2;
                else if (priority == 1)
                    prI = 1;
                setKeysColorByPriority(buttonI, prI);
                break;
            case 'O':
                if (priority == 2 && prO > 2)
                    prO = 2;
                else if (priority == 1)
                    prO = 1;
                setKeysColorByPriority(buttonO, prO);
                break;
            case 'P':
                if (priority == 2 && prP > 2)
                    prP = 2;
                else if (priority == 1)
                    prP = 1;
                setKeysColorByPriority(buttonP, prP);
                break;
            case 'A':
                if (priority == 2 && prA > 2)
                    prA = 2;
                else if (priority == 1)
                    prA = 1;
                setKeysColorByPriority(buttonA, prA);
                break;
            case 'S':
                if (priority == 2 && prS > 2)
                    prS = 2;
                else if (priority == 1)
                    prS = 1;
                setKeysColorByPriority(buttonS, prS);
                break;
            case 'D':
                if (priority == 2 && prD > 2)
                    prD = 2;
                else if (priority == 1)
                    prD = 1;
                setKeysColorByPriority(buttonD, prD);
                break;
            case 'F':
                if (priority == 2 && prF > 2)
                    prF = 2;
                else if (priority == 1)
                    prF = 1;
                setKeysColorByPriority(buttonF, prF);
                break;
            case 'G':
                if (priority == 2 && prG > 2)
                    prG = 2;
                else if (priority == 1)
                    prG = 1;
                setKeysColorByPriority(buttonG, prG);
                break;
            case 'H':
                if (priority == 2 && prH > 2)
                    prH = 2;
                else if (priority == 1)
                    prH = 1;
                setKeysColorByPriority(buttonH, prH);
                break;
            case 'J':
                if (priority == 2 && prJ > 2)
                    prJ = 2;
                else if (priority == 1)
                    prJ = 1;
                setKeysColorByPriority(buttonJ, prJ);
                break;
            case 'K':
                if (priority == 2 && prK > 2)
                    prK = 2;
                else if (priority == 1)
                    prK = 1;
                setKeysColorByPriority(buttonK, prK);
                break;
            case 'L':
                if (priority == 2 && prL > 2)
                    prL = 2;
                else if (priority == 1)
                    prL = 1;
                setKeysColorByPriority(buttonL, prL);
                break;
            case 'Ñ':
                if (priority == 2 && prEnye > 2)
                    prEnye = 2;
                else if (priority == 1)
                    prEnye = 1;
                setKeysColorByPriority(buttonEnye, prEnye);
                break;
            case 'Z':
                if (priority == 2 && prZ > 2)
                    prZ = 2;
                else if (priority == 1)
                    prZ = 1;
                setKeysColorByPriority(buttonZ, prZ);
                break;
            case 'X':
                if (priority == 2 && prX > 2)
                    prX = 2;
                else if (priority == 1)
                    prX = 1;
                setKeysColorByPriority(buttonX, prX);
                break;
            case 'C':
                if (priority == 2 && prC > 2)
                    prC = 2;
                else if (priority == 1)
                    prC = 1;
                setKeysColorByPriority(buttonC, prC);
                break;
            case 'V':
                if (priority == 2 && prV > 2)
                    prV = 2;
                else if (priority == 1)
                    prV = 1;
                setKeysColorByPriority(buttonV, prV);
                break;
            case 'B':
                if (priority == 2 && prB > 2)
                    prB = 2;
                else if (priority == 1)
                    prB = 1;
                setKeysColorByPriority(buttonB, prB);
                break;
            case 'N':
                if (priority == 2 && prN > 2)
                    prN = 2;
                else if (priority == 1)
                    prN = 1;
                setKeysColorByPriority(buttonN, prN);
                break;
            case 'M':
                if (priority == 2 && prM > 2)
                    prM = 2;
                else if (priority == 1)
                    prM = 1;
                setKeysColorByPriority(buttonM, prM);
                break;
            default: break;
        }
    }

    private void setKeysColorByPriority(Button key, int priorityColor) { //parte usada dentro de setKeysColor
        if (priorityColor == 1) { //pinta la tecla siempre si es verde
            key.setBackgroundColor(ContextCompat.getColor(this, R.color.greenTile));
        }
        else if (priorityColor == 2) { //pinta la tecla de amarillo en cualquier caso que no sea ya verde
            key.setBackgroundColor(ContextCompat.getColor(this, R.color.yellowTile));
        }
        else if (priorityColor == 3) { //pinta la tecla de negro si no es amarilla o verde
            key.setBackgroundColor(ContextCompat.getColor(this, R.color.blackTile));
        }
    }

    @Override
    public void onClick (View v) { //implementación de lo que hace cada botón pulsado
        //utilizar un switch es inviable en versiones nuevas de Android
        //Log.i("YES", "El ID pasado es "+ v.getId() +" y el ID de Q es "+ R.id.buttonQ);
        if (v.getId() == R.id.buttonQ)
            setTilesChar("Q");
        else if (v.getId() == R.id.buttonW)
            setTilesChar("W");
        else if (v.getId() == R.id.buttonE)
            setTilesChar("E");
        else if (v.getId() == R.id.buttonR)
            setTilesChar("R");
        else if (v.getId() == R.id.buttonT)
            setTilesChar("T");
        else if (v.getId() == R.id.buttonY)
            setTilesChar("Y");
        else if (v.getId() == R.id.buttonU)
            setTilesChar("U");
        else if (v.getId() == R.id.buttonI)
            setTilesChar("I");
        else if (v.getId() == R.id.buttonO)
            setTilesChar("O");
        else if (v.getId() == R.id.buttonP)
            setTilesChar("P");
        else if (v.getId() == R.id.buttonA)
            setTilesChar("A");
        else if (v.getId() == R.id.buttonS)
            setTilesChar("S");
        else if (v.getId() == R.id.buttonD)
            setTilesChar("D");
        else if (v.getId() == R.id.buttonF)
            setTilesChar("F");
        else if (v.getId() == R.id.buttonG)
            setTilesChar("G");
        else if (v.getId() == R.id.buttonH)
            setTilesChar("H");
        else if (v.getId() == R.id.buttonJ)
            setTilesChar("J");
        else if (v.getId() == R.id.buttonK)
            setTilesChar("K");
        else if (v.getId() == R.id.buttonL)
            setTilesChar("L");
        else if (v.getId() == R.id.buttonEnye)
            setTilesChar("Ñ");
        else if (v.getId() == R.id.buttonZ)
            setTilesChar("Z");
        else if (v.getId() == R.id.buttonX)
            setTilesChar("X");
        else if (v.getId() == R.id.buttonC)
            setTilesChar("C");
        else if (v.getId() == R.id.buttonV)
            setTilesChar("V");
        else if (v.getId() == R.id.buttonB)
            setTilesChar("B");
        else if (v.getId() == R.id.buttonN)
            setTilesChar("N");
        else if (v.getId() == R.id.buttonM)
            setTilesChar("M");

        else if (v.getId() == R.id.buttonDelete)
            deleteTile();
        else if (v.getId() == R.id.buttonEnter)
            pressEnter();
        else if (v.getId() == R.id.imageViewReglas)
            startActivity(new Intent(JugarActivity.this, ReglasActivity.class));
        else if (v.getId() == R.id.imageViewBackButton)
            finish(); //el botón atrás finaliza la actividad de jugar y vuelve a la pantalla de inicio
        else if (v.getId() == R.id.imageViewResetButton) {
            finish();
            startActivity(new Intent(JugarActivity.this, JugarActivity.class));
            //finaliza la actividad e inicia una nueva con otra palabra
        }
    }
}
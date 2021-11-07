package com.example.calculator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class CalculatorFragment extends Fragment {

    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Calculator");

        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        TextView textViewScreen = view.findViewById(R.id.screenTextView);

        view.findViewById(R.id.oneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append("1");
                Log.d("iss", "Button one clicked.");
            }
        });

        view.findViewById(R.id.twoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append("2");
                Log.d("iss", "Button two clicked.");
            }
        });
        view.findViewById(R.id.threeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append("3");
                Log.d("iss", "Button three clicked.");
            }
        });
        view.findViewById(R.id.fourButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append("4");
                Log.d("iss", "Button four clicked.");
            }
        });
        view.findViewById(R.id.fiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append("5");
                Log.d("iss", "Button five clicked.");
            }
        });
        view.findViewById(R.id.sixButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append("6");
                Log.d("iss", "Button six clicked.");
            }
        });
        view.findViewById(R.id.sevenButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append("7");
                Log.d("iss", "Button seven clicked.");
            }
        });
        view.findViewById(R.id.eightButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append("8");
                Log.d("iss", "Button eight clicked.");
            }
        });
        view.findViewById(R.id.nineButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append("9");
                Log.d("iss", "Button nine clicked.");
            }
        });
        view.findViewById(R.id.zeroButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append("0");
                Log.d("iss", "Button zero clicked.");
            }
        });

        view.findViewById(R.id.plusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append(" + ");
                Log.d("iss", "Button plus clicked.");
            }
        });
        view.findViewById(R.id.multiplyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append(" x ");
                Log.d("iss", "Button multiply clicked.");
            }
        });
        view.findViewById(R.id.divideButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append(" / ");
                Log.d("iss", "Button divide clicked.");
            }
        });
        view.findViewById(R.id.minusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.append(" - ");
                Log.d("iss", "Button minus clicked.");
            }
        });

        view.findViewById(R.id.equalButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] calculate = textViewScreen.getText().toString().split(" ");

                ArrayList<Double> numbers = new ArrayList<Double>();

                ArrayList<String> operators = new ArrayList<String>();

                for(int x = 0; x < calculate.length; x++){
                    if(StringUtils.isNumeric(calculate[x])){
                        numbers.add(Double.parseDouble(calculate[x]));
                    } else if(calculate[x].equals("x") || calculate[x].equals("/") || calculate[x].equals("-") || calculate[x].equals("+")){
                      operators.add(calculate[x]);
                    }
                }
                double result = 0.0;

                while(true){
                    if(operators.indexOf("x") >= 0) {
                        result = numbers.get(operators.indexOf("x")+1) * numbers.get(operators.indexOf("x"));
                        Log.d("iss", "result: " + result);
                        numbers.remove(operators.indexOf("x")+1);
                        numbers.remove(operators.indexOf("x"));

                        numbers.add(operators.indexOf("x"), result);
                        operators.remove(operators.indexOf("x"));

                    } else if(operators.indexOf("/") >= 0){
                        result =  numbers.get(operators.indexOf("/")) / numbers.get(operators.indexOf("/")+1);

                        numbers.remove(operators.indexOf("/")+1);
                        numbers.remove(operators.indexOf("/"));

                        numbers.add(operators.indexOf("/"), result);
                        operators.remove(operators.indexOf("/"));

                    } else if(operators.indexOf("+") >= 0){
                        result = numbers.get(operators.indexOf("+")+1) + numbers.get(operators.indexOf("+"));

                        numbers.remove(operators.indexOf("+")+1);
                        numbers.remove(operators.indexOf("+"));

                        numbers.add(operators.indexOf("+"), result);
                        operators.remove(operators.indexOf("+"));

                    } else if(operators.indexOf("-") >= 0){
                        result = numbers.get(operators.indexOf("-")) - numbers.get(operators.indexOf("-")+1) ;

                        numbers.remove(operators.indexOf("-")+1);
                        numbers.remove(operators.indexOf("-"));

                        numbers.add(operators.indexOf("-"), result);
                        operators.remove(operators.indexOf("-"));

                    } else{
                        textViewScreen.setText(Double.toString(result));
                        break;
                    }


                }
                for(int x=0; x<numbers.size(); x++){
                    Log.d("iss", "calculate: " + numbers.get(x));
                }
                for(int x=0; x<operators.size(); x++){
                    Log.d("iss", "operators: " + operators.get(x));
                }


            }
        });

        view.findViewById(R.id.clearButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewScreen.setText("");
                Log.d("iss", "Button clear clicked.");
            }
        });


        return view;
    }
}
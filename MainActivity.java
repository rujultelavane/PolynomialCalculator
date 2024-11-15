package com.example.polynomialcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {

    Button c, delete, x, carat, button7, button8, button9, multiply, button4, button5, button6, subtract, button1, button2, button3, add, button0, enter;
    TextView result;
    String expression="", token, display="";
    ArrayList<String> expressionList=new ArrayList<>();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c=findViewById(R.id.buttonC);
        delete=findViewById(R.id.buttonDelete);
        x=findViewById(R.id.buttonX);
        carat=findViewById(R.id.carat);
        button7=findViewById(R.id.button7);
        button8=findViewById(R.id.button8);
        button9=findViewById(R.id.button9);
        multiply=findViewById(R.id.multiply);
        button4=findViewById(R.id.button4);
        button5=findViewById(R.id.button5);
        button6=findViewById(R.id.button6);
        subtract=findViewById(R.id.subtract);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        add=findViewById(R.id.add);
        button0=findViewById(R.id.button0);
        enter=findViewById(R.id.buttonEnter);
        result=findViewById(R.id.textView);


        c.setBackgroundColor(Color.GRAY);
        delete.setBackgroundColor(Color.GRAY);
        x.setBackgroundColor(Color.GRAY);
        carat.setBackgroundColor(Color.GRAY);

        c.setOnClickListener(new ButtonInterface());
        delete.setOnClickListener(new ButtonInterface());
        x.setOnClickListener(new ButtonInterface());
        carat.setOnClickListener(new ButtonInterface());
        button7.setOnClickListener(new ButtonInterface());
        button8.setOnClickListener(new ButtonInterface());
        button9.setOnClickListener(new ButtonInterface());
        multiply.setOnClickListener(new ButtonInterface());
        button4.setOnClickListener(new ButtonInterface());
        button5.setOnClickListener(new ButtonInterface());
        button6.setOnClickListener(new ButtonInterface());
        subtract.setOnClickListener(new ButtonInterface());
        button1.setOnClickListener(new ButtonInterface());
        button2.setOnClickListener(new ButtonInterface());
        button3.setOnClickListener(new ButtonInterface());
        add.setOnClickListener(new ButtonInterface());
        button0.setOnClickListener(new ButtonInterface());
        enter.setOnClickListener(new ButtonInterface());

    }
    public void calculate(String e)
    {
        display="";
        StringTokenizer tokenizer = new StringTokenizer(e, "+-*", true);     //use StringTokenizer to break up the expression - include ops as delimiters
        while(tokenizer.hasMoreTokens()) {
            token = tokenizer.nextToken();
            expressionList.add(token);      //Add values to an arraylist
        }
        sort(expressionList);
        userErrors(expressionList);

        for(int x=0; x<expressionList.size(); x++){
            sort(expressionList);
            String term=expressionList.get(x);
            String term2=null;
            String operator; //operator
            if(x<expressionList.size()-2) {
                operator=expressionList.get(expressionList.indexOf(term)+1); //only when next to each other in pemdas
                term2 = expressionList.get(expressionList.indexOf(operator)+1);
            }
            else {
                operator="idk"; //placeholder
                term2="idk";
            }

            if(operator.equals("*")) {
                sort(expressionList);
                display += multiply(term, term2);
                expressionList.add(expressionList.indexOf(term), display);
                expressionList.remove(term);
                expressionList.remove(operator);
                expressionList.remove(term2);
                display=toString(expressionList);
            }

            if(operator.equals("+") || operator.equals("-")) {
                sort(expressionList);
                if(operator.equals("+"))
                    display += addAndSubtract(term, term2);
                if(operator.equals("-"))
                    display += addAndSubtract(term, "-"+term2);
                expressionList.add(expressionList.indexOf(term), display);
                expressionList.remove(term);
                expressionList.remove(operator);
                expressionList.remove(term2);
                display=toString(expressionList);
            }
            sort(expressionList);
        }
        result.setText(display);
    }

    public String multiply(String term1, String term2) {
        int num1=getCoefficient(term1);
        int num2=getCoefficient(term2);
        int exp1=0;
        int exp2=0;
        int exp=0;
        int product=num1*num2;

        if(term1.contains("x") || term2.contains("x")) { //if there is x
            exp1=getExponent(term1);
            exp2=getExponent(term2);
            exp=exp1+exp2;
        }
        else return String.valueOf(product);
        if(exp==1) //if the exponent is 1
        {
            return product + "x"; //don't put ^1
        }
        if(product==1)
            return "x^"+exp;
        return product +"x^"+ exp;
    }
    public String addAndSubtract(String term1, String term2) {
        int num1=getCoefficient(term1);
        int num2=getCoefficient(term2);
        int exp1=1;
        int exp2=1;
        int sum;
        if(term1.contains("x") && term2.contains("x"))//both have x
        {
            exp1=getExponent(term1); //get exponents
            exp2=getExponent(term2);

            if(exp1==exp2) //if the exponents are the same (only way you can add them together)
            {
                sum = num1 + num2; //add coefficients

                if (exp1 == 1)      //if the exponent is 1
                    return sum + "x";       //don't put ^1
                if(sum==1)      //if new coefficient is 1
                    return "x^"+exp1;       //don't put 1x
                return sum + "x^" + exp1;       //normal
            }
            else if(num2>0)
                return term1+"+"+term2; //if you can't add, return the same
            else return term1+"-"+term2;
        }
        else if(term1.contains("x") || term2.contains("x")) //one has x
        {
            return term1+"+"+term2;
        }
        else return String.valueOf(num1 + num2); //no x
    }
    public String toString(ArrayList<String> list) {
        String str="";
        for(String s : list)
            str += s;
        return str;
    }
    public static ArrayList<String> sort(ArrayList<String> list) {
        int count=0;
        for(int x=0; x<list.size(); x++)
        {
            if(list.get(x).equals("-")){ //find any minus signs
                list.set(x, "+"); //change to plus
                String term="-"+list.get(x+1); //adds minus onto term instead of having separate spot in array
                list.set(x+1, term);
            }
        }
        while(count<list.size()/2) {
            for (int x = 0; x < list.size() - 2; x++) {
                if (getExponent(list.get(x)) < getExponent(list.get(x + 2))) {
                    String temp = list.get(x);
                    list.set(x, list.get(x + 2));          //swaps
                    list.set(x + 2, temp);         //uses x+2 because dont want to count operators
                    //System.out.println(list);
                }
            }
            count++;
        }
        for(int x=0; x<list.size(); x++){
            if(list.get(x).contains("-")){
                list.set(x-1, "-");             //resets minus sign back to plus
                String temp=list.get(x);
                temp=temp.substring(1);
                list.set(x,temp);
            }
        }
        return list;

    }
    public static int getExponent(String term) {
        String exponent="";
        if(!term.contains("x"))
            return 0;
        if(!term.contains("^")) //if there is no exponent
            return 1; //exp is 1
        else return Integer.parseInt(term.substring(term.indexOf("^")+1));
    }
    public int getCoefficient(String term) {
        String coefficient="";
        for(int x=0; x<term.length(); x++)
        {
            if(!term.contains("x"))
            {
                coefficient=term;
                return Integer.parseInt(coefficient);
            }

            coefficient=term.substring(0, term.indexOf("x"));
        }
        return Integer.parseInt(coefficient);
    }
    public void userErrors(ArrayList<String> list) {
        /*
        - ends with carrot
        - ends with operator
        - starts with operator
        - double operators
         */
        //for(int x=0; x<expressionList.size()-1; x++)
        //{
            //String val=list.get(x);
            //String nextVal=list.get(x+1);
            String lastVal=list.get(list.size()-1);
            if(lastVal.substring(lastVal.length()-1).equals("^") || isOperator(lastVal.substring(lastVal.length()-1)))
            {
                expression = "ERROR";
                result.setText(expression);
            }
       /* ((isOperator(val) && isOperator(nextVal)) //two ops next to each other
                    || (!val.contains("^") && !isOperator(val))//no carrot
                    || (val.indexOf('^')==val.length()-1) //ends with carrot
                    || (isOperator(val.substring(val.length()-1)) && !isOperator(val)) //ends with operator
                    || (isOperator(val.substring(0, 1))&& !isOperator(val))) //starts with operator*/
    }
    public boolean isOperator(String str) {
        return (str.equals("+")||str.equals("-")||str.equals("*"));
    }
    public class ButtonInterface implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.button0) {
                expression += button0.getText();
                result.setText(expression);
            } else if (id == R.id.button1) {
                expression += button1.getText();
                result.setText(expression);
            } else if (id == R.id.button2) {
                expression += button2.getText();
                result.setText(expression);
            } else if (id == R.id.button3) {
                expression += button3.getText();
                result.setText(expression);
            } else if (id == R.id.button4) {
                expression += button4.getText();
                result.setText(expression);
            } else if (id == R.id.button5) {
                expression += button5.getText();
                result.setText(expression);
            } else if (id == R.id.button6) {
                expression += button6.getText();
                result.setText(expression);
            } else if (id == R.id.button7) {
                expression += button7.getText();
                result.setText(expression);
            } else if (id == R.id.button8) {
                expression += button8.getText();
                result.setText(expression);
            } else if (id == R.id.button9) {
                expression += button9.getText();
                result.setText(expression);
            } else if (id == R.id.buttonX) {
                expression += x.getText();
                result.setText(expression);
            } else if (id == R.id.add) {
                expression += add.getText();
                result.setText(expression);
            } else if (id == R.id.subtract) {
                expression += subtract.getText();
                result.setText(expression);
            } else if (id == R.id.multiply) {
                expression += multiply.getText();
                result.setText(expression);
            } else if (id == R.id.carat) {
                expression += carat.getText();
                result.setText(expression);
            } else if (id == R.id.buttonC) {
                expressionList.clear();
                expression = "";
                display="";
                result.setText("");
            } else if (id == R.id.buttonDelete) {
                expression = expression.substring(0, expression.length() - 1);
                result.setText(expression);
            }
            if (id == R.id.buttonEnter) {
                try {
                    calculate((String) result.getText());
                }catch(Exception e) {
                    result.setText("ERROR");
                }
            }
        }
    }


}
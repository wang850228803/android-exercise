
package com.example.myapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Calculate extends Activity implements OnClickListener {

    private Button pBtn;
    private Button mBtn;
    private Button muBtn;
    private Button dBtn;
    private EditText eText;
    private Button equal;
    
    static final String ACTION="com.example.action.MY_RECEIVER";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        
        eText=(EditText)findViewById(R.id.etext);
        pBtn=(Button)findViewById(R.id.plus);
        pBtn.setOnClickListener(this);
        mBtn=(Button)findViewById(R.id.minus);
        mBtn.setOnClickListener(this);
        muBtn=(Button)findViewById(R.id.mul);
        muBtn.setOnClickListener(this);
        dBtn=(Button)findViewById(R.id.div);    
        dBtn.setOnClickListener(this);
        /*equal=(Button)findViewById(R.id.equal);
        equal.setOnClickListener(this);*/
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.plus:
                eText.setText(eText.getText().append("+"));
                locateCursor();
                break;
            case R.id.minus:
                eText.setText(eText.getText().append("-"));
                locateCursor();
                break;
            case R.id.mul:
                eText.setText(eText.getText().append("*"));
                locateCursor();
                break;
            case R.id.div:
                eText.setText(eText.getText().append("/"));
                locateCursor();
                break;
            /*case R.id.equal:
                String res=computeStirngNoBracket(eText.getText().toString());
                eText.setText(res);
                locateCursor();
                Intent intent=new Intent(ACTION);
                sendBroadcast(intent);
                break;*/
            default:
                break;
        }
    }
    
    public void equal(View v) {
        String res=computeStirngNoBracket(eText.getText().toString());
        eText.setText(res);
    }
   
    private void locateCursor(){
        CharSequence text = eText.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable)text;
            Selection.setSelection(spanText, text.length());
        }
    }
    
    private static String computeStirngNoBracket(String string) {  
        string = string.replaceAll("(^\\()|(\\)$)", "");  
        String regexMultiAndDivision = "[\\d\\.]+(\\*|\\/)[\\d\\.]+";  
        String regexAdditionAndSubtraction = "(^\\-)?[\\d\\.]+(\\+|\\-)[\\d\\.]+";  
  
        String temp = "";  
        int index = -1;  
  
        // 解析乘除法  
        Pattern pattern = Pattern.compile(regexMultiAndDivision);  
        Matcher matcher = null;  
        while (pattern.matcher(string).find()) {  
            matcher = pattern.matcher(string);  
            if (matcher.find()) {  
                temp = matcher.group();  
                index = string.indexOf(temp);  
                string = string.substring(0, index) + doMultiAndDivision(temp)  
                        + string.substring(index + temp.length());  
            }  
        }  
  
        // 解析加减法  
        pattern = Pattern.compile(regexAdditionAndSubtraction);  
        while (pattern.matcher(string).find()) {  
            matcher = pattern.matcher(string);  
            if (matcher.find()) {  
                temp = matcher.group();  
                index = string.indexOf(temp);  
                if (temp.startsWith("-")) {  
                    string = string.substring(0, index)  
                            + doNegativeOperation(temp)  
                            + string.substring(index + temp.length());  
                } else {  
                    string = string.substring(0, index)  
                            + doAdditionAndSubtraction(temp)  
                            + string.substring(index + temp.length());  
                }  
            }  
        }  
  
        return string;  
    }  
  
    /** 
     * 执行乘除法 
     * @param string 
     * @return 
     * @author ZYWANG 2009-8-31 
     */  
    private static String doMultiAndDivision(String string) {  
        String value = "";  
        double d1 = 0;  
        double d2 = 0;  
        String[] temp = null;  
        if (string.contains("*")) {  
            temp = string.split("\\*");  
        } else {  
            temp = string.split("/");  
        }  
  
        if (temp.length < 2)  
            return string;  
  
        d1 = Double.valueOf(temp[0]);  
        d2 = Double.valueOf(temp[1]);  
        if (string.contains("*")) {  
            value = String.valueOf(d1 * d2);  
        } else {  
            value = String.valueOf(d1 / d2);  
        }  
  
        return value;  
    }  
  
    /** 
     * 执行加减法 
     * @param string 
     * @return 
     * @author ZYWANG 2009-8-31 
     */  
    private static String doAdditionAndSubtraction(String string) {  
        double d1 = 0;  
        double d2 = 0;  
        String[] temp = null;  
        String value = "";  
        if (string.contains("+")) {  
            temp = string.split("\\+");  
        } else {  
            temp = string.split("\\-");  
        }  
  
        if (temp.length < 2)  
            return string;  
  
        d1 = Double.valueOf(temp[0]);  
        d2 = Double.valueOf(temp[1]);  
        if (string.contains("+")) {  
            value = String.valueOf(d1 + d2);  
        } else {  
            value = String.valueOf(d1 - d2);  
        }  
  
        return value;  
    }  
  
    /** 
     * 执行负数运算 
     * @param string 
     * @return 
     * @author ZYWANG 2010-11-8 
     */  
    private static String doNegativeOperation(String string) {  
        String temp = string.substring(1);  
        if (temp.contains("+")) {  
            temp = temp.replace("+", "-");  
        } else {  
            temp = temp.replace("-", "+");  
        }  
        temp = doAdditionAndSubtraction(temp);  
        if (temp.startsWith("-")) {  
            temp = temp.substring(1);  
        } else {  
            temp = "-" + temp;  
        }  
        return temp;  
    }  
}

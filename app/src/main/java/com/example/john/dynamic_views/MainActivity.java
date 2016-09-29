package com.example.john.dynamic_views;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button calcGrade, addCourse;
    TextView gpaText;


    public static double determineGrade(String grade) {
        double doubleValue;

        switch (grade) {
            case "A":
                doubleValue = 4.0;
                break;
            case "A-":
                doubleValue = 3.7;
                break;
            case "B+":
                doubleValue = 3.3;
                break;
            case "B":
                doubleValue = 3.0;
                break;
            case "B-":
                doubleValue = 2.7;
                break;
            case "C+":
                doubleValue = 2.3;
                break;
            case "C":
                doubleValue = 2.0;
                break;
            case "C-":
                doubleValue = 1.7;
                break;
            case "D+":
                doubleValue = 1.5;
                break;
            case "D":
                doubleValue = 1.0;
                break;
            default:
                doubleValue = 0.0;
                break;
        }
        return doubleValue;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set variables to reference widgets in xml
        calcGrade = (Button) findViewById(R.id.calc_GPA_Button);
        addCourse = (Button) findViewById(R.id.add_course_button);
        gpaText = (TextView) findViewById(R.id.GPA_text);

        //prompt user to use buttons
        Toast toast = Toast.makeText(this, R.string.promptMessage, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        // Parent layout
        final LinearLayout parentLayout = (LinearLayout) findViewById(R.id.layout);

        // Layout inflater
        final LayoutInflater layoutInflater = getLayoutInflater();

        //initialize ArrayList to accept View editTexts
        final ArrayList<Spinner> courseGrade = new ArrayList<>();
        final ArrayList<EditText> unitAmount = new ArrayList<>();

        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                // Add the text layout to the parent layout
                final View view = layoutInflater.inflate(R.layout.text_layout, parentLayout, false);

                //Create reference for grade spinner
                final Spinner gradeSpinner = (Spinner) view.findViewById(R.id.gradeText);
                // Create an ArrayAdapter and a default spinner layout
                ArrayAdapter adapter = ArrayAdapter.createFromResource(MainActivity.this,
                        R.array.grades_Array, android.R.layout.simple_spinner_dropdown_item);
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                gradeSpinner.setAdapter(adapter);

                final EditText unitsText = (EditText) view.findViewById(R.id.unitLine);
                unitsText.setHint("Units");
                final EditText className = (EditText) view.findViewById(R.id.courseTitle);
                className.setHint("Course Name");

                Button removeCourse = (Button) view.findViewById(R.id.remove);

                removeCourse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parentLayout.removeView(view);
                        courseGrade.remove(gradeSpinner);
                    }

                });

                // Add the view to the parent layout which consist of all child views in xml file
                parentLayout.addView(view);
                //update arrays
                unitAmount.add(unitsText);
                courseGrade.add(gradeSpinner);
            }
        });

        calcGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double totalUnits = 0;
                double totalCourseGradePoints = 0;
                double doubleGrade;
                for (int i = 0; i < courseGrade.size(); i++) {

                    EditText unitLooper = unitAmount.get(i);
                    Spinner gradeLooper = courseGrade.get(i);
                    String userInput = unitLooper.getText().toString();

                    //check for empty values
                    if (userInput.equals("") || userInput.equals(".") || userInput.equals("0")) {
                        Toast errorMSG = Toast.makeText(MainActivity.this, R.string.unitValidation, Toast.LENGTH_LONG);
                        errorMSG.setGravity(Gravity.CENTER, 0, 0);
                        errorMSG.show();
                        break;
                    }


                    String textChoice = gradeLooper.getSelectedItem().toString();
                    doubleGrade = determineGrade(textChoice);

                    double points = doubleGrade * Double.parseDouble(unitLooper.getText().toString());

                    totalCourseGradePoints += points;
                    totalUnits += Double.parseDouble(unitLooper.getText().toString());
                }

                //calc gpa
                double avgGPA = (totalCourseGradePoints) / (totalUnits);
                DecimalFormat df = new DecimalFormat("#.##");
                String stringGPA = "GPA: " + df.format(avgGPA);
                gpaText.setText(stringGPA);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}

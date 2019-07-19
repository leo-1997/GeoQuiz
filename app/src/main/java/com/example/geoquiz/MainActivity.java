package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = {new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
};

    private int mCurrentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionTextView = findViewById(R.id.question_text_view);

        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mPrevButton = findViewById(R.id.prev_button);
        mNextButton = findViewById(R.id.next_button);

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentQuestionIndex = (++mCurrentQuestionIndex) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentQuestionIndex == 0) {
                    Toast.makeText(MainActivity.this, R.string.first_toast, Toast.LENGTH_SHORT).show();
                } else {
                    mCurrentQuestionIndex = (--mCurrentQuestionIndex) % mQuestionBank.length;
                    updateQuestion();
                }
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                mCurrentQuestionIndex = (++mCurrentQuestionIndex) % mQuestionBank.length;
                updateQuestion();
            }
        });

        updateQuestion();
    }

    private void updateQuestion() {
        mQuestionTextView.setText(mQuestionBank[mCurrentQuestionIndex].getTextResId());
    }

    private void checkAnswer(boolean userAnswer) {
        boolean answerIsTrue = mQuestionBank[mCurrentQuestionIndex].isAnswerTrue();

        int messageResId;

        messageResId = (userAnswer == answerIsTrue ? R.string.correct_toast : R.string.wrong_toast);

        Toast toast = Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
}

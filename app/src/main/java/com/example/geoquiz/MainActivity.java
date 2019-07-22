package com.example.geoquiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANS = "answer";
    private static final String KEY_CHEAT = "cheat";
    private static final String KEY_SCORE = "score";
    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.answer_is_true";
    private static final int REQUEST_CODE_CHEAT = 0;

    private static Toast mToast;
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;

    private int mScore;

    private Question[] mQuestionBank = {new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
};

    private ArrayList<Integer> mCheatedQuestions = new ArrayList<>();
    private ArrayList<Integer> mAnsweredQuestions = new ArrayList<>();

    private int mCurrentQuestionIndex = 0;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate (Bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentQuestionIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mScore = savedInstanceState.getInt(KEY_SCORE);
            mAnsweredQuestions = savedInstanceState.getIntegerArrayList(KEY_ANS);
            mCheatedQuestions = savedInstanceState.getIntegerArrayList(KEY_CHEAT);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mCheatButton = findViewById(R.id.cheat_button);
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

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(MainActivity.newIntent(MainActivity.this,
                        mQuestionBank[mCurrentQuestionIndex].isAnswerTrue()), REQUEST_CODE_CHEAT);
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
//                    if (mToast == null) {
                        mToast = Toast.makeText(MainActivity.this, R.string.first_toast, Toast.LENGTH_SHORT);
//                        mToast.show();
//                    } else {
//                        mToast.setText(R.string.first_toast);
//                    }
                    mToast.setGravity(Gravity.TOP, 0, 0);
                    mToast.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHEAT && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (CheatActivity.wasAnswerShown(data)) {
                    mCheatedQuestions.add(mCurrentQuestionIndex);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentQuestionIndex);
        savedInstanceState.putIntegerArrayList(KEY_ANS, mAnsweredQuestions);
        savedInstanceState.putInt(KEY_SCORE, mScore);
        savedInstanceState.putIntegerArrayList(KEY_CHEAT, mCheatedQuestions);
    }

    @Override
    public void onStart () {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume () {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause () {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

        @Override
        public void onStop () {
            super.onStop();
            Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        mQuestionTextView.setText(mQuestionBank[mCurrentQuestionIndex].getTextResId());
        //if upcoming answer page is answered, hide the page
        if (mAnsweredQuestions.contains(mCurrentQuestionIndex)) {
            hideButton();
        } else {
            showButton();
        }
    }

    private void checkAnswer(boolean userAnswer) {
        boolean answerIsTrue = mQuestionBank[mCurrentQuestionIndex].isAnswerTrue();

        mAnsweredQuestions.add(mCurrentQuestionIndex);
        int messageResId;

        if (mCheatedQuestions.contains(mCurrentQuestionIndex)) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userAnswer == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mScore++;
            } else {
                messageResId = R.string.wrong_toast;
            }
        }
//        messageResId = (userAnswer == answerIsTrue ? R.string.correct_toast : R.string.wrong_toast);

//        if (mToast == null) {
            mToast = Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_SHORT);
//            mToast.show();
//        } else {
//            mToast.setText(messageResId);
//        }
        mToast.setGravity(Gravity.TOP, 0, 0);
        mToast.show();

        if (mAnsweredQuestions.size() == mQuestionBank.length) {
            calculateScore();
            updateQuestion();
        } else {
            //refresh current page and hide the button
            updateQuestion();
        }
    }

    private void calculateScore() {
        DecimalFormat df = new DecimalFormat("0.00%");
        String finalScore = df.format((double)mScore/mQuestionBank.length);
        CharSequence res = "You have correctly answered " + finalScore + "of questions!";
        System.out.println(res);
        System.out.println(finalScore);
        System.out.println(mScore);
//        if (mToast == null) {
            mToast = Toast.makeText(MainActivity.this, res, Toast.LENGTH_SHORT);
//            mToast.show();
//        } else {
//            mToast.setText(res);
//        }
        mToast.setGravity(Gravity.BOTTOM, 0, 0);
        mToast.show();
    }

    private void showButton() {
        mTrueButton.setVisibility(View.VISIBLE);
        mFalseButton.setVisibility(View.VISIBLE);
    }

    private void hideButton() {
        mTrueButton.setVisibility(View.INVISIBLE);
        mFalseButton.setVisibility(View.INVISIBLE);
    }
}

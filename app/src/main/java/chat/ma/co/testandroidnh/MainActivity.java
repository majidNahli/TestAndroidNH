package chat.ma.co.testandroidnh;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editNumber;
    private TextView txtResult, txtPercent;
    private LinearLayout lnResult;
    private ProgressBar progressBar;
    private CalculatePrimeTask asyncTask;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        initViews();

    }


    public void initViews(){

        editNumber  = (EditText) findViewById(R.id.edit_number);
        txtPercent  = (TextView) findViewById(R.id.txt_percent);
        txtResult   = (TextView) findViewById(R.id.txt_result);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        lnResult    = (LinearLayout) findViewById(R.id.ln_result);

        ((Button) findViewById(R.id.btn_calculate)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.img_cancel)).setOnClickListener(this);



    }
    // Get prime number

    public  List<Integer> primeFactors(int number) {
        int n = number;
        List<Integer> factors = new ArrayList<Integer>();
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {

                factors.add(i);
                n /= i;
            }
        }
        return factors;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_calculate :
                // calculate
                int number = Integer.parseInt(editNumber.getText().toString());
                if (number > 0 && number <= Math.pow(10, 20)){

                    asyncTask = new CalculatePrimeTask(number);
                    asyncTask.execute();

                } else
                    Toast.makeText(getApplicationContext(), "Please set number between 1 and 10^20", Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_cancel :
                // cancel operation
                asyncTask.cancel(true);
                break;

            default: break;
        }
    }


    private class CalculatePrimeTask extends AsyncTask<Void, Integer, List<Integer>> {

        private int number;

        public CalculatePrimeTask(int number) {
            this.number = number;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            editNumber.setText("");


        }

        @Override
        protected List<Integer> doInBackground(Void... params) {


                try {
                    //Get the current thread's token
                    synchronized (this) {
                        //Initialize an integer (that will act as a counter) to zero
                        int counter = 0;
                        //While the counter is smaller than four
                        while (counter <= 4) {
                            //Wait 850 milliseconds
                            this.wait(1000);
                            //Increment the counter
                            counter++;

                            //Set the current progress.
                            //This value is going to be passed to the onProgressUpdate() method.
                            publishProgress(counter * 20);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            return primeFactors(number);
        }

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values) {
            //set the current progress of the progress dialog
            txtPercent.setText(""+values[0]+"%");
            progressBar.setProgress(values[0]);
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(List<Integer> result) {
            //close the progress dialog
            String res = " ";
            lnResult.setVisibility(View.VISIBLE);
            if (result != null)
                for (int i =0;i<result.size() ; i++){
                    if (i>0)
                        res+="^"+result.get(i);
                    else
                        res+=""+result.get(i);
                }

            txtResult.setText(res);

        }

        @Override
        protected void onCancelled() {
            progressBar.setProgress(0);
            txtPercent.setText("");
        }
    }
}

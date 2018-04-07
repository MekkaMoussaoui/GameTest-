package dz.atelier.ntic.remplircases.game;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.view.View;

import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import dz.atelier.ntic.remplircases.game.model.RowData;
import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static android.widget.Toast.makeText;

public class GameMic extends Activity implements RecognitionListener {

    ////////////////////////////////////////////////////
    RatingBar ratingBar;
    int score;
    ImageView caption_image;


    //ArrayList<Cat> tab_image_grammar = new ArrayList<>();
    String grammar_sphinx;
    int image_sphinx;

    ////////////////////////////////////////////////////

    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";

    private static final String DIGITS_SEARCH = "digits";
    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "oh mighty computer";

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;

    @Override
    public void onCreate(Bundle state) {

        super.onCreate(state);
        /////////////////////////

        Bundle extras = getIntent().getExtras();

        grammar_sphinx = extras.getString("grammar");
        image_sphinx = extras.getInt("image");


        //////////////////////////

        // Prepare the data for UI
        captions = new HashMap<>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(DIGITS_SEARCH, R.string.digits_caption);
        setContentView(R.layout.activity_game_mic);

        ((TextView) findViewById(R.id.caption_text)).setText("Preparing the recognizer");

        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new SetupTask(this).execute();
    }


    private class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<GameMic> activityReference;
        SetupTask(GameMic activity) {
            this.activityReference = new WeakReference<>(activity);
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(activityReference.get());
                File assetDir = assets.syncAssets();
                activityReference.get().setupRecognizer(assetDir);
            } catch (IOException e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception result) {
            ///////////////////////////////////////////////
            caption_image = (ImageView)findViewById(R.id.caption_image);

            caption_image.setImageResource(image_sphinx);

            ///////////////////////////////////////////


            if (result != null) {
                ((TextView) activityReference.get().findViewById(R.id.caption_text))
                        .setText("Failed to init recognizer " + result);
            } else {

                activityReference.get().switchSearch(KWS_SEARCH);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recognizer initialization is a time-consuming and it involves IO,
                // so we execute it in async task
                new SetupTask(this).execute();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recognizer != null) {

            recognizer.cancel();
            recognizer.shutdown();
        }
    }


    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    public void DigitsClick(View view) {
        switchSearch(DIGITS_SEARCH);
    }



    @Override
    public void onPartialResult(Hypothesis hypothesis) {

        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        if (text.equals(KEYPHRASE))
            switchSearch(DIGITS_SEARCH);


        else {


            ((TextView) findViewById(R.id.result_text)).setText(text);
            score = hypothesis.getBestScore();
        }
    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {



        ((TextView) findViewById(R.id.result_text)).setText("");
        //////////////////////////////////////////////////



        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(0);

        ///////////////////////////////////////////////////
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            ratingBar.setRating(getScoreStars(score));

        }
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }


    private void switchSearch(final String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))

            recognizer.startListening(searchName);

        else
            recognizer.startListening(searchName, 10000);

        String caption = getResources().getString(captions.get(searchName));
        ((TextView) findViewById(R.id.caption_text)).setText(caption);
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)

                .getRecognizer();
        recognizer.addListener(this);

        /* In your application you might not need to add all those searches.
          They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);


        // Create grammar-based search for digit recognition
        File digitsGrammar = new File(assetsDir, grammar_sphinx);
        recognizer.addGrammarSearch(DIGITS_SEARCH, digitsGrammar);
    }

    @Override
    public void onError(Exception error) {
        ((TextView) findViewById(R.id.caption_text)).setText(error.getMessage());
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }


    ////////////////////////////////////////////////////
    public float getScoreStars(int getBestScore){
        if (getBestScore <= 0 && getBestScore > -1000)
            return 3.0f;
        else if (getBestScore <= -1000 && getBestScore > -2000)
            return 2.5f;
        else if (getBestScore <= -2000 && getBestScore > -3000)
            return 2;
        else if (getBestScore <= -3000 && getBestScore > -4000)
            return 1.5f;
        else if (getBestScore <= -4000 && getBestScore > -5000)
            return 1;
        else if (getBestScore <= -5000 && getBestScore > -6000)
            return 0.5f;
        else
            return 0;

    }
    ///////////////////////////////////////////////////
}
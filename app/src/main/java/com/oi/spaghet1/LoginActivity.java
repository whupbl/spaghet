package com.oi.spaghet1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mLoginView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Retrofit retrofit;
    private SpaghetAPI spaghetAPI;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mLoginView = (AutoCompleteTextView) findViewById(R.id.Login);
        populateAutoComplete();

        applyFont((TextView)findViewById(R.id.logoText), this);

        mPasswordView = (EditText) findViewById(R.id.password);

        retrofit = new Retrofit.Builder()
                .baseUrl(spaghetAPI.serverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        spaghetAPI = retrofit.create(SpaghetAPI.class);

        Button btn_clientSign = (Button) findViewById(R.id.login_sign_in_button_client);
        btn_clientSign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin("client");
            }
        });

        Button btn_cookSign = (Button) findViewById(R.id.login_sign_in_button_cook);
        btn_cookSign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin("cook");
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        // Передача параметров
        message = getIntent().getStringExtra("message");
        // Вывод сообщения на экран, если оно есть
        if (message != null) {
            Toast toast = Toast.makeText(this, message,Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0,200);
            toast.show();
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mLoginView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(String userType) {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mLoginView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_invalid_login));
            focusView = mLoginView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (userType.equals("client")) {
                final Call<UserList> authClient = spaghetAPI.authClient(login, password);
                final UserList users = new UserList();
                final User user = new User();
                authClient.enqueue(new Callback<UserList>() {
                    @Override
                    public void onResponse(Call<UserList> call, Response<UserList> response) {
                        if (response.isSuccessful()) {
                            users.setChildren(response.body().getChildren());
                            if (users.getChildren().size() == 0){
                                Intent intent = getIntent();
                                intent.putExtra("message", "Вы ввели неверный логин или пароль. Попробуйте снова.");
                                finish();
                                startActivity(intent);
                                return;
                            } else if (users.getChildren().get(0).getId() == "") {
                                Intent intent = getIntent();
                                intent.putExtra("message", "Вы зарегистрированы в системе, однако пытаетесь войти не в свой раздел");
                                finish();
                                startActivity(intent);
                                return;
                            }
                            user.setId(users.getChildren().get(0).getId());
                            user.setName(users.getChildren().get(0).getName());
                            Log.i("AUTH", user.getId());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("id", user.getId());
                            intent.putExtra("name", user.getName());
                            startActivity(intent);
                            return;
                        } else {
                            Intent intent = getIntent();
                            intent.putExtra("message", String.valueOf(response.code()));
                            finish();
                            startActivity(intent);
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<UserList> call, Throwable t) {
                        Intent intent = getIntent();
                        intent.putExtra("message", t.toString());
                        finish();
                        startActivity(intent);
                        return;
                    }
                });
            } else if (userType.equals("cook")) {
                final Call<UserList> authCook = spaghetAPI.authCook(login, password);
                final UserList users = new UserList();
                final User user = new User();
                authCook.enqueue(new Callback<UserList>() {
                    @Override
                    public void onResponse(Call<UserList> call, Response<UserList> response) {
                        if (response.isSuccessful()) {
                            users.setChildren(response.body().getChildren());
                            if (users.getChildren().size() == 0){
                                Intent intent = getIntent();
                                intent.putExtra("message", "Вы ввели неверный логин или пароль. Попробуйте снова.");
                                finish();
                                startActivity(intent);
                                return;
                            }
                            else if (users.getChildren().get(0).getId() == "") {
                                Intent intent = getIntent();
                                intent.putExtra("message", "Вы зарегистрированы в системе, однако пытаетесь войти не в свой раздел");
                                finish();
                                startActivity(intent);
                                return;
                            }
                            user.setId(users.getChildren().get(0).getId());
                            user.setName(users.getChildren().get(0).getName());
                            Log.i("AUTH", user.getId());
                            Intent intent = new Intent(LoginActivity.this, CookActivity.class);
                            intent.putExtra("id", user.getId());
                            intent.putExtra("name", user.getName());
                            startActivity(intent);
                            return;
                        } else {
                            Intent intent = getIntent();
                            intent.putExtra("message", String.valueOf(response.code()));
                            finish();
                            startActivity(intent);
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<UserList> call, Throwable t) {
                        Intent intent = getIntent();
                        intent.putExtra("message", t.toString());
                        finish();
                        startActivity(intent);
                        return;
                    }
                });
            }
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(login, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        /*List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mLoginView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String Login;
        private final String mPassword;

        UserLoginTask(String login, String password) {
            Login = login;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(Login)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }
    public static void applyFont(TextView tv, Activity context) {
        tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Playlist Script.otf"));
    }
}


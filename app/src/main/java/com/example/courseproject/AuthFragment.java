package com.example.courseproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.example.courseproject.albums.AlbumsFragment;

import java.net.ProtocolException;
import java.net.UnknownHostException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.courseproject.ApiUtils.getBasicAuthClient;

public class AuthFragment extends Fragment {
    private AutoCompleteTextView mEmail;
    private EditText mPassword;
    private Button mEnter;
    private Button mRegister;

    public static AuthFragment newInstance() {
        Bundle args = new Bundle();
        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_auth, container, false);

        mEmail = v.findViewById(R.id.etEmail);
        mPassword = v.findViewById(R.id.etPassword);
        mEnter = v.findViewById(R.id.buttonEnter);
        mRegister = v.findViewById(R.id.buttonRegister);

        mEnter.setOnClickListener(mOnEnterClickListener);
        mRegister.setOnClickListener(mOnRegisterClickListener);
        mEmail.setOnFocusChangeListener(mOnEmailFocusChangeListener);

        return v;
    }

    //------------------------------------AUTH BUTTON CLICK LISTENER--------------------------------//
    private View.OnClickListener mOnEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isEmailValid() && isPasswordValid()) {
                getBasicAuthClient(mEmail.getText().toString(), mPassword.getText().toString(), true);

            ApiUtils.getApiService(true).authorization()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            AlbumsFragment albumsFragment = AlbumsFragment.newInstance();
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.fragmentContainer, albumsFragment)
                                    .addToBackStack(AlbumsFragment.class.getSimpleName())
                                    .commit();
                        }, throwable -> {
                            if(throwable instanceof ProtocolException) {
                                showMessage(R.string.auth_error);
                            }
                            else if(throwable instanceof UnknownHostException) {
                                showMessage(R.string.network_error);
                            }
                            }
                        );
            } else {
                showMessage(R.string.input_error);
            }
        }
    };

    //----------------------------REGISTRATION BUTTON CLICK LISTENER--------------------------------//
    private View.OnClickListener mOnRegisterClickListener = view -> getFragmentManager()
            .beginTransaction()
            .replace(R.id.fragmentContainer, RegistrationFragment.newInstance())
            .addToBackStack(RegistrationFragment.class.getName())
            .commit();

    //----------------------------EMAIL FOCUS CHANGE LISTENER---------------------------------------//
    private View.OnFocusChangeListener mOnEmailFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                mEmail.showDropDown();
            }
        }
    };

    //---------------------------------------VALID INPUT DATA---------------------------------------//
    private boolean isEmailValid() {
        return !TextUtils.isEmpty(mEmail.getText())
                && Patterns.EMAIL_ADDRESS.matcher(mEmail.getText()).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(mPassword.getText());
    }
    //----------------------------------------------------------------------------------------------//

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

}

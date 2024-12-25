package com.hope.igb.savvy.screens.login.signup

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.hope.igb.savvy.R
import com.hope.igb.savvy.common.Constants
import com.hope.igb.savvy.common.views.BaseObservable
import com.hope.igb.savvy.databinding.FragmentLoginSignupBinding

class SignUpViewMvc(inflater: LayoutInflater) :BaseObservable<SignUpViewMvc.Listener>(),
    View.OnClickListener {


    interface Listener {
        fun onSignUpClickedWith(name: String, email: String, password: String)
        fun onSignUpAsAnonymousClicked()
        fun onSignInClicked()
        fun onLoginGoogleClicked()
    }


    private val binding: FragmentLoginSignupBinding
    private var passwordVisibility = false

    init {
        binding = FragmentLoginSignupBinding.inflate(inflater)
        initClickableViews()
    }

    private fun initClickableViews(){
        binding.signUp.setOnClickListener (this)
        binding.signupAnonymous.setOnClickListener(this)
        binding.loginGoogle.setOnClickListener(this)
        binding.signIn.setOnClickListener(this)

        binding.passwordVisibility.setOnClickListener(this)

    }





    override fun onClick(p0: View?) {
        when(p0?.id){
            binding.signUp.id -> onSignUpClicked()
            binding.signupAnonymous.id -> onSignUpAsAnonymousClicked()
            binding.loginGoogle.id -> onLoginGoogleClicked()
            binding.signIn.id -> onSignInClicked()
            binding.passwordVisibility.id -> onPasswordVisibilityChangeClicked()
        }
    }




    private fun onSignUpClicked() {
         if (!isEmailValid())
             Snackbar.make(binding.root, R.string.invalid_email, Snackbar.LENGTH_LONG).show()


        else if (password().length < Constants.MIN_PASSWORD_LENGTH )
             Snackbar.make(binding.root, R.string.invalid_password, Snackbar.LENGTH_LONG).show()


        else
            for (listener in getListeners())
                listener.onSignUpClickedWith(name(), email(), password())

    }

    private fun isEmailValid(): Boolean {
        return Patterns.EMAIL_ADDRESS
            .matcher(email()).matches()
    }

    private fun onSignUpAsAnonymousClicked() {
        for (listener in getListeners())
            listener.onSignUpAsAnonymousClicked()
    }


    private fun onLoginGoogleClicked() {
        for (listener in getListeners())
            listener.onLoginGoogleClicked()
    }


    private fun onSignInClicked() {
        for (listener in getListeners())
            listener.onSignInClicked()
    }


    private fun onPasswordVisibilityChangeClicked() {
        if (!passwordVisibility) {
            //show password
            binding.editPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()

            binding.passwordVisibility.setImageResource(R.drawable.l_login_password_visible_offd)
            passwordVisibility = true
        }else {

            //hide password
            binding.editPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.passwordVisibility.setImageResource(R.drawable.l_login_password_visible_ond)
            passwordVisibility = false

        }
    }


    private fun name(): String{
        return binding.editName.text.toString()
    }

    private fun email(): String{
        return binding.editEmail.text.toString()
    }


    private fun password(): String{
        return binding.editPassword.text.toString()
    }


}
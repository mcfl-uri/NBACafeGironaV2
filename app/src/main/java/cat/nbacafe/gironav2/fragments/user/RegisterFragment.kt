package cat.nbacafe.gironav2.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.nbacafe.gironav2.R
import cat.nbacafe.gironav2.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(
            inflater,
            R.layout.fragment_register, container, false
        )

        auth = FirebaseAuth.getInstance()

        binding.createUserBtn.setOnClickListener { View ->
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString())
                    .matches() && binding.passEditText.text.toString().length >= 8 && binding.passEditText.text.toString()
                    .equals(binding.confirmPassEditText.text.toString())
            ) {
                auth.createUserWithEmailAndPassword(
                    binding.emailEditText.text.toString(),
                    binding.passEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Usuari registrat", Toast.LENGTH_SHORT).show()
                        view?.findNavController()
                            ?.navigate(R.id.action_registerFragment_to_loginFragment)
                    } else {
                        Toast.makeText(context, "S'ha produït un error", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString())
                        .matches()
                ) {
                    Toast.makeText(context, "Email invàlid", Toast.LENGTH_SHORT).show()
                } else if (binding.passEditText.text.toString().length < 8) {
                    Toast.makeText(
                        context,
                        "La contrasenya ha de ser de més de 8 caràcters",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (binding.passEditText.text.toString() != binding.confirmPassEditText.text.toString()) {
                    Toast.makeText(context, "Les contrasenyes no coincideixen", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Revisa les dades", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.backToLoginBtn.setOnClickListener { View ->
            view?.findNavController()?.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return binding.root
    }
}